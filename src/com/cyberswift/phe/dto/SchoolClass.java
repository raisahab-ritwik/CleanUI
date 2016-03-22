package com.cyberswift.phe.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class SchoolClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7L;

	private String block_Id = "";
	private String cluster_Id = "";
	private String district_Id = "";
	// private String Facility_Drinking_Water = "";
	// private String Facility_Sanitation = "";
	private String habitation_Id = "";
	private String id = "";
	private String lat = "";
	private String lon = "";
	private String no_Of_Student = "";
	private String school_Category = "";
	private String school_Classification = "";
	private String school_Name = "";
	private String state = "";
	private String village_Id = "";
	private ArrayList<FacilityClass> facilityList;

	public String getBlock_Id() {
		return block_Id;
	}

	public void setBlock_Id(String block_Id) {
		this.block_Id = block_Id;
	}

	public String getCluster_Id() {
		return cluster_Id;
	}

	public void setCluster_Id(String cluster_Id) {
		this.cluster_Id = cluster_Id;
	}

	public String getDistrict_Id() {
		return district_Id;
	}

	public void setDistrict_Id(String district_Id) {
		this.district_Id = district_Id;
	}

	public String getHabitation_Id() {
		return habitation_Id;
	}

	public void setHabitation_Id(String habitation_Id) {
		this.habitation_Id = habitation_Id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getNo_Of_Student() {
		return no_Of_Student;
	}

	public void setNo_Of_Student(String no_Of_Student) {
		this.no_Of_Student = no_Of_Student;
	}

	public String getSchool_Category() {
		return school_Category;
	}

	public void setSchool_Category(String school_Category) {
		this.school_Category = school_Category;
	}

	public String getSchool_Classification() {
		return school_Classification;
	}

	public void setSchool_Classification(String school_Classification) {
		this.school_Classification = school_Classification;
	}

	public String getSchool_Name() {
		return school_Name;
	}

	public void setSchool_Name(String school_Name) {
		this.school_Name = school_Name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVillage_Id() {
		return village_Id;
	}

	public void setVillage_Id(String village_Id) {
		this.village_Id = village_Id;
	}

	public ArrayList<FacilityClass> getFacilityList() {
		return facilityList;
	}

	public void setFacilityList(ArrayList<FacilityClass> facilityList) {
		this.facilityList = facilityList;
	}

}
