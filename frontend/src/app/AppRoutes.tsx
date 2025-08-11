import { Route, Routes } from 'react-router-dom';
import MainPage from '../pages/MainPage.tsx';
import SignUpPage from '../pages/SignUpPage.tsx';

function AppRoutes(){
    return (
        <Routes>
            <Route path="/" element={<MainPage/>}/>

            <Route path="/signup" element={<SignUpPage/>}/>
        </Routes>
    )
} 

export default AppRoutes;