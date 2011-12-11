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
import edu.dhbw.t10.type.profile.Profile_V2;


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
	
	// only for creating a ZIP archive
	static final int					BUFFER	= 2048;

	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Static class!
	 * 
	 * @author DirkK
	 */
	private ImportExportManager() {
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
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
			int[] newi = { 0, 255 };
			allowedChars.add(newi);
			// saveChars to generate a new chars file
			saveChars(pathToAllowedChars, allowedChars);
			logger.debug("Chars-File was generated at: " + pathToAllowedChars);
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
	
	/**
	 * this method exports a whole proflie. It puts the tree, chars and layout file into a zip archive
	 * @param prof
	 * @param folder
	 * @throws IOException
	 * @author DirkK
	 */
	public static void exportProfiles(Profile_V2 prof, File folder) throws IOException {
		logger.debug("Exporting profile " + folder.getName());
		BufferedInputStream origin = null;
		String zipFile = folder.toString();
		logger.debug("Exporting to " + zipFile);
		FileOutputStream dest = new FileOutputStream(zipFile);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[BUFFER];
		
		for (Entry<String, String> fileS : prof.getPaths().entrySet()) {
			File file = new File(fileS.getValue());
			if (file.exists()) {
				logger.debug("Writing to zip file: " + file);
				FileInputStream fi = new FileInputStream(file);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.getName().toString());
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			} else {
				logger.warn("Could not writ to zip file: " + file + ". Will be ignored");
			}
		}
		out.close();
	}
	
	
	/**
	 * imports a profile from file
	 * just puts the zip files to the correct folders
	 * 
	 * @param zipFile
	 * @throws ZipException
	 * @throws IOException
	 * @author DirkK
	 */
	public static void importProfiles(File zipFile, Profile_V2 prof) throws ZipException, IOException {
		logger.debug("Extracting zip file " + zipFile.toString());

		// Reading the zip file
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> e = zipfile.entries();
		
		// Reading one element after the other in the zip file
		while (e.hasMoreElements()) {
			entry = (ZipEntry) e.nextElement();
			is = new BufferedInputStream(zipfile.getInputStream(entry));
			int count;
			byte data[] = new byte[BUFFER];
			
			// ignoring the names of the files, renaming them to [profileName].(tree|char|profile)
			// save this files to [datapath]/profiles/
			String file = prof.getPaths().get(entry.getName().substring(entry.getName().lastIndexOf(".") + 1));
			FileOutputStream fos = new FileOutputStream(file);
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			is.close();
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- unused methods --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	//
	// /**
	// * unused
	// * @param exportMap
	// * @param fileName
	// * @author DirkK
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
