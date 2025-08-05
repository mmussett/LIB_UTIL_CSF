<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:tns="http://www.royallondon.com/xsd/resource/util/csf/rest/v1"
	xmlns:tns1="http://www.royallondon.com/xsd/resource/util/csf/exceptioninfo/v1"
	xmlns:tns2="http://www.royallondon.com/xsd/internal/util/csf/restexception/v1">

	<xsl:template match="/tns2:TransformExceptionRequest/tns2:Response">
		<tns2:TransformExceptionResponse>
			<tns2:Response>
				<xsl:element name="{local-name(node())}" namespace="{namespace-uri(node())}">
					<tns:errorID>
						<xsl:value-of select="/tns2:TransformExceptionRequest/tns1:ExceptionInfo/@exceptionID" />
					</tns:errorID>
					<tns:errorCode>
						<xsl:value-of select="/tns2:TransformExceptionRequest/tns1:ExceptionInfo/@exceptionCode" />
					</tns:errorCode>
					<tns:errorMsg>
						<xsl:value-of select="/tns2:TransformExceptionRequest/tns1:ExceptionInfo/@exceptionMessage" />
					</tns:errorMsg>
					<xsl:apply-templates select="@*|node()" />
				</xsl:element>
			</tns2:Response>
		</tns2:TransformExceptionResponse>
	</xsl:template>

	<xsl:template match="/tns2:TransformExceptionRequest/tns1:ExceptionInfo"/>

</xsl:stylesheet>