<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="2.0">
    <xsl:template name="admin-viewport.Forum.babelfish.de">
        <xsl:param name="id" tunnel="yes"/>
        <xsl:choose>
            <xsl:when test="$id='mainImage'">
                <xsl:text>Standardbild</xsl:text>
            </xsl:when>
            <xsl:when test="$id='alternativeImages'">
                <xsl:text>weitere Bilder</xsl:text>
            </xsl:when>
            <xsl:when test="$id='toMainImage'">
                <xsl:text>als Standardbild übernehmen</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <strong>[10!<xsl:value-of select="$id"/>!]</strong>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
