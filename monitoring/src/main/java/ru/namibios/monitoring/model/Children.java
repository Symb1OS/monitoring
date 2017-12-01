package ru.namibios.monitoring.model;

public class Children {

	private String text;
	private boolean leaf;
	private String icon;

	public Children() {}
	
	public Children(String text, boolean leaf, String icon) {
		this.text = text;
		this.leaf = leaf;
		this.icon = icon;
	}

	public Children(String text) {
		this.text = text;
		this.leaf = true;
		this.icon = "resources/images/user.png";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Children [text=" + text + ", leaf=" + leaf + ", icon=" + icon + "]";
	}

}