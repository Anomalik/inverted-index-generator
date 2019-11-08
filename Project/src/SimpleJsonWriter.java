import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Outputs several simple data structures in "pretty" JSON format where
 * newlines are used to separate elements and nested elements are indented.
 * <p>
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SimpleJsonWriter {
	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void asArray(Collection<Integer> elements, Writer writer, int level) throws IOException {
		writer.write("[");
		var iterator = elements.iterator();

		if (iterator.hasNext()) {
			writer.write("\n");
			indent(iterator.next(), writer, level + 1);
		}

		while (iterator.hasNext()) {
			writer.write(",\n");
			indent(iterator.next(), writer, level + 1);
		}

		writer.write("\n");
		indent("]", writer, level);
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException when file specified in writer can not be found or written to
	 * @see #asArray(Collection, Writer, int)
	 */
	public static void asArray(Collection<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 * @throws IOException if file is not able to written to
	 * @see #asArray(Collection, Writer, int)
	 */
	public static String asArray(Collection<Integer> elements) throws IOException {
		StringWriter writer = new StringWriter();
		asArray(elements, writer, 0);
		return writer.toString();
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void asObject(Map<String, Integer> elements, Writer writer, int level) throws IOException {
		writer.write("{");
		var iterator = elements.entrySet().iterator();

		if (iterator.hasNext()) {
			writeEntry(iterator.next(), writer, level + 1);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writeEntry(iterator.next(), writer, level + 1);
		}

		writer.write('\n');
		indent("}", writer, level - 1);
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if file is not able to be written to
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asObject(Map<String, Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 * @throws IOException if file is not able to be written to
	 * @see #asObject(Map, Writer, int)
	 */
	public static String asObject(Map<String, Integer> elements) throws IOException {
		StringWriter writer = new StringWriter();
		asObject(elements, writer, 0);
		return writer.toString();
	}

	/**
	 * Writes the elements as a nested pretty JSON object. The generic notation used
	 * allows this method to be used for any type of map with any type of nested
	 * collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Writer writer, int level) throws IOException {
		writer.write("{");
		var iterator = elements.entrySet().iterator();

		if (iterator.hasNext()) {
			writeNestedEntry(iterator.next(), writer, level + 1);
		}

		while (iterator.hasNext()) {
			writer.write(",");
			writeNestedEntry(iterator.next(), writer, level + 1);
		}

		writer.write('\n');
		indent("}", writer, level - 1);
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if file is not able to be written to
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 * @throws IOException if file is not able to be written to
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static String asNestedObject(Map<String, ? extends Collection<Integer>> elements) throws IOException {
		StringWriter writer = new StringWriter();
		asNestedObject(elements, writer, 0);
		return writer.toString();
	}

	/**
	 * Writes the {@code \t} tab symbol by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void indent(Writer writer, int times) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException when file specified in writer can not be found or written to
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException {
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException when file specified in writer can not be found or written to
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException when file specified in writer can not be found or written to
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException {
		indent(writer, times);
		quote(element, writer);
	}

	/**
	 * Writes a specific map entry found within map object passed in through elements.
	 *
	 * @param entry  the entry to write
	 * @param writer the writer to use
	 * @param level  level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void writeEntry(Map.Entry<String, Integer> entry, Writer writer, int level) throws IOException {
		writer.write('\n');
		quote(entry.getKey(), writer, level);
		writer.write(": ");
		writer.write(entry.getValue().toString());
	}

	/**
	 * Writes a specific map entry found within map object passed in through elements.
	 *
	 * @param entry  the entry to write
	 * @param writer the writer to use
	 * @param level  level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void writeNestedEntry(Map.Entry<String, ? extends Collection<Integer>> entry, Writer writer, int level) throws IOException {
		writer.write('\n');
		quote(entry.getKey(), writer, level);
		writer.write(": ");
		asArray(entry.getValue(), writer, level + 1);
	}

	/**
	 * Writes the elements as an inverted index to a json file
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Writer writer, int level) throws IOException {
		writer.write("{");
		var iterator = elements.entrySet().iterator();

		if (iterator.hasNext()) {
			Map.Entry<String, TreeMap<String, TreeSet<Integer>>> entry = iterator.next();
			writer.write('\n');
			quote(entry.getKey(), writer, level + 1);
			writer.write(": ");
			asNestedObject(entry.getValue(), writer, level + 1);
		}

		while (iterator.hasNext()) {
			Map.Entry<String, TreeMap<String, TreeSet<Integer>>> entry = iterator.next();
			writer.write(",\n");
			quote(entry.getKey(), writer, level + 1);
			writer.write(": ");
			asNestedObject(entry.getValue(), writer, level + 1);
		}

		writer.write('\n');
		indent("}", writer, level);
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if file is not able to be written to
	 * @see #asInvertedIndex(TreeMap, Writer, int)
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asInvertedIndex(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 * @throws IOException if file is not able to be written to
	 * @see #asInvertedIndex(TreeMap, Writer, int)
	 */
	public static String asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements) throws IOException {
		StringWriter writer = new StringWriter();
		asInvertedIndex(elements, writer, 0);
		return writer.toString();
	}

	/**
	 * asNestedSearchResults helper method. This handles writing out results for one query.
	 *
	 * @param elements 		list of result objects needed to be written out
	 * @param writer   		the writer to use
	 * @param level    		level which the writer will indent
	 * @throws IOException 	when file specified in writer can not be found or written to
	 */
	public static void asSearchResults (ArrayList<Result> elements, Writer writer, int level) throws IOException {
		var iterator = elements.iterator();

		if (iterator.hasNext()) {
			Result searchResult = iterator.next();
			writer.write("\n");
			indent("{\n", writer, level);
			indent("\"where\": " + "\"" + searchResult.get("where") + "\",\n", writer, level + 1);
			indent("\"count\": " + searchResult.get("count") + ",\n", writer, level + 1);
			indent("\"score\": " + searchResult.get("score") + "\n", writer, level + 1);
			indent("}", writer, level);
		}

		while (iterator.hasNext()) {
			Result searchResult = iterator.next();
			writer.write(",\n");
			indent("{\n", writer, level);
			indent("\"where\": " + "\"" + searchResult.get("where") + "\",\n", writer, level + 1);
			indent("\"count\": " + searchResult.get("count") + ",\n", writer, level + 1);
			indent("\"score\": " + searchResult.get("score") + "\n", writer, level + 1);
			indent("}", writer, level);
		}
	}

	/**
	 * Writes the elements as a nestedSearchIndex to a json file
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    level which the writer will indent
	 * @throws IOException when file specified in writer can not be found or written to
	 */
	public static void asNestedSearchIndex (TreeMap<Query, ArrayList<Result>> elements, Writer writer, int level) throws IOException {
		writer.write("{");
		var iterator = elements.entrySet().iterator();

		if (iterator.hasNext()) {
			Map.Entry<Query, ArrayList<Result>> entry = iterator.next();
			writer.write("\n");
			quote(entry.getKey().toString(), writer, level + 1);
			writer.write(": [");
			asSearchResults(entry.getValue(), writer, level + 2);
			writer.write("\n");
			indent("]", writer, level + 1);
		}

		while (iterator.hasNext()) {
			Map.Entry<Query, ArrayList<Result>> entry = iterator.next();
			writer.write(",\n");
			quote(entry.getKey().toString(), writer, level + 1);
			writer.write(": [");
			asSearchResults(entry.getValue(), writer, level + 2);
			writer.write("\n");
			indent("]", writer, level + 1);
		}
		writer.write("\n}");
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if file is not able to be written to
	 * @see #asNestedSearchIndex(TreeMap, Writer, int)
	 */
	public static void asNestedSearchIndex(TreeMap<Query, ArrayList<Result>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedSearchIndex(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 * @throws IOException if file is not able to be written to
	 * @see #asNestedSearchIndex(TreeMap, Writer, int)
	 */
	public static String asNestedSearchIndex(TreeMap<Query, ArrayList<Result>> elements) throws IOException {
		StringWriter writer = new StringWriter();
		asNestedSearchIndex(elements, writer, 0);
		return writer.toString();
	}
}
