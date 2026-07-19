import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { documentService } from '@/services/documents.service'
import { useToast } from '@/contexts/ToastContext'
import { useAuth } from '@/contexts/AuthContext'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'
import { Select } from '@/components/ui/Select'
import { ArrowLeft, UploadCloud, X, FileText, ShieldOff } from 'lucide-react'

export function UploadPage() {
  const navigate = useNavigate()
  const toast = useToast()
  const { hasPermission, authReady } = useAuth()
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const [uploading, setUploading] = useState(false)
  const [digitizeFormat, setDigitizeFormat] = useState('NONE')

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!file || !title.trim()) return
    setUploading(true)
    try {
      await documentService.upload(file, title.trim(), description.trim(), undefined, undefined, digitizeFormat)
      toast.success('Document uploaded successfully')
      navigate('/library')
    } catch {
      // Error handled by interceptor
    } finally {
      setUploading(false)
    }
  }

  if (authReady && !hasPermission('doc.upload')) {
    return (
      <div className="max-w-2xl mx-auto p-6 flex flex-col items-center justify-center min-h-64 gap-3">
        <ShieldOff size={40} className="text-zinc-300" />
        <h2 className="text-lg font-semibold text-zinc-700">Access Denied</h2>
        <p className="text-sm text-zinc-500">You don't have permission to upload documents in this organization.</p>
        <Button variant="outline" onClick={() => navigate(-1)}><ArrowLeft size={16} /> Go Back</Button>
      </div>
    )
  }

  return (
    <div className="max-w-2xl mx-auto p-6">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-sm text-zinc-500 hover:text-zinc-800 mb-6">
        <ArrowLeft size={16} /> Back
      </button>

      <h1 className="text-2xl font-bold mb-2">Upload Document</h1>
      <p className="text-sm text-zinc-500 mb-8">Upload a document to your workspace</p>

      <form onSubmit={handleSubmit} className="space-y-5">
        <div>
          <label className="block text-sm font-medium mb-1.5">File *</label>
          {file ? (
            <div className="flex items-center justify-between bg-zinc-50 border rounded-lg px-4 py-3">
              <div className="flex items-center gap-3">
                <FileText size={20} className="text-zinc-400" />
                <div>
                  <p className="text-sm font-medium">{file.name}</p>
                  <p className="text-xs text-zinc-400">{(file.size / 1024).toFixed(1)} KB</p>
                </div>
              </div>
              <button type="button" onClick={() => setFile(null)} className="p-1 hover:bg-zinc-200 rounded">
                <X size={16} />
              </button>
            </div>
          ) : (
            <label className="flex flex-col items-center justify-center border-2 border-dashed border-zinc-300 rounded-lg py-10 cursor-pointer hover:border-zinc-400">
              <UploadCloud size={32} className="text-zinc-300 mb-2" />
              <p className="text-sm text-zinc-500">Click to select a file</p>
              <input type="file" className="hidden" onChange={e => setFile(e.target.files?.[0] || null)} />
            </label>
          )}
        </div>

        <Input label="Title *" placeholder="Document title" value={title} onChange={e => setTitle(e.target.value)} />
        <Input label="Description (optional)" placeholder="Brief description" value={description} onChange={e => setDescription(e.target.value)} />

        <div>
          <label className="block text-sm font-medium mb-1.5">OCR Digitization</label>
          <Select value={digitizeFormat} onChange={e => setDigitizeFormat(e.target.value)}>
            <option value="NONE">None (skip OCR)</option>
            <option value="TXT">Extract text (.txt)</option>
            <option value="PDF">Searchable PDF (.pdf)</option>
          </Select>
        </div>

        <div className="flex gap-3 pt-2">
          <Button type="submit" disabled={!file || !title.trim() || uploading}>
            {uploading ? 'Uploading...' : 'Upload'}
          </Button>
          <Button type="button" variant="outline" onClick={() => navigate('/library')}>Cancel</Button>
        </div>
      </form>
    </div>
  )
}
