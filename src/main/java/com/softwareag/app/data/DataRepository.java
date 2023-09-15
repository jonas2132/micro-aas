package com.softwareag.app.data;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;

public interface DataRepository {
    public Environment read();
    public void write(Environment environment);
}
