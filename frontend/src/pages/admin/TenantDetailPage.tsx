import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { adminService, type TenantDto } from '@/services/admin.service'
import { ArrowLeft, Check, X } from 'lucide-react'

export function AdminTenantDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [tenant, setTenant] = useState<TenantDto | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!id) return
    setLoading(true)
    adminService.getTenant(Number(id))
      .then(setTenant)
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <p className="text-zinc-400">Loading...</p>
  if (!tenant) return <p className="text-red-500">Tenant not found</p>

  return (
    <div>
      <Link to="/admin/tenants" className="inline-flex items-center gap-1.5 text-sm text-zinc-500 hover:text-zinc-800 mb-6">
        <ArrowLeft size={14} /> Back to tenants
      </Link>

      <div className="flex items-center gap-4 mb-6">
        <div className="w-12 h-12 rounded-xl bg-[#e8622a]/10 flex items-center justify-center text-[#e8622a] font-bold text-lg">
          {tenant.name[0]}
        </div>
        <div>
          <h1 className="text-2xl font-bold">{tenant.name}</h1>
          <p className="text-zinc-500 text-sm">{tenant.slug}</p>
        </div>
        <span className={`ml-auto text-xs font-medium px-2.5 py-1 rounded-full ${tenant.isActive ? 'bg-emerald-50 text-emerald-600' : 'bg-zinc-100 text-zinc-500'}`}>
          {tenant.isActive ? 'Active' : 'Inactive'}
        </span>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div className="bg-white rounded-xl border border-zinc-200 p-5">
          <p className="text-xs text-zinc-400 uppercase font-medium mb-1">Tenant ID</p>
          <p className="text-lg font-bold">{tenant.id}</p>
        </div>
        <div className="bg-white rounded-xl border border-zinc-200 p-5">
          <p className="text-xs text-zinc-400 uppercase font-medium mb-1">Slug</p>
          <p className="text-lg font-medium">{tenant.slug}</p>
        </div>
        <div className="bg-white rounded-xl border border-zinc-200 p-5">
          <p className="text-xs text-zinc-400 uppercase font-medium mb-1">Created</p>
          <p className="text-lg font-medium">{tenant.createdDate ? new Date(tenant.createdDate).toLocaleDateString() : '-'}</p>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-zinc-200 p-5">
        <h2 className="font-bold mb-1">Actions</h2>
        <p className="text-sm text-zinc-500">Manage this tenant's users, entities, and documents from the main dashboard.</p>
      </div>
    </div>
  )
}
