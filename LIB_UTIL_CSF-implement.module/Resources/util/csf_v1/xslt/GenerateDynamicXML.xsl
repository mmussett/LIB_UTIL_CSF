<?xml version="1.0" encoding="UTF-8" ?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
	<xsl:output method="xml" omit-xml-declaration="yes"
		encoding="UTF-8" indent="yes" />

	<xsl:template match="/">
		<xsl:element name="JSON">
			<xsl:apply-templates select="*" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="*">
		<xsl:call-template name="Name" />
	</xsl:template>

	<xsl:template name="Name">
		<xsl:for-each select="attribute">
			<xsl:element name="{Name}">
				<xsl:call-template name="Value" />
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="Value">
		<xsl:choose>
			<xsl:when test="attribute">
				<xsl:call-template name="Name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="Value" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:transform>