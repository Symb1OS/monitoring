package ru.namibios.monitoring.websocket.chat;

import java.util.Date;

public class Message {

	private Date time;

	private String from;

	private String to;

	private String data;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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
		return "Message [time=" + time + ", from=" + from + ", to=" + to + ", data=" + data + "]";
	}
}