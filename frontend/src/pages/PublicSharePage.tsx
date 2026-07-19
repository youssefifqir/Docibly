import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { FileText, Lock, Download, Eye } from 'lucide-react'
import { Button } from '@/components/ui/Button'
import { API_BASE_URL } from '@/services/api'

interface SharedDocument {
  id: number
  title: string
  description: string | null
  mimeType: string
  fileSizeBytes: number
  originalFilename: string
}

interface ShareInfo {
  token: string
  permission: string
  expiresAt: string | null
}

export function PublicSharePage() {
  const { token } = useParams<{ token: string }>()
  const [document, setDocument] = useState<SharedDocument | null>(null)
  const [share, setShare] = useState<ShareInfo | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [password, setPassword] = useState('')
  const [needsPassword, setNeedsPassword] = useState(false)

  useEffect(() => {
    if (!token) return
    setLoading(true)
    fetch(`${API_BASE_URL}/public/shares/${token}`)
      .then(res => {
        if (!res.ok && res.status === 401) {
          setNeedsPassword(true)
          return null
        }
        return res.json()
      })
      .then(data => {
        if (data) {
          setDocument(data.document)
          setShare(data.share)
        }
      })
      .catch(() => setError('This share link is invalid or has expired.'))
      .finally(() => setLoading(false))
  }, [token])

  async function handleVerifyPassword() {
    if (!token || !password.trim()) return
    try {
      const res = await fetch(`${API_BASE_URL}/public/shares/${token}/verify-password`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ password }),
      })
      if (res.ok) {
        setNeedsPassword(false)
        const data = await fetch(`${API_BASE_URL}/public/shares/${token}?password=${encodeURIComponent(password)}`).then(r => r.json())
        setDocument(data.document)
        setShare(data.share)
      } else {
        setError('Incorrect password')
      }
    } catch { setError('Failed to verify password') }
  }

  function handleDownload() {
    if (!token) return
    const url = `${API_BASE_URL}/public/shares/${token}/download${password ? `?password=${encodeURIComponent(password)}` : ''}`
    window.open(url, '_blank')
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="text-zinc-400">Loading...</div>
      </div>
    )
  }

  if (error && !needsPassword) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="text-center max-w-md">
          <div className="w-14 h-14 rounded-xl bg-red-50 flex items-center justify-center mx-auto mb-4">
            <Lock size={24} className="text-red-400" />
          </div>
          <h2 className="text-lg font-semibold mb-1">Link not available</h2>
          <p className="text-sm text-zinc-500">{error}</p>
        </div>
      </div>
    )
  }

  if (needsPassword) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-zinc-50">
        <div className="bg-white border rounded-xl p-8 max-w-sm w-full text-center space-y-4">
          <div className="w-12 h-12 rounded-xl bg-zinc-100 flex items-center justify-center mx-auto">
            <Lock size={20} className="text-zinc-500" />
          </div>
          <h2 className="font-semibold">Password required</h2>
          <p className="text-sm text-zinc-500">This document is protected. Enter the password to continue.</p>
          <input type="password" placeholder="Enter password"
            value={password} onChange={e => setPassword(e.target.value)}
            className="w-full border border-zinc-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            onKeyDown={e => e.key === 'Enter' && handleVerifyPassword()} />
          {error && <p className="text-xs text-red-500">{error}</p>}
          <Button onClick={handleVerifyPassword} className="w-full" disabled={!password.trim()}>Unlock</Button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-zinc-50 flex items-center justify-center p-4">
      <div className="bg-white border rounded-xl p-8 max-w-lg w-full space-y-5">
        <div className="flex items-center gap-3">
          <div className="w-12 h-12 rounded-xl bg-blue-50 flex items-center justify-center">
            <FileText size={20} className="text-blue-600" />
          </div>
          <div className="min-w-0">
            <h1 className="font-semibold text-lg truncate">{document?.title}</h1>
            <p className="text-xs text-zinc-400">{document?.originalFilename}</p>
          </div>
        </div>

        {document?.description && (
          <p className="text-sm text-zinc-600">{document.description}</p>
        )}

        <div className="flex items-center gap-4 text-sm text-zinc-500">
          <span className="flex items-center gap-1">
            <Eye size={14} /> {share?.permission}
          </span>
          <span>{document?.mimeType}</span>
          <span>{document?.fileSizeBytes ? `${(document.fileSizeBytes / 1024).toFixed(1)} KB` : ''}</span>
        </div>

        <Button onClick={handleDownload} className="w-full" variant="cta">
          <Download size={16} /> Download
        </Button>
      </div>
    </div>
  )
}
