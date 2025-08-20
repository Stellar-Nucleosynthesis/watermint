import { Avatar, Divider, Group, Paper, Text, Flex } from "@mantine/core";
import { base64ToMimeDataUrl } from "../utils/base64Utils";

interface ChatHeaderProps {
    picture: string | undefined;
    name: string;
    onClick?: () => void;
}

function ChatHeader({ picture, name }: ChatHeaderProps) {
    return (
        <Paper
            h={75}
            w="100%"
            radius={0}
            style={{
                position: "sticky",
                top: 0,
                zIndex: 100,
                display: "flex",
                alignItems: "center",
                backgroundColor: "white",
                cursor: "pointer"
            }}
        >
            <Flex direction="column" h="100%" w="100%">
                <Group gap="sm" p="sm">
                    <Avatar src={base64ToMimeDataUrl(picture)} radius="50%" size={48} />
                    <Text size="md" fw={500} style={{ whiteSpace: "nowrap" }}>
                        {name}
                    </Text>
                </Group>
                <Divider />
            </Flex>
        </Paper>
    );
}

export default ChatHeader;