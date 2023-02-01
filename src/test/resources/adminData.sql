insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (101, '2023-01-25 01:21:31.888000', 'abi@gmail.com', 'morteza', 'karimi', 'Mok31200', null, 0, 'NewState', null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (11, '2023-01-25 01:21:31.888000', 'salman@gmail.com', 'salman', 'mohammadi', 'Sok90900', null, 0, 'NewState',
        null);
insert into basic_job (id, name_base)
values (2, 'home');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (3333, 'clean of windows', 2000, 'blender', 2);
insert into public.admin (id, password, user_name)
values (200,'Admin123','admin@gmail.com');