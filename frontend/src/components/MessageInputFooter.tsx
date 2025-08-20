import { Paper, Textarea, ActionIcon, Flex, Divider } from "@mantine/core";
import { IconExclamationCircle, IconSend } from "@tabler/icons-react";
import { useState } from "react";
import { useUserAccountStore } from "../stores/userAccountStore";
import { createChatMessage } from "../services/chatMessageService";
import type { ChatMessageRequestDto } from "../models/ChatActivity";
import type { ChatContent } from "../models/ChatContent";
import { notifications } from "@mantine/notifications";

interface MessageInputFooterProps {
    chatContent: ChatContent;
}

function MessageInputFooter({ chatContent }: MessageInputFooterProps) {
    const { userAccount } = useUserAccountStore();

    const [message, setMessage] = useState("");
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    if (!userAccount)
        return <></>;

    const handleSend = async () => {
        setError(null);
        setLoading(true);
        try {
            const dto: ChatMessageRequestDto = {
                userAccountId: userAccount.id,
                chatContentId: chatContent.id,
                text: message
            };
            await createChatMessage(dto);
            setMessage("");
        } catch (e: unknown) {
            if (e instanceof Error) {
                setError(e);
            } else {
                setError(new Error("An unknown error occurred"));
            }
            notifications.show({
                title: 'Error!',
                message: error?.message,
                color: 'green',
                autoClose: 2000,
                icon: <IconExclamationCircle size={20} />,
                withCloseButton: false
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <Paper
            w="100%"
            radius={0}
            style={{
                position: "sticky",
                bottom: 0,
                zIndex: 100,
                backgroundColor: "white",
            }}
        >
            <Divider />
            <Flex align="flex-end" gap="sm" p="sm">
                <Textarea
                    value={message}
                    onChange={(e) => setMessage(e.currentTarget.value)}
                    placeholder="Type a message..."
                    autosize
                    minRows={1}
                    maxRows={5}
                    style={{ flex: 1 }}
                    radius="xl"
                    variant="filled"
                    size="sm"
                />
                <ActionIcon
                    onClick={handleSend}
                    variant="filled"
                    radius="xl"
                    size="lg"
                    color="blue"
                    loading={loading}
                    disabled={!message}
                >
                    <IconSend size={18} />
                </ActionIcon>
            </Flex>
        </Paper>
    );
}

export default MessageInputFooter;
