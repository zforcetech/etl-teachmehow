package com.teachmehow.reader;

import com.teachmehow.model.File;
import com.teachmehow.model.Row;
import org.apache.commons.io.FileUtils;

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


    public void readZip() {

        try {
            ZipFile zipFile = new ZipFile("/home/lapras/Documents/test_zip1.zip");
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            List<File> images = new ArrayList<>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains(".png") || entry.getName().contains(".jpg") ||
                        entry.getName().contains(".jpeg")) {
                    File file = readImage(zipFile, entry);
                    if (file != null)
                        images.add(file);
                } else if (entry.getName().contains(".csv"))
                    readCSV(zipFile, entry);
            }
        } catch (Exception e) {
            LOGGER.info("Error while reading zip file : " + e.getMessage());
        }
    }

    private void readCSV(ZipFile zipFile, ZipEntry entry) {
        InputStream stream = zipFile.getInputStream(entry);
        LOGGER.info(entry.getName());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        List<Row> rows = bufferedReader.lines()
                .map(this::prepareRow)
                .collect(toList());
    }

    private Row prepareRow(String rowString) {
        String[] columns = rowString.split(";");
        Row row = new Row();
        row.setColumn1(columns[0]);
        row.setColumn2(columns[1]);
        row.setColumn3(columns[2]);
        row.setColumn4(columns[3]);
        row.setColumn5(columns[4]);
        row.setColumn6(columns[5]);
        row.setColumn7(columns[6]);
        row.setColumn8(columns[7]);
        row.setColumn9(columns[8]);
        row.setColumn10(columns[9]);
        row.setColumn11(columns[10]);
        row.setColumn12(columns[11]);
        row.setColumn13(columns[12]);
        row.setColumn14(columns[13]);
        row.setColumn15(columns[14]);
        return row;
    }

    // Handles image
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
