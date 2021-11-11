DROP TABLE  IF EXISTS public.companyroles_consens;
DROP TABLE IF EXISTS public.companyroles_signed_consens;
DROP TABLE IF EXISTS public.company_selected_roles;

DROP TABLE IF EXISTS public.companyroles;
CREATE TABLE public.companyroles (
    id  SERIAL  PRIMARY KEY,
    title varchar(40) NOT NULL
);

CREATE TABLE public.company_selected_roles (
    id  SERIAL  PRIMARY KEY,
    company_id varchar(100) NOT NULL,
    role_id int references public.companyroles(id)
);

CREATE TABLE public.company_selected_idp (
    id  SERIAL  PRIMARY KEY,
    company_id varchar(100) NOT NULL,
    idp VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS public.consens;
CREATE TABLE public.consens (
    id  SERIAL  PRIMARY KEY,
    title varchar(40) NOT NULL,
    link varchar(250) NOT NULL
);

CREATE TABLE public.companyroles_consens (
  role_id int references public.companyroles(id),
  consens_id  int references public.consens(id),
  primary key (role_id, consens_id)
);

CREATE TABLE public.companyroles_signed_consens(
company_id varchar(100),
    role_id int references public.companyroles(id),
  consens_id  int references public.consens(id),
  signature_date TIMESTAMP,
  signatory varchar(40),
  primary key (company_id, role_id, consens_id)
);

DROP FUNCTION IF EXISTS public.get_company_role();
CREATE FUNCTION public.get_company_role() RETURNS TABLE(role_id int,role_title varchar(40), consens_id int, consens_title varchar(40), link varchar(100))
AS $$
SELECT cr.id as role_id, cr.title as role_title, c.id as consens_id, c.title as consens_title, c.link
FROM   companyroles_consens cc
JOIN  companyroles cr ON cc.role_id = cr.id
JOIN consens c on c.id = cc.consens_id
$$
LANGUAGE SQL;

DROP FUNCTION IF EXISTS public.get_company_role(int);
CREATE FUNCTION public.get_company_role(int) RETURNS TABLE(role_id int,role_title varchar(40), consens_id int, consens_title varchar(40), link varchar(100))
AS $$
SELECT cr.id as role_id, cr.title as role_title, c.id as consens_id, c.title as consens_title, c.link
FROM   companyroles_consens cc
JOIN  companyroles cr ON cc.role_id = cr.id
JOIN consens c on c.id = cc.consens_id
   where cr.id = $1
$$
LANGUAGE SQL;

DROP FUNCTION IF EXISTS public.get_consenss(int[]);
CREATE FUNCTION public.get_consenss(int[]) RETURNS TABLE(id int, title varchar(40), link varchar(250) )
AS $$
SELECT * from public.consens WHERE id = ANY($1)
$$
LANGUAGE SQL;

DROP FUNCTION IF EXISTS public.sign_consens(varchar(100), int, int, varchar(40));
CREATE FUNCTION public.sign_consens(varchar(100), int, int, varchar(40) ) RETURNS void
AS $$
insert into public.companyroles_signed_consens(company_id, role_id, consens_id, signature_date, signatory )
VALUES($1,$2,$3,(now() at time zone 'utc'), $4);
$$
LANGUAGE SQL;

DROP FUNCTION IF EXISTS public.get_signed_consenss_for_company_id(varchar(100));
CREATE FUNCTION public.get_signed_consenss_for_company_id(varchar(100)) RETURNS TABLE(company_id varchar(100), role_id int , consens_id int, signature_date TIMESTAMP,
  signatory varchar(40) )
AS $$
SELECT * from public.companyroles_signed_consens WHERE company_id = $1
$$
LANGUAGE SQL;
