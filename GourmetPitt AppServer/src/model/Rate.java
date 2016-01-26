package model;

import java.io.Serializable;

public class Rate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String customerName;
	public String restaurantName;
	public float rating;
	public String review;
	
	public Rate(String customerName, String RestaurantName, float Rating, String Review){
		this.customerName = customerName;
		this.restaurantName = RestaurantName;
		this.rating = Rating;
		this.review = Review;
	}
}
