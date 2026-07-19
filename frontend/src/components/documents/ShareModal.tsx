import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { sharesService, type CreateShareResult } from '@/services/shares.service'
import { Modal } from '@/components/ui/Modal'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Select } from '@/components/ui/Select'
import { Link2, Copy, Clock, Lock, Trash2, Check } from 'lucide-react'
import { useToast } from '@/contexts/ToastContext'

interface ShareModalProps {
  documentId: number
  open: boolean
  onClose: () => void
}

const SHARE_PERMISSIONS = ['VIEW', 'COMMENT', 'EDIT']

export function ShareModal({ documentId, open, onClose }: ShareModalProps) {
  const { orgId } = useAuth()
  const toast = useToast()
  const [shares, setShares] = useState<CreateShareResult[]>([])
  const [loading, setLoading] = useState(false)
  const [permission, setPermission] = useState('VIEW')
  const [password, setPassword] = useState('')
  const [expiresIn, setExpiresIn] = useState('')
  const [creating, setCreating] = useState(false)
  const [copiedId, setCopiedId] = useState<number | null>(null)

  async function load() {
    if (!open) return
    setLoading(true)
    try {
      const res = await sharesService.listShares(0, 50)
      setShares(res.content.map(s => ({
        id: s.id,
        token: '',
        permission: s.permission,
        expiresAt: s.expiresAt || null,
        requiresPassword: s.requiresPassword ?? null,
        url: '',
      })))
    } catch {} finally { setLoading(false) }
  }

  useEffect(() => { load() }, [open])

  async function handleCreate() {
    if (!orgId) return
    setCreating(true)
    try {
      const expiresAt = expiresIn
        ? new Date(Date.now() + parseInt(expiresIn) * 86400000).toISOString()
        : undefined
      const result = await sharesService.createShare(documentId, orgId, permission, expiresAt, password || undefined)
      setShares(prev => [...prev, result])
      toast.success('Share link created')
      setPassword('')
      setExpiresIn('')
    } catch {} finally { setCreating(false) }
  }

  async function handleRevoke(shareId: number) {
    if (!orgId) return
    try {
      await sharesService.revokeShare(shareId, orgId)
      setShares(prev => prev.filter(s => s.id !== shareId))
      toast.success('Share link revoked')
    } catch {}
  }

  function copyToClipboard(url: string, id: number) {
    navigator.clipboard.writeText(window.location.origin + url)
    setCopiedId(id)
    setTimeout(() => setCopiedId(null), 2000)
  }

  return (
    <Modal open={open} onClose={onClose} title="Share Document" size="md">
      <div className="space-y-5">

        {/* Create new share */}
        <div className="bg-zinc-50 rounded-xl p-4 space-y-3">
          <h4 className="text-sm font-semibold flex items-center gap-2"><Link2 size={14} /> Create share link</h4>
          <div className="grid grid-cols-3 gap-2">
            {SHARE_PERMISSIONS.map(p => (
              <button key={p} onClick={() => setPermission(p)}
                className={`text-xs px-3 py-1.5 rounded-lg border text-center font-medium transition-colors ${
                  permission === p
                    ? 'bg-primary text-white border-primary'
                    : 'bg-white text-zinc-600 border-zinc-200 hover:border-zinc-300'
                }`}>
                {p.charAt(0) + p.slice(1).toLowerCase()}
              </button>
            ))}
          </div>
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <Lock size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" />
              <input type="text" placeholder="Password (optional)"
                value={password} onChange={e => setPassword(e.target.value)}
                className="w-full pl-8 pr-3 py-2 text-sm border border-zinc-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div className="relative w-32">
              <Clock size={14} className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400 z-10" />
              <Select value={expiresIn} onChange={e => setExpiresIn(e.target.value)} className="pl-8">
                <option value="">No expiry</option>
                <option value="1">1 day</option>
                <option value="7">7 days</option>
                <option value="30">30 days</option>
              </Select>
            </div>
          </div>
          <Button size="sm" onClick={handleCreate} disabled={creating} className="w-full">
            {creating ? 'Creating...' : 'Generate Link'}
          </Button>
        </div>

        {/* Existing shares */}
        <div>
          <h4 className="text-sm font-semibold mb-2">Active share links</h4>
          {loading ? (
            <p className="text-sm text-zinc-400">Loading...</p>
          ) : shares.length === 0 ? (
            <p className="text-sm text-zinc-400">No share links yet</p>
          ) : (
            <div className="space-y-2 max-h-60 overflow-y-auto">
              {shares.map(s => {
                const shareUrl = s.url || `/api/v1/public/shares/${s.token}`
                return (
                  <div key={s.id} className="flex items-center justify-between bg-zinc-50 rounded-lg px-3 py-2">
                    <div className="min-w-0 flex-1">
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-medium">
                          {s.permission}
                        </span>
                        {s.requiresPassword && <Lock size={10} className="text-zinc-400" />}
                        {s.expiresAt && (
                          <span className="text-[10px] text-zinc-400">
                            Expires {new Date(s.expiresAt).toLocaleDateString()}
                          </span>
                        )}
                      </div>
                    </div>
                    <div className="flex gap-1 shrink-0">
                      <button onClick={() => copyToClipboard(shareUrl, s.id)}
                        className="p-1.5 hover:bg-zinc-200 rounded text-zinc-500" title="Copy link">
                        {copiedId === s.id ? <Check size={13} className="text-green-500" /> : <Copy size={13} />}
                      </button>
                      <button onClick={() => handleRevoke(s.id)}
                        className="p-1.5 hover:bg-red-50 rounded text-red-400" title="Revoke">
                        <Trash2 size={13} />
                      </button>
                    </div>
                  </div>
                )
              })}
            </div>
          )}
        </div>
      </div>
    </Modal>
  )
}
