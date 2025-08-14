import { Button, Text, Title, TextInput, Stack, Group, Modal, Divider } from "@mantine/core";
import { DateInput } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import type { UserAccountRequestDto } from "../models/UserAccount";
import { IconAt, IconCheck } from '@tabler/icons-react';
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../stores/authStore";
import { useUserAccountStore } from "../stores/userAccountStore";
import { validateEmail, validateName } from "../validators/userAccountValidator";
import { updateUserAccount } from "../services/userAccountService";
import EditableAvatar from "./EditableAvatar";

interface AccountSettingsModalProps {
    opened: boolean,
    onClose: (param: void) => void,
    w?: string
}

function AccountSettingsModal({ opened, onClose, w="40vw" }: AccountSettingsModalProps) {
    const navigate = useNavigate();
    const { isAuthenticated, loadingAuth } = useAuthStore();
    const { userAccount, fetchByUsername } = useUserAccountStore();

    const [email, setEmail] = useState<string>(userAccount?.email ?? "");
    const [name, setName] = useState<string>(userAccount?.name ?? "");
    const [profilePicture, setProfilePicture] = useState<string | undefined>(userAccount?.profilePicture);
    const [birthDate, setBirthDate] = useState<Date | undefined>(userAccount?.birthDate);

    const [emailError, setEmailError] = useState<string | null>(null);
    const [nameError, setNameError] = useState<string | null>(null);

    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    const emailIcon = <IconAt size={16} />;
    const checkIcon = <IconCheck size={20} />;

    useEffect(() => {
        if (userAccount) {
            setEmail(userAccount.email ?? "");
            setName(userAccount.name ?? "");
            setProfilePicture(userAccount.profilePicture);
            setBirthDate(userAccount.birthDate);
        }
    }, [userAccount]);

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

    const onBirthDateChange = (value: string | null) => {
        if (!value)
            setBirthDate(undefined);
        else
            setBirthDate(new Date(value));
    };

    const inputIsValid =
        !emailError &&
        !nameError &&
        email &&
        name;

    useEffect(() => {
        if (!isAuthenticated && !loadingAuth) {
            navigate("/login");
        }
    }, [isAuthenticated, navigate, loadingAuth]);

    if (!userAccount)
        return <></>;

    const handleSubmit = async () => {
        setError(null);
        setLoading(true);
        try {
            const request: UserAccountRequestDto = {
                email,
                name,
                profilePicture,
                birthDate
            };
            await updateUserAccount(userAccount.id, request);
            fetchByUsername(userAccount.username);
            notifications.show({
                title: 'Success!',
                message: 'Account information has been updated.',
                color: 'green',
                autoClose: 2000,
                icon: checkIcon,
                withCloseButton: false
            });
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

    const handleClose = () => {
        setEmail(userAccount.email);
        setName(userAccount.name);
        setProfilePicture(userAccount.profilePicture);
        setBirthDate(userAccount.birthDate);
        setEmailError(null);
        setNameError(null);
        setError(null);
        setLoading(false);
        onClose();
    }

    return (
        <Modal
            opened={opened}
            onClose={handleClose}
            shadow="sm"
            title={<Title c="grey">My profile</Title>}
            size="auto"
        >
            <Stack
                w={w}
                maw={400}
                miw={280}
                p="md"
            >
                <Group display="flex" align="flex-start" gap="md">
                    <EditableAvatar picture={profilePicture} setPicture={setProfilePicture} />
                    <Stack gap="sm">
                        <TextInput
                            size="md"
                            label="Name"
                            placeholder="Name"
                            value={name}
                            onChange={onNameChange}
                            error={nameError}
                        />
                        <Text size="sm" c="grey">
                            Username: {userAccount.username}
                        </Text>
                    </Stack>
                </Group>

                <Divider />

                <TextInput
                    label="E-mail"
                    placeholder="E-mail"
                    leftSection={emailIcon}
                    value={email}
                    onChange={onEmailChange}
                    error={emailError}
                />

                <DateInput
                    value={birthDate}
                    onChange={onBirthDateChange}
                    label="Birth date"
                    placeholder="Birh date"
                />

                <Button
                    fullWidth
                    disabled={!inputIsValid}
                    loading={loading}
                    onClick={handleSubmit}>
                    Save changes
                </Button>
                {
                    error &&
                    <Text size="sm" fw={500} c="red">
                        {error?.message}
                    </Text>
                }
            </Stack>
        </Modal>
    );
}

export default AccountSettingsModal;