<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tib= "http://www.tibco.com/xmlns/repo/types/2002" exclude-result-prefixes="tib">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
	
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()"/>
		</xsl:copy>
    </xsl:template>

	<xsl:template match="tib:globalVariable">
		<xsl:choose>
			<xsl:when test="tib:value">
	  			<xsl:copy-of select="."/>
			</xsl:when>
			<xsl:otherwise>
			    <globalVariable>
				    <name><xsl:value-of select="tib:name"/></name>
			  	    <value/>
			  	    <deploymentSettable><xsl:value-of select="tib:deploymentSettable"/></deploymentSettable>
			  	    <serviceSettable><xsl:value-of select="tib:serviceSettable"/></serviceSettable>
			  	    <type><xsl:value-of select="tib:type"/></type>
			  	    <isOverride><xsl:value-of select="tib:isOverride"/></isOverride>
			    </globalVariable>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>