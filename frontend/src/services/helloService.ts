import api from './api.ts';

export const getHello = async () => {
    const response = await api.get<string>('/api/hello');
    return response.data;
}