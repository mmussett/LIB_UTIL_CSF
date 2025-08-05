<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:tns="http://www.royallondon.com/xsd/resource/util/csf/rest/v1"
	xmlns:rsp="http://www.royallondon.com/xsd/internal/util/csf/restresponse/v1">
	
  <xsl:output indent="yes"/>
  <xsl:strip-space elements="*"/>
<!--     <xsl:variable name="filters" select="'/root,/root/path,/root/path/dir,/root/path/dir/file,/root/path/dir/dir,/root/path/dir/dir/file,b,c,'"/>-->
    <xsl:param name="FILTERS"/>


  <xsl:template match="/">
<rsp:Response>
      <xsl:apply-templates select="node()|@*"/>
</rsp:Response>
  </xsl:template>

  <xsl:template match="text()|@*">
<xsl:variable name="X">
        <xsl:call-template name="genPath"/>
</xsl:variable>
<!--<xsl:if test="contains($filters, concat($X,','))">-->
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
<!--</xsl:if>-->
<!--<xsl:value-of select="$X"/>-->
  </xsl:template>

  <xsl:template match="*">
<xsl:variable name="X">
        <xsl:call-template name="genPath"/>
</xsl:variable>
<xsl:if test="contains($FILTERS, concat($X,'/',local-name(),',')) or contains($FILTERS, concat($X,'/',local-name(),'/')) or contains($FILTERS, concat($X,','))">
    <xsl:copy>
<!--       <xsl:attribute name="path" select="concat($X,'')"/>-->
<!-- <xsl:attribute name="match" select="contains($FILTERS, concat($X,'/',local-name(),',')) or contains($FILTERS, concat($X,'/',local-name(),'/')) or contains($FILTERS, concat($X,','))"/>-->
<!--        <xsl:call-template name="genPath"/>
      </xsl:attribute>-->
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>    
</xsl:if>
  </xsl:template>

  <xsl:template name="genPath">
    <xsl:param name="prevPath"/>
<!--    <xsl:variable name="currPath" select="concat('/',name(),'[',
      count(preceding-sibling::*[name() = name(current())])+1,']',$prevPath)"/>-->
    <xsl:variable name="currPath" select="if ($prevPath != '') then concat('/',local-name(),$prevPath) else ' '"/>
    <xsl:for-each select="parent::*">
      <xsl:call-template name="genPath">
        <xsl:with-param name="prevPath" select="$currPath"/>
      </xsl:call-template>
    </xsl:for-each>
    <xsl:if test="not(parent::*)">
      <xsl:value-of select="normalize-space($currPath)"/>      
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>