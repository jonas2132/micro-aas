package com.softwareag.app.data;


//Controller-Service Repository Pattern

import com.softwareag.app.service.EnvironmentService;


public interface DataRepository {
    public abstract EnvironmentService read(String path);
    public abstract void write(EnvironmentService environment, String outputFilename);
}
