drop table if exists providers;
create table providers
(
    id             uuid          not null,
    code           varchar(100)  not null,
    name           varchar(300)  not null,
    official_tel   varchar(30)   not null,
    official_email varchar(300)  not null,
    address        varchar(1000) not null,
    description    varchar(5000) not null,
    website        varchar(1000) not null,
    logo           varchar(100)  not null,
    constraint pk_providers primary key (id, code)
);