import api from '@/services/api'
import type { PageResponse } from '@/types/api'

export interface TenantDto {
  id?: number
  name: string
  slug: string
  logoUrl?: string
  isActive?: boolean
  createdDate?: string
}

export const adminService = {
  async getTenants(page = 0, size = 20): Promise<PageResponse<TenantDto>> {
    const res = await api.get('/admin/tenants', { params: { page, size } })
    return res.data
  },

  async getTenant(id: number): Promise<TenantDto> {
    const res = await api.get(`/admin/tenants/${id}`)
    return res.data
  },

  async createTenant(data: { name: string; slug: string }): Promise<TenantDto> {
    const res = await api.post('/admin/tenants', data)
    return res.data
  },

  async updateTenant(id: number, data: Partial<TenantDto>): Promise<TenantDto> {
    const res = await api.put(`/admin/tenants/${id}`, data)
    return res.data
  },

  async deleteTenant(id: number): Promise<void> {
    await api.delete(`/admin/tenants/${id}`)
  },

  async getUsers(page = 0, size = 20): Promise<PageResponse<any>> {
    const res = await api.get('/admin/users', { params: { page, size } })
    return res.data
  },

  async activateUser(id: string): Promise<void> {
    await api.put(`/admin/users/${id}/activate`)
  },

  async deactivateUser(id: string): Promise<void> {
    await api.put(`/admin/users/${id}/deactivate`)
  },

  async deleteUser(id: string): Promise<void> {
    await api.delete(`/admin/users/${id}`)
  },

  async getEntities(page = 0, size = 20): Promise<PageResponse<any>> {
    const res = await api.get('/administrativeentitys', { params: { page, size } })
    return res.data
  },

  async getTags(page = 0, size = 50): Promise<PageResponse<any>> {
    const res = await api.get('/tags', { params: { page, size } })
    return res.data
  },

  async createTag(data: { name: string; color?: string }): Promise<any> {
    const res = await api.post('/tags', data)
    return res.data
  },

  async deleteTag(id: number): Promise<void> {
    await api.delete(`/tags/${id}`)
  },
}
