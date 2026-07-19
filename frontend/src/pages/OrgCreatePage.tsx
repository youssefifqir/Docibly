import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '@/services/api'
import { useAuth } from '@/contexts/AuthContext'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Building2, ArrowRight } from 'lucide-react'

export function OrgCreatePage() {
  const { setOrgId } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [slug, setSlug] = useState('')
  const [description, setDescription] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function onNameChange(value: string) {
    setName(value)
    if (!slug || slug === name.toLowerCase().replace(/\s+/g, '-').replace(/[^a-zA-Z0-9-]/g, '')) {
      setSlug(value.toLowerCase().replace(/\s+/g, '-').replace(/[^a-zA-Z0-9-]/g, ''))
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!name.trim()) { setError('Organization name is required'); return }
    if (!slug.trim()) { setError('Slug is required'); return }
    setLoading(true)
    setError('')
    try {
      const res = await api.post<{ id: number }>('/user/organizations/create-workspace', {
        name: name.trim(),
        slug: slug.trim(),
        description: description.trim(),
      })
      setOrgId(res.data.id)
      navigate('/dashboard', { replace: true })
    } catch (err: unknown) {
      const axiosErr = err as { response?: { status?: number; data?: { message?: string } } }
      if (axiosErr.response?.status === 409) {
        setError('This slug is already taken. Choose a different one.')
      } else {
        setError(axiosErr.response?.data?.message || 'Failed to create organization')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-12 bg-zinc-50">
      <div className="w-full max-w-sm">
        <div className="flex items-center gap-2.5 mb-10 justify-center">
          <img src="/ged-logo.svg" alt="Docibly" className="w-8 h-8 rounded-lg object-cover shrink-0" />
          <span className="text-lg font-bold">Docibly</span>
        </div>

        <h1 className="text-2xl font-bold mb-1">Create your workspace</h1>
        <p className="text-zinc-500 text-sm mb-7">Set up your organization to get started</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <Input label="Organization name" placeholder="Acme Corp" value={name} onChange={e => onNameChange(e.target.value)} icon={<Building2 size={15} />} required />
          <Input label="Slug" placeholder="acme-corp" value={slug} onChange={e => setSlug(e.target.value.toLowerCase().replace(/[^a-z0-9-]/g, ''))} hint="Used in URLs — must be unique" required />
          <Input label="Description (optional)" placeholder="Our company workspace" value={description} onChange={e => setDescription(e.target.value)} />
          {error && <p className="text-red-500 text-sm">{error}</p>}
          <Button type="submit" variant="cta" size="lg" loading={loading} className="w-full">
            Create workspace <ArrowRight size={16} />
          </Button>
        </form>
      </div>
    </div>
  )
}
