import { cn } from '@/lib/cn'
import { HTMLAttributes } from 'react'

type CardVariant = 'feature' | 'stat' | 'default' | 'document'

interface CardProps extends HTMLAttributes<HTMLDivElement> {
  variant?: CardVariant
}

const variantClasses: Record<CardVariant, string> = {
  feature:  'feature-card',
  stat:     'stat-card',
  default:  'bg-white rounded-xl border border-zinc-200',
  document: 'doc-card',
}

export const Card = ({ variant = 'default', className, children, ...props }: CardProps) => (
  <div className={cn(variantClasses[variant], className)} {...props}>
    {children}
  </div>
)
