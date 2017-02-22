
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Seat Finder has One main point in this Reservation System. It is to as the
 * name says, to find Seats that are requested from the available seats data
 * structure.
 * 
 * @author Mebin Skaria
 * @version 1.0
 * @since 2/18/2017
 */
public class SeatFinder {

	/*
	 * FIELDS
	 */
	public final int economyRowMin = 10;
	public final int economyRowMax = 29;
	private final String economySeats[] = { "A", "B", "C", "D", "E", "F" };
	private final String economyAisles[] = { "C", "D" };
	private final String economyWindows[] = { "A", "F" };
	private final String economyCenters[] = { "B", "E" };

	public final int firstRowMin = 1;
	public final int firstRowMax = 2;
	private final String firstSeats[] = { "A", "B", "C", "D" };
	private final String firstAisles[] = { "B", "C" };
	private final String firstWindows[] = { "A", "D" };

	/**
	 * Constructs the Seat Finder Object.
	 */
	public SeatFinder() {

	}
	/*
	 * METHODS
	 */

	/**
	 * Function finds a seat in the Economy class, or returns a blank string if
	 * no seats were found.
	 * 
	 * @param available The available seats data structure.
	 * @param pref The Preference of Seat [W]indow,[C]enter,[A]isle.
	 * @return String that is a seat key from the requested preference.
	 */
	public String economyFinder(TreeMap<String, Boolean> available, String pref) {
		String seat = "";
		switch (pref.toLowerCase()) {
		case "w":
			for (int letterIndex = 0; letterIndex < economyWindows.length; letterIndex++) {
				for (int i = economyRowMin; i <= economyRowMax; i++) {
					String key = economyWindows[letterIndex] + "" + i;
					if (available.get(key))
						return key;
				}
			}
			System.out.println("No seats are open in Economy: Window");
			break;
		case "c":
			for (int letterIndex = 0; letterIndex < economyCenters.length; letterIndex++) {
				for (int i = economyRowMin; i <= economyRowMax; i++) {
					String key = economyCenters[letterIndex] + "" + i;
					if (available.get(key))
						return key;
				}
			}
			System.out.println("No seats are open in Economy: Center");
			break;
		case "a":
			for (int letterIndex = 0; letterIndex < economyCenters.length; letterIndex++) {
				for (int i = economyRowMin; i <= economyRowMax; i++) {
					String key = economyAisles[letterIndex] + "" + i;
					if (available.get(key))
						return key;
				}
			}
			System.out.println("No seats are open in Economy: Aisle");
			break;
		default:
			System.out.println("Incorrect Preference: " + pref);
		}
		return seat;
	}

	/**
	 * Function finds a seat in the First class, or returns a blank string if no
	 * seats were found.
	 * 
	 * @param available The available seats data structure.
	 * @param pref The Preference of Seat [W]indow,[C]enter,[A]isle.
	 * @return String that is a seat key from the requested preference.
	 */
	public String firstFinder(TreeMap<String, Boolean> available, String pref) {
		String seat = "";
		switch (pref.toLowerCase()) {
		case "w":
			for (int letterIndex = 0; letterIndex < firstWindows.length; letterIndex++) {
				for (int i = firstRowMin; i <= firstRowMax; i++) {
					String key = firstWindows[letterIndex] + "" + i;
					if (available.get(key))
						return key;
				}
			}
			System.out.println("No seats are open in First: Window");
			break;
		case "a":
			for (int letterIndex = 0; letterIndex < economyCenters.length; letterIndex++) {
				for (int i = firstRowMin; i <= firstRowMax; i++) {
					String key = firstAisles[letterIndex] + "" + i;
					if (available.get(key))
						return key;
				}
			}
			System.out.println("No seats are open in First: Aisle");
			break;
		}
		return seat;
	}

	/**
	 * Gets the total amount of seats in First Class
	 * @param available Available seats data structure.
	 * @return Total amount of seats free in the First class.
	 */
	public int getAvailableFirstSeats(TreeMap<String, Boolean> available) {
		int counter = 0;
		for (int j = 0; j < firstSeats.length; j++) {
			for (int i = firstRowMin; i < firstRowMax; i++) {
				if (available.get(firstSeats[j] + "" + i))
					counter++;
			}
		}
		return counter;
	}
	/**
	 * Gets the total amount of seats in Economy Class
	 * @param available Available seats data structure.
	 * @return Total amount of seats free in the Economy class.
	 */
	public int getAvailableEconomySeats(TreeMap<String, Boolean> available) {
		int counter = 0;
		for (int j = 0; j < economySeats.length; j++) {
			for (int i = economyRowMin; i < economyRowMax; i++) {
				if (available.get(economySeats[j] + "" + i))
					counter++;
			}
		}
		return counter;
	}

