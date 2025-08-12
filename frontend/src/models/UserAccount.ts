export interface UserAccount {
    id: string;
    createTime?: string;
    updateTime?: string;
    email: string;
    verified?: boolean;
    username: string;
    name: string;
    birthDate?: string;
    profilePicture?: Uint8Array;
};

export interface UserAccountRequestDto {
    email: string;
    username: string;
    password: string;
    name: string;
    birthDate?: string;
    profilePicture?: Uint8Array;
};