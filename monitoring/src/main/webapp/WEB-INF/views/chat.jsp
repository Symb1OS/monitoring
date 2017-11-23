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
	
	var to = "symbios";
	var socket = new WebSocket("ws://localhost:8080/monitoring/chat/${user}");
	
	socket.onopen = function() {
		chatPanel.update({
    		time : new Date(),
    		from : "${user}",
    		to   : to,
    		data : 'Пользователь присоединился к чату'	
    	});
	};

	socket.onclose = function(event) {
	  if (!event.wasClean) {
	    Ext.Msg.alert('Внимание', 'Соединение закрыто. Код - ' + event.reason);
	  }
	};

	socket.onmessage = function(event) {
		console.log('Получено сообщение');
		var message = Ext.JSON.decode(event.data);
		updateChat(message);
	};

	socket.onerror = function(error) {
	  alert("Ошибка " + error.message);
	};
	
	var userPanel = Ext.create('Ext.panel.Panel', {
		title: 'Пользователи',
		region: 'east',
		width: '20%',
	    margin: '0 0 0 5',
		bodyStyle: {
			'background':'white'
		},
		
		items: [{
			xtype: 'treepanel',
			width: '100%',
			height: '100%',
	        rootVisible: false,
	        lines: false,
	        store: Ext.create('Ext.data.TreeStore', {
        	 
	        	proxy: {
                    type: 'ajax',
                    url: 'chat/users'
                },
	                
	        	root: {
	                expanded: true,
	                lines: false
	        	},
	        	
	        	listeners: {
	        		load: function(records, successful, operation, node){
	        			console.log
	        		}
	        	}
	        }),
	        
	        listeners: {
	        	selectionchange: function(selModel, selection) {
	        		to = selModel.getSelection()[0].get('text');
	        		console.log('to ' + to);
	        		
	        	}
	        }
		}]
	});
	
	var chatPanel = Ext.create('Ext.panel.Panel', {
		title: 'Написать разработчику',
		layout: 'border',
		region: 'center',
		collapsible : false,
		scrollable: true,
		
		bodyPadding: 5,
		bodyStyle: {
			'background':'white'
		},
		
	    tpl: new Ext.XTemplate( 
	    		'[{[this.formatDate(values.time)]}] <span style="color:green">{from}</span> : {data} <br>',
	    		{
	    			formatDate : function(date){
	    				return Ext.Date.format(date, 'H:i:s')
	    			}
	    		}
   		),
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
	
	function updateChat(message){
		console.log("Message: " + Ext.JSON.encode(message));
		chatPanel.update({
    		time : new Date(),
    		from : message.from,
    		to   : message.to,
    		data : message.data
    	});
	}
	
    function sendMessage(){
    	var message = Ext.getCmp('message');
    	var jsonMessage = {
        		time : new Date(),
        		from : '${user}',
        		to   : to,
        		data : message.getValue()	
       	};
    	message.setValue();
    	
    	chatPanel.update(jsonMessage);
    	socket.send(Ext.JSON.encode(jsonMessage));
    	
    }
    
    function loadHistory(){
    	console.log('load history from ' + to);
    	chatPanel.update({
    		time: new Date(),
    		data: 'Диалог с ' + to
    	})
    }
    
	var viewport = Ext.create('Ext.container.Viewport', {
		layout: 'border',
		items : [ chatPanel , userPanel] 
	})
	
})

</script>

</body>
</html>