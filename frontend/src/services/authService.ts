
import type { AuthRequestDto, AuthResponseDto } from "../models/AuthDto";
import api from "./api";

export const logIn = async (
    request: AuthRequestDto
): Promise<AuthResponseDto> => {
    const response = await api.post<AuthResponseDto>("/auth/login", request);
    return response.data;
};