import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import type { IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { ChatMessageNotification } from "../models/ChatActivityNotifications";
import { useAuthStore } from "../stores/authStore";

interface ChatMessageNotificationHook {
    lastNotification: ChatMessageNotification | null;
}

function useChatMessageNotification(chatContentId: string): ChatMessageNotificationHook {
    const { token } = useAuthStore();
    const [lastNotification, setLastNotification] = useState<ChatMessageNotification | null>(null);
    const clientRef = useRef<Client>(null);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/api/ws"),
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/chat-content/${chatContentId}/messages`, (message: IMessage) => {
                    setLastNotification(JSON.parse(message.body));
                });
            },
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [chatContentId]);

    return { lastNotification };
}

export default useChatMessageNotification;