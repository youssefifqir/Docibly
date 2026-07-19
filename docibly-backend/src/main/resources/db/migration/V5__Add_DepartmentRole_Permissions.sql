-- V5__Add_DepartmentRole_Permissions.sql
-- Add permissions column to app_departmentroledefinition for the permission matrix

ALTER TABLE app_departmentroledefinition
    ADD COLUMN IF NOT EXISTS permissions TEXT;
