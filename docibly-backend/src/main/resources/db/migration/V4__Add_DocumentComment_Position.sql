ALTER TABLE app_documentcomment ADD COLUMN IF NOT EXISTS positionx numeric(21,2) DEFAULT 0;
ALTER TABLE app_documentcomment ADD COLUMN IF NOT EXISTS positiony numeric(21,2) DEFAULT 0;
