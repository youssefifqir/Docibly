import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { organizationService, type MemberInfo } from '@/services/organization.service'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Select } from '@/components/ui/Select'
import { UserPlus, Shield, ShieldOff, Trash2, Mail } from 'lucide-react'
import { useToast } from '@/contexts/ToastContext'

const ROLE_OPTIONS = ['OWNER', 'ADMIN', 'MEMBER', 'VIEWER']

export function MembersPage() {
  const { orgId } = useAuth()
  const toast = useToast()
  const [members, setMembers] = useState<MemberInfo[]>([])
  const [loading, setLoading] = useState(true)
  const [inviteEmail, setInviteEmail] = useState('')
  const [inviteRole, setInviteRole] = useState('MEMBER')
  const [inviting, setInviting] = useState(false)

  async function load() {
    if (!orgId) return
    setLoading(true)
    try {
      const data = await organizationService.listMembers(orgId)
      setMembers(data)
    } catch {} finally { setLoading(false) }
  }

  useEffect(() => { load() }, [orgId])

  async function handleInvite(e: React.FormEvent) {
    e.preventDefault()
    if (!orgId || !inviteEmail.trim()) return
    setInviting(true)
    try {
      await organizationService.inviteMember(orgId, inviteEmail.trim(), inviteRole)
      toast.success(`Invitation sent to ${inviteEmail.trim()}`)
      setInviteEmail('')
      load()
    } catch {} finally { setInviting(false) }
  }

  async function handleRemove(memberId: number, email: string | null) {
    if (!orgId) return
    if (!window.confirm(`Remove ${email || 'this member'} from the workspace?`)) return
    try {
      await organizationService.removeMember(orgId, memberId)
      toast.success('Member removed')
      load()
    } catch {}
  }

  const isOwner = (role: string) => role === 'OWNER'

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-2">Members</h1>
      <p className="text-sm text-zinc-500 mb-8">Manage who has access to your workspace</p>

      <div className="bg-white border rounded-xl p-5 mb-8">
        <h2 className="font-semibold text-sm mb-4 flex items-center gap-2"><UserPlus size={16} /> Invite Member</h2>
        <form onSubmit={handleInvite} className="flex items-end gap-3">
          <div className="flex-1">
            <Input label="Email address" type="email" placeholder="colleague@company.com"
              value={inviteEmail} onChange={e => setInviteEmail(e.target.value)} />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1.5">Role</label>
            <Select value={inviteRole} onChange={e => setInviteRole(e.target.value)}>
              {ROLE_OPTIONS.filter(r => r !== 'OWNER').map(r => (
                <option key={r} value={r}>{r.charAt(0) + r.slice(1).toLowerCase()}</option>
              ))}
            </Select>
          </div>
          <Button type="submit" disabled={!inviteEmail.trim() || inviting}>
            {inviting ? 'Sending...' : 'Invite'}
          </Button>
        </form>
      </div>

      {loading ? (
        <div className="text-center py-12 text-zinc-400">Loading...</div>
      ) : members.length === 0 ? (
        <div className="text-center py-12 text-zinc-400">No members yet</div>
      ) : (
        <div className="bg-white border rounded-xl overflow-hidden">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b bg-zinc-50 text-left">
                <th className="px-4 py-3 font-medium text-zinc-500">Name</th>
                <th className="px-4 py-3 font-medium text-zinc-500">Email</th>
                <th className="px-4 py-3 font-medium text-zinc-500">Role</th>
                <th className="px-4 py-3 font-medium text-zinc-500">Joined</th>
                <th className="px-4 py-3 font-medium text-zinc-500">Actions</th>
              </tr>
            </thead>
            <tbody>
              {members.map(m => (
                <tr key={m.id} className="border-b last:border-0 hover:bg-zinc-50">
                  <td className="px-4 py-3 font-medium">
                    {m.firstName || m.lastName ? `${m.firstName || ''} ${m.lastName || ''}`.trim() : '—'}
                  </td>
                  <td className="px-4 py-3 text-zinc-500">{m.email || '—'}</td>
                  <td className="px-4 py-3">
                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${
                      isOwner(m.role) ? 'bg-yellow-50 text-yellow-700' :
                      m.role === 'ADMIN' ? 'bg-blue-50 text-blue-700' :
                      'bg-zinc-50 text-zinc-600'
                    }`}>{m.role}</span>
                  </td>
                  <td className="px-4 py-3 text-zinc-500">
                    {m.joinedAt ? new Date(m.joinedAt).toLocaleDateString() : '—'}
                  </td>
                  <td className="px-4 py-3">
                    {!isOwner(m.role) && (
                      <button onClick={() => handleRemove(m.id, m.email)}
                        className="p-1.5 hover:bg-red-50 rounded text-red-400 hover:text-red-600 transition-colors"
                        title="Remove member">
                        <Trash2 size={14} />
                      </button>
                    )}
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
