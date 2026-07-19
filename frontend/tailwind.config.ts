import type { Config } from 'tailwindcss'

export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['"Inter"', '-apple-system', 'BlinkMacSystemFont', '"Segoe UI"', 'sans-serif'],
      },
      letterSpacing: {
        tight2: '-0.02em',
      },
      animation: {
        'fade-in': 'fade-in 0.3s ease both',
      },
      keyframes: {
        'fade-in': {
          from: { opacity: '0' },
          to:   { opacity: '1' },
        },
      },
    },
  },
  plugins: [require('daisyui')],
  daisyui: {
    themes: [
      {
        ubersuggest: {
          "primary":          "#e8622a",
          "primary-content":  "#ffffff",
          "secondary":        "#374151",
          "secondary-content":"#ffffff",
          "accent":           "#e8622a",
          "accent-content":   "#ffffff",
          "neutral":          "#1a1a2e",
          "neutral-content":  "#f5f5f5",
          "base-100":         "#ffffff",
          "base-200":         "#f4f4f5",
          "base-300":         "#e4e4e7",
          "base-content":     "#1a1a2e",
          "info":             "#3b82f6",
          "success":          "#16a34a",
          "warning":          "#d97706",
          "error":            "#dc2626",
          "--rounded-box":   "0.75rem",
          "--rounded-btn":   "0.5rem",
          "--rounded-badge": "0.375rem",
          "--animation-btn":   "0.15s",
          "--animation-input": "0.15s",
          "--btn-focus-scale": "1",
        },
      },
    ],
    base: true,
    styled: true,
    utils: true,
    logs: false,
  },
} satisfies Config
