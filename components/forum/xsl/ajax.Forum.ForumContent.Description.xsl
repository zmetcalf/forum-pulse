<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ext="http://ext-js.com" xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output encoding="UTF-8" indent="yes" method="xhtml" omit-xml-declaration="yes"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
        doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"/>

    <xsl:include href="../../../xsl/globals.xsl"/>


    <!-- 
        main (description-reload-)template
        use during description reloads
    -->
    <xsl:template match="/*">
        <div>
            <xsl:apply-templates
                select="//result[@class='org.torweg.pulse.site.content.admin.AbstractBasicContentEditorResult']/Content/description"
                mode="description"/>
        </div>
    </xsl:template>

    <!-- the editor -->
    <xsl:template match="Content" mode="description.editor">
        <div style="margin-top:5px;">
            <!-- panel -->
            <div id="{@class}.{@id}.description"> </div>
            <!-- panel-content -->
            <!--<div id="{@class}.{@id}.description.content" style="padding:5px;">
                <xsl:apply-templates select="description" mode="description"/>
            </div>-->
        </div>
    </xsl:template>

    <!-- simply returns the string-value of the description (used for description-panel-reloads) -->
    <xsl:template match="description" mode="description">
        <!--<xsl:value-of select="description-string/text()" disable-output-escaping="yes"/>-->
        <xsl:apply-templates select="body" mode="xhtml">
            <xsl:with-param name="scripts" tunnel="yes">
                <xsl:text>false</xsl:text>
            </xsl:with-param>
            <xsl:with-param name="editor" tunnel="yes">true</xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

</xsl:stylesheet>
