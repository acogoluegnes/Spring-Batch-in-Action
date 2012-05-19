drop table if exists product;

create table product
(
  id character(9) not null,
  name character varying(50),
  description character varying(255),
  price float,
  constraint product_pkey primary key (id)
);
