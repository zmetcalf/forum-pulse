<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
     xmlns="http://www.w3.org/1999/xhtml" version="2.0">

    <!-- include available locales -->
    <xsl:import href="../../../../xsl/globals.xsl"/>
    <xsl:include href="admin-viewport.Forum.babelfish.de.xsl"/>
    <xsl:include href="admin-viewport.Forum.babelfish.en.xsl"/>

    <!-- choose the right locale -->
    <xsl:template name="admin-viewport.Forum.babelfish">

        <!-- replace ' with \' within any string -->

        <xsl:variable name="jsText">
            <!-- call sub-babelfish -->
            <xsl:choose>
                <xsl:when
                    test="$locale='de' or $locale='de_DE' or $locale='de_AT' or $locale='de_CH'">
                    <xsl:call-template name="admin-viewport.Forum.babelfish.de"/>
                </xsl:when>
                <!-- default locale -->
                <xsl:otherwise>
                    <xsl:call-template name="admin-viewport.Forum.babelfish.en"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:value-of select="fn:replace($jsText,$apos,$backslashApos)"/>

    </xsl:template>
</xsl:stylesheet>
