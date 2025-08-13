import { Button, Paper, PasswordInput, Text, TextInput, Title, Stack, Checkbox, Group, Anchor } from "@mantine/core";
import { IconAt, IconCheck } from '@tabler/icons-react';
import { useState } from "react";
import type { UserAccountRequestDto } from "../models/UserAccount";
import { createUserAccount } from "../services/userAccountService";
import { useNavigate } from "react-router-dom";
import { notifications } from '@mantine/notifications';
import { validateEmail, validateName, validateUsername, 
    validatePassword, validatePasswordConfirm, validateAgreement } from "../validators/userAccountValidator";

type SignUpPanelProps = {
    w: string;
};

function SignUpPanel({ w }: SignUpPanelProps) {
    const navigate = useNavigate();
    const [email, setEmail] = useState<string>("");
    const [name, setName] = useState<string>("");
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [passwordConfirm, setPasswordConfirm] = useState<string>("");
    const [hasAgreed, setHasAgreed] = useState<boolean>(true);

    const [emailError, setEmailError] = useState<string | null>(null);
    const [nameError, setNameError] = useState<string | null>(null);
    const [usernameError, setUsernameError] = useState<string | null>(null);
    const [passwordError, setPasswordError] = useState<string | null>(null);
    const [passwordConfirmError, setPasswordConfirmError] = useState<string | null>(null);
    const [agreementError, setAgreementError] = useState<string | null>(null);

    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const emailIcon = <IconAt size={16} />;
    const checkIcon = <IconCheck size={20} />;

    const onEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setEmail(val);
        setEmailError(validateEmail(val));
    };

    const onNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setName(val);
        setNameError(validateName(val));
    };

    const onUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setUsername(val);
        setUsernameError(validateUsername(val));
    };

    const onPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setPassword(val);
        setPasswordError(validatePassword(val));
        setPasswordConfirmError(validatePasswordConfirm(passwordConfirm, val));
    };

    const onPasswordConfirmChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setPasswordConfirm(val);
        setPasswordConfirmError(validatePasswordConfirm(val, password));
    };

    const onAgreementChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const checked = e.currentTarget.checked;
        setHasAgreed(checked);
        setAgreementError(validateAgreement(checked));
    };

    const inputIsValid =
        !emailError &&
        !nameError &&
        !usernameError &&
        !passwordError &&
        !passwordConfirmError &&
        !agreementError &&
        email &&
        name &&
        username &&
        password &&
        passwordConfirm &&
        hasAgreed;


    const handleSubmit = async () => {
        setError(null);
        setLoading(true);
        try {
            const request: UserAccountRequestDto = {
                email, username, name, password
            };
            await createUserAccount(request);
            notifications.show({
                title: 'Success!',
                message: 'Your account has been created.',
                color: 'green',
                autoClose: 2000,
                icon: checkIcon,
                withCloseButton: false
            });
            navigate("/login");
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
        >
            <Stack>
                <Title order={1} ta="center">Watermint</Title>
                <Text size="xl" fw={1000} ta="center" c="grey">
                    Sign Up
                </Text>
                <TextInput
                    placeholder="E-mail"
                    leftSection={emailIcon}
                    value={email}
                    onChange={onEmailChange}
                    error={emailError}
                />
                <TextInput
                    placeholder="Name"
                    value={name}
                    onChange={onNameChange}
                    error={nameError}
                />
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
                <PasswordInput
                    placeholder="Confirm password"
                    value={passwordConfirm}
                    onChange={onPasswordConfirmChange}
                    error={passwordConfirmError}
                />
                <Checkbox
                    label="I agree to the terms and conditions as set out by the user agreement."
                    c="grey"
                    checked={hasAgreed}
                    onChange={onAgreementChange}
                    error={agreementError}
                />
                <Button
                    fullWidth
                    disabled={!inputIsValid}
                    loading={loading}
                    onClick={handleSubmit}>
                    Sign up
                </Button>
                {
                    error &&
                    <Text size="sm" fw={500} c="red">
                        {error?.message}
                    </Text>
                }
                <Group gap="xs">
                    <Text size="sm" c="grey">Have an account?</Text>
                    <Anchor size="sm" href="/login" target="_self" underline="hover">
                        Log In
                    </Anchor>
                </Group>
            </Stack>
        </Paper>
    );
}

export default SignUpPanel;