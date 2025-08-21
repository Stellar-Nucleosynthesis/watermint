import { Avatar, Flex, Paper, Stack, Text, Space, Menu } from "@mantine/core";
import type { ChatMessage } from "../models/ChatActivity";
import { base64ToMimeDataUrl } from "../utils/base64Utils";
import { useUserAccountStore } from "../stores/userAccountStore";
import { useState } from "react";
import { IconCopy, IconEdit, IconExclamationCircle, IconTrash } from "@tabler/icons-react";
import { deleteChatMessageById } from "../services/chatMessageService";
import { notifications } from "@mantine/notifications";

interface MessageProps {
    message: ChatMessage;
    includeAvatar: boolean;
    editMessage: () => void;
}

function formatDate(date: Date): string {
    return date.toLocaleString("en-US", {
        day: "2-digit",
        month: "long",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    }).replace(",", "");
}

function Message({ message, includeAvatar, editMessage }: MessageProps) {
    const { userAccount } = useUserAccountStore();
    const [menuOpened, setMenuOpened] = useState(false);

    if (!userAccount || !message)
        return <></>;
    const myMessage = userAccount.id == message.userAccount.id;

    const handleContextMenu = (e: React.MouseEvent) => {
        e.preventDefault();
        setMenuOpened(true);
    };

    const handleClose = () => setMenuOpened(false);

    const handleCopy = () => {
        navigator.clipboard.writeText(message.text);
        handleClose();
    };

    const handleEdit = () => {
        editMessage();
        handleClose();
    };

    const handleDelete = () => {
        try {
            deleteChatMessageById(message.id);
        } catch (e: unknown) {
            notifications.show({
                title: 'Error!',
                message: 'Could not delete the message',
                color: 'red',
                autoClose: 2000,
                icon: <IconExclamationCircle size={20} />,
                withCloseButton: false
            });
        }
        handleClose();
    };

    return (
        <Flex justify="flex-start" align="flex-start" direction="row" gap="xs">
            {
                includeAvatar ?
                    <Avatar src={base64ToMimeDataUrl(message.userAccount.profilePicture)} radius="50%" size={36} />
                    :
                    <Space h={36} w={36} />
            }

            <Menu
                opened={menuOpened}
                onClose={handleClose}
                withinPortal
                closeOnClickOutside
                transitionProps={{ transition: 'pop', duration: 150 }}
                offset={-20}
                position="top"
            >
                <Menu.Target>
                    <Paper
                        withBorder
                        radius="md"
                        style={{ backgroundColor: myMessage ? "cornflowerblue" : "white", cursor: "pointer" }}
                        mih={36}
                        py={5}
                        px={10}
                        onContextMenu={handleContextMenu}
                    >
                        <Stack align="stretch" justify="space-between" gap={5}>
                            <Text size="md">{message.text}</Text>
                            <Text size="xs" c={myMessage ? "powderblue" : "dimmed"} ta="right">
                                <i>{message.wasUpdated && "edited "}</i>
                                {formatDate(new Date(message.createTime))}
                            </Text>
                        </Stack>
                    </Paper>
                </Menu.Target>
                <Menu.Dropdown>
                    <Menu.Item leftSection={<IconCopy size={14} />} onClick={handleCopy}>
                        Copy
                    </Menu.Item>
                    {myMessage && (
                        <>
                            <Menu.Item leftSection={<IconEdit size={14} />} onClick={handleEdit}>
                                Edit
                            </Menu.Item>
                            <Menu.Item
                                leftSection={<IconTrash size={14} />}
                                color="red"
                                onClick={handleDelete}
                            >
                                Delete
                            </Menu.Item>
                        </>
                    )}
                </Menu.Dropdown>
            </Menu>
        </Flex>
    );
}

export default Message;