---
name: frontend-daisyui
description: >
  Generate production-grade React + TypeScript + Vite apps with DaisyUI + Tailwind.
  Use when the user asks to build a frontend app, landing page, dashboard,
  SaaS UI, or any React interface. Delivers a modern project with the DocVault
  design system: warm orange accent on dark navy, Bricolage Grotesque + Instrument
  Serif typography, cinematic landing pages, and a clean enterprise dashboard UI.
  Triggers: "build a react app", "create a landing page", "make a dashboard",
  "react typescript vite", "daisyui app", "design a frontend".
---

# Frontend Design Skill — DocVault Design System

## Your Mandate

You generate **production-grade React + TypeScript + Vite** apps using DaisyUI + Tailwind.
Every output is distinctive, opinionated, and feels hand-crafted — not assembled from defaults.
You follow the **DocVault design DNA** as the canonical reference for colors, typography,
spacing, components, layouts, and motion.

Each new project should feel like it belongs to the same family as DocVault — sharing its
design DNA — but with its own personality applied on top (different content, slightly
different accent shade, different section ordering, unique copy).

---

## The Design DNA — Exact Values

### Color System

```css
/* ═══ APP PALETTE (light theme — dashboard/inner pages) ═══ */
--c-orange:  #e8622a;        /* THE brand accent — warm orange */
--c-black:   #1a1a2e;        /* near-black with blue undertone — body text */
--c-dark:    #2d2d3f;        /* secondary dark for subheadings */
--c-gray-1:  #71717a;        /* muted body text */
--c-gray-2:  #a1a1aa;        /* labels, placeholders */
--c-gray-3:  #e4e4e7;        /* borders, dividers */
--c-gray-4:  #f4f4f5;        /* subtle backgrounds */
--c-gray-5:  #fafafa;        /* section backgrounds */
--c-white:   #ffffff;        /* base surface */
--c-peach:   #fff6f4;        /* warm card backgrounds */
--c-peach-2: #ffefea;        /* tag pills, warm badges */
--sidebar-bg:#1a1a2e;        /* sidebar — same as --c-black */

/* ═══ LANDING PAGE PALETTE (dark theme) ═══ */
--lp-bg:       #0c0e1a;      /* deep dark navy background */
--lp-surface:  #13152a;      /* card/section surface */
--lp-surface-2:#1a1c34;      /* elevated surface */
--lp-text:     #f0ece4;      /* warm cream text */
--lp-muted:    #8a8596;      /* secondary text */
--lp-accent:   #e8622a;      /* same orange — consistency */
--lp-accent-lt:#ff8b53;      /* lighter orange for hovers */
--lp-accent-glow: rgba(232,98,42,0.14);  /* ambient glow */
--lp-line:     rgba(255,255,255,0.06);    /* subtle dividers */
```

### Shadows

```css
--shadow-xs:  0 1px 2px rgba(0,0,0,0.04);
--shadow-sm:  0 1px 3px rgba(0,0,0,0.06), 0 1px 2px rgba(0,0,0,0.04);
--shadow-md:  0 4px 6px -1px rgba(0,0,0,0.06), 0 2px 4px -2px rgba(0,0,0,0.04);
--shadow-lg:  0 10px 15px -3px rgba(0,0,0,0.06), 0 4px 6px -4px rgba(0,0,0,0.04);
```

### Typography

**Fonts:**
- **App body:** `Inter` (weights 300–800) — clean, professional for dashboard/forms
- **Landing page headlines:** `Bricolage Grotesque` (200–800) — editorial, distinctive
- **Landing page accent text:** `Instrument Serif` (italic) — elegant contrast

```css
/* Sizes */
--text-hero:    clamp(2.6rem, 6vw, 5.2rem);   /* landing hero */
--text-section: 2.75rem;                        /* section titles */
--text-body:    14px–15px;                      /* body text */
--text-small:   12px–13px;                      /* labels */

/* Tracking */
--tracking-hero:   -0.03em;    /* hero headlines */
--tracking-tight:  -0.02em;    /* section headings */
--tracking-caps:    0.15–0.3em; /* uppercase labels */

/* Line heights */
--lh-hero:  1.02;
--lh-section: 1.1;
--lh-body:  1.6;
```

