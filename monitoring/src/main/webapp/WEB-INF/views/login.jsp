<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Авторизация</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>

<body>

<script type="text/javascript">


Ext.onReady(function(){	
		
	var loginPanel = Ext.create('Ext.form.Panel', {
		width: 310,
		height: 160,
		region:'center',
		title: 'Войти',
		frame: true,
		icon:'resources/images/login.png',
		layout: {
	        type: 'vbox',   
	        align: 'stretch',   
	        padding: 5
	    }, 
		items:[
		{
		    xtype: 'form',
		   	bodyStyle: "padding: 5px;",
		    items: [{
		        xtype: 'textfield',
		        id: 'username',
		        fieldLabel: 'Пользователь',
		        allowBlank: false
		    }, {
		        xtype: 'textfield',
		        id: 'password',
		        inputType: 'password',
		        fieldLabel: 'Пароль',
		        allowBlank: false,
		        
		        listeners: {
		        	specialkey: function(field, e){
	            		if (e.getKey() == e.ENTER) {
	            			submit();
	            		}
			        }	
		        }
		        
		    }],
		    buttons: [{
		        text: 'Войти',
		        id: 'submit',
		        handler: function(){
		        	submit();
			        	
		        }		  
		    }]
		 }]
		 
	}); 
	
	function submit(){
		var user = Ext.getCmp('username').value;
    	var pas = Ext.getCmp('password').value;	  
    	
    	Ext.Ajax.request({
       		url: 'j_spring_security_check',
    	    method: 'POST', 
        	params: {
            	'username'				 : user,
            	'password'				 : pas,
            	"${_csrf.parameterName}" : "${_csrf.token}"
        	},
        	success: function(response){
        		location.href = 'app'
    		},
    		failure: function(form, action) {
    			Ext.Msg.alert('Ошибка авторизации', 'Ошибка соединения с сервером');  
    		}
    		
    	})
	}
	
	 var viewport = Ext.create('Ext.container.Viewport', {
			style : 'background-image: url(resources/images/background2.jpg)',
	        layout : {
	        	type: 'vbox',
	            align: 'center',
	            pack: 'center'
	        },
	        items : [loginPanel]
	    });
});
</script>   


</body>
</html>