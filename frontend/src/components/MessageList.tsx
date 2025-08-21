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
        size: 20
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

function MessageList({ chatContent }: MessageListProps) {
    const [page, setPage] = useState(0);
    const [items, setItems] = useState<ChatTimelineItem[]>([]);
    const [hasMore, setHasMore] = useState(true);
    const [editedMessage, setEditedMessage] = useState<ChatMessage | null>(null);
    const containerRef = useRef<HTMLDivElement>(null);

    const fetchArgs = useMemo<[string, number]>(
        () => [chatContent.id, page],
        [chatContent.id, page]
    );

    const { data, loading } = useFetch(fetchTimelinePage, fetchArgs);

    useEffect(() => {
        if (!data)
            return;
        if (data.length === 0) {
            setHasMore(false);
            return;
        }
        setItems((prev) => [...data, ...prev]);
    }, [data]);

    const onScroll = useCallback(() => {
        const el = containerRef.current;
        if (!el || loading || !hasMore) return;

        if (el.scrollTop <= 0) {
            const prevHeight = el.scrollHeight;
            setPage((p) => p + 1);

            requestAnimationFrame(() => {
                const newHeight = el.scrollHeight;
                el.scrollTop = newHeight - prevHeight;
            });
        }
    }, [loading, hasMore]);

    useEffect(() => {
        const el = containerRef.current;
        if (!el) return;
        el.addEventListener("scroll", onScroll);
        return () => el.removeEventListener("scroll", onScroll);
    }, [onScroll]);

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
