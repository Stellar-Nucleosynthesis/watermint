import { Paper, Text } from "@mantine/core";

interface ChatEventComponentProps {
    text: string;
}

function ChatEventComponent({ text }: ChatEventComponentProps) {
    return (
        <Paper
            radius="xl"
            bg="lightsteelblue"
            mx="auto"
            py={5}
            px={10}
            style={{ display: "inline-block", width: "fit-content" }}
        >
            <Text c="white" ta="center">{text}</Text>
        </Paper>
    );
}

export default ChatEventComponent;