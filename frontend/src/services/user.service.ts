import api from './api';
import type { ProfileResponse, ProfileUpdateRequest } from '@/types/api';

export const userService = {
  async getProfile(): Promise<ProfileResponse> {
    const res = await api.get('/users/profile');
    return res.data;
  },

  async updateProfile(data: ProfileUpdateRequest): Promise<void> {
    await api.patch('/users/profile', data);
  },

  async getDeptPermissions(orgId: number): Promise<Set<string>> {
    const res = await api.get('/user/me/dept-permissions', { headers: { 'X-Org-Id': orgId.toString() } });
    const perms: string[] = res.data?.permissions ?? []
    return new Set(perms)
  },
};
