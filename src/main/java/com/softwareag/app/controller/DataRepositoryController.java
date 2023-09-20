package com.softwareag.app.controller;

import java.util.ArrayList;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;

import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.DataType;
import com.softwareag.app.data.JsonDataRepository;
import com.softwareag.app.service.EnvironmentService;

public class DataRepositoryController {
    private DataType dataType;
    private DataRepository currentDataRepository;

    public DataRepositoryController(DataType dataType) {
        this.dataType = dataType;
        switch(dataType) {
            case AASX:
            currentDataRepository = new AASXDataRepository();
            break;
            case JSON:
            currentDataRepository = new JsonDataRepository();
            break;
        }
    }

    public void processData(double data, String fileName) {
        EnvironmentService envServ = currentDataRepository.read(fileName + (dataType == DataType.AASX ? ".aasx" : ".json"));
        //envServ.updatePCFCO2eq(Double.toString(data));
        currentDataRepository.write(envServ, fileName + "_output" + (dataType == DataType.AASX ? ".aasx" : ".json"));
        envServ.printPCFCO2eq();
    }
}
