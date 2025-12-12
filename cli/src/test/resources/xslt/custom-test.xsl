<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    
    <xsl:template match="/prompt">
        <xsl:text>CUSTOM_XSLT_OUTPUT</xsl:text>
        <xsl:if test="title">
            <xsl:text>
Title: </xsl:text><xsl:value-of select="title"/>
        </xsl:if>
        <xsl:if test="goal">
            <xsl:text>
Goal: </xsl:text><xsl:value-of select="goal"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="text()"/>
</xsl:stylesheet>
