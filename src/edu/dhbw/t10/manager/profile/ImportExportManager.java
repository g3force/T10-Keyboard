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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.type.profile.Profile;


/**
 * Handles the import/export of the files for the profile management (tree and chars).
 * 
 * @author DirkK
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
	 * TODO DirkK DELETE
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
	
	
	// ----------------------------CHARS-----------------------------
	
	/**
	 * reads the .chars file at the given path and returns the content in a list
	 * @param pathToAllowedChars the path to the .chars file of the current profile
	 * @return a linked list containing the ranges of the chars which are allowed
	 * @throws NumberFormatException
	 * @throws IOException
	 * @author DirkK
	 */
	public static LinkedList<int[]> loadChars(String pathToAllowedChars) throws NumberFormatException, IOException {
		LinkedList<int[]> allowedChars = new LinkedList<int[]>();
		File confFile = new File(pathToAllowedChars);
		if (confFile.exists()) {
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);
			String entry = "";
			while ((entry = br.readLine()) != null) {
				String[] entries = entry.split("-");
				int[] newi = { Integer.parseInt(entries[0]), Integer.parseInt(entries[1]) };
				allowedChars.add(newi);
			}
		} else {
			logger.warn("No allowed chars file found at " + pathToAllowedChars);
			int[] newi = { 0, 255 };
			allowedChars.add(newi);
		}
		return allowedChars;
	}
	
	
	/**
	 * reads the .chars file at the given path and returns the content in a list
	 * @param pathToAllowedChars the path to the .chars file of the current profile
	 * @param allowedChars the chars to be saved
	 * @throws IOException
	 * @author DirkK
	 */
	public static void saveChars(String pathToAllowedChars, LinkedList<int[]> allowedChars) throws IOException {
		File confFile = new File(pathToAllowedChars);
		FileWriter fw = new FileWriter(confFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (int i = 0; i < allowedChars.size(); i++) {
			bw.write(allowedChars.get(i)[0] + "-" + allowedChars.get(i)[1] + "\n");
		}
		bw.close();
	}
	
	
	// -------------------------------TREE----------------------------------

	/**
	 * takes an arbitrary text files and inserts all contained words in the tree
	 * Tree cleaning recommended after this
	 * @param fileName path to the input file
	 * @throws IOException
	 */
	public static HashMap<String, Integer> importFromText(String fileName) throws IOException {
		HashMap<String, Integer> importMap = new HashMap<String, Integer>();
		logger.debug("reading form file");
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
		logger.debug("read form file");
		return importMap;
	}
	
	
	/**
	 * takes a Map with the structure word -> frequency and prints it into a file
	 * @param exportMap the dictionary map
	 * @param fileName the path inclusive file name
	 * @throws IOException
	 * @author DirkK
	 */
	public static void exportToFile(HashMap<String, Integer> exportMap, String fileName) throws IOException {
		logger.debug("saving to file");
		int counter = 0;
		FileWriter file = new FileWriter(fileName);
		BufferedWriter o = new BufferedWriter(file);
		for (Entry<String, Integer> entry : exportMap.entrySet()) {
			o.write(entry.getKey() + " " + entry.getValue() + "\n");
			counter++;
		}
		o.close();
		logger.debug("saved to file (" + counter + " words written)");
	}
	
	
	/**
	 * imports a dictionary from a file
	 * returns the input of the file as HashMap (word->frequency)
	 * @param fileName
	 * @return HashMap
	 * @throws IOException
	 * @author DirkK
	 */
	public static HashMap<String, Integer> importFromFile(String fileName, boolean withFreq) throws IOException {
		HashMap<String, Integer> importMap = new HashMap<String, Integer>();
		logger.debug("reading form file");
		int amount = 0;
		FileReader fr = new FileReader(fileName);
		BufferedReader x = new BufferedReader(fr);
		String res = x.readLine();
		while (res != null) {
			if (withFreq && res.contains(" ")) {
				String[] entries = res.split(" ");
				if (entries.length == 2 && entries[0].length() > 1 && entries[1].length() > 0) {
					importMap.put(entries[0], Integer.parseInt(entries[1]));
					amount++;
				}
			} else {
				increase(importMap, res);
			}
			res = x.readLine();
		}
		logger.debug("read form file (" + amount + ")");
		return importMap;
	}
	
	
	/**
	 * inserts word to the HashMap importMap
	 * if it exists it will be increased by one
	 * @param importMap the affected HashMap
	 * @param word inserted word
	 * @author DirkK
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
	
	
	// ----------------------------------PROFILES----------------------------
	static final int	BUFFER	= 2048;
	
	
	public static void exportProfiles(Profile prof, File folder) throws IOException {
		logger.debug("Exporting profile " + prof.getName());
		BufferedInputStream origin = null;
		String zipFile = folder.toString();
		logger.debug("Exporting to " + zipFile);
		FileOutputStream dest = new FileOutputStream(zipFile);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[BUFFER];
		// get a list of files from current directory
		File[] files = new File[3];
		files[0] = new File(prof.getPathToTree());
		files[1] = new File(prof.getPathToAllowedChars());
		files[2] = new File(prof.getPathToLayoutFile());
		
		for (int i = 0; i < files.length; i++) {
			logger.debug("Writing to zip file: " + files[i]);
			FileInputStream fi = new FileInputStream(files[i]);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(files[i].getName().toString());
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
		out.close();
	}
	
	
	/**
	 * imports a profile from file
	 * just puts the zip files to the correct folders
	 * TODO OPTIONAL dirty, neets ProfileManager
	 * @param zipFile
	 * @throws ZipException
	 * @throws IOException
	 * @author dirk
	 */
	
	public static void importProfiles(ProfileManager profM, File zipFile) throws ZipException, IOException {
		logger.debug("Extracting zip file " + zipFile.toString());
		String profileName = zipFile.getName();
		profileName = profileName.replace(".zip", "");
		int counter = 0;
		while (profM.existProfile(profileName)) {
			counter++;
			if (counter == 0)
				profileName += counter;
			else
				profileName += profileName.substring(0, profileName.length() - 1) + counter;
		}
		logger.debug("Profile " + profileName + " created");
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> e = zipfile.entries();
		while (e.hasMoreElements()) {
			entry = (ZipEntry) e.nextElement();
			is = new BufferedInputStream(zipfile.getInputStream(entry));
			int count;
			byte data[] = new byte[BUFFER];
			String file = "data/profiles/" + profileName + entry.getName().substring(entry.getName().lastIndexOf("."));
			FileOutputStream fos = new FileOutputStream(file);
			logger.debug(file + " extracted");
			// TODO if data folder changebale -> change here too
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			is.close();
		}
		Controller.getInstance().createProfile(profileName);
	}

	
	// --------------------------------------------------------------------------
	// --- unused methods --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	//
	// /**
	// * unused
	// * @param exportMap
	// * @param fileName
	// * @author dirk
	// */
	// public static void exportToSortedFile(HashMap<String, Integer> exportMap, String fileName) {
	// logger.error("saving to file");
	// try {
	// FileWriter file = new FileWriter(fileName);
	// BufferedWriter o = new BufferedWriter(file);
	// while (exportMap.size() != 0) {
	// String word = "";
	// int freq = 0;
	// for (Entry<String, Integer> entry : exportMap.entrySet()) {
	// if (entry.getValue() > freq) {
	// word = entry.getKey();
	// freq = entry.getValue();
	// }
	// }
	// exportMap.remove(word);
	// o.write(word + " " + freq + "\n");
	// }
	//
	// o.close();
	// } catch (IOException err) {
	// logger.error(err.getMessage());
	// }
	// logger.error("saved to file");
	// }
	//
	//
	// public static void exportToFileFromSortedlist(LinkedList<PriorityElement> ll, String fileName) {
	// logger.error("saving to file");
	// try {
	// FileWriter file = new FileWriter(fileName);
	// BufferedWriter o = new BufferedWriter(file);
	// for (PriorityElement entry : ll) {
	// o.write(entry.buildWord() + " " + entry.getFrequency() + "\n");
	// }
	// o.close();
	// } catch (IOException err) {
	// logger.error(err.getMessage());
	// }
	// logger.error("saved to file");
	// }

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
