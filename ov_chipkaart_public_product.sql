create table product
(
    product_nummer integer        not null
        constraint product_nummer
            primary key,
    naam           varchar(30)    not null,
    beschrijving   varchar(512)   not null,
    prijs          numeric(16, 2) not null
);

alter table product
    owner to postgres;

