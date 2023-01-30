insert into basic_job (id, name_base)values (2222,'cooking');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (33333,'cleanCarpet',2000,'Washing',2222);
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (333333,'garden',2000,'plant',2222);
insert into public.customer (id, registration_date, email, first_name, last_name, password, credit_id)
values (9,'2023-01-25' ,'tara@gmail.com','tara','ahmadi','Tara6789',null);
