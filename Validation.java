/*
 * This class is to support the validation of inputs
 *
 */
 
 // Import dependencies
  import java.util.Date;

 // Create necessary objects
 
 public class Validation {
 	
	/**
		This method determines if a provided string is an integer or not.
		@param str The string to be tested.
		@return True of the string is an integer, false if it is not.
	*/
	public boolean isInteger(String str)
	{
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;				
	} // END isInteger method

	
	/**
		Checks if the difference between two dates is negative
		and returns false if so.
		
		@param d1 the starting date
		@param d2 the ending date
	*/
	public boolean validTrip(Date d1, Date d2) {
		// Declare testing variable
		long[] dateDifference = CalendarTools.calculateTimeDifference(d1, d2);
		
		// Declare return variable
		boolean valid = true;
		
		// Declare loop counter
		int index = 0;
		
		// For loop to check all elements for negatives
		for (index = 0; index < dateDifference.length; index++) {
			if (dateDifference[index] < 0) {
				valid = false;
			} // END if
		} // END for
		
		return valid;
		
	} // END validTrip method
	
	/**
		This function verifies the amount entered was positive since
		the expense cannot be a negative expense
	*/
	
	public boolean isMoney(float expense) {
		boolean valid = true;
		
		if (expense < 0) {
			valid = false;
		}
		
		return valid;  		
	}	
	
 } // END class Validation