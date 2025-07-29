import useFetch from '../hooks/useFetch.ts'
import { getHello } from '../services/helloService.ts'

function MainPage(){
    const { data: greeting } = useFetch(getHello, []);
    return (
        <div>
            <h1>My First Heading</h1>
            <p>My first paragraph.</p>
            <p>Greeting: {greeting}</p>
        </div>
    );
}

export default MainPage;