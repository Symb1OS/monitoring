<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Личный кабинет</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/style/image.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>

<body>

<script type="text/javascript">

var defaultUrl = 'status';

var monitoringUrl = 'monitoring'
var settingUrl = 'settings';
var versionUrl = 'version';
var paymentUrl = 'payment';
var bugtrackerUrl = 'tracker';
var chatUrl = 'chat';
var documentationUrl = 'https://docs.google.com/document/d/1DkkaUYzsAG57zADdlMZyV0jzGTR5s-Vo13wi64Z0TC8/edit#';

Ext.onReady(function showWindow() {
	var menu = Ext.create('Ext.panel.Panel', {
		title : 'Навигация',
		region : 'west',
		width : 210,
		minSize : 300,
		maxSize : 300,
		iconCls: 'icon-menu',
		collapsible : true,
		split : false,
		
	    viewModel: {
	        data: {
	            hidden: ${isBlocked}
	        }
	    },
		
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
				iconCls: 'icon-monitoring',
				bind:{
					hidden: '{hidden}'
				},
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = monitoringUrl;	
	  			}
			},
			{
	  			text:'Настройки',
				iconCls: 'icon-settings',
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = settingUrl;
	  			}
			},
			{
	  			text:'Инструкция',
	  			iconCls: 'icon-instruction',
	  			bind:{
					hidden: '{hidden}'
				},
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = documentationUrl;
	  			}
			},
			{
	  			text:'Скачать',
	  			iconCls: 'icon-download',
	  			bind:{
					hidden: '{hidden}'
				},
	  			handler: function(){
	  				Ext.getCmp('main').getEl().dom.src = versionUrl;
	  			}
			},
			{
				text: 'Оплата',
				iconCls: 'icon-payment',
				handler: function(){
					Ext.getCmp('main').getEl().dom.src = paymentUrl;	
				}
			},
			{
				text: 'Баг-трекер',
				iconCls: 'icon-bugtracker',
				handler: function(){
					Ext.getCmp('main').getEl().dom.src = bugtrackerUrl;	
				}
			},{
				text: 'Связь',
				iconCls: 'icon-chat',
				handler: function(){
					Ext.getCmp('main').getEl().dom.src = chatUrl;
				}
			},{
	  			text:'Выйти',
	  			iconCls: 'icon-logout',
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
		region : 'center',
		frame: true,
		bodyStyle : {
			'padding' : '5px'
		},
		
		items : [{
        	xtype: 'box',
        	id: 'main',
        	        	
         	autoEl: {
            	tag: 'iframe',
            	style: 'height: 100%; width: 100%; border: none',
            	src: 'settings'
        	}  
        	
    	}],
    	
    	listeners: {
    		afterrender : function(){
    			Ext.getCmp('main').el.dom.onload = function(){
    				var url = window.frames[0].document.URL;
    				if(url.indexOf('login') != -1){
    					location.href = 'login'
    				}
    			}
    		}
    	}  
	}); 
	
	var viewport = Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [ menu, main]
	});
	
});

</script>

</body>
</html>