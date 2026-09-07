package com.example.cruds.zpractise;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExample {

    public static void main(String[] args) {

        try (
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream =
                        new FileOutputStream("accounts.xlsx")
        ) {

            // Create Sheet
            Sheet sheet = workbook.createSheet("Accounts");

            // Create Header Row
            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Balance");

            // Create First Data Row
            Row row1 = sheet.createRow(1);

            row1.createCell(0).setCellValue(1);
            row1.createCell(1).setCellValue("Ram");
            row1.createCell(2).setCellValue(5000);

            // Create Second Data Row
            Row row2 = sheet.createRow(2);

            row2.createCell(0).setCellValue(2);
            row2.createCell(1).setCellValue("John");
            row2.createCell(2).setCellValue(10000);

            // Create Third Data Row
            Row row3 = sheet.createRow(3);

            row3.createCell(0).setCellValue(3);
            row3.createCell(1).setCellValue("Shalini");
            row3.createCell(2).setCellValue(15000);

            // Write workbook data into file
            workbook.write(outputStream);

            System.out.println("Excel file created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}