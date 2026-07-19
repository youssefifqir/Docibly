import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import {
  LayoutDashboard,
  FolderOpen,
  Upload,
  Search,
  Users,
  Settings,
  LogOut,
  ChevronLeft,
  ChevronRight,
  ChevronDown,
  ScanLine,
  Shield,
  Tag,
  Building2,
  UserPlus,
  ScrollText,
} from 'lucide-react'
import { cn } from '@/lib/cn'
import { useAuth } from '@/contexts/AuthContext'
import { Avatar } from '@/components/ui/Avatar'
import { useState, useRef, useEffect } from 'react'

const navItems = [
  { to: '/dashboard',   icon: LayoutDashboard, label: 'Dashboard'   },
  { to: '/library',     icon: FolderOpen,      label: 'Library',     permission: 'doc.view'   },
  { to: '/upload',      icon: Upload,          label: 'Upload',      permission: 'doc.upload' },
  { to: '/digitize',    icon: ScanLine,        label: 'Digitize'    },
  { to: '/tags',        icon: Tag,             label: 'Tags'        },
  { to: '/departments', icon: Building2,       label: 'Departments' },
  { to: '/shared',      icon: Users,           label: 'Shared'      },
  { to: '/members',     icon: UserPlus,        label: 'Members'     },
  { to: '/roles',       icon: Shield,          label: 'Roles'       },
  { to: '/audit',       icon: ScrollText,      label: 'Audit Log',   permission: 'org.audit'  },
  { to: '/search',      icon: Search,          label: 'Search'      },
]

