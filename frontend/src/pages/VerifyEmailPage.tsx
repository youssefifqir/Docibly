import { useEffect, useState } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import { authService } from '@/services/auth.service'

export function VerifyEmailPage() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token')
  const [status, setStatus] = useState<'verifying' | 'success' | 'error'>('verifying')
  const [message, setMessage] = useState('')

  useEffect(() => {
    if (!token) {
      setStatus('error')
      setMessage('No verification token provided.')
      return
    }
    authService.verifyEmail(token)
      .then(() => {
        setStatus('success')
        setMessage('Email verified! You can now sign in.')
      })
      .catch(() => {
        setStatus('error')
        setMessage('Token invalid or expired. Try registering again.')
      })
  }, [token])

  return (
    <div className="min-h-screen flex items-center justify-center bg-zinc-50">
      <div className="text-center max-w-sm">
        {status === 'verifying' && (
          <>
            <div className="animate-spin w-8 h-8 border-2 border-primary border-t-transparent rounded-full mx-auto mb-4" />
            <p className="text-zinc-500">Verifying your email...</p>
          </>
        )}
        {status === 'success' && (
          <>
            <h2 className="text-2xl font-bold mb-3">Email verified</h2>
            <p className="text-zinc-500 mb-6">{message}</p>
            <Link to="/login" className="text-primary font-medium hover:underline">Sign in</Link>
          </>
        )}
        {status === 'error' && (
          <>
            <h2 className="text-2xl font-bold mb-3">Verification failed</h2>
            <p className="text-zinc-500 mb-6">{message}</p>
            <Link to="/register" className="text-primary font-medium hover:underline">Register again</Link>
          </>
        )}
      </div>
    </div>
  )
}
