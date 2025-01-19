CREATE SCHEMA transportsschema;

CREATE TABLE transportsschema.users (
    id int PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL
);

-- table of orders to transport
CREATE TABLE transportsschema.orders (
    id int PRIMARY KEY,
    description VARCHAR(200),
    user_id int references transportsschema.users(id),
    vol int not null,
    order_date timestamp default current_timestamp
);

-- Inserting data into the users table
INSERT INTO transportsschema.users (id, name, email) VALUES
(1, 'John Doe', 'john.doe@example.com'),
(2, 'Jane Smith', 'jane.smith@example.com'),
(3, 'Alice Johnson', 'alice.johnson@example.com'),
(4, 'Bob Brown', 'bob.brown@example.com');

-- Inserting data into the orders table
INSERT INTO transportsschema.orders (id, description, user_id, vol, order_date) VALUES
(1, 'Laptop', 1, 23, NOW()),
(2, 'Smartphone', 2, 56, NOW()),
(3, 'Tablet', 1, 45, NOW()),
(4, 'Headphones', 3, 34, NOW()),
(5, 'Monitor', 1, 22, NOW());


select * from transportsschema.users where id = 1;
select u1_0.id,u1_0.email,u1_0.name from users u1_0 where u1_0.id=1;


CREATE TABLE transportsschema.roles (
  rol_id int PRIMARY KEY,
  descripcion varchar(100)
);

CREATE TABLE transportsschema.usuarios_roles (
  usuario_id int,
  rol_id int
);

ALTER TABLE transportsschema.usuarios_roles ADD FOREIGN KEY ("usuario_id") REFERENCES transportsschema.users (id);

ALTER TABLE transportsschema.usuarios_roles ADD FOREIGN KEY ("rol_id") REFERENCES transportsschema.roles (rol_id);

insert into transportsschema.roles (rol_id, descripcion) values 
(1, 'USER'), 
(2, 'ADMIN'), 
(3, 'TRANSPORTER'), 
(4, 'CLIENT');

insert into transportsschema.usuarios_roles (usuario_id, rol_id) values 
(1, 1),(1, 3),
(2, 1),(2, 4),
(3, 2),
(4, 1), (4, 3);
