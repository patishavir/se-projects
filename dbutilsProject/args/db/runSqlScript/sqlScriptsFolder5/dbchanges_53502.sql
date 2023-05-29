-- BS''D 

-- Created automatically by t181722


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_dev\App\NiarotErechServer\DBChanges\DB_SaritL - dbchanges for niyarotErech - for 5.35.01.sql"

ALTER TABLE "MATAF"."NEST_TOFES" ADD COLUMN NE_TOFES_NAME_FF VARCHAR(6);
ALTER TABLE "MATAF"."NEST_TOFES" ADD COLUMN NE_TOFES_AMLA_NAME_FF VARCHAR(6);
REORG TABLE NEST_TOFES;


UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0127'
WHERE  NE_MAP_NAME_PEULA = 'NE_KniyaRetzef' OR NE_MAP_NAME_PEULA = 'NE_MechiraRetzef';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0226'
WHERE NE_MAP_NAME_AMLA = 'NE_KniyaRetzefAmla' OR NE_MAP_NAME_AMLA = 'NE_MechiraRetzefAmla';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0135'
WHERE NE_MAP_NAME_PEULA = 'NE_ShinuyRetzef'OR NE_MAP_NAME_PEULA = 'NE_BitulRetzef';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0235'
--WHERE NE_MIS_MAP_AMLA = '3622' OR NE_MIS_MAP_AMLA = '3623';
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0235'
WHERE NE_MAP_NAME_AMLA = 'NE_ShinuyRetzefAmla' OR NE_MAP_NAME_AMLA = 'NE_BitulRetzefAmla';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0122'
--WHERE NE_MIS_MAP_PEULA = '3602'OR NE_MIS_MAP_PEULA = '3603' OR NE_MIS_MAP_PEULA = '3604';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0122'
WHERE NE_MAP_NAME_PEULA = 'NE_KniyaMaof'OR NE_MAP_NAME_PEULA = 'NE_MechiraMaof' OR NE_MAP_NAME_PEULA = 'NE_YetziraMaof';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0222'
--WHERE NE_MIS_MAP_AMLA = '3624' OR NE_MIS_MAP_AMLA = '3625'  OR NE_MIS_MAP_AMLA = '3626' ;

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0222'
WHERE NE_MAP_NAME_AMLA = 'NE_KniyaMaofAmla' OR NE_MAP_NAME_AMLA = 'NE_MechiraMaofAmla'  OR NE_MAP_NAME_AMLA = 'NE_YetziraMaofAmla' ;

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0119'
--WHERE NE_MIS_MAP_PEULA = '3609' OR NE_MIS_MAP_PEULA = '3612';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0119'
WHERE NE_MAP_NAME_PEULA = 'NE_ShinuyMaof' OR NE_MAP_NAME_PEULA = 'NE_BitulMaof';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0136'
--WHERE NE_MIS_MAP_AMLA = '3627' OR NE_MIS_MAP_AMLA = '3628' ;

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0136'
WHERE NE_MAP_NAME_AMLA = 'NE_ShinuyMaofAmla' OR NE_MAP_NAME_AMLA = 'NE_BitulMaofAmla' ;

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0130'
--WHERE NE_MIS_MAP_PEULA = '3616' OR NE_MIS_MAP_PEULA = '3617';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0130'
WHERE NE_MAP_NAME_PEULA = 'NE_ShinuyZarim' OR NE_MAP_NAME_PEULA = 'NE_BitulZarim';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0140'
--WHERE NE_MIS_MAP_AMLA = '3636' OR NE_MIS_MAP_AMLA = '3637' ;

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0140'
WHERE NE_MAP_NAME_AMLA = 'NE_ShinuyZarimAmla' OR NE_MAP_NAME_AMLA = 'NE_BitulZarimAmla' ;

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0139'
--WHERE NE_MIS_MAP_PEULA = '3614' OR NE_MIS_MAP_PEULA = '3615';

UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0139'
WHERE NE_MAP_NAME_PEULA = 'NE_KniyaZarim' OR NE_MAP_NAME_PEULA = 'NE_MechiraZarim';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0239'
--WHERE NE_MIS_MAP_AMLA = '3634' OR NE_MIS_MAP_AMLA = '3635' ;
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0239'
WHERE NE_MAP_NAME_AMLA = 'NE_KniyaZarimAmla' OR NE_MAP_NAME_AMLA = 'NE_MechiraZarimAmla' ;

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0120'
--WHERE NE_MIS_MAP_PEULA = '3610'OR NE_MIS_MAP_PEULA = '3613';
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0120'
WHERE NE_MAP_NAME_PEULA = 'NE_ShinuyChozim' OR NE_MAP_NAME_PEULA = 'NE_BitulChozim';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0138'
--WHERE NE_MIS_MAP_AMLA = '3632' OR NE_MIS_MAP_AMLA = '3633';
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0138'
WHERE NE_MAP_NAME_AMLA = 'NE_ShinuyChozimAmla' OR NE_MAP_NAME_AMLA = 'NE_BitulChozimAmla';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0132'
--WHERE NE_MIS_MAP_PEULA = '3605' OR NE_MIS_MAP_PEULA = '3606' OR NE_MIS_MAP_PEULA = '3607';
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_NAME_FF = 'NE0132'
WHERE NE_MAP_NAME_PEULA = 'NE_KniyaChozim' OR NE_MAP_NAME_PEULA = 'NE_MechiraChozim' OR NE_MAP_NAME_PEULA = 'NE_YetziraChozim';

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0239'
--WHERE NE_MIS_MAP_AMLA = '3634' OR NE_MIS_MAP_AMLA = '3635' ;
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0239'
WHERE NE_MAP_NAME_AMLA = 'NE_KniyaZarimAmla' OR NE_MAP_NAME_AMLA = 'NE_MechiraZarimAmla' ;

--UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0232'
--WHERE NE_MIS_MAP_AMLA = '3631' OR NE_MIS_MAP_AMLA = '3629' OR NE_MIS_MAP_AMLA = '3630';
UPDATE "MATAF"."NEST_TOFES" SET NE_TOFES_AMLA_NAME_FF = 'NE0232'
WHERE NE_MAP_NAME_AMLA = 'NE_YetziraChozimAmla' OR NE_MAP_NAME_AMLA = 'NE_KniyaChozimAmla' OR NE_MAP_NAME_AMLA = 'NE_MechiraChozimAmla';

COMMIT;

-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_dev\App\NiarotErechServer\DBChanges\DB_SaritL - dbchanges for niyarotErech - for 5.35.01.sql"


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_dev\App2\HamchaotMeutadotServer\DBChanges\DB_TalmorK - HamchaotMeutadot - S140-S144 - Change to a new table HMST_SUG_HAFKADA - 5.35.01.sql"

-- Please add here yours SQL lines for release number 5.34.01:

DROP ALIAS "MATAF"."HMST_SUG_HAFKADA";
CALL MATAF.mataf_drop('CHST_SUG_HAFKADA');

-- HMST_SUG_HAFKADA (local table) - START
CREATE TABLE "MATAF"."HMST_SUG_HAFKADA" 
(
  "HM_SUG_HAFKADA"	   	  VARCHAR(3)	NOT NULL,
  "HM_SUG_HAFKADA_TEUR"	  VARCHAR(50)	NOT NULL,
  "HM_KOD_HAFKADA"	  	  VARCHAR(1)	NOT NULL
) 
  IN "SNIFITZ";

ALTER TABLE "MATAF"."HMST_SUG_HAFKADA"
  ADD CONSTRAINT "HMST_SUG_HAFKADA_PK" PRIMARY KEY
    ("HM_SUG_HAFKADA");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE "MATAF"."HMST_SUG_HAFKADA" TO USER "MATAFAPP";

INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('01', '�����', '1');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('02', '������', '1');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('03', '���� ����� �����', '3');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('04', '������ �����', '3');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('05', '�������', '2');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('06', '����� ����', '1');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('07', '����� ����', '1');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('98', '������+����� ����', '0');
INSERT INTO HMST_SUG_HAFKADA (HM_SUG_HAFKADA, HM_SUG_HAFKADA_TEUR, HM_KOD_HAFKADA) VALUES ('99', '���', '0');

COMMIT;
-- HMST_SUG_HAFKADA (local tablet) - END

-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_dev\App2\HamchaotMeutadotServer\DBChanges\DB_TalmorK - HamchaotMeutadot - S140-S144 - Change to a new table HMST_SUG_HAFKADA - 5.35.01.sql"


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_dev\App\PtichatCheshbonServer\DBChanges\DB_Changes_535_01_dvora_kolitz.sql"

--start data changes in PCST_MAKOR_PEULA  [dvora kolitz]

update MATAF.PCST_MAKOR_PEULA
set PC_MAKOR_PEULA_TEUR = '���� ������ ����� �����'
where PC_MAKOR_PEULA = '04';

commit;
--end data changes in PCST_MAKOR_PEULA  [dvora kolitz]

-- start PCST_ISHUR_BAALUT [dvora kolitz]
CALL MATAF.mataf_drop('PCST_ISHUR_BAALUT');

CREATE TABLE "MATAF"."PCST_ISHUR_BAALUT" (
  "PC_ISHUREY_BAALUT"	VARCHAR(1)	NOT NULL,
  "PC_ISHUREY_BAALUT_TEUR"	VARCHAR(40)
) ;

ALTER TABLE "MATAF"."PCST_ISHUR_BAALUT"
  ADD CONSTRAINT "PCST_ISHUR_BAALUT_PK" PRIMARY KEY
    ("PC_ISHUREY_BAALUT");
    
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE "MATAF"."PCST_ISHUR_BAALUT" TO USER "MATAFAPP";   

INSERT INTO PCST_ISHUR_BAALUT
(PC_ISHUREY_BAALUT , PC_ISHUREY_BAALUT_TEUR) VALUES
('3', '���� �������� ������� ���� ��������'),
('2', '�� ����� ���'), 
('1', '����� �����');
COMMIT;
-- end PCST_ISHUR_BAALUT

--start data changes in PCST_NIHUL_LAKOACH_TABS  [dvora kolitz]
update MATAF.PCST_NIHUL_LAKOACH_TABS
set PC_STATUS = '2'
where PC_KESHER_LA_CH = '01' AND (PC_OFI_LAKOACH = '03' OR PC_OFI_LAKOACH = '05') AND PC_INDEX_TAB = '4';

commit;
--end data changes in PCST_NIHUL_LAKOACH_TABS  [dvora kolitz]

-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_dev\App\PtichatCheshbonServer\DBChanges\DB_Changes_535_01_dvora_kolitz.sql"


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_dev\App2\TochniyotChisachonServer\DBChanges\DB_MosheH TC TochniyotChisachon srikot for 5.35.02.sql"


-- mahut peula hafkada change table values for P132
DELETE FROM TCST_MAHUT_PEULA_HAFKADA;
COMMIT;

INSERT INTO TCST_MAHUT_PEULA_HAFKADA VALUES ('2', '����� ������ ��� �����');
INSERT INTO TCST_MAHUT_PEULA_HAFKADA VALUES ('3', '����� �� ����� ������ ���� ���� ����');
COMMIT;
-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_dev\App2\TochniyotChisachonServer\DBChanges\DB_MosheH TC TochniyotChisachon srikot for 5.35.02.sql"


-- *** Del Tbl Ver for forcing full tables submission (143 + 9) 

delete from REFTABLES_STS;
COMMIT;


-- *** Update Ver:

UPDATE mataf.BTT_VERSION_SUPPORT SET  VERSION = '5.35.02' WHERE VERSION = '5.35.01';
COMMIT;

