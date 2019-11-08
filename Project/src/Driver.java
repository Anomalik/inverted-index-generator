import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class Driver {
	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		// store initial start time
		Instant start = Instant.now();
		ArgumentParser parser = new ArgumentParser(args);
		InvertedIndex index = new InvertedIndex();
		InvertedIndexGenerator generator = new InvertedIndexGenerator(index);
		TreeMap<Query, ArrayList<Result>> searchResults = new TreeMap<>();
		ArrayList<Query> searchQueries = new ArrayList<>();


		if (parser.hasFlag("-path") && parser.hasValue("-path")) {
			Path path = parser.getPath("-path");
			try {
				generator.build(path);
			} catch (IOException e) {
				System.out.println("Unable to generate index from path: " + path);
			}
		}

		if (parser.hasFlag("-index")) {
			Path path = parser.getPath("-index", Path.of("index.json"));
			try {
				index.writeIndex(path);
			} catch (IOException e) {
				System.out.println("Unable to write index to file at: " + path);
			}
		}

		if (parser.hasFlag("-counts")) {
			Path path = parser.getPath("-counts", Path.of("counts.json"));
			try {
				index.writeCounts(path);
			} catch (IOException e) {
				System.out.println("Unable to write counts to file at: " + path);
			}
		}

		if (parser.hasFlag("-query") && parser.hasValue("-query")) {
			try {
				searchQueries = QueriesGenerator.uniqueQueryStems(parser.getPath("-query"));
			} catch (IOException e) {
				System.out.println("Query file (" + parser.getPath("-query") + ") could not be read. ");
			}
		}

		if (parser.hasFlag("-results")) {
			Path path = parser.getPath("-results", Path.of("results.json"));

			if (parser.hasFlag("-exact")) {
				for (Query searchQuery : searchQueries) {
					searchResults.put(searchQuery, index.getExactResults(searchQuery));
				}
			} else {
				for (Query searchQuery : searchQueries) {
					searchResults.put(searchQuery, index.getPartialResults(searchQuery));
				}
			}

			try {
				SimpleJsonWriter.asNestedSearchIndex(searchResults, path);
			} catch (IOException e) {
				System.out.println("Unable to write search results to file at: " + path);
			}
		}

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}