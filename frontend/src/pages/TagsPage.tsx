import { useState, useEffect } from 'react'
import { tagService } from '@/services/tags.service'
import { Plus, X } from 'lucide-react'

export function TagsPage() {
  const [tags, setTags] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [newTag, setNewTag] = useState({ name: '', color: '#e8622a' })

  async function load() {
    setLoading(true)
    try {
      const res = await tagService.findAll(0, 100)
      setTags(res.content)
    } catch {}
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [])

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault()
    if (!newTag.name.trim()) return
    try {
      await tagService.create(newTag)
      setNewTag({ name: '', color: '#e8622a' })
      load()
    } catch {}
  }

  async function handleDelete(id: number) {
    try { await tagService.delete(id); load() } catch {}
  }

  return (
    <div className="max-w-3xl mx-auto">
      <h1 className="text-2xl font-bold mb-1">Tags</h1>
      <p className="text-zinc-500 text-sm mb-6">Manage tags used to organize documents</p>

      <form onSubmit={handleCreate} className="flex items-end gap-3 mb-6 bg-white rounded-xl border border-zinc-200 p-4">
        <div className="flex-1">
          <label className="text-xs font-medium text-zinc-500 mb-1 block">Tag name</label>
          <input value={newTag.name} onChange={e => setNewTag(p => ({ ...p, name: e.target.value }))} className="w-full px-3 py-2 border border-zinc-200 rounded-lg text-sm" placeholder="e.g. contract, invoice, urgent" />
        </div>
        <div>
          <label className="text-xs font-medium text-zinc-500 mb-1 block">Color</label>
          <input type="color" value={newTag.color} onChange={e => setNewTag(p => ({ ...p, color: e.target.value }))} className="w-10 h-9 border border-zinc-200 rounded-lg cursor-pointer" />
        </div>
        <button type="submit" className="px-4 py-2 bg-[#e8622a] text-white rounded-lg text-sm font-medium flex items-center gap-1.5">
          <Plus size={14} /> Add
        </button>
      </form>

      {loading ? <p className="text-zinc-400 text-sm">Loading...</p> : (
        <div className="flex flex-wrap gap-2">
          {tags.map(tag => (
            <span key={tag.id} className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm font-medium border" style={{ borderColor: tag.color || '#ccc', backgroundColor: (tag.color || '#ccc') + '15', color: tag.color || '#666' }}>
              {tag.name}
              <button onClick={() => handleDelete(tag.id)} className="hover:opacity-70"><X size={12} /></button>
            </span>
          ))}
          {tags.length === 0 && <p className="text-zinc-400 text-sm">No tags yet</p>}
        </div>
      )}
    </div>
  )
}
