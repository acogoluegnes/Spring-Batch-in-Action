drop table if exists partner_mapping;

create table partner_mapping
(
  partner_id character varying(9),
  partner_product_id character varying(9),
  store_product_id character varying(9)
);