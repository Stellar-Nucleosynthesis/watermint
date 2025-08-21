import { Paper, Flex, Title } from "@mantine/core";

function NoChatSelectedComponent() {
    return (
        <Paper w="75%" bg="aliceblue">
            <Flex justify="center" align="center" w="100%" h="100%">
                <Title order={1} c="dimmed">Select a chat to start communicating!</Title>
            </Flex>
        </Paper>
    );
}

export default NoChatSelectedComponent;