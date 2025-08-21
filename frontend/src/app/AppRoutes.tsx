import { Route, Routes } from 'react-router-dom';
import MainPage from '../pages/MainPage.tsx';
import SignUpPage from '../pages/SignUpPage.tsx';
import LogInPage from '../pages/LogInPage.tsx';
import ChatPage from '../pages/ChatPage.tsx';
import ViewedPrivateChat from '../components/ViewedPrivateChat.tsx';
import NoSelectedChat from '../components/NoSelectedChat.tsx';

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<MainPage />} />

            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/login" element={<LogInPage />} />
            <Route path="/chats" element={<ChatPage />}>
                <Route index element={<NoSelectedChat />} />
                <Route path="private-chat/:chatId" element={<ViewedPrivateChat />} />
            </Route>
        </Routes>
    )
}

export default AppRoutes;