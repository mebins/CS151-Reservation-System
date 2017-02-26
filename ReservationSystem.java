
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Reservation System is the main class that holds the entry point in the
 * program. The main point of this class is to prompt questions for the user,
 * add people to the database, remove people from the database print manifest
 * and print availability of seats.
 * 
 * @author Mebin Skaria
 * @version 1.0
 * @since 2/18/2017
 * 
 */
public class ReservationSystem extends Database {

	/**
	 * Creates a database and prompts a menu for the user.
	 * 
	 * @param name
	 *            The name of the flight
	 */
	public ReservationSystem(String name) {
		super(name);
		menu();
	}

	/**
	 * Prompts the User with a menu.
	 */
	public void menu() {

		System.out.println(
				"Add [P]assenger, Add [G]roup, [C]ancel Reservations, Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit");
		String input = "";
		if (sc.hasNextLine()) {
			input = sc.nextLine();
		}

		if (input.length() == 1) {
			switch (input.charAt(0)) {
			case 'p':
			case 'P':
				reserveMenu();
				menu();
				break;
			case 'g':
			case 'G':
				String groupName = "";
				String section = "";
				String temp = "";
				while (groupName.equals("")) {
					System.out.println("Group Name");
					if (sc.hasNextLine()) {
						groupName = sc.nextLine();
					} else {
						break;
					}
				}
				while (section.equals("")) {
					System.out.println("Class(First or Economy): ");
					if (sc.hasNextLine()) {
						section = sc.nextLine();
					}
					else {
						break;
					}
					if (!(section.toLowerCase().equals("economy") || section.toLowerCase().equals("first"))) {
						System.out.println("Incorrect Class: " + section);
						section = "";
					}
				}
				while (temp.equals("")) {
					System.out.println("Names seperated by commas EX:John Smith, John Smith, John Smith:");
					if (sc.hasNextLine()) {
						temp = sc.nextLine();
					} else {
						break;
					}
				}
				String names[] = temp.split(",");
				for(int i = 0; i < names.length;i++)
				{
					names[i] = names[i].trim();
				}
				ArrayList<String> keys = sf.findGroupSeats(available, names.length,
						section.toLowerCase().equals("first"));
				int i = 0;
				for (String key : keys) {

					manifest.put(key, names[i]);
					available.replace(key, false);
					group.put(key, groupName);
					System.out.println("Added: " + names[i] + " to " + key);
					i++;
				}
				menu();
				break;
			case 'c':
			case 'C':
				cancelMenu();
				menu();
				break;
			case 'a':
			case 'A':
				printAvailable();
				menu();
				break;
			case 'm':
			case 'M':
				printManifest();
				menu();
				break;
			case 'q':
			case 'Q':
				saveData();
				System.out.println("Reservation System Terminated");
				System.exit(0);
				break;
			default:
				menu();
			}
		} else {
			menu();
		}
	}

	/**
	 * Menu for Canceling a reservation.
	 */
	private void cancelMenu() {
		String name = "";
		System.out.println("Cancel Reservation Name: ");
		if (sc.hasNextLine()) {
			name = sc.nextLine();
			cancelReservation(name);
		} else {
			return;
		}
	}

	/**
	 * Menu for reserving a passenger.
	 */
	private void reserveMenu() {

		String section = "";
		String name = "";
		String pref = "";

		while (name.equals("")) {
			System.out.print("Name: ");
			if (sc.hasNextLine()) {
				name = sc.nextLine();
			} else {
				break;
			}

		}
		while (section.equals("")) {
			System.out.print("Which Class(First or Economy): ");
			if (sc.hasNextLine()) {
				section = sc.nextLine();
			}
			else
			{
				break;
			}
			if (!(section.toLowerCase().equals("economy") || section.toLowerCase().equals("first"))) {
				System.out.println("Incorrect Class:" + section);
				section = "";
			}
		}

		while (pref.equals("")) {
			if (section.toLowerCase().equals("economy")) {
				System.out.print("Preference[W]indow, [C]enter, [A]isle: ");
			} else if (section.toLowerCase().equals("first")) {
				System.out.print("Preference[W]indow, [A]isle: ");
			}
			
			if (sc.hasNextLine()) {
				pref = sc.nextLine();
				if(!(pref.toLowerCase().equals("w")||pref.toLowerCase().equals("c")||pref.toLowerCase().equals("a")))
				{
					System.out.println("Incorrect Seat Preference: " + pref);
					pref = "";
				}
			} else {
				break;
			}
		}
		reservePassenger(name, section, pref);
	}

