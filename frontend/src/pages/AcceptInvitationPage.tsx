import { useState, useEffect } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { organizationService } from '@/services/organization.service'
import { Button } from '@/components/ui/Button'
import { ArrowRight, CheckCircle2, AlertCircle, Building2, Loader2 } from 'lucide-react'

export function AcceptInvitationPage() {
  const { token } = useParams<{ token: string }>()
  const { isAuthenticated, user, refreshOrgs } = useAuth()
  const navigate = useNavigate()
  const [accepting, setAccepting] = useState(false)
  const [accepted, setAccepted] = useState(false)
  const [error, setError] = useState('')

  async function handleAccept() {
    if (!token) return
    setAccepting(true)
    setError('')
    try {
      await organizationService.acceptInvitation(token)
      await refreshOrgs()
      setAccepted(true)
      setTimeout(() => navigate('/org/select'), 1500)
    } catch (err: any) {
      const msg = err?.response?.data?.message || err?.message || 'Failed to accept invitation. The link may be invalid or expired.'
      setError(msg)
    } finally { setAccepting(false) }
  }

  if (!token) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="text-center max-w-md p-8">
          <AlertCircle size={40} className="text-red-400 mx-auto mb-4" />
          <h2 className="text-xl font-bold mb-2">Invalid link</h2>
          <p className="text-zinc-500 text-sm">This invitation link is invalid.</p>
        </div>
      </div>
    )
  }

  if (accepted) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="text-center max-w-md p-8">
          <CheckCircle2 size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold mb-2">You've joined the organization!</h2>
          <p className="text-zinc-500 text-sm mb-4">Redirecting to dashboard...</p>
          <Loader2 size={20} className="animate-spin mx-auto text-zinc-400" />
        </div>
      </div>
    )
  }

  if (!isAuthenticated) {
    const redirectTo = `/invite/${token}`
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50 px-6">
        <div className="text-center max-w-md">
          <div className="w-16 h-16 rounded-2xl bg-orange-100 flex items-center justify-center mx-auto mb-6">
            <Building2 size={32} className="text-[#e8622a]" />
          </div>
          <h1 className="text-2xl font-bold mb-2">You're invited!</h1>
          <p className="text-zinc-500 text-sm mb-8 max-w-sm mx-auto">
            Someone has invited you to join their organization on Docibly. Sign in or create an account to accept.
          </p>
          <div className="flex flex-col gap-3">
            <Link to={`/login?redirect=${redirectTo}`}>
              <Button variant="cta" size="lg" className="w-full">Sign in to accept</Button>
            </Link>
            <Link to={`/register?redirect=${redirectTo}`}>
              <Button variant="outline" size="lg" className="w-full">Create an account</Button>
            </Link>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-zinc-50 px-6">
      <div className="text-center max-w-md">
        <div className="w-16 h-16 rounded-2xl bg-orange-100 flex items-center justify-center mx-auto mb-6">
          <Building2 size={32} className="text-[#e8622a]" />
        </div>
        <h1 className="text-2xl font-bold mb-2">Organization invitation</h1>
        <p className="text-zinc-500 text-sm mb-2">
          You've been invited to join a workspace on Docibly.
        </p>
        <p className="text-zinc-400 text-xs mb-8">
          Signed in as <span className="font-medium text-zinc-600">{user?.email}</span>
        </p>

        {error && (
          <div className="bg-red-50 border border-red-200 rounded-xl p-4 mb-6 text-sm text-red-700 flex items-start gap-3 text-left">
            <AlertCircle size={16} className="mt-0.5 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <div className="flex flex-col gap-3">
          <Button onClick={handleAccept} variant="cta" size="lg" loading={accepting} className="w-full">
            Accept invitation <ArrowRight size={16} />
          </Button>
          <Link to="/dashboard">
            <Button variant="outline" size="lg" className="w-full">Decline</Button>
          </Link>
        </div>
      </div>
    </div>
  )
}
