import { Avatar, Stack, Group, Title, Paper, Divider } from "@mantine/core";
import { useUserAccountStore } from "../stores/userAccountStore";
import { useAuthStore } from "../stores/authStore";
import { base64ToMimeDataUrl } from "../utils/base64Utils";
import { IconUserCircle, IconSettings, IconUserSearch } from "@tabler/icons-react";
import IconButton from "../components/IconButton";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import ChatList from "../components/ChatList";
import AccountSettingsModal from "../components/AccountSettingsModal";

function ChatPage() {
    const navigate = useNavigate();
    const { userAccount } = useUserAccountStore();
    const { isAuthenticated, loadingAuth } = useAuthStore();

    // const [ searchedName, setSearchedName ] = useState<string>("");
    const [ profileModalOpen, setProfileModalOpen ] = useState<boolean>(false);

    useEffect(() => {
        if (!isAuthenticated && !loadingAuth) {
            navigate("/login");
        }
    }, [isAuthenticated, navigate, loadingAuth]);

    if(!userAccount)
        return <></>;

    return (
        <div
            style={{
                display: 'flex',
                height: '100vh',
            }}
        >
            <Paper w="25%" miw={250} withBorder>
                <Stack gap="0" p="md">
                    <Group
                        style={{
                            alignItems: "center"
                        }}
                        mb="md"
                    >
                        <Avatar src={base64ToMimeDataUrl(userAccount?.profilePicture)} radius="50%" size={80} />
                        <Title size="xl">{userAccount?.name}</Title>
                    </Group>

                    <IconButton text="My profile" icon={<IconUserCircle size={24}/>} onClick={() => setProfileModalOpen(true)}/>
                    <AccountSettingsModal opened={profileModalOpen} onClose={() => setProfileModalOpen(false)}/>

                    <IconButton text="Settings" icon={<IconSettings size={24} />} />

                    <IconButton text="Add a friend" icon={<IconUserSearch size={24} />} />
                </Stack>

                <Divider/>

                <ChatList searchedName={""}/>
            </Paper>

        </div>
    );
}

export default ChatPage;