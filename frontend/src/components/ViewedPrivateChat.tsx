import { Flex } from "@mantine/core";
import ChatHeader from "./ChatHeader";
import { useUserAccountStore } from "../stores/userAccountStore";
import MessageList from "./MessageList";
import { useParams } from "react-router-dom";
import { getPrivateChatById } from "../services/privateChatService";
import useFetch from "../hooks/useFetch";

function ViewedPrivateChat() {
    const { chatId } = useParams<{ chatId: string }>();
    const { data: chat } = useFetch(getPrivateChatById, [chatId ?? null]);
    const { userAccount } = useUserAccountStore();

    if(!chat || !userAccount)
        return <></>;

    const otherAccount = userAccount.id == chat.userAccount1.id ? chat.userAccount2 : chat.userAccount1
    if (!chat)
        return <></>;
    return (
        <Flex w="75%" h="100vh" direction="column">
            <ChatHeader picture={otherAccount.profilePicture} name={otherAccount.name} />
            <MessageList chatContent={chat.chatContent} />
        </Flex>
    );
}

export default ViewedPrivateChat;