**Font loading (Google Fonts imports):**
```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:opsz,wght@12..96,200..800&family=Instrument+Serif:ital@0;1&display=swap');
```

### Spacing & Radii

```
Card radius:     16px (landing), 12px (app dashboard)
Button radius:   8px (app, --rounded-btn: 0.5rem), 12px (landing)
Card padding:    28px (landing), 24px (app)
Sidebar width:   240px (expanded), 64px (collapsed)
Nav height:      64px (h-16)
Inner max-width: 1200px
Inner padding:   24px mobile, 32px desktop
Section padding: py-24 md:py-32 (landing), py-16 md:py-20 (app)
```

---

## Project Structure — Always Use This

```
my-app/
├── index.html
├── vite.config.ts
├── tailwind.config.ts
├── postcss.config.js
├── tsconfig.json
├── package.json
└── src/
    ├── main.tsx
    ├── App.tsx
    ├── styles/
    │   └── globals.css          ← all CSS variables + component classes + keyframes
    ├── lib/
    │   └── cn.ts                ← clsx + tailwind-merge helper
    ├── contexts/                ← auth context, etc.
    ├── hooks/                   ← custom hooks
    ├── types/
    │   └── index.ts
    ├── data/
    │   └── mock.ts              ← mock data for development
    ├── components/
    │   ├── ui/                  ← DaisyUI-wrapped primitive components
    │   │   ├── Button.tsx
    │   │   ├── Card.tsx
    │   │   ├── Badge.tsx
    │   │   ├── Input.tsx
    │   │   ├── Modal.tsx
    │   │   ├── Avatar.tsx
    │   │   ├── EmptyState.tsx
    │   │   ├── GradientText.tsx
    │   │   └── Section.tsx
    │   ├── layout/
    │   │   ├── AppLayout.tsx    ← sidebar + navbar + <Outlet />
    │   │   ├── Sidebar.tsx      ← dark navy, collapsible
    │   │   └── Navbar.tsx       ← top bar with search + actions
    │   ├── dashboard/           ← StatsCards, QuickActions, ActivityFeed, etc.
    │   └── [domain]/            ← domain-specific components
    └── pages/
        ├── LandingPage.tsx      ← dark theme, cinematic
        ├── LoginPage.tsx        ← split layout
        ├── DashboardPage.tsx
        └── [other pages].tsx
```

---

## DaisyUI Theme Configuration

```typescript
// tailwind.config.ts
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
```

Set the HTML `data-theme="ubersuggest"` on the root `<html>` element.

---

## UI Component Patterns (src/components/ui/)

### Button.tsx

Variants: `cta`, `primary`, `secondary`, `ghost`, `outline`
Sizes: `sm`, `md`, `lg`

```tsx
const variantClasses: Record<Variant, string> = {
  cta:       'btn btn-cta',                    // custom: solid orange + hover glow
  primary:   'btn btn-primary',
  secondary: 'btn btn-ghost bg-base-200 hover:bg-base-300',
  ghost:     'btn btn-ghost',
  outline:   'btn btn-outline border-base-300 hover:bg-base-200 hover:border-base-300',
}
```

The `btn-cta` class is defined in globals.css:
```css
.btn-cta {
  background: var(--c-orange) !important;
  border: none !important;
  color: white !important;
  font-weight: 600;
  transition: background 0.15s ease, box-shadow 0.15s ease;
}
.btn-cta:hover {
  background: #d4571f !important;
  box-shadow: 0 2px 8px rgba(232, 98, 42, 0.2);
}
```

### Card.tsx

Variants: `feature`, `stat`, `default`, `document`

```
feature-card  → --c-peach bg, 16px radius, 28px padding, orange accent border on hover
doc-card      → white bg, --c-gray-3 border, 12px radius, shadow-md on hover
stat-card     → white bg, --c-gray-3 border, 12px radius, 24px padding
```

