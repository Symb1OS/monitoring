package ru.namibios.monitoring.model;

import java.util.List;

public class TreeUser {

	private String text;
	private List<Children> children;

	public void setText(String text) {
		this.text = text;
	}

	public void setChildren(List<Children> children) {
		this.children = children;
	}

	public String getText() {
		return text;
	}

	public List<Children> getChildren() {
		return children;
	}
}