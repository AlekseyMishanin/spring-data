DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id                    bigserial,
  password              varchar(80) NOT NULL,
  first_name            VARCHAR(50) NOT NULL,
  last_name             VARCHAR(50) NOT NULL,
  email                 VARCHAR(50) NOT NULL,
  phone                 VARCHAR(15) NOT NULL unique,
  enabled               boolean not null,
  type_reg              VARCHAR(50) NOT NULL,
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
('ROLE_USER'), ('ROLE_MANAGER'), ('ROLE_ADMIN'), ('ROLE_CUSTOMER');

INSERT INTO users (password, first_name, last_name, email,phone, enabled, type_reg)
VALUES
('$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','Admin','Admin','mishanin_a_a@mail.ru','+79263545028', 'true', 'FULL'),
('$2a$10$LrQ5VVqf1M293xzqI3mH8.dtTTnHLGoZ.xgOBZtF4u7WsJ4TY3tw.','anon','anon','mishanin_a_a@mail.ru','+71111111111', 'true', 'FULL');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4);

DROP TABLE IF EXISTS products_group;
CREATE TABLE products_group (
    id bigserial,
    title varchar(255),
    PRIMARY KEY (id)
);

INSERT INTO products_group (title) VALUES
('Food'),
('Instruments');

DROP TABLE IF EXISTS products;
CREATE TABLE products (
    id bigserial,
    title varchar(255),
    price int,
    product_group_id bigint,
    foreign key (product_group_id)
    references products_group(id),
    PRIMARY KEY (id)
);
INSERT INTO products (title, price, product_group_id) VALUES
('Cheese', 320, 1),
('Milk', 90, 1),
('Apples', 120, 1),
('Hammer', 200, 2);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  id bigserial,
  id_user bigint not null,
  status varchar(255) not null,
  created_at timestamp,
  updated_at timestamp,
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

DROP TABLE IF EXISTS verification_tokens;
CREATE TABLE verification_tokens (
  id bigserial,
  token varchar(255) not null,
  user_id bigint not null,
  date_create timestamp,
  primary key (id),
  FOREIGN KEY (user_id)
  REFERENCES users (id)
);

DROP TABLE IF EXISTS products_images;
CREATE TABLE products_images (
    id bigserial PRIMARY KEY,
    product_id bigint not null,
    path varchar(255),
    foreign key (product_id)
    references products(id)
);
INSERT INTO products_images (product_id, path) VALUES
(1, 'img_1.jpg'),
(2, 'img_1.jpg'),
(3, 'img_1.jpg'),
(4, 'img_1.jpg');
