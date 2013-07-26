/**
 * 
 */
DBG.ForumContentEditor = DBG.apply(DBG.ContentEditor, {

	// initComponent: { ForumContentEditor: false },
	initFieldMonitoring : {
		ForumContentEditor : false
	},
	addSubEditor : {
		ForumContentEditor : false
	},

	initTab : false,
	
	editDescription : {
		click : false
	},
	initDescriptionPanel : false,
	loadDescripton : false,

	MSG : false
		// checked also in PULSE_VIEWPORT_CONTROLLER

	});

ForumContentEditor = {
		/**
		 * 
		 * @type String
		 */
		type : 'ForumContentEditor',
		
		/**
		 * uncomment below for global DBG
		 */
		DBG : false/* DBG.ForumContentEditor */,
		
		/**
		 * 
		 * @return {}
		 */
		getEditDescriptionButtonConfig : function() {
			return {
				text : VIEWPORT_PROPERTIES.BUTTON_TEXT_EDIT,
				disabled : true,
				scope : this,
				handler : function() {

					var fckWinCfg = {
						loadURL : this.initDescriptionEditorURL,
						loadParams : {
							id : this.getContent().id
						},
						saveURL : this.saveDescriptionURL,
						saveParams : {
							id : this.getContent().id
						},
						scope : this,
						onSaveSuccess : 'loadDescription'
					};

					// DBG >>
					if (this.DBG) {
						THROW({
									propsObj : fckWinCfg,
									props : ['loadURL', 'loadParams', 'saveURL',
											'saveParams', 'scope', 'onSaveSuccess']
								}, this, 'editDescription.click');
					}

					AbstractBasicContentEditor.getFCKEditorWindow(fckWinCfg);
				},
				iconCls : 'editIconCls'
			};

		},

		/**
		 * 
		 * @return {}
		 */
		getReloadDescriptionButtonConfig : function() {
			return {
				text : VIEWPORT_PROPERTIES.BUTTON_TEXT_RELOAD,
				disabled : true,
				scope : this,
				handler : function() {
					this.loadDescription();
				},
				iconCls : 'reloadIconCls'
			};
		},
		
		/**
		 * loads the description-panel
		 */
		loadDescription : function() {

			// DBG >>
			if (this.DBG) {
				THROW({
							props : ['loadDescriptionURL', 'content.id']
						}, this, 'loadDescripton');
			}

			try {

				var loadCfg = {
					url : this.loadDescriptionURL,
					method : 'POST',
					callback : LPCB_DISABLE,
					params : {
						id : this.getContent().id
					},
					text : 'loading...',
					discardUrl : true,
					nocache : true,
					timeout : 30
				};
				this.descriptionPanel.load(loadCfg);

			} catch (e) {
				FATAL(e, this, 'loadDescripton');
			}
		},
		
		initDescriptionPanel : function() {

			// DBG >>
			if (this.DBG) {
				THROW({
							props : ['initDescriptionEditorURL',
									'saveDescriptionURL']
						}, this, 'initDescriptionPanel');
			}

			try {

				var buttons = [];

				// enable edit-description-button if URL
				var editDescriptionButtonConfig = this
						.getEditDescriptionButtonConfig();
				if (typeof(this.initDescriptionEditorURL) == 'string'
						&& this.initDescriptionEditorURL !== ''
						&& typeof(this.saveDescriptionURL) == 'string'
						&& this.saveDescriptionURL !== '') {
					editDescriptionButtonConfig.disabled = false;
					editDescriptionButtonConfig.scope = this;
				}

				var reloadDescriptionButtonConfig = this
						.getReloadDescriptionButtonConfig();
				if (typeof(this.loadDescriptionURL) == 'string'
						&& this.loadDescriptionURL !== '') {
					reloadDescriptionButtonConfig.disabled = false;
					reloadDescriptionButtonConfig.scope = this;
				}

				// builds description panel
				var tbar = [editDescriptionButtonConfig, '-', '->', '-',
						reloadDescriptionButtonConfig];
				var panelCfg = {
					id : this.getContent().clazz + '.' + this.getContent().id
							+ '.description.panel',
					title : VIEWPORT_PROPERTIES.DESCRIPTION + ':',
					collapsible : true,
					collapsed : true,
					titleCollapse : true,
					renderTo : this.getContent().clazz + '.' + this.getContent().id
							+ '.description',
					html : '...',
					bodyStyle : 'padding:5px;',
					tbar : tbar
				};
				this.descriptionPanel = new Ext.Panel(panelCfg);

				// disable summary-panels' content on expand
				this.descriptionPanel.on('expand', function(p) {
							if (true !== this.descriptionPanel.initialExpand) {
								this.loadDescription();
								this.descriptionPanel.initialExpand = true;
							}
						}, this);

			} catch (e) {
				// DBG >>
				if (this.DBG) {
					FATAL(e, this, 'initDescriptionPanel');
				}
			}

		},
		
		
		/**
		 * initialises the field-monitoring (calls super-class-method)
		 */
		initFieldMonitoring : function() {

			// DBG >>
			if (this.DBG) {
				THROW({
							args : arguments
						}, this, 'initFieldMonitoring');
			}

			try {

				// inititialise field-monitoring for
				// abstract-basic-content-editor-part
				Pulse.ForumContentEditor.superclass.initFieldMonitoring.call(this);

				// inititialise field-monitoring for Forum-field
				this.initForumMonitor();

			} catch (e) {
				// DBG >>
				if (this.DBG) {
					FATAL(e, this, 'initFieldMonitoring');
				}
			}
		},
		
		/**
		 * initializes this component.
		 */
		initComponent : function(boolInitTab) {

			// call super
			Pulse.ForumContentEditor.superclass.initComponent.call(this, boolInitTab);

			// // DBG >>
			// if (this.DBG) {
			// props = ['id','type'];
			// props = props.concat(DBG.getPropertyArray(this.initialConfig));
			// THROW({args:arguments,props:props},this,'initComponent.CMSContentEditor');
			// }

			try {

				// call inits
				this.initDescriptionPanel(); // builds descritpion-editor
				this.initFieldMonitoring();
				if (false !== boolInitTab) {
					this.initTab(); // adds buttons to tab-tool-bar
				}

			} catch (e) {
				FATAL(e, this, 'initComponent.ForumContentEditor');
			}

		}
};

Pulse.ForumContentEditor = Ext.extend(Pulse.ContentEditor, ForumContentEditor);
//END :: Pulse.ForumContentEditor