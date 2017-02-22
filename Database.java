
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Stores all the data in this Reservation Program. 
 * Reads and Writes files to the database.
 * Saves a final snapshot of the data when the program is closed.
 * Generates all necessary file for the database.
 * If Generated, the database will be loaded at program start.
 * 
 * @author Mebin Skaria
 * @version 1.0
 * @since 2/18/2017
 */
public class Database {
	/*
	 * FIELDS
	 */
	public HashMap<String, String> manifest = new HashMap<>();
	public HashMap<String, String> group = new HashMap<>();
	public TreeMap<String, Boolean> available = new TreeMap<>();
	private final String name;
	private final File database;
	private final File folder;
	private Scanner databaseReader;
	public static SeatFinder sf;
	public static Scanner sc = new Scanner(System.in);

	/**
	 * Creates a database, loading or generating the necessary files.
	 * If generating the database, a folder with the database's name is created, a readMe
	 * on how to use the input.txt, the database files, and then initializes the database.
	 * @param name The name of the database
	 */
	public Database(String name) {
		database = new File(name + "\\" + name + ".txt");
		sf = new SeatFinder();
		this.name = name;
		folder = new File(name);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		if (!database.exists()) {
			try {
				database.createNewFile();
				initalAvailable();
				readMe();
				System.out.println("Generated " + name + " Database");
				saveData();
				loadDatabase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loadDatabase();
		}
	}
	/*
	 * METHODS
	 */

	/**
	 * If the database has not been generated before, this function will 
	 * initialize the available seats for the database.
	 */
	private void initalAvailable() {
		String firstRow[] = { "A", "B", "C", "D" };
		String economyRow[] = { "A", "B", "C", "D", "E", "F" };
		for (int i = 0; i < firstRow.length; i++) {
			for (int j = 1; j <= 2; j++) {
				available.put(firstRow[i] + "" + j, true);
			}
		}

		for (int i = 0; i < economyRow.length; i++) {
			for (int j = 10; j <= 29; j++) {
				available.put(economyRow[i] + "" + j, true);
			}
		}
	}

	/**
	 * Loads the Database when first starting up the program.
	 */
	private void loadDatabase() {
		try {
			databaseReader = new Scanner(database);
			System.out.println("Loading Database");
			while (databaseReader.hasNextLine()) {
				String s = databaseReader.nextLine();
				String input[];
				switch (s.substring(0, 1)) {
				case "A":
					input = s.substring(2).split(":");
					boolean temp = input[1].equals("true");
					available.put(input[0], temp);
					break;
				case "M":
					input = s.substring(2).split(":");
					manifest.put(input[0], input[1]);
					break;
				case "G":
					input = s.substring(2).split(":");
					group.put(input[0], input[1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Generates the readMe txt File when the database has not been generated before.
	 * Provides information on how to use the input txt file when doing input redirection.
	 */
	private void readMe() {
		if (!new File("readMe.txt").exists()) {
			ArrayList<String> list = new ArrayList<>();
			list.add("-----------------------------------KEY FOR INPUT TEXT-------------------------------");
			list.add("To add a passenger:");
			list.add("P");
			list.add("(NAME)");
			list.add("(CLASS - [FIRST,ECONOMY])");
			list.add("(Preference of Seat-ECONOMY:[W]indow,[A]isle,[C]enter-FIRST:[W]indow,[A]isle)");
			list.add("-----------------------------------------------------------------------------------");
			list.add("To add a group: ");
			list.add("G");
			list.add("(Group Name)");
			list.add("(Class)");
			list.add("(Name,Name,Name) < NO SPACES BETWEEN NAMES");
			list.add("CORRECT EXAMPLE: ('John Smith,John Smith,John Smith')");
			list.add("WRONG EXAMPLE: ('John Smith, John Smith, John Smith')");
			list.add("----------------------------------------------------------------------------------");
			list.add("To remove a passenger or group:");
			list.add("C");
			list.add("(Name or Group Name)");
			list.add("----------------------------------------------------------------------------------");
			list.add("To print manifest list");
			list.add("M");
			list.add("----------------------------------------------------------------------------------");
			list.add("To print Available Seats");
			list.add("A");
			list.add("------------------------------!!!!!REQUIRED!!!!!----------------------------------");
			list.add("At the end of the input.txt, you must put Q to save all data");
			writeFile("readMe.txt", list, false);
			System.out.println("Generated readMe File");
		}
	}

	/**
	 * Writes a File with the given information in the same location as the source java files.
	 * @param target The File name and extension.
	 * @param data The data that should be written in the file.
	 * @param append If the file should be appended or rewritten.
	 */
	private void writeFile(String target, ArrayList<String> data, boolean append) {

		try {
			FileWriter fw;
			fw = new FileWriter(name + "\\" + target, append);
			BufferedWriter br = new BufferedWriter(fw);
			PrintWriter pr = new PrintWriter(br);

			for (String e : data) {
				pr.println(e);
			}
			pr.close();
			br.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves all the data structures into the database file.
	 */
	public void saveData() {
		System.out.println("Saving Data");
		ArrayList<String> list = new ArrayList<String>();
		for (String key : available.keySet()) {
			list.add("A:" + key + ":" + available.get(key));
		}
		for (String key : manifest.keySet()) {
			list.add("M:" + key + ":" + manifest.get(key));
		}
		for (String key : group.keySet()) {
			list.add("G:" + key + ":" + group.get(key));
		}
		writeFile(name + ".txt", list, false);
	}

	/**
	 * 
	 * @return Name of the database.
	 */
	public String getName() {
		return name;
	}
}
