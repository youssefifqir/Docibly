import api from './api';
import axios from 'axios';

export interface MemberInfo {
  id: number
  userId: string | null
  email: string | null
  firstName: string | null
  lastName: string | null
  role: string
  joinedAt: string | null
  isActive: boolean | null
}

export interface InvitationInfo {
  id: number
  token: string
  email: string
  role: string
  expiresAt: string
  departmentRoleId?: number
}

export interface AcceptedInvitation {
  id: number
  orgId: number
  role: string
}

export interface CurrentOrg {
  id: number
  name: string
  slug: string
  role: string
}

export interface OrgInfo {
  id: number
  name: string
  slug: string
  role: string
}

export interface OrgGroup {
  label: string
  orgs: OrgInfo[]
}

export const organizationService = {
  async listMyOrganizations(): Promise<OrgGroup[]> {
    const res = await api.get<OrgGroup[]>('/user/organizations/mine')
    return res.data
  },

  async getCurrent(): Promise<CurrentOrg | null> {
    try {
      const res = await api.get<CurrentOrg>('/user/organizations/current', { suppressErrorToast: true })
      return res.data
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.status === 404) return null
      throw err
    }
  },

  async listMembers(orgId: number): Promise<MemberInfo[]> {
    const res = await api.get('/user/organizations/members', {
      headers: { 'X-Org-Id': orgId.toString() },
    })
    return res.data
  },

  async inviteMember(orgId: number, email: string, role = 'MEMBER', departmentRoleId?: number): Promise<InvitationInfo> {
    const body: Record<string, string> = { email, role }
    if (departmentRoleId != null) body.departmentRoleId = departmentRoleId.toString()
    const res = await api.post('/user/organizations/invite', body, {
      headers: { 'X-Org-Id': orgId.toString() },
    })
    return res.data
  },

  async acceptInvitation(token: string): Promise<AcceptedInvitation> {
    const res = await api.post('/user/organizations/accept-invitation', { token })
    return res.data
  },

  async changeRole(orgId: number, memberId: number, role: string): Promise<void> {
    await api.put(`/user/organizations/members/${memberId}/role`, { role }, {
      headers: { 'X-Org-Id': orgId.toString() },
    })
  },

  async removeMember(orgId: number, memberId: number): Promise<void> {
    await api.delete(`/user/organizations/members/${memberId}`, {
      headers: { 'X-Org-Id': orgId.toString() },
    })
  },
};
