-- User.java declares plan_tier, ai_credits_used, and ai_credits_reset_date, but no
-- migration ever added them to `users` — same class of drift as the missing
-- department tables (masked locally by ddl-auto: update, only surfaced by validate).
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS plan_tier VARCHAR(255),
    ADD COLUMN IF NOT EXISTS ai_credits_used INTEGER,
    ADD COLUMN IF NOT EXISTS ai_credits_reset_date DATE;
