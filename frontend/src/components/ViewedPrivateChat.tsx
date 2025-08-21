import { Flex } from "@mantine/core";
import type { PrivateChat } from "../models/PrivateChat";
import ChatHeader from "./ChatHeader";
import { useUserAccountStore } from "../stores/userAccountStore";
import MessageList from "./MessageList";

interface ViewedPrivateChatProps {
    chat: PrivateChat;
}

function ViewedPrivateChat({ chat }: ViewedPrivateChatProps) {
    const { userAccount } = useUserAccountStore();
    if(!userAccount)
        return <></>;
    const otherAccount = userAccount.id == chat.userAccount1.id ? chat.userAccount2 : chat.userAccount1
    if (!chat)
        return <></>;
    return (
        <Flex w="75%" h="100vh" direction="column">
            <ChatHeader picture={otherAccount.profilePicture} name={otherAccount.name}/>
            <MessageList chatContent={chat.chatContent}/>
        </Flex>
    );
}

export default ViewedPrivateChat;