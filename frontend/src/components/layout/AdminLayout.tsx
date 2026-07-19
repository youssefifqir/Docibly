import { Outlet, Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'
import { Building2, Users, LayoutDashboard, LogOut, Settings } from 'lucide-react'

export function AdminLayout() {
  const { user, logout } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/')
  }

  const navItems = [
    { href: '/admin/tenants', label: 'Tenants', icon: Building2 },
    { href: '/admin/users', label: 'Users', icon: Users },
    { href: '/dashboard', label: 'App Dashboard', icon: LayoutDashboard },
  ]

  return (
    <div className="flex h-screen bg-zinc-50">
      <aside className="w-60 bg-[#1a1a2e] flex flex-col shrink-0">
        <div className="p-5 border-b border-white/5">
          <div className="flex items-center gap-2.5">
            <img src="/ged-logo.svg" alt="Docibly" className="w-8 h-8 rounded-lg shadow-lg shadow-[#e8622a]/30" />
            <div>
              <span className="text-white font-bold text-base">Docibly</span>
              <span className="text-[#e8622a] text-[10px] block font-medium uppercase tracking-wider">Admin Panel</span>
            </div>
          </div>
        </div>

        <nav className="flex-1 p-3 space-y-0.5">
          {navItems.map(item => {
            const active = location.pathname.startsWith(item.href)
            return (
              <Link
                key={item.href}
                to={item.href}
                className={`flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${active ? 'bg-[#e8622a]/15 text-[#e8622a]' : 'text-white/50 hover:text-white hover:bg-white/5'}`}
              >
                <item.icon size={18} />
                {item.label}
              </Link>
            )
          })}
        </nav>

        <div className="p-3 border-t border-white/5">
          <div className="flex items-center gap-2.5 px-3 py-2 mb-1">
            <div className="w-7 h-7 rounded-full bg-[#e8622a] flex items-center justify-center text-white text-xs font-bold">
              {user?.name?.[0] || 'A'}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-white text-xs font-medium truncate">{user?.email || 'Admin'}</p>
              <p className="text-[#e8622a] text-[10px] uppercase font-medium">Super Admin</p>
            </div>
          </div>
          <button onClick={handleLogout} className="flex items-center gap-3 px-3 py-2 rounded-lg text-white/40 hover:text-red-400 hover:bg-white/5 text-sm w-full transition-colors">
            <LogOut size={16} /> Sign out
          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-auto">
        <div className="p-6">
          <Outlet />
        </div>
      </main>
    </div>
  )
}
