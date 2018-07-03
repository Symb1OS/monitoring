package ru.namibios.monitoring.model;

public abstract class Notification {
	
	public static final String KEY_TELEGRAM;
	
	public static final String REG_DATA = "Username: %s \nPassword: %s \nlicence_key: %s";
	
	static {
		KEY_TELEGRAM = System.getenv("TELEGRAMM_API_KEY") == null ? "0bc852-1b0d72-4ca6e6"
													 			 : System.getenv("TELEGRAMM_API_KEY");  
	} 
	
	protected String message; 
	
	private Notification nextNotification; 
	
	public Notification(String message) {
		this.message = message;
	}
	
	public void setNextNotification(Notification notification) {
		this.nextNotification = notification;
	}
	
	public void notifyUser() {
		
		send();
		
		if(nextNotification != null) {
			nextNotification.notifyUser();
		}
	}
	
	public abstract void send();
	
	public static void main(String[] args) {
		System.out.println(Notification.KEY_TELEGRAM);
	}
}