package net.it_tim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsLoader{
	/**
	 * Конструктор класу.
	 * @param config_file - файл конфігурації вигляду .[app_dir]/configfile
	 */
	public SettingsLoader(String config_file){
		String homedir = System.getProperty("user.home"); // Get a system property

		// Read properties from a configuration file
		options = new Properties(); // Empty properties list
		configfile = new File(homedir, config_file); // The configuration file
	}
	
	public boolean loadConfig() throws IOException {
		try {
			 options.load(new FileInputStream(configfile)); // Load props from the file
			 return true;
			} catch (IOException ex) {
				throw ex;
			}
	}
	
	public boolean saveConfig() {
		try {
			 options.store(new FileOutputStream(configfile), // Output stream
			        "Config File"); // File header comment text
			 return true;
			} catch (IOException ex) { 
				ex.printStackTrace(); 
				return false;
			}
	}
	
	/**
	 * Отримує параметр з файлу.
	 * @param option
	 * @param default_value
	 */	
	public String getOption(String option, String default_value) {
		String result = options.getProperty(option, default_value);
		return result;
	}
	
	/**
	 * Записує параметр.
	 * @param option
	 * @param default_value
	 */	
	public void setOption(String option, String value) {
		options.setProperty(option, value);
	}
	
	private Properties options;
	private File configfile;
}