### Input.tsx

- Label + optional error text + optional left icon
- Focus ring uses primary color (orange)
- Error state with red border + text

### Modal.tsx

- Uses native HTML `<dialog>` element
- Sizes: `sm` (max-w-md), `md` (max-w-xl), `lg` (max-w-3xl)
- Close X button, backdrop click to close
- Overlay: `rgba(0,0,0,0.5)` + `backdrop-filter: blur(2px)`

### Avatar.tsx

- Image or initials fallback
- Deterministic bg color from name hash (6 options: orange, blue, emerald, violet, amber, rose)
- Sizes: `sm` (w-7), `md` (w-9), `lg` (w-12)

### Badge.tsx

- Variants: `default` (DaisyUI badge-primary), `outline`, `tag` (uses .tag-pill class)
- Tag pill = peach background + orange text, 6px radius

### EmptyState.tsx

- Centered: icon in a box → title → description → optional action button
- Light gray icon box, muted text

---

## Layout Architecture

### AppLayout.tsx
```
┌──────────────────────────────────────────────────┐
│ Sidebar (fixed left, dark navy #1a1a2e)          │
│ ┌──────────────────────────────────────────────┐ │
│ │ Logo + brand                                 │ │
│ │ Navigation links (.sidebar-link)             │ │
│ │ Active: orange bg highlight + 3px left bar   │ │
│ │ User profile card at bottom                  │ │
│ └──────────────────────────────────────────────┘ │
│                                                  │
│            Main content area                     │
│ ┌──────────────────────────────────────────────┐ │
│ │ Navbar (sticky top, white, search + actions) │ │
│ │ <Outlet /> (page content)                    │ │
│ └──────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────┘
```

- Sidebar: 240px → 64px collapsed, dark bg, icon + label nav items
- Navbar: h-16, sticky top, white bg, border-b, search bar + CTA button
- Main: `ml-[240px]` offset, content area with padding

### Sidebar Active State Pattern
```css
.sidebar-link.active {
  background: rgba(232, 98, 42, 0.12);
  color: #fff;
}
.sidebar-link.active::before {
  content: '';
  position: absolute;
  left: 0; top: 50%;
  transform: translateY(-50%);
  width: 3px; height: 20px;
  background: var(--c-orange);
  border-radius: 0 3px 3px 0;
}
```

---

## Landing Page Architecture

The landing page uses a **dark theme** while the rest of the app uses a light/white theme.
The `.landing-page` class overrides the entire page surface to dark navy with warm cream text.

### Page Sections (in order)

1. **Nav** — fixed, frosted glass (`backdrop-filter: blur(16px)`), logo + anchor links + CTA
2. **Hero** — full-viewport, centered text, ambiently glowing radial gradients, floating geometric shapes (orbiting rings, floating diamond, pulsing dots), dot-grid background, film-grain overlay
3. **Gradient line** — 1px orange gradient divider
4. **Marquee** — infinite horizontal scroll of uppercase keywords with diamond separators
5. **Stats** — 4 key metrics with large serif numbers in orange on dark surface
6. **Features** — 3×2 card grid with icon badges (orange/10 bg), dark surface cards, hover lift
7. **How It Works** — 3 numbered steps with gradient step badges, ambient corner glow
8. **Security Strip** — 4 trust signals (icons + text) in a horizontal row
9. **Pricing** — 3-column cards (Starter/Pro/Enterprise), highlighted card scales up with orange gradient border + glow, feature checklists
10. **Testimonials** — 3 quote cards with star ratings, avatar initials with orange gradient bg
11. **FAQ** — collapsible items with chevron toggle, centered narrow layout
12. **CTA** — final call-to-action with radial orange glow background
13. **Footer** — 4-column: brand+tagline, Product links, Company links, Contact info (email/phone/location), bottom bar with legal links

### Hero Motion Pattern

