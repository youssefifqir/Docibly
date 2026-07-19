import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import {
  ArrowRight, CheckCircle, Search, Shield, Users,
  Upload, Zap, FolderOpen, ChevronDown, ChevronUp, Globe,
  Lock, BarChart3, Mail, MapPin, Phone,
} from 'lucide-react'

/* ── Data ── */

const features = [
  { icon: Search,     title: 'Instant Search',      desc: 'Full-text search across all your files, tags, and metadata — results in under 100ms.' },
  { icon: Shield,     title: 'Enterprise Security',  desc: '256-bit encryption, role-based access control, and complete audit trails for compliance.' },
  { icon: Users,      title: 'Team Collaboration',   desc: 'Share with granular permissions. Track who viewed, edited, or downloaded every file.' },
  { icon: Upload,     title: 'Effortless Uploads',   desc: 'Drag-and-drop with automatic metadata extraction, smart tagging, and instant previews.' },
  { icon: Zap,        title: 'Lightning Fast',        desc: 'Sub-100ms search, real-time sync across devices, and a 99.9% uptime guarantee.' },
  { icon: FolderOpen, title: 'Smart Organization',    desc: 'Tags, nested folders, and custom metadata fields keep everything structured and findable.' },
]

const steps = [
  { title: 'Upload your documents', desc: 'Drag and drop files of any type. Our system automatically extracts metadata and generates previews.' },
  { title: 'Organize with tags & folders', desc: 'Classify documents with custom tags, metadata fields, and a flexible folder structure.' },
  { title: 'Search & collaborate', desc: 'Find any document instantly. Share with your team with fine-grained permissions and real-time tracking.' },
]

const stats = [
  { value: '10M+', label: 'Documents managed' },
  { value: '50K+', label: 'Active users' },
  { value: '99.9%', label: 'Uptime SLA' },
  { value: '<100ms', label: 'Avg. search speed' },
]

const testimonials = [
  { name: 'Sarah Chen', role: 'Operations Director', company: 'Meridian Group', quote: 'Docibly cut our document retrieval time by 80%. What used to take 15 minutes now takes seconds.' },
  { name: 'Marc Dubois', role: 'IT Manager', company: 'Axon Industries', quote: 'The security features and audit trails gave us the confidence to go fully digital. Best decision we made.' },
  { name: 'Amira Belkacem', role: 'Team Lead', company: 'Novalink', quote: 'The collaboration features are seamless. Our remote team finally feels connected through our documents.' },
]

const faqs = [
  { q: 'How secure is Docibly?', a: 'Docibly uses 256-bit AES encryption at rest and TLS 1.3 in transit. We offer role-based access control, full audit logs, and are SOC 2 Type II compliant.' },
  { q: 'Can I migrate from another system?', a: 'Yes — we provide bulk import tools and migration assistance. Most teams are fully migrated within a day.' },
  { q: 'What file types are supported?', a: 'Docibly supports all common document types including PDF, Word, Excel, images, and more. Full-text search works across all of them.' },
  { q: 'Is there a free trial?', a: 'Absolutely. Start with a 14-day free trial — no credit card required. Cancel anytime with no questions asked.' },
  { q: 'How does team collaboration work?', a: 'Share documents or folders with specific team members using read, edit, or admin permissions. Track all activity with detailed logs.' },
]

const plans = [
  {
    name: 'Starter',
    price: '0',
    period: 'forever',
    desc: 'For individuals and small projects getting started with document management.',
    features: ['Up to 500 documents', '1 GB storage', 'Basic search', 'Single user', 'Email support'],
    cta: 'Get started',
    highlighted: false,
  },
  {
    name: 'Professional',
    price: '19',
    period: '/user/mo',
    desc: 'For growing teams that need collaboration, security, and advanced search.',
    features: ['Unlimited documents', '50 GB storage', 'Full-text search', 'Up to 25 users', 'Role-based access', 'Audit trails', 'Priority support'],
    cta: 'Start free trial',
    highlighted: true,
  },
  {
    name: 'Enterprise',
    price: '49',
    period: '/user/mo',
    desc: 'For large organizations that need maximum control, compliance, and integrations.',
    features: ['Unlimited everything', '500 GB+ storage', 'SSO & SAML', 'Unlimited users', 'Custom metadata', 'API access', 'Dedicated account manager', 'SLA guarantee'],
    cta: 'Contact sales',
    highlighted: false,
  },
]

