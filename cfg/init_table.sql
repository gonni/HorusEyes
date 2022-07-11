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

alter table crawl_unit1  add index idx_pagedate(PAGE_DATE);

--
insert into CRAWL_SEEDS(URL_PATTERN, TITLE, STATUS)
values ('https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=001&listType=title', '네이버뉴>속보>전체', 'AVTV');

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME)
values(3, 'LIST_URL_PATTERN_FILTER', '^(https://n.news.naver.com/mnews/).*$', '뉴스 리스트 추출');

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME)
values(3, 'LIST_URL_TOP_AREA_FILTER', 'ul.type02', '뉴스 리스트 영역');
--
insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME)
values(3, 'CONT_TITLE_ON_PAGE', 'div.media_end_head_title > h2', '뉴스내용 제목');

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME)
values(3, 'CONT_MAIN_CONT', 'div.newsct_article', '뉴스내용 본문');

insert into WRAPPER_RULE(SEED_NO, WRAP_TYPE, WRAP_VAL, WRAP_NAME)
values(3, 'CONT_DATE_ON_PAGE', 'span._ARTICLE_DATE_TIME', '뉴스내용 날짜');

select * from WRAPPER_RULE wr  ;