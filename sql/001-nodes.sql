-- make device index unique

DROP INDEX IF EXISTS public.i_nodes_type;

CREATE UNIQUE INDEX IF NOT EXISTS i_nodes_device ON public.nodes (device);
