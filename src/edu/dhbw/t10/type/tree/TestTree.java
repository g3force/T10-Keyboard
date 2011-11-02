/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.dhbw.t10.manager.profile.ImportExportManager;
import edu.dhbw.t10.type.profile.Profile;


/**
 * TODO DELETE
 * @author DirkK
 * 
 */
public class TestTree {
	
	/**
	 * TO DELETE
	 * only for testing the tree
	 * 
	 * @param args
	 */
	private static final Logger	logger	= Logger.getLogger(TestTree.class);


	public static void main(String[] args) throws IOException {
		// BasicConfigurator.configure();
		File logConfFile = new File("data/conf/log4j.conf");
		logger.error("start");
		if (logConfFile.exists()) {
			PropertyConfigurator.configure(logConfFile.getPath());
		} else {
			// basic config with only a console appender
			BasicConfigurator.configure();
			logger.setLevel(Level.ALL);
		}
		Profile prof = new Profile("projektarbeiten");
		PriorityTree tree = prof.getTree();
		// tree.importFromHashMap(ImportExportManager.importFromFile("/home/DirkK/Desktop/dict"));
		// tree.printTree();
		HashMap<String, Integer> map = ImportExportManager.importFromText("/home/DirkK/Desktop/projektarbeiten");
		logger.error("10 rfc standards, 2 reports, law of copyright (usa) and the bible");
		logger.error("amount of words " + map.size());
		tree.importFromHashMap(map);
		tree.setWords(map);
		String string = "no";
		int times = 1;
		logger.error(tree.getSuggest(string));
		logger.error("start " + times + " times getSuggest (tree)");
		for (int i = 0; i < times; i++) {
			tree.getSuggest(string);
		}
		logger.error("finished " + times + " times getSuggest(tree)");
		logger.error(tree.suggestInHashMap(string));
		logger.error("start " + times + " times suggestInHashMap");
		for (int i = 0; i < times; i++) {
			tree.suggestInHashMap(string);
		}
		logger.error("finished " + times + " times suggestInHashMap");
		logger.error("insert in tree");
		tree.insert("declation");
		logger.error("finished insert in tree");
		logger.error("saving the tree");
		prof.save();
		logger.error("saved the tree");
		logger.error("loading the tree");
		prof.load();
		logger.error("load the tree");
		
		File confFile = new File("/home/dirk/Desktop/list");
		FileWriter fw = new FileWriter(confFile);
		BufferedWriter bw = new BufferedWriter(fw);
		logger.error("saving the list");
		for (Entry<String, Integer> entry : map.entrySet()) {
			bw.write(entry.getKey() + " " + entry.getValue() + "\n");
		}
		bw.close();
		logger.error("saved the list");


		// System.out.println("Import completed");
		// Profile prof = new Profile(1, "Pflichteheft", "/home/DirkK/Desktop/PFL", tree);
		// System.out.println("starting serializsation");
		// prof.saveTree();
		// System.out.println("finished - exporting");
		// tree.importFromHashMap(ImportExportManager.importFromText("/home/DirkK/Desktop/projektarbeiten"));
		// tree.autoCleaning(1, 20000000000000L, 3);
		// tree.printTree();
		// for (PriorityElement pe : tree.getFreqSortedList())
		// pe.print();
		// tree.autoCleaning(1, new Long(20000000 * 10000000), 0);
		// System.out.println(tree.getSuggest("ha"));
		// tree.printTree();
		// ImportExportManager.exportToFile(tree.exportToHashMap(), "/home/DirkK/Desktop/dict");
		// System.out.println("finished");
		// System.out.println("1");
		// Profile prof = new Profile("Pflichteheft");
		// System.out.println("2");
		// prof.saveTree();
		// prof.setTree(new PriorityTree(""));
		// System.out.println(prof.getTree().getSuggest("ha"));
		// prof.loadTree();
		// System.out.println(prof.getTree().getSuggest("ha"));
		// System.out.println("3");
		// prof.setTree(new PriorityTree());
		// System.out.println(prof.getTree().getSuggest("ha"));
		// prof.loadTree();
		// System.out.println(prof.getTree().getSuggest("ha"));
		// System.out.println("Go");
		// // for (int i = 0; i < 1000000; i++) {
		// prof.getTree().getSuggest("ha");
		// }
		// System.out.println("1000000 times getSuggest");
		// for (int i = 0; i < 100; i++) {
		// prof.saveTree();
		// prof.loadTree();
		// }
		// System.out.println("100 times load/saveTree");
		// ImportManager imp = new ImportManager();
		// imp.readFile("/home/DirkK/Desktop/test");
		// tree.importFromHashMap(imp.getImportedInfos());
		// tree.printTree();
		// System.out.println(tree.getSuggest("No"));
		// System.out.println(tree.getSuggest("app"));
		// System.out.println(tree.getSuggest("cou"));
		// System.out.println(tree.getSuggest("be"));
		// System.out.println(tree.getSuggest("fou"));
		// System.out.println(tree.getSuggest("fo"));
		// System.out.println(tree.getSuggest("log"));
		// System.out.println(tree.getSuggest("Ple"));
		// System.out.println(tree.getSuggest("ini"));
		// System.out.println(tree.getSuggest("sys"));
		// System.out.println(tree.getSuggest("pro"));
		// System.out.println(tree.getSuggest("Jav"));
		// tree.printTree("ic", false);
		// System.out.println(tree.getSuggest("Das"));
		// System.out.println(tree.getSuggest("Arc"));
		// System.out.println(tree.getSuggest("bei"));
		// System.out.println(tree.getSuggest("die"));
		// System.out.println(tree.getSuggest("Arc"));
		// System.out.println(tree.getSuggest("für"));
		// System.out.println(tree.getSuggest("uns"));
		// System.out.println(tree.getSuggest("Pro"));
		// System.out.println(tree.getSuggest("Bil"));
		// System.out.println(tree.getSuggest("Tas"));
		// System.out.println(tree.getSuggest("mit"));
		// System.out.println(tree.getSuggest("Pri"));
		// System.out.println(tree.getSuggest("und"));
		// System.out.println(tree.getSuggest("Obe"));
		
		
		// tree.insert("hallo");
		// tree.insert("test");
		// tree.insert("holla");
		// tree.insert("holla");
		// tree.insert("holoo");
		// tree.insert("holoo");
		// tree.insert("holoo");
		// tree.insert("ich");
		// tree.printTree();
		// tree.delete("holla");
		// tree.printTree();
		// System.out.println(tree.getSuggest("h"));
		
	}
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
