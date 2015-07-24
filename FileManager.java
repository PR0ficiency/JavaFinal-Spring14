/*
 *	This class assists with the handling of files.
 */
 
 // Import dependencies
 import java.io.*;
 import java.util.Scanner;
 
 /* Begin class */
 
 public class FileManager {
	/* Declare class-global variables */
	String filename = null;
	int readLines = 0;
	
	/* Create class-global objects */
	private PromptTools Prompts = new PromptTools();
	PrintWriter outputFile;
 
	/**
		Constructor
		@param filename is a String that is used as a filename
	*/
	public FileManager(String filename) {
		this.filename = filename;
	
	} // END constructor
	
	/**
		This method accepts a String argument and writes it to an existing file
	*/
	public void appendFile(String stringToSave) throws IOException {
		// Open file to append
		FileWriter fwriter = new FileWriter(filename, true);
		outputFile = new PrintWriter(fwriter);
		
		outputFile.println(stringToSave);
		
		outputFile.close();
	} // END appendFile method
	
	public void appendFile(int value1, int value2, int value3) throws IOException {
		// Append to file
		appendFile(Integer.toString(value1));
		appendFile(Integer.toString(value2));
		appendFile(Integer.toString(value3));
	}
	
	public void appendFile(String string1, String string2) throws IOException {
		// Append to file
		appendFile(string1);
		appendFile(string2);
	}
	
	/**
		The newFile method accepts a String and creates a new file that 
		begins with the passed String argument
	*/
	public void newFile(String stringToSave) throws FileNotFoundException {
		outputFile = new PrintWriter(filename);
		
		// Write the line
		outputFile.println(stringToSave);		
		
		// Close the file
		outputFile.close();
	}
	
	
	/**
		This method accepts a String[] array as a parameter and
		creates a new file, writing the data from the array.
	*/
	public void newFile(String[] arrayOfStrings) throws IOException, FileNotFoundException {
		// Declare variables
		int index = 0; 		// To count loop
		
		newFile(arrayOfStrings[0]);
		
		for (index = 1; index < arrayOfStrings.length; index++) {
			appendFile(arrayOfStrings[index]);
		}
	} // END newFile method
	
	/**
		
	*/
	public String readFile() throws FileNotFoundException {
		// Declare return variable
		String lineFromFile = null;
		
		// Declare loop counter
		int index = 0;
		
		// Open input file
		File file = new File(filename);
		Scanner InputFile = new Scanner(file);
		
		// Read from file if there's more data
		if (InputFile.hasNextLine()) {
			// Loop to skip any previously read lines
			for (index = 0; index < readLines; index++) {
				InputFile.nextLine();
			}
			lineFromFile = InputFile.nextLine();
		}
		else {
			Prompts.showErrorMessage("Fatal Error!");
		}
	
		// Iterate readLines variable
		readLines++;
		InputFile.close();
		return lineFromFile;
	}	
	
	/**
	
	*/
	public boolean hasNextLine() throws FileNotFoundException {
		// Create scanner object
		File file = new File(filename);
		Scanner ReadFile = new Scanner(file);
		
		// Declare loop counter
		int index = 0;
		
		// Skip any previously read lines
		for (index = 0; index < readLines; index++) {
			ReadFile.nextLine();
		}
		
		// Return whether there is another line
		return ReadFile.hasNextLine();
	}
 
 }	// END class FileManager