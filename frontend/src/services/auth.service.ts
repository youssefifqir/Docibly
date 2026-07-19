import api from './api';
import type { AuthResponse } from '@/types/api';

interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  password: string;
  confirmPassword: string;
}

function decodeJwt(token: string): Record<string, unknown> | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    return JSON.parse(atob(parts[1]));
  } catch {
    return null;
  }
}

function getEmailFromToken(token: string): string | null {
  const claims = decodeJwt(token);
  if (claims && typeof claims.sub === 'string') return claims.sub;
  return null;
}

export const authService = {
  async login(data: LoginRequest): Promise<AuthResponse & { email?: string }> {
    const res = await api.post<AuthResponse>('/auth/login', data);
    const email = getEmailFromToken(res.data.access_token);
    return { ...res.data, email: email || undefined };
  },

  async register(data: RegisterRequest): Promise<void> {
    await api.post('/auth/register', data);
  },

  async refreshToken(refreshToken: string): Promise<AuthResponse & { email?: string }> {
    const res = await api.post<AuthResponse>('/auth/refresh', { refreshToken });
    const email = getEmailFromToken(res.data.access_token);
    return { ...res.data, email: email || undefined };
  },

  async logout(): Promise<void> {
    try { await api.post('/auth/logout'); } catch { /* ignore */ }
  },

  async forgotPassword(email: string): Promise<void> {
    await api.post('/auth/forgot-password', { email });
  },

  async resetPassword(token: string, newPassword: string, confirmNewPassword: string): Promise<void> {
    await api.post('/auth/reset-password', { token, newPassword, confirmNewPassword });
  },

  async verifyEmail(token: string): Promise<void> {
    await api.post('/auth/verify-email', { token });
  },
};
