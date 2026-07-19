import { useState, useEffect, useMemo } from 'react'
import { LayoutGrid, List, FolderOpen, Trash2, Eye, Share2, ShieldOff, Search, X, SlidersHorizontal, FileText, FileImage, FileSpreadsheet, FileType, File } from 'lucide-react'
import { cn } from '@/lib/cn'
import { documentService } from '@/services/documents.service'
import type { DocumentDto } from '@/types/api'
import { EmptyState } from '@/components/ui/EmptyState'
import { Button } from '@/components/ui/Button'
import { Modal } from '@/components/ui/Modal'
import { ShareModal } from '@/components/documents/ShareModal'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'

type Tab = 'all' | 'my' | 'shared'

function getFileTypeIcon(mimeType?: string, size = 20) {
  const cls = 'shrink-0'
  if (!mimeType || mimeType.includes('octet-stream')) return <File size={size} className={cn(cls, 'text-zinc-400')} />
  if (mimeType.includes('pdf')) return <FileText size={size} className={cn(cls, 'text-red-500')} />
  if (mimeType.includes('image')) return <FileImage size={size} className={cn(cls, 'text-purple-500')} />
  if (mimeType.includes('word') || mimeType.includes('document')) return <FileText size={size} className={cn(cls, 'text-blue-600')} />
  if (mimeType.includes('presentation') || mimeType.includes('powerpoint') || mimeType.includes('ppt')) return <FileText size={size} className={cn(cls, 'text-orange-500')} />
  if (mimeType.includes('sheet') || mimeType.includes('excel') || mimeType.includes('spreadsheet')) return <FileSpreadsheet size={size} className={cn(cls, 'text-emerald-600')} />
  if (mimeType.includes('text')) return <FileText size={size} className={cn(cls, 'text-zinc-500')} />
  return <File size={size} className={cn(cls, 'text-zinc-400')} />
}

function getFileTypeGroup(mimeType?: string): string {
  if (!mimeType) return 'other'
  if (mimeType.includes('pdf')) return 'pdf'
  if (mimeType.includes('word') || mimeType.includes('document')) return 'doc'
  if (mimeType.includes('sheet') || mimeType.includes('spreadsheet')) return 'spreadsheet'
  if (mimeType.includes('image')) return 'image'
  return 'other'
}

function formatSize(bytes?: number) {
  if (!bytes) return '—'
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
}

function formatDate(dateStr?: string) {
  if (!dateStr) return '—'
  const d = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  if (diff < 3600000) return `${Math.floor(diff / 60000)}m ago`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}h ago`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}d ago`
  return d.toLocaleDateString()
}

const FILE_TYPES = ['pdf', 'doc', 'spreadsheet', 'image'] as const

