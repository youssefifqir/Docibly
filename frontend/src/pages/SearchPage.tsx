import { useState, useEffect, useMemo, useCallback } from 'react'
import { useSearchParams, useNavigate } from 'react-router-dom'
import { Search, SlidersHorizontal, FileText, ChevronLeft, ChevronRight, Eye } from 'lucide-react'
import { searchService } from '@/services/search.service'
import { tagService } from '@/services/tags.service'
import type { SearchResultDto, TagDto } from '@/types/api'
import { Badge } from '@/components/ui/Badge'
import { EmptyState } from '@/components/ui/EmptyState'
import { cn } from '@/lib/cn'

const PAGE_SIZE = 9

function getFileTypeIcon(mimeType?: string) {
  if (!mimeType) return '📄'
  if (mimeType.includes('pdf')) return '📕'
  if (mimeType.includes('image')) return '🖼️'
  if (mimeType.includes('word') || mimeType.includes('document')) return '📝'
  if (mimeType.includes('sheet') || mimeType.includes('spreadsheet')) return '📊'
  return '📄'
}

function getFileTypeFromMime(mimeType?: string): string {
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

function HighlightedText({ text, term }: { text: string; term: string }) {
  if (!term.trim()) return <>{text}</>
  const regex = new RegExp(`(${term.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')})`, 'gi')
  const parts = text.split(regex)
  return (
    <>
      {parts.map((part, i) =>
        regex.test(part)
          ? <mark key={i} className="bg-primary/15 text-primary rounded px-0.5">{part}</mark>
          : part
      )}
    </>
  )
}

function getSnippet(text: string, term: string, maxLen = 200): string {
  if (!term.trim()) return text.slice(0, maxLen) + (text.length > maxLen ? '…' : '')
  const idx = text.toLowerCase().indexOf(term.toLowerCase())
  if (idx === -1) return text.slice(0, maxLen) + (text.length > maxLen ? '…' : '')
  const start = Math.max(0, idx - 60)
  const end   = Math.min(text.length, idx + term.length + 140)
  return (start > 0 ? '…' : '') + text.slice(start, end) + (end < text.length ? '…' : '')
}

export function SearchPage() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [query, setQuery] = useState(searchParams.get('q') || '')
  const [inputValue, setInputValue] = useState(searchParams.get('q') || '')
  const [results, setResults] = useState<SearchResultDto[]>([])
  const [totalHits, setTotalHits] = useState(0)
  const [loading, setLoading] = useState(false)
  const [selectedTags, setSelectedTags] = useState<Set<string>>(new Set())
  const [fileTypeFilter, setFileTypeFilter] = useState('')
  const [page, setPage] = useState(1)
  const [tags, setTags] = useState<TagDto[]>([])
  const navigate = useNavigate()

  useEffect(() => {
    tagService.findAll(0, 100).then(res => setTags(res.content)).catch(() => {})
  }, [])

  useEffect(() => {
    const q = searchParams.get('q')
    if (q) { setQuery(q); setInputValue(q) }
  }, [searchParams])

  useEffect(() => { setPage(1) }, [query, selectedTags, fileTypeFilter])

  const doSearch = useCallback(async (q: string, p: number) => {
    if (!q.trim()) {
      setResults([])
      setTotalHits(0)
      return
    }
    setLoading(true)
    try {
      const res = await searchService.search(q, p - 1, PAGE_SIZE)
      setResults(res.results || [])
      setTotalHits(res.totalHits || 0)
    } catch {
      setResults([])
      setTotalHits(0)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { doSearch(query, page) }, [query, page, doSearch])

  const filtered = useMemo(() => {
    let arr = results
    if (fileTypeFilter) {
      arr = arr.filter(r => getFileTypeFromMime(r.mimeType) === fileTypeFilter)
    }
    // Tag filtering is done client-side since search results include tags as comma-separated string
    if (selectedTags.size > 0) {
      arr = arr.filter(r => {
        if (!r.tags) return false
        const resultTags = r.tags.split(',').map(t => t.trim().toLowerCase())
        return Array.from(selectedTags).some(tagId => {
          const tag = tags.find(t => t.id.toString() === tagId)
          return tag && resultTags.includes(tag.name.toLowerCase())
        })
      })
    }
    return arr
  }, [results, fileTypeFilter, selectedTags, tags])

  const totalPages = Math.max(1, Math.ceil(totalHits / PAGE_SIZE))

  function handleSearch(e: React.FormEvent) {
    e.preventDefault()
    setQuery(inputValue)
    setSearchParams(inputValue ? { q: inputValue } : {})
    setPage(1)
  }

  function toggleTag(tagId: string) {
    setSelectedTags(prev => {
      const next = new Set(prev)
      if (next.has(tagId)) next.delete(tagId); else next.add(tagId)
      return next
    })
  }

  function renderPageButtons() {
    const btns: (number | '...')[] = []
    if (totalPages <= 7) {
      for (let i = 1; i <= totalPages; i++) btns.push(i)
    } else {
      btns.push(1)
      if (page > 3) btns.push('...')
      for (let i = Math.max(2, page - 1); i <= Math.min(totalPages - 1, page + 1); i++) btns.push(i)
      if (page < totalPages - 2) btns.push('...')
      btns.push(totalPages)
    }
    return btns
  }

  const hasFilters = query || selectedTags.size > 0 || fileTypeFilter

  return (
    <div className="space-y-5">
      <form onSubmit={handleSearch}>
        <div className="search-bar flex items-center">
          <Search size={16} className="ml-3 text-zinc-400 shrink-0" />
          <input
            type="text"
            placeholder="Search by keyword, document name, tags…"
            value={inputValue}
            onChange={e => setInputValue(e.target.value)}
            className="flex-1 bg-transparent border-none outline-none px-3 py-2.5 text-sm placeholder:text-zinc-400"
            autoFocus
          />
          <button type="submit" className="btn btn-primary btn-sm rounded-lg px-5 mr-1">Search</button>
        </div>
      </form>

      <div className="flex flex-wrap items-center gap-1.5">
        <SlidersHorizontal size={12} className="text-zinc-400 shrink-0" />
        {['pdf', 'doc', 'spreadsheet', 'image'].map(type => (
          <button key={type} onClick={() => setFileTypeFilter(fileTypeFilter === type ? '' : type)}
            className={cn('tag-pill cursor-pointer', fileTypeFilter === type && '!bg-primary !text-white')}>
            {type.toUpperCase()}
          </button>
        ))}
        <span className="w-px h-4 bg-zinc-200 mx-0.5" />
        {tags.slice(0, 6).map(tag => (
          <button key={tag.id} onClick={() => toggleTag(tag.id.toString())}
            className={cn('tag-pill cursor-pointer', selectedTags.has(tag.id.toString()) && '!bg-primary !text-white')}>
            {tag.name}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="text-center py-12 text-zinc-400">Searching...</div>
      ) : hasFilters ? (
        <>
          <div className="flex items-center justify-between gap-3 flex-wrap">
            <div>
              <h2 className="text-lg font-bold">
                Results for <span className="text-primary">'{query || 'filtered'}'</span>
              </h2>
              <p className="text-sm text-zinc-500 mt-0.5">
                {totalHits.toLocaleString()} result{totalHits !== 1 ? 's' : ''} found
              </p>
            </div>
          </div>

          {filtered.length === 0 ? (
            <EmptyState icon={Search} title="No results found" description="Try adjusting your search terms or filters" />
          ) : (
            <>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                {filtered.map((doc, idx) => {
                  const snippet = getSnippet(doc.content || doc.description || '', query)
                  return (
                    <div
                      key={doc.documentId + '-' + idx}
                      onClick={() => navigate(`/preview/${doc.documentId}`)}
                      className="doc-card p-5 cursor-pointer group flex flex-col gap-3 relative"
                    >
                      <div className="text-2xl absolute top-4 right-4 w-9 h-9 rounded-lg flex items-center justify-center shrink-0">
                        {getFileTypeIcon(doc.mimeType)}
                      </div>

                      <div className="pr-12">
                        <h3 className="font-bold text-sm text-zinc-900 leading-snug line-clamp-2">
                          <HighlightedText text={doc.title} term={query} />
                        </h3>
                        <p className="text-xs text-zinc-400 mt-1">
                          {doc.originalFilename} · {doc.createdDate ? new Date(doc.createdDate).toLocaleDateString('en-CA') : ''}
                        </p>
                      </div>

                      <p className="text-xs text-zinc-500 leading-relaxed line-clamp-3 flex-1">
                        <HighlightedText text={snippet} term={query} />
                      </p>

                      <div className="flex items-center justify-between pt-2 border-t border-zinc-100">
                        <div className="flex flex-wrap gap-1">
                          {doc.tags?.split(',').slice(0, 2).map((tagName, i) => (
                            <Badge key={i} variant="tag" className="!text-[10px] !px-1.5 !py-0">{tagName.trim()}</Badge>
                          ))}
                          {(doc.tags?.split(',').length || 0) > 2 && (
                            <span className="text-[10px] text-zinc-400 self-center">+{(doc.tags?.split(',').length || 0) - 2}</span>
                          )}
                        </div>
                        <div className="flex items-center gap-2 shrink-0">
                          <span className="text-[11px] text-zinc-400">{formatSize(0)}</span>
                          <button
                            onClick={e => { e.stopPropagation(); navigate(`/preview/${doc.documentId}`) }}
                            className="w-7 h-7 rounded-md flex items-center justify-center opacity-0 group-hover:opacity-100 hover:bg-primary/10 text-primary transition-all"
                            title="Preview document"
                          >
                            <Eye size={13} />
                          </button>
                        </div>
                      </div>
                    </div>
                  )
                })}
              </div>

              {totalPages > 1 && (
                <div className="flex items-center justify-center gap-1 pt-2">
                  <button onClick={() => setPage(p => Math.max(1, p - 1))} disabled={page === 1}
                    className="w-8 h-8 rounded-lg flex items-center justify-center text-zinc-500 hover:bg-zinc-100 disabled:opacity-30 disabled:cursor-not-allowed transition-colors" aria-label="Previous">
                    <ChevronLeft size={16} />
                  </button>
                  {renderPageButtons().map((btn, i) =>
                    btn === '...'
                      ? <span key={`e-${i}`} className="w-8 h-8 flex items-center justify-center text-zinc-400 text-sm">…</span>
                      : <button key={btn} onClick={() => setPage(btn)}
                          className={cn('w-8 h-8 rounded-lg text-sm font-medium transition-colors',
                            page === btn ? 'bg-primary text-white shadow-sm' : 'text-zinc-600 hover:bg-zinc-100')}>
                          {btn}
                        </button>
                  )}
                  <button onClick={() => setPage(p => Math.min(totalPages, p + 1))} disabled={page === totalPages}
                    className="w-8 h-8 rounded-lg flex items-center justify-center text-zinc-500 hover:bg-zinc-100 disabled:opacity-30 disabled:cursor-not-allowed transition-colors" aria-label="Next">
                    <ChevronRight size={16} />
                  </button>
                </div>
              )}
            </>
          )}
        </>
      ) : (
        <div className="text-center py-16">
          <div className="w-14 h-14 rounded-xl bg-zinc-100 flex items-center justify-center mx-auto mb-4">
            <FileText size={24} className="text-zinc-400" />
          </div>
          <h3 className="text-base font-semibold mb-1">Start searching</h3>
          <p className="text-zinc-500 text-sm max-w-sm mx-auto">Type a keyword or select a filter above to search through your documents</p>
        </div>
      )}
    </div>
  )
}
