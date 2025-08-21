import type { ChatEvent, ChatMessage } from "./ChatActivity";

export interface ChatEventNotification{
    action: string;
    event: ChatEvent;
}

export interface ChatMessageNotification{
    action: string;
    message: ChatMessage;
}