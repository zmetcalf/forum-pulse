<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ext="http://ext-js.com" xmlns="http://www.w3.org/1999/xhtml">

    <!-- includes -->
    <xsl:import href="i18n/admin-viewport.Forum.babelfish.xsl"/>

    <!-- import Store-Object js-templates -->
    <xsl:import href="ajax.Forum.jsObject.xsl"/>

    <!-- imports -->
    <xsl:import href="../../../xsl/globals.xsl"/>
    <xsl:import href="../../core/xsl/ajax.AbstractBasicContent.Editor.xsl"/>
    <xsl:import href="../../core/xsl/ajax.AbstractBasicContent.ContentLocalizationMap.xsl"/>
    <xsl:import href="../../core/xsl/ajax.AbstractBasicContent.SitemapNodes.xsl"/>
    <xsl:import href="../../core/xsl/ajax.AbstractBasicContent.Attachments.xsl"/>
    <xsl:import href="../../core/xsl/ajax.AbstractBasicContent.ReferenceDurationEditor.xsl"/>
    <xsl:import href="ajax.Forum.ForumContent.Description.xsl"/>
    
    <xsl:output encoding="UTF-8" indent="yes" method="xhtml" omit-xml-declaration="yes"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
        doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"/>
    
</xsl:stylesheet>