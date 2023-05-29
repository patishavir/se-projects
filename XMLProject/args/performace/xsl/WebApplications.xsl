<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />

	<xsl:variable name="newline">

		<xsl:text>&#10;</xsl:text>
	</xsl:variable>

	<xsl:variable name="comma" select='","' />
	<xsl:variable name="equalSign" select='"="' />
	<xsl:strip-space elements="*" />
	<xsl:template match="/">
		<xsl:for-each select="//Stat[@name='Web Applications']">

			<xsl:sort select="ancestor::Server/@name" />

			<xsl:for-each select="CountStatistic[@name='RequestCount']">
				<xsl:sort select="ancestor::Server/@name" />
				<xsl:text>statName=</xsl:text>
				<xsl:value-of select="../@name" />
				<xsl:value-of select="$comma" />
				<xsl:text>server=</xsl:text>
				<xsl:value-of select="ancestor::Server/@name" />
				<xsl:value-of select="$comma" />
				<xsl:value-of select="./@name" />
				<xsl:text>=</xsl:text>
				<xsl:value-of select="./@count" />
			</xsl:for-each>

			<xsl:for-each select="TimeStatistic[@name='ServiceTime']">
				<xsl:value-of select="$comma" />
				<xsl:text>max </xsl:text>
				<xsl:value-of select="./@name" />
				<xsl:value-of select="$equalSign" />
				<xsl:value-of select="./@max" />
				<xsl:value-of select="$comma" />
				<xsl:text>startTime=</xsl:text>
				<xsl:value-of select="./@startTime" />
				<xsl:value-of select="$comma" />
				<xsl:text>lastSampleTime=</xsl:text>
				<xsl:value-of select="./@lastSampleTime" />
			</xsl:for-each>

			<!-- lastSampleTime="1505637835693" max="302361" min="0" name="ServiceTime" 
				startTime="1504451385181" totalTime="406401850" unit="MILLISECOND"/> -->
			<xsl:value-of select="$newline" />
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
