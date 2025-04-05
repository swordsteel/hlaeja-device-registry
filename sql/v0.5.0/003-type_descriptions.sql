-- Table: public.type_descriptions
-- DROP TABLE IF EXISTS public.type_descriptions;

CREATE TABLE IF NOT EXISTS public.type_descriptions
(
  type_id     uuid                                                 NOT NULL,
  description character varying(1000) COLLATE pg_catalog."default" NOT NULL DEFAULT ''::character varying,
  CONSTRAINT pk_type_descriptions PRIMARY KEY (type_id),
  CONSTRAINT fk_type_descriptions_types FOREIGN KEY (type_id)
    REFERENCES public.types (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.type_descriptions
  OWNER to role_administrator;

-- Revoke all permissions from existing roles
REVOKE ALL ON TABLE public.type_descriptions FROM role_administrator, role_maintainer, role_support, role_service;

-- Grant appropriate permissions
GRANT ALL ON TABLE public.type_descriptions TO role_administrator;
GRANT SELECT, INSERT, UPDATE ON TABLE public.type_descriptions TO role_maintainer, role_service;
GRANT SELECT ON TABLE public.type_descriptions TO role_support;
