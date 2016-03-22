package com.cyberswift.phe.dto;

import java.io.Serializable;

public class FacilityClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	private String tag = "";
	private String name = "";
	private boolean isSelected = false;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
