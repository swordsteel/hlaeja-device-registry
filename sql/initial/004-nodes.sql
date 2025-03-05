-- Table: public.nodes
-- DROP TABLE IF EXISTS public.nodes;

CREATE TABLE IF NOT EXISTS public.nodes
(
  id        UUID                 DEFAULT gen_uuid_v7(),
  timestamp TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  client    UUID        NOT NULL,
  device    UUID        NOT NULL,
  name      VARCHAR(50) NOT NULL,
  CONSTRAINT pk_nodes PRIMARY KEY (id),
  CONSTRAINT fk_nodes_type FOREIGN KEY (device) REFERENCES public.devices (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE IF EXISTS public.nodes
  OWNER to role_administrator;


-- Index: public.i_nodes_type
-- DROP INDEX IF EXISTS public.i_nodes_type;

CREATE UNIQUE INDEX IF NOT EXISTS i_nodes_device ON public.nodes (device);


-- Revoke all permissions from existing roles
REVOKE ALL ON TABLE public.nodes FROM role_administrator, role_maintainer, role_support, role_service;

-- Grant appropriate permissions
GRANT ALL ON TABLE public.nodes TO role_administrator;
GRANT SELECT, INSERT, UPDATE ON TABLE public.nodes TO role_maintainer, role_service;
GRANT SELECT ON TABLE public.nodes TO role_support;
