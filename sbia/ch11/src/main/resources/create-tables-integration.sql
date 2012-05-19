drop table if exists product_import;

create table product_import
(
  import_id character(50) not null,
  creation_date timestamp,
  job_instance_id integer,
  constraint product_import_pkey primary key (import_id)
);