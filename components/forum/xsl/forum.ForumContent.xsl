<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="#all">
    
    <!-- imports -->
    <xsl:import href="i18n/forum.babelfish.xsl"/>
    <xsl:import href="../../core/xsl/core.content.attachments.xsl"/>
    
    <!-- org.torweg.pulse.component.forum.model.ForumContent -->
    <xsl:template match="Content[@class='org.torweg.pulse.component.forum.model.ForumContent']"
        mode="forum">

        <!-- debug -->
        <xsl:if test="$debug.site='true'">
            <xsl:comment>forum:forum.ForumContent.xsl</xsl:comment>
        </xsl:if>
        
        <!-- attachments -->
        <xsl:apply-templates select="self::node()" mode="attachments"/>

        <!-- headline -->
        <h1>
            <xsl:value-of select="name/text()"/>
        </h1>
            
        
   	</xsl:template>
    
</xsl:stylesheet>