export function LibraryPage() {
  const navigate = useNavigate()
  const { hasPermission, authReady } = useAuth()
  const [docs, setDocs] = useState<DocumentDto[]>([])
  const [loading, setLoading] = useState(true)
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('list')
  const [tab, setTab] = useState<Tab>('all')
  const [shareDocId, setShareDocId] = useState<number | null>(null)
  const [searchQuery, setSearchQuery] = useState('')
  const [fileTypeFilter, setFileTypeFilter] = useState('')
  const [selectedIds, setSelectedIds] = useState<Set<number>>(new Set())
  const [deleteTarget, setDeleteTarget] = useState<DocumentDto | null>(null)
  const [bulkDeleteConfirm, setBulkDeleteConfirm] = useState(false)

  async function load() {
    setLoading(true)
    try {
      const res = await documentService.findAll(0, 200)
      setDocs(res.content)
    } catch { setDocs([]) }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [tab])

  useEffect(() => { setSelectedIds(new Set()) }, [docs])

  const filtered = useMemo(() => {
    let arr = docs
    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase()
      arr = arr.filter(d =>
        (d.title?.toLowerCase().includes(q)) ||
        (d.description?.toLowerCase().includes(q)) ||
        (d.originalFilename?.toLowerCase().includes(q))
      )
    }
    if (fileTypeFilter) {
      arr = arr.filter(d => getFileTypeGroup(d.mimeType) === fileTypeFilter)
    }
    return arr
  }, [docs, searchQuery, fileTypeFilter])

  async function handleDelete(id: number) {
    try { await documentService.delete(id); load() } catch {}
  }

  async function handleBulkDelete() {
    try {
      await Promise.all(Array.from(selectedIds).map(id => documentService.delete(id)))
      setSelectedIds(new Set())
      setBulkDeleteConfirm(false)
      load()
    } catch {}
  }

  function toggleSelect(id: number) {
    setSelectedIds(prev => {
      const next = new Set(prev)
      if (next.has(id)) next.delete(id); else next.add(id)
      return next
    })
  }

  function toggleSelectAll() {
    if (selectedIds.size === filtered.length) {
      setSelectedIds(new Set())
    } else {
      setSelectedIds(new Set(filtered.map(d => d.id)))
    }
  }

  const hasFilters = !!searchQuery || !!fileTypeFilter
  const allSelected = filtered.length > 0 && selectedIds.size === filtered.length
  const someSelected = selectedIds.size > 0

  const tabs: Tab[] = ['all', 'my', 'shared']

  if (authReady && !hasPermission('doc.view')) {
    return (
      <div className="p-6 flex flex-col items-center justify-center min-h-64 gap-3">
        <ShieldOff size={40} className="text-zinc-300" />
        <h2 className="text-lg font-semibold text-zinc-700">Access Denied</h2>
        <p className="text-sm text-zinc-500">You don't have permission to view documents in this organization.</p>
      </div>
    )
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold text-zinc-800">Document Library</h1>
        <div className="flex items-center gap-3">
          <div className="bg-zinc-100 rounded-lg p-1 flex gap-1">
            <button onClick={() => setViewMode('grid')} className={cn('p-1.5 rounded', viewMode === 'grid' && 'bg-white shadow')}>
              <LayoutGrid size={16} />
            </button>
            <button onClick={() => setViewMode('list')} className={cn('p-1.5 rounded', viewMode === 'list' && 'bg-white shadow')}>
              <List size={16} />
            </button>
          </div>
          {hasPermission('doc.upload') && <Button onClick={() => navigate('/upload')}>+ Upload</Button>}
        </div>
      </div>

      <div className="flex gap-1 mb-4 border-b">
        {tabs.map(t => (
          <button key={t} onClick={() => { setTab(t); setSearchQuery(''); setFileTypeFilter('') }}
            className={cn('px-4 py-2 text-sm font-medium capitalize border-b-2 transition',
              tab === t ? 'border-blue-600 text-blue-600' : 'border-transparent text-zinc-500 hover:text-zinc-800')}>
            {t}
          </button>
        ))}
      </div>

      <div className="space-y-3 mb-4">
        <div className="relative">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400" />
          <input
            type="text"
            placeholder="Search by title, description, or filename..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
            className="w-full h-10 pl-10 pr-8 rounded-lg border border-zinc-300 bg-white text-sm placeholder:text-zinc-400 focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary/20 transition-colors"
          />
          {searchQuery && (
            <button onClick={() => setSearchQuery('')} className="absolute right-3 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-zinc-600">
              <X size={14} />
            </button>
          )}
        </div>

        <div className="flex flex-wrap items-center gap-1.5">
          <SlidersHorizontal size={12} className="text-zinc-400 shrink-0" />
          <button onClick={() => setFileTypeFilter('')}
            className={cn('tag-pill cursor-pointer', !fileTypeFilter && '!bg-primary !text-white')}>
            ALL
          </button>
          {FILE_TYPES.map(type => (
            <button key={type} onClick={() => setFileTypeFilter(fileTypeFilter === type ? '' : type)}
              className={cn('tag-pill cursor-pointer', fileTypeFilter === type && '!bg-primary !text-white')}>
              {type === 'spreadsheet' ? 'SHEET' : type.toUpperCase()}
            </button>
          ))}

        </div>
      </div>

      {someSelected && (
        <div className="flex items-center gap-3 mb-4 px-4 py-2.5 bg-primary/5 border border-primary/20 rounded-lg">
          <span className="text-sm font-medium text-zinc-700">{selectedIds.size} selected</span>
          {hasPermission('doc.delete') && (
            <Button variant="primary" size="sm" onClick={() => setBulkDeleteConfirm(true)} className="bg-red-600 hover:bg-red-700 border-red-600 text-white">
              <Trash2 size={14} /> Delete Selected
            </Button>
          )}
          <Button variant="ghost" size="sm" onClick={() => setSelectedIds(new Set())}>
            <X size={14} /> Clear
          </Button>
        </div>
      )}

      {loading ? (
        <div className="text-center py-12 text-zinc-400">Loading...</div>
      ) : filtered.length === 0 ? (
        <EmptyState
          icon={hasFilters ? Search : FolderOpen}
          title={hasFilters ? "No matching documents" : "No documents yet"}
          description={hasFilters ? "Try adjusting your search or filters" : "Upload your first document to get started"}
          action={!hasFilters && <Button onClick={() => navigate('/upload')}>Upload Document</Button>}
        />
      ) : viewMode === 'grid' ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {filtered.map(doc => {
            const isSelected = selectedIds.has(doc.id)
            return (
              <div key={doc.id} className={cn('border rounded-lg p-4 hover:shadow-md transition bg-white relative', isSelected && 'ring-2 ring-primary/50 border-primary/30')}>
                <div className="absolute top-2 left-2 z-10">
                  <input
                    type="checkbox"
                    checked={isSelected}
                    onChange={() => toggleSelect(doc.id)}
                    onClick={e => e.stopPropagation()}
                    className="checkbox checkbox-sm rounded"
                  />
                </div>
                <div className="cursor-pointer" onClick={() => navigate(`/preview/${doc.id}`)}>
                  <div className="mb-2">{getFileTypeIcon(doc.mimeType, 36)}</div>
                  <h3 className="font-medium text-sm truncate pr-4">{doc.title}</h3>
                  <p className="text-xs text-zinc-400 mt-1">{formatSize(doc.fileSizeBytes)}</p>
                  <div className="flex items-center justify-between mt-3 pt-3 border-t">
                    <span className="text-xs text-zinc-400">{formatDate(doc.createdDate)}</span>
                    <div className="flex gap-1">
                      {hasPermission('doc.share') && <button title="Share" onClick={e => { e.stopPropagation(); setShareDocId(doc.id) }} className="p-1 hover:bg-zinc-100 rounded text-zinc-500"><Share2 size={14} /></button>}
                      <button title="Preview" onClick={e => { e.stopPropagation(); navigate(`/preview/${doc.id}`) }} className="p-1 hover:bg-zinc-100 rounded"><Eye size={14} /></button>
                      {hasPermission('doc.delete') && <button title="Delete" onClick={e => { e.stopPropagation(); setDeleteTarget(doc) }} className="p-1 hover:bg-red-100 rounded text-red-500"><Trash2 size={14} /></button>}
                    </div>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      ) : (
        <div className="border rounded-lg overflow-hidden bg-white">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b bg-zinc-50 text-left text-zinc-500">
                <th className="pb-3 pt-3 pl-4 w-10">
                  <input
                    type="checkbox"
                    checked={allSelected}
                    onChange={toggleSelectAll}
                    className="checkbox checkbox-sm rounded"
                  />
                </th>
                <th className="pb-3 pt-3 font-medium">Name</th>
                <th className="pb-3 pt-3 font-medium">Type</th>
                <th className="pb-3 pt-3 font-medium">Size</th>
                <th className="pb-3 pt-3 font-medium">Date</th>
                <th className="pb-3 pt-3 pr-4 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(doc => {
                const isSelected = selectedIds.has(doc.id)
                return (
                  <tr key={doc.id} className={cn('border-b hover:bg-zinc-50 transition', isSelected && 'bg-primary/5')}>
                    <td className="py-3 pl-4">
                      <input
                        type="checkbox"
                        checked={isSelected}
                        onChange={() => toggleSelect(doc.id)}
                        className="checkbox checkbox-sm rounded"
                      />
                    </td>
                    <td className="py-3 cursor-pointer" onClick={() => navigate(`/preview/${doc.id}`)}>
                      <div className="flex items-center gap-2">
                        <span>{getFileTypeIcon(doc.mimeType)}</span>
                        <span className="font-medium text-zinc-800">{doc.title}</span>
                      </div>
                    </td>
                    <td className="py-3 text-zinc-500">{doc.mimeType?.split('/')[1] || '—'}</td>
                    <td className="py-3 text-zinc-500">{formatSize(doc.fileSizeBytes)}</td>
                    <td className="py-3 text-zinc-500">{formatDate(doc.createdDate)}</td>
                    <td className="py-3 pr-4">
                      <div className="flex gap-1">
                        {hasPermission('doc.share') && <button title="Share" onClick={() => setShareDocId(doc.id)} className="p-1.5 hover:bg-zinc-100 rounded text-zinc-500"><Share2 size={14} /></button>}
                        <button title="Preview" onClick={() => navigate(`/preview/${doc.id}`)} className="p-1.5 hover:bg-zinc-100 rounded text-zinc-500"><Eye size={14} /></button>
                        {hasPermission('doc.delete') && <button title="Delete" onClick={() => setDeleteTarget(doc)} className="p-1.5 hover:bg-red-100 rounded text-red-500"><Trash2 size={14} /></button>}
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}

      {shareDocId && <ShareModal documentId={shareDocId} open={true} onClose={() => setShareDocId(null)} />}

      <Modal open={deleteTarget !== null} onClose={() => setDeleteTarget(null)} title="Delete Document" size="sm">
        <p className="text-sm text-zinc-600 mb-4">Are you sure you want to delete <strong>{deleteTarget?.title}</strong>? This action cannot be undone.</p>
        <div className="flex justify-end gap-2">
          <Button variant="ghost" onClick={() => setDeleteTarget(null)}>Cancel</Button>
          <Button variant="primary" onClick={() => { if (deleteTarget) handleDelete(deleteTarget.id); setDeleteTarget(null) }} className="bg-red-600 hover:bg-red-700 border-red-600">Delete</Button>
        </div>
      </Modal>

      <Modal open={bulkDeleteConfirm} onClose={() => setBulkDeleteConfirm(false)} title="Delete Documents" size="sm">
        <p className="text-sm text-zinc-600 mb-4">Are you sure you want to delete <strong>{selectedIds.size}</strong> document{selectedIds.size !== 1 ? 's' : ''}? This action cannot be undone.</p>
        <div className="flex justify-end gap-2">
          <Button variant="ghost" onClick={() => setBulkDeleteConfirm(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleBulkDelete} className="bg-red-600 hover:bg-red-700 border-red-600">Delete {selectedIds.size}</Button>
        </div>
      </Modal>
    </div>
  )
}
