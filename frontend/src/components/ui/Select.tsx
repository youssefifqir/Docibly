import { type ChangeEvent, type ReactElement, type ReactNode, type SelectHTMLAttributes, Children, isValidElement, useEffect, useMemo, useRef, useState, forwardRef } from 'react'
import { ChevronDown } from 'lucide-react'
import { cn } from '@/lib/cn'

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  wrapperClassName?: string
}

type OptionLike = {
  value?: string | number
  children?: ReactNode
  disabled?: boolean
}

export const Select = forwardRef<HTMLSelectElement, SelectProps>(
  ({ className, wrapperClassName, children, value, defaultValue, onChange, disabled, name, id, ...props }, ref) => {
    const rootRef = useRef<HTMLDivElement>(null)
    const [open, setOpen] = useState(false)
    const [internalValue, setInternalValue] = useState<string>(() => {
      if (typeof defaultValue === 'string') return defaultValue
      if (typeof value === 'string') return value
      return ''
    })

    const optionItems = useMemo(() => {
      return Children.toArray(children)
        .filter(isValidElement)
        .map(child => {
          const option = child as ReactElement<OptionLike>
          return {
            value: String(option.props.value ?? ''),
            label: option.props.children,
            disabled: Boolean(option.props.disabled),
          }
        })
    }, [children])

    const currentValue = typeof value === 'string' ? value : internalValue
    const selectedItem = optionItems.find(option => option.value === currentValue) ?? optionItems[0] ?? null
    const buttonLabel = selectedItem?.label ?? 'Select an option'

    useEffect(() => {
      const handlePointerDown = (event: MouseEvent) => {
        if (!rootRef.current?.contains(event.target as Node)) setOpen(false)
      }

      const handleEscape = (event: KeyboardEvent) => {
        if (event.key === 'Escape') setOpen(false)
      }

      document.addEventListener('mousedown', handlePointerDown)
      document.addEventListener('keydown', handleEscape)
      return () => {
        document.removeEventListener('mousedown', handlePointerDown)
        document.removeEventListener('keydown', handleEscape)
      }
    }, [])

    function emitChange(nextValue: string) {
      if (typeof value !== 'string') setInternalValue(nextValue)
      onChange?.({
        target: { value: nextValue, name },
        currentTarget: { value: nextValue, name },
      } as ChangeEvent<HTMLSelectElement>)
    }

    return (
      <div ref={rootRef} className={cn('relative', wrapperClassName)}>
        <select
          ref={ref}
          className="sr-only"
          value={currentValue}
          defaultValue={defaultValue}
          onChange={onChange}
          disabled={disabled}
          name={name}
          id={id}
          tabIndex={-1}
          aria-hidden="true"
          {...props}
        >
          {children}
        </select>

        <button
          type="button"
          disabled={disabled}
          aria-haspopup="listbox"
          aria-expanded={open}
          onClick={() => setOpen(prev => !prev)}
          className={cn(
            'flex w-full items-center justify-between gap-3 rounded-lg border border-zinc-300 bg-white px-3 py-[7px] text-left text-[13px] font-medium text-zinc-800 shadow-sm transition-colors',
            'hover:border-zinc-400 focus:outline-none focus:ring-2 focus:ring-[#e8622a]/10 focus:border-[#e8622a]',
            disabled && 'cursor-not-allowed opacity-60 hover:border-zinc-300',
            className,
          )}
        >
          <span className={cn('truncate', !selectedItem && 'text-zinc-400')}>
            {buttonLabel}
          </span>
          <ChevronDown size={14} className="shrink-0 text-zinc-400" />
        </button>

        {open && !disabled && (
          <div className="absolute left-0 right-0 top-full z-50 mt-1 overflow-hidden rounded-lg border border-zinc-200 bg-white shadow-lg">
            <div className="max-h-64 overflow-auto py-1">
              {optionItems.map(option => {
                const active = option.value === currentValue
                return (
                  <button
                    key={option.value || '__empty__'}
                    type="button"
                    disabled={option.disabled}
                    onClick={() => {
                      emitChange(option.value)
                      setOpen(false)
                    }}
                    className={cn(
                      'flex w-full items-center px-3 py-2 text-left text-sm transition-colors',
                      active ? 'bg-[#fff6f4] text-[#e8622a]' : 'text-zinc-700 hover:bg-zinc-50',
                      option.disabled && 'cursor-not-allowed opacity-50 hover:bg-white',
                    )}
                  >
                    <span className="truncate">{option.label}</span>
                  </button>
                )
              })}
            </div>
          </div>
        )}
      </div>
    )
  },
)
Select.displayName = 'Select'
