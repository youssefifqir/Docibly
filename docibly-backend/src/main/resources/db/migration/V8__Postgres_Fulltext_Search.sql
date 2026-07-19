-- Replaces the Elasticsearch-backed document search with native Postgres full-text
-- search (no free-tier hosted Elasticsearch exists anymore — Bonsai dropped its free
-- plan, Elastic Cloud is a 14-day trial only). Neon already hosts Postgres for free,
-- so this adds zero extra infrastructure.

-- Original columns were VARCHAR(255), far too small to hold OCR/document body text.
ALTER TABLE app_searchindex ALTER COLUMN document_title TYPE VARCHAR(500);
ALTER TABLE app_searchindex ALTER COLUMN full_text TYPE TEXT;
ALTER TABLE app_searchindex ALTER COLUMN ocr_text TYPE TEXT;
ALTER TABLE app_searchindex ALTER COLUMN tags TYPE VARCHAR(1000);

-- One row per document — enforced at the app level already, but a unique index lets
-- indexDocument() upsert (ON CONFLICT) instead of select-then-insert/update.
CREATE UNIQUE INDEX IF NOT EXISTS uq_app_searchindex_document_id ON app_searchindex(document_id);

-- Every search is scoped to the caller's organization.
CREATE INDEX IF NOT EXISTS idx_app_searchindex_organization_id ON app_searchindex(organization_id);

-- Precomputed, weighted tsvector (title > tags > body text > OCR text). STORED means
-- it's computed once on write, not recomputed per search — the GIN index below is
-- what makes `search_vector @@ websearch_to_tsquery(...)` fast at read time.
ALTER TABLE app_searchindex ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (
        setweight(to_tsvector('english', coalesce(document_title, '')), 'A') ||
        setweight(to_tsvector('english', coalesce(tags, '')), 'B') ||
        setweight(to_tsvector('english', coalesce(full_text, '')), 'C') ||
        setweight(to_tsvector('english', coalesce(ocr_text, '')), 'D')
    ) STORED;

CREATE INDEX IF NOT EXISTS idx_app_searchindex_search_vector ON app_searchindex USING GIN (search_vector);
