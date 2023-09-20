package com.softwareag.app.data;


//Controller-Service Repository Pattern

import com.softwareag.app.service.EnvironmentService;


public interface DataRepository {
    public EnvironmentService read(String inputFilename);
    public void write(EnvironmentService environment, String outputFilename);
}
