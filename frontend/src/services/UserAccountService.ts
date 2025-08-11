import type { UserAccount, UserAccountRequestDto } from "../models/UserAccount";
import api from "./api";

export const createUserAccount = async (
    request: UserAccountRequestDto
): Promise<UserAccount> => {
    const response = await api.post<UserAccount>("/user-account", request);
    return response.data;
};