-- make name index unique order by name

DROP INDEX IF EXISTS types_name_key;

CREATE UNIQUE INDEX IF NOT EXISTS  types_name_key
  ON types (name ASC);
