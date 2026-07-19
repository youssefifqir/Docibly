import { cn } from '@/lib/cn'

interface AvatarProps {
  src?: string
  name: string
  size?: 'sm' | 'md' | 'lg'
  className?: string
}

const sizeClasses = {
  sm: 'w-7 h-7 text-[10px]',
  md: 'w-9 h-9 text-xs',
  lg: 'w-12 h-12 text-sm',
}

function getInitials(name: string) {
  return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
}

const bgColors = [
  'bg-orange-100 text-orange-700',
  'bg-blue-100 text-blue-700',
  'bg-emerald-100 text-emerald-700',
  'bg-violet-100 text-violet-700',
  'bg-amber-100 text-amber-700',
  'bg-rose-100 text-rose-700',
]

function getColor(name: string) {
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return bgColors[Math.abs(hash) % bgColors.length]
}

export const Avatar = ({ src, name, size = 'md', className }: AvatarProps) => (
  <div className={cn('rounded-full flex items-center justify-center font-semibold shrink-0', sizeClasses[size], !src && getColor(name), className)}>
    {src ? (
      <img src={src} alt={name} className="w-full h-full rounded-full object-cover" />
    ) : (
      getInitials(name)
    )}
  </div>
)
