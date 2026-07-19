import { cn } from '@/lib/cn'
import { LucideIcon } from 'lucide-react'

interface EmptyStateProps {
  icon: LucideIcon
  title: string
  description: string
  action?: React.ReactNode
  className?: string
}

export const EmptyState = ({ icon: Icon, title, description, action, className }: EmptyStateProps) => (
  <div className={cn('flex flex-col items-center justify-center py-16 text-center', className)}>
    <div className="w-14 h-14 rounded-xl bg-zinc-100 flex items-center justify-center mb-4">
      <Icon size={24} className="text-zinc-400" />
    </div>
    <h3 className="text-base font-semibold mb-1">{title}</h3>
    <p className="text-zinc-500 text-sm max-w-sm mb-5">{description}</p>
    {action}
  </div>
)
