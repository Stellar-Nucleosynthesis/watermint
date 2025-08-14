import { Stack } from "@mantine/core";
import ChatPreviewElement, { type ChatPreviewProps } from "./ChatPreviewElement";
import useFetch from "../hooks/useFetch";
import { getPrivateChatsWithFilter } from "../services/privateChatService";
import { useUserAccountStore } from "../stores/userAccountStore";
import type { PrivateChat } from "../models/PrivateChat.ts";
import { useMemo } from "react";

interface ChatListProps {
    searchedName: string;
}

function ChatList({ searchedName }: ChatListProps) {
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

    const chatProps: ChatPreviewProps[] = chats.map((chat: PrivateChat) => {
        const id1 = chat.userAccount1.id;
        const anotherAccount = userAccount.id == id1 ? chat.userAccount2 : chat.userAccount1;
        return { name: anotherAccount.name, picture: anotherAccount.profilePicture };
    });

    return (
        <Stack gap="xs">
            {chatProps.map((props) => {
                return (
                    <ChatPreviewElement
                        key={props.name}
                        picture={props.picture}
                        name={props.name}
                    />
                )
            })}
        </Stack>
    );
}

export default ChatList;