import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { adminService, type TenantDto } from '@/services/admin.service'
import { Building2, Plus, Search, Check, X } from 'lucide-react'

export function AdminTenantsPage() {
  const [tenants, setTenants] = useState<TenantDto[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ name: '', slug: '' })
  const [error, setError] = useState('')

  async function load() {
    setLoading(true)
    try {
      const res = await adminService.getTenants(0, 100)
      setTenants(res.content)
    } catch { setError('Failed to load tenants') }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [])

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault()
    setError('')
    try {
      await adminService.createTenant(form)
      setForm({ name: '', slug: '' })
      setShowForm(false)
      load()
    } catch { setError('Failed to create tenant') }
  }

  async function handleToggle(id: number, current: boolean) {
    try {
      await adminService.updateTenant(id, { isActive: !current } as any)
      load()
    } catch {}
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold">Tenants</h1>
          <p className="text-zinc-500 text-sm mt-0.5">Manage organizations on the platform</p>
        </div>
        <button onClick={() => setShowForm(!showForm)} className="inline-flex items-center gap-2 px-4 py-2.5 bg-[#e8622a] text-white rounded-lg text-sm font-medium hover:bg-[#d15520] transition-colors">
          <Plus size={16} /> New Tenant
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleCreate} className="bg-white rounded-xl border border-zinc-200 p-5 mb-6 grid grid-cols-3 gap-4">
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Name</label>
            <input value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" placeholder="Acme Corp" required />
          </div>
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1 block">Slug</label>
            <input value={form.slug} onChange={e => setForm(p => ({ ...p, slug: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" placeholder="acme-corp" required />
          </div>
          <div className="flex items-end gap-2">
            <button type="submit" className="px-4 py-2 bg-[#e8622a] text-white rounded-lg text-sm font-medium">Create</button>
            <button type="button" onClick={() => setShowForm(false)} className="px-4 py-2 border border-zinc-200 rounded-lg text-sm">Cancel</button>
          </div>
          {error && <p className="text-red-500 text-xs col-span-3">{error}</p>}
        </form>
      )}

      {error && !showForm && <p className="text-red-500 text-sm mb-4">{error}</p>}

      <div className="bg-white rounded-xl border border-zinc-200 overflow-hidden">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-zinc-100">
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Name</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Slug</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Status</th>
              <th className="text-left px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Created</th>
              <th className="text-right px-5 py-3 text-xs font-medium text-zinc-400 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={5} className="text-center py-10 text-zinc-400">Loading...</td></tr>
            ) : tenants.length === 0 ? (
              <tr><td colSpan={5} className="text-center py-10 text-zinc-400">No tenants yet</td></tr>
            ) : tenants.map(t => (
              <tr key={t.id} className="border-b border-zinc-50 hover:bg-zinc-50/50">
                <td className="px-5 py-3 font-medium">{t.name}</td>
                <td className="px-5 py-3 text-zinc-500">{t.slug}</td>
                <td className="px-5 py-3">
                  <span className={`inline-flex items-center gap-1 text-xs font-medium px-2 py-0.5 rounded-full ${t.isActive ? 'bg-emerald-50 text-emerald-600' : 'bg-zinc-100 text-zinc-500'}`}>
                    {t.isActive ? <Check size={12} /> : <X size={12} />}
                    {t.isActive ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td className="px-5 py-3 text-zinc-500 text-xs">{t.createdDate ? new Date(t.createdDate).toLocaleDateString() : '-'}</td>
                <td className="px-5 py-3 text-right">
                  <div className="flex items-center justify-end gap-2">
                    <Link to={`/admin/tenants/${t.id}`} className="px-3 py-1.5 text-xs border border-zinc-200 rounded-lg hover:bg-zinc-50">View</Link>
                    <button onClick={() => handleToggle(t.id!, t.isActive!)} className="px-3 py-1.5 text-xs border border-zinc-200 rounded-lg hover:bg-zinc-50">
                      {t.isActive ? 'Deactivate' : 'Activate'}
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
