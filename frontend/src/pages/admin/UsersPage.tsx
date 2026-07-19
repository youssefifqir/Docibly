import { useState, useEffect } from 'react'
import { adminService } from '@/services/admin.service'
import api from '@/services/api'
import { Select } from '@/components/ui/Select'
import { Check, X, Plus, UserPlus } from 'lucide-react'

const ROLES = ['ROLE_SUPER_ADMIN', 'ROLE_TENANT_ADMIN', 'ROLE_DEPARTMENT_MANAGER', 'ROLE_MEMBER', 'ROLE_VIEWER', 'ROLE_GUEST']

export function AdminUsersPage() {
  const [users, setUsers] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [tenants, setTenants] = useState<any[]>([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ email: '', password: '', firstName: '', lastName: '', role: 'ROLE_TENANT_ADMIN', tenantId: '' })
  const [formError, setFormError] = useState('')

  async function load() {
    setLoading(true)
    try {
      const [userRes, tenantRes] = await Promise.all([adminService.getUsers(0, 100), adminService.getTenants(0, 100)])
      setUsers(userRes.content)
      setTenants(tenantRes.content)
    } catch {}
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [])

  async function handleToggle(user: any) {
    try {
      if (user.enabled) { await adminService.deactivateUser(user.id) }
      else { await adminService.activateUser(user.id) }
      load()
    } catch {}
  }

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault()
    setFormError('')
    try {
      await api.post('/admin/users', {
        email: form.email,
        password: form.password,
        firstName: form.firstName,
        lastName: form.lastName,
        role: form.role,
        tenantId: form.tenantId ? Number(form.tenantId) : null,
      })
      setShowForm(false)
      setForm({ email: '', password: '', firstName: '', lastName: '', role: 'ROLE_TENANT_ADMIN', tenantId: '' })
      load()
    } catch (err: any) {
      setFormError(err?.response?.data?.message || 'Failed to create user')
    }
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold">Users</h1>
          <p className="text-zinc-500 text-sm mt-0.5">Platform-wide user management</p>
        </div>
        <button onClick={() => setShowForm(!showForm)} className="inline-flex items-center gap-2 px-4 py-2.5 bg-[#e8622a] text-white rounded-lg text-sm font-medium hover:bg-[#d15520] transition-colors">
          <UserPlus size={16} /> Create User
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleCreate} className="bg-white rounded-xl border border-zinc-200 p-5 mb-6 grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Email</label>
            <input value={form.email} onChange={e => setForm(p => ({ ...p, email: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" type="email" required />
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Password</label>
            <input value={form.password} onChange={e => setForm(p => ({ ...p, password: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" type="password" required />
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">First Name</label>
            <input value={form.firstName} onChange={e => setForm(p => ({ ...p, firstName: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" required />
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Last Name</label>
            <input value={form.lastName} onChange={e => setForm(p => ({ ...p, lastName: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" required />
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Role</label>
            <Select value={form.role} onChange={e => setForm(p => ({ ...p, role: e.target.value }))}>
              {ROLES.map(r => <option key={r} value={r}>{r.replace('ROLE_', '')}</option>)}
            </Select>
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Tenant (optional)</label>
            <Select value={form.tenantId} onChange={e => setForm(p => ({ ...p, tenantId: e.target.value }))}>
              <option value="">No tenant (SUPER_ADMIN)</option>
              {tenants.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
            </Select>
          </div>
          <div className="col-span-full flex items-center gap-2">
            <button type="submit" className="px-4 py-2 bg-[#e8622a] text-white rounded-lg text-sm font-medium">Create</button>
            <button type="button" onClick={() => setShowForm(false)} className="px-4 py-2 border border-zinc-200 rounded-lg text-sm">Cancel</button>
          </div>
          {formError && <p className="text-red-500 text-xs col-span-full">{formError}</p>}
        </form>
      )}

      <div className="bg-white rounded-xl border border-zinc-200 overflow-hidden">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-zinc-100">
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Name</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Email</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Roles</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Status</th>
              <th className="text-right px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={5} className="text-center py-10 text-zinc-400">Loading...</td></tr>
            ) : users.length === 0 ? (
              <tr><td colSpan={5} className="text-center py-10 text-zinc-400">No users found</td></tr>
            ) : users.map(u => (
              <tr key={u.id} className="border-b border-zinc-50 hover:bg-zinc-50/50">
                <td className="px-5 py-3 font-medium">{u.firstName} {u.lastName}</td>
                <td className="px-5 py-3 text-zinc-500">{u.email}</td>
                <td className="px-5 py-3">
                  <div className="flex flex-wrap gap-1">
                    {(u.roles || []).map((r: string) => (
                      <span key={r} className="text-[10px] font-medium px-1.5 py-0.5 rounded bg-zinc-100 text-zinc-600">{r.replace('ROLE_', '')}</span>
                    ))}
                  </div>
                </td>
                <td className="px-5 py-3">
                  <span className={`inline-flex items-center gap-1 text-xs font-medium px-2 py-0.5 rounded-full ${u.enabled ? 'bg-emerald-50 text-emerald-600' : 'bg-red-50 text-red-600'}`}>
                    {u.enabled ? <Check size={12} /> : <X size={12} />}
                    {u.enabled ? 'Active' : 'Disabled'}
                  </span>
                </td>
                <td className="px-5 py-3 text-right">
                  <button onClick={() => handleToggle(u)} className={`px-3 py-1.5 text-xs border rounded-lg ${u.enabled ? 'border-red-200 text-red-600 hover:bg-red-50' : 'border-emerald-200 text-emerald-600 hover:bg-emerald-50'}`}>
                    {u.enabled ? 'Deactivate' : 'Activate'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
