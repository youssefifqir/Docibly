import { X, Plus, Loader2 } from 'lucide-react'
import { useState, useEffect } from 'react'
import { tagService } from '@/services/tags.service'
import type { TagDto } from '@/types/api'

interface TagInputProps {
  selectedTags: TagDto[]
  onChange: (tags: TagDto[]) => void
}

export function TagInput({ selectedTags, onChange }: TagInputProps) {
  const [allTags, setAllTags] = useState<TagDto[]>([])
  const [isOpen, setIsOpen] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    tagService.findAll(0, 100)
      .then((res: { content: TagDto[] }) => setAllTags(res.content))
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [])

  const available = allTags.filter(t => !selectedTags.some(s => s.id === t.id))

  return (
    <div>
      <label className="text-xs font-medium text-zinc-500 mb-1.5 block">Tags</label>
      <div className="flex flex-wrap gap-1.5 items-center">
        {selectedTags.map(tag => (
          <span key={tag.id} className="tag-pill inline-flex items-center gap-1">
            {tag.name}
            <button onClick={() => onChange(selectedTags.filter(t => t.id !== tag.id))} className="hover:text-red-500 transition-colors">
              <X size={10} />
            </button>
          </span>
        ))}
        <div className="relative">
          <button
            onClick={() => setIsOpen(!isOpen)}
            className="w-7 h-7 rounded-full border-2 border-dashed border-zinc-300 flex items-center justify-center hover:border-primary hover:text-primary transition-colors text-zinc-400"
          >
            {loading ? <Loader2 size={12} className="animate-spin" /> : <Plus size={12} />}
          </button>
          {isOpen && available.length > 0 && (
            <>
              <div className="fixed inset-0 z-10" onClick={() => setIsOpen(false)} />
              <div className="absolute left-0 top-9 z-20 bg-white rounded-lg shadow-lg border border-zinc-200 py-1 min-w-[160px] max-h-[200px] overflow-y-auto">
                {available.map(tag => (
                  <button
                    key={tag.id}
                    onClick={() => { onChange([...selectedTags, tag]); setIsOpen(false) }}
                    className="flex items-center gap-2 px-3 py-1.5 text-sm w-full hover:bg-zinc-50 transition-colors"
                  >
                    <span className="w-1.5 h-1.5 rounded-full bg-primary/40" />
                    {tag.name}
                  </button>
                ))}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  )
}
