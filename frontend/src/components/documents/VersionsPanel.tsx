import { useState, useEffect, useRef } from 'react'
import { documentService } from '@/services/documents.service'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import type { DocumentVersionDto } from '@/types/api'
import { History, Upload, Clock, HardDrive, CheckCircle2, XCircle, Loader2, FileText, ChevronDown, ChevronUp } from 'lucide-react'

interface VersionsPanelProps {
  documentId: number
  onVersionChange?: () => void
}

export function VersionsPanel({ documentId, onVersionChange }: VersionsPanelProps) {
  const [versions, setVersions] = useState<DocumentVersionDto[]>([])
  const [loading, setLoading] = useState(true)
  const [expanded, setExpanded] = useState(false)
  const [uploading, setUploading] = useState(false)
  const [label, setLabel] = useState('')
  const [changeNote, setChangeNote] = useState('')
  const fileRef = useRef<HTMLInputElement>(null)

  async function load() {
    setLoading(true)
    try {
      const data = await documentService.getVersions(documentId)
      setVersions(data)
    } catch { setVersions([]) }
    setLoading(false)
  }

  useEffect(() => { load() }, [documentId])

  async function handleReupload() {
    const file = fileRef.current?.files?.[0]
    if (!file) return
    setUploading(true)
    try {
      await documentService.reupload(documentId, file, label || undefined, changeNote || undefined)
      setLabel('')
      setChangeNote('')
      if (fileRef.current) fileRef.current.value = ''
      await load()
      onVersionChange?.()
    } catch {}
    setUploading(false)
  }

  function formatSize(bytes?: number): string {
    if (!bytes) return '—'
    if (bytes < 1024) return `${bytes} B`
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
  }

  function ocrBadge(status?: string) {
    if (!status || status === 'NONE') return null
    const map: Record<string, { color: string; icon: React.ReactNode }> = {
      COMPLETED: { color: 'text-green-600 bg-green-50', icon: <CheckCircle2 size={10} /> },
      PROCESSING: { color: 'text-blue-600 bg-blue-50', icon: <Loader2 size={10} className="animate-spin" /> },
      FAILED: { color: 'text-red-600 bg-red-50', icon: <XCircle size={10} /> },
    }
    const m = map[status] || { color: 'text-zinc-500 bg-zinc-50', icon: null }
    return (
      <span className={`inline-flex items-center gap-1 text-[10px] px-1.5 py-0.5 rounded-full font-medium ${m.color}`}>
        {m.icon} {status}
      </span>
    )
  }

  return (
    <div className="bg-white border rounded-xl overflow-hidden">
      <button
        onClick={() => setExpanded(!expanded)}
        className="w-full flex items-center justify-between px-4 py-3 text-sm font-semibold text-zinc-700 hover:bg-zinc-50 transition-colors"
      >
        <span className="flex items-center gap-2">
          <History size={15} className="text-zinc-400" />
          Version History
          {!loading && <span className="text-xs text-zinc-400 font-normal">({versions.length})</span>}
        </span>
        {expanded ? <ChevronUp size={15} /> : <ChevronDown size={15} />}
      </button>

      {expanded && (
        <div className="border-t border-zinc-100">
          {/* Upload new version */}
          <div className="p-4 border-b border-zinc-100 bg-zinc-50/50 space-y-2.5">
            <p className="text-xs font-semibold text-zinc-600 flex items-center gap-1.5">
              <Upload size={12} /> Upload New Version
            </p>
            <input
              ref={fileRef}
              type="file"
              className="file:mr-3 file:py-1.5 file:px-3 file:rounded-lg file:border-0 file:text-xs file:font-medium file:bg-primary file:text-white hover:file:bg-primary-dark text-xs text-zinc-500 w-full"
            />
            <div className="flex gap-2">
              <div className="flex-1">
                <Input placeholder="Label (optional)" value={label} onChange={e => setLabel(e.target.value)} />
              </div>
              <div className="flex-1">
                <Input placeholder="Change note (optional)" value={changeNote} onChange={e => setChangeNote(e.target.value)} />
              </div>
              <Button size="sm" onClick={handleReupload} loading={uploading} disabled={uploading}>
                <Upload size={13} /> Upload
              </Button>
            </div>
          </div>

          {/* Version list */}
          {loading ? (
            <div className="p-6 text-center text-zinc-400 text-sm">Loading versions...</div>
          ) : versions.length === 0 ? (
            <div className="p-6 text-center text-zinc-400 text-sm">No versions yet</div>
          ) : (
            <div className="divide-y divide-zinc-100 max-h-80 overflow-y-auto">
              {versions.map((v, i) => (
                <div key={v.id} className={`px-4 py-3 flex items-start gap-3 text-sm ${v.isCurrentVersion ? 'bg-primary/5' : ''}`}>
                  <div className={`mt-0.5 w-6 h-6 rounded-full flex items-center justify-center text-[10px] font-bold ${v.isCurrentVersion ? 'bg-primary text-white' : 'bg-zinc-100 text-zinc-500'}`}>
                    {v.versionNumber ?? i + 1}
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span className="font-medium text-zinc-800">{v.label || `Version ${v.versionNumber}`}</span>
                      {v.isCurrentVersion && <span className="text-[10px] bg-primary/10 text-primary font-semibold px-1.5 py-0.5 rounded">current</span>}
                      {ocrBadge(v.ocrStatus)}
                    </div>
                    {v.changeNote && <p className="text-xs text-zinc-500 mt-0.5">{v.changeNote}</p>}
                    <div className="flex items-center gap-3 mt-1 text-[11px] text-zinc-400">
                      <span className="flex items-center gap-1"><Clock size={10} /> {v.createdDate ? new Date(v.createdDate).toLocaleDateString() : '—'}</span>
                      <span className="flex items-center gap-1"><HardDrive size={10} /> {formatSize(v.fileSizeBytes)}</span>
                      <span className="flex items-center gap-1"><FileText size={10} /> {v.originalFilename || '—'}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  )
}
