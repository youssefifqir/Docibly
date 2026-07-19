import api from './api';
import type { FolderDto } from '@/types/api';

export const folderService = {
  async findAll(page = 0, size = 50): Promise<{ content: FolderDto[] }> {
    const res = await api.get('/folders', { params: { page, size } });
    return res.data;
  },

  async create(data: { name: string; description?: string; color?: string }): Promise<FolderDto> {
    const res = await api.post('/folders', data);
    return res.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/folders/${id}`);
  },
};
