import { Stack } from "@mantine/core";
import ChatPreviewElement from "./ChatPreviewElement";
import useFetch from "../hooks/useFetch";
import { getPrivateChatsWithFilter } from "../services/privateChatService";
import { useUserAccountStore } from "../stores/userAccountStore";
import { useMemo } from "react";
import { useNavigate } from "react-router-dom";

interface ChatListProps {
    searchedName: string;
}

function ChatList({ searchedName }: ChatListProps) {
    const navigate = useNavigate();
    const { userAccount } = useUserAccountStore();
    const filter = useMemo(() => ({
        userAccount1Id: userAccount?.id,
        userAccount2Name: searchedName
    }), [userAccount?.id, searchedName]);
    const { data: chats } = useFetch(getPrivateChatsWithFilter, [filter]);

    if (!userAccount)
        return <></>;

    if (chats == null)
        return <></>;

    return (
        <Stack gap={0}>
            {chats.map((chat) => {
                const id1 = chat.userAccount1.id;
                const anotherAccount = userAccount.id == id1 ? chat.userAccount2 : chat.userAccount1;
                return (
                    <ChatPreviewElement
                        key={anotherAccount.name}
                        picture={anotherAccount.profilePicture}
                        name={anotherAccount.name}
                        onClick={() => navigate(`/chats/private-chat/${chat.id}`)}
                    />
                )
            })}
        </Stack>
    );
}

export default ChatList;