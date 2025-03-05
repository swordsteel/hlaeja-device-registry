-- FUNCTION: public.gen_uuid_v7(timestamp with time zone)
CREATE OR REPLACE FUNCTION public.gen_uuid_v7(p_timestamp timestamp with time zone)
  RETURNS uuid
  LANGUAGE 'sql'
  COST 100
  VOLATILE PARALLEL UNSAFE
AS
$BODY$
-- Replace the first 48 bits of a uuid v4 with the provided timestamp (in milliseconds) since 1970-01-01 UTC, and set the version to 7
SELECT encode(set_bit(set_bit(overlay(uuid_send(gen_random_uuid()) PLACING substring(int8send((extract(EPOCH FROM p_timestamp) * 1000):: BIGINT) FROM 3) FROM 1 FOR 6), 52, 1), 53, 1), 'hex') ::uuid;
$BODY$;

-- FUNCTION: public.gen_uuid_v7()
CREATE OR REPLACE FUNCTION public.gen_uuid_v7()
  RETURNS uuid
  LANGUAGE 'sql'
  COST 100
  VOLATILE PARALLEL UNSAFE
AS
$BODY$
SELECT gen_uuid_v7(clock_timestamp());
$BODY$;

-- Table: public.types

CREATE TABLE IF NOT EXISTS public.types
(
  id         UUID                              DEFAULT gen_uuid_v7(),
  timestamp timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
  name      VARCHAR(50) UNIQUE       NOT NULL,
  CONSTRAINT pk_contact_types PRIMARY KEY (id)
);

-- Table: public.devices

CREATE TABLE IF NOT EXISTS public.devices
(
  id        UUID                 DEFAULT gen_uuid_v7(),
  timestamp TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  type      UUID        NOT NULL,
  CONSTRAINT pk_devices PRIMARY KEY (id),
  CONSTRAINT fk_devices_type FOREIGN KEY (type) REFERENCES public.types (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Index: public.i_devices_type

CREATE INDEX IF NOT EXISTS i_devices_type ON public.devices (type);

-- Table: public.nodes

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

-- Index: public.i_nodes_type

CREATE UNIQUE INDEX IF NOT EXISTS i_nodes_device ON public.nodes (device);
