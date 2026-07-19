import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { auditService } from '@/services/audit.service'
import type { AuditLogDto } from '@/types/api'
import { Button } from '@/components/ui/Button'
import { Shield, ChevronLeft, ChevronRight, Clock, User as UserIcon, FileText, Hash, RefreshCw, ShieldOff } from 'lucide-react'

export function AuditLogPage() {
  const { orgId, hasPermission, authReady } = useAuth()
  const [logs, setLogs] = useState<AuditLogDto[]>([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const size = 20

  async function load(p: number) {
    if (!orgId) return
    setLoading(true)
    try {
      const res = await auditService.getOrgAuditLogs(orgId, p, size)
      setLogs(res.content)
      setTotalPages(res.totalPages)
      setPage(res.page)
    } catch { setLogs([]) }
    setLoading(false)
  }

  useEffect(() => { load(page) }, [orgId])

  function actionBadge(action: string) {
    const colors: Record<string, string> = {
      DOCUMENT_CREATED: 'bg-green-50 text-green-700',
      DOCUMENT_UPLOADED: 'bg-green-50 text-green-700',
      DOCUMENT_UPDATED: 'bg-blue-50 text-blue-700',
      DOCUMENT_DELETED: 'bg-red-50 text-red-700',
      DOCUMENT_DOWNLOADED: 'bg-zinc-50 text-zinc-700',
      DOCUMENT_SHARED: 'bg-purple-50 text-purple-700',
      DOCUMENT_COMMENTED: 'bg-cyan-50 text-cyan-700',
      MEMBER_INVITED: 'bg-orange-50 text-orange-700',
      MEMBER_REMOVED: 'bg-red-50 text-red-700',
      MEMBER_ROLE_CHANGED: 'bg-yellow-50 text-yellow-700',
    }
    return (
      <span className={`text-[10px] px-2 py-0.5 rounded-full font-semibold ${colors[action] || 'bg-zinc-50 text-zinc-600'}`}>
        {action.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase())}
      </span>
    )
  }

  if (authReady && !hasPermission('org.audit')) {
    return (
      <div className="max-w-5xl mx-auto flex flex-col items-center justify-center min-h-64 gap-3">
        <ShieldOff size={40} className="text-zinc-300" />
        <h2 className="text-lg font-semibold text-zinc-700">Access Denied</h2>
        <p className="text-sm text-zinc-500">You don't have permission to view audit logs in this organization.</p>
      </div>
    )
  }

  return (
    <div className="max-w-5xl mx-auto space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-xl font-bold flex items-center gap-2">
            <Shield size={20} className="text-primary" />
            Audit Log
          </h2>
          <p className="text-sm text-zinc-500 mt-0.5">Track all activity across your organization</p>
        </div>
        <Button variant="outline" size="sm" onClick={() => load(page)}><RefreshCw size={13} /> Refresh</Button>
      </div>

      <div className="bg-white rounded-xl border border-zinc-200 overflow-hidden">
        {loading ? (
          <div className="p-8 text-center text-zinc-400">Loading audit logs...</div>
        ) : logs.length === 0 ? (
          <div className="p-8 text-center text-zinc-400">No audit logs found</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-zinc-100 bg-zinc-50/50">
                  <th className="text-left px-4 py-3 text-xs font-semibold text-zinc-500">Action</th>
                  <th className="text-left px-4 py-3 text-xs font-semibold text-zinc-500">Actor</th>
                  <th className="text-left px-4 py-3 text-xs font-semibold text-zinc-500">Target</th>
                  <th className="text-left px-4 py-3 text-xs font-semibold text-zinc-500">Date</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-zinc-100">
                {logs.map(log => (
                  <tr key={log.id} className="hover:bg-zinc-50/50 transition-colors">
                    <td className="px-4 py-3">{actionBadge(log.action)}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1.5 text-xs text-zinc-600">
                        <UserIcon size={11} className="text-zinc-400" />
                        {log.actorEmail || log.actorUserId || '—'}
                      </div>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1.5 text-xs text-zinc-600">
                        <FileText size={11} className="text-zinc-400" />
                        {log.targetEntityType}
                        <Hash size={10} className="text-zinc-300" />
                        <span className="text-zinc-400">#{log.targetEntityId}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1.5 text-xs text-zinc-500">
                        <Clock size={11} />
                        {log.createdDate ? new Date(log.createdDate).toLocaleString() : '—'}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {totalPages > 1 && (
          <div className="flex items-center justify-between px-4 py-3 border-t border-zinc-100 bg-zinc-50/50">
            <span className="text-xs text-zinc-500">Page {page + 1} of {totalPages}</span>
            <div className="flex gap-1">
              <Button variant="outline" size="sm" disabled={page === 0} onClick={() => load(page - 1)}>
                <ChevronLeft size={13} /> Previous
              </Button>
              <Button variant="outline" size="sm" disabled={page >= totalPages - 1} onClick={() => load(page + 1)}>
                Next <ChevronRight size={13} />
              </Button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
