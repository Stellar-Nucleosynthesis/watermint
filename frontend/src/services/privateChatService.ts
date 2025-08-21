import type { PrivateChat, PrivateChatRequestDto } from "../models/PrivateChat";
import api from "./api";

interface PrivateChatFilter {
    userAccount1Id?: string;
    userAccount2Name?: string;
}

export const getPrivateChatsWithFilter = async (
    filter: PrivateChatFilter
): Promise<PrivateChat[]> => {
    if (!filter.userAccount1Id)
        return [];
    const response = await api.get<PrivateChat[]>("/private-chat", { params: filter });
    return response.data;
};

export const getPrivateChatById = async (
    id: string | null
): Promise<PrivateChat | null> => {
    if (!id)
        return null;
    const response = await api.get<PrivateChat>("/private-chat/" + id);
    return response.data;
};

export const createPrivateChat = async (
    request: PrivateChatRequestDto
): Promise<PrivateChat> => {
    const response = await api.post<PrivateChat>("/private-chat", request);
    return response.data;
};