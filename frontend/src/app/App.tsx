import { BrowserRouter } from 'react-router-dom';
import AppRoutes from "./AppRoutes";
import { MantineProvider } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import '@mantine/dates/styles.css';

function App(){
    return (
        <MantineProvider
            theme={{
                fontFamily: 'Open Sans, sans-serif',
                headings: { fontFamily: 'Open Sans, sans-serif' },
            }}
        >
            <Notifications position='top-center' zIndex={1000}/>
            <BrowserRouter>
                <AppRoutes/>
            </BrowserRouter>
        </MantineProvider>
    );
}

export default App;