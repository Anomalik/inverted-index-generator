import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility Class used to traverse a file, stem words
 * within the file, and then return each eligible line
 * as a query object.
 */
public class QueriesGenerator {
	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Returns a set of unique (no duplicates) cleaned and stemmed words parsed
	 * from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of unique cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static TreeSet<String> uniqueStems(String line, Stemmer stemmer) {
		String[] parsedLine = TextParser.parse(line);
		TreeSet<String> stemmedLines = new TreeSet<>();
		for (String word : parsedLine) {
			stemmedLines.add((String) stemmer.stem(word));
		}
		return stemmedLines;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set. After doing this, method attempts to
	 * add stemmed set of word to list of query objects, if not already present.
	 *
	 * @param inputFile the input file to parse
	 * @return a sorted set of query objects
	 * @throws IOException if unable to read or parse file
	 *
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<Query> uniqueQueryStems(Path inputFile) throws IOException {
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		ArrayList<Query> stemmedLines = new ArrayList<>();

		try (
				BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)
		) {
			String line;

			while ((line = reader.readLine()) != null) {
				TreeSet<String> stemmedTreeSet = uniqueStems(line, stemmer);

				if (stemmedTreeSet.size() > 0) {
					Query currSearchLine = new Query();
					currSearchLine.addAll(stemmedTreeSet);

					boolean contains = false;
					for (Query queryElem : stemmedLines) {
						if (queryElem == currSearchLine) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						stemmedLines.add(currSearchLine);
					}
				}
			}
		}
		Collections.sort(stemmedLines);
		return stemmedLines;
	}
}
