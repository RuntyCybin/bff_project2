CREATE SCHEMA transportsschema;


-- USERS / ROLES / USER_ROLES
CREATE TABLE transportsschema.users (
    id int PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE not null,
    password VARCHAR(100) not null
);

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

-- Inserting sample roles
insert into transportsschema.roles (rol_id, descripcion) values 
(1, 'USER'), 
(2, 'ADMIN'), 
(3, 'TRANSPORTER'), 
(4, 'CLIENT');


-- ORDERS
-- table of orders to transport
CREATE TABLE transportsschema.orders (
    id int PRIMARY KEY,
    description VARCHAR(200),
    user_id int references transportsschema.users(id),
    vol int not null,
    order_date timestamp default current_timestamp
);



-- QUERIES
select * from transportsschema.users where id = 1;
select u1_0.id,u1_0.email,u1_0.name from users u1_0 where u1_0.id=1;
