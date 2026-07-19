-- V3__Add_Digitize_Format.sql

ALTER TABLE app_document ADD COLUMN IF NOT EXISTS digitize_format VARCHAR(255) DEFAULT 'NONE';
