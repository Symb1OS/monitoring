<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Мониторинг</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>
<body>

<script type="text/javascript">
	
Ext.onReady(function showWindow() {
		
	Ext.create('Ext.Window', {
		id: 'manageWindow',
        title: 'Панель управления',
        width: 490,
        x: ((Ext.getBody().getViewSize().width / 2) - 245),
       	y:  0,
       	collapsible : true,
       	expandOnShow : true,
    	bodyStyle : {
			'padding' : '5px'
		},
        items: [{
        	xtype: 'fieldset',
        	title: 'Личное сообщение',
        	padding: '5 5 5 5',
        	items: [{
        		xtype: 'textfield',
        		id: 'username',
        		fieldLabel: 'Кому',
        		width: 300
        	},{
        		xtype: 'textfield',
        		id: 'message',
        		fieldLabel: 'Сообщение',
        		width: 450,
        	},{
        		xtype: 'button',
        		text: 'Отправить',
        		width: 100,
        		handler: function(){
        			
        			var username = Ext.getCmp('username')
        			var message = Ext.getCmp('message');
        			
        			Ext.Ajax.request({
        		   		url: 'action',
        			    method: 'POST', 
        			    params: {
        			    	command: 'PM',
        			    	username: username.getValue(),
        			    	message: message.getValue(),
        			    	"${_csrf.parameterName}" : "${_csrf.token}"
        			    },
        		    	success: function(response){
        					console.log(response);
        				},
        				failure: function(form, action) {
        					Ext.Msg.alert('Ошибка соединения с сервером');  
        				}
        			});
        			
        			message.setValue('');
        		
        		}
        	}]
        
        },{
        	xtype: 'fieldset',
        	title: 'Действия',
        	defaults: {
        		width: 130
        	},
        	padding: '5 5 5 5',
        	items: [{
        		xtype: 'button',
        		text: 'Выйти из игры',
        		handler: function(){
        			Ext.Ajax.request({
        		   		url: 'action',
        			    method: 'POST', 
        			    params: {
        			    	command: 'Exit',
        			    	"${_csrf.parameterName}" : "${_csrf.token}"
        			    },
        		    	success: function(response){
        					console.log(response);
        				},
        				failure: function(form, action) {
        					Ext.Msg.alert('Ошибка соединения с сервером');  
        				}
        			});
        		}
        	}
        		
        	]
        }
        ]
      
    }).show();
	
	var video = Ext.create('Ext.panel.Panel', {
		xtype : 'form',
		region : 'center',
		width : 300,
		minSize : 300,
		maxSize : 300,
		split : false,
		items : [{
        	xtype: 'component',
        	id: 'main',
        	autoEl: {
            	tag: 'iframe',
            	style: 'height: 100%; width: 100%; border: 0',
            	src: '${url}'
        	}
    	}]
	});
	
	var viewport = Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [ video]
	}); 
	
	
});

</script> 

</body>
</html>