const ticker = ['SEARCH', 'ORGANIZE', 'COLLABORATE', 'SECURE', 'TAG', 'SHARE', 'ARCHIVE', 'DISCOVER']

const trustedBy = ['Meridian Group', 'Axon Industries', 'Novalink', 'Zenith Corp', 'Atlas Digital', 'Primewave']

/* ── Logo component ── */
function Logo({ size = 'md' }: { size?: 'sm' | 'md' | 'lg' }) {
  const dims = {
    sm: { img: 'w-7 h-7', gap: 'gap-2', text: 'text-sm' },
    md: { img: 'w-9 h-9', gap: 'gap-2.5', text: 'text-lg' },
    lg: { img: 'w-11 h-11', gap: 'gap-3', text: 'text-2xl' },
  }
  const d = dims[size]
  return (
    <div className={`flex items-center ${d.gap}`}>
      <img
        src="/ged-logo.svg"
        alt="Docibly"
        className={`${d.img} rounded-xl object-cover shrink-0 shadow-lg shadow-[#e8622a]/30`}
      />
      <span className={`${d.text} font-extrabold tracking-tight`}>Docibly</span>
    </div>
  )
}

/* ── FAQ Item ── */
function FaqItem({ q, a }: { q: string; a: string }) {
  const [open, setOpen] = useState(false)
  return (
    <div className="lp-faq-item py-5 px-1" onClick={() => setOpen(!open)}>
      <div className="flex items-center justify-between gap-4">
        <h4 className="font-semibold text-[15px]">{q}</h4>
        {open ? <ChevronUp size={18} className="text-[#8a8596] shrink-0" /> : <ChevronDown size={18} className="text-[#8a8596] shrink-0" />}
      </div>
      {open && (
        <p className="text-sm text-[#8a8596] mt-3 leading-relaxed pr-8">{a}</p>
      )}
    </div>
  )
}

/* ══════════════════════════════════
   LANDING PAGE
   ══════════════════════════════════ */

