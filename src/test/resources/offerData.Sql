insert into public.customer (id, registration_date, email, first_name, last_name, password, credit_id)
values (5, '2023-01-25 01:21:31.888000', 'somaye@gmail.com', 'somaye', 'karimi', 'Sok61058', null);

insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (1111, '2023-01-25 01:21:31.888000', 'mohammad@gmail.com', 'morteza', 'karimi', 'Mok31200', null, 0, 'Confirmed',
        null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (11111, '2023-01-25 01:21:31.888000', 'ali@gmail.com', 'ali', 'karimi', 'Aok31200', null, 0, 'Confirmed', null);
insert into public.expert (id, registration_date, email, first_name, last_name, password, expert_image, performance,
                           specialty_status, credit_id)
values (111111, '2023-01-25 01:21:31.888000', 'reza@gmail.com', 'reza', 'karimi', 'Rok31200', null, 0, 'Confirmed',
        null);
insert into basic_job (id, name_base)
values (22, 'home');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (33, 'clean of windows', 2000, 'windows', 22);
insert into basic_job (id, name_base)
values (222, 'vehicle');
insert into sub_job (id, description, price, sub_job_name, basic_job_id)
values (333, 'driver ', 2000, 'car', 222);
insert into expert_services_list (expert_id, services_list_id)
values (1111, 33);
insert into expert_services_list (expert_id, services_list_id)
values (1111, 333);
insert into expert_services_list (expert_id, services_list_id)
values (11111, 33);
insert into expert_services_list (expert_id, services_list_id)
values (111111, 33);
insert into address (id, city, pelak, street)
values (10, 'kerman', '512', 'bahonar');
insert into order_customer (id, start_date_do_work, about_work, code_order, end_date_do_work, offer_price, order_status,
                            address_id, expert_id, sub_job_id)
values (7, '2023-01-26', 'homeClean', 'order16', null, 3000.00, 'WaitingSelectTheExpert', 10, null, 33);
insert into order_customer (id, start_date_do_work, about_work, code_order, end_date_do_work, offer_price, order_status,
                            address_id, expert_id, sub_job_id)
values (77, '2023-01-26', 'driversWithCar', 'order27', null, 3500.00, 'WaitingForOfferTheExperts', 10, null, 333);
