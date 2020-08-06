drop table if exists datasets;
create table datasets
(
    id           uuid          not null,
    title        varchar(300)  NOT NULL,
    sub_title    varchar(5000) NOT NULL,
    description  varchar(5000) NOT NULL,
    image_src    varchar(1000) NOT NULL,
    file_license varchar(300)  NOT NULL,
    api_license  varchar(300)  NOT NULL,
    category     varchar(100)  NOT NULL,

    constraint pk_datasets primary key (id, title)
);