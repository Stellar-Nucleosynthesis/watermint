import type { ChatContent } from "./ChatContent"
import type { UserAccount } from "./UserAccount"

export interface ChatItem {
    createTime: Date;
    text: string;
}

export interface ChatEvent extends ChatItem {
    id: string;
    createTime: Date;
    text: string;
    chatContent: ChatContent;
}

export interface ChatMessage extends ChatItem {
    id: string;
    createTime: Date;
    updateTime: Date;
    wasUpdated: boolean;
    text: string;
    userAccount: UserAccount;
    chatContent: ChatContent;
}

export interface ChatMessageRequestDto {
    text?: string;
    userAccountId?: string;
    chatContentId?: string;
}
