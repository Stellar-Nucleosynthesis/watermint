import { useEffect, useRef, useState, useCallback, useMemo } from "react";
import { Paper, Stack, Loader } from "@mantine/core";
import type { ChatContent } from "../models/ChatContent";
import type { ChatMessage } from "../models/ChatActivity";
import type { ChatEvent } from "../models/ChatActivity";
import { getChatMessagesWithFilter } from "../services/chatMessageService";
import { getChatEventsWithFilter } from "../services/chatEventService";
import Message from "./Message";
import ChatEventComponent from "./ChatEvent";
import type { Pageable } from "../models/Paging";
import useFetch from "../hooks/useFetch";
import MessageInputFooter from "./MessageInputFooter";
import useChatMessageNotification from "../hooks/useChatMessageNotification";
import useChatEventNotification from "../hooks/useChatEventNotification";

interface MessageListProps {
    chatContent: ChatContent;
}

type ChatTimelineItem =
    | ({ type: "message" } & ChatMessage)
    | ({ type: "event" } & ChatEvent);

async function fetchTimelinePage(
    chatContentId: string,
    page: number
): Promise<ChatTimelineItem[]> {
    const pageable: Pageable = {
        page,
        size: 100,
        sort: ["createTime,desc"]
    };

    const [msgPage, evtPage] = await Promise.all([
        getChatMessagesWithFilter({ chatContentId }, pageable),
        getChatEventsWithFilter({ chatContentId }, pageable),
    ]);

    const merged: ChatTimelineItem[] = [
        ...msgPage.content.map((m) => ({ ...m, type: "message" as const })),
        ...evtPage.content.map((e) => ({ ...e, type: "event" as const })),
    ];

    merged.sort(
        (a, b) =>
            new Date(a.createTime).getTime() - new Date(b.createTime).getTime()
    );

    return merged;
}

function getItemPosition(items: ChatTimelineItem[], id: string, date: Date): number | null {
    if (!items || items.length === 0) return null;
    let left = 0;
    let right = items.length - 1;
    const targetTime = date.getTime();
    while (left <= right) {
        const mid = Math.floor((left + right) / 2);
        const midTime = new Date(items[mid].createTime).getTime();
        if (midTime < targetTime) {
            left = mid + 1;
        } else if (midTime > targetTime) {
            right = mid - 1;
        } else {
            let i = mid;
            while (i >= 0 && new Date(items[i].createTime).getTime() === targetTime) {
                if (items[i].id === id) return i;
                i--;
            }
            i = mid + 1;
            while (i < items.length && new Date(items[i].createTime).getTime() === targetTime) {
                if (items[i].id === id) return i;
                i++;
            }
            return null;
        }
    }
    return null;
}

