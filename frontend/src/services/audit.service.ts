import api from './api';
import type { AuditLogDto, PageResponse } from '@/types/api';

export const auditService = {
  async getOrgAuditLogs(orgId: number, page = 0, size = 20): Promise<PageResponse<AuditLogDto>> {
    const res = await api.get(`/user/auditlogs/org/${orgId}`, { params: { page, size } });
    return res.data;
  },
};
