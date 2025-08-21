import qs from "qs";
import type { ChatEvent } from "../models/ChatActivity";
import type { Page, Pageable } from "../models/Paging";
import api from "./api";

interface ChatEventFilter {
    chatContentId: string;
    text?: string;
    from?: Date;
    to?: Date;
}

export const getChatEventById = async (
    id: string | null
): Promise<ChatEvent | null> => {
    if (!id)
        return null;
    const response = await api.get<ChatEvent>("/chat-event/" + id);
    return response.data;
};

export const getChatEventsWithFilter = async (
    filter: ChatEventFilter, pageable: Pageable
): Promise<Page<ChatEvent>> => {
    const params = {
        ...filter,
        page: pageable.page ?? 0,
        size: pageable.size ?? 20,
        sort: pageable.sort
    };

    const response = await api.get<Page<ChatEvent>>("/chat-event", {
        params,
        paramsSerializer: (p) =>
            qs.stringify(p, { arrayFormat: "repeat" }),
    });
    return response.data;
};