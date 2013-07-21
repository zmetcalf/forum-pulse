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
};

Pulse.ForumContentEditor = Ext.extend(Pulse.ContentEditor, ForumContentEditor);
//END :: Pulse.ForumContentEditor