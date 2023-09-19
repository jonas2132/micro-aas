package com.softwareag.app.data;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
//Controller-Service Repository Pattern
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;


public interface DataRepository {
    public Environment read(String inputFilename);
    public void write(Environment environment, List<InMemoryFile> fileList, String outputFilename);
}
