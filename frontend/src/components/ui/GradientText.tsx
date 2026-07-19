import { cn } from '@/lib/cn'
import { HTMLAttributes } from 'react'

interface GradientTextProps extends HTMLAttributes<HTMLSpanElement> {
  as?: 'span' | 'h1' | 'h2' | 'h3' | 'p'
}

export const GradientText = ({
  as: Tag = 'span', className, children, ...props
}: GradientTextProps) => (
  <Tag className={cn('text-primary', className)} {...props}>
    {children}
  </Tag>
)
