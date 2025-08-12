import { jwtDecode } from "jwt-decode";
import { useState, useEffect, useRef } from "react";

const tokenKey = "jwt_watermint";

interface AuthStore{
    token: string | null,
    setToken: (token: string) => void,
    isAuthenticated: boolean,
    currentUsername: string | null
}

function isValid(token: string | null): boolean {
    if(token == null) 
        return false;
    try {
        const { exp } = jwtDecode<{ exp?: number }>(token);
        if(exp == undefined) 
            return false;
        return Date.now() < exp * 1000;
    } catch {
        return false;
    }
}

function getUsernameFromToken(token: string | null): string | null {
    if(token == null)
        return null;
    try {
        const { sub } = jwtDecode<{ sub?: string }>(token);
        return sub ?? null;
    } catch {
        return null;
    }
}

function getTimeToExpiration(token: string | null): number | null {
    if(token == null)
        return null;
    try {
        const { exp } = jwtDecode<{ exp?: number }>(token);
        if(exp == null)
            return null;
        const now = Date.now();
        return exp * 1000 - now;
    } catch {
        return null;
    }
}

function getValidToken(): string | null {
    const storedToken = localStorage.getItem(tokenKey);
    return isValid(storedToken) ? storedToken : null;
}

function storeToken(token: string) {
    if(isValid(token))
        localStorage.setItem(tokenKey, token);
}

function deleteToken() {
    localStorage.removeItem(tokenKey);
}

export function useAuthStore(): AuthStore {
    const [token, setTokenState] = useState<string | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [currentUsername, setCurrentUsername] = useState<string | null>(null);
    const expirationTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

    const updateStateFromToken = (token: string | null) => {
        if (expirationTimerRef.current != null) {
            clearTimeout(expirationTimerRef.current);
            expirationTimerRef.current = null;
        }
        setTokenState(token);
        setIsAuthenticated(token != null);
        setCurrentUsername(getUsernameFromToken(token));

        const msToExpiration = getTimeToExpiration(token);
        if (msToExpiration != null && msToExpiration > 0) {
            expirationTimerRef.current = setTimeout(() => {
                deleteToken();
                updateStateFromToken(null);
            }, msToExpiration);
        }
    }

    const setToken = (token: string) => {
        storeToken(token);
        updateStateFromToken(getValidToken());
    }

    useEffect(() => {
        const syncToken = () => {
            const latest = getValidToken();
            updateStateFromToken(latest);
        };

        syncToken();
        window.addEventListener('storage', syncToken);
        return () => {
            window.removeEventListener('storage', syncToken);
            if (expirationTimerRef.current != null) 
                clearTimeout(expirationTimerRef.current);
        }
    }, []);

    return {token, setToken, isAuthenticated, currentUsername};
}