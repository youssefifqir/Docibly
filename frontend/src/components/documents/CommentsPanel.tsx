import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { commentsService, type CommentResponse } from '@/services/comments.service'
import { Button } from '@/components/ui/Button'
import { Avatar } from '@/components/ui/Avatar'
import { MessageSquare, Reply, Edit3, Trash2, CheckCircle, XCircle } from 'lucide-react'

interface CommentsPanelProps {
  documentId: number
}

export function CommentsPanel({ documentId }: CommentsPanelProps) {
  const { orgId, user } = useAuth()
  const [comments, setComments] = useState<CommentResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [newComment, setNewComment] = useState('')
  const [replyTo, setReplyTo] = useState<number | null>(null)
  const [replyText, setReplyText] = useState('')
  const [editingId, setEditingId] = useState<number | null>(null)
  const [editText, setEditText] = useState('')
  const [sending, setSending] = useState(false)

  async function load() {
    if (!orgId) return
    setLoading(true)
    try {
      const data = await commentsService.getComments(documentId, orgId)
      setComments(data)
    } catch {} finally { setLoading(false) }
  }

  useEffect(() => { load() }, [documentId, orgId])

  const topLevel = comments.filter(c => !c.parentCommentId)
  const replies = (parentId: number) => comments.filter(c => c.parentCommentId === parentId)

  async function handleAdd() {
    if (!orgId || !newComment.trim()) return
    setSending(true)
    try {
      await commentsService.addComment(documentId, orgId, newComment.trim())
      setNewComment('')
      load()
    } finally { setSending(false) }
  }

  async function handleReply(parentId: number) {
    if (!orgId || !replyText.trim()) return
    setSending(true)
    try {
      await commentsService.addComment(documentId, orgId, replyText.trim(), parentId)
      setReplyText('')
      setReplyTo(null)
      load()
    } finally { setSending(false) }
  }

  async function handleEdit(commentId: number) {
    if (!orgId || !editText.trim()) return
    try {
      await commentsService.editComment(documentId, commentId, orgId, editText.trim())
      setEditingId(null)
      setEditText('')
      load()
    } catch {}
  }

  async function handleResolve(commentId: number) {
    if (!orgId) return
    try {
      await commentsService.toggleResolve(documentId, commentId, orgId)
      load()
    } catch {}
  }

  async function handleDelete(commentId: number) {
    if (!orgId || !window.confirm('Delete this comment?')) return
    try {
      await commentsService.deleteComment(documentId, commentId, orgId)
      load()
    } catch {}
  }

  function renderComment(c: CommentResponse, depth = 0) {
    const isOwner = user?.id === c.authorId || user?.email === c.authorEmail
    const childReplies = replies(c.id)

    return (
      <div key={c.id} className={`${depth > 0 ? 'ml-6 pl-4 border-l-2 border-zinc-100' : ''}`}>
        <div className={`py-3 ${c.isResolved ? 'opacity-50' : ''}`}>
          <div className="flex items-start gap-2.5">
            <Avatar name={c.authorEmail || '?'} size="sm" />
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                <span className="text-xs font-medium">{c.authorEmail?.split('@')[0] || 'Anonymous'}</span>
                <span className="text-[10px] text-zinc-400">
                  {new Date(c.createdDate).toLocaleDateString()}
                </span>
                {c.isEdited && <span className="text-[10px] text-zinc-400">(edited)</span>}
                {c.isResolved && <CheckCircle size={12} className="text-green-500" />}
              </div>

              {editingId === c.id ? (
                <div className="mt-1 space-y-1.5">
                  <textarea value={editText} onChange={e => setEditText(e.target.value)}
                    className="w-full border border-zinc-300 rounded-lg p-2 text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none" rows={2} />
                  <div className="flex gap-1">
                    <Button size="sm" onClick={() => handleEdit(c.id)} disabled={!editText.trim()}>Save</Button>
                    <Button size="sm" variant="ghost" onClick={() => { setEditingId(null); setEditText('') }}>Cancel</Button>
                  </div>
                </div>
              ) : (
                <p className="text-sm text-zinc-700 mt-0.5">{c.content}</p>
              )}

              <div className="flex items-center gap-2 mt-1.5">
                <button onClick={() => setReplyTo(replyTo === c.id ? null : c.id)}
                  className="text-[11px] text-zinc-400 hover:text-zinc-600 flex items-center gap-0.5">
                  <Reply size={11} /> Reply
                </button>
                {isOwner && !editingId && (
                  <>
                    <button onClick={() => { setEditingId(c.id); setEditText(c.content) }}
                      className="text-[11px] text-zinc-400 hover:text-zinc-600 flex items-center gap-0.5">
                      <Edit3 size={11} /> Edit
                    </button>
                    <button onClick={() => handleDelete(c.id)}
                      className="text-[11px] text-red-400 hover:text-red-600 flex items-center gap-0.5">
                      <Trash2 size={11} /> Delete
                    </button>
                  </>
                )}
                <button onClick={() => handleResolve(c.id)}
                  className="text-[11px] text-zinc-400 hover:text-green-600 flex items-center gap-0.5">
                  <CheckCircle size={11} /> {c.isResolved ? 'Unresolve' : 'Resolve'}
                </button>
              </div>

              {/* Reply input */}
              {replyTo === c.id && (
                <div className="mt-2 space-y-1.5">
                  <textarea value={replyText} onChange={e => setReplyText(e.target.value)}
                    placeholder="Write a reply..."
                    className="w-full border border-zinc-300 rounded-lg p-2 text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none" rows={2} />
                  <div className="flex gap-1">
                    <Button size="sm" onClick={() => handleReply(c.id)} disabled={!replyText.trim() || sending}>Reply</Button>
                    <Button size="sm" variant="ghost" onClick={() => { setReplyTo(null); setReplyText('') }}>Cancel</Button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Render replies */}
        {childReplies.map(r => renderComment(r, depth + 1))}
      </div>
    )
  }

  return (
    <div className="border rounded-xl bg-white">
      <div className="px-5 py-4 border-b flex items-center gap-2">
        <MessageSquare size={16} className="text-zinc-400" />
        <h3 className="font-semibold text-sm">Comments</h3>
        <span className="text-xs text-zinc-400">({comments.length})</span>
      </div>

      {/* New comment input */}
      <div className="px-5 py-4 border-b">
        <textarea value={newComment} onChange={e => setNewComment(e.target.value)}
          placeholder="Add a comment..."
          className="w-full border border-zinc-300 rounded-lg p-3 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none" rows={2} />
        <div className="flex justify-end mt-2">
          <Button size="sm" onClick={handleAdd} disabled={!newComment.trim() || sending}>
            {sending ? 'Posting...' : 'Comment'}
          </Button>
        </div>
      </div>

      {/* Comments list */}
      <div className="px-5 py-2 max-h-96 overflow-y-auto">
        {loading ? (
          <p className="text-sm text-zinc-400 py-4 text-center">Loading...</p>
        ) : topLevel.length === 0 ? (
          <p className="text-sm text-zinc-400 py-4 text-center">No comments yet</p>
        ) : (
          topLevel.map(c => renderComment(c))
        )}
      </div>
    </div>
  )
}
