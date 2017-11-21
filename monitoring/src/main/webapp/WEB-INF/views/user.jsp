<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>

<html>
<head>
<title>Пользователи</title>

<link rel="shortcut icon" 	href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
<script type="text/javascript" 	src="<c:url value="/resources/js/utils.js"/>"></script>
</head>
<body>

	<script type="text/javascript">
		Ext.onReady(function() {

			readUrl = 'user/read';
			createUrl = 'user/add';
			updateUrl = 'user/update';
			destroyUrl = 'user/delete';

			Ext.define('userModel', {
				extend : 'Ext.data.Model',
				fields : [ 'username', 'enabled', 'licence_key', 'date_create', 'date_valid' ]
			});
			
			var userStore = Ext.create('Ext.data.Store', {
				model : 'userModel',
				autoLoad : true,
				proxy : {
					type : 'ajax',

					actionMethods : {
						read : 'POST',
						create : 'POST',
						update : 'POST',
						destroy : 'POST'
					},

					api : {
						read : readUrl,
						create : createUrl,
						update : updateUrl,
						destroy : destroyUrl
					},

					extraParams : {
						"${_csrf.parameterName}" : "${_csrf.token}"
					},
					
					writer: {
			            type: 'json',
			            writeAllFields: true
				    }
				}

			});
			
			var statusStore = Ext.create('Ext.data.Store', {
				fields : [ 'name', 'value' ],
				data : [ {
					name : 'Неактивна',
					value : 0
				}, {
					name : 'Активна',
					value : 1
				} ]
			});
			
			var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
				clicksToMoveEditor : 2,
				autoCancel : false
			});

				
			var userGrid = Ext.create('Ext.grid.Panel', {
				id : 'userGrid',
				store : userStore,
				region: 'center',
				collapsible : false,

				columns : {

					defaults : {
						align : 'center'
					},

					items : [ {
						text : 'Имя пользователя',
						dataIndex : 'username',
						width : 200,
						editor : {
							xtype : 'textfield',
							allowBlank : false
						}
					}, {
						text : 'Ключ',
						dataIndex : 'licence_key',
						width : 300
					}, {
						text : 'Дата создания',
						dataIndex : 'date_create',
						width : 200,
						renderer : function(val) {
							return Ext.Date.format(new Date(val), 'Y-m-d')
						}
					}, {
						text : 'Статус',
						type : 'combo',
						dataIndex : 'enabled',
						width : 100,
						allowBlank : false,
						renderer : function(val) {
							var index = statusStore.find('value', val);
							var rec = statusStore.getAt(index);
							return rec.get('name');
						},
						editor : {
							xtype : 'combo',
							autoComplete : true,
							editable : false,
							allowBlank : false,
							displayField : 'name',
							valueField : 'value',
							store : statusStore
						}
					}, {
						text : 'Дата <br> истечения',
						dataIndex : 'date_valid',
						align : 'center',
						width : 120,
						renderer : function(val) {
							return Ext.Date.format(new Date(val), 'Y-m-d')
						},
						editor : {
							xtype : 'datefield',
							startDay : 1,
							allowBlank : false,
							format : 'Y-m-d'
						}
					} ]

				},
				
				tbar : [ {
					text : 'Добавить',
					icon : 'resources/images/database_add.png',
					id : 'add',
					handler : function() {
						
						var r = Ext.create('userModel', {
							'username' : 'username',
							'licence_key' : '',
							'enabled' : 1,
							'date_create' : new Date(),
							'date_valid' : addDays(new Date(), 30)
						});

						userStore.insert(0, r);
						rowEditing.startEdit(0, 0);
					}
				}, {
					icon : 'resources/images/database_delete.png',
					itemId : 'remove',
					text : 'Удалить',
					disabled : true,
					handler : function() {

						var sm = Ext.getCmp('userGrid').getSelectionModel();
						userStore.remove(sm.getSelection());
						if (userStore.getCount() > 0) {
							sm.select(0);
						}
					}
				}, {
					text : 'Сохранить',
					icon : 'resources/images/save.png',
					scope : this,
					handler : function() {
						Ext.getBody().mask("Please wait..");
						userStore.sync({
							success : function(batch, options) {
								userStore.reload();		
							}
						});
						Ext.getBody().unmask();
					}
				}, {
					
					text : 'Обновить',
					icon : 'resources/images/refresh.png',
					scope : this,
					handler : function() {
						userStore.load();
					}
				} ],

				plugins : [ rowEditing ],

				listeners : {
					selectionchange : function(view, records) {
						if (!Ext.getCmp('add').isDisabled()) {
							Ext.getCmp('userGrid').down('#remove').setDisabled(
									!records.length);
						}
					}
				}
			});

			var userPanel = Ext.create('Ext.panel.Panel', {
				title : 'Пользователи',
				height : '100%',
				layout: 'border',
				collapsible : false,
				autoScroll : true,
				items : [ userGrid ]
			});
			

			var viewport = Ext.create('Ext.container.Viewport', {
				layout : {
					type : 'vbox',
					align : 'stretch',
					pack : 'start'
				},
				defaults : {
					bodyPadding : 10,
					margin : '0 0 10 0'
				},
				items : [ userPanel ]
			})

		});
		
	</script>

</body>
</html>