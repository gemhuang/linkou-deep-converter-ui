drop table if exists apis;
create table apis
(
    id             uuid          not null,
    dataset_id     uuid          not null,
    api_name       varchar(300)  not null,
    method         varchar(10)   not null,
    url            varchar(1000) not null,
    format         varchar(300)  not null,
    query_mode     varchar(300)  not null,
    authentication varchar(300)  not null,
    frequency      varchar(300)  not null,

    constraint pk_apis primary key (id, api_name, method, url)
);