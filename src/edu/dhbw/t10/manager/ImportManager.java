/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): hpadmin
 *
 * *********************************************************
 */
package edu.dhbw.t10.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


/**
 * Handles the import of a file.
 */
public class ImportManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private HashMap<String, Integer>	fileContent;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ImportManager() {
		fileContent = new HashMap<String, Integer>();
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void readFile(String fileName) {
		try {
			fileContent = null;
			fileContent = new HashMap<String, Integer>();
			
			File fh = new File(fileName);
			FileReader fr = new FileReader(fileName);
			BufferedReader x = new BufferedReader(fr);

			int offset = 0;
			while (offset <= fh.length()) {
				String res = x.readLine();
				System.out.println("Tmpbuf: " + res);

				if (res.indexOf(",") >= 0) {
					int divider = res.indexOf(",");
					fileContent.put(res.substring(0, divider), Integer.parseInt(res.substring(divider + 1, res.length())));
				} else {
					fileContent.put(res, 0);
				}
				offset += res.length() + 2;
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public HashMap<String, Integer> getImportedInfos() {
		return fileContent;
	}
}
