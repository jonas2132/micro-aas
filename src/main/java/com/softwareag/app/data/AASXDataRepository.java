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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.DeserializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.AASXSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;


import com.softwareag.app.service.EnvironmentService;
import com.softwareag.app.utils.Constants;

public class AASXDataRepository implements DataRepository{


    /**
     * Read digital twin environment data from an AASX file.
     *
     * @return The Environment object containing the read data, or null if an error occurs.
     */
    public EnvironmentService read(File inputFile) {
        System.out.println("Reading from file: " + inputFile);

        try {
            InputStream in = new FileInputStream(inputFile);
            AASXDeserializer deserializer = new AASXDeserializer(in);
            Environment env = deserializer.read();
            
            EnvironmentService envService = new EnvironmentService(env);
            //envService.setFilelist(deserializer.getRelatedFiles());
            return envService;

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
    public void write(EnvironmentService env, String outputFilename) {
        File folder = new File(Constants.OUTPUT_DIRECTORY + "/" + env.getAssetIDShort());
        if(!folder.exists())
            folder.mkdir();
        File outputFile = new File(Constants.OUTPUT_DIRECTORY + "/" + env.getAssetIDShort() + "/" + outputFilename);

        try{
        // Example of adding additional files to the AASX, if needed:
        // byte[] operationManualContent = { 0, 1, 2, 3, 4 };
        // InMemoryFile file = new InMemoryFile(operationManualContent, "Draft_PCF_Submodel.pdf");
        // fileList.add(file);

        //byte[] fileContent = Files.readAllBytes(Path.of(resourceDir + "/test.txt"));
        //fileList.add(0, new InMemoryFile(fileContent, resourceDir + "/test.txt"));

        try (OutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            AASXSerializer serializer = new AASXSerializer();
            serializer.write(env.getEnvironmentInstance(), env.getFileList(), fileOutputStream);
            System.out.println("Successfully wrote on output file (" + outputFilename +")");
        } catch (SerializationException | IOException e) {
            // Handle the exceptions here
            System.err.println("Failed to write on the output file.");
            e.printStackTrace(); // Printing the stack trace
        }
        }catch(Exception ex) { 
            //muss noch behoben werden
            //ex.printStackTrace(); 
        }
    }
}
