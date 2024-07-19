--default users
INSERT INTO public.users
(id, username, "password", email, enabled, created_at, last_login)
VALUES(2, 'admin2', 'admin2', 'nhoona2@admin.com', true, '2024-07-18 23:28:05.669', NULL);
INSERT INTO public.users
(id, username, "password", email, enabled, created_at, last_login)
VALUES(1, 'admin', 'admin', 'nhoona@admin.com', true, '2024-07-18 22:31:20.800', NULL);
INSERT INTO public.users
(id, username, "password", email, enabled, created_at, last_login)
VALUES(3, 'nhoona', 'nhoona', 'nhoona3@admin.com', true, '2024-07-18 23:28:05.669', NULL);

-- default roles
INSERT INTO public.roles
(id, "name")
VALUES(1, 'ROLE_USER');
INSERT INTO public.roles
(id, "name")
VALUES(2, 'ROLE_ADMIN');

-- default user_roles
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(1, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(2, 2);
INSERT INTO public.user_roles
(user_id, role_id)
VALUES(3, 1);


