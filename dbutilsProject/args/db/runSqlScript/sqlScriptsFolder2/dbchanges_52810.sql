﻿--G.w.

-- ARST_SUG_BITACHON - local table - START
DROP TABLE "MATAF"."ARST_SUG_BITACHON";

CREATE TABLE "MATAF"."ARST_SUG_BITACHON" (
  "AR_SUG_BITACHON"	   	  VARCHAR(1)	NOT NULL,
  "AR_SUG_BITACHON_TEUR"  VARCHAR(40)	NOT NULL
) 
  IN "SNIFITZ";

ALTER TABLE "MATAF"."ARST_SUG_BITACHON"
  ADD CONSTRAINT "ARST_SUG_BITACHON_PK" PRIMARY KEY
    ("AR_SUG_BITACHON");

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE "MATAF"."ARST_SUG_BITACHON" TO USER "MATAFAPP";

INSERT INTO ARST_SUG_BITACHON (AR_SUG_BITACHON, AR_SUG_BITACHON_TEUR) VALUES ('1', 'בטחון אחר');
INSERT INTO ARST_SUG_BITACHON (AR_SUG_BITACHON, AR_SUG_BITACHON_TEUR) VALUES ('2', 'בטחון כספי');
-- ARST_SUG_BITACHON - local table - END
COMMIT;

-- <IditG>, <IditG MP dbchanges - add message MP1003 to GLST_SGIA_MISHNI for 5.28.21>, 5/02/2012>
INSERT INTO GLST_SGIA_MISHNI (GL_ZIHUY_HODAA,GL_HODAA,GL_MIVNE_HALON_HODAA,GL_HUMRA_HODAA,GL_KOD_TIPUL)
VALUES('MP1003','יציאה מהעסקה ללא שמירה/שידור, הפעולה הגיעה מעסקה כספית דרוש אישור מנהל','MS','S','S');
COMMIT;

UPDATE mataf.BTT_VERSION_SUPPORT SET  VERSION = '5.28.10' WHERE VERSION = '5.28.09';
COMMIT;

-- MosheH 07.02.2012.

DROP TABLE "MATAF"."GLST_SIBA_DCHIA";

 

CREATE TABLE "MATAF"."GLST_SIBA_DCHIA" (

  "GL_KOD_SIBA_DCHIA"   VARCHAR(2)  NOT NULL,

  "GL_SIBA_DCHIA_TEUR"  VARCHAR(25),

  "GL_NOSE" VARCHAR(1),

  "GL_ZIHUY_MAARECHET" VARCHAR(2)

) 

  IN "SNIFITZ";

 

ALTER TABLE "MATAF"."GLST_SIBA_DCHIA"

      ADD CONSTRAINT "GLST_KOD_SIBA_DCHIA_PK" PRIMARY KEY

    ("GL_KOD_SIBA_DCHIA");

 

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE "MATAF"."GLST_SIBA_DCHIA" TO USER "MATAFAPP";

 

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('1','אין כיסוי','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('2','חוסר ביטחונות','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('3','התקבלה הוראת ביטול מהלקוח','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('4','פרטי העברה שגויים','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('5','סכום שגוי','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('6','מטבע שגוי','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('7','פרטי מוטב שגויים','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('8','פרטי בנק מוטב שגויים','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('9','פרטי תשלום שגויים','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('10','ערך העברה בטל','W','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('11','חריגה מסמכות מנהל סניף','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('12','לא הושלמו מסמכי הבטחות','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('13','אחוז השענות לבטחון שגוי','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('14','הסכום שנלקח לביטחון שגוי','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('15','הבטחון לא תואם להתחייבות','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('16','אחר','B','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('17','הלקוח התחרט','O','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('18','אחר','O','GL');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('19','אין כיסוי','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('20','חוסר ביטחונות','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('21','פרטי העברה שגויים','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('22','סכום שגוי','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('23','פרטי מוטב שגויים','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('24','פרטי בנק מוטב שגויים','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('25','אחר','A','TG');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('26','אין כיסוי','A','HV');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('27','משמש לביטחון','A','HV');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('28','חוסר ביטחונות','A','HV');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('29','התקבלה הוראת ביטול מהלקוח','A','HV');

INSERT INTO "MATAF"."GLST_SIBA_DCHIA" (GL_KOD_SIBA_DCHIA,GL_SIBA_DCHIA_TEUR,GL_NOSE,GL_ZIHUY_MAARECHET) 

      VALUES ('30','אחר','A','HV');

commit;
