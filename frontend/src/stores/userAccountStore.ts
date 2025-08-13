import { create } from "zustand";
import type { UserAccount } from "../models/UserAccount";
import { getUserAccountByUsername } from "../services/userAccountService";


interface UserAccountStore {
    userAccount: UserAccount | null;
    loadingAccount: boolean;
    error: string | null;
    fetchByUsername: (username: string) => Promise<void>;
    clearUserAccount: () => void;
}

export const useUserAccountStore = create<UserAccountStore>((set) => ({
    userAccount: null,
    loadingAccount: false,
    error: null,

    fetchByUsername: async (username: string) => {
        set({ loadingAccount: true, error: null });
        try {
            const data = await getUserAccountByUsername(username);
            set({ userAccount: data });
        } catch (err) {
            set({ error: err instanceof Error ? err.message : "Unknown error" });
        } finally {
            set({ loadingAccount: false });
        }
    },

    clearUserAccount: () => set({ userAccount: null, error: null }),
}));