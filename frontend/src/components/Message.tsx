import { Avatar, Flex, Paper, Stack, Text, Space } from "@mantine/core";
import type { ChatMessage } from "../models/ChatActivity";
import { base64ToMimeDataUrl } from "../utils/base64Utils";
import { useUserAccountStore } from "../stores/userAccountStore";

interface MessageProps {
    message: ChatMessage;
    includeAvatar: boolean;
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

function Message({ message, includeAvatar }: MessageProps) {
    const { userAccount } = useUserAccountStore();
    if (!userAccount || !message)
        return <></>;
    const myMessage = userAccount.id == message.userAccount.id;
    return (
        <Flex justify="flex-start" align="flex-start" direction="row" gap="xs">
            {
                includeAvatar ?
                    <Avatar src={base64ToMimeDataUrl(message.userAccount.profilePicture)} radius="50%" size={36} />
                    :
                    <Space h={36} w={36} />
            }
            <Paper
                withBorder
                radius="md"
                style={{ backgroundColor: myMessage ? "cornflowerblue" : "white" }}
                mih={36}
                py={5}
                px={10}
            >
                <Stack align="stretch" justify="space-between" gap={5}>
                    <Text size="md">{message.text}</Text>
                    <Text size="xs" c={myMessage ? "powderblue" : "dimmed"} ta="right">
                        <i>{message.wasUpdated && "updated "}</i>
                        {formatDate(new Date(message.createTime))}
                    </Text>
                </Stack>
            </Paper>
        </Flex>
    );
}

export default Message;