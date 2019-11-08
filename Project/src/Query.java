import java.util.ArrayList;
import java.util.TreeSet;

/**
 * A data structure class that stores a list of strings that will later
 * be used to conduct searches
 */
public class Query implements Comparable<Query> {

	/**
	 * ArrayList storage structure for words that will be sought after later
	 */
	private final ArrayList<String> searchWords;

	/**
	 * Constructor for query object
	 */
	public Query() {
		this.searchWords = new ArrayList<>();
	}

	/**
	 * Compares different query objects. Query objects are sorted based on
	 * an alphabetical order determined by the words within the object and the
	 * way they are sorted.
	 *
	 * @param o query object to be compared to
	 * @return 1 if query object is greater than, 0 if query objects are equal,
	 *     	   -1 if query object is less than
	 */
	@Override
	public int compareTo(Query o) {
		int max = Math.min(this.searchWords.size(), o.searchWords.size());

		String query1 = this.toString();
		String query2 = o.toString();

		return query1.compareToIgnoreCase(query2);
	}

	/**
	 * Prints the strings within the searchWords data structure
	 */
	public void print() {
		for (String word : this.searchWords) {
			System.out.println(word);
		}
	}

	/**
	 * Adds all elements from stemmedTreeSet to searchWords
	 *
	 * @param stemmedTreeSet TreeSet containing stemmed words from file
	 */
	public void addAll(TreeSet<String> stemmedTreeSet) {
		searchWords.addAll(stemmedTreeSet);
	}

	/**
	 * Get method to return this.searchWords
	 *
	 * @return ArrayList of strings in query object
	 */
	public ArrayList<String> get() {
		return this.searchWords;
	}

	/**
	 * Prints the list of strings in searchWords out in a bare format
	 * @return list of strings as one string
	 */
	public String toString() {
		if (this.searchWords.size() > 0) {
			StringBuilder queryWords = new StringBuilder(this.searchWords.get(0));

			for (int i = 1; i < this.searchWords.size(); i++) {
				queryWords.append(" ").append(this.searchWords.get(i));
			}
			return queryWords.toString();
		}
		return null;
	}
}