import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Utility class for parsing and stemming text and text files
 * Writes stemmed word, files word was found, and location within file
 * to an InvertedIndex object.
 * Also determines word count of files at the same time and writes
 * that information to an InvertedIndex object.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 * <p>
 * Testing comment
 * @see TextParser
 */
public class InvertedIndexGenerator {
	/**
	 * InvertedIndex object used as a storage location to write
	 * invertedIndex and count to.
	 */
	private final InvertedIndex index;

	/**
	 * InvertedIndexGenerator constructor. Takes in an InvertedIndex
	 * object, and performs any build operations onto it.
	 *
	 * @param index InvertedIndex object all build operations will affect
	 */
	public InvertedIndexGenerator(InvertedIndex index) {
		this.index = index;
	}

	/**
	 * The default stemmer algorithm used by this class.
	 */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * This function iterates through every text file found in inputFile,
	 * and stems words within file. Stemmed word is then put into index,
	 * along with file it was found in, and location within the file.
	 * <p>
	 * At the same time, word counts for each file are determined and
	 * put into index.
	 *
	 * @param inputFile File path that is a inverted word index is being generated for
	 * @throws IOException if a file was not able to be read
	 */
	public void build(Path inputFile) throws IOException {
		for (Path file : TextFileFinder.list(inputFile)) {
			addFile(file, this.index);
		}
	}

	/**
	 * Adds the data of a file to the invertedIndex
	 *
	 * @param inputFile file whose data will be added to invertedIndex
	 * @param index index object inputFile's data will be added to
	 * @throws IOException if a file was not able to be read
	 */
	public static void addFile(Path inputFile, InvertedIndex index) throws IOException {
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		int position = 0;

		try (
				BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)
		) {
			String line;
			String inputFileName = inputFile.toString();
			while ((line = reader.readLine()) != null) {
				String[] parsedWords = TextParser.parse(line);

				for (String word : parsedWords) {
					++position;
					index.add(String.valueOf(stemmer.stem(word)), inputFileName, position);
				}
			}
		}
	}
}
