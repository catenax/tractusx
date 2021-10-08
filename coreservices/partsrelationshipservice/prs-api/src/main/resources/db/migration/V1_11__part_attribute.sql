ALTER TABLE public.part_attribute
    RENAME COLUMN name TO attribute;

ALTER TABLE public.part_attribute DROP CONSTRAINT part_attribute_pkey;

ALTER TABLE ONLY public.part_attribute
    ADD CONSTRAINT part_attribute_pkey PRIMARY KEY (oneidmanufacturer, objectidmanufacturer, attribute);