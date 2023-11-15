package com.softwareag.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.softwareag.app.controller.DataRepositoryController;
import com.softwareag.app.data.DataType;

import jakarta.servlet.http.HttpServletResponse;

public class DownloadService {

    public static void downloadEnvironment(EnvironmentService environmentService,
            String directory, DataRepositoryController dataRepositoryController, HttpServletResponse response) {

        String assetIDshort = environmentService.getAssetIDShort();
        String fileName = assetIDshort
                + (dataRepositoryController.getCurrentDataType() == DataType.AASX ? ".aasx" : ".json");

        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        dataRepositoryController.getCurrenDataRepository().write(environmentService, fileName);

        File outputFile = new File(directory + "/" + fileName);

        try (FileInputStream in = new FileInputStream(outputFile);
                OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputFile.delete();

    }

    public static void downloadEnvironments(List<EnvironmentService> environmentServices,
            String directory, DataRepositoryController dataRepositoryController, HttpServletResponse response) {

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=AAS_files.zip");

        try {
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            environmentServices.forEach(environmentService -> {

                String assetIDshort = environmentService.getAssetIDShort();
                String fileName = assetIDshort
                        + (dataRepositoryController.getCurrentDataType() == DataType.AASX ? ".aasx" : ".json");
                dataRepositoryController.getCurrenDataRepository().write(environmentService, fileName);

                try {
                    zipOut.putNextEntry(new ZipEntry("files/" + fileName));

                    File outputFile = new File(directory + "/" + fileName);

                    try (FileInputStream in = new FileInputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            zipOut.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    outputFile.delete();

                    zipOut.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            zipOut.finish();
            zipOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void downloadFile(String directory, String fileName,
            DataType datatype, HttpServletResponse response) {

        response.setContentType("application/" + datatype.getFormatString());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "." + datatype.getFormatString());

        File outputFile = new File(directory + "/" + fileName + "." +datatype.getFormatString());

        try (FileInputStream in = new FileInputStream(outputFile);
                OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //outputFile.delete();

    }

}
