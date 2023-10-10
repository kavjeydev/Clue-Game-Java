package clueGame;

public class BadConfigFormatException extends Exception {
	
	
	//Default message thrown with no input.
	public BadConfigFormatException() {
		super("There was an error reading the configuration files");
	}
	
	//Shows the line of bad format and the content of the line
	public BadConfigFormatException(int line, String content) {
		super("The configuration files do not have the correct format on line "
				 + line + ": " + content);
	}

	//Shows the file that had a bad configuration.
	public BadConfigFormatException(String string) {
		super("Config file error: " + string);
	}
}
