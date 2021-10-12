ALTER TABLE public.part_relationship
    ADD COLUMN version bigint NOT NULL DEFAULT 0;