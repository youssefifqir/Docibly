import { cn } from '@/lib/cn'
import { useEffect, useRef } from 'react'
import { X } from 'lucide-react'

interface ModalProps {
  open: boolean
  onClose: () => void
  title?: string
  children: React.ReactNode
  className?: string
  size?: 'sm' | 'md' | 'lg'
}

const sizeClasses = {
  sm: 'max-w-md',
  md: 'max-w-xl',
  lg: 'max-w-3xl',
}

export const Modal = ({ open, onClose, title, children, className, size = 'md' }: ModalProps) => {
  const dialogRef = useRef<HTMLDialogElement>(null)

  useEffect(() => {
    const dialog = dialogRef.current
    if (!dialog) return
    if (open) {
      dialog.showModal()
    } else {
      dialog.close()
    }
  }, [open])

  return (
    <dialog
      ref={dialogRef}
      className="modal modal-bottom sm:modal-middle"
      onClose={onClose}
    >
      <div className={cn(
        'modal-box rounded-xl p-0 overflow-hidden',
        sizeClasses[size],
        className
      )}>
        {title && (
          <div className="flex items-center justify-between px-6 pt-5 pb-0">
            <h3 className="font-semibold text-lg">{title}</h3>
            <button onClick={onClose} className="btn btn-ghost btn-sm btn-circle">
              <X size={16} />
            </button>
          </div>
        )}
        <div className="p-6 pt-4">{children}</div>
      </div>
      <form method="dialog" className="modal-backdrop">
        <button onClick={onClose}>close</button>
      </form>
    </dialog>
  )
}
