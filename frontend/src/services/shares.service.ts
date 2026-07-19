import api from './api';
import type { DocumentShareDto, PageResponse } from '@/types/api';

export interface CreateShareResult {
  id: number
  token: string
  permission: string
  expiresAt: string | null
  requiresPassword: boolean | null
  url: string
}

export const sharesService = {
  async createShare(
    documentId: number,
    orgId: number,
    permission = 'VIEW',
    expiresAt?: string,
    password?: string,
  ): Promise<CreateShareResult> {
    const res = await api.post('/user/documentshares/create',
      { documentId, permission, expiresAt, password },
      { headers: { 'X-Org-Id': orgId.toString() } },
    )
    return res.data
  },

  async revokeShare(shareId: number, orgId: number): Promise<void> {
    await api.post(`/user/documentshares/${shareId}/revoke`, {},
      { headers: { 'X-Org-Id': orgId.toString() } },
    )
  },

  async listShares(page = 0, size = 20): Promise<PageResponse<DocumentShareDto>> {
    const res = await api.get('/user/documentshares', { params: { page, size } })
    return res.data
  },

  async getShare(id: number): Promise<DocumentShareDto> {
    const res = await api.get(`/user/documentshares/${id}`)
    return res.data
  },
}
