import api from './api';
import type { TagDto } from '@/types/api';

export const tagService = {
  async findAll(page = 0, size = 50): Promise<{ content: TagDto[] }> {
    const res = await api.get('/tags', { params: { page, size } });
    return res.data;
  },

  async create(data: { name: string; color?: string; slug?: string }): Promise<TagDto> {
    const res = await api.post('/tags', data);
    return res.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/tags/${id}`);
  },
};
