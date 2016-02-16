package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String userName;
	public String password;
	public String identification;
	public String flavor;
	public ArrayList<String> favorites;
	public ArrayList<Reservation> reservations;
	public ArrayList<Rate> rates;
	
	public Customer(String UserName, String Password, String Identification, String Flavor,
			ArrayList<String> Favorites, ArrayList<Reservation> Reservations, ArrayList<Rate> Rates){
		this.userName = UserName;
		this.password = Password;
		this.identification = Identification;
		this.flavor = Flavor;
		this.favorites = Favorites;
		this.reservations = Reservations;
		this.rates = Rates;
	}
}