function MessageList({ chatContent }: MessageListProps) {
    const [page, setPage] = useState(0);
    const [items, setItems] = useState<ChatTimelineItem[]>([]);
    const [hasMore, setHasMore] = useState(true);
    const [editedMessage, setEditedMessage] = useState<ChatMessage | null>(null);
    const containerRef = useRef<HTMLDivElement>(null);

    const { lastNotification: lastMessageNotification } = useChatMessageNotification(chatContent.id);
    const { lastNotification: lastEventNotification } = useChatEventNotification(chatContent.id);

    const autoScrollRef = useRef<boolean>(true);
    const AUTO_SCROLL_THRESHOLD = 100;

    const handleScroll = useCallback(() => {
        const el = containerRef.current;
        if (!el) return;

        const distanceFromBottom = el.scrollHeight - el.scrollTop - el.clientHeight;
        autoScrollRef.current = distanceFromBottom <= AUTO_SCROLL_THRESHOLD;

        if (!el || el.scrollTop > 0) return;
        if (hasMore) {
            const prevHeight = el.scrollHeight;
            setPage((p) => p + 1);
            requestAnimationFrame(() => {
                const newHeight = el.scrollHeight;
                el.scrollTop = newHeight - prevHeight;
            });
        }
    }, [hasMore]);

    useEffect(() => {
        const el = containerRef.current;
        if (!el) return;
        el.addEventListener("scroll", handleScroll);
        autoScrollRef.current = true;
        return () => el.removeEventListener("scroll", handleScroll);
    }, [handleScroll]);

    useEffect(() => {
        if (!lastMessageNotification) return;

        const msg = lastMessageNotification.message;

        switch (lastMessageNotification.action) {
            case "CREATE": {
                const newItem: ChatTimelineItem = { ...msg, type: "message" };
                setItems((prev) => {
                    const ts = new Date(newItem.createTime).getTime();
                    const idx = prev.findIndex(p => new Date(p.createTime).getTime() > ts);
                    if (idx === -1)
                        return [...prev, newItem];
                    return [...prev.slice(0, idx), newItem, ...prev.slice(idx)];
                });

                requestAnimationFrame(() => {
                    const el = containerRef.current;
                    if (el && autoScrollRef.current) {
                        el.scrollTop = el.scrollHeight;
                    }
                });
                break;
            }
            case "UPDATE": {
                setItems((prev) => {
                    const idx = getItemPosition(prev, msg.id, new Date(msg.createTime));
                    if (idx === null) 
                        return prev;
                    const replaced: ChatTimelineItem = { ...msg, type: "message" };
                    const copy = prev.slice();
                    copy[idx] = replaced;
                    copy.sort((a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime());
                    return copy;
                });
                break;
            }
            case "DELETE": {
                setItems((prev) => prev.filter(it => !(it.type === "message" && it.id === msg.id)));
                break;
            }
        }
    }, [lastMessageNotification]);

    useEffect(() => {
        if (!lastEventNotification) return;
        if (lastEventNotification.action !== "CREATE") return;

        const evt = lastEventNotification.event;
        const newItem: ChatTimelineItem = { ...evt, type: "event" };
        setItems((prev) => {
            const ts = new Date(newItem.createTime).getTime();
            const idx = prev.findIndex(p => new Date(p.createTime).getTime() > ts);
            if (idx === -1) return [...prev, newItem];
            return [...prev.slice(0, idx), newItem, ...prev.slice(idx)];
        });

        requestAnimationFrame(() => {
            const el = containerRef.current;
            if (el && autoScrollRef.current) {
                el.scrollTop = el.scrollHeight;
            }
        });
    }, [lastEventNotification]);

    const fetchArgs = useMemo<[string, number]>(
        () => [chatContent.id, page],
        [chatContent.id, page]
    );

    const { data, loading } = useFetch(fetchTimelinePage, fetchArgs);

    useEffect(() => {
        if (!data) return;
        if (data.length === 0) {
            setHasMore(false);
            return;
        }
        setItems((prev) => {
            const merged = [...data, ...prev];
            const seen = new Set<string>();
            const deduped: ChatTimelineItem[] = [];
            for (const it of merged) {
                const key = `${it.type}-${it.id}`;
                if (!seen.has(key)) {
                    seen.add(key);
                    deduped.push(it);
                }
            }
            deduped.sort((a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime());
            return deduped;
        });

        requestAnimationFrame(() => {
            const el = containerRef.current;
            if (el && autoScrollRef.current) {
                el.scrollTop = el.scrollHeight;
            }
        });
    }, [data]);

    let lastUserId: string | null = null;

    return (
        <>
            <Paper
                w="100%"
                h="100%"
                miw={250}
                ref={containerRef}
                style={{ overflowY: "auto" }}
                radius={0}
                bg="aliceblue"
            >
                <Stack p="sm" gap="xs">
                    {loading && page == 0 && <Loader size="md" mr="auto" ml="auto" />}

                    {items.map((item) => {
                        if (item.type === "event") {
                            return (
                                <ChatEventComponent key={`evt-${item.id}`} text={item.text} />
                            );
                        } else {
                            const includeAvatar = item.userAccount.id !== lastUserId;
                            lastUserId = item.userAccount.id;
                            return (
                                <Message
                                    key={`msg-${item.id}`}
                                    message={item}
                                    includeAvatar={includeAvatar}
                                    editMessage={() => setEditedMessage(item)}
                                />
                            );
                        }
                    })}

                    {loading && page > 0 && <Loader size="sm" />}
                </Stack>
            </Paper>

            <MessageInputFooter
                chatContent={chatContent}
                editedMessage={editedMessage}
                finishEditing={() => setEditedMessage(null)}
            />
        </>
    );
}

export default MessageList;
