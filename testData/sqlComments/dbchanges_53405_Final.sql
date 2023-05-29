-- BS''D 

-- Created automatically by t181722


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\ExternalAppsServer\DBChanges\DB_DannyS - 5.33.20 - Fix URL for S380.sql"

DELETE FROM GLST_URL_N_PARAMETERS WHERE SUBJECT='S380';
commit;

INSERT INTO GLST_URL_N_PARAMETERS (SUBJECT,DESCRIPTION,URL_T,URL_Q,URL_K,URL_V,URL_P,APPKEY,PARAMETERS,USE_SVIVA) VALUES ('S380','סולמות ריבית','http://s6800045/S380_Sulamot_Ribit','http://s6800045/S380_Sulamot_Ribit','http://s6800045/S380_Sulamot_Ribit','http://s6800045/S380_Sulamot_Ribit','http://s6800045/S380_Sulamot_Ribit','','','Y');
/*
INSERT INTO GLST_URL_N_PARAMETERS (SUBJECT,DESCRIPTION,URL_T,URL_Q,URL_K,URL_V,URL_P,APPKEY,PARAMETERS,USE_SVIVA) VALUES ('S380','סולמות ריבית','HTTP://FIBIRUNOUTAPPT/RUNOUTAPP/RUN.ASPX','HTTP://FIBIRUNOUTAPPT/RUNOUTAPP/RUN.ASPX','HTTP://FIBIRUNOUTAPPT/RUNOUTAPP/RUN.ASPX','HTTP://FIBIRUNOUTAPPT/RUNOUTAPP/RUN.ASPX','HTTP://FIBIRUNOUTAPPP/RUNOUTAPP/RUN.ASPX','SR','AppKey={0}&P_ACC_TYP={1}&P_CURR={2}&sap-language={3}&P_TOKEN={4}&comp={5}','Y');
*/
commit;

-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\ExternalAppsServer\DBChanges\DB_DannyS - 5.33.20 - Fix URL for S380.sql"
sssstttttttttart -- hhhhhhhhhhhhhhhhhhhhhhhhhhololo

/*
this is a block comment 10
*/
-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_Int\Infra\MatafServer\DBChanges\DB_TibiG-update GL1823 for change 3232.sql"

SET SCHEMA = 'MATAF';
UPDATE "MATAF"."GLST_SGIA_MISHNI" SET GL_HODAA = 'לא ניתן לשלוף טפסים עקב תקלה בפניה למערכת P8' WHERE GL_HODAA = 'GL1823';
COMMIT; 
-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_Int\Infra\MatafServer\DBChanges\DB_TibiG-update GL1823 for change 3232.sql"


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\PtichatCheshbonServer\DBChanges\DB_Changes_534_05_eliezer_verzberger.sql"

--Start Develop 5.34.05 ------------------------------------------

--start data changes in GLST_URL_N_PARAMETERS  [Eliezer Verzberger]

update MATAF.GLST_URL_N_PARAMETERS
SET
  PARAMETERS = 'AppKey={0}&actimizeFormActionType={1}&displayTitle=onboarding&AcmModuleName=onboarding&pType={2}&primaryPartyKey={3}'
WHERE SUBJECT = 'ptichatCheshbonNewKYCFlow';

commit;
--end data changes in GLST_URL_N_PARAMETERS  [Eliezer Verzberger]

--End Develop 5.34.05 ------------------------------------------

/*
this is a block comment 20
bla bla
*/
-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\PtichatCheshbonServer\DBChanges\DB_Changes_534_05_eliezer_verzberger.sql"


-- *** Start File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\TifulServer\DBChanges\TifulDB_IlanaA-5.34.05.sql"

UPDATE GLST_SGIA_MISHNI SET GL_KOD_TIPUL ='P' WHERE GL_ZIHUY_HODAA = 'TI1008';
commit;
-- *** End File: "C:\ClearCaseViews\Snifit_BTT710_Int\App\TifulServer\DBChanges\TifulDB_IlanaA-5.34.05.sql"


-- *** Del Tbl Ver for forcing full tables submission (143 + 9) 

delete from REFTABLES_STS;
COMMIT;


-- *** Update Ver:

UPDATE mataf.BTT_VERSION_SUPPORT SET  VERSION = '5.34.05' WHERE VERSION = '5.34.04';
COMMIT;
/*
this is a block comment 100
*/
