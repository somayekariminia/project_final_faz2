insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (1,'2023-01-25 01:21:31.888000','morteza@gmail.com','morteza','karimi','Mok31200',null,0,'NewState',null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (2,'2023-01-25 01:21:31.888000','salman@gmail.com','salman','mohammadi','Sok90900',null,0,'NewState',null);
insert into basic_job (id, name_base)
values (1,'home');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (1,'clean of windows',2000,'windows',1);