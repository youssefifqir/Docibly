import { Users } from 'lucide-react'

export function SharedPage() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center">
      <div className="w-16 h-16 rounded-2xl bg-zinc-100 flex items-center justify-center mb-4">
        <Users size={32} className="text-zinc-300" />
      </div>
      <h2 className="text-lg font-semibold text-zinc-700">Shared Documents</h2>
      <p className="text-sm text-zinc-500 mt-1 max-w-md">
        Documents shared with you via public links will appear here.
      </p>
    </div>
  )
}