	/**
	 * Function for Reserving a Passenger.
	 * 
	 * @param name
	 *            Name of the passenger.
	 * @param section
	 *            Economy or First.
	 * @param pref
	 *            [W]indow,[C]enter,[A]isle.
	 * @return If successfully reserved a passenger will return true, else will
	 *         return false.
	 */
	public boolean reservePassenger(String name, String section, String pref) {
		String seat = "";
		boolean processed = false;
		if (section.toLowerCase().equals("economy")) {
			seat = sf.economyFinder(available, pref);
			if (!seat.equals("")) {
				available.put(seat, false);
				manifest.put(seat, name);
				System.out.println(seat);
				processed = true;
			} else {
				String choice = "";
				System.out.println("[M]ain Menu or [T]ry Again");
				if (sc.hasNextLine()) {
					choice = sc.nextLine();
				} else {
					return false;
				}
				switch (choice.toLowerCase()) {
				case "m":
					menu();
					break;
				case "t":
					reserveMenu();
					break;
				default:
					menu();
					break;
				}
			}
		} else if (section.toLowerCase().equals("first")) {
			seat = sf.firstFinder(available, pref);
			if (!seat.equals("")) {
				available.remove(seat);
				available.put(seat, false);
				manifest.put(seat, name);
				System.out.println(seat);
				processed = true;
			} else {
				System.out.println("[M]ain Menu or [T]ry Again");
				String choice = "";
				if (sc.hasNextLine()) {
					choice = sc.nextLine();
				}
				switch (choice.toLowerCase()) {
				case "m":
					menu();
					break;
				case "t":
					reserveMenu();
					break;
				default:
					menu();
					break;
				}
			}
		}
		return processed;
	}

	/**
	 * Function for Reserving a group of passengers.
	 * 
	 * @param groupName
	 *            Group name of passengers.
	 * @param section
	 *            Economy or First.
	 * @param names
	 *            Names of the Passengers.
	 * @return If successfully reserved a passenger will return true, else will
	 *         return false.
	 */
	public boolean reserveGroup(String groupName, String section, String names) {
		String input[] = names.split(",");

		switch (section.toLowerCase()) {
		case "economy":
			if (input.length > sf.getAvailableEconomySeats(available))
				return false;

			break;

		case "first":
			if (input.length > sf.getAvailableFirstSeats(available))
				return false;

			break;
		}

		return true;
	}

	/**
	 * Cancels a reservation for group or passenger.
	 * 
	 * @param name
	 *            The name of the group or the passenger.
	 * @return If successfully reserved a passenger will return true, else will
	 *         return false.
	 *
	 */
	public boolean cancelReservation(String name) {
		name = name.toLowerCase();
		ArrayList<String> keys = new ArrayList<>();
		for (String key : manifest.keySet()) {
			if (manifest.get(key).toLowerCase().equals(name)) {
				keys.add(key);
			}
		}
		for (String key : group.keySet()) {
			if (group.get(key).toLowerCase().equals(name)) {
				keys.add(key);
			}
		}
		if (keys.isEmpty()) {
			System.out.println("Name not found: " + name);
			return false;
		}
		for (String e : keys) {
			System.out.println("Removed: " + manifest.get(e));
			available.replace(e, true);
			manifest.remove(e);
			group.remove(e);
		}
		return true;
	}

	/**
	 * Prints all available seats.
	 */
	public void printAvailable() {
		System.out.println("AVAILABLE SEATS");
		ArrayList<String> firstList = new ArrayList<>();
		ArrayList<String> economyList = new ArrayList<>();
		String firstAvailableSeats[] = new String[sf.firstRowMax];
		String economyAvailableSeats[] = new String[sf.economyRowMax - (sf.economyRowMin-1)];
		Arrays.fill(firstAvailableSeats, "");
		Arrays.fill(economyAvailableSeats, "");
		for (String key : available.keySet()) {
			if (available.get(key)) {
				if (key.length() == 2) {
					firstList.add(key);
				} else {
					economyList.add(key);
				}
			}

		}
		if (firstList.size() > 0) {
			System.out.println("FIRST");
			for (String key : firstList) {
				String letter = key.substring(0, 1);
				String seat = key.substring(1);
				firstAvailableSeats[Integer.parseInt(seat) - 1] += letter + ",";
			}
			for (int i = 0; i < firstAvailableSeats.length; i++) {
				String s = firstAvailableSeats[i];

				System.out.println(i + 1 + ": " + s.substring(0, s.length() - 1));
			}
		}
		if(economyList.size() > 0)
		{	
			System.out.println("ECONOMY");
			for (String key : economyList) {
				String letter = key.substring(0, 1);
				String seat = key.substring(1);
				economyAvailableSeats[Integer.parseInt(seat) - sf.economyRowMin] += letter + ",";
			}
			for (int i = 0; i < economyAvailableSeats.length; i++) {
				String s = economyAvailableSeats[i];

				System.out.println((i + sf.economyRowMin) + ": " + s.substring(0, s.length() - 1));
			}
		}
		
	}

	/**
	 * Prints manifest list.
	 */
	public void printManifest() {
		ArrayList<String> firstList = new ArrayList<String>();
		ArrayList<String> economyList = new ArrayList<String>();
		System.out.println("MANIFEST");
		for (String key : manifest.keySet()) {
			if (key.length() == 2) {
				firstList.add(key);
			} else {
				economyList.add(key);
			}
		}
		if (firstList.size() > 0) {
			System.out.println("FIRST");
		}
		for (String key : firstList) {
			System.out.println(key + " : " + manifest.get(key));
		}
		if (economyList.size() > 0) {
			System.out.println("ECONOMY");
		}

		for (String key : economyList) {
			System.out.println(key + " : " + manifest.get(key));
		}
	}

	public static void main(String args[]) {

		String databaseName = "CL34";

		if (args.length > 0) {
			if (args[0].length() > 0) {
				databaseName = args[0];
			} else {
				System.out.println("Error, Incorrect database name: " + args[0]);
			}
		}
		new ReservationSystem(databaseName);
	}

}
