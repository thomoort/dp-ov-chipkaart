create table ov_chipkaart_product
(
    kaart_nummer   integer not null
        constraint kaart_nummer
            references ov_chipkaart,
    product_nummer integer not null
        constraint product_nummer
            references product,
    status         varchar(10),
    last_update    date,
    constraint kaart_product_nummer
        primary key (kaart_nummer, product_nummer)
);

alter table ov_chipkaart_product
    owner to postgres;