Staggered reveal on page load using CSS animation-delay:
```css
.lp-reveal { animation: lp-reveal 1s cubic-bezier(0.16, 1, 0.3, 1) both; }
.lp-d1 { animation-delay: 0.12s; }
.lp-d2 { animation-delay: 0.28s; }
.lp-d3 { animation-delay: 0.44s; }
.lp-d4 { animation-delay: 0.6s; }
```

Ambient effects:
- `lp-glow-drift` — radial gradient blobs drift slowly (22s/28s cycles)
- `lp-orbit` — concentric rings rotate (70s cycle)
- `lp-float` — diamond shape floats up/down (9s cycle)
- `lp-pulse-dot` — accent-colored dots pulse (4s cycle)
- `lp-marquee` — keyword ticker scrolls left infinitely (40s cycle)

### Landing Page Card Styles

```css
/* Feature / generic card */
.lp-card {
  background: var(--lp-surface);    /* #13152a */
  border: 1px solid var(--lp-line); /* rgba(255,255,255,0.06) */
  border-radius: 16px;
  padding: 28px;
  /* Hover: orange border tint + lift + shadow */
}

/* Pricing highlighted card (inline classes): */
bg-gradient-to-b from-[#e8622a]/[0.08] to-[#13152a]
border-[#e8622a]/30
shadow-[0_0_60px_rgba(232,98,42,0.08)]
scale-[1.02] md:scale-105

/* Testimonial card: same as lp-card but flatter hover */
/* Step number badge: 56px, 16px radius, orange gradient bg */
```

### Logo Component

Reusable logo with gradient orange icon + brand name + subtitle:
```tsx
<div className="rounded-xl bg-gradient-to-br from-[#e8622a] to-[#ff8b53] flex items-center justify-center shadow-lg shadow-[#e8622a]/20">
  <FileText className="text-white" />
</div>
<span className="font-extrabold tracking-tight">AppName</span>
<span className="text-[10px] text-[#8a8596] tracking-wide uppercase">by Brand</span>
```

### Section Header Pattern

Every section follows this structure:
```tsx
<span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">
  Label
</span>
<h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight mb-4">
  Main headline
  <br />
  <span className="lp-serif italic text-[#8a8596]">serif accent line.</span>
</h2>
<p className="text-[#8a8596] max-w-lg mx-auto text-sm md:text-base">
  Supporting description.
</p>
```

---

## Login Page Pattern

