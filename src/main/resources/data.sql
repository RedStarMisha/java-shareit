insert into booking_status as bs (id, name) values (1, 'WAITING'), (2, 'APPROVED'), (3, 'REJECTED'), (4, 'CANCELED')
                           on conflict (id) do nothing;
