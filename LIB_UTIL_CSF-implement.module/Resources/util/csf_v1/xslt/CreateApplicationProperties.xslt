<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tib= "http://www.tibco.com/xmlns/repo/types/2002">
  <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>
  
  <xsl:template match="/tib:repository">
    <xsl:element name="configuration">
      <xsl:element name="properties">
        <xsl:apply-templates select="*"/>
      </xsl:element>
    </xsl:element>
  </xsl:template>

  <xsl:template match="//tib:globalVariable">
    <xsl:if test="not(contains(./tib:name, '//BW.')) and (not(starts-with(./tib:name, '//LIB_RESOURCE_HTTP-implement.module/')) or contains(./tib:name, '/HTTP_v1/httpConnectorResource/port')) and not(starts-with(./tib:name, '//LIB_RESOURCE_FTL-implement.module/')) and not(starts-with(./tib:name, '//LIB_RESOURCE_FTLCLIENT-implement.module/')) and not(starts-with(./tib:name, '//LIB_UTIL_CSF-implement.module/'))">
      <xsl:element name="property">
        <xsl:attribute name="name"><xsl:value-of select="./tib:name"/></xsl:attribute>
        <xsl:element name="environment">
          <xsl:attribute name="name">DEFAULT</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="./tib:value"/></xsl:attribute>
        </xsl:element>
      </xsl:element>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>