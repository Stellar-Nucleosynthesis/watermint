import { Avatar, Group, Paper, Text } from "@mantine/core";

export interface ChatPreviewProps {
    picture: string | undefined;
    name: string;
}

function ChatPreviewElement({ picture, name }: ChatPreviewProps) {
    return (
        <Paper
            h={50}
            w="100%"
            radius="md"
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
                <Avatar src={picture} radius="50%" size={32} />
                <Text size="sm" fw={500} style={{ whiteSpace: "nowrap" }}>
                    {name}
                </Text>
            </Group>
        </Paper>
    );
}

export default ChatPreviewElement;