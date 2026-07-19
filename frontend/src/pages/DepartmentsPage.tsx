import { useState, useEffect, useCallback, useMemo } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { useToast } from '@/contexts/ToastContext'
import {
  departmentsService,
  type DepartmentDto,
  type DepartmentMemberDto,
  type DepartmentSharedDocDto,
  type DepartmentRoleDto,
} from '@/services/departments.service'
import { organizationService, type MemberInfo } from '@/services/organization.service'
import { documentService } from '@/services/documents.service'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Select } from '@/components/ui/Select'
import { Avatar } from '@/components/ui/Avatar'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { cn } from '@/lib/cn'
import {
  Building2, Plus, Trash2, ChevronRight, Folder, FolderTree, FileText,
  Users, X, Pencil, Settings2, Crown, Home,
} from 'lucide-react'
import type { DocumentDto } from '@/types/api'

const DEPT_COLORS = ['#e8622a', '#2563eb', '#16a34a', '#9333ea', '#dc2626', '#0891b2']
const PERMISSIONS = ['Can View', 'Can Comment', 'Can Edit', 'Can Manage']

function formatDate(s?: string | null) {
  if (!s) return '—'
  return new Date(s).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

// ───────────────────────── Role manager ─────────────────────────

function RoleManager({ orgId, roles, onChanged }: { orgId: number; roles: DepartmentRoleDto[]; onChanged: () => void }) {
  const toast = useToast()
  const [open, setOpen] = useState(false)
  const [name, setName] = useState('')
  const [color, setColor] = useState(DEPT_COLORS[1])
  const [saving, setSaving] = useState(false)

  async function handleAdd(e: React.FormEvent) {
    e.preventDefault()
    if (!name.trim()) return
    setSaving(true)
    try {
      await departmentsService.createRole(orgId, name.trim(), color)
      setName('')
      onChanged()
    } catch {} finally { setSaving(false) }
  }

  async function toggleLead(role: DepartmentRoleDto) {
    try {
      await departmentsService.updateRole(orgId, role.id, { isLead: !role.isLead })
      onChanged()
    } catch {}
  }

  async function handleDelete(roleId: number) {
    try {
      await departmentsService.deleteRole(orgId, roleId)
      onChanged()
    } catch {
      toast.error('Role is assigned to members — remove it from members first')
    }
  }

  return (
    <div className="relative">
      <button onClick={() => setOpen(o => !o)}
        className="flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 px-2.5 py-1.5 rounded-lg hover:bg-zinc-100 border border-zinc-200">
        <Settings2 size={13} /> Manage roles
      </button>
      {open && (
        <div className="absolute right-0 top-10 z-20 w-80 bg-white border border-zinc-200 rounded-xl shadow-lg p-4">
          <div className="flex items-center justify-between mb-1">
            <h4 className="text-sm font-semibold text-zinc-800">Department roles</h4>
            <button onClick={() => setOpen(false)} className="text-zinc-400 hover:text-zinc-600"><X size={14} /></button>
          </div>
          <p className="text-[11px] text-zinc-400 mb-3">Org-wide roles, usable in any department. Star a role to mark it as the department's "responsible" role.</p>
          <div className="space-y-1.5 mb-3 max-h-48 overflow-y-auto">
            {roles.map(r => (
              <div key={r.id} className="flex items-center justify-between bg-zinc-50 rounded-lg px-2.5 py-2">
                <span className="flex items-center gap-2 text-sm font-medium">
                  <span className="w-2 h-2 rounded-full" style={{ background: r.color || '#9ca3af' }} />
                  {r.name}
                </span>
                <div className="flex items-center gap-2">
                  <button onClick={() => toggleLead(r)} title="Mark as responsible role"
                    className={cn('hover:text-amber-500', r.isLead ? 'text-amber-500' : 'text-zinc-300')}>
                    <Crown size={14} fill={r.isLead ? 'currentColor' : 'none'} />
                  </button>
                  <button onClick={() => handleDelete(r.id)} className="text-zinc-400 hover:text-red-500"><Trash2 size={13} /></button>
                </div>
              </div>
            ))}
          </div>
          <form onSubmit={handleAdd} className="flex items-center gap-1.5">
            <input value={name} onChange={e => setName(e.target.value)} placeholder="New role name"
              className="flex-1 border border-zinc-300 rounded-lg px-2.5 py-1.5 text-xs min-w-0 focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary" />
            <div className="flex gap-1 shrink-0">
              {DEPT_COLORS.slice(0, 4).map(c => (
                <button key={c} type="button" onClick={() => setColor(c)} className="w-4 h-4 rounded-full border"
                  style={{ background: c, borderColor: color === c ? '#18181b' : 'transparent' }} />
              ))}
            </div>
            <Button type="submit" size="sm" disabled={!name.trim() || saving}><Plus size={12} /></Button>
          </form>
        </div>
      )}
    </div>
  )
}

// ───────────────────────── Department tile (grid card) ─────────────────────────

function DepartmentTile({ dept, onOpen, onDelete }: { dept: DepartmentDto; onOpen: () => void; onDelete: () => void }) {
  return (
    <div onClick={onOpen} className="doc-card p-5 cursor-pointer group flex flex-col gap-3 relative animate-fade-in">
      <div className="flex items-center justify-between">
        <div className="w-10 h-10 rounded-lg flex items-center justify-center shrink-0" style={{ background: (dept.color || '#9ca3af') + '20' }}>
          <Folder size={18} style={{ color: dept.color || '#9ca3af' }} />
        </div>
        <button onClick={e => { e.stopPropagation(); onDelete() }}
          className="w-7 h-7 rounded-md flex items-center justify-center opacity-0 group-hover:opacity-100 hover:bg-red-50 text-zinc-400 hover:text-red-500 transition-all">
          <Trash2 size={13} />
        </button>
      </div>
      <div>
        <h3 className="font-bold text-sm text-zinc-900 leading-snug truncate">{dept.name}</h3>
        {dept.description && <p className="text-xs text-zinc-400 mt-0.5 line-clamp-2">{dept.description}</p>}
      </div>
      <div className="flex items-center gap-3 text-[11px] text-zinc-400 pt-3 border-t border-zinc-100">
        <span className="flex items-center gap-1"><Users size={12} /> {dept.memberCount}</span>
        {dept.subDepartmentCount > 0 && (
          <span className="flex items-center gap-1"><FolderTree size={12} /> {dept.subDepartmentCount}</span>
        )}
        <ChevronRight size={13} className="ml-auto text-zinc-300 group-hover:text-primary transition-colors" />
      </div>
      {dept.responsibleName && (
        <div className="flex items-center gap-1.5 -mt-1">
          <Avatar name={dept.responsibleName} size="sm" className="w-5 h-5 text-[9px]" />
          <span className="text-[11px] text-zinc-500 truncate">
            Responsible: <span className="font-medium text-zinc-700">{dept.responsibleName}</span>
          </span>
        </div>
      )}
    </div>
  )
}

// ───────────────────────── Create form ─────────────────────────

function CreateDepartmentForm({ parentId, parentName, onClose, onCreated, orgId }: {
  parentId: number | null
  parentName: string | null
  onClose: () => void
  onCreated: () => void
  orgId: number
}) {
  const toast = useToast()
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [color, setColor] = useState(DEPT_COLORS[0])
  const [saving, setSaving] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!name.trim()) return
    setSaving(true)
    try {
      await departmentsService.create(orgId, { name: name.trim(), description: description.trim() || undefined, color, parentId })
      toast.success('Department created')
      onCreated()
      onClose()
    } catch {} finally { setSaving(false) }
  }

  return (
    <div className="bg-white border border-zinc-200 rounded-xl p-4 mb-5 animate-fade-in">
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-sm font-semibold">
          {parentId == null ? 'New top-level department' : `New sub-department of "${parentName}"`}
        </h3>
        <button onClick={onClose} className="text-zinc-400 hover:text-zinc-600"><X size={15} /></button>
      </div>
      <form onSubmit={handleSubmit} className="flex items-end gap-3 flex-wrap">
        <div className="flex-1 min-w-[180px]">
          <Input placeholder="Department name" value={name} onChange={e => setName(e.target.value)} autoFocus />
        </div>
        <div className="flex-1 min-w-[180px]">
          <Input placeholder="Description (optional)" value={description} onChange={e => setDescription(e.target.value)} />
        </div>
        <div className="flex gap-1.5 pb-2.5">
          {DEPT_COLORS.map(c => (
            <button key={c} type="button" onClick={() => setColor(c)} className="w-6 h-6 rounded-full border-2"
              style={{ background: c, borderColor: color === c ? '#18181b' : 'transparent' }} />
          ))}
        </div>
        <Button type="submit" size="sm" disabled={!name.trim() || saving}>{saving ? 'Creating...' : 'Create'}</Button>
      </form>
    </div>
  )
}

