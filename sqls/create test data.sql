INSERT INTO public.companyroles (title)
VALUES('ACTIVE_PARTICIPANT');
INSERT INTO public.companyroles (title)
VALUES('APP_PROVIDER');
INSERT INTO public.companyroles (title)
VALUES('OPERATIONS_INFRASTRUCTURE_PROVIDER');
INSERT INTO public.companyroles (title)
VALUES('CONSULTING');
INSERT INTO public.companyroles (title)
VALUES('CLEARING_HOUSE');

INSERT INTO public.consens (title, link)
VALUES ('Test0', 'https://github.com/tractusx-team-portal-onboarding/tractusx/pull/7');
INSERT INTO public.consens (title, link)
VALUES ('Test1', 'https://github.com/tractusx-team-portal-onboarding/tractusx/pull/7');
INSERT INTO public.consens (title, link)
VALUES ('Test2', 'https://github.com/tractusx-team-portal-onboarding/tractusx/pull/7');
INSERT INTO public.consens (title, link)
VALUES ('Test3', 'https://github.com/tractusx-team-portal-onboarding/tractusx/pull/7');
INSERT INTO public.consens (title, link)
VALUES ('Test4', 'https://github.com/tractusx-team-portal-onboarding/tractusx/pull/7');

insert into public.companyroles_consens(role_id, consens_id)
VALUES(1,1);
insert into public.companyroles_consens(role_id, consens_id)
VALUES(1,2);
insert into public.companyroles_consens(role_id, consens_id)
VALUES(2,3);

insert into public.companyroles_signed_consens(company_id, role_id, consens_id)
VALUES('TEST Company',2,3);
insert into public.companyroles_signed_consens(company_id, role_id, consens_id)
VALUES('TEST Company 2',2,3);



select * from public.get_company_role();
select * from public.get_company_role(1);

select * from public.get_consenses(ARRAY [1,2]);

SELECT public.sign_consens('test 3',1,2);
SELECT public.sign_consens('test 3',1,1);

select * from public.get_signed_consenses_for_company_id('test 3');
