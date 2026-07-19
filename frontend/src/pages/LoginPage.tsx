import { useState } from 'react'
import { useNavigate, useSearchParams, Link } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Mail, Lock, ArrowRight } from 'lucide-react'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')
    setLoading(true)
    const success = await login(email, password)
    setLoading(false)
    if (success) {
      const redirect = searchParams.get('redirect')
      navigate(redirect || '/dashboard')
    } else { setError('Invalid email or password') }
  }

  return (
    <div className="min-h-screen flex">
      {/* Left panel */}
      <div className="hidden lg:flex lg:w-1/2 relative bg-[#1a1a2e]">
        <div className="relative z-10 flex flex-col justify-center px-16 max-w-xl mx-auto">
          <div className="flex items-center gap-2.5 mb-14">
            <img
              src="/ged-logo.svg"
              alt="Docibly"
              className="w-10 h-10 rounded-xl object-cover shrink-0 shadow-lg shadow-[#e8622a]/30"
            />
            <div>
              <span className="text-white font-bold text-xl">Docibly</span>
              <span className="text-white/30 text-xs block">Document Platform</span>
            </div>
          </div>

          <h2 className="text-3xl font-bold text-white leading-tight mb-5">
            Your documents,<br />
            <span className="text-primary">organized</span> and accessible.
          </h2>
          <p className="text-white/40 text-base leading-relaxed">
            A centralized platform to store, search, and share documents across your organization.
          </p>

          <div className="mt-14 flex items-center gap-5 text-white/20 text-sm">
            <div className="flex items-center gap-1.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
              <span>256-bit encryption</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-400" />
              <span>SOC 2 compliant</span>
            </div>
          </div>
        </div>
      </div>

      {/* Right panel */}
      <div className="flex-1 flex items-center justify-center px-6 py-12 bg-zinc-50">
        <div className="w-full max-w-sm">
          <div className="lg:hidden flex items-center gap-2.5 mb-10 justify-center">
            <img
              src="/ged-logo.svg"
              alt="Docibly"
              className="w-8 h-8 rounded-lg object-cover shrink-0"
            />
            <span className="text-lg font-bold">Docibly</span>
          </div>

          <h1 className="text-2xl font-bold mb-1">Welcome back</h1>
          <p className="text-zinc-500 text-sm mb-7">Sign in to your workspace</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input label="Email" type="email" placeholder="you@company.com" value={email} onChange={e => setEmail(e.target.value)} icon={<Mail size={15} />} required />
            <Input label="Password" type="password" placeholder="\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022" value={password} onChange={e => setPassword(e.target.value)} icon={<Lock size={15} />} error={error} required />

            <div className="flex items-center justify-between">
              <label className="flex items-center gap-2 cursor-pointer">
                <input type="checkbox" className="checkbox checkbox-primary checkbox-sm rounded" />
                <span className="text-sm text-zinc-500">Remember me</span>
              </label>
              <button type="button" className="text-sm text-primary font-medium hover:underline">Forgot password?</button>
            </div>

            <Button type="submit" variant="cta" size="lg" loading={loading} className="w-full">
              Sign in <ArrowRight size={16} />
            </Button>
          </form>

          <p className="text-center text-sm text-zinc-400 mt-7">
            Don't have an account? <Link to={searchParams.get('redirect') ? `/register?redirect=${searchParams.get('redirect')}` : '/register'} className="text-primary font-medium hover:underline">Sign up</Link>
          </p>
        </div>
      </div>
    </div>
  )
}
