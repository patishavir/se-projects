<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />

	<xsl:variable name="newline">

		<xsl:text>&#10;</xsl:text>
	</xsl:variable>

	<xsl:variable name="comma" select='","' />
	<xsl:strip-space elements="*" />
	<xsl:template match="/">
		<xsl:for-each select="//Stat[@name='Transaction Manager']">
			<xsl:sort select="ancestor::Server/@name" />
			<xsl:for-each select="CountStatistic[@name='LocalBegunCount']">
				<xsl:value-of select="../@name" />
				<xsl:value-of select="$comma" />
				<xsl:value-of select="ancestor::Server/@name" />
				<xsl:value-of select="$comma" />
				<xsl:value-of select="./@name" />
				<xsl:text>=</xsl:text>
				<xsl:value-of select="./@count" />
			</xsl:for-each>

			<xsl:for-each select="CountStatistic[@name='CommittedCount']">
				<xsl:value-of select="$comma" />
				<xsl:value-of select="./@name" />
				<xsl:text>=</xsl:text>
				<xsl:value-of select="./@count" />
			</xsl:for-each>

			<xsl:for-each select="CountStatistic[@name='RolledbackCount']">
				<xsl:value-of select="$comma" />
				<xsl:value-of select="./@name" />
				<xsl:text>=</xsl:text>
				<xsl:value-of select="./@count" />
			</xsl:for-each>

			<xsl:value-of select="$newline" />

		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
