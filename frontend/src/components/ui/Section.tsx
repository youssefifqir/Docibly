import { cn } from '@/lib/cn'
import { HTMLAttributes } from 'react'

type SectionVariant = 'default' | 'light' | 'warm' | 'dark'

interface SectionProps extends HTMLAttributes<HTMLElement> {
  variant?: SectionVariant
  inner?:   boolean
}

const variantClasses: Record<SectionVariant, string> = {
  default: 'bg-white',
  light:   'bg-zinc-50',
  warm:    'bg-[#fff6f4]',
  dark:    'bg-[#1a1a2e] text-white',
}

export const Section = ({ variant = 'default', inner = true, className, children, ...props }: SectionProps) => (
  <section className={cn('py-16 md:py-20', variantClasses[variant], className)} {...props}>
    {inner ? <div className="inner">{children}</div> : children}
  </section>
)
