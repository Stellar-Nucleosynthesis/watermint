import type { ChatContent } from "./ChatContent";
import type { UserAccount } from "./UserAccount";

export interface PrivateChat {
    id: string;
    createTime: Date;
    userAccount1: UserAccount;
    userAccount2: UserAccount;
    chatContent: ChatContent;
}

export interface PrivateChatRequestDto {
    userAccount1Id: string;
    userAccount2Id: string;
}