/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): hpadmin
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.profile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.type.tree.PriorityElement;


/**
 * Handles the import/export of a file.
 */
public class ImportExportManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(ImportExportManager.class);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ImportExportManager() {
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * import for special dictionary
	 * @param fileName path to text file
	 * @return importable HashMap
	 */
	public static HashMap<String, Integer> readFile(String fileName) {
		HashMap<String, Integer> fileContent = new HashMap<String, Integer>();
		try {
			File fh = new File(fileName);
			FileReader fr = new FileReader(fileName);
			BufferedReader x = new BufferedReader(fr);
			
			int offset = 0;
			while (offset <= fh.length()) {
				String res = x.readLine();
				// System.out.println("Tmpbuf: " + res);
				
				if (res.indexOf(" ") >= 0) {
					int divider = res.indexOf(" ");
					fileContent.put(res.substring(0, divider),
							17 - Integer.parseInt(res.substring(divider + 1, res.length())));
				} else {
					fileContent.put(res, 0);
				}
				offset += res.length() + 2;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileContent;
	}
	
	
	/**
	 * takes an arbitrary text files and inserts all contained words in the tree
	 * Tree cleaning recommended after this
	 * @param fileName path to the input file
	 */
	public static HashMap<String, Integer> importFromText(String fileName) {
		HashMap<String, Integer> importMap = new HashMap<String, Integer>();
		logger.error("reading form file");
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader x = new BufferedReader(fr);
			
			String res = x.readLine();
			while (res != null) {
				// System.out.println("Tmpbuf: " + res);
				res = StringHelper.removePunctuation(res);
				String[] words = res.split(" ");
				for (String word : words)
					if (word.length() > 1)
						increase(importMap, word);
				res = x.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.error("read form file");
		return importMap;
	}
	
	
	/**
	 * takes a Map with the structure word -> frequency and prints it into a file
	 * @param exportMap the dictionary map
	 * @param fileName the path inclusive file name
	 */
	public static void exportToFile(HashMap<String, Integer> exportMap, String fileName) {
		logger.error("saving to file");
		try {
			FileWriter file = new FileWriter(fileName);
			BufferedWriter o = new BufferedWriter(file);
			for (Entry<String, Integer> entry : exportMap.entrySet()) {
				o.write(entry.getKey() + " " + entry.getValue() + "\n");
			}
			o.close();
		} catch (IOException err) {
			logger.error(err.getMessage());
		}
		logger.error("saved to file");
	}
	
	
	public static void exportToSortedFile(HashMap<String, Integer> exportMap, String fileName) {
		logger.error("saving to file");
		try {
			FileWriter file = new FileWriter(fileName);
			BufferedWriter o = new BufferedWriter(file);
			while (exportMap.size() != 0) {
				String word = "";
				int freq = 0;
				for (Entry<String, Integer> entry : exportMap.entrySet()) {
					if(entry.getValue()>freq) {
						word=entry.getKey();
						freq = entry.getValue();
					}
				}
				exportMap.remove(word);
				o.write(word + " " + freq + "\n");
			}

			o.close();
		} catch (IOException err) {
			logger.error(err.getMessage());
		}
		logger.error("saved to file");
	}
	
	
	public static void exportToFileFromSortedlist(LinkedList<PriorityElement> ll, String fileName) {
		logger.error("saving to file");
		try {
			FileWriter file = new FileWriter(fileName);
			BufferedWriter o = new BufferedWriter(file);
			for (PriorityElement entry : ll) {
				o.write(entry.buildWord() + " " + entry.getFrequency() + "\n");
			}
			o.close();
		} catch (IOException err) {
			logger.error(err.getMessage());
		}
		logger.error("saved to file");
	}

	
	/**
	 * imports a dictionary from a file
	 * returns the input of the file as HashMap (word->frequency)
	 * @param fileName
	 * @return HashMap
	 */
	public static HashMap<String, Integer> importFromFile(String fileName, boolean withFreq) {
		HashMap<String, Integer> importMap = new HashMap<String, Integer>();
		logger.error("reading form file");
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader x = new BufferedReader(fr);
			String res = x.readLine();
			while (res != null) {
				if (withFreq && res.contains(" ")) {
					String[] entries = res.split(" ");
					if (entries.length == 2 && entries[0].length() > 1 && entries[1].length() > 0) {
						importMap.put(entries[0], Integer.parseInt(entries[1]));
					}
				} else {
					increase(importMap, res);
				}
				res = x.readLine();
			}
		} catch (IOException err) {
			logger.error(err.getMessage());
		}
		logger.error("read form file");
		return importMap;
	}
	
	
	/**
	 * inserts word to the HashMap importMap
	 * if it exists it will be increased by one
	 * @param importMap the affected HashMap
	 * @param word inserted word
	 */
	private static void increase(HashMap<String, Integer> importMap, String word) {
		if (importMap.containsKey(word)) {
			int amount = importMap.get(word);
			amount++;
			importMap.remove(word);
			importMap.put(word, amount);
		} else {
			importMap.put(word, 1);
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
