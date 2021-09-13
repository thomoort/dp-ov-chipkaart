create table adres
(
    adres_id    serial
        constraint adres_id
            primary key,
    postcode    varchar(10)  not null,
    huisnummer  varchar(10)  not null,
    straat      varchar(255) not null,
    woonplaats  varchar(255) not null,
    reiziger_id integer      not null
        constraint reiziger_id
            references reiziger
);

alter table adres
    owner to postgres;

create unique index adres_reiziger_id_uindex
    on adres (reiziger_id);

INSERT INTO public.adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (5, '1181PD', '13', 'Oud Mijl', 'Amstelveen', 1);
INSERT INTO public.adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (6, '1234AB', '2', 'Laan van straaten', 'Utrecht', 3);
INSERT INTO public.adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (7, '0001QR', '27A', 'Paus straat', 'Vaticaan', 4);
INSERT INTO public.adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (8, '6969HA', '69', 'Humor weg', 'Oud Zeeland', 5);