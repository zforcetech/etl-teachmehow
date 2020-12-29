package com.etl.model;

import java.util.List;

public class ZipResult {

    private List<Row> csvRows;
    private List<File> images;
    private String zipName;
    private List<String> instructions;

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public List<Row> getCsvRows() {
        return csvRows;
    }

    public void setCsvRows(List<Row> csvRows) {
        this.csvRows = csvRows;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
