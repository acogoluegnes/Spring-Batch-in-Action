create table inventory (
	product_id character varying(9),
	quantity integer
); 

create table inventory_order (
	order_id character varying(9),
	processing_date timestamp
);