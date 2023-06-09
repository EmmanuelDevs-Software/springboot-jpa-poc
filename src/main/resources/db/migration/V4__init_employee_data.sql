insert into employee (id, birthdate, email, firstname, identifier, lastname, role)
values (
           nextVal('hibernate_sequence'),
           current_date,
           'user@gmail.com',
           'chad',
           '12134',
           'chadwick',
           'ROLE_ADMIN'
       );