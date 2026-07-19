import { useState, useEffect, useCallback, useMemo } from 'react'
import { useLocation } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { useToast } from '@/contexts/ToastContext'
import {
  departmentsService,
  type DepartmentDto,
  type DepartmentMemberDto,
  type DepartmentRoleDto,
  type PermissionGroupDef,
} from '@/services/departments.service'
import { organizationService, type MemberInfo } from '@/services/organization.service'
import { documentService } from '@/services/documents.service'
import type { DocumentDto } from '@/types/api'
import { Button } from '@/components/ui/Button'
import { Select } from '@/components/ui/Select'
import { cn } from '@/lib/cn'
import {
  Plus, X, Search, Users, FileText,
  Trash2, Check, Copy, Share2, Mail, Send, Shield, Crown,
} from 'lucide-react'

const ACCENT = '#e8622a'
const DEPT_COLORS = ['#e8622a', '#2a6fdb', '#1f8a5b', '#8b5cf6', '#d97706', '#db2777', '#0891b2', '#16a34a', '#6366f1']

function tint(hex: string, a: number) {
  const h = hex.replace('#', '')
  const r = parseInt(h.slice(0, 2), 16)
  const g = parseInt(h.slice(2, 4), 16)
  const b = parseInt(h.slice(4, 6), 16)
  return `rgba(${r},${g},${b},${a})`
}

function formatDate(s?: string | null) {
  if (!s) return '—'
  try { return new Date(s).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) } catch { return '—' }
}

function initials(name: string) {
  return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) || '?'
}

function docBadgeStyle(type: string) {
  const map: Record<string, string> = { pdf: '#dc2626', xls: '#16a34a', doc: '#2a6fdb' }
  const c = map[type.toLowerCase()] || '#71717a'
  return { background: tint(c, 0.14), color: c, width: 30, height: 30, borderRadius: 7, display: 'flex' as const, alignItems: 'center' as const, justifyContent: 'center' as const, fontSize: 9, fontWeight: 800, flex: 'none' as const }
}

