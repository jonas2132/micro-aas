package com.softwareag.app.data;

public enum DataType {
    AASX("aasx"), 
    JSON("json");

    private String formatString;

    DataType(String formatString) {
        this.formatString = formatString;
    }

    public String getFormatString() {
        return formatString;
    }

}
