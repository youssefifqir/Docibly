import { Search, Bell, Plus, CheckCheck } from 'lucide-react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { Avatar } from '@/components/ui/Avatar'
import { Button } from '@/components/ui/Button'
import { notificationsService, type NotificationItem } from '@/services/notifications.service'
import { cn } from '@/lib/cn'

const pageTitles: Record<string, string> = {
  '/':        'Dashboard',
  '/library': 'Document Library',
  '/upload':  'Upload Document',
  '/shared':  'Shared Documents',
  '/search':  'Search Documents',
  '/profile': 'Profile & Settings',
  '/members': 'Members',
}

export function Navbar() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [searchQuery, setSearchQuery] = useState('')
  const [notifications, setNotifications] = useState<NotificationItem[]>([])
  const [unreadCount, setUnreadCount] = useState(0)
  const [showDropdown, setShowDropdown] = useState(false)
  const dropdownRef = useRef<HTMLDivElement>(null)

  const pageTitle = pageTitles[location.pathname] || 'Documents'

  useEffect(() => {
    loadNotifications()
    const interval = setInterval(loadNotifications, 30000)
    return () => clearInterval(interval)
  }, [])

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target as Node)) {
        setShowDropdown(false)
      }
    }
    document.addEventListener('mousedown', handleClick)
    return () => document.removeEventListener('mousedown', handleClick)
  }, [])

  async function loadNotifications() {
    try {
      const [items, count] = await Promise.all([
        notificationsService.getMyNotifications(),
        notificationsService.getUnreadCount(),
      ])
      setNotifications(items)
      setUnreadCount(count)
    } catch {}
  }

  async function handleMarkRead(id: number) {
    try {
      await notificationsService.markRead(id)
      loadNotifications()
    } catch {}
  }

  async function handleMarkAllRead() {
    try {
      await notificationsService.markAllRead()
      loadNotifications()
    } catch {}
  }

  function handleSearchSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`)
    }
  }

  function getNotifIcon(type: string) {
    if (type.includes('COMMENT')) return '💬'
    if (type.includes('INVITE') || type.includes('MEMBER')) return '👋'
    if (type.includes('SHARE')) return '🔗'
    if (type.includes('OCR')) return '🔍'
    return '🔔'
  }

  const recentNotifs = notifications.slice(0, 10)

  return (
    <header className="h-16 flex items-center justify-between px-6 bg-white border-b border-zinc-200 sticky top-0 z-30">
      <div>
        <h1 className="text-base font-semibold text-zinc-900">{pageTitle}</h1>
      </div>

      <form onSubmit={handleSearchSubmit} className="hidden md:flex items-center flex-1 max-w-md mx-6">
        <div className="search-bar flex items-center w-full">
          <Search size={16} className="ml-3 text-zinc-400 shrink-0" />
          <input
            type="text"
            placeholder="Search documents..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
            className="flex-1 bg-transparent border-none outline-none px-3 py-2 text-sm placeholder:text-zinc-400"
          />
        </div>
      </form>

      <div className="flex items-center gap-2">
        <Button
          variant="cta"
          size="sm"
          onClick={() => navigate('/upload')}
          className="hidden sm:flex"
        >
          <Plus size={14} />
          Upload
        </Button>

        <div className="relative" ref={dropdownRef}>
          <button
            onClick={() => setShowDropdown(!showDropdown)}
            className="btn btn-ghost btn-sm btn-circle relative"
          >
            <Bell size={18} className="text-zinc-500" />
            {unreadCount > 0 && (
              <span className="absolute -top-0.5 -right-0.5 w-4 h-4 bg-red-500 text-white text-[9px] font-bold rounded-full flex items-center justify-center">
                {unreadCount > 9 ? '9+' : unreadCount}
              </span>
            )}
          </button>

          {showDropdown && (
            <div className="absolute right-0 top-full mt-2 w-80 bg-white border rounded-xl shadow-lg overflow-hidden z-50">
              <div className="flex items-center justify-between px-4 py-3 border-b">
                <span className="text-sm font-semibold">Notifications</span>
                {unreadCount > 0 && (
                  <button onClick={handleMarkAllRead} className="text-xs text-primary hover:underline flex items-center gap-1">
                    <CheckCheck size={12} /> Mark all read
                  </button>
                )}
              </div>
              <div className="max-h-72 overflow-y-auto">
                {recentNotifs.length === 0 ? (
                  <p className="text-sm text-zinc-400 text-center py-8">No notifications</p>
                ) : (
                  recentNotifs.map(n => (
                    <button
                      key={n.id}
                      onClick={() => { handleMarkRead(n.id); setShowDropdown(false) }}
                      className={cn(
                        'w-full text-left px-4 py-3 flex items-start gap-3 hover:bg-zinc-50 transition-colors border-b border-zinc-50 last:border-0',
                        !n.read && 'bg-blue-50/30'
                      )}
                    >
                      <span className="text-lg shrink-0 mt-0.5">{getNotifIcon(n.type)}</span>
                      <div className="min-w-0 flex-1">
                        <p className="text-sm font-medium truncate">{n.title}</p>
                        {n.message && <p className="text-xs text-zinc-500 line-clamp-2">{n.message}</p>}
                        <p className="text-[10px] text-zinc-400 mt-0.5">
                          {n.createdAt ? new Date(n.createdAt).toLocaleDateString() : ''}
                        </p>
                      </div>
                      {!n.read && <span className="w-2 h-2 rounded-full bg-blue-500 shrink-0 mt-1.5" />}
                    </button>
                  ))
                )}
              </div>
            </div>
          )}
        </div>

        {user && (
          <button
            onClick={() => navigate('/profile')}
            className="flex items-center gap-2 hover:bg-zinc-100 rounded-lg px-2 py-1.5 transition-colors"
          >
            <Avatar name={user.name} size="sm" />
            <span className="text-sm font-medium text-zinc-700 hidden lg:inline">{user.name.split(' ')[0]}</span>
          </button>
        )}
      </div>
    </header>
  )
}
