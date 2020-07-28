drop table if exists files;
create table files (
    id uuid not null,
	dataset_id uuid not null,
    file_name varchar(300) not null,
	file_size int not null,
	file_type varchar(30) not null,
	spec varchar(10000) not null,
	description varchar(10000) not null,

    constraint pk_files primary key(id, file_name)
);