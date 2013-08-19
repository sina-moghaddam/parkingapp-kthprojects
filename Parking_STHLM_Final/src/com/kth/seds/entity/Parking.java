package com.kth.seds.entity;

import java.io.Serializable;

public class Parking implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private int totalSpace;
	private int availableSpace;
	private String openingHours;
	private double price;
	private String description;
	private boolean favorite;
	
	public Parking(String id, String name, String address, double latitude,
			double longitude, int totalSpace, int availableSpace,
			String openingHours, double price, String description, boolean favorite) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.totalSpace = totalSpace;
		this.availableSpace = availableSpace;
		this.openingHours = openingHours;
		this.price = price;
		this.description = description;
		this.favorite = favorite;
	}
	
	public Parking(){
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}
	public int getAvailableSpace() {
		return availableSpace;
	}
	public void setAvailableSpace(int availableSpace) {
		this.availableSpace = availableSpace;
	}
	public String getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFavorite(){
		return favorite;
	}
	public void setFavorite(boolean favorite){
		this.favorite = favorite;
	}
}
