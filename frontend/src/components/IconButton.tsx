import { Paper, Flex, Text } from "@mantine/core";
import React from "react";

interface IconButtonProps {
    icon: React.ReactNode;
    text: string;
    onClick?: () => void;
}

function IconButton({ icon, text, onClick }: IconButtonProps) {
    return (
        <Paper
            h={50}
            w="100%"
            radius="md"
            p="xs"
            style={{
                display: "flex",
                alignItems: "center",
                backgroundColor: "white",
                cursor: "pointer",
                transition: "background-color 0.15s ease"
            }}
            onClick={onClick}
            onMouseEnter={(e) => ((e.currentTarget.style.backgroundColor = "#f8f9fa"))}
            onMouseLeave={(e) => ((e.currentTarget.style.backgroundColor = "white"))}
        >
            <Flex gap="sm" justify="flex-start" align="center" direction="row">
                {icon}
                <Text size="20" lh={1}>{text}</Text>
            </Flex>
        </Paper>
    );
};

export default IconButton;