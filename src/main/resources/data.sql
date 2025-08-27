-- addr_ods.IBD_TB_ADDR_CODE_OF_DATA_STANDARD definition
CREATE SCHEMA addr_ods;
CREATE TABLE addr_ods.IBD_TB_ADDR_CODE_OF_DATA_STANDARD
(
    SEQ int,
    ADDRESS_ID varchar(80),
    FULL_ADDRESS varchar(3000),
    VALIDITY varchar(1),
    COUNTY varchar(9),
    COUNTY_CD varchar(5),
    TOWN varchar(12),
    TOWN_CD varchar(3),
    POST_CODE varchar(3),
    POST_CODE_DT varchar(100),
    TC_ROAD varchar(80),
    ROAD_ID varchar(80),
    ROAD_ID_DT varchar(100),
    X numeric(45,15),
    Y numeric(45,15),
    WGS_X numeric(45,15),
    WGS_Y numeric(45,15),
    GEOHASH varchar(8),
    XY_YEAR varchar(4),
    ADR_VERSION varchar(6),
    ETLDT timestamp
);

INSERT INTO addr_ods.IBD_TB_ADDR_CODE_OF_DATA_STANDARD
(SEQ, ADDRESS_ID, FULL_ADDRESS, VALIDITY, COUNTY, COUNTY_CD, TOWN, TOWN_CD, POST_CODE, POST_CODE_DT, TC_ROAD, ROAD_ID, ROAD_ID_DT, X, Y, WGS_X, WGS_Y, GEOHASH, XY_YEAR, ADR_VERSION, ETLDT)
VALUES(509425, 'BHB0915-2', '苗栗縣苗栗市恭敬里008鄰中正路１０６６號', 'T', '苗栗縣', '10005', '苗栗市', '010', '360', '2022-07-12', '中正路', '648110K', '2022-03-14', 231367.616000000000000, 2715701.525000000000000, 120.816078970426350, 24.547768504289390, 'wsmgthz9', '2023', '2.00', '2023-12-26 16:34:24.775');

CREATE TABLE addr_ods.IBD_TB_ADDR_DATA_REPOSITORY_NEW
(
    DATA_SOURCE varchar(80),
    VALIDITY varchar(1),
    RELIABILITY float,
    SEQ int,
    COUNTY varchar(9),
    COUNTY_CD varchar(5),
    TOWN varchar(12),
    TOWN_CD varchar(3),
    VILLAGE varchar(100),
    VILLAGE_CD varchar(10),
    NEIGHBOR_CD varchar(3),
    ROAD varchar(100),
    AREA varchar(100),
    ROAD_AREA_SN varchar(7),
    LANE varchar(100),
    LANE_ID_SN varchar(4),
    ALLEY varchar(100),
    SUBALLEY varchar(100),
    ALLEY_ID_SN varchar(7),
    NUM_TYPE varchar(3),
    NUM_TYPE_CD varchar(2),
    NUM_FLR_1 varchar(42),
    NUM_FLR_ID_1 varchar(6),
    NUM_FLR_2 varchar(30),
    NUM_FLR_ID_2 varchar(5),
    NUM_FLR_3 varchar(24),
    NUM_FLR_ID_3 varchar(4),
    NUM_FLR_4 varchar(24),
    NUM_FLR_ID_4 varchar(3),
    NUM_FLR_5 varchar(12),
    NUM_FLR_ID_5 varchar(1),
    BASEMENT_STR varchar(30),
    NUM_FLR_ID varchar(19),
    NUM_FLR_POS varchar(5),
    ROOM varchar(80),
    ROOM_ID_SN varchar(5),
    ADDR_REMAINS varchar(1000),
    REMARK varchar(3000),
    CREATE_DT varchar(7),
    UPDATE_TYPE varchar(20),
    ADR_VERSION varchar(6),
    ETLDT timestamp
);

-- addr_ods.IBD_TB_IH_CHANGE_DOORPLATE_HIS definition
CREATE TABLE addr_ods.IBD_TB_IH_CHANGE_DOORPLATE_HIS
(
    NEW_ADR varchar(3000),
    HIS_ADR varchar(3000),
    HIS_CITY varchar(100),
    HIS_VILLAGE varchar(50),
    HIS_NEIGHBOR_CD varchar(3),
    HIS_NEIGHBOR varchar(3),
    HIS_ADDRESS varchar(1000),
    UPDATE_DT varchar(8),
    UPDATE_CODE varchar(1),
    BUSINESS_CODE varchar(22),
    LV int,
    DOORPLATE_SEQ int,
    HISTORY_SEQ int,
    STATUS varchar(3),
    DATA_YR varchar(6),
    ADR_VERSION varchar(6),
    ADDRESS_ID varchar(80),
    ETL_DT timestamp
)

CREATE SCHEMA addr_stage
CREATE TABLE addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(
    UPDATE_CODE varchar(1),
    UPDATE_TYPE varchar(64),
    ETLDT timestamp
);

INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('0', '補發', '2024-03-04 14:11:56.713');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('1', '門牌初編', '2024-03-04 14:12:14.582');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('2', '門牌改編', '2024-03-04 14:12:22.005');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('3', '門牌增編', '2024-03-04 14:12:30.042');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('4', '門牌合併', '2024-03-04 14:12:37.782');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('5', '門牌廢止', '2024-03-04 14:12:46.142');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('6', '行政區域調整', '2024-03-04 14:12:54.562');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('7', '門牌整編', '2024-03-04 14:13:03.165');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('8', '戶政事務合併', '2024-03-04 14:13:11.363');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('A', '重送通報', '2024-03-04 14:13:20.328');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('B', '職權新增', '2024-03-04 14:13:28.042');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('C', '職權修改', '2024-03-04 14:13:35.942');
INSERT INTO addr_stage.IBD_TB_HISTORY_CODE_UPDATE
(UPDATE_CODE, UPDATE_TYPE, ETLDT)
VALUES('D', '職權刪除', '2024-03-04 14:13:41.030');


