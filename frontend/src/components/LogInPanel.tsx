import { Button, Paper, PasswordInput, Text, TextInput, Title, Stack, Group, Anchor } from "@mantine/core";
import { IconCheck } from '@tabler/icons-react';
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { notifications } from '@mantine/notifications';
import type { AuthRequestDto, AuthResponseDto } from "../models/AuthDto";
import { logIn } from "../services/authService";
import { useAuthStore } from "../stores/authStore";
import { validateUsername, validatePassword } from "../validators/userAccountValidator";

type SignUpPanelProps = {
    w: string;
};

function LogInPanel({ w }: SignUpPanelProps) {
    const navigate = useNavigate();
    const { setToken } = useAuthStore();
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const [usernameError, setUsernameError] = useState<string | null>(null);
    const [passwordError, setPasswordError] = useState<string | null>(null);

    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const checkIcon = <IconCheck size={20} />;

    const onUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setUsername(val);
        setUsernameError(validateUsername(val));
    };

    const onPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setPassword(val);
        setPasswordError(validatePassword(val));
    };

    const inputIsValid =
        !usernameError &&
        !passwordError &&
        username &&
        password;

    const handleSubmit = async () => {
        setError(null);
        setLoading(true);
        try {
            const request: AuthRequestDto = {
                username, password
            };
            const response: AuthResponseDto = await logIn(request);
            setToken(response.token);
            notifications.show({
                title: 'Success!',
                message: 'You have logged in.',
                color: 'green',
                autoClose: 2000,
                icon: checkIcon,
                withCloseButton: false
            });
            navigate("/chats");
        } catch (e: unknown) {
            if (e instanceof Error) {
                setError(e);
            } else {
                setError(new Error("An unknown error occurred"));
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Paper
            shadow="sm"
            withBorder
            p="xl"
            style={{ width: w, maxWidth: 400, minWidth: 280 }}
            radius="md"
        >
            <Stack>
                <Title order={1} ta="center">Watermint</Title>
                <Text size="xl" fw={1000} ta="center" c="grey">
                    Log In
                </Text>
                <TextInput
                    placeholder="Username"
                    value={username}
                    onChange={onUsernameChange}
                    error={usernameError}
                />
                <PasswordInput
                    placeholder="Password"
                    value={password}
                    onChange={onPasswordChange}
                    error={passwordError}
                />
                <Button
                    fullWidth
                    disabled={!inputIsValid}
                    loading={loading}
                    onClick={handleSubmit}>
                    Log In
                </Button>
                {
                    error &&
                    <Text size="sm" fw={500} c="red">
                        {error?.message}
                    </Text>
                }
                <Group gap="xs">
                    <Text size="sm" c="grey">Don't have an account?</Text>
                    <Anchor size="sm" href="/signup" target="_self" underline="hover">
                        Sign Up
                    </Anchor>
                </Group>
            </Stack>
        </Paper>
    );
}

export default LogInPanel;