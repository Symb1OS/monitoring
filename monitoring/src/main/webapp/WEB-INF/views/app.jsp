<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Личный кабинет</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>

<body>

<script type="text/javascript">

var defaultUrl = 'status';

var monitoringUrl = 'monitoring'
var settingUrl = 'settings';
var versionUrl = 'version';
var documentationUrl = 'https://docs.google.com/document/d/1nHBPqPJMfjfIYPM7Lq5DOQcpgsyJqxCyknAwGIVdd_M/edit#';

Ext.onReady(function showWindow() {
	
	var menu = Ext.create('Ext.panel.Panel', {
		title : 'Навигация',
		region : 'west',
		width : 210,
		minSize : 300,
		maxSize : 300,
		icon: 'resources/images/menu.png',
		collapsible : true,
		split : false,
		bodyStyle : {
			'padding' : '5px'
		},
		layout: {	
			 type:  'vbox',
			 align: 'center',
			 pack:  'start'
		},
		defaults:{
			xtype: 'button',
			scale: 'large',
			iconAlign: 'left',
			width: 200,
			height: 40,
			style:{
				marginBottom: '10px'
			}
		},
		items : [
			{
	  			text:'Мониторинг',
				icon:'resources/images/monitoring.png',
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = monitoringUrl;	
	  			}
			},
			{
	  			text:'Настройки',
				icon:'resources/images/settings.png',
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = settingUrl;
	  			}
			},
			{
	  			text:'Инструкция',
	  			icon:'resources/images/instruction.png',
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = documentationUrl;
	  			}
			},
			{
	  			text:'Скачать',
	  			icon:'resources/images/download.png',
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = versionUrl;
	  			}
			},
			{
	  			text:'Выйти',
	  			icon:'resources/images/logout.png',
	  			handler: function(){
	  				logout();
	  			}
			}
		
		]
	});
	
	function logout(){
		
		Ext.Ajax.request({
       		url: 'j_spring_security_logout',
    	    method: 'POST', 
        	params: {
            	"${_csrf.parameterName}" : "${_csrf.token}"
        	},
        	success: function(response){
        		location.href = 'login'
    		},
    		failure: function(form, action) {
    			Ext.Msg.alert('Ошибка соединения с сервером');  
    		}
    		
    	})
	}
	
	var main = Ext.create('Ext.panel.Panel', {
		xtype : 'form',
		region : 'center',
		width : 300,
		minSize : 300,
		maxSize : 300,
		split : false,
		frame: true,
		bodyStyle : {
			'padding' : '5px'
		},
		items : [{
        	xtype: 'component',
        	id: 'main',
        	autoEl: {
            	tag: 'iframe',
            	style: 'height: 100%; width: 100%; border: none',
            	src: 'settings'
        	}
    	}]
	});
	
	var viewport = Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [ menu, main]
	});
	
});

</script>

</body>
</html>