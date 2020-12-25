package com.etl.reader;

import com.etl.model.File;
import com.etl.model.Row;
import com.etl.model.ZipResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.stream.Collectors.toList;

public class Reader {

    Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public ZipResult readZip(String path) {
        try {
            ZipFile zipFile = new ZipFile(path);
            return readZip(zipFile);
        } catch (Exception e) {
            LOGGER.severe("Error while reading zip file");
        }
        return null;
    }

    public ZipResult readZip(ZipFile zipFile) {

        try {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ZipResult zipResult = new ZipResult();
            List<File> images = new ArrayList<>();
            List<Row> csvRows = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains(".png") || entry.getName().contains(".jpg") ||
                        entry.getName().contains(".jpeg")) {
                    File file = readImage(zipFile, entry);
                    if (file != null)
                        images.add(file);
                } else if (entry.getName().contains(".csv")) {
                    csvRows = readCSV(zipFile, entry);
                }

            }
            if (csvRows == null)
                return null;
            zipResult.setImages(images);
            zipResult.setCsvRows(csvRows);
            return zipResult;
        } catch (Exception e) {
            LOGGER.info("Error while reading zip file : " + e.getMessage());
        }
        return null;
    }

    private List<Row> readCSV(ZipFile zipFile, ZipEntry entry) {
        InputStream stream = null;
        try {
            stream = zipFile.getInputStream(entry);
        } catch (Exception e) {
            try {
                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        if (stream != null) {
            LOGGER.info(entry.getName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            List<Row> rows =
                    bufferedReader.lines()
                            .map(this::prepareRow)
                            .collect(toList());
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rows;
        }
        return null;
    }

    private Row prepareRow(String rowString) {
        String[] columns = rowString.split(";");
        Row row = new Row();
        if (columns.length > 0)
            row.setColumn1(columns[0]);
        if (columns.length > 1)
            row.setColumn2(columns[1]);
        if (columns.length > 2)
            row.setColumn3(columns[2]);
        if (columns.length > 3)
            row.setColumn4(columns[3]);
        if (columns.length > 4)
            row.setColumn5(columns[4]);
        if (columns.length > 5)
            row.setColumn6(columns[5]);
        if (columns.length > 6)
            row.setColumn7(columns[6]);
        if (columns.length > 7)
            row.setColumn8(columns[7]);
        if (columns.length > 8)
            row.setColumn9(columns[8]);
        if (columns.length > 9)
            row.setColumn10(columns[9]);
        if (columns.length > 10)
            row.setColumn11(columns[10]);
        if (columns.length > 11)
            row.setColumn12(columns[11]);
        if (columns.length > 12)
            row.setColumn13(columns[12]);
        if (columns.length > 13)
            row.setColumn14(columns[13]);
        if (columns.length > 14)
            row.setColumn15(columns[14]);
        return row;
    }

    private File readImage(ZipFile zipFile, ZipEntry entry) {

        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = zipFile.getInputStream(entry);
            try {
                String imageFormat;
                if (entry.getName().contains(".png"))
                    imageFormat = "png";
                else if (entry.getName().contains(".jpg"))
                    imageFormat = "jpg";
                else imageFormat = "jpeg";
                LOGGER.info("Processing start for : " + entry.getName());
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, imageFormat, byteArrayOutputStream);
                String base64Image = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                LOGGER.info("Successfully processed : " + entry.getName());
                File image = new File();
                image.setData(base64Image);
                image.setFormat(imageFormat);
                image.setName(entry.getName());
                return image;
            } catch (IOException i2) {
                try {
                    byteArrayOutputStream.close();
                    inputStream.close();
                } catch (Exception e1) {
                }
                LOGGER.severe("Error while reading image from zip entry");
            }

        } catch (IOException i1) {
            try {
                inputStream.close();
            } catch (Exception e2) {
            }
            LOGGER.severe("Error while reading zip entry");
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e3) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e4) {
                }
            }
        }
        return null;
    }
}
