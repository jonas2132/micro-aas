package com.softwareag.app.data;


import java.io.File;

//Controller-Service Repository Pattern

import com.softwareag.app.service.EnvironmentService;


public interface DataRepository {
    public abstract EnvironmentService read(File inputFile);
    public abstract void write(EnvironmentService environment, String outputFilename);
}
