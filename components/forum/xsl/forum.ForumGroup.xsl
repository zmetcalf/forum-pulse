<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    version="2.0" exclude-result-prefixes="#all">

    <!-- maximum number of contents to be displayed by group in bundle: Forum -->
    <xsl:variable name="max.forum.contents" select="5"/>

    <!-- ContentGroup -->
    <xsl:template match="Content[@class='org.torweg.pulse.site.content.ContentGroup']" mode="forum">
        <xsl:apply-templates select="self::node()" mode="core">
            <xsl:with-param name="max.contents" select="$max.forum.contents" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

</xsl:stylesheet>
