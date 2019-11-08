import java.util.HashMap;

/**
 * A storage data structure class that stores a search result
 */
public class Result implements Comparable<Result> {

	/**
	 * HashMap structure used to store details of a search result including location,
	 * score, and count
	 */
	private final HashMap<String, String> result;

	/**
	 * Constructor for result object
	 */
	public Result() {
		this.result = new HashMap<>();
	}

	/**
	 * Overridden compareTo method. Score has the most priority, then count, then where,
	 * which is based alphabetically.
	 *
	 * @param o result object to be compared to
	 * @return 1 if greater, 0 if equal, -1 if less than
	 */
	@Override
	public int compareTo(Result o) {
		if (Double.parseDouble(this.result.get("score")) > Double.parseDouble(o.result.get("score"))) {
			return -1;
		} else if (Double.parseDouble(this.result.get("score")) < Double.parseDouble(o.result.get("score"))) {
			return 1;
		} else {
			if (Integer.parseInt(this.result.get("count")) > Integer.parseInt(o.result.get("count"))) {
				return -1;
			} else if (Integer.parseInt(this.result.get("count")) < Integer.parseInt(o.result.get("count"))) {
				return 1;
			} else {
				return this.result.get("where").compareToIgnoreCase(o.result.get("where"));
			}
		}
	}

	/**
	 * Get method to return value of key in this.result
	 *
	 * @param key for value to be returned
	 * @return value associated with key
	 */
	public String get(String key) {
		return this.result.get(key);
	}

	/**
	 * Method to put a key value pair into this.result
	 *
	 * @param key key to put into this.result
	 * @param value mapped value to put into this.result
	 */
	public void put(String key, String value) {
		this.result.put(key, value);
	}

	/**
	 * Prints a string form of hash map to stdout.
	 *
	 * @return string version of data structure
	 */
	public String toString() {
		return result.toString();
	}
}

