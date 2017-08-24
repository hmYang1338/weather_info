DESC PERSON;
DESC COORDINATES;
DESC INTERESTAREA;
DESC WEATHER_INFO;
DESC CATEGORY;
DESC PRODUCT;
DESC SELL;
DROP TABLE PERSON;
DROP TABLE INTERESTAREA;
DROP TABLE COORDINATES;
DROP TABLE INTERESTAREA;

CREATE TABLE PERSON (
ID          VARCHAR2(40)    primary key,
PW          VARCHAR2(40)    not null,
NAME        VARCHAR2(20)    not null,
ADDR        VARCHAR2(100),
isADMIN     NUMBER         CHECK(isADMIN in (0, 1)));

CREATE TABLE COORDINATES (
LOCAL_CODE  NUMBER(15)      PRIMARY KEY,
LOCAL_NAME VARCHAR2(30),
PARENT_CODE NUMBER(15),
PARENT_NAME VARCHAR2(30),
X       NUMBER(4),
Y       NUMBER(4)
);

CREATE TABLE INTERESTAREA (
ID      VARCHAR2(40)        REFERENCES PERSON(ID),
LOCAL_CODE NUMBER(15)       REFERENCES COORDINATES(LOCAL_CODE)
);

CREATE TABLE WEATHER_INFO (
    X NUMBER(4)     , --�׸��� x��ǥ
    Y NUMBER(4)     , --�׸��� y��ǥ
    "HOUR" NUMBER(2), -- ���׿��� 3�ð� ����( 3, 6, 9, 12, 15, 18, 21, 24 ..)
    data_seq NUMBER(2), --�ð� ����
    "DAY" NUMBER(1), -- (����:0/����:1/��:2 ǥ��)
    TEMP NUMBER(4,1), --���� �ð� �µ� (hour�� 18�̶�� 15~18�� �µ�)
    TMX NUMBER(4,1), --���ְ���
    TMN NUMBER(4,1), --���������
    SKY NUMBER(1), --�ϴû��� (�ڵ尪) 1:����, 2:��������, 3:��������, 4:�帲
    PTY NUMBER(1), --�������� (�ڵ尪) 0:����, 1:��, 2:��/��, 3:��/��, 4: ��
    WFKOR VARCHAR2(50), -- ���� �ѱ��� (����, ��������, ���� ����, �帲, ��, ��/��, ��)
    wfEn varchar2(50), --���� ����
    POP NUMBER(3), --����Ȱ��(%)
    REH NUMBER(3), --����(%)
    WS NUMBER(4,1),--ǳ��(m/s) 
    WD NUMBER(1), --ǳ�� 0~7(8��) :��, �ϵ�, ��, ����, ��, ����, ��, �ϼ�
    wdKor varchar2(50), --ǳ�� �ѱ��� (��:E, ��:N, �ϵ�:NE, �ϼ�:NW, ��:S, ����:SE, ����:SW, ��:W)
    wdEn varchar2(50), --ǳ�� ���� 
    R12 NUMBER(4,1), --12�ð� ���� ������
    S12 NUMBER(4,1), --12�ð� ���� ������
    R06 NUMBER(4,1), --6�ð� ���� ������
    S06 NUMBER(4,1), --6�ð� ���� ������
    PRIMARY KEY (X, Y, "HOUR")
);

CREATE TABLE CATEGORY(
CATE_CODE   NUMBER      PRIMARY KEY,
CATE_NAME   VARCHAR2(50)
);

CREATE TABLE PRODUCT (
PRO_CODE    NUMBER      PRIMARY KEY,
PRO_NAME    VARCHAR2(30),
PRICE       NUMBER,
COMMENTS     VARCHAR2(200),
CATE_CODE   NUMBER      REFERENCES CATEGORY(CATE_CODE)
);

CREATE TABLE SELL (
SELL_DATE   NUMBER,
PRO_CODE    NUMBER      REFERENCES PRODUCT(PRO_CODE),
PRIMARY KEY(SELL_DATE, PRO_CODE)
);

select * from person;
SELECT name, pw, salt FROM person WHERE id = 'iiiii';
ALTER TABLE person ADD salt VARCHAR(300);
ALTER TABLE person ADD passwd VARCHAR2(50);
ALTER TABLE person MODIFY pw VARCHAR2(1024);
