import { Avatar, Group, Paper, Text } from "@mantine/core";
import { base64ToMimeDataUrl } from "../utils/base64Utils";

export interface ChatPreviewProps {
    picture: string | undefined;
    name: string;
}

function ChatPreviewElement({ picture, name }: ChatPreviewProps) {
    return (
        <Paper
            h={75}
            w="100%"
            radius={0}
            p="sm"
            withBorder
            style={{
                display: "flex",
                alignItems: "center",
                backgroundColor: "white",
                cursor: "pointer",
                transition: "background-color 0.15s ease",
            }}
            onMouseEnter={(e) =>
                ((e.currentTarget.style.backgroundColor = "#f8f9fa"))
            }
            onMouseLeave={(e) =>
                ((e.currentTarget.style.backgroundColor = "white"))
            }
        >
            <Group gap="sm">
                <Avatar src={base64ToMimeDataUrl(picture)} radius="50%" size={48} />
                <Text size="md" fw={500} style={{ whiteSpace: "nowrap" }}>
                    {name}
                </Text>
            </Group>
        </Paper>
    );
}

export default ChatPreviewElement;