create table ov_chipkaart
(
    kaart_nummer integer        not null
        constraint kaart_nummer
            primary key,
    geldig_tot   date           not null,
    klasse       integer        not null,
    saldo        numeric(16, 2) not null,
    reiziger_id  integer        not null
        constraint reiziger_id
            references reiziger
);

alter table ov_chipkaart
    owner to postgres;

