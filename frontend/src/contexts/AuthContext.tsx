import { createContext, useContext, useState, useCallback, useEffect, type ReactNode } from 'react'
import { authService } from '@/services/auth.service'
import { organizationService, type OrgInfo } from '@/services/organization.service'
import { userService } from '@/services/user.service'

interface AuthUser {
  id: string
  email: string
  name: string
  role: string
  avatar?: string | null
  createdAt?: string
}

interface AuthState {
  accessToken: string
  refreshToken: string
  user: AuthUser
  orgId: number | null
}

interface AuthContextType {
  user: AuthUser | null
  isAuthenticated: boolean
  authReady: boolean
  login: (email: string, password: string) => Promise<boolean>
  register: (data: {
    firstName: string
    lastName: string
    email: string
    phoneNumber: string
    password: string
    confirmPassword: string
  }) => Promise<void>
  logout: () => void
  orgId: number | null
  setOrgId: (id: number | null) => void
  orgRole: string | null
  isOrgAdmin: boolean
  orgName: string | null
  orgs: OrgInfo[]
  switchOrg: (id: number) => void
  refreshOrgs: () => Promise<void>
  deptPermissions: Set<string>
  hasPermission: (perm: string) => boolean
}

const AuthContext = createContext<AuthContextType | null>(null)

function getStoredAuth(): AuthState | null {
  const stored = localStorage.getItem('ged_user')
  if (!stored) return null
  try {
    const parsed = JSON.parse(stored)
    if (!parsed.accessToken || !parsed.user) return null
    return parsed as AuthState
  } catch {
    return null
  }
}

function persistOrgId(id: number | null) {
  const raw = localStorage.getItem('ged_user')
  if (raw) {
    try {
      const parsed = JSON.parse(raw)
      parsed.orgId = id
      localStorage.setItem('ged_user', JSON.stringify(parsed))
    } catch {}
  }
  if (id != null) localStorage.setItem('ged_org_id', id.toString())
  else localStorage.removeItem('ged_org_id')
}

function persistUser(patch: Partial<AuthUser>) {
  const raw = localStorage.getItem('ged_user')
  if (!raw) return
  try {
    const parsed = JSON.parse(raw)
    parsed.user = { ...parsed.user, ...patch }
    localStorage.setItem('ged_user', JSON.stringify(parsed))
  } catch {}
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => getStoredAuth()?.user || null)
  const [orgId, setOrgIdState] = useState<number | null>(() => {
    const stored = getStoredAuth()
    if (stored?.orgId) return stored.orgId
    const fromLs = localStorage.getItem('ged_org_id')
    return fromLs ? parseInt(fromLs, 10) : null
  })
  const [authReady, setAuthReady] = useState(false)
  const [orgRole, setOrgRole] = useState<string | null>(null)
  const [orgName, setOrgName] = useState<string | null>(null)
  const [orgs, setOrgs] = useState<OrgInfo[]>([])
  const [deptPermissions, setDeptPermissions] = useState<Set<string>>(new Set())

  const setOrgId = useCallback((id: number | null) => {
    persistOrgId(id)
    setOrgIdState(id)
  }, [])

  const switchOrg = useCallback((id: number) => {
    const match = orgs.find(o => o.id === id)
    if (!match) return
    setOrgId(id)
    setOrgRole(match.role)
    setOrgName(match.name)
  }, [orgs, setOrgId])

  const refreshOrgs = useCallback(async () => {
    try {
      const groups = await organizationService.listMyOrganizations()
      const all = groups.flatMap(g => g.orgs)
      setOrgs(all)
      const storedId = localStorage.getItem('ged_org_id')
      const current = storedId ? all.find(o => o.id === parseInt(storedId, 10)) : all[0]
      if (current) {
        setOrgId(current.id)
        setOrgRole(current.role)
        setOrgName(current.name)
      } else {
        setOrgId(null)
        setOrgRole(null)
        setOrgName(null)
      }
    } catch {
      setOrgId(null)
      setOrgRole(null)
      setOrgName(null)
    }
  }, [setOrgId])

  const syncOrg = refreshOrgs

  const syncProfile = useCallback(async () => {
    const profile = await userService.getProfile()
    const isAdmin = profile.roles?.includes('ROLE_ADMIN')
    const patch: Partial<AuthUser> = {
      id: profile.id,
      name: [profile.firstName, profile.lastName].filter(Boolean).join(' ') || profile.email.split('@')[0],
      avatar: profile.avatarUrl || null,
      role: isAdmin ? 'ROLE_ADMIN' : 'ROLE_USER',
    }
    persistUser(patch)
    setUser(prev => prev ? { ...prev, ...patch } : prev)
  }, [])

  useEffect(() => {
    if (!user) { setAuthReady(true); return }
    Promise.allSettled([syncOrg(), syncProfile()]).finally(() => setAuthReady(true))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const login = useCallback(async (email: string, password: string) => {
    try {
      const res = await authService.login({ email, password })
      const userEmail = res.email || email
      const userData: AuthUser = {
        id: userEmail,
        email: userEmail,
        name: email.split('@')[0],
        role: 'ROLE_USER',
        avatar: null,
        createdAt: new Date().toISOString(),
      }

      const state: AuthState = {
        accessToken: res.access_token,
        refreshToken: res.refresh_token,
        user: userData,
        orgId: null,
      }

      setUser(userData)
      localStorage.setItem('ged_user', JSON.stringify(state))

      await Promise.allSettled([syncOrg(), syncProfile()])

      return true
    } catch {
      return false
    }
  }, [syncOrg, syncProfile])

  const register = useCallback(async (data: {
    firstName: string
    lastName: string
    email: string
    phoneNumber: string
    password: string
    confirmPassword: string
  }) => {
    await authService.register(data)
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('ged_user')
    localStorage.removeItem('ged_org_id')
    setUser(null)
    setOrgIdState(null)
    setOrgRole(null)
    setOrgName(null)
    setOrgs([])
    setDeptPermissions(new Set())
    try { authService.logout() } catch {}
  }, [])

  const isOrgAdmin = orgRole === 'Owner' || orgRole === 'Admin'

  useEffect(() => {
    if (!user || !orgId) { setDeptPermissions(new Set()); return }
    userService.getDeptPermissions(orgId)
      .then(perms => setDeptPermissions(perms))
      .catch(() => setDeptPermissions(new Set()))
  }, [user, orgId])

  const hasPermission = useCallback((perm: string) => {
    if (orgRole === 'Owner' || orgRole === 'Admin') return true
    return deptPermissions.has(perm)
  }, [orgRole, deptPermissions])

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, authReady, login, register, logout, orgId, setOrgId, orgRole, isOrgAdmin, orgName, orgs, switchOrg, refreshOrgs, deptPermissions, hasPermission }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
