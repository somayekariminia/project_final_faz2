insert into public.customer (id, registration_date, email, first_name, last_name, password, credit_id)
values (1,'2023-01-25 01:21:31.888000','somaye@gmail.com','somaye','karimi','Sok61058',null);

insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (1,'2023-01-25 01:21:31.888000','morteza@gmail.com','morteza','karimi','Mok31200',null,0,'Confirmed',null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (2,'2023-01-25 01:21:31.888000','ali@gmail.com','ali','karimi','Aok31200',null,0,'Confirmed',null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (3,'2023-01-25 01:21:31.888000','reza@gmail.com','reza','karimi','Rok31200',null,0,'Confirmed',null);
insert into basic_job (id, name_base)
values (1,'home');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (1,'clean of windows',2000,'windows',1);
insert into expert_services_list (expert_id, services_list_id)
values (1,1);
insert into expert_services_list (expert_id, services_list_id)
values (2,1);
insert into expert_services_list (expert_id, services_list_id)
values (3,1);
insert into address (id, city, pelak, street)
values (1,'kerman','512','bahonar');
insert into order_customer (id, about_work, code_order, end_date, offer_price, offer_start_date_customer, order_status,
                            address_id, expert_id, sub_job_id)
values (1,'cleanHomeAndCooking','order1',null,3000.00,'2023-01-25','WaitingSelectTheExpert',1,null,1);