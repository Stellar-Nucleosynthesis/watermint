import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import type { IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { ChatEventNotification } from "../models/ChatActivityNotifications";
import { useAuthStore } from "../stores/authStore";

interface ChatEventNotificationHook {
    lastNotification: ChatEventNotification | null;
}

function useChatEventNotification(chatContentId: string): ChatEventNotificationHook {
    const { token } = useAuthStore();
    const [lastNotification, setLastNotification] = useState<ChatEventNotification | null>(null);
    const clientRef = useRef<Client>(null);

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/api/ws"),
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/chat-content/${chatContentId}/events`, (message: IMessage) => {
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

export default useChatEventNotification;