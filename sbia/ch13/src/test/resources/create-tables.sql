create table product (
	id varchar(255) not null,
	name varchar(255) not null,
	description varchar(255) not null,
	price float not null,
	processed boolean not null default false,
	primary key (id)
);