<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ext="http://ext-js.com" xmlns="http://www.w3.org/1999/xhtml">

    <!-- includes -->
    <xsl:import href="i18n/admin-viewport.Forum.babelfish.xsl"/>

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
        
    <xsl:template match="/*">
        <div style="padding:5px;padding-bottom:15px;">

            <!-- inline-js localizations -->
            <xsl:call-template name="forumContent.Editor.inlineJs.LOC"/>
    
            <!-- builds html-part -->
            <xsl:apply-templates
                select="//result[@class='org.torweg.pulse.component.forum.admin.ForumContentEditorResult']/Content"
                mode="editorBuilder"/>

            <!-- inline-js -->
            <xsl:apply-templates
                select="//result[@class='org.torweg.pulse.component.forum.admin.ForumContentEditorResult']/Content"
                mode="inlineJs"/>

            <!-- debug -->
            <xsl:if test="$debug='true'">
                <div style="padding: 5px;">
                    <h1>This is an XSL output [<xsl:value-of select="$locale"/>]</h1>
                    <textarea rows="30" style="width:100%;">
                        <xsl:copy-of select="/"/>
                    </textarea>
                </div>
            </xsl:if>

        </div>
    </xsl:template>
    
    <xsl:template match="Content[@class='org.torweg.pulse.component.forum.model.StoreContent']"
        mode="editorBuilder">
        
        <!-- builds AbstractBasicContentEditor-Part -->
        <xsl:apply-templates select="self::node()" mode="AbstractBasicContent.Editor"/>
        
        <!-- builds description-editor --><!-- Put back in with description/post etc -->
        <!-- <xsl:apply-templates select="self::node()" mode="description.editor"/> -->

        <!-- builds ContentLocalizationMap-editor -->
        <xsl:apply-templates select="self::node()" mode="ContentLocalizationMap.Editor"/>

        <!-- builds sitemap-nodes -->
        <xsl:apply-templates select="self::node()" mode="SitemapNodes.Editor"/>

        <!-- builds attachments-editor -->
        <xsl:apply-templates select="self::node()" mode="Attachments.Editor"/>

        <!-- builds reference duration editor -->
        <xsl:apply-templates select="self::node()" mode="reference.duration.editor.div"/>

    </xsl:template>
    
    <!-- 
        localization
    -->
    <xsl:template name="forumContent.Editor.inlineJs.LOC">
        <script type="text/javascript">
            
            <!--// LOCALIZATIONS -->
            Pulse.LOC.ForumContentImageGroupEditor.toMainImage = '<xsl:call-template name="admin-viewport.Forum.babelfish">
                <xsl:with-param name="id" tunnel="yes">toMainImage</xsl:with-param>
            </xsl:call-template>';
            
            
        </script>
    </xsl:template>

    <xsl:template match="Content[@class='org.torweg.pulse.component.forum.model.ForumContent']" mode="inlineJs">
        <xsl:variable name="contentBundle" select="@bundle"/>
        <xsl:variable name="commandGeneratorResult"
            select="/result/result[@bundle=$contentBundle]/descendant::result[@class='org.torweg.pulse.component.core.CommandGeneratorResult']"/>

        <script type="text/javascript">    
            
            Ext.onReady(function() {
                
                <!-- tab-title set in ajax.AbstractBasicContent.Editor.xsl -->
                thisTab = Ext.getCmp('<xsl:value-of select="@class"/>.<xsl:value-of select="@id"/>.editor');
                
                <!-- init Editor -->
                thisTab.setEditor(new Pulse.ForumContentEditor({
                
                    tab: thisTab,
                    
                    id: '<xsl:value-of select="@class"/>.<xsl:value-of select="@id"/>.contentEditor',
                    
                    content: <xsl:apply-templates select="self::node()" mode="jsObject"/>,
                    
                    saveContentURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='saveContent']/text()"/>',
                    copyContentURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='copyContent']/text()"/>',
<!-- Use this for future decription/post etc --> <!--                     
                    initDescriptionEditorURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='initDescriptionEditor']/text()"/>',
                    saveDescriptionURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='saveDescription']/text()"/>',
                    loadDescriptionURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='loadDescription']/text()"/>',
                    -->
                    <!-- content-loc-map-editor -->
                    initContentLocalizationMapURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='initContentLocalizationMap']/text()"/>',
                    contentLocalizationMapAddURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='contentLocalizationMapAdd']/text()"/>',
                    browseContentRegistryURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='browseContentRegistrySelect']/text()"/>',
                    contentExpandPathURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='getContentRegistryIdPathContent']/text()"/>',
                    
                    <!-- shows sitemap-nodes -->
                    loadSitemapNodesURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='loadSitemapNodesForContent']/text()"/>',
                    
                    <!-- attachments -->
                    loadAttachmentsURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='loadAttachmentsForContent']/text()"/>',
                    initFileBrowserURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='initFileBrowserForAttachmentsForContent']/text()"/>',
                    fileBrowserTreeURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='browseVirtualFileSystem']/text()"/>',
                    fileBrowserPreviewURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='previewVirtualFileSystem']/text()"/>',
                    addAttachmentURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='addAttachmentToContent']/text()"/>',
                    editAttachmentURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='editAttachmentOfContent']/text()"/>',
                    deleteAttachmentURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='removeAttachmentFromContent']/text()"/>',
                    initAttachmentDescriptionEditorURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='initAttachmentDescriptionEditor']/text()"/>',
                    loadAttachmentDescriptionURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='loadAttachmentDescription']/text()"/>',
                    saveAttachmentDescriptionEditorURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='saveAttachmentDescriptionEditor']/text()"/>',
            
                    initReferenceDurationEditorURL: '<xsl:value-of select="$commandGeneratorResult/command[@name='initReferenceDurationEditor']"/>',
                    referenceDurationEditorTitle: '<xsl:call-template name="admin-viewport.babelfish">
                        <xsl:with-param name="id" tunnel="yes">AbstractBasicContent.reference.duration.editor</xsl:with-param>
                    </xsl:call-template>',
            
                    <xsl:variable name="class.id.parameter">
                        <xsl:text>?iHasContentPluginsClass=</xsl:text>
                        <xsl:value-of select="@class"/>
                        <xsl:text>&amp;iHasContentPluginsId=</xsl:text>
                        <xsl:value-of select="@id"/>
                    </xsl:variable>
            
                    getCreateContentPluginURLsURL : '<xsl:value-of select="$commandGeneratorResult/command[@name='getCreateContentPluginURLs']/text()"/><xsl:value-of select="$class.id.parameter" disable-output-escaping="yes"/>',
                    loadContentPluginsURL : '<xsl:value-of select="$commandGeneratorResult/command[@name='loadContentPlugins']/text()"/><xsl:value-of select="$class.id.parameter" disable-output-escaping="yes"/>'
            
                }));

                return;
                
            });
        </script>
    </xsl:template>   
</xsl:stylesheet>