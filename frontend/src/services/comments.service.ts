import api from './api';

export interface CommentResponse {
  id: number
  content: string
  isResolved: boolean | null
  isEdited: boolean | null
  createdDate: string
  editedAt: string | null
  authorId: string | null
  authorEmail: string | null
  parentCommentId: number | null
}

export const commentsService = {
  async getComments(documentId: number, orgId: number): Promise<CommentResponse[]> {
    const res = await api.get(`/user/documents/${documentId}/comments`, {
      headers: { 'X-Org-Id': orgId.toString() },
    })
    return res.data
  },

  async addComment(documentId: number, orgId: number, content: string, parentCommentId?: number): Promise<CommentResponse> {
    const res = await api.post(`/user/documents/${documentId}/comments`,
      { content, parentCommentId },
      { headers: { 'X-Org-Id': orgId.toString() } },
    )
    return res.data
  },

  async editComment(documentId: number, commentId: number, orgId: number, content: string): Promise<CommentResponse> {
    const res = await api.put(`/user/documents/${documentId}/comments/${commentId}`,
      { content },
      { headers: { 'X-Org-Id': orgId.toString() } },
    )
    return res.data
  },

  async toggleResolve(documentId: number, commentId: number, orgId: number): Promise<CommentResponse> {
    const res = await api.post(`/user/documents/${documentId}/comments/${commentId}/resolve`,
      {},
      { headers: { 'X-Org-Id': orgId.toString() } },
    )
    return res.data
  },

  async deleteComment(documentId: number, commentId: number, orgId: number): Promise<void> {
    await api.delete(`/user/documents/${documentId}/comments/${commentId}`, {
      headers: { 'X-Org-Id': orgId.toString() },
    })
  },
}
