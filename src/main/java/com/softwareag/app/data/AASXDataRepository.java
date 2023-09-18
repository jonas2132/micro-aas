/**
 * AASXDataRepository - A utility class for reading and writing digital twin data in AASX format.
 * This class provides methods to deserialize and serialize digital twin environment data using
 * the Eclipse Digital Twin AAS4J library with AASX format support.
 *
 * Features:
 * - Read digital twin environment data from an AASX file.
 * - Write digital twin environment data to an AASX file.
 *
 * Usage:
 * 1. Use the `AASXReading` method to read digital twin environment data from an AASX file.
 * 2. Use the `AASXWriting` method to write digital twin environment data to a new AASX file.
 *
 * Dependencies:
 * - Eclipse Digital Twin AAS4J library (org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx)
 * - Eclipse Digital Twin AAS4J library (org.eclipse.digitaltwin.aas4j.v3.model)
 *
 * Note:
 * - Ensure that the Eclipse Digital Twin AAS4J library is included in your project.
 * - The file paths for input and output files are configured within the class.
 *
 * Example:
 * ```
 * // Reading digital twin environment data from an AASX file
 * Environment env = AASXDataRepository.AASXReading();
 *
 * // Modifying the environment data as needed
 *
 * // Writing the modified environment data to a new AASX file
 * AASXDataRepository.AASXWriting(env);
 * ```
 *
 * @author [jrud&DevAZK]
 * @version 1.0
 */
package com.softwareag.app.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.DeserializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXSerializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;

public class AASXDataRepository implements DataRepository{

    // Static variables for file paths
    private final String workingDir = System.getProperty("user.dir");
    private final String resourceDir = workingDir + "/src/main/resources";
    private final String outputDir = workingDir + "/output";
    //private final String inputFileName = "CarbonFootprint_v.03.aasx";
    //private final String outputFileName = "CarbonFootprint_Output_v.03.aasx";

    /**
     * Read digital twin environment data from an AASX file.
     *
     * @return The Environment object containing the read data, or null if an error occurs.
     */
    public  Environment read(String inputFilename) {
        File inputFile = new File(resourceDir + "/" + inputFilename);
        System.out.println("Reading from the file: " + inputFile);

        try {
            InputStream in = new FileInputStream(inputFile);
            AASXDeserializer deserializer = new AASXDeserializer(in);
            Environment env = deserializer.read();
            return env;

        } catch (FileNotFoundException e) {
            System.err.println("Error: The specified file was not found. Please check the file path and try again.");
            e.printStackTrace();
            return null;
        } catch (InvalidFormatException e) {
            System.err.println("Error: There is an InvalidFormatException.");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.err.println("Error: There is an IOException.");
            e.printStackTrace();
            return null;
        } catch (DeserializationException e) {
            System.err.println("Error: There is a DeserializationException.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write digital twin environment data to a new AASX file.
     *
     * @param env The Environment object containing the data to be written.
     */
    public void write(Environment env, String outputFilename) {
        File outputFile = new File(outputDir + "/" + outputFilename);


        final List<InMemoryFile> fileList = new ArrayList<>();
        // Example of adding additional files to the AASX, if needed:
        // byte[] operationManualContent = { 0, 1, 2, 3, 4 };
        // InMemoryFile file = new InMemoryFile(operationManualContent, "Draft_PCF_Submodel.pdf");
        // fileList.add(file);

        try (OutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            AASXSerializer serializer = new AASXSerializer();
            serializer.write(env, fileList, fileOutputStream);
            System.out.println("Successfully wrote on Output file.");
        } catch (SerializationException | IOException e) {
            // Handle the exceptions here
            System.err.println("Failed to write on the output file.");
            e.printStackTrace(); // Printing the stack trace
        }
    }
}
