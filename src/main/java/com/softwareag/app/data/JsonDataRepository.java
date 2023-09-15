/**
 * JsonDataRepository - A utility class for reading and writing digital twin data in JSON format.
 * This class provides methods to deserialize and serialize digital twin environment data using
 * the Eclipse Digital Twin AAS4J library.
 *
 * Features:
 * - Read digital twin environment data from a JSON file.
 * - Write digital twin environment data to a JSON file.
 *
 * Usage:
 * 1. Use the `JsonReading` method to read digital twin environment data from a JSON file.
 * 2. Use the `JsonWriting` method to write digital twin environment data to a JSON file.
 *
 * Dependencies:
 * - Eclipse Digital Twin AAS4J library (org.eclipse.digitaltwin.aas4j.v3.dataformat)
 * - Eclipse Digital Twin AAS4J library (org.eclipse.digitaltwin.aas4j.v3.model)
 *
 * Note:
 * - Ensure that the Eclipse Digital Twin AAS4J library is included in your project.
 * - The file paths for input and output files are configured within the class.
 *
 * Example:
 * ```
 * // Reading digital twin environment data from a JSON file
 * Environment env = JsonReading();
 *
 * // Modifying the environment data as needed
 *
 * // Writing the modified environment data to a new JSON file
 *   JsonWriting(env);
 * ```
 *
 * @author [jrud]
 * @version 1.0
 */
package com.softwareag.app.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.DeserializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;

public class JsonDataRepository {

    // Static variables for file paths
    private static String workingDir = System.getProperty("user.dir");
    private static String resourceDir = workingDir + "/src/main/resources";
    private static String outputDir = workingDir + "/output";
    private static String inputFileName = "CarbonFootprint_v.03.json";
    private static String outputFileName = "CarbonFootprint_Output_v.03.json";

    /**
     * Read digital twin environment data from a JSON file.
     *
     * @return The Environment object containing the read data, or null if an error occurs.
     */
    public static Environment JsonReading() {
        File inputFile = new File(resourceDir + "/" + inputFileName);

        System.out.println("Reading from the file: " + inputFile);

        try {
            InputStream in = new FileInputStream(inputFile);
            JsonDeserializer deserializer = new JsonDeserializer();

            Environment env = deserializer.read(in);

            return env;
        } catch (FileNotFoundException e) {
            System.err.println("Error: The specified file was not found. Please check the file path and try again.");
            e.printStackTrace();
            return null;
        } catch (DeserializationException e) {
            System.err.println("Error: There is a DeserializationException.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write digital twin environment data to a JSON file.
     *
     * @param env The Environment object containing the data to be written.
     */
    public static void JsonWriting(Environment env) {
        File outputFile = new File(outputDir + "/" + outputFileName);
        System.out.println("Writing to the file: " + outputFile);

        try {
            JsonSerializer serializer = new JsonSerializer();
            serializer.write(outputFile, env);
            System.out.println("Successfully wrote on Output file.");
        } catch (SerializationException | IOException e) {
            // Handle the exceptions here
            System.err.println("Failed to write on the output file.");
            e.printStackTrace(); // Printing the stack trace
        }
    }
}
