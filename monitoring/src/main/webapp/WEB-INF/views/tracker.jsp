<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Трекер</title>
<link rel="shortcut icon" 	href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>
<body>

<script type="text/javascript">

Ext.onReady(function(){	
	
	function statusRender(val){
		var index = statusStore.find('id', val);
		var rec = statusStore.getAt(index);
		var name = rec.get('name');
		if(val == 1){
			return '<span style="color:green">' + name + '</span>'
		}else if(2){
			return '<span style="color:red">' + name + '</span>'
		}
		
	}
	
	function typeRender(val){
		var index = typeStore.find('id', val);
		var rec = typeStore.getAt(index);
		return  rec.get('name');
		
	}
	
	Ext.define('issues.model.type', {
		extend : 'Ext.data.Model',
	    fields : [ 'id', 'name']	    
	});
	
	Ext.define('issues.model.status', {
		extend : 'Ext.data.Model',
		fields : [ 'id', 'name']
	});
	
	var typeStore = Ext.create('Ext.data.Store', {
		model : 'issues.model.type',
		autoLoad: true,
		
		proxy : {
			type : 'ajax',
			url: 'tracker/type'
		}

	});
	
	var statusStore = Ext.create('Ext.data.Store', {
		model : 'issues.model.status',
		autoLoad: true,
		
		proxy : {
			type : 'ajax',
			url: 'tracker/status'
		}

	});
	
	Ext.define('trackerModel', {
		extend : 'Ext.data.Model',
		fields : [ 'id', 'name', 'type', 'creator', 'executor','description']
	});
	
	var trackerStore = Ext.create('Ext.data.Store', {
		model : 'trackerModel',
		autoLoad: true,
		
		proxy : {
			type : 'ajax',
			url: 'tracker/issues'
		}

	});
	
	var trackerGrid = Ext.create('Ext.grid.Panel', {
		title : 'Ваши задачи',
		id : 'trackerGrid',
		store : trackerStore,
		height : '100%',
		region: 'center',
		collapsible : false,

		columns : {

			defaults : {
				align : 'center'
			},

			items : [{
				hidden: true,
				text : 'id',
				dataIndex : 'id',
				width : 100
				
			},{
				text : 'Название',
				dataIndex : 'name',
				width : 300
			}, {
				text : 'Тип',
				dataIndex : 'type',
				width : 100 ,
				renderer :typeRender, 
			},{
				text: 'Статус',
				dataIndex: 'status',
				width: 100 ,
				renderer : statusRender, 
			},{
				hidden: true,
				text: 'Создатель',
				dataIndex: 'creator',
				width: 100
			},{
				hidden: true,
				text: 'Исполнитель',
				dataIndex: 'executor',
				width: 100
			},{
				hidden: true,
				text: 'Описание',
				dataIndex: 'description',
				width: 100
			}]

		},
		
		tbar : [ {
			text : 'Создать задачу',
			id : 'add',
			icon : 'resources/images/add-bug.png',
			handler : function() {
				Ext.getCmp('issueWindow').show().center();		
			}
		
		}],

		listeners : {
			 itemdblclick: function(dv, record, item, index, e) {
				
			 	Ext.getCmp('editform').load({
        			url: 'tracker/issues/get',
        			waitMsg: 'Идет загрузка...',
        			method: 'GET',
        			params: {
        				id : record.id
        			},
        			success: function(form, action){
        	    		var success = action.result.success;
        	    		if(success == true){
        	    			  var data= Ext.JSON.decode(action.result.data)
        	    			  Ext.getCmp('editform').getForm().setValues(data);
       					}
        			},
        			failure: function(form, action) {
       			        Ext.Msg.alert("Load failed", 'Ошибка');
       			    }
        		})
        		
        		editIssue.center().show();
			 	
		     }
		}
		
	});
	
	 var editIssue = Ext.create('Ext.Window', {
        title: 'Просмотр и редактирование',
        icon : 'resources/images/edit-notes.png',
        closeAction: 'method-hide',
        width: 500,
        height: 450,
        
        items:[{
        	xtype: 'form',
        	id: 'editform',
        	
        	bodyStyle : {
      			'padding' : '5px'
      		},
              
            defaults: {
              	width: '100%',
            },
              
        	items:[{
            	xtype: 'textfield',
            	name: 'id',
            	editable: false,
            	fieldLabel : 'Номер задачи'
            },{
            	xtype: 'textfield',
            	name: 'creator',
            	editable: false,
            	fieldLabel : 'Автор'
            },{
            	xtype: 'textfield',
            	name: 'name',
            	fieldLabel : 'Тема'
            },{
            	xtype: 'combo',
            	name: 'type',
            	forceSelection: true,
            	store: typeStore,
            	fieldLabel : 'Тип',
				displayField: 'name',
                valueField: 'id',
            	renderer : typeRender
            },{
            	xtype: 'combo',
            	forceSelection: true,
            	store: statusStore,
            	name: 'status',
            	fieldLabel : 'Статус',
            	displayField: 'name',
                valueField: 'id',
            	renderer : statusRender
            },{
            	xtype: 'textarea',
            	name: 'description',
            	height: 200,
            	fieldLabel: 'Описание'
            }],
            
            buttons: [
            	{
            		text: 'Сохранить',
            		handler: function(){
            			Ext.getCmp('editform').submit({
            				url: 'tracker/issues/update',
            				method: 'POST',
            				params: {
            					"${_csrf.parameterName}" : "${_csrf.token}",
            				},
            				success: function(form, action) {
                                Ext.toast(action.result.message);
                                trackerStore.reload();
                                editIssue.hide();
                             },
                             
                             failure: function(form, action) {
                                 Ext.Msg.alert('Failed', action.result.message);
                             }
            				
            			})
            		}
            	},
            	{
            		text: 'Отмена',
            		handler: function(){
            			editIssue.hide();
            		}
            	}
            ]
        }],
        
        listeners: {
        	
        	hide: function(){
        		Ext.getCmp('editform').reset();
        	}
        }
	}); 
	
	Ext.create('Ext.Window', {
		id: 'issueWindow',
		title: 'Новая задача',
		icon : 'resources/images/add-bug.png',
		resizible: false,
        closeAction: 'method-hide',
        width: 500,
        height: 350,
       	
    	bodyStyle : {
			'padding' : '5px'
		},
		
		defaults:{
			labelWidth: 100,
			width: '100%',
			allowBlank: false
		},
		
        items: [{
        	xtype: 'textfield',
        	id: 'issuename',
        	fieldLabel: 'Название'
        },{
        	xtype: 'combo',
        	id: 'issuetype',
        	fieldLabel: 'Тип',
        	store: typeStore,
        	forceSelection : true,
            displayField: 'name',
            valueField: 'id',
        },{
        	xtype: 'textareafield',
        	id: 'issuedescription',
        	fieldLabel: 'Описание',
        	height: 200
        	
        }],
        
        buttons: [{
        	text: 'Сохранить',
        	handler : function(){

        		Ext.Ajax.request({
	           		url: 'tracker/issues/add',
	           		method: 'POST', 
	           		params: {
	           			"${_csrf.parameterName}" : "${_csrf.token}",
	           			
	           			name: Ext.getCmp('issuename').getValue(),
	            		type: Ext.getCmp('issuetype').getValue(),
	            		description : Ext.getCmp('issuedescription').getValue()
	                	
	            	},
	            	success: function(response){
	            		Ext.toast("Задача создана")
	            		Ext.getCmp('issueWindow').hide();
	            		trackerStore.reload();
	        		},
	        		failure: function(response) {
	            		Ext.Msg.alert('Ошибка', 'Внутрення ошибка'); 
	        		}
	        	})
        		
        	}
        },{
        	text: 'Отмена',
        	handler: function(){
        		Ext.getCmp('issueWindow').hide();
        	}
        }
        ],
        
        listeners: {
        	hide: function(){
        		Ext.getCmp('issuename').setValue(''),
        		Ext.getCmp('issuetype').setValue(''),
        		Ext.getCmp('issuedescription').setValue('')
        	}
        }
	})
	
	var trackerPanel = Ext.create('Ext.panel.Panel', {
		height : '100%',
		layout: 'border',
		collapsible : false,
		autoScroll : true,
		items : [ trackerGrid ]
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
		items : [ trackerGrid ]
	})

})

</script>
</body>
</html>