// ───────────────────────── Members section ─────────────────────────

function MembersSection({ dept, roles, orgId, onChanged }: {
  dept: DepartmentDto
  roles: DepartmentRoleDto[]
  orgId: number
  onChanged: () => void
}) {
  const [members, setMembers] = useState<DepartmentMemberDto[]>([])
  const [orgMembers, setOrgMembers] = useState<MemberInfo[]>([])
  const [loading, setLoading] = useState(true)
  const [addMemberId, setAddMemberId] = useState('')
  const [addMemberRoleId, setAddMemberRoleId] = useState('')

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const [m, om] = await Promise.all([
        departmentsService.listMembers(orgId, dept.id),
        organizationService.listMembers(orgId),
      ])
      setMembers(m)
      setOrgMembers(om)
    } catch {} finally { setLoading(false) }
  }, [orgId, dept.id])

  useEffect(() => { load() }, [load])

  const defaultRoleId = useMemo(() => {
    const lead = roles.find(r => r.isLead)
    return (lead || roles[0])?.id.toString() || ''
  }, [roles])
  useEffect(() => { setAddMemberRoleId(defaultRoleId) }, [defaultRoleId])

  async function handleAdd() {
    if (!addMemberId || !addMemberRoleId) return
    try {
      await departmentsService.addMember(orgId, dept.id, addMemberId, parseInt(addMemberRoleId, 10))
      setAddMemberId('')
      load()
      onChanged()
    } catch {}
  }

  async function handleRemove(memberId: number) {
    try {
      await departmentsService.removeMember(orgId, dept.id, memberId)
      load()
      onChanged()
    } catch {}
  }

  async function handleRoleChange(memberId: number, roleId: string) {
    try {
      await departmentsService.changeMemberRole(orgId, dept.id, memberId, parseInt(roleId, 10))
      load()
      onChanged()
    } catch {}
  }

  const memberUserIds = new Set(members.map(m => m.userId))

  return (
    <div>
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-sm font-semibold flex items-center gap-1.5"><Users size={14} /> Members ({members.length})</h3>
      </div>
      <div className="flex gap-1.5 mb-3">
        <Select value={addMemberId} onChange={e => setAddMemberId(e.target.value)} wrapperClassName="flex-1 min-w-0" className="!text-xs !py-1.5">
          <option value="">Select a member to add...</option>
          {orgMembers.filter(m => m.userId && !memberUserIds.has(m.userId)).map(m => (
            <option key={m.userId} value={m.userId!}>{m.email || m.userId}</option>
          ))}
        </Select>
        <Select value={addMemberRoleId} onChange={e => setAddMemberRoleId(e.target.value)} wrapperClassName="w-32 shrink-0" className="!text-xs !py-1.5">
          {roles.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
        </Select>
        <Button size="sm" onClick={handleAdd} disabled={!addMemberId}><Plus size={13} /></Button>
      </div>
      {loading ? (
        <p className="text-xs text-zinc-400 py-2">Loading...</p>
      ) : members.length === 0 ? (
        <p className="text-xs text-zinc-400 py-2">No members yet</p>
      ) : (
        <div className="space-y-1.5">
          {members.map(m => (
            <div key={m.id} className="flex items-center gap-2.5 bg-zinc-50 rounded-lg px-3 py-2">
              <Avatar name={m.firstName ? `${m.firstName} ${m.lastName || ''}` : (m.email || '?')} size="sm" />
              <span className="text-sm font-medium truncate flex-1">{m.email || m.userId}</span>
              <Select value={m.roleId} onChange={e => handleRoleChange(m.id, e.target.value)} wrapperClassName="w-28 shrink-0" className="!text-[11px] !py-1">
                {roles.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
              </Select>
              <button onClick={() => handleRemove(m.id)} className="p-1 hover:bg-red-50 rounded text-zinc-400 hover:text-red-500 shrink-0"><Trash2 size={13} /></button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

// ───────────────────────── Shared documents section ─────────────────────────

function SharedDocsSection({ dept, orgId, userId, onChanged }: {
  dept: DepartmentDto
  orgId: number
  userId: string
  onChanged: () => void
}) {
  const [sharedDocs, setSharedDocs] = useState<DepartmentSharedDocDto[]>([])
  const [allDocs, setAllDocs] = useState<DocumentDto[]>([])
  const [loading, setLoading] = useState(true)
  const [addDocId, setAddDocId] = useState('')
  const [addDocPermission, setAddDocPermission] = useState('Can View')

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const [s, docs] = await Promise.all([
        departmentsService.listSharedDocs(orgId, dept.id),
        documentService.findAll(0, 100),
      ])
      setSharedDocs(s)
      setAllDocs(docs.content)
    } catch {} finally { setLoading(false) }
  }, [orgId, dept.id])

  useEffect(() => { load() }, [load])

  async function handleShare() {
    if (!addDocId) return
    try {
      await departmentsService.shareDocument(orgId, dept.id, parseInt(addDocId, 10), addDocPermission, userId)
      setAddDocId('')
      load()
      onChanged()
    } catch {}
  }

  async function handlePermissionChange(shareId: number, permission: string) {
    try {
      await departmentsService.updateSharePermission(orgId, dept.id, shareId, permission)
      load()
    } catch {}
  }

  async function handleRemove(shareId: number) {
    try {
      await departmentsService.removeShare(orgId, dept.id, shareId)
      load()
      onChanged()
    } catch {}
  }

  const sharedDocIds = new Set(sharedDocs.map(s => s.documentId))

  return (
    <div>
      <h3 className="text-sm font-semibold flex items-center gap-1.5 mb-3"><FileText size={14} /> Shared documents ({sharedDocs.length})</h3>
      <div className="flex gap-1.5 mb-3">
        <Select value={addDocId} onChange={e => setAddDocId(e.target.value)} wrapperClassName="flex-1 min-w-0" className="!text-xs !py-1.5">
          <option value="">Select a document to share...</option>
          {allDocs.filter(d => !sharedDocIds.has(d.id)).map(d => (
            <option key={d.id} value={d.id}>{d.title}</option>
          ))}
        </Select>
        <Select value={addDocPermission} onChange={e => setAddDocPermission(e.target.value)} wrapperClassName="w-36 shrink-0" className="!text-xs !py-1.5">
          {PERMISSIONS.map(p => <option key={p} value={p}>{p.replace('Can ', '')}</option>)}
        </Select>
        <Button size="sm" onClick={handleShare} disabled={!addDocId}><Plus size={13} /></Button>
      </div>

      {loading ? (
        <p className="text-xs text-zinc-400 py-2">Loading...</p>
      ) : sharedDocs.length === 0 ? (
        <EmptyState icon={FileText} title="No documents shared yet" description="Share a document with this department so members can access it" />
      ) : (
        <div className="bg-white border border-zinc-200 rounded-xl overflow-hidden">
          <table className="w-full ged-table text-sm">
            <thead>
              <tr className="bg-zinc-50 text-left">
                <th>Document</th>
                <th>Shared by</th>
                <th>Shared on</th>
                <th>Permission</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {sharedDocs.map(s => (
                <tr key={s.id}>
                  <td className="font-medium flex items-center gap-2">
                    <FileText size={14} className="text-zinc-400 shrink-0" />
                    <span className="truncate">{s.documentTitle}</span>
                  </td>
                  <td className="text-zinc-500">{s.sharedBy || '—'}</td>
                  <td className="text-zinc-500">{formatDate(s.sharedAt)}</td>
                  <td>
                    <Select value={s.permission} onChange={e => handlePermissionChange(s.id, e.target.value)} wrapperClassName="w-32" className="!text-[11px] !py-1">
                      {PERMISSIONS.map(p => <option key={p} value={p}>{p.replace('Can ', '')}</option>)}
                    </Select>
                  </td>
                  <td>
                    <button onClick={() => handleRemove(s.id)} className="p-1 hover:bg-red-50 rounded text-zinc-400 hover:text-red-500"><Trash2 size={13} /></button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

// ───────────────────────── Department detail (opened level) ─────────────────────────

function DepartmentDetail({ dept, allDepartments, roles, orgId, userId, onChanged, onOpen, onAddSub }: {
  dept: DepartmentDto
  allDepartments: DepartmentDto[]
  roles: DepartmentRoleDto[]
  orgId: number
  userId: string
  onChanged: () => void
  onOpen: (dept: DepartmentDto) => void
  onAddSub: () => void
}) {
  const toast = useToast()
  const [editingName, setEditingName] = useState(false)
  const [nameDraft, setNameDraft] = useState(dept.name)

  useEffect(() => { setNameDraft(dept.name); setEditingName(false) }, [dept.id, dept.name])

  const subDepartments = allDepartments.filter(d => d.parentId === dept.id)

  async function handleRename() {
    if (!nameDraft.trim() || nameDraft === dept.name) { setEditingName(false); return }
    try {
      await departmentsService.update(orgId, dept.id, { name: nameDraft.trim() })
      onChanged()
    } catch {} finally { setEditingName(false) }
  }

  async function handleDelete() {
    if (subDepartments.length > 0) {
      toast.error('Move or delete its sub-departments first')
      return
    }
    if (!window.confirm(`Delete "${dept.name}"?`)) return
    try {
      await departmentsService.remove(orgId, dept.id)
      onChanged()
    } catch {}
  }

  async function handleDeleteSub(sub: DepartmentDto) {
    if (sub.subDepartmentCount > 0) {
      toast.error('Move or delete its sub-departments first')
      return
    }
    if (!window.confirm(`Delete "${sub.name}"?`)) return
    try {
      await departmentsService.remove(orgId, sub.id)
      onChanged()
    } catch {}
  }

  return (
    <div className="animate-fade-in">
      <div className="flex items-start justify-between mb-2">
        <div className="flex items-center gap-3 min-w-0">
          <div className="w-12 h-12 rounded-xl flex items-center justify-center shrink-0" style={{ background: (dept.color || '#9ca3af') + '20' }}>
            <Folder size={22} style={{ color: dept.color || '#9ca3af' }} />
          </div>
          <div className="min-w-0">
            {editingName ? (
              <input
                value={nameDraft}
                onChange={e => setNameDraft(e.target.value)}
                onBlur={handleRename}
                onKeyDown={e => { if (e.key === 'Enter') handleRename(); if (e.key === 'Escape') { setNameDraft(dept.name); setEditingName(false) } }}
                autoFocus
                className="text-xl font-bold border-b border-primary outline-none bg-transparent"
              />
            ) : (
              <h2 className="text-xl font-bold flex items-center gap-2 group">
                {dept.name}
                <button onClick={() => setEditingName(true)} className="opacity-0 group-hover:opacity-100 text-zinc-400 hover:text-zinc-600">
                  <Pencil size={13} />
                </button>
              </h2>
            )}
            {dept.description && <p className="text-sm text-zinc-500 mt-0.5">{dept.description}</p>}
          </div>
        </div>
        <div className="flex items-center gap-2 shrink-0">
          <Button size="sm" variant="outline" onClick={onAddSub}><Plus size={13} className="mr-1" /> Sub-department</Button>
          <button onClick={handleDelete} className="p-2 hover:bg-red-50 rounded-lg text-zinc-400 hover:text-red-500"><Trash2 size={15} /></button>
        </div>
      </div>

      <div className="flex items-center gap-4 text-xs text-zinc-500 mb-6 ml-[60px]">
        <span className="flex items-center gap-1.5"><Users size={13} /> {dept.memberCount} member{dept.memberCount !== 1 ? 's' : ''}</span>
        {dept.responsibleName && (
          <span className="flex items-center gap-1.5">
            <Avatar name={dept.responsibleName} size="sm" className="w-5 h-5 text-[9px]" />
            Responsible: <span className="font-medium text-zinc-700">{dept.responsibleName}</span>
          </span>
        )}
      </div>

      {subDepartments.length > 0 && (
        <div className="mb-8">
          <h3 className="text-sm font-semibold flex items-center gap-1.5 mb-3"><FolderTree size={14} /> Sub-departments ({subDepartments.length})</h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
            {subDepartments.map(sub => (
              <DepartmentTile key={sub.id} dept={sub} onOpen={() => onOpen(sub)} onDelete={() => handleDeleteSub(sub)} />
            ))}
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <SharedDocsSection dept={dept} orgId={orgId} userId={userId} onChanged={onChanged} />
        <MembersSection dept={dept} roles={roles} orgId={orgId} onChanged={onChanged} />
      </div>
    </div>
  )
}

// ───────────────────────── Admin root ─────────────────────────

function AdminDepartmentsView({ orgId, userId, orgName }: { orgId: number; userId: string; orgName: string }) {
  const toast = useToast()
  const [departments, setDepartments] = useState<DepartmentDto[]>([])
  const [roles, setRoles] = useState<DepartmentRoleDto[]>([])
  const [loading, setLoading] = useState(true)
  const [path, setPath] = useState<DepartmentDto[]>([])
  const [createForm, setCreateForm] = useState<{ parentId: number | null; parentName: string | null } | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const [depts, roleList] = await Promise.all([
        departmentsService.list(orgId),
        departmentsService.listRoles(orgId),
      ])
      setDepartments(depts)
      setRoles(roleList)
    } catch {} finally { setLoading(false) }
  }, [orgId])

  useEffect(() => { load() }, [load])

  // keep breadcrumb path in sync with fresh data after edits
  useEffect(() => {
    if (path.length === 0 || departments.length === 0) return
    const refreshed = path.map(p => departments.find(d => d.id === p.id)).filter(Boolean) as DepartmentDto[]
    if (refreshed.length !== path.length) {
      setPath(refreshed) // a department in the path got deleted/moved away
    } else if (JSON.stringify(refreshed) !== JSON.stringify(path)) {
      setPath(refreshed)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [departments])

  const current = path[path.length - 1] || null
  const topLevel = departments.filter(d => d.parentId == null)

  function handleDeleteTopLevel(dept: DepartmentDto) {
    if (dept.subDepartmentCount > 0) {
      toast.error('Move or delete its sub-departments first')
      return
    }
    if (!window.confirm(`Delete "${dept.name}"?`)) return
    departmentsService.remove(orgId, dept.id).then(load).catch(() => {})
  }

  if (loading) return <div className="text-center py-12 text-zinc-400">Loading...</div>

  return (
    <div>
      {/* Breadcrumb */}
      <div className="flex items-center gap-1 text-sm mb-5 flex-wrap">
        <button onClick={() => setPath([])}
          className={cn('flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg font-medium',
            path.length === 0 ? 'bg-primary/10 text-primary' : 'text-zinc-500 hover:bg-zinc-100 hover:text-zinc-800')}>
          <Building2 size={14} /> {orgName}
        </button>
        {path.map((d, i) => (
          <span key={d.id} className="flex items-center gap-1">
            <ChevronRight size={14} className="text-zinc-300" />
            <button onClick={() => setPath(path.slice(0, i + 1))}
              className={cn('px-2.5 py-1.5 rounded-lg font-medium truncate max-w-[160px]',
                i === path.length - 1 ? 'bg-primary/10 text-primary' : 'text-zinc-500 hover:bg-zinc-100 hover:text-zinc-800')}>
              {d.name}
            </button>
          </span>
        ))}
      </div>

      <div className="flex items-center justify-between mb-1">
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            {path.length === 0 ? <><Home size={20} className="text-zinc-400" /> {orgName}</> : current!.name}
          </h1>
          <p className="text-zinc-500 text-sm">
            {path.length === 0
              ? 'Your organization\'s departments'
              : 'Sub-departments, members and shared documents'}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <RoleManager orgId={orgId} roles={roles} onChanged={load} />
          <Button size="sm" onClick={() => setCreateForm({ parentId: current?.id ?? null, parentName: current?.name ?? null })}>
            <Plus size={14} className="mr-1" /> New department
          </Button>
        </div>
      </div>

      <div className="h-4" />

      {createForm && (
        <CreateDepartmentForm
          parentId={createForm.parentId}
          parentName={createForm.parentName}
          orgId={orgId}
          onClose={() => setCreateForm(null)}
          onCreated={load}
        />
      )}

      {path.length === 0 ? (
        topLevel.length === 0 ? (
          <EmptyState icon={Building2} title="No departments yet" description="Create your first department to start modeling your org structure" />
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {topLevel.map(d => (
              <DepartmentTile key={d.id} dept={d} onOpen={() => setPath([d])} onDelete={() => handleDeleteTopLevel(d)} />
            ))}
          </div>
        )
      ) : (
        <DepartmentDetail
          dept={current!}
          allDepartments={departments}
          roles={roles}
          orgId={orgId}
          userId={userId}
          onChanged={load}
          onOpen={sub => setPath([...path, sub])}
          onAddSub={() => setCreateForm({ parentId: current!.id, parentName: current!.name })}
        />
      )}
    </div>
  )
}

// ───────────────────────── Member (read-only) view ─────────────────────────

function MyDepartmentsView({ orgId, orgName }: { orgId: number; orgName: string }) {
  const [departments, setDepartments] = useState<DepartmentDto[]>([])
  const [sharedDocs, setSharedDocs] = useState<DepartmentSharedDocDto[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      departmentsService.myDepartments(orgId),
      departmentsService.sharedWithMyDepartments(orgId),
    ]).then(([d, s]) => { setDepartments(d); setSharedDocs(s) })
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [orgId])

  if (loading) return <div className="text-center py-12 text-zinc-400">Loading...</div>

  return (
    <div className="max-w-3xl mx-auto space-y-8">
      <div>
        <h1 className="text-2xl font-bold mb-1 flex items-center gap-2"><Building2 size={20} className="text-zinc-400" /> {orgName}</h1>
        <p className="text-zinc-500 text-sm mb-6">Departments you belong to</p>
        {departments.length === 0 ? (
          <EmptyState icon={Building2} title="Not in any department" description="Ask an org admin to add you to a department" />
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            {departments.map(d => (
              <div key={d.id} className="doc-card p-4 flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg flex items-center justify-center shrink-0" style={{ background: (d.color || '#9ca3af') + '20' }}>
                  <Folder size={18} style={{ color: d.color || '#9ca3af' }} />
                </div>
                <div className="min-w-0">
                  <p className="font-semibold text-sm truncate">{d.name}</p>
                  <p className="text-xs text-zinc-400 truncate">{d.memberCount} member{d.memberCount !== 1 ? 's' : ''}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div>
        <h2 className="text-lg font-bold mb-3">Shared with my departments</h2>
        {sharedDocs.length === 0 ? (
          <p className="text-sm text-zinc-400">No documents shared yet</p>
        ) : (
          <div className="bg-white border border-zinc-200 rounded-xl overflow-hidden">
            <table className="w-full ged-table text-sm">
              <thead>
                <tr className="bg-zinc-50 text-left">
                  <th>Document</th><th>Department</th><th>Shared by</th><th>Shared on</th><th>Permission</th>
                </tr>
              </thead>
              <tbody>
                {sharedDocs.map(s => (
                  <tr key={s.id}>
                    <td className="font-medium">{s.documentTitle}</td>
                    <td className="text-zinc-500">{s.departmentName}</td>
                    <td className="text-zinc-500">{s.sharedBy || '—'}</td>
                    <td className="text-zinc-500">{formatDate(s.sharedAt)}</td>
                    <td><Badge variant="tag">{s.permission}</Badge></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}

export function DepartmentsPage() {
  const { orgId, isOrgAdmin, user, orgName } = useAuth()
  if (!orgId) return null
  const resolvedOrgName = orgName || 'My Organization'
  return isOrgAdmin
    ? <AdminDepartmentsView orgId={orgId} userId={user!.id} orgName={resolvedOrgName} />
    : <MyDepartmentsView orgId={orgId} orgName={resolvedOrgName} />
}
