import { useState, useEffect } from 'react';

interface FetchResult<T> {
    data: T | null;
    loading: boolean;
    error: Error | null;
}

function useFetch<T>(fetchFunction: () => Promise<T>): FetchResult<T> {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<Error | null>(null);

    useEffect(() => {
        setData(null);
        setLoading(true);
        setError(null);
        fetchFunction()
        .then(result => {
            setData(result);
        })
        .catch(error => {
            setError(error);
        })
        .finally(() => {
            setLoading(false);
        });
    }, [fetchFunction])

    return { data, loading, error };
}

export default useFetch;