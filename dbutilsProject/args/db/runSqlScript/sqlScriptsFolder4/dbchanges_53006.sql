﻿--BS"D
--SariM H625 - rikuz hatavot beamlot for 5.30.05 HAVARA DECHUFA FOR SNIF 93 - PILOT  25/04/13
--
--
UPDATE GLST_URL_N_PARAMETERS SET URL_P = 'HTTP://FIBIRUNOUTAPPP/RUNOUTAPP/RUN.ASPX' WHERE SUBJECT = 'K625';   
COMMIT;
--SariM end 5.30.05

-- benym  update into HTST_MASAB_BAYATI for 5.30.05 TIKUN DACHUF
UPDATE "MATAF"."HZST_SUG_HORAT_BITUL" SET HZ_TEUR_SUG_BITUL = 'התערבות ידנית ויש הוראת ביטול לחשבון' WHERE HZ_KOD_SUG_BITUL = '3'; 
-- benym  END update for 5.30.05

UPDATE mataf.BTT_VERSION_SUPPORT SET  VERSION = '5.30.06' WHERE VERSION = '5.30.05';
COMMIT;