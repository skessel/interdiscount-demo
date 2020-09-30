create table posts (
	id uuid not null, 
	creation_user varchar(255) not null, 
	creation_date timestamp not null, 
	modification_date timestamp not null, 
	modification_user varchar(255) not null, 
	content varchar(100000) not null, 
	title varchar(255) not null, 
	primary key (id)
);