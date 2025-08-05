<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:tns="http://www.royallondon.com/xsd/interface/util/csf/exception/v1"
	xmlns:ns="http://www.royallondon.com/xsd/resource/util/csf/appexception/v1"
	xmlns:fd="http://tns.tibco.com/bw/fault" 
	xmlns:et="http://www.tibco.com/pe/EngineTypes">

	<xsl:template match="/">
		<ns:FaultDetails>
			<xsl:apply-templates select="node()" />
		</ns:FaultDetails>
	</xsl:template>

	<xsl:template match="fd:FaultDetails|et:ErrorReport">
		<xsl:if test="exists(./*[local-name()='ActivityName'])">
			<ns:ActivityName>
				<xsl:value-of select="string(./*[local-name()='ActivityName'])" />
			</ns:ActivityName>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='Data']/*)">
			<ns:Data>
				<xsl:copy-of select="./*[local-name()='Data']/*" />
			</ns:Data>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='Msg'])">
			<ns:Msg>
				<xsl:value-of select="string(./*[local-name()='Msg'])" />
			</ns:Msg>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='MsgCode'])">
			<ns:MsgCode>
				<xsl:value-of select="string(./*[local-name()='MsgCode'])" />
			</ns:MsgCode>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='FullClass'])">
			<ns:FullClass>
				<xsl:value-of select="string(./*[local-name()='FullClass'])" />
			</ns:FullClass>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='Class'])">
			<ns:Class>
				<xsl:value-of select="string(./*[local-name()='Class'])" />
			</ns:Class>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='ProcessStack'])">
			<ns:ProcessStack>
				<xsl:value-of select="string(./*[local-name()='ProcessStack'])" />
			</ns:ProcessStack>
		</xsl:if>
		<xsl:if test="exists(./*[local-name()='StackTrace'])">
			<ns:StackTrace>
				<xsl:value-of select="string(./*[local-name()='StackTrace'])" />
			</ns:StackTrace>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
