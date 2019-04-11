drop TABLE if exists borrowdetail;
drop TABLE if exists lnkcustserv;
drop TABLE if exists address;
drop TABLE if exists book;
drop TABLE if exists service;
drop TABLE if exists customer;

create table address(
    id bigint not null AUTO_INCREMENT,
    chvHomNumber varchar(50) not null,
    chvRoad varchar(50) not null,
    chvProvince varchar(40) not null,
    chvZipCode varchar(25) not null,
    adcustid bigint not null,
    PRIMARY KEY (id)
);
create table book(
    id bigint not null auto_increment,
    chvTitle varchar(25) not null,
    chvDes varchar(50) not null,
    PRIMARY KEY (id)
);
create table service(
    id bigint not null auto_increment,
    chvDes varchar(50) not null,
    PRIMARY KEY (id)
);
create table borrowdetail(
    datRat date,
    datBor date not null,
    dateDue date not null,
    adbookid bigint,
    adcustid bigint,
    PRIMARY KEY (adbookid, adcustid)
);
create table lnkcustserv(
    adcustid bigint not null,
    adservid bigint not null,
    PRIMARY KEY (adcustid, adservid)
);
ALTER TABLE address ADD FOREIGN KEY (adcustid) REFERENCES customer(ID);
ALTER TABLE borrowdetail ADD FOREIGN KEY (adcustid) REFERENCES customer(ID);

ALTER TABLE lnkcustserv ADD FOREIGN KEY (adcustid) REFERENCES customer(ID);
ALTER TABLE lnkcustserv ADD FOREIGN KEY (adservid) REFERENCES service(ID);
ALTER TABLE borrowdetail ADD FOREIGN KEY (adbookid) REFERENCES book(ID);