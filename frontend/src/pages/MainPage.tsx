import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

function MainPage() {
    const navigate = useNavigate();
    useEffect(() => {
        navigate('/signup');
    }, [navigate]);
    return (<></>);
}

export default MainPage;