package myexception;

public class noPotionException extends Exception {
	
	public String getError() {
		return "You dont have any potion right now!";
	}
}
