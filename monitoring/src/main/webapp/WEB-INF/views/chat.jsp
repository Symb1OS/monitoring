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
	
	var userIcon = 'resources/images/user.png';
	var newMessageIcon = 'resources/images/new.png';
	var to = "symbios";
	
	var socket = new WebSocket("ws://localhost:8080/monitoring/chat/${user}");
	
	socket.onopen = function() {
		console.log('connection open');
	};

	socket.onclose = function(event) {
	  if (!event.wasClean) {
	    Ext.Msg.alert('Внимание', 'Соединение закрыто. Код - ' + event.reason);
	  }
	};

	socket.onmessage = function(event) {
		var message = Ext.JSON.decode(event.data);
		var tab = Ext.getCmp(message.from);
		
		console.log('new message')
		console.log(message);
		console.log(tab);
		
		if(typeof tab == 'undefined'){
			tab = chatPanel.add(Ext.create('chat.panel',{
    			id: message.from,
    			title: message.from,
    			icon: userIcon
    		}));
		}
		
		tab.update({
			time : new Date(),
			from : message.from,
			to   : message.to,
			data : message.data
		});
	
		var active = chatPanel.getActiveTab().getId();
		if(typeof active == 'undefined' | active !== tab.getId()){
			notify(tab)	
		}else{
			soundClick();
			tab.getTargetEl().scroll('b', 100000, true);
		}
		
	};

	socket.onerror = function(error) {
	  alert("Ошибка " + error.message);
	};
	
	function notify(tab){
		tab.setIcon(newMessageIcon);
		soundClick();
	}
	
	function soundClick() {
		  var audio = new Audio();
		  audio.src = 'resources/sound/message.mp3';
		  audio.autoplay = true;
	}
	
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
			id: 'usertree',
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
	        			Ext.getCmp('usertree').getSelectionModel().select(0);
	        		}
	        	}
	        }),
	        
	        listeners: {
	        	selectionchange: function(selModel, selection) {
	        		to = selModel.getSelection()[0].get('text');
	        		
	        		var tabTo = Ext.getCmp(to);
	        		if( typeof tabTo == 'undefined'){
	        			
		        		var tab = chatPanel.add(Ext.create('chat.panel',{
		        			id: to,
		        			title: to,
		        			icon: userIcon
		        		}))
		        		
		        		chatPanel.setActiveTab(tab);
		        		
	        		}else{
	        			chatPanel.setActiveTab(tabTo);
	        			chatPanel.getTargetEl().scroll('b', 100000, true);
	        		}
	        		
	        	}
	        }
		}]
	});
	
	var chatPanel = Ext.create('Ext.tab.Panel', {
		layout: 'border',
		region: 'center',
		collapsible : false,
		scrollable: true,
		
		bodyPadding: 5,
		bodyStyle: {
			'background':'white'
		},
		
		listeners: {
			
			add: function ( item, index) {
            	loadHistory(index);
            },
            
			tabchange: function(newCard ,newTab) {
				newTab.setIcon(userIcon);
                
            }
            
        }
	    	
	});
	
	function updateChat(message){
		chatPanel.update({
    		time : new Date(),
    		from : message.from,
    		to   : message.to,
    		data : message.data
    	});
	}
	
    function sendMessage(chatPanel){
    	to = chatPanel.getId();
    	var message = chatPanel.down('textfield');
    	var jsonMessage = {
        		time : new Date(),
        		from : '${user}',
        		to   : to,
        		data : message.getValue()	
       	};
    	message.setValue();
    	
    	chatPanel.update(jsonMessage);
    	chatPanel.getTargetEl().scroll('b', 100000, true);
    	socket.send(Ext.JSON.encode(jsonMessage));
    	
    } 
    
    function loadHistory(chattab){
    	console.log('load history')
    	Ext.Ajax.request({
       		url: 'chat/history',
    	    method: 'GET', 
        	params: {
            	username : chattab.getId(),
        	},
        	success: function(response){
        		json = Ext.JSON.decode(response.responseText);
        		console.log(response);
        		for(var index = 0; index < json.length; index++){
        			console.log(index);
        			var message = json[index];
        			chattab.update({
        				time: new Date(message.time),
        				from: message.from,
        				to : message.to,
        				data: message.data
        			});
        		}
        		
        		if(chatPanel.getActiveTab().getId() == chattab.getId()){
        			chattab.getTargetEl().scroll('b', 100000, true);
        		}
        			
    		},
    		failure: function(form, action) {
    			Ext.Msg.alert('Ошибка авторизации', 'Ошибка соединения с сервером');  
    		}
    		
    	})
    	
    }
    
	var viewport = Ext.create('Ext.container.Viewport', {
		layout: 'border',
		items : [ chatPanel , userPanel] 
	})
	
	Ext.define('chat.panel', {
		extend: 'Ext.panel.Panel',
		scrollable: true,
        closable: false,
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
		            width: '80%',
		            emptyText: 'Текст сообщения',
		            
		            listeners: {
		            	specialkey: function(field, e){
		            		if (e.getKey() == e.ENTER) {
		            			var chatPanel = this.up('panel');
		            			sendMessage(chatPanel);
		            		}
				        }	
		            }
		            
		        }, {
		            xtype: 'button',
		            width: '20%',
		            text: 'Отправить',
		            handler: function(){
		            	var chatPanel = this.up('panel');
            			sendMessage(chatPanel);
		            }
		        }]
	    }]
		
	})
	
})

</script>

</body>
</html>