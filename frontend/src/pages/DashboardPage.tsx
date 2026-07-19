import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { documentService } from '@/services/documents.service'
import type { DocumentDto } from '@/types/api'
import { FileText, HardDrive, Upload } from 'lucide-react'
import { useNavigate } from 'react-router-dom'

export function DashboardPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [docs, setDocs] = useState<DocumentDto[]>([])
  const [stats, setStats] = useState({ total: 0, totalSize: 0 })

  useEffect(() => {
    documentService.findAll(0, 200).then(res => {
      setDocs(res.content)
      const total = res.totalElements
      const totalSize = res.content.reduce((sum, d) => sum + (d.fileSizeBytes || 0), 0)
      setStats({ total, totalSize })
    }).catch(() => {})
  }, [])

  const greeting = (() => {
    const hour = new Date().getHours()
    if (hour < 12) return 'Good morning'
    if (hour < 18) return 'Good afternoon'
    return 'Good evening'
  })()

  function formatSize(bytes: number) {
    if (bytes < 1024) return `${bytes} B`
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-xl font-bold">
          {greeting}, <span className="text-primary">{user?.name?.split(' ')[0] || user?.email?.split('@')[0]}</span>
        </h2>
        <p className="text-zinc-500 text-sm mt-0.5">Here's what's happening in your workspace</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white border border-zinc-200 rounded-xl p-5">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-blue-50 flex items-center justify-center text-blue-600"><FileText size={20} /></div>
            <div>
              <p className="text-2xl font-bold">{stats.total}</p>
              <p className="text-xs text-zinc-400">Total Documents</p>
            </div>
          </div>
        </div>
        <div className="bg-white border border-zinc-200 rounded-xl p-5">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600"><HardDrive size={20} /></div>
            <div>
              <p className="text-2xl font-bold">{formatSize(stats.totalSize)}</p>
              <p className="text-xs text-zinc-400">Storage Used</p>
            </div>
          </div>
        </div>
        <div className="bg-white border border-zinc-200 rounded-xl p-5">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-purple-50 flex items-center justify-center text-purple-600"><Upload size={20} /></div>
            <div>
              <p className="text-2xl font-bold">—</p>
              <p className="text-xs text-zinc-400">Shared With You</p>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <button onClick={() => navigate('/upload')} className="bg-white border border-zinc-200 rounded-xl p-4 text-left hover:border-[#e8622a]/50 transition-colors">
          <p className="font-medium text-sm">Upload</p>
          <p className="text-xs text-zinc-400 mt-0.5">Add new documents</p>
        </button>
        <button onClick={() => navigate('/library')} className="bg-white border border-zinc-200 rounded-xl p-4 text-left hover:border-[#e8622a]/50 transition-colors">
          <p className="font-medium text-sm">Library</p>
          <p className="text-xs text-zinc-400 mt-0.5">Browse all documents</p>
        </button>
        <button onClick={() => navigate('/search')} className="bg-white border border-zinc-200 rounded-xl p-4 text-left hover:border-[#e8622a]/50 transition-colors">
          <p className="font-medium text-sm">Search</p>
          <p className="text-xs text-zinc-400 mt-0.5">Find documents</p>
        </button>
      </div>

      <div>
        <h3 className="font-bold text-sm mb-3">Recent Documents</h3>
        <div className="bg-white border border-zinc-200 rounded-xl overflow-hidden">
          {docs.slice(0, 5).map(doc => (
            <div key={doc.id} className="flex items-center gap-3 px-4 py-3 border-b border-zinc-50 last:border-0">
              <span className="text-lg">{doc.mimeType?.includes('pdf') ? '📕' : doc.mimeType?.includes('image') ? '🖼️' : '📄'}</span>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium truncate">{doc.title}</p>
                <p className="text-xs text-zinc-400">{doc.originalFilename || doc.storedFilename || ''} · {doc.createdDate ? new Date(doc.createdDate).toLocaleDateString() : ''}</p>
              </div>
            </div>
          ))}
          {docs.length === 0 && <p className="text-center text-sm text-zinc-400 py-8">No documents yet. <button onClick={() => navigate('/upload')} className="text-[#e8622a] hover:underline">Upload one</button></p>}
        </div>
      </div>
    </div>
  )
}
