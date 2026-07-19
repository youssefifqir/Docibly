import api from './api';

export interface NotificationItem {
  id: number
  type: string
  title: string
  message: string | null
  read: boolean | null
  readAt: string | null
  createdAt: string | null
  targetUrl: string | null
  relatedEntityType: string | null
  relatedEntityId: string | null
}

export const notificationsService = {
  async getMyNotifications(): Promise<NotificationItem[]> {
    const res = await api.get('/user/notifications')
    return res.data
  },

  async getUnreadCount(): Promise<number> {
    const res = await api.get('/user/notifications/unread-count')
    return res.data.count ?? 0
  },

  async markRead(id: number): Promise<NotificationItem> {
    const res = await api.post(`/user/notifications/${id}/read`)
    return res.data
  },

  async markAllRead(): Promise<void> {
    await api.post('/user/notifications/read-all')
  },
}
