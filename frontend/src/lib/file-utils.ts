import type { LucideIcon } from 'lucide-react'
import { FileText, FileImage, FileSpreadsheet, Presentation, FileArchive, Folder } from 'lucide-react'

export function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}

export function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffMins = Math.floor(diffMs / 60000)
  const diffHours = Math.floor(diffMs / 3600000)
  const diffDays = Math.floor(diffMs / 86400000)

  if (diffMins < 1) return 'Just now'
  if (diffMins < 60) return `${diffMins}m ago`
  if (diffHours < 24) return `${diffHours}h ago`
  if (diffDays < 7) return `${diffDays}d ago`

  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

export function getFileIcon(fileType: string, fileName?: string): LucideIcon {
  const ext = (fileName?.split('.').pop() || '').toLowerCase()
  if (ext === 'ppt' || ext === 'pptx') return Presentation
  if (ext === 'xls' || ext === 'xlsx' || ext === 'csv') return FileSpreadsheet
  if (ext === 'doc' || ext === 'docx') return FileText
  if (ext === 'pdf') return FileText
  if (['png', 'jpg', 'jpeg', 'gif', 'svg', 'webp'].includes(ext)) return FileImage
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) return FileArchive
  switch (fileType) {
    case 'pdf': return FileText
    case 'image': return FileImage
    case 'doc': return FileText
    case 'spreadsheet': return FileSpreadsheet
    default: return Folder
  }
}

export function getFileTypeLabel(fileType: string): string {
  switch (fileType) {
    case 'pdf': return 'PDF'
    case 'image': return 'Image'
    case 'doc': return 'Document'
    case 'spreadsheet': return 'Spreadsheet'
    default: return 'File'
  }
}

export function getFileTypeColor(fileType: string): string {
  switch (fileType) {
    case 'pdf': return 'bg-red-50 text-red-600'
    case 'image': return 'bg-purple-50 text-purple-600'
    case 'doc': return 'bg-blue-50 text-blue-600'
    case 'spreadsheet': return 'bg-green-50 text-green-600'
    default: return 'bg-gray-50 text-gray-600'
  }
}
