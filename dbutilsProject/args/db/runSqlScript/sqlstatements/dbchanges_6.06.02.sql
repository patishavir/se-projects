-- BS''D 

-- Created automatically by T181722


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT820_Dev\App\PtichatCheshbonServer\DBChanges\DB_Changes_6_06_01_shiraR_Create and update tables.sql"

--Start Develop 6.06.01 --
-- Meshudarot 
-- -------------------------------------------------------------------------------------
-- DB changes in PCST_SUG_NEEMANUT (PCSTSNMT) [Shira]
CALL MATAF.mataf_drop('MATAF.PCST_SUG_NEEMANUT');
COMMIT;

CREATE TABLE "MATAF"."PCST_SUG_NEEMANUT" (
  "PC_SUG_NEEMANUT"	VARCHAR(3)	NOT NULL,
  "PC_SUG_NEEMANUT_TEUR"	VARCHAR(30),
  "PC_CHALVA_SW"	VARCHAR(1),
  "PC_CHAL_PEREK_D_SW" VARCHAR(1),
  "PC_YOTZER_MAGEN_NEEMANUT_SW" VARCHAR(1)
);

ALTER TABLE "MATAF"."PCST_SUG_NEEMANUT"
  ADD CONSTRAINT "PC_SUG_NEEMANUT_PK" PRIMARY KEY
    ("PC_SUG_NEEMANUT");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE MATAF.PCST_SUG_NEEMANUT TO USER MATAFAPP;
COMMIT;
---------------------------------------------------------------------------------------
-- DB changes in PCST_YOTZER_MAGEN_NEEMANUT (PCSTYMNT) [Shira]
CALL MATAF.mataf_drop('MATAF.PCST_YOTZER_MAGEN_NEEMANUT');
COMMIT;

CREATE TABLE "MATAF"."PCST_YOTZER_MAGEN_NEEMANUT" (
  PC_YOTZER_MAGEN_NEEMANUT VARCHAR(1) NOT NULL,
  PC_YOTZER_MAGEN_NEEMANUT_TEUR VARCHAR(53)) ;

ALTER TABLE "MATAF"."PCST_YOTZER_MAGEN_NEEMANUT"
  ADD CONSTRAINT "PC_YOTZER_MAGEN_NEEMANUT_PK" PRIMARY KEY
    ("PC_YOTZER_MAGEN_NEEMANUT");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE MATAF.PCST_YOTZER_MAGEN_NEEMANUT TO USER MATAFAPP;
COMMIT;
---------------------------------------------------------------------------------------
-- DB changes in PCST_FATCA_FILTER (PCSTFTCF) [Shira]

CALL MATAF.mataf_drop('MATAF.PCST_FATCA_FILTER');
COMMIT;

CREATE TABLE "MATAF"."PCST_FATCA_FILTER" (
  "PC_TAT_SIVUG_FATCA"		VARCHAR(2)	NOT NULL,
  "PC_GIIN_SW"				VARCHAR(1) NOT NULL,
  "PC_CHATIMA_W9_SW"		VARCHAR(1)  NOT NULL,
  "PC_CHATIMA_W8_SW"		VARCHAR(1)  NOT NULL,
  "PC_MS_NIYAR_ISIN_SW"		VARCHAR(1)  NOT NULL,
  "PC_SIBAT_PTOR_FATCA_SW" VARCHAR(1)  NOT NULL,
  "PC_HASHKAOT_LO_CRS_SW" VARCHAR(1)  NOT NULL
);

ALTER TABLE "MATAF"."PCST_FATCA_FILTER"
  ADD CONSTRAINT "PC_FATCA_FILTER_PK" PRIMARY KEY ("PC_TAT_SIVUG_FATCA");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE "MATAF"."PCST_FATCA_FILTER" TO USER "MATAFAPP";
COMMIT;

) ;
---------------------------------------------------------------------------------------
-- DB changes in PCST_PART1_4_CHAPTER3 (PCSTP1C3) [Shira]
CALL MATAF.mataf_drop('MATAF.PCST_PART1_4_CHAPTER3');
COMMIT;

CREATE TABLE "MATAF"."PCST_PART1_4_CHAPTER3" (
  PC_PART1_4_CHAPTER3 VARCHAR(2) NOT NULL,
  PC_PART1_4_CHAPTER3_TEUR VARCHAR(53)) ;

ALTER TABLE "MATAF"."PCST_PART1_4_CHAPTER3"
  ADD CONSTRAINT "PC_PART1_4_CHAPTER3_PK" PRIMARY KEY
    ("PC_PART1_4_CHAPTER3");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE MATAF.PCST_PART1_4_CHAPTER3 TO USER MATAFAPP;
COMMIT;
---------------------------------------------------------------------------------------
-- DB changes in PCST_PART3_14B (PCSTP3NB) [Shira]
CALL MATAF.mataf_drop('MATAF.PCST_PART3_14B');
COMMIT;

CREATE TABLE "MATAF"."PCST_PART3_14B" (
  PC_PART3_14B VARCHAR(2) NOT NULL,
  PC_PART3_14B_TEUR VARCHAR(53)) ;

ALTER TABLE "MATAF"."PCST_PART3_14B"
  ADD CONSTRAINT "PC_PART3_14B_PK" PRIMARY KEY
    ("PC_PART3_14B");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE MATAF.PCST_PART3_14B TO USER MATAFAPP;
COMMIT;