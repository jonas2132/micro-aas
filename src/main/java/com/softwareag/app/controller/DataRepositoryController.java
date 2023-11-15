package com.softwareag.app.controller;

import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.DataType;
import com.softwareag.app.data.JsonDataRepository;
import com.softwareag.app.service.EnvironmentService;

public class DataRepositoryController {
    private DataType dataType = null;
    private DataRepository currentDataRepository = null;

    public DataRepositoryController(DataType dataType) {
        setDataRepository(dataType);
    }

    public void setDataRepository(DataType dataType) {
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

    public DataRepository getCurrenDataRepository() {
        return currentDataRepository;
    }

    public DataType getCurrentDataType() {
        return dataType;
    }

    public void processData(double data, String fileName) {
        EnvironmentService envServ = currentDataRepository.read(fileName + (dataType == DataType.AASX ? ".aasx" : ".json"));
        //envServ.updateProperty("64280", "CarbonFootprint", SubmodelElementPropertyType.ZIPCODE, SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER);
        currentDataRepository.write(envServ, fileName + "_output" + (dataType == DataType.AASX ? ".aasx" : ".json"));
    }
}
