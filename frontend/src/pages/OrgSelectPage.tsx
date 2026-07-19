import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/contexts/AuthContext'

const ACCENT = '#e8622a'

export function OrgSelectPage() {
  const { orgs, switchOrg } = useAuth()
  const navigate = useNavigate()
  const [tab, setTab] = useState<'owned' | 'invited'>('owned')

  const owned = orgs.filter(o => o.role === 'Owner')
  const invited = orgs.filter(o => o.role !== 'Owner')

  const handleSelect = (id: number) => {
    switchOrg(id)
    navigate('/dashboard')
  }

  const list = tab === 'owned' ? owned : invited

  return (
    <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#f4f4f5' }}>
      <div style={{ background: '#fff', borderRadius: 14, boxShadow: '0 4px 24px rgba(0,0,0,.07)', width: 480, maxWidth: '94vw', overflow: 'hidden' }}>
        <div style={{ padding: '28px 28px 0' }}>
          <h2 style={{ fontSize: 20, fontWeight: 700, color: '#18181b', margin: 0 }}>Select Organization</h2>
          <p style={{ fontSize: 13.5, color: '#71717a', marginTop: 5 }}>Choose which organization to open</p>
        </div>

        <div style={{ display: 'flex', borderBottom: '1px solid #e4e4e7', margin: '16px 28px 0', gap: 0 }}>
          <button onClick={() => setTab('owned')}
            style={{ flex: 1, padding: '10px 0', border: 'none', borderBottom: tab === 'owned' ? `2px solid ${ACCENT}` : '2px solid transparent', background: 'none', cursor: 'pointer', fontSize: 13.5, fontWeight: 600, color: tab === 'owned' ? ACCENT : '#71717a', transition: 'all .15s' }}>
            My Organization{owned.length > 0 ? ` (${owned.length})` : ''}
          </button>
          <button onClick={() => setTab('invited')}
            style={{ flex: 1, padding: '10px 0', border: 'none', borderBottom: tab === 'invited' ? `2px solid ${ACCENT}` : '2px solid transparent', background: 'none', cursor: 'pointer', fontSize: 13.5, fontWeight: 600, color: tab === 'invited' ? ACCENT : '#71717a', transition: 'all .15s' }}>
            Invited{invited.length > 0 ? ` (${invited.length})` : ''}
          </button>
        </div>

        <div style={{ padding: '16px 28px 28px', maxHeight: 360, overflowY: 'auto' }}>
          {list.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '32px 0', color: '#a1a1aa', fontSize: 14 }}>
              {tab === 'owned' ? 'No organizations yet. Create one to get started.' : 'No pending invitations.'}
            </div>
          ) : list.map(o => (
            <div key={o.id} onClick={() => handleSelect(o.id)}
              style={{ display: 'flex', alignItems: 'center', gap: 14, padding: '14px 16px', borderRadius: 10, cursor: 'pointer', background: '#fafafa', marginBottom: 8, border: '1px solid #e4e4e7', transition: 'all .15s' }}
              onMouseEnter={e => { e.currentTarget.style.borderColor = ACCENT; e.currentTarget.style.background = '#fff4ed' }}
              onMouseLeave={e => { e.currentTarget.style.borderColor = '#e4e4e7'; e.currentTarget.style.background = '#fafafa' }}>
              <div style={{ width: 40, height: 40, borderRadius: 10, background: ACCENT, display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 700, fontSize: 16, flex: 'none' }}>
                {o.name.charAt(0).toUpperCase()}
              </div>
              <div style={{ flex: 1 }}>
                <div style={{ fontWeight: 600, fontSize: 14, color: '#18181b' }}>{o.name}</div>
                <div style={{ fontSize: 12, color: '#a1a1aa', marginTop: 2 }}>{o.role}</div>
              </div>
              <span style={{ color: '#a1a1aa', fontSize: 18 }}>→</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
