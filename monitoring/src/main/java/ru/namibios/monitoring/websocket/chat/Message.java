package ru.namibios.monitoring.websocket.chat;

import java.sql.Date;

public class Message {

	private Date date;

	private String from;

	private String to;

	private String data;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Message [date=" + date + ", from=" + from + ", to=" + to + ", data=" + data + "]";
	}

}