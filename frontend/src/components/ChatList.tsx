import { Stack } from "@mantine/core";
import ChatPreviewElement from "./ChatPreviewElement";
import useFetch from "../hooks/useFetch";
import { getPrivateChatsWithFilter } from "../services/privateChatService";
import { useUserAccountStore } from "../stores/userAccountStore";
import { useMemo } from "react";
import ViewedPrivateChat from "./ViewedPrivateChat.tsx";

interface ChatListProps {
    searchedName: string;
    setChatDisplay: (component: React.ReactElement) => void;
}

function ChatList({ searchedName, setChatDisplay }: ChatListProps) {
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
                        onClick={() => setChatDisplay(<ViewedPrivateChat chat={chat} />)}
                    />
                )
            })}
        </Stack>
    );
}

export default ChatList;