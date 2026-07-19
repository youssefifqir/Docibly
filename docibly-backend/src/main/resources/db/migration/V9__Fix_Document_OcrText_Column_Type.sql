-- V1 created app_document.ocr_text as VARCHAR(255), but Document.java declares it
-- @Lob @Column(columnDefinition = "TEXT") — a real OCR body needs far more than 255
-- chars. Only surfaced now because ddl-auto: validate (prod/staging) was never
-- actually reached end-to-end before; dev's ddl-auto: update doesn't retroactively
-- widen an existing column's type, so it went unnoticed there too.
ALTER TABLE app_document ALTER COLUMN ocr_text TYPE TEXT;
