<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
	 <!--<xsl:value-of select="/PerformanceMonitor/Node/Server/Stat/Stat/Stat/Stat/CountStatistic/@count"></xsl:value-of>-->
	<!-- <xsl:value-of select="sum(//CountStatistic/@count)"></xsl:value-of>-->
      <count>

       <xsl:apply-templates select="/PerformanceMonitor/Node/Server/Stat/Stat"> 
       
       
       </xsl:apply-templates>
</count>
	</xsl:template>
	
	<xsl:template match="Stat">
	 <app>
	 <xsl:value-of select="@name"/>
	  <xsl:value-of select="sum(./Stat//CountStatistic/@count)"/>
	 </app>
	</xsl:template>
	
</xsl:stylesheet>