Split layout:
- **Left panel** (hidden on mobile): dark navy bg (#1a1a2e), brand logo, headline with orange accent word, subtitle, trust indicators (green dots + text)
- **Right panel**: white bg, centered form card, email + password inputs with icons, "Remember me" checkbox, forgot password link, submit CTA button

---

## Dashboard Page Pattern

- Time-based greeting: "Good morning/afternoon/evening, **Name**" (name in orange)
- Stats cards row (4 cards with icon, value, label, trend)
- Quick actions row (4 clickable buttons: Upload, Search, Library, Shared)
- Two-column grid: RecentDocuments (3 cols) + ActivityFeed (2 cols)

---

## Global CSS Component Classes

Always define these in globals.css:

```css
/* Buttons */
.btn-cta          — solid orange, hover darkens + glow shadow

/* Cards */
.feature-card     — peach bg, 16px radius, orange border on hover
.doc-card         — white bg, gray border, shadow on hover
.stat-card        — white bg, gray border, 24px padding

/* Sections */
.bg-section-light — #fafafa
.bg-section-warm  — peach (#fff6f4)
.bg-section-dark  — dark navy (#1a1a2e), light text

/* Sidebar */
.sidebar-link     — flex row, icon + label, whitesmoke text
.sidebar-link.active — orange tint bg + 3px left bar

/* Search */
.search-bar       — white bg, gray border, orange focus border

/* Tags */
.tag-pill         — inline-flex, peach-2 bg, orange text, 6px radius

/* Upload */
.upload-zone      — dashed gray border, peach hover state with orange border

/* Table */
.ged-table th     — 11px uppercase, 0.05em tracking, gray-1 color
.ged-table td     — 12px/16px padding, gray-4 bottom border
.ged-table tr:hover — gray-5 bg

/* Animations */
.animate-fade-in  — 0.3s ease
.delay-1 through .delay-4 — 0.05s increments

/* Landing page specific */
.lp-card, .lp-testimonial, .lp-faq-item, .lp-step-num
.lp-btn-accent, .lp-btn-outline, .lp-btn-ghost
.lp-nav, .lp-hero, .lp-dot-grid, .lp-gradient-line, .lp-glow-text
.lp-reveal, .lp-d1–d5
.lp-orbit, .lp-float, .lp-pulse-dot, .lp-marquee-track
.lp-footer-link
```

---

## Animation & Motion

### App-level (subtle, fast)
- fade-in: 0.3s ease
- Transitions: 0.15s ease (borders, colors, backgrounds)
- No distracting animation in dashboard/workflow pages

### Landing page (cinematic, impactful)
- Page load: staggered reveals with cubic-bezier(0.16, 1, 0.3, 1)
- Ambient: slowly drifting gradient blobs (22-28s)
- Decorative: orbiting rings (70s), floating shapes (9s), pulsing dots (4s)
- Marquee: infinite horizontal scroll (40s)
- Card hover: translateY(-2px) + shadow expansion + orange border tint
- Button hover: translateY(-1px) + glow shadow

---

## Anti-Patterns — Never Do These

| ❌ Never | ✅ Instead |
|---|---|
| `font-family: Inter` on hero text | Bricolage Grotesque for display, Instrument Serif for accents |
| `color: #000` for text | `#1a1a2e` (--c-black) with blue undertone |
| `border-radius: 4px` on cards | 12px (app) or 16px (landing) |
| Generic box shadow | Use the defined shadow scale (xs/sm/md/lg) |
| Purple/blue gradient backgrounds | Orange `#e8622a` → `#ff8b53` gradients |
| White landing page | Dark navy `#0c0e1a` with cream text |
| `text-gray-500` for muted text | `#8a8596` (landing) or `#71717a` (app) |
| Flat section backgrounds | Layered radial gradients + dot grid + grain texture |
| Generic card hover | translateY(-2px) + shadow + orange border tint |
| Missing animation-delay stagger | Always stagger hero elements (d1→d4) |
| System fonts | Inter (app body) + Bricolage Grotesque + Instrument Serif |
| `gap: 16px` everywhere | 20px (cards), 28px (card padding), 32px (section padding) |
| Inline link hover | `.sidebar-link` pattern with orange bar, `.lp-footer-link` pattern |

---

## Completion Checklist

Before delivering any app, verify:

- [ ] `tailwind.config.ts` has `ubersuggest` DaisyUI theme with exact orange primary
- [ ] `globals.css` has all CSS variables + component classes + keyframes
- [ ] Google Fonts import: Inter + Bricolage Grotesque + Instrument Serif
- [ ] Landing page uses dark theme (`.landing-page` class) with orange accents
- [ ] App dashboard uses white/light theme with orange CTA buttons
- [ ] Sidebar: dark navy bg, orange active state with 3px left bar
- [ ] Navbar: white bg, sticky, search bar with orange focus, CTA upload button
- [ ] Hero: staggered reveal animation, ambient gradient blobs, serif italic accent
- [ ] Landing sections: Features (card grid) + How It Works (steps) + Pricing (3 tiers) + Testimonials + FAQ + full Footer
- [ ] Pricing: highlighted middle card with orange gradient border + scale-up + "Most popular" badge
- [ ] Section headers: uppercase label (orange) → bold headline → serif italic line → description
- [ ] Cards hover with translateY(-2px) + shadow + orange border tint
- [ ] Logo: gradient orange icon + brand name + subtitle
- [ ] Login: split layout (dark left panel + white right form)
- [ ] `src/lib/cn.ts` utility present
- [ ] `vite.config.ts` has `@` path alias
- [ ] Responsive at 768px breakpoint (mobile-first)
- [ ] All interactive elements have hover states
- [ ] Icons: Lucide React throughout