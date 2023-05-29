#!/usr/bin/ksh
targetFile=$1
cd /dshome/dsusers/;/usr/bin/find . -name "*\.sh" -o -name "*\.profile" | grep -v OLDVER| xargs zip $targetFile

zip $targetFile /oracle11/app/oracle11/product/11.2.0/client_1/network/admin/tnsnames.ora
zip $targetFile /Ds_act/ActHalbHonProd/uvodbc.config
zip $targetFile /Ds_bazel/Bazel2Prod/uvodbc.config
zip $targetFile /Ds_crm_prod/ProdCrm/uvodbc.config
zip $targetFile /Ds_dm_prod/ProdDm/uvodbc.config
zip $targetFile /Ds_gl_prod/ProdGl/uvodbc.config
zip $targetFile /Ds_liq_prod/LiqProd/uvodbc.config
zip $targetFile /Ds_ozr_prod/OtzProd/uvodbc.config
zip $targetFile /Ds_prod/KgProdL/uvodbc.config
zip $targetFile /Ds_prod/HrbnProd/uvodbc.config
zip $targetFile /Ds_prod/TkProd/uvodbc.config
zip $targetFile /Ds_prod/OutProjects/uvodbc.config
zip $targetFile /Ds_prod/DwhProd/uvodbc.config
zip $targetFile /Ds_sap/SapBitProd/uvodbc.config


 