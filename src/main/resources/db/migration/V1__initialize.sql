DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id                    bigserial,
  username              varchar(50) NOT NULL,
  password              varchar(80) NOT NULL,
  first_name            VARCHAR(50) NOT NULL,
  last_name             VARCHAR(50) NOT NULL,
  email                 VARCHAR(50) NOT NULL,
  phone                 VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id                    serial,
  name                  VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
  user_id               INT NOT NULL,
  role_id               INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id)
  REFERENCES users (id),
  FOREIGN KEY (role_id)
  REFERENCES roles (id)
);

INSERT INTO roles (name)
VALUES
('ROLE_USER'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username, password, first_name, last_name, email,phone)
VALUES
('admin','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','Admin','Admin','admin@gmail.com','+79881111111'),
('anon','$2a$10$LrQ5VVqf1M293xzqI3mH8.dtTTnHLGoZ.xgOBZtF4u7WsJ4TY3tw.','anon','anon','anon@gmail.com','+71111111111');;

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id bigserial,
    title varchar(255),
    price int,
    PRIMARY KEY (id)
);
INSERT INTO products (title, price) VALUES
('Cheese', 320),
('Milk', 90),
('Apples', 120);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id bigserial,
  id_user bigint not null,
  status boolean not null default false,
  phone bigint,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS orders_details;
CREATE TABLE orders_details (
  id bigserial,
  id_order bigint not null,
  id_product bigint not null,
  count integer not null,
  product_cost numeric(8,2) not null,
  primary key (id),
  FOREIGN KEY (id_order)
  REFERENCES orders (id),
  FOREIGN KEY (id_product)
  REFERENCES products (id)
);