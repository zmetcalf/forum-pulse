<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

    <!-- imports -->
    <xsl:import href="../../../../xsl/i18n/misc.babelfish.xsl"/>

    <!-- includes -->
    <xsl:include href="forum.babelfish.de.xsl"/>
    <xsl:include href="forum.babelfish.en.xsl"/>

    <!-- choose locale -->
    <xsl:template name="forum.babelfish">

        <xsl:choose>
            <xsl:when test="$locale='de' or $locale='de_DE' or $locale='de_AT' or $locale='de_CH'">
                <xsl:call-template name="forum.babelfish.de"/>
            </xsl:when>
            <!-- default locale -->
            <xsl:otherwise>
                <xsl:call-template name="forum.babelfish.en"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>