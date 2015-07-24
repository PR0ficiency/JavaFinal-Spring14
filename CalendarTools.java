/*
 * This class handles calendar operations including retrieving
 * dates and times from a user and performing calculations 
 * on dates (mainly the difference between two dates)
 */
 
 // Import dependencies
 import java.awt.*;
 import java.awt.event.*;
 import javax.swing.*;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.TimeZone;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.io.*;

 public class CalendarTools extends JFrame {
 
	/* Declare variables */
 	private JPanel labelPanel;		// To hold the prompt
	private JPanel datesPanel;		// To hold components
	private JPanel timesPanel;		// To hold components
	private JPanel buttonPanel;		// To hold the button
	private JButton mainButton; 	// The main button
	private JComboBox<String> monthsBox; 	// A list of months
	private JComboBox<String> daysBox;		// A list of dates
	private JComboBox<String> yearsBox;		// A list of years
	private JComboBox<String> hoursBox;		// A list of hours
	private JComboBox<String> minutesBox;	// A list of minutes
	private JComboBox<String> amPmBox;		// A list of a.m. and p.m.
	private JLabel promptLabel;		// Displays a prompt to the user
 
	private String[] months = { "January",
								"February",
								"March",
								"April",
								"May",
								"June",
								"July",
								"August",
								"September",
								"October",
								"November",
								"December" };
								
	private String[] days = { "1", "2", "3", "4", "5", "6", "7" , "8", "9", "10",
					  	      "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
						      "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	
	private String[] years = new String[50];	// years to be filled with loop
	
	private String[] hours = { "1", "2", "3", "4", "5", "6", "7" , "8", "9", "10", "11", "12" };
	
	private String[] minutes = new String[60];	// Minutes to be filled with loop
	
	private String[] amPM = { "a.m.", "p.m." };	
	
	private int[] selectionIndex = new int[6];
	private int[] departureSelectionIndex = new int[6];
	private int[] returnSelectionIndex = new int[6];
	private int[] arrivalSelectionIndex = new int[3];
	
	private String departureYear = "";
	private String departureMonth = "";
	private String departureDay = "";
	private String departureHour = "";
	private String departureMinutes = "";
	private String departureAMpm = "";
	 
	private String returnYear = "";
	private String returnMonth = "";
	private String returnDay = "";
	private String returnHour = "";
	private String returnMinutes = "";
	private String returnAMpm = "";
	
	private String arrivalHour = "";
	private String arrivalMinutes = "";
	private String arrivalAMpm = "";	
	
	private String arrivalPrompt;
	private String arrivalTitle;
	private String returnPrompt;
	private String returnTitle;
	private String departurePrompt;
	
	private boolean firstFlag = true;		// To check if first entry
	private boolean secondFlag = false;		// To check if second entry
	
	private static PromptTools IOtools = new PromptTools();
	
 
	/**
		Constructor
		No parameters accepted, performs basic initialization of object
	*/
	public CalendarTools() {
		// Variable for loop fill
		int year = 1965;
		int index = 0;
		
		// For loop to fill years array
		for (index = 0; index < years.length; index++) {
			years[index] = String.valueOf(year++);
		}
		
		// For loop to fill out minutes array
		for (index = 0; index < minutes.length; index++) {
			minutes[index] = String.valueOf(index+1);
		}
		
		// Specify an action for the close button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a BorderLayout manager
		setLayout(new BorderLayout());
		
	} // END constructor
	
	/**
		The getDateTime method uses provided prompts to get two full dates and times and one additional time.
		The first date will be the departure date for a trip, and the second the return date. The additional
		time is the time the person arrived home from the trip.
		
		@param departurePrompt is a String used to prompt the user for the departure information
		@param departureTitle is a String used to label the title of the window.
		@param returnPrompt is a String used to prompt the user for the return information
		@param returnTitle is a String used to label the title of the window
		@param arrivalPrompt is a String used to prompt the user for the arrival information
		@param arrivalTitle is a String used to label the title of the window
	*/
	public void getDateTime(String departurePrompt, String departureTitle, String returnPrompt, String returnTitle, String arrivalPrompt, String arrivalTitle) {
	
		// Store passed data locally
		this.returnPrompt = returnPrompt;
		this.returnTitle = returnTitle;
		this.arrivalPrompt = arrivalPrompt;
		this.arrivalTitle = arrivalTitle;
		this.departurePrompt = departurePrompt;
	
		// Set the title
		setTitle(departureTitle);
		
		// Build the panels
		buildLabelPanel(departurePrompt);
		buildDatesPanel();
		buildTimesPanel();
		buildButtonPanel();
		
		// Add the panels to the content pane
		add(labelPanel, BorderLayout.NORTH);
		add(datesPanel, BorderLayout.WEST);
		add(timesPanel, BorderLayout.EAST);
		add(buttonPanel, BorderLayout.SOUTH);
		
		// Pack and display the window
		pack();
		setLocationRelativeTo(null);	// To display in center of screen
		setVisible(true);
	
	} // END getDateTime method
	
	/**
		This method modifies an already built JFrame to accept
		only a time input.
	*/
	public void getTime() {
		remove(datesPanel);
		remove(timesPanel);
		add(timesPanel, BorderLayout.CENTER);
		promptLabel.setText(arrivalPrompt);
		setTitle(arrivalTitle);
		
		pack();
		setLocationRelativeTo(null);
	    setVisible(true);
		
		
	} // END getTime method
	
	/**
		This method calculates the time difference between
		two Date objects. This is used to find the duration
		of a trip.
		
		Source of getTimeDifference method: technojeeves.com
		
		@param d1 is the starting date
		@param d2 is the ending date
		
		@return is a long array storing the { days, hours, minutes, seconds, milliseconds } between the two dates
	*/	
	public static long[] calculateTimeDifference(Date d1, Date d2) {
        long[] result = new long[5];
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(d1);

        long t1 = cal.getTimeInMillis();
        cal.setTime(d2);

        long diff = cal.getTimeInMillis() - t1;
        final int ONE_DAY = 1000 * 60 * 60 * 24;
        final int ONE_HOUR = ONE_DAY / 24;
        final int ONE_MINUTE = ONE_HOUR / 60;
        final int ONE_SECOND = ONE_MINUTE / 60;

        long d = diff / ONE_DAY;
        diff %= ONE_DAY;

        long h = diff / ONE_HOUR;
        diff %= ONE_HOUR;

        long m = diff / ONE_MINUTE;
        diff %= ONE_MINUTE;

        long s = diff / ONE_SECOND;
        long ms = diff % ONE_SECOND;
        result[0] = d;
        result[1] = h;
        result[2] = m;
        result[3] = s;
        result[4] = ms;

        return result;	
	} // END calculateTimeDifference method
	
	/* PRIVATE */
	
	/**
		The buildLabelPanel builds a label using a provided
		string argument as the prompt.
	*/
	private void buildLabelPanel(String prompt) {
		
		// Create a new panel
		labelPanel = new JPanel();
		
		// Create a new label
		promptLabel = new JLabel(prompt);
		
		// Add the label to the panel
		labelPanel.add(promptLabel);
		
	}
	
	/**
		The buildDatesPanel method adds combo boxes with various
		date selectors to a panel.
	*/
	
	private void buildDatesPanel() {
		// Create a panel to hold the boxes
		datesPanel = new JPanel();

		// Create the months combo box
		monthsBox = new JComboBox<>(months);
		
		// Create the days combo box
		daysBox = new JComboBox<>(days);
		
		// Create the years combo box
		yearsBox = new JComboBox<>(years);
		
		// Add the boxes to the panel
		datesPanel.add(monthsBox);
		datesPanel.add(daysBox);
		datesPanel.add(yearsBox);
		
	}
	
	/**
		The buildTimesPanel method adds combo boxes with
		selectable times to a panel.
	*/
	
	private void buildTimesPanel() {
		// Create a panel for the boxes
		timesPanel = new JPanel();
		
		// Create the hours combo box
		hoursBox = new JComboBox<>(hours);
		
		// Create the minutes combo box
		minutesBox = new JComboBox<>(minutes);
		
		// Create the a.m./p.m. combo box
		amPmBox = new JComboBox<>(amPM);
		
		// Add the boxes to the panel
		timesPanel.add(hoursBox);
		timesPanel.add(minutesBox);
		timesPanel.add(amPmBox);
	}
	
	/**
		The buildButtonPanel method adds a 
		clickable confirmation button to a
		panel.
	*/
	
	private void buildButtonPanel() {
		
		// Create the panel
		buttonPanel = new JPanel();
		
		// Create the button
		mainButton = new JButton("Confirm");
		
		// Register an event listener
		mainButton.addActionListener(new ButtonListener());
		
		// Add the button to the panel
		buttonPanel.add(mainButton);
	}	
	
	/**
		Private inner class that handles the event when the user clicks the button.
	*/
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (firstFlag || secondFlag) {
				// Collect the data from the boxes
				selectionIndex[0] = monthsBox.getSelectedIndex();
				selectionIndex[1] = daysBox.getSelectedIndex();
				selectionIndex[2] = yearsBox.getSelectedIndex();
				selectionIndex[3] = hoursBox.getSelectedIndex();
				selectionIndex[4] = minutesBox.getSelectedIndex();
				selectionIndex[5] = amPmBox.getSelectedIndex();
			}
			else {
				selectionIndex[3] = hoursBox.getSelectedIndex();
				selectionIndex[4] = minutesBox.getSelectedIndex();
				selectionIndex[5] = amPmBox.getSelectedIndex();
			}
			
			int index = 0;
			
			if (firstFlag) {
				for (index = 0; index < 6; index++) {
					departureSelectionIndex[index] = selectionIndex[index];
				}
				firstFlag = false;
				secondFlag = true;
				promptLabel.setText(returnPrompt);
				setTitle(returnTitle);
				pack();
			}
			else if (secondFlag) {
				for (index = 0; index < 6; index++) {
					returnSelectionIndex[index] = selectionIndex[index];
				}
				secondFlag = false;
				setVisible(false);
				promptLabel.setText(arrivalPrompt);
				setTitle(arrivalTitle);
				getTime();
			}
			else {
				for (index = 0; index < 3; index++) {
					arrivalSelectionIndex[index] = selectionIndex[(index+3)];
				}
				setVisible(false);
				try {
				verifyDateTime();
				} catch(IOException ex) {
				}
			}
		} // END actionPerformed method
	} // END ButtonListener inner class	

	/**
		This method prompts the user to verify
		they entered the correct date and time
	*/
	private void verifyDateTime() throws IOException {
		boolean correctDates = false;

		// Set values retrieved
		departureMonth = months[departureSelectionIndex[0]];
		departureDay = days[departureSelectionIndex[1]];
		departureYear = years[departureSelectionIndex[2]];
		departureHour = hours[departureSelectionIndex[3]];
		departureMinutes = minutes[departureSelectionIndex[4]];
		departureAMpm = amPM[departureSelectionIndex[5]];
		
		returnMonth = months[returnSelectionIndex[0]];
		returnDay = days[returnSelectionIndex[1]];
		returnYear = years[returnSelectionIndex[2]];
		returnHour = hours[returnSelectionIndex[3]];
		returnMinutes = minutes[returnSelectionIndex[4]];
		returnAMpm = amPM[returnSelectionIndex[5]];
		
		arrivalHour = hours[arrivalSelectionIndex[0]];
		arrivalMinutes = minutes[arrivalSelectionIndex[1]];
		arrivalAMpm = amPM[arrivalSelectionIndex[2]];
		
		
		correctDates = IOtools.getYesNo("You entered a departure date of " + departureMonth + " " + departureDay + ", " + departureYear
									  + " at " + departureHour + ":" + departureMinutes + " " + departureAMpm + " and a return date of " 
									  + returnMonth + " " + returnDay + ", " + returnYear + " at " + returnHour + ":" + returnMinutes
									  + " " + returnAMpm + " with a home arrival time of " + arrivalHour + ":" + arrivalMinutes + " "
									  + arrivalAMpm + " Is this correct?");
		
		if (!correctDates) {
			firstFlag = true;
			promptLabel.setText(departurePrompt);
			pack();			
			setVisible(true);
		}
		else {
			createDates();
		}
	
	} // END verifyDateTime method	
	
	
	private void createDates() throws IOException {
		// Convert selection to date objects
		String startDateString = null;
		String endDateString = null;
		int intArrivalHour = 0;
		int intDepartureHour = 0;
		int intReturnHour = 0;
		int index = 0;
		
		DateFormat date = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		
		Date startDate = null;
		Date endDate = null;
		
		// Set month for date strings
		if (departureSelectionIndex[0] < 9) {
			startDateString = "0" + String.valueOf(departureSelectionIndex[0] + 1);
		}
		else {
			startDateString = String.valueOf(departureSelectionIndex[0] + 1);
		}
		
		if (returnSelectionIndex[0] < 9) {
			endDateString = "0" + String.valueOf(returnSelectionIndex[0] + 1);
		}
		else {
			endDateString = String.valueOf(returnSelectionIndex[0] + 1);
		}
		
		// Set day for date strings
		if (departureDay.length() == 1) {
			startDateString += "-0" + String.valueOf(departureDay);
		}
		else {
			startDateString += "-" + String.valueOf(departureDay);
		}
		
		if (returnDay.length() == 1) {
			endDateString += "-0" + String.valueOf(returnDay);
		}
		else {
			endDateString += "-" + String.valueOf(returnDay);
		}
		
		// Set year for date strings
		startDateString += "-" + departureYear;
		endDateString += "-" + returnYear;
		
		// Set hours for date strings
		if (departureSelectionIndex[5] == 1) {
			if (departureHour == "12") {
				startDateString += " " + departureHour;
			}
			else {
				startDateString += " " + String.valueOf(departureSelectionIndex[3] + 13);
			}
		}
		else {
			if (departureHour.length() == 1) {
				startDateString += " 0" + departureHour;
			}
			else {
				startDateString += " " + departureHour;
			}
		}
		
		if (returnSelectionIndex[5] == 1) {
			if (returnHour == "12") {
				endDateString += " " + returnHour;
			}
			else {
				endDateString += " " + String.valueOf(returnSelectionIndex[3] + 13);
			}
		}
		else {
			if (returnHour.length() == 1) {
				endDateString += " 0" + returnHour;
			}
			else {
				endDateString += " " + returnHour;
			}
		}
		
		// Set minutes for date string
		startDateString += ":" + departureMinutes;
		endDateString += ":" + returnMinutes;
		
		try {
		startDate = date.parse(startDateString);
		endDate = date.parse(endDateString);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		// Store arrivalHour as 24-hour time
		for (index = 0; index < 13; index++) {
			if (arrivalAMpm == amPM[1] && index == arrivalSelectionIndex[0]) {
				intArrivalHour = index + 12;
			}
			else if (index == arrivalSelectionIndex[0]) {
				intArrivalHour = index;
			}
		}
		
		// Store departureHour as 24-hour time
		for (index = 0; index < 13; index++) {
			if (departureAMpm == amPM[1] && index == departureSelectionIndex[0]) {
				intDepartureHour = index + 12;
			}
			else if (index == departureSelectionIndex[0]) {
				intDepartureHour = index;
			}
		}		
		
		// Store returnHour as 24-hour time
		for (index = 0; index < 13; index++) {
			if (returnAMpm == amPM[1] && index == returnSelectionIndex[0]) {
				intReturnHour = index + 12;
			}
			else if (index == returnSelectionIndex[0]) {
				intReturnHour = index;
			}
		}		

		// setTravelDates
		SnapperFinalProject.setTravelDates(startDate, endDate, intArrivalHour, intDepartureHour, intReturnHour);
		
	} // END createDates method
 
 } // END class CalendarTools