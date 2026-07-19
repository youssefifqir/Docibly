ALTER TABLE app_organizationinvitation
    ADD COLUMN department_role_id BIGINT,
    ADD CONSTRAINT fk_invitation_department_role
        FOREIGN KEY (department_role_id)
        REFERENCES app_departmentroledefinition(id);
