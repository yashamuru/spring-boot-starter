CREATE TABLE HOSPITAL(
    id integer PRIMARY KEY,
    name varchar(255) UNIQUE NOT NULL,
    address varchar(255),
    email varchar(255),
    phone_number varchar(20)
);
