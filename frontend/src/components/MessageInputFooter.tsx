import { Paper, Textarea, ActionIcon, Flex, Divider, Text } from "@mantine/core";
import { IconExclamationCircle, IconSend, IconX } from "@tabler/icons-react";
import { useEffect, useState } from "react";
import { useUserAccountStore } from "../stores/userAccountStore";
import { createChatMessage, updateChatMessage } from "../services/chatMessageService";
import type { ChatMessage, ChatMessageRequestDto } from "../models/ChatActivity";
import type { ChatContent } from "../models/ChatContent";
import { notifications } from "@mantine/notifications";

interface MessageInputFooterProps {
    chatContent: ChatContent;
    editedMessage: ChatMessage | null;
    finishEditing: () => void;
}

function MessageInputFooter({ chatContent, editedMessage, finishEditing }: MessageInputFooterProps) {
    const { userAccount } = useUserAccountStore();

    const [text, setText] = useState(editedMessage?.text ?? "");
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        setText(editedMessage?.text ?? "");
    }, [editedMessage]);

    if (!userAccount)
        return <></>;

    const handleSend = async () => {
        setError(null);
        setLoading(true);
        try {
            const dto: ChatMessageRequestDto = {
                userAccountId: userAccount.id,
                chatContentId: chatContent.id,
                text: text
            };
            if (!editedMessage) {
                await createChatMessage(dto);
            } else {
                await updateChatMessage(editedMessage.id, dto);
                finishEditing();
            }
            setText("");
        } catch (e: unknown) {
            if (e instanceof Error) {
                setError(e);
            } else {
                setError(new Error("An unknown error occurred"));
            }
            notifications.show({
                title: 'Error!',
                message: error?.message,
                color: 'red',
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

            {editedMessage &&
                <>
                    <Flex align="center" gap="sm" p="sm" bg="ghostwhite">
                        <ActionIcon
                            onClick={finishEditing}
                            variant="outline"
                            radius="xl"
                            size="md"
                            color="gray"
                        >
                            <IconX size={18} />
                        </ActionIcon>
                        <Text c="dimmed" truncate="end" lineClamp={1}>
                            {editedMessage.text}
                        </Text>
                    </Flex>

                    <Divider />
                </>
            }

            <Flex align="flex-end" gap="sm" p="sm">
                <Textarea
                    value={text}
                    onChange={(e) => setText(e.currentTarget.value)}
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
                    disabled={!text}
                >
                    <IconSend size={18} />
                </ActionIcon>
            </Flex>
        </Paper>
    );
}

export default MessageInputFooter;
