import { useState } from 'react'
import { useNavigate, useSearchParams, Link } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { authService } from '@/services/auth.service'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Mail, Lock, User, Phone, ArrowRight, CheckCircle2 } from 'lucide-react'

export function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', phoneNumber: '', password: '', confirmPassword: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [success, setSuccess] = useState(false)
  const [token, setToken] = useState('')
  const [verifying, setVerifying] = useState(false)
  const [verified, setVerified] = useState(false)
  const [verifyError, setVerifyError] = useState('')

  function update(field: string, value: string) {
    setForm(prev => ({ ...prev, [field]: value }))
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match')
      return
    }
    setLoading(true)
    try {
      await register(form)
      setSuccess(true)
    } catch {
      setError('Registration failed. Email may already be in use.')
    } finally {
      setLoading(false)
    }
  }

  async function handleVerify() {
    if (!token.trim()) return
    setVerifying(true)
    setVerifyError('')
    try {
      await authService.verifyEmail(token.trim())
      setVerified(true)
    } catch {
      setVerifyError('Invalid or expired token.')
    } finally {
      setVerifying(false)
    }
  }

  if (verified) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="text-center max-w-sm">
          <CheckCircle2 size={40} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-2xl font-bold mb-3">Email verified</h2>
          <p className="text-zinc-500 mb-6">You can now sign in to your account.</p>
          <Link to="/login" className="text-primary font-medium hover:underline">Sign in</Link>
        </div>
      </div>
    )
  }

  if (success) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="max-w-sm">
          <h2 className="text-2xl font-bold mb-3">Check your email</h2>
          <p className="text-zinc-500 mb-4">We sent a verification email to <strong>{form.email}</strong>. Paste the token below to verify.</p>
          <Input label="Verification token" value={token} onChange={e => setToken(e.target.value)} placeholder="Paste your token here" />
          {verifyError && <p className="text-red-500 text-sm mt-1">{verifyError}</p>}
          <Button onClick={handleVerify} loading={verifying} variant="cta" size="lg" className="w-full mt-4">Verify email</Button>
          <div className="text-center mt-4">
            <Link to={searchParams.get('redirect') ? `/login?redirect=${searchParams.get('redirect')}` : '/login'} className="text-sm text-zinc-400 hover:underline">Skip — sign in later</Link>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-12 bg-zinc-50">
      <div className="w-full max-w-sm">
        <div className="flex items-center gap-2.5 mb-10 justify-center">
          <img src="/ged-logo.svg" alt="Docibly" className="w-8 h-8 rounded-lg object-cover shrink-0" />
          <span className="text-lg font-bold">Docibly</span>
        </div>

        <h1 className="text-2xl font-bold mb-1">Create an account</h1>
        <p className="text-zinc-500 text-sm mb-7">Join your organization's workspace</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <Input label="First name" placeholder="John" value={form.firstName} onChange={e => update('firstName', e.target.value)} icon={<User size={15} />} required />
            <Input label="Last name" placeholder="Doe" value={form.lastName} onChange={e => update('lastName', e.target.value)} required />
          </div>
          <Input label="Email" type="email" placeholder="you@company.com" value={form.email} onChange={e => update('email', e.target.value)} icon={<Mail size={15} />} required />
          <Input label="Phone" type="tel" placeholder="+1 234 567 890" value={form.phoneNumber} onChange={e => update('phoneNumber', e.target.value)} icon={<Phone size={15} />} />
          <Input label="Password" type="password" placeholder="\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022" value={form.password} onChange={e => update('password', e.target.value)} icon={<Lock size={15} />} error={error} required />
          <Input label="Confirm password" type="password" placeholder="\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022" value={form.confirmPassword} onChange={e => update('confirmPassword', e.target.value)} icon={<Lock size={15} />} required />

          <Button type="submit" variant="cta" size="lg" loading={loading} className="w-full">
            Create account <ArrowRight size={16} />
          </Button>
        </form>

        <p className="text-center text-sm text-zinc-400 mt-7">
          Already have an account? <Link to={searchParams.get('redirect') ? `/login?redirect=${searchParams.get('redirect')}` : '/login'} className="text-primary font-medium hover:underline">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
