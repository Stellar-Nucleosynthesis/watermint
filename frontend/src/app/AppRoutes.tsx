import { Route, Routes } from 'react-router-dom';
import MainPage from '../pages/MainPage.tsx';
import SignUpPage from '../pages/SignUpPage.tsx';
import LogInPage from '../pages/LogInPage.tsx';

function AppRoutes(){
    return (
        <Routes>
            <Route path="/" element={<MainPage/>}/>

            <Route path="/signup" element={<SignUpPage/>}/>
            <Route path="/login" element={<LogInPage/>}/>
        </Routes>
    )
} 

export default AppRoutes;