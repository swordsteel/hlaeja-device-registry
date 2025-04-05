-- make type description for existing types
INSERT INTO public.type_descriptions (type_id)
SELECT id
FROM public.types
ON CONFLICT (type_id) DO NOTHING;