export function Sidebar() {
  const { user, logout, orgName, orgId, orgs, switchOrg, hasPermission } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()
  const [collapsed, setCollapsed] = useState(false)
  const [orgOpen, setOrgOpen] = useState(false)
  const orgRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (orgRef.current && !orgRef.current.contains(e.target as Node)) setOrgOpen(false)
    }
    document.addEventListener('mousedown', handleClick)
    return () => document.removeEventListener('mousedown', handleClick)
  }, [])

  const isSuperAdmin = user?.role === 'ROLE_ADMIN'

  return (
    <aside
      className={cn(
        'fixed left-0 top-0 bottom-0 z-40 flex flex-col transition-all duration-200',
        collapsed ? 'w-[64px]' : 'w-[240px]',
      )}
      style={{ background: '#1a1a2e' }}
    >
      {/* Logo */}
      <div className={cn('flex items-center gap-3 px-4 h-16 shrink-0', collapsed && 'justify-center px-0')}>
        <img
          src="/ged-logo.svg"
          alt="Docibly"
          className="w-8 h-8 rounded-lg object-cover shrink-0 shadow-md shadow-[#e8622a]/30"
        />
        {!collapsed && (
          <div>
            <span className="text-white font-semibold text-base">Docibly</span>
            <span className="text-white/30 text-[10px] block -mt-0.5">Document Platform</span>
          </div>
        )}
      </div>

      {/* Collapse toggle */}
      <button
        onClick={() => setCollapsed(c => !c)}
        className="absolute -right-3 top-[46px] w-6 h-6 rounded-full bg-zinc-800 border border-zinc-600 flex items-center justify-center hover:bg-zinc-700 transition-colors z-50"
      >
        {collapsed
          ? <ChevronRight size={12} className="text-zinc-300" />
          : <ChevronLeft size={12} className="text-zinc-300" />
        }
      </button>

      {/* Org switcher */}
      {!collapsed && (
        <div ref={orgRef} className="px-3 mb-1 relative">
          <button onClick={() => setOrgOpen(o => !o)}
            className="flex items-center gap-2 w-full px-3 py-2 rounded-lg text-white/80 hover:bg-white/5 text-sm transition-colors">
            <Building2 size={16} className="shrink-0" />
            <span className="truncate flex-1 text-left">{orgName || 'Select Org'}</span>
            <ChevronDown size={14} className={cn('transition-transform', orgOpen && 'rotate-180')} />
          </button>
          {orgOpen && (
            <div className="absolute left-3 right-3 top-full mt-1 bg-zinc-800 border border-zinc-700 rounded-lg shadow-xl overflow-hidden z-50">
              {orgs.map(o => (
                <button key={o.id} onClick={() => { switchOrg(o.id); setOrgOpen(false) }}
                  className={cn('flex items-center gap-2 w-full px-3 py-2 text-sm text-left transition-colors',
                    o.id === orgId ? 'text-white bg-white/10' : 'text-white/70 hover:bg-white/5')}>
                  <div className="w-6 h-6 rounded bg-[#e8622a]/20 flex items-center justify-center text-[10px] font-bold text-[#e8622a] shrink-0">
                    {o.name.charAt(0).toUpperCase()}
                  </div>
                  <span className="truncate">{o.name}</span>
                  <span className="text-[10px] text-white/30 ml-auto">{o.role}</span>
                </button>
              ))}
              {orgs.length > 1 && (
                <button onClick={() => { setOrgOpen(false); navigate('/org/select') }}
                  className="flex items-center gap-2 w-full px-3 py-2 text-xs text-white/50 hover:bg-white/5 border-t border-white/10">
                  Switch Organization
                </button>
              )}
            </div>
          )}
        </div>
      )}

      {/* Navigation */}
      <nav className="flex-1 px-3 space-y-0.5 overflow-y-auto">
        {!collapsed && (
          <span className="text-[10px] font-semibold uppercase tracking-wider text-white/25 px-3 mb-2 block">
            Menu
          </span>
        )}
        {navItems.filter(item => !item.permission || hasPermission(item.permission)).map(item => {
          const isActive = location.pathname === item.to ||
            location.pathname.startsWith(item.to)
          return (
            <NavLink
              key={item.to}
              to={item.to}
              className={cn(
                'sidebar-link',
                isActive && 'active',
                collapsed && 'justify-center px-0 mx-1',
              )}
              title={collapsed ? item.label : undefined}
            >
              <item.icon size={18} />
              {!collapsed && <span>{item.label}</span>}
            </NavLink>
          )
        })}

        {isSuperAdmin && (
          <>
            {!collapsed && (
              <span className="text-[10px] font-semibold uppercase tracking-wider text-[#e8622a]/60 px-3 mb-2 mt-5 block">
                Admin
              </span>
            )}
            <NavLink
              to="/admin/tenants"
              className={cn(
                'sidebar-link',
                location.pathname.startsWith('/admin') && 'active',
                collapsed && 'justify-center px-0 mx-1',
              )}
              title={collapsed ? 'Admin Panel' : undefined}
            >
              <Shield size={18} />
              {!collapsed && <span>Admin Panel</span>}
            </NavLink>
          </>
        )}
      </nav>

      {/* User section */}
      <div className={cn('px-3 pb-3 mt-auto', collapsed && 'px-2')}>
        <div className="border-t border-white/8 pt-3 mb-2" />
        <NavLink
          to="/profile"
          className={cn('sidebar-link', location.pathname === '/profile' && 'active', collapsed && 'justify-center px-0 mx-1')}
          title={collapsed ? 'Settings' : undefined}
        >
          <Settings size={18} />
          {!collapsed && <span>Settings</span>}
        </NavLink>
        <button
          onClick={logout}
          className={cn('sidebar-link w-full hover:!text-red-400', collapsed && 'justify-center px-0 mx-1')}
          title={collapsed ? 'Logout' : undefined}
        >
          <LogOut size={18} />
          {!collapsed && <span>Logout</span>}
        </button>

        {!collapsed && user && (
          <div className="flex items-center gap-2.5 p-2.5 mt-2 rounded-lg bg-white/5">
            <Avatar name={user.name} size="sm" />
            <div className="min-w-0">
              <div className="text-xs font-medium text-white truncate">{user.name}</div>
              <div className="text-[10px] text-white/30 truncate">{user.email}</div>
            </div>
          </div>
        )}
      </div>
    </aside>
  )
}
