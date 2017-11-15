<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>

<html>
<head>
<title>Версии</title>

<link rel="shortcut icon" 	href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>
<body>

<script type="text/javascript">

Ext.onReady(function(){	
	
	Ext.define('versionModel', {
		extend : 'Ext.data.Model',
		fields : [ 'name', 'type', 'size']
	});
	
	var versionStore = Ext.create('Ext.data.Store', {
		model : 'versionModel',
		autoLoad: true,
		
		proxy : {
			type : 'ajax',
			url: 'version/files'
		},
		
		sorters : {
	        property: 'name',
	        direction: 'DESC'
	    }

	});
	
	var versionGrid = Ext.create('Ext.grid.Panel', {
		title: 'Версии',
		id : 'versionGrid',
		store : versionStore,
		region: 'center',
		collapsible : false,

		columns : {
			
			defaults:{
				align: 'center'
			},

			items : [{
				text : 'Версия',
				sortable: true,
				dataIndex : 'name',
				width : 200,
			},{
				hidden: true,
				text : 'Тип',
				dataIndex : 'type',
				width : 100
			},{
				text : 'Размер',
				dataIndex : 'size',
				width : 100 ,
				renderer: function(val){
					return ((val / 1000000).toFixed(2)) + 'Mb';
				} 
				
			},{
				text : 'Скачать',
				xtype : 'actioncolumn',
				width : 100,
				menuDisabled : true,
				items : [{
					align : 'center',
					icon : 'resources/images/downloadcolumn.png',
					scope : this,
					handler : function(grid, rowIndex, colIndex) {

						var rec = grid.getStore().getAt(rowIndex);
						var name = rec.get('name');
						  
						requestForm.submit({
							url: 'version/download',
							params: {
								name: name,
								"${_csrf.parameterName}" : "${_csrf.token}"
							}
					 	 });
						
					}
				}]
			
			}]
		}
	
	});
	
	var requestForm = Ext.create('Ext.form.Panel', {
 	 	standardSubmit: true,
 	 	hidden: true
	});
	
	var viewport = Ext.create('Ext.container.Viewport', {
		layout : {
			type : 'vbox',
			align : 'stretch',
			pack : 'start'
		},
		defaults : {
			margin : '0 0 10 0'
		},
		items : [ versionGrid ]
	})
	
});

</script>

</body>
</html>