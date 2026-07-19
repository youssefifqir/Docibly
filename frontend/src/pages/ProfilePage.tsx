import { useState, useEffect } from 'react'
import { useAuth } from '@/contexts/AuthContext'
import { userService } from '@/services/user.service'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'
import { Avatar } from '@/components/ui/Avatar'
import type { ProfileResponse } from '@/types/api'
import { User, Mail, Phone, Briefcase, FileText, Save, Shield, Calendar, HardDrive, CheckCircle2, XCircle } from 'lucide-react'

export function ProfilePage() {
  const { user: authUser } = useAuth()
  const [profile, setProfile] = useState<ProfileResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [jobTitle, setJobTitle] = useState('')
  const [bio, setBio] = useState('')
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(false)

  useEffect(() => {
    userService.getProfile()
      .then(p => {
        setProfile(p)
        setFirstName(p.firstName)
        setLastName(p.lastName)
        setJobTitle(p.jobTitle || '')
        setBio(p.bio || '')
      })
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [])

  async function handleSave(e: React.FormEvent) {
    e.preventDefault()
    setSaving(true)
    try {
      await userService.updateProfile({ firstName, lastName, jobTitle: jobTitle || undefined, bio: bio || undefined })
      setSaved(true)
      setTimeout(() => setSaved(false), 2000)
    } catch {}
    setSaving(false)
  }

  if (loading) return <div className="p-6 text-center text-zinc-400">Loading...</div>
  if (!profile) return <div className="p-6 text-center text-zinc-500">Could not load profile.</div>

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h2 className="text-xl font-bold">Profile & Settings</h2>
        <p className="text-sm text-zinc-500 mt-0.5">Manage your account and preferences</p>
      </div>

      <div className="bg-white rounded-xl border border-zinc-200 overflow-hidden">
        <div className="h-24 bg-primary" />
        <div className="px-6 pb-5 -mt-8">
          <div className="ring-4 ring-white rounded-full inline-block">
            <Avatar name={`${profile.firstName} ${profile.lastName}`} size="lg" className="!w-16 !h-16 !text-xl" />
          </div>
          <div className="mt-2">
            <h3 className="text-lg font-bold">{profile.firstName} {profile.lastName}</h3>
            <p className="text-sm text-zinc-500">{profile.email}</p>
            <div className="flex flex-wrap gap-x-3 gap-y-1 mt-1 text-xs text-zinc-400">
              {profile.jobTitle && <span className="flex items-center gap-1"><Briefcase size={11} />{profile.jobTitle}</span>}
              <span className="flex items-center gap-1"><Shield size={11} />{profile.authProvider || 'LOCAL'}</span>
              <span className="flex items-center gap-1"><Calendar size={11} />Joined {profile.createdDate ? new Date(profile.createdDate).toLocaleDateString() : '—'}</span>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="bg-white rounded-xl border border-zinc-200 p-4 flex items-center gap-3">
          <HardDrive size={18} className="text-zinc-400" />
          <div>
            <p className="text-xs text-zinc-500">Storage Used</p>
            <p className="font-semibold">{profile.storageUsedBytes ? `${(profile.storageUsedBytes / (1024 * 1024)).toFixed(1)} MB` : '0 B'}</p>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-zinc-200 p-4 flex items-center gap-3">
          {profile.emailVerified ? <CheckCircle2 size={18} className="text-green-500" /> : <XCircle size={18} className="text-zinc-300" />}
          <div>
            <p className="text-xs text-zinc-500">Email Verified</p>
            <p className="font-semibold">{profile.emailVerified ? 'Yes' : 'No'}</p>
          </div>
        </div>
      </div>

      <form onSubmit={handleSave} className="space-y-5">
        <div className="bg-white rounded-xl border border-zinc-200 p-6 space-y-4">
          <h3 className="font-semibold text-sm flex items-center gap-2">
            <User size={15} className="text-primary" /> Account Information
          </h3>
          <div className="grid grid-cols-2 gap-4">
            <Input label="First Name" value={firstName} onChange={e => setFirstName(e.target.value)} />
            <Input label="Last Name" value={lastName} onChange={e => setLastName(e.target.value)} />
          </div>
          <Input label="Email Address" value={profile.email} disabled icon={<Mail size={14} />} />
          <Input label="Phone Number" value={profile.phoneNumber || '—'} disabled icon={<Phone size={14} />} />
          <Input label="Job Title" value={jobTitle} onChange={e => setJobTitle(e.target.value)} icon={<Briefcase size={14} />} />
          <div>
            <label className="text-xs font-medium text-zinc-500 mb-1.5 block">Bio</label>
            <textarea value={bio} onChange={e => setBio(e.target.value)} placeholder="Tell us about yourself" rows={3} className="w-full border border-zinc-300 rounded-lg bg-white px-3 py-2 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/10 resize-none transition-colors" />
          </div>
        </div>

        <div className="flex items-center justify-end gap-2">
          {saved && <span className="text-sm text-emerald-600 font-medium">✓ Changes saved</span>}
          <Button variant="cta" size="md" type="submit" loading={saving}>
            <Save size={14} /> Save Changes
          </Button>
        </div>
      </form>
    </div>
  )
}
