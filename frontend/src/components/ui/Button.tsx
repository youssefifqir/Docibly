import { cn } from '@/lib/cn'
import { ButtonHTMLAttributes, forwardRef } from 'react'

type Variant = 'cta' | 'primary' | 'secondary' | 'ghost' | 'outline'
type Size    = 'sm' | 'md' | 'lg'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant
  size?:    Size
  loading?: boolean
}

const variantClasses: Record<Variant, string> = {
  cta:       'btn btn-cta',
  primary:   'btn btn-primary',
  secondary: 'btn btn-ghost bg-base-200 hover:bg-base-300',
  ghost:     'btn btn-ghost',
  outline:   'btn btn-outline border-base-300 hover:bg-base-200 hover:border-base-300',
}
const sizeClasses: Record<Size, string> = {
  sm: 'btn-sm text-sm',
  md: 'text-sm px-5 h-10 min-h-[40px]',
  lg: 'btn-lg text-base px-7',
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', size = 'md', loading, className, children, ...props }, ref) => (
    <button
      ref={ref}
      className={cn(variantClasses[variant], sizeClasses[size], 'font-medium gap-2', className)}
      disabled={loading || props.disabled}
      {...props}
    >
      {loading && <span className="loading loading-spinner loading-sm" />}
      {children}
    </button>
  )
)
Button.displayName = 'Button'
