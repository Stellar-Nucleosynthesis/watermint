import { Button, Title, TextInput, Stack, Paper, Modal, Loader, Text, Group, Avatar } from "@mantine/core";
import { IconCheck } from '@tabler/icons-react';
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../stores/authStore";
import { useUserAccountStore } from "../stores/userAccountStore";
import { validateUsername } from "../validators/userAccountValidator";
import { getUserAccountByUsername } from "../services/userAccountService";
import useFetch from "../hooks/useFetch";
import type { PrivateChatRequestDto } from "../models/PrivateChat";
import { createPrivateChat } from "../services/privateChatService";
import { notifications } from "@mantine/notifications";
import { base64ToMimeDataUrl } from "../utils/base64Utils";

interface AccountSettingsModalProps {
    opened: boolean,
    onClose: (param: void) => void,
    w?: string
}

function AddFriendModal({ opened, onClose, w = "40vw" }: AccountSettingsModalProps) {
    const navigate = useNavigate();
    const { isAuthenticated, loadingAuth } = useAuthStore();
    const { userAccount } = useUserAccountStore();

    const [username, setUsername] = useState<string>("");
    const [usernameError, setUsernameError] = useState<string | null>(null);
    const { data: selectedUser, loading: loadingSearch } = useFetch(getUserAccountByUsername, [username]);

    const [chatCreationError, setChatCreationError] = useState<Error | null>(null);
    const [loadingChat, setLoadingChat] = useState<boolean>(false);

    const onUsernameChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value;
        setUsername(val);
        setChatCreationError(null);
        setUsernameError(validateUsername(val));
    };

    useEffect(() => {
        if (!isAuthenticated && !loadingAuth) {
            navigate("/login");
        }
    }, [isAuthenticated, navigate, loadingAuth]);

    if (!userAccount)
        return <></>;

    const handleSubmit = async () => {
        if (!selectedUser)
            return;
        setChatCreationError(null);
        setLoadingChat(true);
        try {
            const request: PrivateChatRequestDto = {
                userAccount1Id: userAccount.id,
                userAccount2Id: selectedUser.id
            };
            await createPrivateChat(request);
            notifications.show({
                title: 'Success!',
                message: 'A chat with the user has been created.',
                color: 'green',
                autoClose: 2000,
                icon: <IconCheck size={20} />,
                withCloseButton: false
            });
            onClose();
        } catch (e: unknown) {
            if (e instanceof Error) {
                setChatCreationError(e);
            } else {
                setChatCreationError(new Error("An unknown error occurred"));
            }
        } finally {
            setLoadingChat(false);
        }
    };

    const validAccountSelected = selectedUser && selectedUser.id != userAccount.id;

    return (
        <Modal
            opened={opened}
            onClose={onClose}
            shadow="sm"
            title={<Title order={3} c="grey">Find by username</Title>}
            size="auto"
        >
            <Stack
                w={w}
                maw={400}
                miw={280}
                p="md"
            >
                <TextInput
                    placeholder="Username"
                    value={username}
                    onChange={onUsernameChange}
                    error={usernameError}
                    rightSection={loadingSearch && <Loader color="blue" size="sm" />}
                />
                <Paper h={75} w="100%" radius="md" p="sm" withBorder style={{
                    display: "flex",
                    justifyContent: validAccountSelected ? "flex-start" : "center",
                    alignItems: "center",
                }}>
                    {
                        validAccountSelected ?
                            <Group gap="sm">
                                <Avatar src={base64ToMimeDataUrl(selectedUser.profilePicture)} radius="50%" size={48} />
                                <Text size="md" fw={500} style={{ whiteSpace: "nowrap" }}>
                                    {selectedUser.name}
                                </Text>
                            </Group>
                            :
                            <Title order={4} c="dimmed">No user found</Title>
                    }
                </Paper>

                <Button
                    variant="filled"
                    w={150}
                    ml="auto"
                    disabled={!validAccountSelected}
                    loading={loadingChat}
                    onClick={handleSubmit}
                >
                    Add a friend
                </Button>
                {
                    chatCreationError &&
                    <Text size="sm" fw={500} c="red">
                        {chatCreationError.message}
                    </Text>
                }
            </Stack>
        </Modal>
    );
}

export default AddFriendModal;