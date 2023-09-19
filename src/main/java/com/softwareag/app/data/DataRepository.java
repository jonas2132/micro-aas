package com.softwareag.app.data;

//Controller-Service Repository Pattern
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;


public interface DataRepository {
    public Environment read(String inputFilename);
    public void write(Environment environment, String outputFilename);
}
