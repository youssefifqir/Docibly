import { cn } from '@/lib/cn'
import { InputHTMLAttributes, forwardRef } from 'react'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  hint?: string
  icon?: React.ReactNode
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, hint, icon, className, ...props }, ref) => (
    <div className="w-full">
      {label && (
        <label className="block text-sm font-medium text-zinc-700 mb-1.5">{label}</label>
      )}
      <div className="relative">
        {icon && (
          <div className="absolute left-3 top-1/2 -translate-y-1/2 text-zinc-400">
            {icon}
          </div>
        )}
        <input
          ref={ref}
          className={cn(
            'input w-full bg-white border-zinc-300 rounded-lg h-10',
            'focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary/20',
            'placeholder:text-zinc-400 text-sm transition-colors',
            icon && 'pl-10',
            error && 'border-error focus:border-error focus:ring-error/20',
            className
          )}
          {...props}
        />
      </div>
      {error && (
        <p className="text-error text-xs mt-1">{error}</p>
      )}
      {!error && hint && (
        <p className="text-zinc-400 text-xs mt-1">{hint}</p>
      )}
    </div>
  )
)
Input.displayName = 'Input'
