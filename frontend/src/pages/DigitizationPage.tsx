import { useState, useRef, useCallback, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { documentService } from '@/services/documents.service'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Select } from '@/components/ui/Select'
import { TagInput } from '@/components/documents/TagInput'
import { ScanLine, ArrowLeft, FileText, CheckCircle2, Upload, X } from 'lucide-react'
import { cn } from '@/lib/cn'
import type { TagDto } from '@/types/api'

const FORMATS = [
  { value: 'NONE', label: 'Store as-is (no OCR)' },
  { value: 'TXT', label: 'Extract text only (TXT)' },
  { value: 'PDF', label: 'Digitize to searchable PDF' },
]

export function DigitizationPage() {
  const navigate = useNavigate()
  const fileRef = useRef<HTMLInputElement>(null)
  const [file, setFile] = useState<File | null>(null)
  const [previewUrl, setPreviewUrl] = useState<string | null>(null)
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [selectedTags, setSelectedTags] = useState<TagDto[]>([])
  const [digitizeFormat, setDigitizeFormat] = useState('PDF')
  const [uploading, setUploading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    return () => { if (previewUrl) URL.revokeObjectURL(previewUrl) }
  }, [previewUrl])

  const handleFiles = useCallback((files: FileList | null) => {
    if (!files?.length) return
    const f = files[0]
    if (previewUrl) URL.revokeObjectURL(previewUrl)
    setFile(f)
    setPreviewUrl(URL.createObjectURL(f))
    setTitle(f.name.replace(/\.[^/.]+$/, '').replace(/[-_]/g, ' '))
    setError('')
  }, [previewUrl])

  async function handleUpload(e: React.FormEvent) {
    e.preventDefault()
    if (!file || !title.trim()) return
    setUploading(true)
    setError('')
    try {
      const doc = await documentService.upload(
        file,
        title.trim(),
        description || undefined,
        undefined,
        selectedTags.length ? selectedTags.map(t => t.id) : undefined,
        digitizeFormat,
      )
      navigate(`/preview/${doc.id}`)
    } catch {
      setError('Upload failed. Please try again.')
    }
    setUploading(false)
  }

  function reset() {
    if (previewUrl) URL.revokeObjectURL(previewUrl)
    setFile(null)
    setPreviewUrl(null)
    setTitle('')
    setDescription('')
    setSelectedTags([])
    setDigitizeFormat('PDF')
    setError('')
  }

  return (
    <div className="max-w-3xl mx-auto pb-10">
      <div className="flex items-center gap-3 mb-6">
        <button onClick={() => navigate(-1)} className="btn btn-ghost btn-sm btn-circle text-zinc-400 hover:text-primary">
          <ArrowLeft size={16} />
        </button>
        <div>
          <h2 className="text-xl font-bold flex items-center gap-2">
            <ScanLine size={20} className="text-primary" />
            Digitize Document
          </h2>
          <p className="text-zinc-500 text-sm">Upload an image or document to digitize with OCR</p>
        </div>
      </div>

      <form onSubmit={handleUpload} className="space-y-5">
        {!file ? (
          <div
            className="upload-zone p-14 text-center cursor-pointer"
            onClick={() => fileRef.current?.click()}
            onDragOver={e => { e.preventDefault(); e.currentTarget.classList.add('drag-over') }}
            onDragLeave={e => { e.preventDefault(); e.currentTarget.classList.remove('drag-over') }}
            onDrop={e => { e.preventDefault(); e.currentTarget.classList.remove('drag-over'); handleFiles(e.dataTransfer.files) }}
          >
            <input ref={fileRef} type="file" accept="image/*,application/pdf" onChange={e => handleFiles(e.target.files)} className="hidden" />
            <div className="w-20 h-20 rounded-2xl mx-auto mb-5 flex items-center justify-center bg-zinc-100">
              <ScanLine size={34} className="text-zinc-400" />
            </div>
            <h3 className="text-lg font-semibold mb-2">Upload a document to digitize</h3>
            <p className="text-sm text-zinc-500 mb-4">Drag & drop or click to browse</p>
            <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-lg bg-zinc-100 text-xs text-zinc-500 font-medium">
              Supports: PNG · JPG · TIFF · PDF
            </span>
          </div>
        ) : (
          <div className="bg-white rounded-xl border border-zinc-200 overflow-hidden shadow-sm">
            <div className="px-4 py-3 border-b border-zinc-100 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-9 h-9 rounded-lg bg-green-50 flex items-center justify-center">
                  <CheckCircle2 size={18} className="text-green-500" />
                </div>
                <div>
                  <p className="font-semibold text-sm leading-none">{file.name}</p>
                  <p className="text-xs text-zinc-400 mt-0.5">{(file.size / 1024).toFixed(1)} KB</p>
                </div>
              </div>
              <button onClick={reset} className="btn btn-ghost btn-sm btn-circle text-zinc-400 hover:text-red-500"><X size={14} /></button>
            </div>
            {previewUrl && file.type.startsWith('image/') && (
              <div className="bg-zinc-50 flex items-center justify-center overflow-hidden" style={{ maxHeight: 300 }}>
                <img src={previewUrl} alt="Preview" className="max-h-[300px] w-full object-contain" />
              </div>
            )}
          </div>
        )}

        {file && (
          <div className="bg-white rounded-xl border border-zinc-200 p-5 space-y-4 shadow-sm">
            <h3 className="font-semibold text-sm flex items-center gap-2"><FileText size={15} className="text-primary" /> Document Details</h3>
            <Input label="Title" value={title} onChange={e => setTitle(e.target.value)} required placeholder="Document title" />
            <div>
              <label className="text-xs font-medium text-zinc-500 mb-1.5 block">Description</label>
              <textarea value={description} onChange={e => setDescription(e.target.value)} placeholder="Brief description" rows={3} className="w-full border border-zinc-300 rounded-lg bg-white px-3 py-2 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/10 resize-none transition-colors" />
            </div>
            <TagInput selectedTags={selectedTags} onChange={setSelectedTags} />
            <div>
              <label className="text-xs font-medium text-zinc-500 mb-1.5 block">Digitize Format</label>
              <Select value={digitizeFormat} onChange={e => setDigitizeFormat(e.target.value)}>
                {FORMATS.map(f => <option key={f.value} value={f.value}>{f.label}</option>)}
              </Select>
            </div>
          </div>
        )}

        {error && <p className="text-sm text-red-600 text-center">{error}</p>}

        {file && (
          <div className="flex items-center justify-end gap-2.5 pt-2">
            <Button variant="ghost" type="button" onClick={() => navigate(-1)}>Cancel</Button>
            <Button variant="cta" type="submit" loading={uploading} disabled={uploading || !title.trim()}>
              <Upload size={15} className="mr-1.5" />
              {uploading ? 'Uploading...' : 'Upload & Digitize'}
            </Button>
          </div>
        )}
      </form>
    </div>
  )
}