	/**
	 * @param available Available Seats data structure.
	 * @param amountOfPeople The amount of seats the function is asked to find.
	 * @param first If the function should look in first or economy.
	 * @return All the seat keys for the amount of people requested or an empty arrayList if request was not complete.
	 */
	public ArrayList<String> findGroupSeats(TreeMap<String, Boolean> available, int amountOfPeople, boolean first) {
		ArrayList<String> seats = new ArrayList<String>();
		if (first) {
			seats = recurseFindLargestFirstRow(available, amountOfPeople);
		} else {
			seats = recurseFindLargestEconRow(available, amountOfPeople);
		}

		if (seats.size() != amountOfPeople) {
			System.out.println("Not Enough Seats for Group Size of : " + amountOfPeople);
			seats.clear();
		}
		return seats;
	}

	/**
	 * Finds the largest row in the whole First Class. 
	 * Continues to use recursion to find amountOfPeople worth of seats.
	 * @param list Available Seats data structure.
	 * @param amountOfPeople Amount of people the function is looking for.
	 * @return a ArrayList of strings with keys or an empty ArrayList if no seats could be found.
	 */
	private ArrayList<String> recurseFindLargestFirstRow(TreeMap<String, Boolean> list, int amountOfPeople) {

		ArrayList<String> seats = new ArrayList<>();
		if (amountOfPeople <= 0) {
			return seats;
		}
		for (int i = firstRowMin; i <= firstRowMax; i++) {
			ArrayList<String> temp = adjacentSeatsInRow(list, i);
			if (seats.size() < temp.size()) {
				seats = temp;
				if (temp.size() >= amountOfPeople) {
					break;
				}
			}
		}
		while (seats.size() > amountOfPeople) {
			seats.remove(seats.size() - 1);
		}

		if (seats.size() == 0) {
			return seats;
		}
		for (String key : seats) {
			list.replace(key, false);
		}
		seats.addAll(recurseFindLargestFirstRow(list, amountOfPeople - seats.size()));
		return seats;
	}

	/**
	 * Finds the largest row in the whole Economy Class. 
	 * Continues to use recursion to find amountOfPeople worth of seats.
	 * @param list Available Seats data structure.
	 * @param amountOfPeople Amount of people the function is looking for.
	 * @return a ArrayList of strings with keys or an empty ArrayList if no seats could be found.
	 */
	private ArrayList<String> recurseFindLargestEconRow(TreeMap<String, Boolean> list, int amountOfPeople) {

		ArrayList<String> seats = new ArrayList<>();

		if (amountOfPeople <= 0) {
			return seats;
		}

		for (int i = economyRowMin; i <= economyRowMax; i++) {
			ArrayList<String> temp = adjacentSeatsInRow(list, i);
			if (seats.size() < temp.size()) {
				seats = temp;
				if (temp.size() >= amountOfPeople) {
					break;
				}
			}
		}
		while (seats.size() > amountOfPeople) {
			seats.remove(seats.size() - 1);
		}
		if (seats.size() == 0) {
			return seats;
		}

		for (String key : seats) {
			list.replace(key, false);
		}

		seats.addAll(recurseFindLargestEconRow(list, amountOfPeople - seats.size()));
		return seats;
	}
	/**
	 * Looks at row and find thes largest amount of adjacent seats together.
	 * @param list Available Seats data structure.
	 * @param row The row looked at
	 * @return ArrayList of Strings that hold seat keys of the adjacent seats.
	 */
	public ArrayList<String> adjacentSeatsInRow(TreeMap<String, Boolean> list, int row) {
		ArrayList<String> seats = new ArrayList<String>();
		int counter = 0;
		int maxCounter = -1;
		int indexStart = 0;
		int tempStart = 0;
		String rows[];
		if (row >= firstRowMin || row <= firstRowMax) {
			rows = firstSeats;
		} else if (row >= economyRowMin || row <= economyRowMax) {
			rows = economySeats;
		} else {
			rows = null;
		}
		if (rows == null) {
			System.out.println("Wrong Row Input");
		}
		for (int i = 0; i < rows.length; i++) {
			String key = rows[i] + "" + row;

			if (list.get(key)) {
				if (tempStart < 0)
					tempStart = i;
				counter++;
				if (maxCounter < counter) {
					indexStart = tempStart;
					maxCounter = counter;
				}
			} else {
				tempStart = -1;
				counter = 0;
			}
		}
		maxCounter += indexStart;
		for (int i = indexStart; i < maxCounter; i++) {
			String key = rows[i] + "" + row;
			seats.add(key);
		}
		return seats;
	}
}
