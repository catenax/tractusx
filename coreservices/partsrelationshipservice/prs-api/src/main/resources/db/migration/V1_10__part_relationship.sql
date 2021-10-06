ALTER TABLE public.part_relationship
    ADD COLUMN effect_time timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    ADD COLUMN removed boolean NOT NULL DEFAULT false,
    ADD COLUMN life_cycle_stage character varying(255) NOT NULL DEFAULT 'BUILD';

ALTER TABLE public.part_relationship DROP CONSTRAINT part_relationship_pkey;

ALTER TABLE ONLY public.part_relationship
    ADD CONSTRAINT part_relationship_pkey PRIMARY KEY (oneidmanufacturer, objectidmanufacturer, parent_oneidmanufacturer, parent_objectidmanufacturer, part_relationship_list_id, effect_time, removed, life_cycle_stage);
