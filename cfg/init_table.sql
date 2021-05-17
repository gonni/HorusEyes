create table CRAWL_UNIT(
	CRAWL_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	URL varchar(2048),
	ANCHOR_TEXT varchar(1024),
	ANCHOR_IMG VARCHAR(1024),
	STATUS varchar(8),
	SEED_NO INT,
	PAGE_DATE VARCHAR(50),
	REG_DATE datetime,
	UPD_DATE datetime,
	PAGE_TEXT text(200000),
	PAGE_TITLE VARCHAR(1024)
);

set global innodb_large_prefix = ON;
set global innodb_file_format = BARRACUDA;

create table CRAWL_UNIT1(
	CRAWL_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	URL varchar(700),
	ANCHOR_TEXT varchar(1024),
	ANCHOR_IMG VARCHAR(1024),
	STATUS varchar(8),
	SEED_NO INT,
	PAGE_DATE VARCHAR(50),
	REG_DATE datetime,
	UPD_DATE datetime,
	PAGE_TEXT text(200000),
	PAGE_TITLE VARCHAR(1024),
	PARSED_PAGE_DATE datetime,
	INDEX IDX_URL (URL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

create table CRAWL_SEEDS(
	SEED_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	URL_PATTERN varchar(2048),
	TITLE varchar(128),
	STATUS varchar(4)
);

create table WRAPPER_RULE(
	WRAPPER_NO INT not null AUTO_INCREMENT PRIMARY KEY,
	SEED_NO INT,
	WRAP_TYPE varchar(64),
	WRAP_VAL varchar(1024),
	WRAP_NAME varchar(128),
	REG_DT datetime
);

create table CRAWL_KOSPI(
	TARGET_DT	VARCHAR(10) not null PRIMARY KEY,

	INDEX_VALUE	FLOAT4,
	INDEX_UP	BOOLEAN,
	DIFF_AMOUNT	FLOAT4,
	UP_DOWN_PER	FLOAT4,
	TOTAL_EA	INT,
	TOTAL_VOLUME	INT,

	ANT	INT,
	FOREIGNER INT,
	COMPANY INT,
	INVEST_BANK INT,
	INSURANCE INT,
	INVEST_TRUST INT,
	BANK INT,
	ETC_BANK INT,
	PENSION_FUND INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

insert into CRAWL_SEEDS(URL_PATTERN, TITLE)
values('https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=402',
'네이버>뉴스포커스>기업종목분석');

insert into CRAWL_SEEDS(URL_PATTERN, TITLE)
values('https://finance.naver.com/news/news_list.nhn?mode=LSS3D&section_id=101&section_id2=258&section_id3=401',
'네이버>뉴스포커스>시황전망');

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME, REG_DT)
values(2, "LIST_URL_PATTERN_FILTER", "^(https:\\/\\/finance.naver.com\\/news\\/news_read.nhn\\?article_id=).*$",
"ListFiltering", SYSDATE()) ;

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME, REG_DT)
values(19, "LIST_URL_TOP_AREA_FILTER", "ul.realtimeNewsList", "페이지내 내용추출", SYSDATE()) ;

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME, REG_DT)
values(19, "CONT_TITLE_ON_PAGE", "div.article_info > h3", "ListFiltering", SYSDATE()) ;

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME, REG_DT)
values(19, "CONT_MAIN_CONT", "div#content", "페이지내 내용추출", SYSDATE()) ;

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME, REG_DT)
values(19, "CONT_DATE_ON_PAGE", "span.article_date", "페이지내 날짜추출", SYSDATE()) ;

alter table crawl_unit1  add index idx_pagedate(PAGE_DATE);