export function AdminEntitiesPage() {
  const { user, orgId, orgName } = useAuth()
  const location = useLocation()
  const toast = useToast()

  const route = location.pathname.replace('/', '')
  const activeView = route === 'members' ? 'members' : route === 'roles' ? 'roles' : 'departments'

  // Shared state
  const [departments, setDepartments] = useState<DepartmentDto[]>([])
  const [members, setMembers] = useState<DepartmentMemberDto[]>([])
  const [orgMembers, setOrgMembers] = useState<MemberInfo[]>([])
  const [roles, setRoles] = useState<DepartmentRoleDto[]>([])
  const [permDefs, setPermDefs] = useState<PermissionGroupDef[]>([])
  const [allDocs, setAllDocs] = useState<DocumentDto[]>([])
  const [loading, setLoading] = useState(true)

  // Department state
  const [selectedDeptId, setSelectedDeptId] = useState<number | null>(null)
  const [expanded, setExpanded] = useState<Record<string, boolean>>({})
  const [deptTab, setDeptTab] = useState('overview')

  // Members state
  const [memberTab, setMemberTab] = useState('active')

  // Roles state
  const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null)

  // Modal state
  const [modal, setModal] = useState<string | null>(null)
  const [toastMsg, setToastMsg] = useState<string | null>(null)
  const [newDept, setNewDept] = useState({ name: '', parent: '', desc: '', color: '#2a6fdb' })
  const [newRole, setNewRole] = useState({ name: '', base: 'viewer', desc: '' })
  const [assignSel, setAssignSel] = useState({ memberId: '', role: '' })
  const [inviteEmail, setInviteEmail] = useState('')
  const [inviteToken, setInviteToken] = useState('')
  const [inviting, setInviting] = useState(false)
  const [inviteError, setInviteError] = useState('')
  const [inviteRoleId, setInviteRoleId] = useState<number | null>(null)
  const [shareDoc, setShareDoc] = useState<any>(null)
  const [shareRecipients, setShareRecipients] = useState<{ id: string; perm: string }[]>([])
  const [linkPerm, setLinkPerm] = useState('view')
  const [deptNameEdit, setDeptNameEdit] = useState('')
  const [deptDescEdit, setDeptDescEdit] = useState('')

  let toastTimer: ReturnType<typeof setTimeout>

  const flash = useCallback((msg: string) => {
    setToastMsg(msg)
    clearTimeout(toastTimer)
    toastTimer = setTimeout(() => setToastMsg(null), 2400)
  }, [])

  const loadAll = useCallback(async () => {
    if (!orgId) return
    setLoading(true)
    try {
      const [depts, roleList, perms, docs] = await Promise.all([
        departmentsService.list(orgId),
        departmentsService.listRoles(orgId),
        departmentsService.listRolePermissionDefs(orgId).catch(() => []),
        documentService.findAll(0, 200).catch(() => ({ content: [] as DocumentDto[] })),
      ])
      setDepartments(depts)
      setRoles(roleList)
      setPermDefs(perms)
      setAllDocs(docs.content)

      if (depts.length > 0 && !selectedDeptId) {
        setSelectedDeptId(depts[0].id)
        const exp: Record<string, boolean> = {}
        depts.forEach(d => { if (!d.parentId) exp[d.id] = true })
        setExpanded(exp)
      }

      const om = await organizationService.listMembers(orgId).catch(() => [])
      setOrgMembers(om)
    } catch {} finally { setLoading(false) }
  }, [orgId])

  useEffect(() => { loadAll() }, [loadAll])

  // Load members for selected department
  useEffect(() => {
    if (!orgId || !selectedDeptId) { setMembers([]); return }
    departmentsService.listMembers(orgId, selectedDeptId).then(setMembers).catch(() => {})
  }, [orgId, selectedDeptId])

  const deptById = useCallback((id: number | null) => departments.find(d => d.id === id) || null, [departments])
  const childrenOf = useCallback((id: number | null) => departments.filter(d => d.parentId === id), [departments])
  const topLevel = useMemo(() => childrenOf(null), [childrenOf])
  const selectedDept = useMemo(() => deptById(selectedDeptId), [deptById, selectedDeptId])
  const selectedRole = useMemo(() => roles.find(r => r.id === selectedRoleId) ?? null, [roles, selectedRoleId])
  const parentDept = useMemo(() => selectedDept ? deptById(selectedDept.parentId) : null, [selectedDept, deptById])

  const roleMeta = useCallback((id: number | string) => {
    const rid = typeof id === 'string' ? parseInt(id, 10) : id
    return roles.find(r => r.id === rid)
  }, [roles])

  // Permissions helpers
  const hasPerm = useCallback((roleId: number, permId: string) => {
    const role = roles.find(r => r.id === roleId)
    if (!role || !role.permissions) return false
    try {
      const perms = JSON.parse(role.permissions)
      return perms.includes(permId)
    } catch { return false }
  }, [roles])

  const togglePerm = useCallback(async (roleId: number, permId: string) => {
    if (!orgId) return
    const role = roles.find(r => r.id === roleId)
    if (!role) return
    let prevPerms: string[] = []
    try { prevPerms = role.permissions ? JSON.parse(role.permissions) : [] } catch {}
    const newPerms = prevPerms.includes(permId)
      ? prevPerms.filter(p => p !== permId)
      : [...prevPerms, permId]
    // Optimistic update — no full reload, no column flicker
    setRoles(prev => prev.map(r => r.id === roleId ? { ...r, permissions: JSON.stringify(newPerms) } : r))
    try {
      await departmentsService.updateRole(orgId, roleId, { permissions: JSON.stringify(newPerms) })
    } catch {
      // Revert on failure
      setRoles(prev => prev.map(r => r.id === roleId ? { ...r, permissions: JSON.stringify(prevPerms) } : r))
    }
  }, [orgId, roles])

  // Department CRUD
  const createDept = useCallback(async () => {
    if (!orgId || !newDept.name.trim()) return
    try {
      await departmentsService.create(orgId, {
        name: newDept.name.trim(),
        description: newDept.desc.trim() || undefined,
        color: newDept.color,
        parentId: newDept.parent ? parseInt(newDept.parent, 10) : null,
      })
      setModal(null)
      setNewDept({ name: '', parent: '', desc: '', color: '#2a6fdb' })
      await loadAll()
      flash('Department created')
    } catch {
      toast.error('Failed to create department')
    }
  }, [orgId, newDept, loadAll, flash, toast])

  const updateDept = useCallback(async (patch: { name?: string; description?: string }) => {
    if (!orgId || !selectedDeptId) return
    try {
      await departmentsService.update(orgId, selectedDeptId, patch)
      await loadAll()
      flash('Department settings saved')
    } catch { toast.error('Failed to update') }
  }, [orgId, selectedDeptId, loadAll, flash, toast])

  const deleteDept = useCallback(async () => {
    if (!orgId || !selectedDeptId) return
    const subs = childrenOf(selectedDeptId)
    if (subs.length > 0) {
      toast.error('Remove sub-departments first')
      return
    }
    try {
      await departmentsService.remove(orgId, selectedDeptId)
      setSelectedDeptId(null)
      await loadAll()
      flash('Department deleted')
    } catch { toast.error('Failed to delete') }
  }, [orgId, selectedDeptId, childrenOf, loadAll, flash, toast])

  // Role CRUD
  const createRole = useCallback(async () => {
    if (!orgId || !newRole.name.trim()) return
    try {
      const color = DEPT_COLORS[roles.length % DEPT_COLORS.length]
      const created = await departmentsService.createRole(orgId, newRole.name.trim(), color, false)
      setModal(null)
      setNewRole({ name: '', base: 'viewer', desc: '' })
      await loadAll()
      setSelectedRoleId(created.id)
      flash('Role created')
    } catch { toast.error('Failed to create role') }
  }, [orgId, newRole, roles.length, loadAll, flash, toast])

  // Member operations
  const assignMember = useCallback(async () => {
    if (!orgId || !selectedDeptId || !assignSel.memberId || !assignSel.role) return
    try {
      await departmentsService.addMember(orgId, selectedDeptId, assignSel.memberId, parseInt(assignSel.role, 10))
      setModal(null)
      setAssignSel({ memberId: '', role: '' })
      const m = await departmentsService.listMembers(orgId, selectedDeptId)
      setMembers(m)
      flash('Member assigned to department')
    } catch { toast.error('Failed to assign member') }
  }, [orgId, selectedDeptId, assignSel, flash, toast])

  const removeMember = useCallback(async (memberId: number) => {
    if (!orgId || !selectedDeptId) return
    try {
      await departmentsService.removeMember(orgId, selectedDeptId, memberId)
      const m = await departmentsService.listMembers(orgId, selectedDeptId)
      setMembers(m)
      flash('Member removed from department')
    } catch {}
  }, [orgId, selectedDeptId, flash])

  const changeMemberRole = useCallback(async (memberId: number, roleId: string) => {
    if (!orgId || !selectedDeptId) return
    try {
      await departmentsService.changeMemberRole(orgId, selectedDeptId, memberId, parseInt(roleId, 10))
      const m = await departmentsService.listMembers(orgId, selectedDeptId)
      setMembers(m)
    } catch {}
  }, [orgId, selectedDeptId])

  // Share operations
  const addRecipient = useCallback((id: string) => {
    if (!id || shareRecipients.find(r => r.id === id)) return
    setShareRecipients(prev => [...prev, { id, perm: 'view' }])
  }, [shareRecipients])

  const removeRecipient = useCallback((id: string) => {
    setShareRecipients(prev => prev.filter(r => r.id !== id))
  }, [])

  const inviteHandler = useCallback(async () => {
    if (!orgId || !inviteEmail.trim()) return
    setInviting(true)
    setInviteError('')
    try {
      const res = await organizationService.inviteMember(orgId, inviteEmail.trim(), 'MEMBER', inviteRoleId ?? undefined)
      setInviteToken(res.token)
      setInviteEmail('')
      flash(`Invitation sent to ${res.email}`)
    } catch (err: any) {
      setInviteError(err?.response?.data?.message || 'Failed to send invitation')
    } finally { setInviting(false) }
  }, [orgId, inviteEmail, inviteRoleId, flash])

  const shareDocName = shareDoc?.title || 'Document'

  // Check if a member is in a specific department
  const memberUserIds = useMemo(() => new Set(members.map(m => m.userId)), [members])

  const tree = useMemo(() => {
    const treeRowStyle = (id: number) => ({
      display: 'flex' as const, alignItems: 'center' as const, gap: 9, padding: '8px 10px',
      borderRadius: 9, cursor: 'pointer' as const,
      background: selectedDeptId === id ? tint(ACCENT, 0.10) : 'transparent',
      fontWeight: selectedDeptId === id ? 600 : 500,
      color: selectedDeptId === id ? '#1a1a2e' : '#3f3f51',
    })
    return topLevel.map(r => {
      const kids = childrenOf(r.id)
      const isExpanded = !!expanded[r.id]
      return {
        id: r.id,
        name: r.name,
        expanded: isExpanded,
        hasKids: kids.length > 0,
        memberCount: r.memberCount ?? 0,
        rowStyle: treeRowStyle(r.id),
        dotStyle: { width: 9, height: 9, borderRadius: 3, background: r.color || '#71717a', flex: 'none' as const },
        caretStyle: { transform: isExpanded ? 'rotate(90deg)' : 'none', transition: 'transform .15s' as const, display: 'block' as const },
        onSelect: () => { setSelectedDeptId(r.id); setDeptTab('overview') },
        onToggle: (e: React.MouseEvent) => { e.stopPropagation(); setExpanded(prev => ({ ...prev, [r.id]: !prev[r.id] })) },
        children: kids.map(k => ({
          id: k.id, name: k.name,
          memberCount: k.memberCount ?? 0,
          rowStyle: treeRowStyle(k.id),
          dotStyle: { width: 8, height: 8, borderRadius: 2, background: k.color || '#71717a', flex: 'none' as const },
          onSelect: () => { setSelectedDeptId(k.id); setDeptTab('overview') },
        })),
      }
    })
  }, [topLevel, childrenOf, selectedDeptId, expanded, members])

  const btnPrimary = {
    background: ACCENT, color: '#fff', border: 'none', borderRadius: 8,
    padding: '9px 16px', fontSize: 13.5, fontWeight: 600, cursor: 'pointer' as const,
    display: 'inline-flex' as const, alignItems: 'center' as const, gap: 8, whiteSpace: 'nowrap' as const,
  }
  const btnGhost = {
    background: '#fff', color: '#1a1a2e', border: '1px solid #e4e4e7', borderRadius: 8,
    padding: '9px 14px', fontSize: 13.5, fontWeight: 600, cursor: 'pointer' as const,
    display: 'inline-flex' as const, alignItems: 'center' as const, gap: 8, whiteSpace: 'nowrap' as const,
  }

  if (!orgId) return null
  if (loading) return <div className="flex items-center justify-center py-20 text-zinc-400 text-sm">Loading...</div>

  const deptTabs = ['overview', 'members', 'subdepts', 'documents', 'settings']
  const deptTabLabels: Record<string, string> = { overview: 'Overview', members: 'Members', subdepts: 'Sub-departments', documents: 'Documents', settings: 'Settings' }
  const orgNameStr = orgName || 'Organization'

  const selMembers = selectedDeptId ? members : []
  const selSubs = selectedDept ? childrenOf(selectedDept.id) : []
  const selDocs = allDocs.slice(0, 5)

  return (
    <div className="flex flex-col h-full">
      {/* Toast */}
      {toastMsg && (
        <div style={{
          position: 'fixed', bottom: 26, left: '50%', transform: 'translateX(-50%)',
          background: '#1a1a2e', color: '#fff', padding: '12px 20px', borderRadius: 11,
          fontSize: 13.5, fontWeight: 500, boxShadow: '0 12px 30px rgba(0,0,0,.25)',
          zIndex: 60, display: 'flex', alignItems: 'center', gap: 10,
        }}>
          <Check size={16} color="#4ade80" />{toastMsg}
        </div>
      )}

      {/* Modals backdrop */}
      {modal && (
        <div onClick={() => setModal(null)} style={{
          position: 'fixed', inset: 0, background: 'rgba(15,16,30,.5)', backdropFilter: 'blur(3px)',
          zIndex: 50, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 24,
        }}>
          {/* NEW DEPARTMENT */}
          {modal === 'newDept' && (
            <div onClick={e => e.stopPropagation()} style={{
              background: '#fff', borderRadius: 16, width: 480, maxWidth: '100%',
              boxShadow: '0 24px 60px rgba(0,0,0,.25)',
            }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid #f4f4f5', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h3 style={{ margin: 0, fontSize: 17, fontWeight: 700 }}>New department</h3>
                <span onClick={() => setModal(null)} style={{ cursor: 'pointer', color: '#a1a1aa', display: 'flex' }}><X size={20} /></span>
              </div>
              <div style={{ padding: '22px 24px' }}>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Name</label>
                  <input value={newDept.name} onChange={e => setNewDept(p => ({ ...p, name: e.target.value }))} placeholder="e.g. Communications"
                    style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14 }} />
                </div>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Parent department</label>
                  <Select value={newDept.parent} onChange={e => setNewDept(p => ({ ...p, parent: e.target.value }))}>
                    <option value="">— None (top-level department)</option>
                    {departments.map(d => <option key={d.id} value={d.id}>{d.parentId ? '— ' : ''}{d.name}</option>)}
                  </Select>
                </div>
                <div style={{ marginBottom: 4 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Description</label>
                  <textarea value={newDept.desc} onChange={e => setNewDept(p => ({ ...p, desc: e.target.value }))} rows={2} placeholder="What this department is responsible for"
                    style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14, resize: 'vertical' }} />
                </div>
              </div>
              <div style={{ padding: '16px 24px', borderTop: '1px solid #f4f4f5', display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button onClick={() => setModal(null)} style={btnGhost}>Cancel</button>
                <button onClick={createDept} style={btnPrimary}>Create department</button>
              </div>
            </div>
          )}

          {/* ASSIGN MEMBER */}
          {modal === 'assign' && (
            <div onClick={e => e.stopPropagation()} style={{
              background: '#fff', borderRadius: 16, width: 460, maxWidth: '100%',
              boxShadow: '0 24px 60px rgba(0,0,0,.25)',
            }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid #f4f4f5', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h3 style={{ margin: 0, fontSize: 17, fontWeight: 700 }}>Assign member to {selectedDept?.name}</h3>
                <span onClick={() => setModal(null)} style={{ cursor: 'pointer', color: '#a1a1aa', display: 'flex' }}><X size={20} /></span>
              </div>
              <div style={{ padding: '22px 24px' }}>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Member</label>
                  <Select value={assignSel.memberId} onChange={e => setAssignSel(p => ({ ...p, memberId: e.target.value }))}>
                    <option value="">Select a member...</option>
                    {orgMembers.filter(m => m.userId && !memberUserIds.has(m.userId)).map(m => (
                      <option key={m.userId} value={m.userId!}>{m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : m.email} · {m.email}</option>
                    ))}
                  </Select>
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Role in this department</label>
                  <Select value={assignSel.role} onChange={e => setAssignSel(p => ({ ...p, role: e.target.value }))}>
                    {roles.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
                  </Select>
                </div>
              </div>
              <div style={{ padding: '16px 24px', borderTop: '1px solid #f4f4f5', display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button onClick={() => setModal(null)} style={btnGhost}>Cancel</button>
                <button onClick={assignMember} style={btnPrimary}>Assign member</button>
              </div>
            </div>
          )}

          {/* INVITE */}
          {modal === 'invite' && (
            <div onClick={e => e.stopPropagation()} style={{
              background: '#fff', borderRadius: 16, width: 520, maxWidth: '100%',
              boxShadow: '0 24px 60px rgba(0,0,0,.25)',
            }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid #f4f4f5', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h3 style={{ margin: 0, fontSize: 17, fontWeight: 700 }}>Invite members</h3>
                <span onClick={() => setModal(null)} style={{ cursor: 'pointer', color: '#a1a1aa', display: 'flex' }}><X size={20} /></span>
              </div>
              <div style={{ padding: '22px 24px' }}>
                <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Invite by email</label>
                <div style={{ display: 'flex', gap: 8, marginBottom: 7 }}>
                  <input value={inviteEmail} onChange={e => setInviteEmail(e.target.value)} placeholder="colleague@company.com" type="email"
                    style={{ flex: 1, padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14 }} />
                  <Select value={inviteRoleId ?? ''} onChange={e => setInviteRoleId(e.target.value ? Number(e.target.value) : null)} wrapperClassName="w-[140px] shrink-0">
                    <option value="">Member</option>
                    {roles.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
                  </Select>
                  <button onClick={inviteHandler} disabled={!inviteEmail.trim() || inviting}
                    style={{ ...btnPrimary, opacity: (!inviteEmail.trim() || inviting) ? 0.6 : 1 }}>
                    <Send size={14} /> {inviting ? 'Sending...' : 'Invite'}
                  </button>
                </div>
                {inviteError && <div style={{ fontSize: 12, color: '#b91c1c', marginBottom: 12 }}>{inviteError}</div>}

                {inviteToken && (
                  <>
                    <div style={{ height: 1, background: '#e4e4e7', margin: '16px 0' }} />
                    <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Shareable invite link</label>
                    <div style={{ display: 'flex', gap: 8, marginBottom: 7 }}>
                      <div style={{
                        flex: 1, padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9,
                        fontSize: 13, color: '#71717a', background: '#fafafa', whiteSpace: 'nowrap',
                        overflow: 'hidden', textOverflow: 'ellipsis',
                      }}>
                        {`${window.location.origin}/invite/${inviteToken}`}
                      </div>
                      <button onClick={() => { navigator.clipboard.writeText(`${window.location.origin}/invite/${inviteToken}`); flash('Invite link copied to clipboard') }} style={btnPrimary}>
                        <Copy size={14} /> Copy
                      </button>
                    </div>
                    <div style={{ fontSize: 12, color: '#a1a1aa' }}>Anyone with this link can join. New users will be prompted to sign up, existing users can accept immediately.</div>
                  </>
                )}
              </div>
              <div style={{ padding: '16px 24px', borderTop: '1px solid #f4f4f5', display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button onClick={() => setModal(null)} style={btnPrimary}>Done</button>
              </div>
            </div>
          )}

          {/* SHARE DOCUMENT */}
          {modal === 'share' && (
            <div onClick={e => e.stopPropagation()} style={{
              background: '#fff', borderRadius: 16, width: 540, maxWidth: '100%',
              boxShadow: '0 24px 60px rgba(0,0,0,.25)',
            }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid #f4f4f5', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div>
                  <h3 style={{ margin: 0, fontSize: 17, fontWeight: 700 }}>Share document</h3>
                  <div style={{ fontSize: 12.5, color: '#a1a1aa', marginTop: 3 }}>{shareDocName}</div>
                </div>
                <span onClick={() => setModal(null)} style={{ cursor: 'pointer', color: '#a1a1aa', display: 'flex' }}><X size={20} /></span>
              </div>
              <div style={{ padding: '22px 24px' }}>
                <Select onChange={e => { addRecipient(e.target.value); e.target.value = '' }} wrapperClassName="mb-[18px]">
                  <option value="">+ Add a person or team...</option>
                  {orgMembers.filter(m => m.userId && !shareRecipients.find(r => r.id === m.userId)).map(m => (
                    <option key={m.userId} value={m.userId!}>{m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : m.email}</option>
                  ))}
                </Select>
                <div style={{ fontSize: 11, letterSpacing: '.08em', textTransform: 'uppercase', color: '#a1a1aa', fontWeight: 700, marginBottom: 10 }}>People with access</div>
                {shareRecipients.map(r => {
                  const m = orgMembers.find(om => om.userId === r.id)
                  return (
                    <div key={r.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '9px 0' }}>
                      <div style={{
                        background: m ? '#e4e4e7' : '#e4e4e7', color: '#71717a', width: 34, height: 34,
                        borderRadius: 9, display: 'flex', alignItems: 'center', justifyContent: 'center',
                        fontSize: 12.5, fontWeight: 700, flex: 'none',
                      }}>{m ? initials(m.firstName || m.email || '') : '?'}</div>
                      <div style={{ flex: 1, minWidth: 0 }}>
                        <div style={{ fontSize: 13.5, fontWeight: 600 }}>{m ? (m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : m.email) : r.id}</div>
                        <div style={{ fontSize: 12, color: '#a1a1aa' }}>{m?.email || ''}</div>
                      </div>
                      <Select value={r.perm} onChange={e => setShareRecipients(prev => prev.map(x => x.id === r.id ? { ...x, perm: e.target.value } : x))} wrapperClassName="w-36 shrink-0">
                        <option value="view">Can view</option>
                        <option value="comment">Can comment</option>
                        <option value="edit">Can edit</option>
                        <option value="download">Can download</option>
                        <option value="manage">Full access</option>
                      </Select>
                      <span onClick={() => removeRecipient(r.id)} style={{ color: '#c0c0c8', cursor: 'pointer', display: 'flex' }}><X size={17} /></span>
                    </div>
                  )
                })}
                {shareRecipients.length === 0 && <div style={{ fontSize: 13, color: '#a1a1aa', padding: '4px 0 12px' }}>No additional people added yet.</div>}
              </div>
              <div style={{ padding: '16px 24px', borderTop: '1px solid #f4f4f5', display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button onClick={() => { setModal(null); flash('Sharing updated') }} style={btnPrimary}>Done</button>
              </div>
            </div>
          )}

          {/* NEW ROLE */}
          {modal === 'newRole' && (
            <div onClick={e => e.stopPropagation()} style={{
              background: '#fff', borderRadius: 16, width: 460, maxWidth: '100%',
              boxShadow: '0 24px 60px rgba(0,0,0,.25)',
            }}>
              <div style={{ padding: '20px 24px', borderBottom: '1px solid #f4f4f5', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h3 style={{ margin: 0, fontSize: 17, fontWeight: 700 }}>New role</h3>
                <span onClick={() => setModal(null)} style={{ cursor: 'pointer', color: '#a1a1aa', display: 'flex' }}><X size={20} /></span>
              </div>
              <div style={{ padding: '22px 24px' }}>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Role name</label>
                  <input value={newRole.name} onChange={e => setNewRole(p => ({ ...p, name: e.target.value }))} placeholder="e.g. Reviewer"
                    style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14 }} />
                </div>
                <div style={{ marginBottom: 16 }}>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Start from</label>
                  <Select value={newRole.base} onChange={e => setNewRole(p => ({ ...p, base: e.target.value }))}>
                    <option value="viewer">Viewer (read-only)</option>
                    <option value="contributor">Contributor</option>
                    <option value="editor">Editor</option>
                    <option value="admin">Dept. Admin</option>
                  </Select>
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Description</label>
                  <textarea value={newRole.desc} onChange={e => setNewRole(p => ({ ...p, desc: e.target.value }))} rows={2} placeholder="What this role is for"
                    style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14, resize: 'vertical' }} />
                </div>
                <div style={{ fontSize: 12, color: '#a1a1aa', marginTop: 10 }}>You can fine-tune its permissions in the matrix after creating it.</div>
              </div>
              <div style={{ padding: '16px 24px', borderTop: '1px solid #f4f4f5', display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button onClick={() => setModal(null)} style={btnGhost}>Cancel</button>
                <button onClick={createRole} style={btnPrimary}>Create role</button>
              </div>
            </div>
          )}
        </div>
      )}

      {/* ======================== DEPARTMENTS VIEW ======================== */}
      {activeView === 'departments' && (
        <div className="flex-1 overflow-y-auto px-8 py-6">
          <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16, marginBottom: 22 }}>
            <div>
              <div style={{ fontSize: 11, letterSpacing: '.16em', textTransform: 'uppercase', color: ACCENT, fontWeight: 700, marginBottom: 7 }}>{orgNameStr}</div>
              <h1 style={{ margin: 0, fontSize: 27, fontWeight: 800, letterSpacing: '-.02em' }}>Departments</h1>
              <p style={{ margin: '6px 0 0', color: '#71717a', fontSize: 14 }}>Model your organization structure. Create departments, nest sub-departments, and manage members & documents within each.</p>
            </div>
            <button onClick={() => setModal('newDept')} style={btnPrimary}><Plus size={15} /> New department</button>
          </div>

          <div style={{ display: 'flex', gap: 20, alignItems: 'flex-start' }}>
            {/* Tree */}
            <div style={{ width: 320, flex: 'none', background: '#fff', border: '1px solid #e4e4e7', borderRadius: 14, padding: 14 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 9, background: '#f4f4f5', border: '1px solid #e4e4e7', borderRadius: 9, padding: '8px 12px', marginBottom: 12 }}>
                <Search size={15} color="#a1a1aa" />
                <span style={{ color: '#a1a1aa', fontSize: 13 }}>Filter departments</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '0 8px 8px', fontSize: 10.5, letterSpacing: '.12em', textTransform: 'uppercase', color: '#a1a1aa', fontWeight: 700 }}>Organization tree</div>
              {tree.length === 0 && <div style={{ padding: 20, textAlign: 'center', color: '#a1a1aa', fontSize: 13 }}>No departments yet</div>}
              {tree.map(node => (
                <div key={node.id} style={{ marginBottom: 1 }}>
                  <div onClick={node.onSelect} style={node.rowStyle}>
                    {node.hasKids && (
                      <span onClick={node.onToggle} style={{ width: 18, height: 18, display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 'none', color: '#a1a1aa' }}>
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={node.caretStyle}>
                          <polyline points="9 18 15 12 9 6" />
                        </svg>
                      </span>
                    )}
                    {!node.hasKids && <span style={{ width: 18, flex: 'none' }} />}
                    <span style={node.dotStyle} />
                    <span style={{ flex: 1, fontSize: 13.5, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{node.name}</span>
                    <span style={{ background: '#f4f4f5', color: '#71717a', fontSize: 11, fontWeight: 600, padding: '2px 8px', borderRadius: 20 }}>{node.memberCount}</span>
                  </div>
                  {node.expanded && node.children.map((kid: any) => (
                    <div key={kid.id} onClick={kid.onSelect} style={kid.rowStyle}>
                      <span style={{ width: 18, flex: 'none' }} />
                      <span style={{ width: 14, flex: 'none', display: 'flex', justifyContent: 'center', color: '#d4d4d8' }}>└</span>
                      <span style={kid.dotStyle} />
                      <span style={{ flex: 1, fontSize: 13, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{kid.name}</span>
                      <span style={{ background: '#f4f4f5', color: '#71717a', fontSize: 11, fontWeight: 600, padding: '2px 8px', borderRadius: 20 }}>{kid.memberCount}</span>
                    </div>
                  ))}
                </div>
              ))}
            </div>

            {/* Detail */}
            <div style={{ flex: 1, minWidth: 0, background: '#fff', border: '1px solid #e4e4e7', borderRadius: 14, overflow: 'hidden' }}>
              {!selectedDept ? (
                <div style={{ padding: 40, textAlign: 'center', color: '#a1a1aa', fontSize: 14 }}>Select a department to view details</div>
              ) : (
                <>
                  <div style={{ padding: '24px 26px 0' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 7, fontSize: 12.5, color: '#a1a1aa', marginBottom: 12 }}>
                      <span>{orgNameStr}</span>
                      {parentDept && <><span>/</span><span>{parentDept.name}</span></>}
                      <span>/</span><span style={{ color: '#71717a', fontWeight: 600 }}>{selectedDept.name}</span>
                    </div>
                    <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 13 }}>
                        <span style={{ width: 14, height: 14, borderRadius: 4, background: selectedDept.color || '#71717a', flex: 'none' }} />
                        <div>
                          <h2 style={{ margin: 0, fontSize: 23, fontWeight: 800, letterSpacing: '-.02em' }}>{selectedDept.name}</h2>
                          <p style={{ margin: '4px 0 0', color: '#71717a', fontSize: 13.5, maxWidth: 560 }}>{selectedDept.description}</p>
                        </div>
                      </div>
                      <div style={{ display: 'flex', gap: 9, flex: 'none' }}>
                        <button onClick={() => setModal('assign')} style={btnGhost}>
                          <Users size={14} /> Add member
                        </button>
                        <button onClick={() => { setNewDept(prev => ({ ...prev, parent: selectedDept.id.toString() })); setModal('newDept') }} style={btnPrimary}>
                          <Plus size={14} /> Sub-department
                        </button>
                      </div>
                    </div>
                    {/* Tabs */}
                    <div style={{ display: 'flex', marginTop: 20, borderBottom: '1px solid #e4e4e7' }}>
                      {deptTabs.map(t => (
                        <div key={t} onClick={() => setDeptTab(t)} style={{
                          padding: '12px 2px', marginRight: 26, fontSize: 14,
                          fontWeight: deptTab === t ? 600 : 500,
                          color: deptTab === t ? '#1a1a2e' : '#71717a',
                          borderBottom: deptTab === t ? `2px solid ${ACCENT}` : '2px solid transparent',
                          cursor: 'pointer', whiteSpace: 'nowrap',
                        }}>{deptTabLabels[t]}</div>
                      ))}
                    </div>
                  </div>

                  <div style={{ padding: '24px 26px 28px' }}>
                    {/* OVERVIEW */}
                    {deptTab === 'overview' && (
                      <div>
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 14, marginBottom: 22 }}>
                          <div style={{ border: '1px solid #e4e4e7', borderRadius: 12, padding: 16 }}>
                            <div style={{ fontSize: 26, fontWeight: 800 }}>{memberUserIds.size}</div>
                            <div style={{ fontSize: 12.5, color: '#71717a' }}>Members</div>
                          </div>
                          <div style={{ border: '1px solid #e4e4e7', borderRadius: 12, padding: 16 }}>
                            <div style={{ fontSize: 26, fontWeight: 800 }}>{selSubs.length}</div>
                            <div style={{ fontSize: 12.5, color: '#71717a' }}>Sub-departments</div>
                          </div>
                          <div style={{ border: '1px solid #e4e4e7', borderRadius: 12, padding: 16 }}>
                            <div style={{ fontSize: 26, fontWeight: 800 }}>{allDocs.length}</div>
                            <div style={{ fontSize: 12.5, color: '#71717a' }}>Documents</div>
                          </div>
                          <div style={{ border: '1px solid #e4e4e7', borderRadius: 12, padding: 16 }}>
                            <div style={{ fontSize: 26, fontWeight: 800 }}>—</div>
                            <div style={{ fontSize: 12.5, color: '#71717a' }}>Storage used</div>
                          </div>
                        </div>
                        <div style={{ display: 'grid', gridTemplateColumns: '1.3fr 1fr', gap: 18 }}>
                          <div style={{ background: '#fafafa', border: '1px solid #e4e4e7', borderRadius: 12, padding: 18 }}>
                            <div style={{ fontSize: 11, letterSpacing: '.1em', textTransform: 'uppercase', color: '#a1a1aa', fontWeight: 700, marginBottom: 14 }}>Members</div>
                            {memberUserIds.size > 0 ? (
                              <>
                                <div style={{ display: 'flex', alignItems: 'center', paddingLeft: 8 }}>
                                  {Array.from(memberUserIds).slice(0, 6).map((uid, i) => {
                                    const m = orgMembers.find(om => om.userId === uid)
                                    const colors = ['#e8622a', '#2a6fdb', '#1f8a5b', '#8b5cf6', '#d97706', '#db2777']
                                    return (
                                      <span key={uid} style={{
                                        background: colors[i % colors.length], color: '#fff', width: 34, height: 34,
                                        borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                                        fontSize: 12, fontWeight: 700, border: '2px solid #fff', marginLeft: i > 0 ? -8 : 0,
                                      }}>{m ? initials(m.firstName || m.email || '') : '?'}</span>
                                    )
                                  })}
                                </div>
                                <div style={{ marginTop: 12, fontSize: 13, color: '#71717a' }}>{memberUserIds.size} member(s) with access to this department.</div>
                              </>
                            ) : (
                              <div style={{ fontSize: 13, color: '#a1a1aa' }}>No members assigned yet.</div>
                            )}
                          </div>
                          <div style={{ background: '#fff6f4', border: '1px solid #ffe0d4', borderRadius: 12, padding: 18 }}>
                            <div style={{ fontSize: 11, letterSpacing: '.1em', textTransform: 'uppercase', color: '#c2541f', fontWeight: 700, marginBottom: 14 }}>Department lead</div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                              <div style={{ background: ACCENT, color: '#fff', width: 40, height: 40, borderRadius: 10, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 14, fontWeight: 700, flex: 'none' }}>
                                {user ? initials(user.name) : '?'}
                              </div>
                              <div>
                                <div style={{ fontSize: 15, fontWeight: 700 }}>{user?.name || 'Unassigned'}</div>
                                <div style={{ fontSize: 12.5, color: '#71717a' }}>Org owner</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    )}

                    {/* MEMBERS TAB */}
                    {deptTab === 'members' && (
                      <div>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
                          <div style={{ fontSize: 14, fontWeight: 700 }}>{memberUserIds.size} member(s)</div>
                          <button onClick={() => setModal('assign')} style={btnGhost}><Plus size={14} /> Add member</button>
                        </div>
                        {memberUserIds.size > 0 ? (
                          orgMembers.filter(m => m.userId && memberUserIds.has(m.userId)).map(m => {
                            const roleName = roles.find(r => members.some(dm => dm.userId === m.userId && dm.roleId === r.id))?.name || '—'
                            const roleColor = roles.find(r => r.name === roleName)?.color || '#71717a'
                            const dm = members.find(dm => dm.userId === m.userId)
                            return (
                              <div key={m.id} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '12px 2px', borderBottom: '1px solid #f4f4f5' }}>
                                <div style={{ background: ACCENT, color: '#fff', width: 34, height: 34, borderRadius: 9, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 12.5, fontWeight: 700, flex: 'none' }}>
                                  {initials(m.firstName || m.email || '')}
                                </div>
                                <div style={{ flex: 1, minWidth: 0 }}>
                                  <div style={{ fontSize: 13.5, fontWeight: 600, display: 'flex', alignItems: 'center', gap: 8 }}>{m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : m.email}</div>
                                  <div style={{ fontSize: 12, color: '#a1a1aa' }}>{m.email}</div>
                                </div>
                                {dm && (
                                  <Select value={dm.roleId} onChange={e => changeMemberRole(dm.id, e.target.value)}
                                    className="!text-[11px] !py-0.5 !pl-2.5 !pr-7 !font-semibold !rounded-full !border-0"
                                    style={{ background: tint(roleColor, 0.13), color: roleColor }}
                                    wrapperClassName="w-auto shrink-0">
                                    {roles.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
                                  </Select>
                                )}
                                <span style={{ fontSize: 12, color: '#a1a1aa', width: 80, textAlign: 'right' }}>{m.joinedAt ? formatDate(m.joinedAt) : '—'}</span>
                                <span onClick={() => dm && removeMember(dm.id)} style={{ color: '#c0c0c8', cursor: 'pointer', display: 'flex', padding: 6 }}>
                                  <Trash2 size={16} />
                                </span>
                              </div>
                            )
                          })
                        ) : (
                          <div style={{ textAlign: 'center', padding: 40, color: '#a1a1aa', fontSize: 13.5 }}>No members in this department yet.</div>
                        )}
                      </div>
                    )}

                    {/* SUB-DEPARTMENTS TAB */}
                    {deptTab === 'subdepts' && (
                      <div>
                        {selSubs.length > 0 ? (
                          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 14 }}>
                            {selSubs.map(s => (
                              <div key={s.id} onClick={() => { setSelectedDeptId(s.id); setDeptTab('overview') }}
                                style={{ border: '1px solid #e4e4e7', borderRadius: 12, padding: 16, cursor: 'pointer' }}>
                                <div style={{ display: 'flex', alignItems: 'center', gap: 9, marginBottom: 8 }}>
                                  <span style={{ width: 10, height: 10, borderRadius: 3, background: s.color || '#71717a', flex: 'none' }} />
                                  <span style={{ fontSize: 15, fontWeight: 700 }}>{s.name}</span>
                                </div>
                                <div style={{ fontSize: 12.5, color: '#71717a', marginBottom: 12 }}>{s.description}</div>
                                <div style={{ display: 'flex', gap: 16, fontSize: 12, color: '#a1a1aa' }}>
                                  <span>{s.memberCount} members</span>
                                  <span>{allDocs.filter(d => d.id).length} documents</span>
                                </div>
                              </div>
                            ))}
                          </div>
                        ) : (
                          <div style={{ textAlign: 'center', padding: '48px 20px', border: '1px dashed #e4e4e7', borderRadius: 12 }}>
                            <div style={{ color: '#71717a', fontSize: 14, fontWeight: 600 }}>No sub-departments</div>
                            <div style={{ color: '#a1a1aa', fontSize: 13, margin: '6px 0 16px' }}>Break this department down into smaller units.</div>
                            <button onClick={() => { setNewDept(prev => ({ ...prev, parent: selectedDeptId?.toString() || '' })); setModal('newDept') }} style={{ ...btnPrimary, margin: '0 auto' }}>
                              Create sub-department
                            </button>
                          </div>
                        )}
                      </div>
                    )}

                    {/* DOCUMENTS TAB */}
                    {deptTab === 'documents' && (
                      <div>
                        {selDocs.length > 0 ? (
                          <div>
                            <div style={{ display: 'flex', padding: '0 2px 10px', fontSize: 11, letterSpacing: '.06em', textTransform: 'uppercase', color: '#a1a1aa', fontWeight: 700, borderBottom: '1px solid #e4e4e7' }}>
                              <span style={{ flex: 1 }}>Name</span>
                              <span style={{ width: 90 }}>Size</span>
                              <span style={{ width: 90 }}>Modified</span>
                              <span style={{ width: 70, textAlign: 'right' }}>Share</span>
                            </div>
                            {selDocs.map(doc => {
                              const ext = (doc.originalFilename || doc.title || '').split('.').pop()?.toLowerCase() || 'file'
                              const size = doc.fileSizeBytes ? (doc.fileSizeBytes / 1024 / 1024).toFixed(1) + ' MB' : '—'
                              return (
                                <div key={doc.id} style={{ display: 'flex', alignItems: 'center', padding: '12px 2px', borderBottom: '1px solid #f4f4f5' }}>
                                  <div style={{ flex: 1, display: 'flex', alignItems: 'center', gap: 13, minWidth: 0 }}>
                                    <span style={docBadgeStyle(ext)}>{ext.toUpperCase()}</span>
                                    <span style={{ fontSize: 13.5, fontWeight: 600, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{doc.title}</span>
                                  </div>
                                  <span style={{ width: 90, fontSize: 12.5, color: '#71717a' }}>{size}</span>
                                  <span style={{ width: 90, fontSize: 12.5, color: '#71717a' }}>{formatDate(doc.lastModifiedDate)}</span>
                                  <span style={{ width: 70, display: 'flex', justifyContent: 'flex-end' }}>
                                    <button onClick={() => { setShareDoc(doc); setShareRecipients([]); setModal('share') }} style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 8, padding: '6px 12px', fontSize: 12.5, fontWeight: 600, cursor: 'pointer', color: '#1a1a2e', display: 'inline-flex', alignItems: 'center', gap: 6 }}>
                                      <Share2 size={13} /> Share
                                    </button>
                                  </span>
                                </div>
                              )
                            })}
                          </div>
                        ) : (
                          <div style={{ textAlign: 'center', padding: 48, color: '#a1a1aa', fontSize: 13.5 }}>No documents in this department yet.</div>
                        )}
                      </div>
                    )}

                    {/* SETTINGS TAB */}
                    {deptTab === 'settings' && (
                      <div style={{ maxWidth: 560 }}>
                        <div style={{ marginBottom: 18 }}>
                          <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Department name</label>
                          <input defaultValue={selectedDept.name} onChange={e => setDeptNameEdit(e.target.value)}
                            style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14 }} />
                        </div>
                        <div style={{ marginBottom: 18 }}>
                          <label style={{ display: 'block', fontSize: 12.5, fontWeight: 600, color: '#3f3f51', marginBottom: 7 }}>Description</label>
                          <textarea defaultValue={selectedDept.description} onChange={e => setDeptDescEdit(e.target.value)} rows={3}
                            style={{ width: '100%', padding: '10px 13px', border: '1px solid #e4e4e7', borderRadius: 9, fontSize: 14, resize: 'vertical' }} />
                        </div>
                        <div style={{ display: 'flex', gap: 10, marginBottom: 26 }}>
                          <button onClick={() => updateDept({ name: deptNameEdit || selectedDept.name, description: deptDescEdit || selectedDept.description })} style={btnPrimary}>Save changes</button>
                        </div>
                        <div style={{ border: '1px solid #fbd5cd', background: '#fef5f3', borderRadius: 12, padding: 16 }}>
                          <div style={{ fontSize: 13.5, fontWeight: 700, color: '#b91c1c' }}>Danger zone</div>
                          <div style={{ fontSize: 12.5, color: '#71717a', margin: '5px 0 12px' }}>Deleting a department removes its document space. Members must be reassigned first.</div>
                          <button onClick={deleteDept} style={{ background: '#fff', border: '1px solid #f1b5ad', color: '#b91c1c', borderRadius: 8, padding: '8px 14px', fontSize: 13, fontWeight: 600, cursor: 'pointer' }}>Delete department</button>
                        </div>
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      )}

      {/* ======================== MEMBERS VIEW ======================== */}
      {activeView === 'members' && (
        <div className="flex-1 overflow-y-auto px-8 py-6">
          <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16, marginBottom: 22 }}>
            <div>
              <div style={{ fontSize: 11, letterSpacing: '.16em', textTransform: 'uppercase', color: ACCENT, fontWeight: 700, marginBottom: 7 }}>{orgNameStr}</div>
              <h1 style={{ margin: 0, fontSize: 27, fontWeight: 800, letterSpacing: '-.02em' }}>Members</h1>
              <p style={{ margin: '6px 0 0', color: '#71717a', fontSize: 14 }}>Invite people to your organization and assign them to departments and roles.</p>
            </div>
            <button onClick={() => setModal('invite')} style={btnPrimary}><Users size={15} /> Invite members</button>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 14, marginBottom: 22 }}>
            <div style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 12, padding: 18 }}>
              <div style={{ fontSize: 26, fontWeight: 800 }}>{orgMembers.filter(m => m.isActive !== false).length}</div>
              <div style={{ fontSize: 12.5, color: '#71717a' }}>Active members</div>
            </div>
            <div style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 12, padding: 18 }}>
              <div style={{ fontSize: 26, fontWeight: 800 }}>{orgMembers.filter(m => m.isActive === false).length}</div>
              <div style={{ fontSize: 12.5, color: '#71717a' }}>Pending invites</div>
            </div>
            <div style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 12, padding: 18 }}>
              <div style={{ fontSize: 26, fontWeight: 800 }}>{orgMembers.filter(m => m.role === 'OWNER' || m.role === 'ADMIN').length}</div>
              <div style={{ fontSize: 12.5, color: '#71717a' }}>Admins & owners</div>
            </div>
            <div style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 12, padding: 18 }}>
              <div style={{ fontSize: 26, fontWeight: 800 }}>{departments.length}</div>
              <div style={{ fontSize: 12.5, color: '#71717a' }}>Departments</div>
            </div>
          </div>

          <div style={{ background: '#fff', border: '1px solid #e4e4e7', borderRadius: 14, overflow: 'hidden' }}>
            <div style={{ display: 'flex', padding: '0 22px', borderBottom: '1px solid #e4e4e7' }}>
              {([['active', 'Active members', orgMembers.filter(m => m.isActive !== false).length] as const, ['pending', 'Pending invites', orgMembers.filter(m => m.isActive === false).length] as const]).map(([id, label, n]) => (
                <div key={id} onClick={() => setMemberTab(id)} style={{
                  padding: '12px 2px', marginRight: 26, fontSize: 14,
                  fontWeight: memberTab === id ? 600 : 500,
                  color: memberTab === id ? '#1a1a2e' : '#71717a',
                  borderBottom: memberTab === id ? `2px solid ${ACCENT}` : '2px solid transparent',
                  cursor: 'pointer',
                }}>{label} ({n})</div>
              ))}
            </div>
            <div style={{ padding: '6px 22px 14px' }}>
              <div style={{ display: 'flex', padding: '12px 2px', fontSize: 11, letterSpacing: '.06em', textTransform: 'uppercase', color: '#a1a1aa', fontWeight: 700, borderBottom: '1px solid #f4f4f5' }}>
                <span style={{ flex: 1 }}>Member</span>
                <span style={{ width: 180 }}>Department(s)</span>
                <span style={{ width: 130 }}>Role</span>
                <span style={{ width: 110 }}>Last active</span>
              </div>
              {orgMembers.filter(m => {
                if (memberTab === 'active') return m.isActive !== false
                return m.isActive === false
              }).map(m => {
                const deptNames: string[] = []
                return (
                  <div key={m.id} style={{ display: 'flex', alignItems: 'center', padding: '12px 2px', borderBottom: '1px solid #f4f4f5' }}>
                    <div style={{ flex: 1, display: 'flex', alignItems: 'center', gap: 12, minWidth: 0 }}>
                      <div style={{ background: ACCENT, color: '#fff', width: 36, height: 36, borderRadius: 10, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 13, fontWeight: 700, flex: 'none' }}>
                        {initials(m.firstName || m.email || '')}
                      </div>
                      <div style={{ minWidth: 0 }}>
                        <div style={{ fontSize: 13.5, fontWeight: 600, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : m.email}</div>
                        <div style={{ fontSize: 12, color: '#a1a1aa', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{m.email}</div>
                      </div>
                    </div>
                    <span style={{ width: 180, fontSize: 12.5, color: '#71717a' }}>{deptNames.join(', ') || '—'}</span>
                    <span style={{ width: 130 }}>
                      <span style={{ background: tint(ACCENT, 0.13), color: ACCENT, padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, whiteSpace: 'nowrap' }}>{m.role}</span>
                    </span>
                    <span style={{ width: 110, fontSize: 12.5, color: '#a1a1aa' }}>{formatDate(m.joinedAt)}</span>
                  </div>
                )
              })}
            </div>
          </div>
        </div>
      )}

      {/* ======================== ROLES VIEW ======================== */}
      {activeView === 'roles' && (
        <div className="flex-1 overflow-y-auto px-8 py-6">
          {/* Header */}
          <div className="flex items-start justify-between gap-4 mb-6">
            <div>
              <p className="text-[11px] font-bold uppercase tracking-widest text-[#e8622a] mb-1">{orgNameStr}</p>
              <h1 className="text-2xl font-extrabold tracking-tight text-zinc-900">Roles & Permissions</h1>
              <p className="text-zinc-500 text-sm mt-1">Define custom roles and control exactly what each can do.</p>
            </div>
            <Button onClick={() => setModal('newRole')}><Plus size={15} /> New role</Button>
          </div>

          {roles.length === 0 ? (
            <div className="doc-card py-20 flex flex-col items-center justify-center text-center">
              <div className="w-14 h-14 rounded-2xl bg-zinc-100 flex items-center justify-center mb-4">
                <Shield size={26} className="text-zinc-300" />
              </div>
              <p className="text-base font-semibold text-zinc-600 mb-1">No roles yet</p>
              <p className="text-sm text-zinc-400 mb-5">Create your first role to start assigning permissions.</p>
              <Button size="sm" onClick={() => setModal('newRole')}><Plus size={13} /> New role</Button>
            </div>
          ) : (
            <div className="flex gap-5 items-start">

              {/* ── Left: role list ── */}
              <div className="w-56 flex-none space-y-1.5">
                <div className="doc-card p-2 space-y-0.5">
                  {roles.map(r => {
                    const memberCount = orgMembers.filter(m => { const rm = roleMeta(m.role); return rm?.id === r.id }).length
                    const isSelected = selectedRoleId === r.id
                    return (
                      <button
                        key={r.id}
                        onClick={() => setSelectedRoleId(r.id)}
                        className={cn(
                          'w-full flex items-center gap-2.5 px-3 py-2.5 rounded-lg text-left transition-colors',
                          isSelected ? 'bg-[#e8622a]/10' : 'hover:bg-zinc-50'
                        )}
                      >
                        <span className="w-2.5 h-2.5 rounded-full flex-none" style={{ background: r.color || '#71717a' }} />
                        <span className={cn('flex-1 text-sm font-medium truncate', isSelected ? 'text-zinc-900' : 'text-zinc-600')}>{r.name}</span>
                        {r.isLead && <Crown size={11} className="text-amber-400 flex-none" fill="currentColor" />}
                        <span className={cn(
                          'text-[11px] font-semibold px-1.5 py-0.5 rounded-full leading-none flex-none',
                          isSelected ? 'bg-[#e8622a]/15 text-[#c2541f]' : 'bg-zinc-100 text-zinc-500'
                        )}>{memberCount}</span>
                      </button>
                    )
                  })}
                </div>
              </div>

              {/* ── Right: role detail ── */}
              {!selectedRole ? (
                <div className="flex-1 doc-card py-16 flex flex-col items-center justify-center text-center">
                  <Shield size={28} className="text-zinc-200 mb-3" />
                  <p className="text-sm text-zinc-400">Select a role to configure its permissions</p>
                </div>
              ) : (
                <div className="flex-1 min-w-0 space-y-4">
                  {/* Role info card */}
                  <div className="doc-card p-5">
                    <div className="flex items-center gap-4">
                      <div
                        className="w-11 h-11 rounded-xl flex items-center justify-center flex-none"
                        style={{ background: (selectedRole.color || '#71717a') + '20' }}
                      >
                        <Shield size={20} style={{ color: selectedRole.color || '#71717a' }} />
                      </div>
                      <div className="flex-1 min-w-0">
                        <h2 className="text-lg font-bold text-zinc-900 leading-tight">{selectedRole.name}</h2>
                        <p className="text-xs text-zinc-400 mt-0.5">
                          {orgMembers.filter(m => { const rm = roleMeta(m.role); return rm?.id === selectedRole.id }).length} member{orgMembers.filter(m => { const rm = roleMeta(m.role); return rm?.id === selectedRole.id }).length !== 1 ? 's' : ''}
                          {' · '}
                          {(() => { try { return selectedRole.permissions ? JSON.parse(selectedRole.permissions).length : 0 } catch { return 0 } })()} permission{(() => { try { return (selectedRole.permissions ? JSON.parse(selectedRole.permissions).length : 0) !== 1 } catch { return true } })() ? 's' : ''}
                        </p>
                      </div>
                      <div className="flex items-center gap-3 flex-none">
                        {/* Color picker */}
                        <div className="flex gap-1.5">
                          {DEPT_COLORS.map(c => (
                            <button
                              key={c}
                              title={c}
                              onClick={async () => {
                                const prev = selectedRole.color
                                setRoles(r => r.map(x => x.id === selectedRole.id ? { ...x, color: c } : x))
                                try { await departmentsService.updateRole(orgId!, selectedRole.id, { color: c }) }
                                catch { setRoles(r => r.map(x => x.id === selectedRole.id ? { ...x, color: prev } : x)) }
                              }}
                              className="w-4 h-4 rounded-full border-2 transition-transform hover:scale-125 focus:outline-none"
                              style={{ background: c, borderColor: selectedRole.color === c ? '#18181b' : 'transparent' }}
                            />
                          ))}
                        </div>
                        {/* Lead toggle */}
                        <button
                          title={selectedRole.isLead ? 'Remove lead status' : 'Mark as lead role'}
                          onClick={async () => {
                            const next = !selectedRole.isLead
                            setRoles(r => r.map(x => x.id === selectedRole.id ? { ...x, isLead: next } : x))
                            try { await departmentsService.updateRole(orgId!, selectedRole.id, { isLead: next }) }
                            catch { setRoles(r => r.map(x => x.id === selectedRole.id ? { ...x, isLead: selectedRole.isLead } : x)) }
                          }}
                          className={cn('p-2 rounded-lg transition-colors', selectedRole.isLead ? 'text-amber-400 bg-amber-50' : 'text-zinc-300 hover:text-amber-400 hover:bg-amber-50')}
                        >
                          <Crown size={15} fill={selectedRole.isLead ? 'currentColor' : 'none'} />
                        </button>
                        {/* Delete */}
                        <button
                          onClick={async () => {
                            try {
                              await departmentsService.deleteRole(orgId!, selectedRole.id)
                              setSelectedRoleId(null)
                              setRoles(prev => prev.filter(r => r.id !== selectedRole.id))
                              flash('Role deleted')
                            } catch {
                              toast.error('Role is assigned to members — remove it from members first')
                            }
                          }}
                          className="p-2 rounded-lg text-zinc-400 hover:text-red-500 hover:bg-red-50 transition-colors"
                        >
                          <Trash2 size={15} />
                        </button>
                      </div>
                    </div>
                  </div>

                  {/* Permissions checklist */}
                  <div className="doc-card overflow-hidden">
                    {/* Checklist header */}
                    <div className="px-5 py-4 border-b border-zinc-100 flex items-center justify-between">
                      <div>
                        <h3 className="text-sm font-semibold text-zinc-800">Permissions</h3>
                        {(() => {
                          const total = permDefs.reduce((a, g) => a + g.perms.length, 0)
                          const granted = (() => { try { return selectedRole.permissions ? JSON.parse(selectedRole.permissions).length : 0 } catch { return 0 } })()
                          return (
                            <p className="text-xs text-zinc-400 mt-0.5">{granted} of {total} granted</p>
                          )
                        })()}
                      </div>
                      {(() => {
                        const total = permDefs.reduce((a, g) => a + g.perms.length, 0)
                        const granted = (() => { try { return selectedRole.permissions ? JSON.parse(selectedRole.permissions).length : 0 } catch { return 0 } })()
                        if (total === 0) return null
                        return (
                          <div className="h-1.5 w-32 bg-zinc-100 rounded-full overflow-hidden">
                            <div
                              className="h-full rounded-full bg-[#e8622a] transition-all duration-300"
                              style={{ width: `${Math.round((granted / total) * 100)}%` }}
                            />
                          </div>
                        )
                      })()}
                    </div>

                    {/* Groups & rows */}
                    {permDefs.length === 0 ? (
                      <div className="px-5 py-10 text-sm text-zinc-400 text-center">No permissions defined</div>
                    ) : permDefs.map(g => (
                      <div key={g.group}>
                        <div className="px-5 py-2.5 text-[10px] font-bold uppercase tracking-widest text-[#c2541f] bg-[#fff6f4] border-y border-[#fde8e0]">
                          {g.group}
                        </div>
                        {g.perms.map(p => {
                          const checked = hasPerm(selectedRole.id, p.id)
                          return (
                            <div
                              key={p.id}
                              onClick={() => togglePerm(selectedRole.id, p.id)}
                              className={cn(
                                'flex items-center gap-4 px-5 py-3.5 cursor-pointer transition-colors border-b border-zinc-50 last:border-0',
                                checked ? 'hover:bg-[#fff9f7]' : 'hover:bg-zinc-50'
                              )}
                            >
                              <div className={cn(
                                'w-5 h-5 rounded-[5px] flex items-center justify-center flex-none transition-all border',
                                checked ? 'bg-[#e8622a] border-[#e8622a]' : 'bg-white border-zinc-300'
                              )}>
                                {checked && <Check size={12} color="#fff" strokeWidth={3} />}
                              </div>
                              <div className="flex-1 min-w-0">
                                <p className={cn('text-sm font-medium', checked ? 'text-zinc-900' : 'text-zinc-600')}>{p.label}</p>
                                <p className="text-[11px] text-zinc-400 font-mono mt-0.5">{p.id}</p>
                              </div>
                              {checked && (
                                <span className="text-[11px] font-semibold px-2 py-0.5 rounded-full bg-[#e8622a]/10 text-[#c2541f] flex-none">
                                  Granted
                                </span>
                              )}
                            </div>
                          )
                        })}
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  )
}
