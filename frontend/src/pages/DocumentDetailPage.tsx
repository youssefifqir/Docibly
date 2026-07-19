import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { documentService } from '@/services/documents.service'
import { useAuth } from '@/contexts/AuthContext'
import type { DocumentDto } from '@/types/api'
import { CommentsPanel } from '@/components/documents/CommentsPanel'
import { ShareModal } from '@/components/documents/ShareModal'
import { VersionsPanel } from '@/components/documents/VersionsPanel'
import { Button } from '@/components/ui/Button'
import { ArrowLeft, Download, Share2, Calendar, HardDrive, Eye, FileText, CheckCircle2, XCircle, Loader2 } from 'lucide-react'

export function DocumentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const { orgId, hasPermission } = useAuth()
  const navigate = useNavigate()
  const [doc, setDoc] = useState<DocumentDto | null>(null)
  const [loading, setLoading] = useState(true)
  const [showShare, setShowShare] = useState(false)

  useEffect(() => {
    if (!id) return
    setLoading(true)
    documentService.findById(parseInt(id))
      .then(setDoc)
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [id])

  async function handleDownload() {
    if (!id) return
    try {
      const blob = await documentService.download(parseInt(id))
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = doc?.originalFilename || 'document'
      a.click()
      URL.revokeObjectURL(url)
    } catch {}
  }

  if (loading) {
    return <div className="p-6 text-center text-zinc-400">Loading...</div>
  }

  if (!doc) {
    return (
      <div className="p-6 text-center">
        <p className="text-zinc-500">Document not found</p>
        <Button onClick={() => navigate('/library')} variant="outline" className="mt-4">Back to Library</Button>
      </div>
    )
  }

  return (
    <div className="p-6 max-w-5xl mx-auto">
      <button onClick={() => navigate('/library')} className="flex items-center gap-1 text-sm text-zinc-500 hover:text-zinc-800 mb-6">
        <ArrowLeft size={16} /> Back to Library
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Document info */}
        <div className="lg:col-span-2 space-y-5">
          <div className="bg-white border rounded-xl p-6">
            <div className="flex items-start justify-between mb-4">
              <div>
                <h1 className="text-xl font-bold">{doc.title}</h1>
                {doc.description && <p className="text-sm text-zinc-500 mt-1">{doc.description}</p>}
              </div>
              <div className="text-3xl">
                {doc.mimeType?.includes('pdf') ? '📕' : doc.mimeType?.includes('image') ? '🖼️' : '📄'}
              </div>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-sm">
              <div>
                <span className="text-zinc-400 text-xs flex items-center gap-1"><FileText size={12} /> Type</span>
                <p className="font-medium">{doc.mimeType?.split('/')[1] || '—'}</p>
              </div>
              <div>
                <span className="text-zinc-400 text-xs flex items-center gap-1"><HardDrive size={12} /> Size</span>
                <p className="font-medium">{doc.fileSizeBytes ? `${(doc.fileSizeBytes / 1024).toFixed(1)} KB` : '—'}</p>
              </div>
              <div>
                <span className="text-zinc-400 text-xs flex items-center gap-1"><Eye size={12} /> Status</span>
                <p className="font-medium capitalize">{doc.status?.toLowerCase() || '—'}</p>
              </div>
              <div>
                <span className="text-zinc-400 text-xs flex items-center gap-1"><Calendar size={12} /> Uploaded</span>
                <p className="font-medium">{doc.createdDate ? new Date(doc.createdDate).toLocaleDateString() : '—'}</p>
              </div>
            </div>

            {doc.ocrStatus && doc.ocrStatus !== 'NONE' && (
              <div className="mt-4 pt-4 border-t">
                <span className="text-xs text-zinc-400">OCR Status</span>
                <div className="mt-1.5 flex items-center gap-2">
                  <span className={`inline-flex items-center gap-1.5 text-xs px-2.5 py-1 rounded-full font-medium ${
                    doc.ocrStatus === 'COMPLETED' ? 'bg-green-50 text-green-700' :
                    doc.ocrStatus === 'FAILED' ? 'bg-red-50 text-red-700' :
                    doc.ocrStatus === 'PROCESSING' ? 'bg-blue-50 text-blue-700' :
                    'bg-zinc-50 text-zinc-500'
                  }`}>
                    {doc.ocrStatus === 'COMPLETED' && <CheckCircle2 size={12} />}
                    {doc.ocrStatus === 'FAILED' && <XCircle size={12} />}
                    {doc.ocrStatus === 'PROCESSING' && <Loader2 size={12} className="animate-spin" />}
                    {doc.ocrStatus}
                  </span>
                  {doc.ocrConfidenceScore != null && (
                    <span className="text-xs text-zinc-400">Confidence: {(doc.ocrConfidenceScore * 100).toFixed(0)}%</span>
                  )}
                </div>
              </div>
            )}

            <div className="flex gap-2 mt-6 pt-4 border-t">
              {hasPermission('doc.download') && <Button onClick={handleDownload}><Download size={16} /> Download</Button>}
              {hasPermission('doc.share') && <Button variant="outline" onClick={() => setShowShare(true)}><Share2 size={16} /> Share</Button>}
            </div>
          </div>

          {/* Version History */}
          {id && <VersionsPanel documentId={parseInt(id)} onVersionChange={() => documentService.findById(parseInt(id)).then(setDoc)} />}
        </div>

        {/* Comments sidebar */}
        <div className="lg:col-span-1 space-y-5">
          {id && <CommentsPanel documentId={parseInt(id)} />}
        </div>
      </div>

      {showShare && id && orgId && (
        <ShareModal documentId={parseInt(id)} open={true} onClose={() => setShowShare(false)} />
      )}
    </div>
  )
}
