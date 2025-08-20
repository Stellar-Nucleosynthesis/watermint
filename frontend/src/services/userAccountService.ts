import type { UserAccount, UserAccountRequestDto } from "../models/UserAccount";
import api from "./api";

export const createUserAccount = async (
    request: UserAccountRequestDto
): Promise<UserAccount> => {
    const response = await api.post<UserAccount>("/user-account", request);
    return response.data;
};

export const getUserAccountByUsername = async (
    username: string | null
): Promise<UserAccount | null> => {
    if(!username)
        return null;
    const response = await api.get<UserAccount>("/user-account/username/" + username);
    return response.data;
};

export const getOpenUserAccountByUsername = async (
    username: string
): Promise<UserAccount> => {
    const response = await api.get<UserAccount>("/user-account/username/" + username);
    return response.data;
};

export const updateUserAccount = async (
    id: string, 
    dto: UserAccountRequestDto
): Promise<UserAccount> => {
    const response = await api.patch<UserAccount>("/user-account/" + id, dto);
    return response.data;
}