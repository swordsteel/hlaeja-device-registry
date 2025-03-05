-- Table: public.types
-- DROP TABLE IF EXISTS public.types;

CREATE TABLE IF NOT EXISTS public.types
(
  id         UUID                              DEFAULT gen_uuid_v7(),
  timestamp timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
  name      VARCHAR(50) UNIQUE       NOT NULL,
  CONSTRAINT pk_contact_types PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.types
  OWNER to role_administrator;


-- Revoke all permissions from existing roles
REVOKE ALL ON TABLE public.types FROM role_administrator, role_maintainer, role_support, role_service;

-- Grant appropriate permissions
GRANT ALL ON TABLE public.types TO role_administrator;
GRANT SELECT, INSERT, UPDATE ON TABLE public.types TO role_maintainer, role_service;
GRANT SELECT ON TABLE public.types TO role_support;
