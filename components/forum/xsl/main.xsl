<?xml version="1.0" encoding="UTF-8"?>
<!-- component:forum -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns="http://www.w3.org/1999/xhtml" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    exclude-result-prefixes="#all">

    <!-- includes -->
    <xsl:include href="forum.ForumGroup.xsl"/>
    <xsl:include href="forum.ForumContent.xsl"/>

    <!-- 
        match ForumContentDisplayerResult
        @see: forum.ForumContent.xsl
    -->
    <xsl:template match="result[@class='org.torweg.pulse.component.forum.ForumContentDisplayerResult']">
        <xsl:apply-templates select="Content" mode="forum"/>
    </xsl:template>

</xsl:stylesheet>