import { cn } from '@/lib/cn'
import { HTMLAttributes } from 'react'

type BadgeVariant = 'default' | 'outline' | 'tag'

interface BadgeProps extends HTMLAttributes<HTMLSpanElement> {
  variant?: BadgeVariant
}

export const Badge = ({ variant = 'default', className, children, ...props }: BadgeProps) => {
  if (variant === 'tag') return (
    <span className={cn('tag-pill', className)} {...props}>
      {children}
    </span>
  )
  return (
    <span className={cn(
      'inline-flex items-center px-2 py-0.5 rounded text-xs font-medium',
      variant === 'outline'
        ? 'border border-zinc-300 text-zinc-600'
        : 'bg-zinc-100 text-zinc-600',
      className
    )} {...props}>
      {children}
    </span>
  )
}
