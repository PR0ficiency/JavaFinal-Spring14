/**
 *            File: SnapperFinalProject.java
 *           Title: TravelEx
 *	  Company name: SpaceTurtles Inc.
 *    Developed by: Craig Medlin, Samantha Haiar, Brandon Maddox
 *   Last modified: April 26, 2014
 *          Course: COP2250, Professor Sauls
 *     Description: This program is used to calculate the reimbursable expenses 
 *                  for the employee of SpaceTurtles Inc. that has specific requirements 
 *                  for allowable expenses on a trip. The program gathers the trip 
 *                  information, then asks the necessary questions to determine 
 *                  the total expenses of the trip before calculating and displaying 
 *                  back the reimbursable expenses and any cost to the employee for 
 *                  the trip.
 *  Dependent data: The following dependencies are used to perform the calculations.
 *					If any of your restrictions are different, this program may 
 *					produce errant results.
 *					
 *					SpaceTurtles Inc. allows the following expenses:
 *                  - $7 per day for parking fees
 *                  - $12 per day for taxi usage
 *                  - $99 per night for lodging
 *                  - $9 for breakfast
 *                  - $12 for lunch
 *                  - $16 for dinner
 *
 *                  On the first day, SpaceTurtles Inc. will cover:
 *                  - breakfast if departure time is before 7 a.m.
 *                  - lunch if departure time is before noon
 *					- dinner if departure time is before 6 p.m.
 *
 * 					On the last day, SpaceTurtles Inc. will cover:
 *					- breakfast if departure time is after 8 a.m.
 *					- lunch if departure time is after 1 p.m.
 *					- dinner if arrival time is after 7 p.m.
 *
 * Dependent files: PromptTools.java
 *                  FileManager.java
 *                  Validation.java
 *                  CalendarTools.java
 *
 *
 * TempFile notes: When program finishes running, the temp file will hold the following values:
 *					Employee Name
 *					Employee Title
 *					Departure date
 *					Arrival date
 *					Duration of trip (days)
 *									 (hours)
 *									 (minutes)
 *					
 *
 *
 */
 
 // Import dependencies
 import java.util.Date;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.io.*;
 import javax.swing.JOptionPane;
 
 public class SnapperFinalProject {
	
	/* Declare class-global variables */
	
	private static final String TEMP_FILENAME = "_tempTravelEx.txt";			// For temporary file filename
	private static final String INVOICE_FILENAME = "TravelEx_Invoice.txt";	// For invoice file filename
	
	// Space Turtles Inc. reimbursement rates in U.S. dollars
	/* CHANGE THESE IF THE RATES HAVE CHANGED */
	
	  private static final int PARKING_RATE = 7;								// For daily parking fee rate
	     private static final int TAXI_RATE = 12;								// For daily taxi usage rate
	    private static final int HOTEL_RATE = 99;								// For daily lodging rate
	private static final int BREAKFAST_RATE = 9;								// For daily breakfast rate
	    private static final int LUNCH_RATE = 12;								// For daily lunch rate
	   private static final int DINNER_RATE = 16;								// For daily dinner rate
	private static final float MILEAGE_RATE = 0.445F;							// Mileage rate in cents per mile
	
	private static float[] dailyExpenses;				 	// To store the expenses for each day
	private static String[] employeeInfo = new String[2];	// Holds employee title[0] and name[1]
	private static boolean[] travelTypes = { false,			// For if employee took flight
											false,			// For if employee used personal vehicle
											false,			// For if employee used taxi
											false,			// For if employee paid for lodging
											false,			// For if employee incurred parking fees
											false };		// For if employee rented a vehicle
											
	private static String msg = null;					// Temporary placeholder for message prompts
	private static int daysOfTrip = 0;					// To hold the number of days the trip lasted
	private static float allowableExpenses = 0.0F;		// To hold the total allowable expenses
	private static float accruedExpenses = 0.0F;		// To hold the total expenses incurred by employee
	private static float outOfPocketExpenses = 0.0F;	// To hold the total amount employee must pay
	private static float airfareExpenses = 0.0F;		// To hold the amount of airfare expenses
	private static float taxiExpenses = 0.0F;           // To hold the amount of taxi expenses
	private static float lodgingExpenses = 0.0F;        // To hold the amount of lodging expenses
	private static float drivingExpenses = 0.0F;        // To hold the amount of personal vehicle expenses
	private static float carRentalExpenses = 0.0F;      // To hold the amount of car rental expenses
	private static float otherExpenses = 0.0F;          // To hold the amount of other expenses
	
	private static boolean firstBreakfast = false;		// Flag to check if breakfast is allowed on departure date
	private static boolean firstLunch = false;			// Flag to check if lunch is allowed on departure date
	private static boolean firstDinner = false;         // Flag to check if dinner is allowed on departure date
	private static boolean lastBreakfast = false;       // Flag to check if breakfast is allowed on return date
	private static boolean lastLunch = false;           // Flag to check if lunch is allowed on return date
	private static boolean lastDinner = false;          // Flag to check if dinner is allowed on return date
	
	/* Create global objects */
	private static Validation Validate = new Validation();							// For validation methods
	private static PromptTools Prompts = new PromptTools();							// For messaging and prompting methods
	private static FileManager InvoiceFile = new FileManager(INVOICE_FILENAME);		// To manage the temporary data file
	private static FileManager TempFile = new FileManager(TEMP_FILENAME);			// To manage the invoice data file
	private static Date DepartureDate = new Date();									// To store the departure date
	private static Date ReturnDate = new Date();									// To store the return date
	private static CalendarTools CalTools = new CalendarTools();						// To handle date retrieval and calculations
	private static DateFormat DateStyle = new SimpleDateFormat("MM-dd-yyyy HH:mm");	// Set style for date formating
	
	
	
	/**
		SnapperFinalProject main method
	*/
	
	public static void main(String[] args) throws IOException {
	
			// Call method to display welcome information
			showWelcome();
			
			// Call method to get employee name and title
			getEmployeeInfo();
			
			// Call method to get employee's travel dates
			/* This will cede control of the program to object event listeners */
			getTravelDates();
	
	} 		// END main module
 
	/**
		The showWelcome module accepts no arguments 
		and displays welcome information to the user
	*/
	
	public static void showWelcome() {
		// Display program information
		msg = "<h1>TravelExN</h1> <br><br>"
			+ "<strong>Company name:</strong> SpaceTurtles Inc. <br>"
		    + "<strong>Description:</strong> This program was designed to calculate "
			+ "the total expenses incurred on a business trip by a company employee, "
			+ "the total allowable expenses for the trip, the amount to be paid by "
			+ "the employee, and the amount saved if the total expenses were less than "
			+ "the total allowable expenses. <br><br>";

		Prompts.showMessage(msg, "Program information", 1);
		
		// Display programmer information
		msg = "<strong>Designed and written by:</strong> Craig Medlin, Samantha Haiar, and Brandon Maddox" 
			+ "<br>Final project for COP2250<br>"
			+ "<strong>Last modified:</strong> April 30, 2014<br>"
			+ "<strong>Instructor:</strong> Professor Sauls <br>"
			+ "<strong>Institution:</strong> Gulf Coast State College, Panama City, Fla. <br><br>";
		
		Prompts.showMessage(msg, "Developer Information", 1);
		
		// Display program dependencies
		msg = "This program relies on the following data:<br><ul>"
			+ "<li>The Company allows up to <strong>$" + PARKING_RATE + "</strong> per day for parking fees. </li>"
			+ "<li>The Company allows up to <strong>$" + TAXI_RATE + "</strong> per day for taxi usage. </li>"
			+ "<li>The Company allows up to <strong>$" + HOTEL_RATE + "</strong> per night for lodging. </li>"
			+ "<li>The Company will cover breakfast on the first day if departure time is before 7 a.m. </li>"
			+ "<li>The Company will cover lunch on the first day of departure time is before noon. </li>"
			+ "<li>The Company will cover dinner on the first day if departure time is before 6 p.m.</li>"
			+ "<li>The Company will cover breakfast on the last day if departure time is after 8 a.m.</li>"
			+ "<li>The Company will cover lunch on the last day if departure time is after 1 p.m.</li>"
			+ "<li>The Company will cover dinner on the last day if the arrival time is after 7 p.m. </li></ul>";
			
		Prompts.showMessage(msg, "Program dependencies", 1);
		
		// Clear placeholder variable
		msg = "";
	}		// END showWelcome method
	
	public static void getEmployeeInfo() throws IOException {
		// Get employee name and title and set to local variables
		String employeeName = Prompts.getString("Please enter the employee's name who took the trip.");
		String employeeTitle = Prompts.getString("Please enter " + employeeName + "'s title or position.");
		
		// Apply retrieved data to class-global array
		employeeInfo[0] = employeeName;
		employeeInfo[1] = employeeTitle;
		
		// Write employee name and title to temporary data file
		TempFile.newFile(employeeInfo);
		
	}		// END getEmployeeInfo method
	
	public static void getTravelDates() {
		// Declare local variables
		String departurePrompt = "Please select the date and time " + employeeInfo[0] + " departed on the trip.";
		String departureTitle = "Departure information";
		String returnPrompt = "Please select the date and time " + employeeInfo[0] + " began his or her return trip.";
		String returnTitle = "Return information";
		String arrivalPrompt = "Please select the time " + employeeInfo[0] + " arrived home from the trip.";
		String arrivalTitle = "Arrival time";
		
		// Call method to collect dates and times
		CalTools.getDateTime(departurePrompt, departureTitle, returnPrompt, returnTitle, arrivalPrompt, arrivalTitle);
		
	}		// END getTravelDates method
	
	/**
		The setTravelDates method receives processed dates
		and stores the data. It also calls a method that
		determines the duration of the business trip
		by calculating the difference between the two dates.
		
		@param startDate stores the employee's departure date
		@param endDate stores the employee's return date
		@param arrivalHour stores the hour (in 24-hour format) of the day the employee arrived home 
		@param departureHour stores the hour (in 24-hour format) of the day the employee departed on the trip
		@param returnHour stores the hour (in 24-hour format) of the day the employe began their return trip
	*/
	
	public static void setTravelDates(Date startDate, Date endDate, int arrivalHour, int departureHour, int returnHour) throws IOException {
		// Declare local variables
		String tempDepartDate = "";
		String tempReturnDate = "";
		int hours = 0;
		int minutes = 0;
		
		// Store retrieved data
		DepartureDate = startDate;
		ReturnDate = endDate;
		
		// Store retrieved dates as strings
		tempDepartDate = DateStyle.format(DepartureDate);
		tempReturnDate = DateStyle.format(ReturnDate);
		
		// Write trip dates to temporary file
		TempFile.appendFile(tempDepartDate, tempReturnDate);
		
		// Call a method to find the difference between the two dates
		// The data is returned as a long array with the elements representing { days, hours, minutes, seconds, milliseconds }
		long[] duration = CalTools.calculateTimeDifference(DepartureDate, ReturnDate);
		
		// Store trip duration
		daysOfTrip = (int) duration[0];
		hours = (int) duration[1];
		minutes = (int) duration[2];
		
		// Write duration trip lasted to temporary file
		TempFile.appendFile(daysOfTrip, hours, minutes);
		
		/* Increase days of trip by one if hours is greater 
		   than zero to ensure proper data collection 		*/
		if (hours > 0) {
			daysOfTrip += 1;
		}
		
		// Call a method to get the mode of transportation the employee used on the trip
		travelTypes = getTravelType();
		
		// Call a method to set the allowable expenses for the trip
		setAllowableExpenses(arrivalHour, departureHour, returnHour);
		
		// Call a method to get the expenses from the user
		getExpenses();
	}		// END method setTravelDates
	
	/**
		The getTravelType method accepts no arguments
		and prompts the user to enter whether the
		employee used certain types of travel during
		the business trip.
		
		@return A boolean array storing true for yes and false for no in the following order: flight, personal vehicle, taxi, parking fees, car rental.
	*/
	
	public static boolean[] getTravelType() {
		// Declare return variable
		boolean[] travelResponses = new boolean[6];
		
		// Prompt user to enter if the employee took a flight
		msg = "Did " + employeeInfo[0] + " purchase round-trip or one-way airfare for this trip?";
		travelResponses[0] = Prompts.getYesNo(msg);
		
		// Prompt user to enter if the employee used a personal vehicle
		msg = "Did " + employeeInfo[0] + " use a personal vehicle during this trip?";
		travelResponses[1] = Prompts.getYesNo(msg);
		
		// Prompt user to enter if the employee used a taxi
		msg = "Did " + employeeInfo[0] + " pay for a taxi at any point during this trip?";
	    travelResponses[2] = Prompts.getYesNo(msg);
	
		// Prompt user to enter if the employee paid for lodging
		msg = "Did " + employeeInfo[0] + " pay for lodging at any point during this trip?";
	    travelResponses[3] = Prompts.getYesNo(msg);
		
		// Prompt user to enter if the employee paid any parking fees
		msg = "Did " + employeeInfo[0] + " pay for any parking fees during this trip?";
	    travelResponses[4] = Prompts.getYesNo(msg);

		// Prompt user to enter if the employee rented a vehicle
		msg = "Did " + employeeInfo[0] + " rent a vehicle during this trip?";
	    travelResponses[5] = Prompts.getYesNo(msg);
		
		// Return filled out array
		return travelResponses;	
	} // END method getTravelType
	
	/**
		The method setAllowableExpenses calculates and stores
		the total allowable expenses that can be determined
		at this point. These expenses are:
			<ul><li>taxi fares</li>
				<li>hotel fees</li>
				<li>parking fees</li></ul>
		
		These are only set if the user chose that the employee
		paid for these services during the trip.
		
		@param arrivalHour the hour the employee arrived home in 24-hour format
		@param departureHour the hour the employee departed home in 24-hour format
		@param returnHour the hour the employee began the return trip in 24-hour format
	*/
	
	public static void setAllowableExpenses(int arrivalHour, int departureHour, int returnHour) {
		// Declare variables
		float allowableTaxiFees = 0.0F;		// To hold the allowable taxi fees
		float allowableHotelFees = 0.0F;		// To hold the allowable hotel fees
		float allowableParkingFees = 0.0F;	// To hold the allowable parking fees

		
		// Set allowable taxi expenses
		if (travelTypes[2]) {
			allowableTaxiFees = (float)(daysOfTrip * TAXI_RATE);
			allowableExpenses += allowableTaxiFees;
		}
		
		// Set allowable hotel expenses
		/* This process subtracts one from the daysOfTrip variable
		   to avoid allowing a hotel expense on the last day 		*/
		if (travelTypes[3]) {
			allowableHotelFees = (float)((daysOfTrip - 1) * HOTEL_RATE);
			allowableExpenses += allowableHotelFees;
		}
		
		// Set allowable parking expenses
		if (travelTypes[4]) {
			allowableParkingFees = (float)(daysOfTrip * PARKING_RATE);
			allowableExpenses += allowableParkingFees;
		}
		
		// Check if the company will cover the first day meals
		if (departureHour < 7) {
			firstBreakfast = true;
			firstLunch = true;
			firstDinner = true;
			allowableExpenses += BREAKFAST_RATE + LUNCH_RATE + DINNER_RATE;
		} else if (departureHour < 12) {
			firstBreakfast = false;
			firstLunch = true;
			firstDinner = true;
			allowableExpenses += LUNCH_RATE + DINNER_RATE;
		} else if (departureHour < 18) {
			firstBreakfast = false;
			firstLunch = false;
			firstDinner = true;
			allowableExpenses += DINNER_RATE;
		} else {
			firstBreakfast = false;
			firstLunch = false;
			firstDinner = false;
		}
		
		// Check if the company will cover the last day meals
		if (returnHour > 13) {
			lastBreakfast = true;
			lastLunch = true;
			allowableExpenses += BREAKFAST_RATE + LUNCH_RATE;
		} else if (returnHour > 8) {
			lastBreakfast = true;
			lastLunch = false;
			allowableExpenses += BREAKFAST_RATE;
		} else {
			lastBreakfast = false;
			lastLunch = false;
		}
		
		if (arrivalHour > 19) {
			lastDinner = true;
			allowableExpenses += DINNER_RATE;
		} else {
			lastDinner = false;
		}
	}		// END setAllowableExpenses method
	
	/**
		The getExpenses method prompts the user to enter
		the different expenses that were incurred by the
		employee during the business trip.
	*/
	
	public static void getExpenses() throws IOException {
		// Declare local variables
		float expenseInput = 0.0F;						// To receive user input for expenses
		float milesDriven = 0.0F;						// To receive user input for miles driven in personal vehicle
		int index = 0;									// Used as a counter for loops
		boolean moreExpenses = false;					// Flag to track if there's additional expenses to get
		dailyExpenses = new float[daysOfTrip]; 
		
		/* Decision structure to determine what expenses to get */
		
		// Get flight expenses, if any
		if (travelTypes[0]) {
			// Prompt for flight expenses
			msg = "<html><body width='325'>Please enter the total amount " + employeeInfo[0] + " spent on airfare for this trip. "
				+ "Your entry must be in U.S. dollars and cannot have more than two decimal places. "
				+ "Your entry can only include numbers. For example, if the flight was $240.54, you "
				+ "would enter 240.54.";
			expenseInput = Prompts.getReal(msg);
			
			// Call internal method to check for validity and get a new value if necessary
			expenseInput = validExpense(msg, expenseInput);
			
			// Add expense to accrued expenses and allowed expenses
			accruedExpenses += expenseInput;
			allowableExpenses += expenseInput;
			
			// Add to airfare expenses
			airfareExpenses += expenseInput;
		} 	// END if
		
		// Get miles driven in personal vehicle, if any
		if (travelTypes[1]) {
			// Prompt for miles driven in personal vehicle
			msg = "<html><body width='325'>Please enter the number of miles "  + employeeInfo[0] + " drove a personal "
				+ "vehicle during this trip in decimal format. For example, if the number of miles "
				+ "was thirty-two and a half, you would enter 32.5";
			milesDriven = Prompts.getReal(msg);
			
			// Determine reimbursed rate for personal vehicle use and add it to totals
			accruedExpenses += (float)(milesDriven * MILEAGE_RATE);
			allowableExpenses += (float)(milesDriven * MILEAGE_RATE);
			
			// Add to personal vehicle expenses
			drivingExpenses += (float)(milesDriven * MILEAGE_RATE);
			
		}	// END if
		
		// Get amount spent on taxi use each day, if any
		if (travelTypes[2]) {
			// Loop to get taxi amounts for each day
			for (index = 0; index < daysOfTrip; index++) {
				msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on taxi usage on day "
						+ (index + 1) + " of the trip. The amount must be in U.S. dollars and cannot contain "
						+ "symbols or more than two decimal places. For example, if the amount spent was "
						+ "eleven dollars and twenty-four cents, you would enter 11.24. If no taxi service "
						+ "was used on this day, please enter 0.";
					expenseInput = Prompts.getReal(msg);
					
					// Call internal function to check for validity of input
					expenseInput = validExpense(msg, expenseInput);
					
					// Add amount to daily and total expenses
					dailyExpenses[index] += expenseInput;
					accruedExpenses += expenseInput;
					
					// Add to taxi expenses
					taxiExpenses += expenseInput;
					
			}	// END for
		}	// END if
		
		// Get amount spent on lodging each day, if any
		if (travelTypes[3]) {
			// Loop to get lodging amounts for each day
			for (index = 0; index < (daysOfTrip - 1); index++) {
				// Prompt user to enter amount spent on hotel each day
				msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on lodging on night "
					+ (index +1) + " of the trip. The amount must be in U.S. dollars and cannot contain "
					+ "symbols or more than two decimal places. For example, if the amount spent "
					+ "was three hundred dollars and twenty cents, you would enter 300.20. If no "
					+ "hotel service was used on this day, please enter 0.";
				expenseInput = Prompts.getReal(msg);

				// Call internal function to check for validity of input
				expenseInput = validExpense(msg, expenseInput);				
				
				// Add amount to daily and total expenses
				dailyExpenses[index] += expenseInput;
				accruedExpenses += expenseInput;
				
				// Add to lodging expenses
				lodgingExpenses += expenseInput;
				
			} 	// END for
		}	// END if
		
		// Get amount spent on car rental, if any
		if (travelTypes[5]) {
			// Prompt user to enter amount spent on car rental
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on motor vehicle "
				+ "rental during this trip. The amount must be in U.S. dollars and cannot contain "
					+ "symbols or more than two decimal places. For example, if the amount spent "
					+ "was three hundred dollars and twenty cents, you would enter 300.20. If no "
					+ "expense was used, please enter 0.";
			expenseInput = Prompts.getReal(msg);
			
			// Call internal function to check for validity
			expenseInput = validExpense(msg, expenseInput);
			
			// Add amount to total and allowable expenses
			accruedExpenses += expenseInput;
			allowableExpenses += expenseInput;
			
			// Add amount to car rental total
			carRentalExpenses += expenseInput;
		}
		
		
		// Get amounts spent on food the first day of the trip, if allowed
		if (firstBreakfast) {
			// Prompt the user to enter the amount spent on the first breakfast
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on breakfast the first day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[0] += expenseInput;
			accruedExpenses += expenseInput;

		}	// END if
		
		if (firstLunch) {
			// Prompt the user to enter the amount spent on the first lunch
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on lunch the first day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[0] += expenseInput;
			accruedExpenses += expenseInput;

		}	// END if		

		if (firstDinner) {
			// Prompt the user to enter the amount spent on the first dinner
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on dinner the first day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[0] += expenseInput;
			accruedExpenses += expenseInput;
			
		}	// END if	

		// Get amounts spent on food during the trip
		if (daysOfTrip > 2) {
			for (index = 1; index < daysOfTrip - 2; index++) {
				// Prompt the user to enter the amount spent on breakfast
				msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on breakfast on day "
					+ (index + 1) + " of the trip. The amount must be in U.S. dollars and cannot contain "
					+ "symbols or more than two decimal places. For example, if the amount spent "
					+ "was eight dollars and twenty cents, you would enter 8.20. If no "
					+ "breakfast was purchased on this day, please enter 0.";
				expenseInput = Prompts.getReal(msg);

				// Call internal function to check for validity of input
				expenseInput = validExpense(msg, expenseInput);				
				
				// Add amount to daily and total expenses
				dailyExpenses[index] += expenseInput;
				accruedExpenses += expenseInput;	

				// Prompt the user to enter the amount spent on lunch
				msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on lunch on day "
					+ (index + 1) + " of the trip. The amount must be in U.S. dollars and cannot contain "
					+ "symbols or more than two decimal places. For example, if the amount spent "
					+ "was eight dollars and twenty cents, you would enter 8.20. If no "
					+ "breakfast was purchased on this day, please enter 0.";
				expenseInput = Prompts.getReal(msg);

				// Call internal function to check for validity of input
				expenseInput = validExpense(msg, expenseInput);				
				
				// Add amount to daily and total expenses
				dailyExpenses[index] += expenseInput;
				accruedExpenses += expenseInput;	

				// Prompt the user to enter the amount spent on dinner
				msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on dinner on day "
					+ (index + 1) + " of the trip. The amount must be in U.S. dollars and cannot contain "
					+ "symbols or more than two decimal places. For example, if the amount spent "
					+ "was eight dollars and twenty cents, you would enter 8.20. If no "
					+ "breakfast was purchased on this day, please enter 0.";
				expenseInput = Prompts.getReal(msg);

				// Call internal function to check for validity of input
				expenseInput = validExpense(msg, expenseInput);				
				
				// Add amount to daily and total expenses
				dailyExpenses[index] += expenseInput;
				accruedExpenses += expenseInput;

			}	// END for
		}	// END if

		// Get amounts spent on food the last day of the trip, if allowed
		if (lastBreakfast) {
			// Prompt the user to enter the amount spent on the last breakfast
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on breakfast the last day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[(daysOfTrip-1)] += expenseInput;
			accruedExpenses += expenseInput;
			
			// Decision structure to see if employee owes any of this
			if (expenseInput > BREAKFAST_RATE) {
				outOfPocketExpenses += expenseInput - BREAKFAST_RATE;
			}	// END if
		}	// END if
		
		if (lastLunch) {
			// Prompt the user to enter the amount spent on the last lunch
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on lunch the last day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[(daysOfTrip-1)] += expenseInput;
			accruedExpenses += expenseInput;
			
			// Decision structure to see if employee owes any of this
			if (expenseInput > LUNCH_RATE) {
				outOfPocketExpenses += expenseInput - LUNCH_RATE;
			}	// END if
		}	// END if		

		if (lastDinner) {
			// Prompt the user to enter the amount spent on the last dinner
			msg = "<html><body width='325'>Please enter the amount " + employeeInfo[0] + " spent on dinner the last day "
				+ "of the trip. The amount must be in U.S. dollars and cannot contain "
				+ "symbols or more than two decimal places. For example, if the amount spent "
				+ "was eight dollars and twenty cents, you would enter 8.20. If no "
				+ "breakfast was purchased on this day, please enter 0.</body></html>";
			expenseInput = Prompts.getReal(msg);

			// Call internal function to check for validity of input
			expenseInput = validExpense(msg, expenseInput);				
			
			// Add amount to daily and total expenses
			dailyExpenses[(daysOfTrip-1)] += expenseInput;
			accruedExpenses += expenseInput;
			
			// Decision structure to see if employee owes any of this
			if (expenseInput > DINNER_RATE) {
				outOfPocketExpenses += expenseInput - DINNER_RATE;
			}	// END if
		}	// END if	
		
		// Get any additional expenses
		msg = "<html><body width='325'>Are there any other expenses that " + employeeInfo[0] + " incurred during "
				+ "the business trip? This could include conference registration fees, tolls, "
				+ "or other fees Space Turtles Inc. would consider essential to the trip.";
		moreExpenses = Prompts.getYesNo(msg);
		
		while (moreExpenses) {
			msg = "<html><body width='325'>Please enter the amount of the additional expense. You can enter all additional "
				+ "expenses as one total, or you can enter them individually.";
			expenseInput = Prompts.getReal(msg);
			
			// Check validity
			expenseInput = validExpense(msg, expenseInput);
			
			// Add amount to accrued and allowed expenses
			accruedExpenses += expenseInput;
			allowableExpenses += expenseInput;
			
			// Add to other expenses
			otherExpenses += expenseInput;
			
			
			// Check for more expenses
			msg = "<html><body width='325'>Are there any other expenses that " + employeeInfo[0] + " incurred during "
				+ "the business trip? This could include conference registration fees, tolls, "
				+ "or other fees Space Turtles Inc. would consider essential to the trip.";
			moreExpenses = Prompts.getYesNo(msg);
		} // END while
		
		outOfPocketExpenses = allowableExpenses - accruedExpenses;
		
		// Write the calculated results to the file
		writeInvoice();
		
		// Show a menu of options for the user
		displayInvoice();
		
	}	// END getExpenses method

	// Begin internal function to check for expense input validity
	private static float validExpense(String prompt, float expense) {
		// Loop to check for validity and reprompt if necessary
		while (!Validate.isMoney(expense)) {
			Prompts.showErrorMessage("<html><body width='325'>Error! Your entry was not a valid input. Please check "
								   + "your entry and try again.");
			expense = Prompts.getReal(prompt);
		}	// END while
		
		// Return new entry if original was invalid or original if it passed the test
		return expense;
		
	} // END validExpense internal method
	
	/**
		This method writes the calculated data to an invoice
	*/
	
	public static void writeInvoice() throws IOException {
		// loop counter
		int index = 0;
		
		// Helper string
		String helper;
		
		InvoiceFile.newFile("Space Turtles Inc.");
		InvoiceFile.appendFile("Business Trip Invoice, powered by TravelEx.");
		InvoiceFile.appendFile("--------------------------------------------");
		InvoiceFile.appendFile(" ");
		InvoiceFile.appendFile("Employee: " + TempFile.readFile());
		InvoiceFile.appendFile("Position: " + TempFile.readFile());
		InvoiceFile.appendFile("Departure date and time: " + TempFile.readFile());
		InvoiceFile.appendFile("Return date and time: " + TempFile.readFile());
		InvoiceFile.appendFile("Trip duration: " + TempFile.readFile() + " days, " + TempFile.readFile() + " hours, " + TempFile.readFile() + " minutes.");
		InvoiceFile.appendFile("--------------------------------------------");
		InvoiceFile.appendFile(" ");
		InvoiceFile.appendFile("Daily breakdown:");
		
		// For loop to fill in daily expenses
		for (index = 0; index < daysOfTrip; index++) {
			helper = String.format("%.2f", dailyExpenses[index]);
			InvoiceFile.appendFile("Day " + (index + 1) + ": $" + helper);
		}
		
		// Print additional amounts, if any
		InvoiceFile.appendFile(" ");
		
		if (airfareExpenses > 0) {
			InvoiceFile.appendFile("Airfare expenses: $" + airfareExpenses);
		}
		
		if (taxiExpenses > 0) {
			InvoiceFile.appendFile("Taxi expenses: $" + taxiExpenses);
		}
		
		if (lodgingExpenses > 0) {
			InvoiceFile.appendFile("Lodging expenses: $" + lodgingExpenses);
		}
		
		if (drivingExpenses > 0) {
			InvoiceFile.appendFile("Personal vehicle expenses: $" + drivingExpenses);
		}
		
		if (carRentalExpenses > 0) {
			InvoiceFile.appendFile("Car rental expenses: $" + carRentalExpenses);
		}
		
		if (otherExpenses > 0) {
			InvoiceFile.appendFile("Other expenses: $" + otherExpenses);
		}
		
		InvoiceFile.appendFile(" ");
		InvoiceFile.appendFile("--------------------------------------------");
		helper = String.format("%.2f", accruedExpenses);
		InvoiceFile.appendFile("Total expenses: $" + helper);
		InvoiceFile.appendFile("Allowable expenses: $" + String.format("%.2f", allowableExpenses));
		
		// Display amount owed or saved, depending on calculation
		if (outOfPocketExpenses < 0) {
			helper = String.format("%.2f", outOfPocketExpenses);
			InvoiceFile.appendFile("Amount owed by employee: $" + helper); 
		} else {
			helper = String.format("%.2f", outOfPocketExpenses);
			InvoiceFile.appendFile("Amount saved by employee: $" + helper);
		}
		
		InvoiceFile.appendFile("--------------------------------------------");
		InvoiceFile.appendFile("--------------------------------------------");
		
		// Display disclaimer
		InvoiceFile.appendFile("Reimbursement subject to manager approval.");
		InvoiceFile.appendFile("Accuracy of calculations not guaranteed.");
	}
	
	/**
		This method displays the results of the calculations to the console
	*/
	
	public static void displayInvoice() throws FileNotFoundException {
		while (InvoiceFile.hasNextLine()) {
			System.out.println(InvoiceFile.readFile());
		} // END while
		
		JOptionPane.showMessageDialog(null, "Please see the console for your invoice details. "
							  + "There also was a file created with the name "
							  + INVOICE_FILENAME + " in the main directory where "
							  + "this program is stored.", "Invoice complete", JOptionPane.INFORMATION_MESSAGE);
							  
		System.exit(0);
	}
 
 } 		// END SnapperFinalProject class