-- Table: public.devices
-- DROP TABLE IF EXISTS public.devices;

CREATE TABLE IF NOT EXISTS public.devices
(
  id        UUID                 DEFAULT gen_uuid_v7(),
  timestamp TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  type      UUID        NOT NULL,
  CONSTRAINT pk_devices PRIMARY KEY (id),
  CONSTRAINT fk_devices_type FOREIGN KEY (type) REFERENCES public.types (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE IF EXISTS public.devices
  OWNER to role_administrator;


-- Index: public.i_devices_type
-- DROP INDEX IF EXISTS public.i_devices_type;

CREATE INDEX IF NOT EXISTS i_devices_type ON public.devices (type);


-- Revoke all permissions from existing roles
REVOKE ALL ON TABLE public.devices FROM role_administrator, role_maintainer, role_support, role_service;

-- Grant appropriate permissions
GRANT ALL ON TABLE public.devices TO role_administrator;
GRANT SELECT, INSERT, UPDATE ON TABLE public.devices TO role_maintainer, role_service;
GRANT SELECT ON TABLE public.devices TO role_support;
