package com.teachmehow.model;

import java.util.List;

public class ZipResult {

    private List<Row> csvRows;
    private List<File> images;

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
}
