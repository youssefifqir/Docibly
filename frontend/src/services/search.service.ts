import api from './api';
import type { SearchResponse } from '@/types/api';

export const searchService = {
  async search(q: string, page = 0, size = 20): Promise<SearchResponse> {
    const res = await api.get('/search', { params: { q, page, size } });
    return res.data;
  },
};
