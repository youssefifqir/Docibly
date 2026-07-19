import api from './api';

export interface DepartmentDto {
  id: number
  name: string
  description: string
  color: string
  isActive: boolean
  memberCount: number
  subDepartmentCount: number
  parentId: number | null
  parentName: string | null
  responsibleName: string | null
  responsibleEmail: string | null
}

export interface DepartmentMemberDto {
  id: number
  userId: string | null
  email: string | null
  firstName: string | null
  lastName: string | null
  roleId: number
  role: string
  joinedAt: string | null
}

export interface DepartmentRoleDto {
  id: number
  name: string
  color: string
  isLead: boolean
  permissions: string | null
}

export interface PermissionDef {
  id: string
  label: string
}

export interface PermissionGroupDef {
  group: string
  perms: PermissionDef[]
}

export interface DepartmentSharedDocDto {
  id: number
  documentId: number | null
  documentTitle: string | null
  permission: string
  sharedBy?: string | null
  departmentName?: string
  sharedAt: string | null
  expiresAt?: string | null
}

function orgHeader(orgId: number) {
  return { headers: { 'X-Org-Id': orgId.toString() } };
}

export const departmentsService = {
  async list(orgId: number): Promise<DepartmentDto[]> {
    const res = await api.get('/admin/departments', orgHeader(orgId));
    return res.data;
  },

  async create(orgId: number, data: { name: string; description?: string; color?: string; parentId?: number | null }): Promise<DepartmentDto> {
    const payload: Record<string, string> = { name: data.name }
    if (data.description) payload.description = data.description
    if (data.color) payload.color = data.color
    if (data.parentId != null) payload.parentId = data.parentId.toString()
    const res = await api.post('/admin/departments', payload, orgHeader(orgId));
    return res.data;
  },

  async update(orgId: number, id: number, data: { name?: string; description?: string; color?: string; isActive?: boolean; parentId?: number | null }): Promise<DepartmentDto> {
    const payload: Record<string, string> = {}
    if (data.name !== undefined) payload.name = data.name
    if (data.description !== undefined) payload.description = data.description
    if (data.color !== undefined) payload.color = data.color
    if (data.isActive !== undefined) payload.isActive = data.isActive.toString()
    if ('parentId' in data) payload.parentId = data.parentId != null ? data.parentId.toString() : ''
    const res = await api.put(`/admin/departments/${id}`, payload, orgHeader(orgId));
    return res.data;
  },

  async remove(orgId: number, id: number): Promise<void> {
    await api.delete(`/admin/departments/${id}`, orgHeader(orgId));
  },

  async listMembers(orgId: number, deptId: number): Promise<DepartmentMemberDto[]> {
    const res = await api.get(`/admin/departments/${deptId}/members`, orgHeader(orgId));
    return res.data;
  },

  async addMember(orgId: number, deptId: number, userId: string, roleId: number): Promise<DepartmentMemberDto> {
    const res = await api.post(`/admin/departments/${deptId}/members`, { userId, roleId: roleId.toString() }, orgHeader(orgId));
    return res.data;
  },

  async changeMemberRole(orgId: number, deptId: number, memberId: number, roleId: number): Promise<void> {
    await api.put(`/admin/departments/${deptId}/members/${memberId}/role`, { roleId: roleId.toString() }, orgHeader(orgId));
  },

  async removeMember(orgId: number, deptId: number, memberId: number): Promise<void> {
    await api.delete(`/admin/departments/${deptId}/members/${memberId}`, orgHeader(orgId));
  },

  async listRoles(orgId: number): Promise<DepartmentRoleDto[]> {
    const res = await api.get('/admin/department-roles', orgHeader(orgId));
    return res.data;
  },

  async createRole(orgId: number, name: string, color?: string, isLead = false, permissions?: string): Promise<DepartmentRoleDto> {
    const payload: Record<string, string> = { name }
    if (color) payload.color = color
    payload.isLead = isLead.toString()
    if (permissions) payload.permissions = permissions
    const res = await api.post('/admin/department-roles', payload, orgHeader(orgId));
    return res.data;
  },

  async updateRole(orgId: number, roleId: number, data: { name?: string; color?: string; isLead?: boolean; permissions?: string }): Promise<DepartmentRoleDto> {
    const payload: Record<string, string> = {}
    if (data.name !== undefined) payload.name = data.name
    if (data.color !== undefined) payload.color = data.color
    if (data.isLead !== undefined) payload.isLead = data.isLead.toString()
    if (data.permissions !== undefined) payload.permissions = data.permissions
    const res = await api.put(`/admin/department-roles/${roleId}`, payload, orgHeader(orgId));
    return res.data;
  },

  async listRolePermissionDefs(orgId: number): Promise<PermissionGroupDef[]> {
    const res = await api.get('/admin/department-roles/permissions', orgHeader(orgId));
    return res.data;
  },

  async deleteRole(orgId: number, roleId: number): Promise<void> {
    await api.delete(`/admin/department-roles/${roleId}`, orgHeader(orgId));
  },

  async listSharedDocs(orgId: number, deptId: number): Promise<DepartmentSharedDocDto[]> {
    const res = await api.get(`/admin/departments/${deptId}/shared-docs`, orgHeader(orgId));
    return res.data;
  },

  async shareDocument(orgId: number, deptId: number, documentId: number, permission: string, sharedByUserId: string): Promise<DepartmentSharedDocDto> {
    const res = await api.post(`/admin/departments/${deptId}/shared-docs`, { documentId, permission, sharedByUserId }, orgHeader(orgId));
    return res.data;
  },

  async updateSharePermission(orgId: number, deptId: number, shareId: number, permission: string): Promise<void> {
    await api.put(`/admin/departments/${deptId}/shared-docs/${shareId}`, { permission }, orgHeader(orgId));
  },

  async removeShare(orgId: number, deptId: number, shareId: number): Promise<void> {
    await api.delete(`/admin/departments/${deptId}/shared-docs/${shareId}`, orgHeader(orgId));
  },

  async myDepartments(orgId: number): Promise<DepartmentDto[]> {
    const res = await api.get('/user/departments/my', orgHeader(orgId));
    return res.data;
  },

  async sharedWithMyDepartments(orgId: number): Promise<DepartmentSharedDocDto[]> {
    const res = await api.get('/user/departments/shared-docs', orgHeader(orgId));
    return res.data;
  },
};
