package model;

import java.io.Serializable;

public class Reservation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String restName;
	public String reserveTime;
	
	public Reservation(String restName, String reserveTime){
		this.restName = restName;
		this.reserveTime = reserveTime;
	}

}
