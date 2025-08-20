import type { ChatMessage, ChatMessageRequestDto } from "../models/ChatActivity";
import type { Page, Pageable } from "../models/Paging";
import api from "./api";

interface ChatMessageFilter {
    chatContentId: string;
    userAccountId?: string;
    text?: string;
    from?: Date;
    to?: Date;
}

export const getChatMessageById = async (
    id: string | null
): Promise<ChatMessage | null> => {
    if (!id)
        return null;
    const response = await api.get<ChatMessage>("/chat-message/" + id);
    return response.data;
};

export const getChatMessagesWithFilter = async (
    filter: ChatMessageFilter, pageable: Pageable
): Promise<Page<ChatMessage>> => {
    const params = {
        ...filter,
        page: pageable.page ?? 0,
        size: pageable.size ?? 20,
        sort: pageable.sort
    };

    const response = await api.get<Page<ChatMessage>>("/chat-message", { params });
    return response.data;
};

export const createChatMessage = async (
    request: ChatMessageRequestDto
): Promise<ChatMessage> => {
    const response = await api.post<ChatMessage>("/chat-message", request);
    return response.data;
};

export const updateChatMessage = async (
    id: string, 
    dto: ChatMessageRequestDto
): Promise<ChatMessage> => {
    const response = await api.patch<ChatMessage>("/chat-message/" + id, dto);
    return response.data;
}

export const deleteChatMessageById = async (
    id: string | null
): Promise<void> => {
    if (!id)
        return;
    await api.delete<ChatMessage>("/chat-message/" + id);
    return;
};