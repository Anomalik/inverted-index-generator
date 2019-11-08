import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * A storage / data structure class that stores an index and word counts
 */
public class InvertedIndex {
	/**
	 * A TreeMap storage structure  that holds a stemmed word mapped to a TreeMap of
	 * Files the stemmed word is found in, which is then mapped to locations within
	 * said file
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;

	/**
	 * A TreeMap storage structure that holds a file mapped to the amount of words
	 * found in that file
	 */
	private final TreeMap<String, Integer> fileWordCounts;

	/**
	 * Class constructor that handles initializing invertedIndex and fileWordCounts
	 */
	public InvertedIndex() {
		this.invertedIndex = new TreeMap<>();
		this.fileWordCounts = new TreeMap<>();
	}

	/**
	 * Adds a stemmed word, along with file it was found in and location within
	 * that file to invertedIndex.
	 *
	 * @param word 			word to add to invertedIndex
	 * @param location   	location word was found in
	 * @param position   	location within location word was found in
	 */
	public void add(String word, String location, int position) {
		invertedIndex.putIfAbsent(word, new TreeMap<>());
		invertedIndex.get(word).putIfAbsent(location, new TreeSet<>());

		if (invertedIndex.get(word).get(location).add(position)) {
			Integer count;
			if ((count = fileWordCounts.putIfAbsent(location, 1)) != null) {
				fileWordCounts.put(location, count + 1);
			}
		}
	}

	/**
	 * Writes invertedIndex as pretty JSON to destination defined
	 * by path. Calls SimpleJsonWriter in order to do so.
	 *
	 * @param path file location to write to
	 * @throws IOException if file is not found are able to be written to
	 * @see SimpleJsonWriter#asInvertedIndex(TreeMap, Path)
	 */
	public void writeIndex(Path path) throws IOException {
		SimpleJsonWriter.asInvertedIndex(invertedIndex, path);
	}

	/**
	 * Writes fileWordCounts as pretty JSON to destination defined
	 * by path. Calls SimpleJsonWriter in order to do so.
	 *
	 * @param path file location to write to
	 * @throws IOException if file is not found are able to be written to
	 * @see SimpleJsonWriter#asObject(Map, Path)
	 */
	public void writeCounts(Path path) throws IOException {
		SimpleJsonWriter.asObject(fileWordCounts, path);
	}

	/**
	 * Checks if a word exists in the inverted index
	 *
	 * @param word key being checked for in invertedIndex
	 * @return true if value is found, false if it is not found
	 */
	public boolean contains(String word) {
		return invertedIndex.containsKey(word);
	}

	/**
	 * Checks if a location exists for word in the invertedIndex
	 *
	 * @param word key being looked into in invertedIndex
	 * @param location key within word, that is being looked for
	 * @return true if value is found, false if it is not found
	 */
	public boolean contains(String word, String location) {
		if (contains(word)) {
			return invertedIndex.get(word).containsKey(location);
		}
		return false;
	}

	/**
	 * Checks if a position is stored, for a location that's for
	 * word.
	 *
	 * @param word key being looked into in invertedIndex
	 * @param location key within word, that is being looked for
	 * @param position value being looked for within location
	 * @return true if value is found, false if it is not found
	 */
	public boolean contains(String word, String location, int position) {
		if (contains(word, location)) {
			return invertedIndex.get(word).get(location).contains(position);
		}
		return false;
	}
	
	/**
	 * Gets the set of keys in the inverted index.
	 *
	 * @return unmodifiable collection of keys (words) in inverted index
	 */
	public Collection<String> get() {
		return Collections.unmodifiableSet(invertedIndex.keySet());
	}

	/**
	 * Get the set of locations for a specific word in invertedIndex
	 *
	 * @param word key where set will be returned from
	 * @return unmodifiable set of locations for word
	 */
	public Collection<String> get(String word) {
		if (contains(word)) {
			return Collections.unmodifiableSet(invertedIndex.get(word).keySet());
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * Get the positions for a word found within a location
	 *
	 * @param word key where set will be returned from
	 * @param location where the positions are found in
	 * @return unmodifiable set of positions for word in location
	 */
	public Collection<Integer> get(String word, String location) {
		if (contains(word, location)) {
			return Collections.unmodifiableSet(invertedIndex.get(word).get(location));
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * Gets the word count for location stored in fileWordCounts
	 *
	 * @param location key in fileWordCounts that will have its Integer value returned
	 * @return mapped Integer value for location if it exits, otherwise null
	 */
	public Integer getWordCount(String location) {
		return fileWordCounts.getOrDefault(location, 0);
	}

	/**
	 * Creates a list of results objects that contain locations words from searchQuery
	 * were found in, number of occurrences, and a score generated for that result. This
	 * method searches the invertedIndex already generated to generate data, looking for
	 * exact word matches.
	 *
	 * @param searchQuery list of strings that are being searched for
	 * @return list of result objects, containing relevant result data
	 */
	public ArrayList<Result> getExactResults(Query searchQuery) {
		ArrayList<Result> results = new ArrayList<>();

		for (String location : fileWordCounts.keySet()) {
			Result newResult = new Result();

			newResult.put("where", String.valueOf(location).replace("\\", "/"));

			int count = 0;
			for (String searchWord : searchQuery.get()) {
				count += this.get(searchWord, location).size();
			}

			newResult.put("count", String.valueOf(count));

			double score = (double)count/fileWordCounts.get(location);
			String scoreFormatted = String.format("%.8f", score);
			newResult.put("score", scoreFormatted);

			if (count > 0) {
				results.add(newResult);
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * Creates a list of results objects that contain locations words from searchQuery
	 * were found in, number of occurrences, and a score generated for that result. This
	 * method searches the invertedIndex already generated to generate data, looking for
	 * partial word matches.
	 *
	 * @param searchQuery list of strings that are being searched for
	 * @return list of result objects, containing relevant result data
	 */
	public ArrayList<Result> getPartialResults(Query searchQuery) {
		ArrayList<Result> results = new ArrayList<>();

		for (String location : fileWordCounts.keySet()) {
			Result newResult = new Result();

			newResult.put("where", String.valueOf(location).replace("\\", "/"));

			int count = 0;
			for (String searchWord : searchQuery.get()) {
				for (String word : this.get()) {
					if (word.startsWith(searchWord) && this.contains(word, location)) {
						count += this.get(word, location).size();
					}
				}
			}

			newResult.put("count", String.valueOf(count));

			double score = (double)count/fileWordCounts.get(location);
			String scoreFormatted = String.format("%.8f", score);
			newResult.put("score", scoreFormatted);

			if (count > 0) {
				results.add(newResult);
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * Prints the invertedIndex and fileWordCounts to stdout.
	 *
	 * @return string version of object's data structures
	 */
	public String toString() {
		try {
			return "Inverted Index: \n" + SimpleJsonWriter.asInvertedIndex(invertedIndex) + "\n\nFile Word Counts: \n" + SimpleJsonWriter.asObject(fileWordCounts);
		} catch (IOException e) {
			System.out.println("Error printing inverted index and word count for locations.");
		}

		return "Error printing inverted index and word count for locations";
	}
}
