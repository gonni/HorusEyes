create table CRAWL_UNIT(
	CRAWL_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	URL varchar(2048),
	ANCHOR_TEXT varchar(1024),
	STATUS varchar(8),
	SEED_NO INT,
	PAGE_DATE VARCHAR(50),
	REG_DATE datetime,
	UPD_DATE datetime,
	PAGE_TEXT text(200000)
);

create table CRAWL_SEEDS(
	SEED_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	URL_PATTERN varchar(2048),
	TITLE varchar(128)
);

create table WRAPPER_RULE(
	WRAPPER_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	SEED_NO INT,
	WRAP_TYPE varchar(64),
	WRAP_VAL varchar(1024),
	WRAP_NAME varchar(128),
	REG_DT datetime
);