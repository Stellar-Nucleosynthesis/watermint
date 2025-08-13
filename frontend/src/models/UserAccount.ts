export interface UserAccount {
    id: string;
    createTime: string;
    updateTime: string;
    email: string;
    verified: boolean;
    username: string;
    name: string;
    birthDate?: Date;
    profilePicture?: string;
};

export interface UserAccountRequestDto {
    email?: string;
    username?: string;
    password?: string;
    name?: string;
    birthDate?: Date;
    profilePicture?: string;
};