export interface UserAccount {
    id: string;
    createTime: Date;
    updateTime: Date;
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