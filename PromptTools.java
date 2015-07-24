/*
 * This class is designed to aid in the creation
 * of messages and message prompts. 
 *
 */
 
 // Import dependencies
 import javax.swing.*;
 import java.math.BigDecimal;
 import java.lang.NumberFormatException;
 
 public class PromptTools {
 
	 // Create necessary objects
	private Validation Validate = new Validation();
	
	/**
		This method accepts a String as an 
		argument and displays it as a message
		
		@param message is a string that this method uses to display in a JOptionPane dialog
	*/
	public void showMessages(String message) {
		JOptionPane.showMessageDialog(null, message);		
	} // END showMessages method
	
	
	public void showMessage(final String message, final String title, int boxType) {
		final int width = 325;								// Set width of box
		final int messageType = getMessageType(boxType);	// Get message type
		
		String openTags = "<html><body width='" + width + "' style='font:11px arial, sans-serif'>";
		String closeTags = "</body></html>";
								
		String completeMessage = openTags + message + closeTags;
				
		JOptionPane.showMessageDialog(null, completeMessage, title, messageType);

	}
	
	/**
		This method accepts an integer and returns 
		a constant value for a specific message type
	*/
	
	public int getMessageType(int boxType) {
		int messageType = 0; 	// For return statement
		
		// Switch to set message type based on argument
		switch (boxType) {
			case 0:
				messageType = JOptionPane.ERROR_MESSAGE;
				break;
			case 1:
				messageType = JOptionPane.INFORMATION_MESSAGE;
				break;
			case 2:
				messageType = JOptionPane.WARNING_MESSAGE;
				break;
			case 3:
				messageType = JOptionPane.QUESTION_MESSAGE;
				break;
			default:
				messageType = JOptionPane.PLAIN_MESSAGE;
				break;
		}
		return messageType;
	}
	
	/**
		This method accepts a String as an 
		argument and displays it as an error message
		
		@param message is a string that this method uses to display in a JOptionPane dialog
	*/
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "ERROR!", JOptionPane.ERROR_MESSAGE);		
	} // END showMessages method		
	
	/**
		This method accepts a String argument that
		it uses to prompt the user for a "Yes" or "No"
		decision with a JOption confirm dialog.
		
		@param prompt is a String used to display as a prompt
		@return boolean indicating if the user chose yes or no
	*/
	public boolean getYesNo(String prompt) {
		// Declare variables
		boolean yesOrNo = false;		// Return variable
		int holder = 0;					// Holder for JOption selection
		boolean validResponse = false; 	// Flag for valid response
		
		do {
			// Receive input
			holder = JOptionPane.showConfirmDialog(null, prompt, "Please make a selection", JOptionPane.YES_NO_OPTION);
		
			switch (holder) {
			case JOptionPane.YES_OPTION:
				yesOrNo = true;
				validResponse = true;
				break;
			case JOptionPane.NO_OPTION:
				yesOrNo = false;
				validResponse = true;
				break;
			case JOptionPane.CLOSED_OPTION:
				JOptionPane.showMessageDialog(null, "You closed the window! You must make a selection to continue.", "ERROR!", JOptionPane.WARNING_MESSAGE);
				break;
			default:
				break;
			}
			
		} while (!validResponse);
		
		return yesOrNo;	
	} // END getYesNo method
	
	
	/**
		This method prompts the user to enter an integer
		and then validates the input before returning 
		it to the calling object.
		
		@param prompt is a String the method uses to prompt for an input
		@return is an Integer value derived from the user input
	*/
	public int getInteger(String prompt) {
		// Declare variables
		String userEntry = "";
					
		// Receive input
		userEntry = JOptionPane.showInputDialog(prompt);
			
		// Verify entry
		userEntry = verifyInput(userEntry, prompt);
		
		// Validation loop
		while (!Validate.isInteger(userEntry))
		{
			// Display error message
			JOptionPane.showMessageDialog(null, "The input you provided was not valid. " +
											    "Please check your entry and try again.");
	
			// Receive input
			userEntry = JOptionPane.showInputDialog(prompt);
		}
		
		// Parse as integer
		return Integer.parseInt(userEntry);	
	} // END getInteger method

	/**
		This method prompts the user to enter a Real value.
		
		@param prompt is a String used to ask the user for an input
		@return float 
	*/	
	public float getReal(String prompt) {
		// Declare variables
		String userEntry = "";
		
		// Receive input
		userEntry = JOptionPane.showInputDialog(prompt);
		
		// Verify entry
		userEntry = verifyInput(userEntry, prompt);
		
		boolean tryAgain = false;
		
		do {
			try {
				return Float.parseFloat(userEntry + "f");
			} catch(NumberFormatException nfe) {
				showErrorMessage("Your entry was invalid! <br> Please check your entry and try again.");
				tryAgain = true;
			}
		} while (tryAgain);
		
		return Float.parseFloat(userEntry + "f");
		
	} // END getReal method

	/**
		This method accepts a String argument as a prompt
		and uses it to retrieve a new String from the user.
		
		@param prompt is used to ask the user for an input
		@return string that the user entered
	*/	
	public String getString(String prompt) {
		// Declare variables
		String userEntry = "";
		
		// Prompt user
		userEntry = JOptionPane.showInputDialog(prompt);
		
		// Verify entry
		userEntry = verifyInput(userEntry, prompt);
		
		return userEntry;	
	} // END getString method
 
 	/**
		This method takes any user input as a string and 
		verifies the user entered what they meant to.
		
		@param userInput is a String that needs validated
		@param prompt is a String used to retrieve the userInput
		
		@return sends back the original String if it was valid or a new String if not
	*/
	private String verifyInput(String userInput, String prompt) {
	
		// Declare variables
		boolean isCorrect = false;
		boolean firstTime = true;
		
		// loop for input verification
		do
		{
			if (!firstTime)
			{
				userInput = JOptionPane.showInputDialog(prompt);
			}
			
			// prompt for verification
			isCorrect = getYesNo("You entered " + userInput + ". Is this correct?");
			
			// Clear firstTime flag
			firstTime = false;
		}	
		while (!isCorrect);
		
		return userInput;
	
	} // END verifyInput method
 
 }	// END class PromptTools