export function LandingPage() {
  const navigate = useNavigate()
  const marquee = [...ticker, ...ticker, ...ticker, ...ticker]

  return (
    <div className="landing-page min-h-screen">
      <div className="lp-grain" aria-hidden />

      {/* ═══ NAV ═══ */}
      <nav className="lp-nav fixed top-0 inset-x-0 z-50">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8 h-16 flex items-center justify-between">
          <Logo />
          <div className="hidden md:flex items-center gap-6 text-sm text-[#8a8596]">
            <a href="#features" className="hover:text-[#f0ece4] transition-colors">Features</a>
            <a href="#how-it-works" className="hover:text-[#f0ece4] transition-colors">How it works</a>
            <a href="#pricing" className="hover:text-[#f0ece4] transition-colors">Pricing</a>
            <a href="#testimonials" className="hover:text-[#f0ece4] transition-colors">Testimonials</a>
            <a href="#faq" className="hover:text-[#f0ece4] transition-colors">FAQ</a>
          </div>
          <div className="flex items-center gap-2">
            <button className="lp-btn-ghost px-4 py-2 text-sm" onClick={() => navigate('/login')}>
              Sign in
            </button>
            <button className="lp-btn-accent px-4 py-2 text-sm rounded-lg" onClick={() => navigate('/login')}>
              Get Started <ArrowRight size={13} />
            </button>
          </div>
        </div>
      </nav>

      {/* ═══ HERO ═══ */}
      <section className="lp-hero min-h-screen flex items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 lp-dot-grid" />

        {/* Ambient geometry */}
        <div className="absolute top-[18%] right-[10%] w-[300px] h-[300px] rounded-full border border-white/[0.025] lp-orbit pointer-events-none hidden md:block" />
        <div className="absolute top-[22%] right-[13%] w-[200px] h-[200px] rounded-full border border-white/[0.015] pointer-events-none hidden md:block" style={{ animation: 'lp-orbit 48s linear infinite reverse' }} />
        <div className="absolute bottom-[22%] left-[6%] w-[140px] h-[140px] border border-white/[0.025] lp-float pointer-events-none hidden md:block" />
        <div className="absolute top-[55%] right-[28%] w-3 h-3 rounded-full bg-[#e8622a] lp-pulse-dot pointer-events-none hidden md:block" />
        <div className="absolute top-[35%] left-[18%] w-2 h-2 rounded-full bg-[#ff8b53] lp-pulse-dot pointer-events-none hidden md:block" style={{ animationDelay: '1.5s' }} />

        {/* Content */}
        <div className="relative z-10 max-w-[1200px] mx-auto px-6 md:px-8 text-center pt-16">
          <div className="lp-reveal inline-flex items-center gap-2 bg-[#e8622a]/10 text-[#ff8b53] text-sm font-medium px-4 py-1.5 rounded-full mb-8 border border-[#e8622a]/15">
            <Zap size={13} /> Now with smart tagging & instant search
          </div>
          <h1 className="lp-reveal lp-d1 font-extrabold leading-[1.02] tracking-[-0.03em] mb-8" style={{ fontSize: 'clamp(2.6rem, 6vw, 5.2rem)' }}>
            Your documents,
            <br />
            <span className="lp-serif italic text-[#e8622a] lp-glow-text">
              organized
            </span>
            <span className="lp-reveal lp-d1 font-extrabold leading-[1.02] tracking-[-0.03em] mb-8"> and </span>
            <span className="lp-serif italic text-[#e8622a] lp-glow-text">
              accessible.
            </span>
          </h1>
          <p className="lp-reveal lp-d2 text-lg md:text-xl text-[#8a8596] max-w-xl mx-auto mb-12 leading-relaxed">
            A centralized platform to store, search, and share documents across your entire organization. No more lost files.
          </p>
          <div className="lp-reveal lp-d3 flex flex-col sm:flex-row items-center justify-center gap-4">
            <button className="lp-btn-accent px-8 py-3.5 text-[15px] rounded-xl" onClick={() => navigate('/login')}>
              Start for free <ArrowRight size={16} />
            </button>
            <button className="lp-btn-outline px-8 py-3.5 text-[15px] rounded-xl" onClick={() => navigate('/login')}>
              View demo
            </button>
          </div>

          {/* Trust strip */}
          <div className="lp-reveal lp-d4 mt-16 flex flex-wrap items-center justify-center gap-8 text-[13px] text-[#8a8596] opacity-40">
            <span className="uppercase tracking-wider text-[11px]">Trusted by</span>
            {trustedBy.map(name => (
              <span key={name} className="font-medium tracking-wide">{name}</span>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ GRADIENT LINE ═══ */}
      <div className="lp-gradient-line" />

      {/* ═══ MARQUEE ═══ */}
      <div className="py-4 overflow-hidden border-b border-white/[0.06]">
        <div className="lp-marquee-track">
          {marquee.map((word, i) => (
            <span key={i} className="text-[12px] tracking-[0.25em] uppercase text-[#8a8596] opacity-30 flex items-center">
              <span className="mx-6">{word}</span>
              <span className="text-[#e8622a] opacity-50 text-[8px]">◆</span>
            </span>
          ))}
        </div>
      </div>

      {/* ═══ STATS ═══ */}
      <section className="py-16 md:py-20 bg-[#13152a] border-b border-white/[0.06]">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 md:gap-12">
            {stats.map(s => (
              <div key={s.label} className="text-center">
                <div className="lp-serif text-[2.5rem] md:text-[3.2rem] tracking-tight leading-none text-[#e8622a]">
                  {s.value}
                </div>
                <div className="text-xs tracking-[0.15em] uppercase text-[#8a8596] mt-2.5">{s.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ FEATURES ═══ */}
      <section id="features" className="py-24 md:py-32">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8">
          <div className="text-center mb-16">
            <span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">Features</span>
            <h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight mb-4">
              Everything you need to manage
              <br />
              <span className="lp-serif italic text-[#8a8596]">your documents.</span>
            </h2>
            <p className="text-[#8a8596] max-w-lg mx-auto text-sm md:text-base">
              Built for teams that need reliable, secure, and fast document management.
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            {features.map(f => (
              <div key={f.title} className="lp-card">
                <div className="w-11 h-11 rounded-xl bg-[#e8622a]/10 flex items-center justify-center mb-4">
                  <f.icon size={20} className="text-[#e8622a]" />
                </div>
                <h3 className="font-semibold text-base mb-2">{f.title}</h3>
                <p className="text-sm text-[#8a8596] leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ HOW IT WORKS ═══ */}
      <section id="how-it-works" className="py-24 md:py-32 bg-[#13152a] relative overflow-hidden">
        <div className="absolute top-0 right-0 w-[500px] h-[500px] bg-gradient-to-bl from-[#e8622a]/[0.04] to-transparent rounded-full pointer-events-none" />
        <div className="max-w-[1200px] mx-auto px-6 md:px-8 relative z-10">
          <div className="mb-16">
            <span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">How it works</span>
            <h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight">
              Three simple steps to
              <br />
              <span className="lp-serif italic text-[#8a8596]">digital clarity.</span>
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {steps.map((step, i) => (
              <div key={i} className="flex flex-col gap-5">
                <div className="lp-step-num">{i + 1}</div>
                <div>
                  <h3 className="font-semibold text-lg mb-2">{step.title}</h3>
                  <p className="text-sm text-[#8a8596] leading-relaxed">{step.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ SECURITY STRIP ═══ */}
      <section className="py-16 border-y border-white/[0.06]">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {[
              { icon: Lock, text: '256-bit encryption' },
              { icon: Shield, text: 'SOC 2 compliant' },
              { icon: Globe, text: 'GDPR ready' },
              { icon: BarChart3, text: 'Full audit trails' },
            ].map(item => (
              <div key={item.text} className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-[#e8622a]/10 flex items-center justify-center shrink-0">
                  <item.icon size={18} className="text-[#e8622a]" />
                </div>
                <span className="text-sm font-medium text-[#8a8596]">{item.text}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ PRICING ═══ */}
      <section id="pricing" className="py-24 md:py-32">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8">
          <div className="text-center mb-16">
            <span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">Pricing</span>
            <h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight mb-4">
              Simple, transparent
              <br />
              <span className="lp-serif italic text-[#8a8596]">pricing.</span>
            </h2>
            <p className="text-[#8a8596] max-w-lg mx-auto text-sm md:text-base">
              No hidden fees. No surprises. Start free, scale when you're ready.
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-5 items-start">
            {plans.map(plan => (
              <div
                key={plan.name}
                className={`rounded-2xl border p-7 md:p-8 flex flex-col transition-all duration-200 ${
                  plan.highlighted
                    ? 'bg-gradient-to-b from-[#e8622a]/[0.08] to-[#13152a] border-[#e8622a]/30 shadow-[0_0_60px_rgba(232,98,42,0.08)] scale-[1.02] md:scale-105'
                    : 'bg-[#13152a] border-white/[0.06] hover:border-white/[0.12]'
                }`}
              >
                {plan.highlighted && (
                  <span className="inline-flex self-start items-center gap-1.5 text-[11px] tracking-[0.2em] uppercase font-bold bg-[#e8622a] text-white px-3 py-1 rounded-full mb-5">
                    <Zap size={11} /> Most popular
                  </span>
                )}
                <h3 className="text-lg font-bold mb-1">{plan.name}</h3>
                <p className="text-sm text-[#8a8596] mb-5">{plan.desc}</p>
                <div className="flex items-baseline gap-1 mb-6">
                  <span className="text-[2.5rem] md:text-[3rem] font-extrabold tracking-tight leading-none">
                    {plan.price === '0' ? 'Free' : `$${plan.price}`}
                  </span>
                  {plan.price !== '0' && (
                    <span className="text-sm text-[#8a8596]">{plan.period}</span>
                  )}
                </div>
                <button
                  className={`w-full py-3 rounded-xl text-sm font-semibold transition-all duration-200 mb-7 ${
                    plan.highlighted
                      ? 'lp-btn-accent justify-center'
                      : 'lp-btn-outline justify-center'
                  }`}
                  onClick={() => navigate('/login')}
                >
                  {plan.cta} <ArrowRight size={14} />
                </button>
                <ul className="space-y-3 flex-1">
                  {plan.features.map(feat => (
                    <li key={feat} className="flex items-start gap-2.5 text-sm text-[#8a8596]">
                      <CheckCircle size={15} className="text-emerald-400 shrink-0 mt-0.5" />
                      {feat}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ TESTIMONIALS ═══ */}
      <section id="testimonials" className="py-24 md:py-32 bg-[#13152a]">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8">
          <div className="text-center mb-16">
            <span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">Testimonials</span>
            <h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight">
              Loved by teams
              <br />
              <span className="lp-serif italic text-[#8a8596]">everywhere.</span>
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
            {testimonials.map(t => (
              <div key={t.name} className="lp-testimonial flex flex-col">
                {/* Stars */}
                <div className="flex gap-1 mb-4">
                  {[...Array(5)].map((_, i) => (
                    <svg key={i} width="16" height="16" viewBox="0 0 16 16" fill="#e8622a"><path d="M8 0l2.47 4.94L16 5.76l-4 3.88.94 5.48L8 12.68l-4.94 2.44.94-5.48-4-3.88 5.53-.82z"/></svg>
                  ))}
                </div>
                <p className="text-sm text-[#8a8596] leading-relaxed flex-1 mb-5">"{t.quote}"</p>
                <div className="flex items-center gap-3 pt-4 border-t border-white/[0.06]">
                  <div className="w-9 h-9 rounded-full bg-gradient-to-br from-[#e8622a] to-[#ff8b53] flex items-center justify-center text-white text-xs font-bold">
                    {t.name.split(' ').map(n => n[0]).join('')}
                  </div>
                  <div>
                    <div className="text-sm font-medium">{t.name}</div>
                    <div className="text-xs text-[#8a8596]">{t.role}, {t.company}</div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ═══ FAQ ═══ */}
      <section id="faq" className="py-24 md:py-32 bg-[#13152a]">
        <div className="max-w-[700px] mx-auto px-6 md:px-8">
          <div className="text-center mb-14">
            <span className="text-xs tracking-[0.3em] uppercase text-[#e8622a] font-semibold block mb-4">FAQ</span>
            <h2 className="text-3xl md:text-[2.75rem] font-bold leading-[1.1] tracking-tight">
              Common questions
            </h2>
          </div>
          <div>
            {faqs.map(faq => (
              <FaqItem key={faq.q} q={faq.q} a={faq.a} />
            ))}
          </div>
        </div>
      </section>

      {/* ═══ CTA ═══ */}
      <section className="py-24 md:py-32 relative overflow-hidden">
        <div className="absolute inset-0 pointer-events-none" style={{ background: 'radial-gradient(ellipse at center, rgba(232,98,42,0.06) 0%, transparent 60%)' }} />
        <div className="relative z-10 max-w-[1200px] mx-auto px-6 md:px-8 text-center">
          <h2 className="text-3xl md:text-5xl font-bold tracking-tight mb-4">
            Ready to get
            <br />
            <span className="lp-serif italic text-[#e8622a] lp-glow-text">organized?</span>
          </h2>
          <p className="text-[#8a8596] max-w-md mx-auto mb-10">
            Join thousands of teams who trust Docibly for their document management.
          </p>
          <button className="lp-btn-accent px-8 py-4 text-base rounded-xl mb-10" onClick={() => navigate('/login')}>
            Get started free <ArrowRight size={16} />
          </button>
          <div className="flex flex-wrap items-center justify-center gap-6 text-sm text-[#8a8596] opacity-70">
            <span className="flex items-center gap-1.5"><CheckCircle size={14} className="text-emerald-400" /> Free 14-day trial</span>
            <span className="flex items-center gap-1.5"><CheckCircle size={14} className="text-emerald-400" /> No credit card</span>
            <span className="flex items-center gap-1.5"><CheckCircle size={14} className="text-emerald-400" /> Cancel anytime</span>
          </div>
        </div>
      </section>

      {/* ═══ FOOTER ═══ */}
      <footer className="border-t border-white/[0.06] bg-[#0a0c18]">
        <div className="max-w-[1200px] mx-auto px-6 md:px-8 py-16">
          {/* Top row */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-10 mb-14">
            {/* Brand */}
            <div className="md:col-span-1">
              <Logo size="sm" />
              <p className="text-sm text-[#8a8596] mt-4 leading-relaxed">
                Centralized document management for modern teams. Store, search, and share — effortlessly.
              </p>
            </div>

            {/* Product */}
            <div>
              <h4 className="text-xs tracking-[0.2em] uppercase text-[#8a8596] font-semibold mb-4">Product</h4>
              <ul className="space-y-2.5">
                {['Features', 'Security', 'Integrations', 'Pricing', 'Changelog'].map(link => (
                  <li key={link}><span className="lp-footer-link text-sm">{link}</span></li>
                ))}
              </ul>
            </div>

            {/* Company */}
            <div>
              <h4 className="text-xs tracking-[0.2em] uppercase text-[#8a8596] font-semibold mb-4">Company</h4>
              <ul className="space-y-2.5">
                {['About', 'Blog', 'Careers', 'Press', 'Partners'].map(link => (
                  <li key={link}><span className="lp-footer-link text-sm">{link}</span></li>
                ))}
              </ul>
            </div>

            {/* Contact */}
            <div>
              <h4 className="text-xs tracking-[0.2em] uppercase text-[#8a8596] font-semibold mb-4">Contact</h4>
              <ul className="space-y-3">
                <li className="flex items-center gap-2.5 text-sm text-[#8a8596]">
                  <Mail size={14} className="text-[#e8622a] shrink-0" /> hello@docibly.io
                </li>
                <li className="flex items-center gap-2.5 text-sm text-[#8a8596]">
                  <Phone size={14} className="text-[#e8622a] shrink-0" /> +1 (555) 234-5678
                </li>
                <li className="flex items-start gap-2.5 text-sm text-[#8a8596]">
                  <MapPin size={14} className="text-[#e8622a] shrink-0 mt-0.5" /> Casablanca, Morocco
                </li>
              </ul>
            </div>
          </div>

          {/* Divider */}
          <div className="border-t border-white/[0.06] pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">
            <div className="text-xs text-[#8a8596] opacity-60">
              &copy; {new Date().getFullYear()} Docibly. All rights reserved.
            </div>
            <div className="flex items-center gap-5 text-xs text-[#8a8596]">
              <span className="lp-footer-link">Privacy Policy</span>
              <span className="lp-footer-link">Terms of Service</span>
              <span className="lp-footer-link">Cookie Policy</span>
            </div>
          </div>
        </div>
      </footer>
    </div>
  )
}
