<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Обратная связь</title>
<link rel="shortcut icon" 	href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" 	src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>
<body>

<script type="text/javascript">

Ext.onReady(function(){	
	
	var user = "${user}";
	var socket = new WebSocket("ws://localhost:8080/monitoring/chat/" + user);
	
	socket.onopen = function() {
		chatPanel.update({
    		time : Ext.Date.format(new Date(), 'H:i:s'),
    		from : "${user}",
    		to   : "${user}",
    		message : 'Пользователь присоединился к чату'	
    	});
	};

	socket.onclose = function(event) {
	  if (!event.wasClean) {
	    Ext.Msg.alert('Внимание', 'Соединение закрыто. Код - ' + event.reason);
	  }
	};

	socket.onmessage = function(event) {
		console.log('Получено сообщение');
		console.log('event ' + evemt);
	};

	socket.onerror = function(error) {
	  alert("Ошибка " + error.message);
	};
	
	var chatPanel = Ext.create('Ext.panel.Panel', {
		title: 'Написать разработчику',
		height : '50%',
		layout: 'border',
		collapsible : false,
		scrollable: true,
		
		bodyPadding: 5,
		bodyStyle: {
			'background':'white'
		},
		
	    tpl: '[{time}] <span style="color:green">{from}</span> : {message} <br>',
       	tplWriteMode : 'append',
		
		dockedItems: [{
		        dock: 'bottom',
		        xtype: 'toolbar',

		        items: [{
		            xtype: 'textfield',
		            id: 'message',
		            width: '80%',
		            emptyText: 'Текст сообщения',
		            
		            listeners: {
		            	specialkey: function(field, e){
		            		if (e.getKey() == e.ENTER) {
		            			sendMessage();
		            		}
				        }	
		            }
		            
		        }, {
		            xtype: 'button',
		            width: '20%',
		            text: 'Отправить',
		            handler: function(){
		            	sendMessage();
		            }
		        }]
	    }]
	});
	
	function updateChat(data){
		console.log('data ' + data);
		chatPanel.update({
    		time : Ext.Date.format(new Date(), 'H:i:s'),
    		from : 'symbios',
    		to   : 'symbios',
    		message : message.getValue()	
    	});
	}
	
    function sendMessage(){
    	var message = Ext.getCmp('message');
    	var jsonMessage = {
        		time : Ext.Date.format(new Date(), 'H:i:s'),
        		from : 'symbios',
        		to   : 'symbios',
        		message : message.getValue()	
       	};
    	message.setValue();
    	
    	chatPanel.update(jsonMessage);
    	socket.send(Ext.JSON.encode(jsonMessage));
    	
    }
    
	var viewport = Ext.create('Ext.container.Viewport', {
		layout : {
			type : 'vbox',
			align : 'stretch',
			pack : 'start'
		},
		defaults : {
			margin : '0 0 10 0'
		},
		items : [ chatPanel ] 
	})
	
})

</script>

</body>
</html>