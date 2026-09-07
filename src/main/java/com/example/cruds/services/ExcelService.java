package com.example.cruds.services;

import com.example.cruds.dto.AccountResponseDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {


    //Manual flow

//    public byte[] generateExcel(
//            List<AccountResponseDTO> accounts) {
//
//        try (
//                Workbook workbook = new XSSFWorkbook();
//                ByteArrayOutputStream outputStream =
//                        new ByteArrayOutputStream()
//        ) {
//
//            Sheet sheet = workbook.createSheet("Accounts");
//
//            // Header Row
//            Row headerRow = sheet.createRow(0);
//
//            headerRow.createCell(0)
//                    .setCellValue("Account ID");
//
//            headerRow.createCell(1)
//                    .setCellValue("Account Number");
//
//            headerRow.createCell(2)
//                    .setCellValue("Account Type");
//
//            headerRow.createCell(3)
//                    .setCellValue("Balance");
//
//
//            // Data Rows
//            int rowNum = 1;
//
//            for (AccountResponseDTO account : accounts) {
//
//                Row row = sheet.createRow(rowNum++);
//
//                row.createCell(0)
//                        .setCellValue(account.getAccountId());
//
//                row.createCell(1)
//                        .setCellValue(account.getAccountNumber());
//
//                row.createCell(2)
//                        .setCellValue(account.getAccountType());
//
//                row.createCell(3)
//                        .setCellValue(account.getBalance());
//            }
//
//
//            // Auto-size columns
//            for (int i = 0; i < 4; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//
//            // Write workbook into memory
//            workbook.write(outputStream);
//
//            // Convert Excel data to byte[]
//            return outputStream.toByteArray();
//
//        } catch (IOException e) {
//
//            throw new RuntimeException(
//                    "Unable to generate Excel file",
//                    e
//            );
//        }
//    }

    //Dynamic flow from reflection
        public byte[] generateExcel(
                List<AccountResponseDTO> accounts
        ) {

            try (
                    Workbook workbook = new XSSFWorkbook();
                    ByteArrayOutputStream outputStream =
                            new ByteArrayOutputStream()
            ) {

                Sheet sheet = workbook.createSheet("Accounts");

                // Get fields dynamically
                Field[] fields = AccountResponseDTO.class
                        .getDeclaredFields();


                // =========================
                // CREATE HEADER ROW
                // =========================

                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < fields.length; i++) {

                    headerRow
                            .createCell(i)
                            .setCellValue(fields[i].getName());
                }


                // =========================
                // CREATE DATA ROWS
                // =========================

                int rowNum = 1;

                for (AccountResponseDTO account : accounts) {

                    Row row = sheet.createRow(rowNum++);

                    for (int colNum = 0;
                         colNum < fields.length;
                         colNum++) {

                        Field field = fields[colNum];

                        field.setAccessible(true);

                        Object value = field.get(account);

                        if (value != null) {

                            row.createCell(colNum)
                                    .setCellValue(
                                            String.valueOf(value)
                                    );
                        }
                    }
                }


                // =========================
                // AUTO SIZE COLUMNS
                // =========================

                for (int i = 0; i < fields.length; i++) {
                    sheet.autoSizeColumn(i);
                }


                workbook.write(outputStream);

                return outputStream.toByteArray();

            } catch (IOException | IllegalAccessException e) {

                throw new RuntimeException(
                        "Unable to generate Excel file",
                        e
                );
            }
        }


        //with dynamic and type safety

//        public byte[] generateExcel(
//                List<AccountResponseDTO> accounts) {
//
//            try (
//                    Workbook workbook = new XSSFWorkbook();
//                    ByteArrayOutputStream outputStream =
//                            new ByteArrayOutputStream()
//            ) {
//
//                Sheet sheet = workbook.createSheet("Accounts");
//
//
//                // =========================
//                // GET DTO FIELDS
//                // =========================
//
//                Field[] fields = AccountResponseDTO.class
//                        .getDeclaredFields();
//
//
//                // =========================
//                // CREATE HEADER ROW
//                // =========================
//
//                Row headerRow = sheet.createRow(0);
//
//                for (int i = 0; i < fields.length; i++) {
//
//                    headerRow
//                            .createCell(i)
//                            .setCellValue(fields[i].getName());
//                }
//
//
//                // =========================
//                // CREATE DATA ROWS
//                // =========================
//
//                int rowNum = 1;
//
//                for (AccountResponseDTO account : accounts) {
//
//                    Row row = sheet.createRow(rowNum++);
//
//
//                    // Loop through every field
//                    for (int colNum = 0;
//                         colNum < fields.length;
//                         colNum++) {
//
//                        Field field = fields[colNum];
//
//                        field.setAccessible(true);
//
//                        Object value = field.get(account);
//
//                        Cell cell = row.createCell(colNum);
//
//
//                        // =========================
//                        // HANDLE DATA TYPES
//                        // =========================
//
//                        if (value == null) {
//
//                            cell.setCellValue("");
//
//                        } else if (value instanceof Number number) {
//
//                            cell.setCellValue(
//                                    number.doubleValue()
//                            );
//
//                        } else if (value instanceof Boolean bool) {
//
//                            cell.setCellValue(bool);
//
//                        } else {
//
//                            cell.setCellValue(
//                                    String.valueOf(value)
//                            );
//                        }
//                    }
//                }
//
//
//                // =========================
//                // AUTO SIZE COLUMNS
//                // =========================
//
//                for (int i = 0; i < fields.length; i++) {
//
//                    sheet.autoSizeColumn(i);
//                }
//
//
//                workbook.write(outputStream);
//
//                return outputStream.toByteArray();
//
//            } catch (IOException | IllegalAccessException e) {
//
//                throw new RuntimeException(
//                        "Unable to generate Excel file",
//                        e
//                );
//            }
//        }


        //Generating Excel Using the Generics works with any type
        public <T> byte[] generateExcel(
                List<T> data,
                String sheetName
        ) {

            try (
                    Workbook workbook = new XSSFWorkbook();
                    ByteArrayOutputStream outputStream =
                            new ByteArrayOutputStream()
            ) {

                Sheet sheet = workbook.createSheet(sheetName);


                // Handle empty list
                if (data == null || data.isEmpty()) {

                    workbook.write(outputStream);

                    return outputStream.toByteArray();
                }

                // Get the class of the first object
                Class<?> clazz = data.get(0).getClass();

                // Get fields dynamically
                Field[] fields = clazz.getDeclaredFields();

                // =========================
                // CREATE HEADER ROW
                // =========================

                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < fields.length; i++) {
                    headerRow
                            .createCell(i)
                            .setCellValue(
                                    formatHeader(fields[i].getName())
                            );
                }


                // =========================
                // CREATE DATA ROWS
                // =========================

                int rowNum = 1;

                for (T object : data) {

                    Row row = sheet.createRow(rowNum++);


                    for (int colNum = 0; colNum < fields.length; colNum++) {

                        Field field = fields[colNum];

//
//                        This tells Java:
//                        Allow reflection to access this private field.
//                        Without reflection, normally we would do:
//                        object.getAccountNumber();
//                        But this is a generic method.
//                        The code does not know in advance whether the field is:
//                        accountNumber
//                        balance
//                        customerName
//                        email
//                        loanAmount
//                        So instead of calling a specific getter, we use reflection.
                        field.setAccessible(true);

                        // field = accountNumber
                        // object = AccountResponseDTO
                        // From this object, get the value of this field.
                        Object value = field.get(object);

                        Cell cell = row.createCell(colNum);

                        // Handle null
                        if (value == null) {
                            cell.setCellValue("");

                        }

                        // Handle numbers
                        //Object value = 5000.0;

                        //if (value instanceof Number number) {
                        // number contains 5000.0 if true
                        // }
                        else if (value instanceof Number number) {

                            cell.setCellValue(
                                    number.doubleValue()
                            );
                        }

                        // Handle boolean
                        else if (value instanceof Boolean bool) {

                            cell.setCellValue(bool);
                        }

                        // Handle everything else
                        else {

                            cell.setCellValue(
                                    String.valueOf(value)
                            );
                        }
                    }
                }


                // =========================
                // AUTO SIZE COLUMNS
                // =========================

                for (int i = 0; i < fields.length; i++) {

                    sheet.autoSizeColumn(i);
                }


                workbook.write(outputStream);

                return outputStream.toByteArray();

            } catch (IOException |
                     IllegalAccessException e) {

                throw new RuntimeException(
                        "Unable to generate Excel file",
                        e
                );
            }
        }


    //Reading the data from the xl and putting that in the java object
    public List<AccountResponseDTO> readExcel(
            MultipartFile file
    ) {

        List<AccountResponseDTO> accounts = new ArrayList<>();

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {

            // Get first sheet
            Sheet sheet = workbook.getSheetAt(0);


            // Start from row 1
            // Row 0 contains headers
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

                Row row = sheet.getRow(rowNum);


                AccountResponseDTO account = new AccountResponseDTO();


                account.setAccountId(
                        (long) row.getCell(0)
                                .getNumericCellValue()
                );

                account.setAccountNumber(
                        row.getCell(1)
                                .getStringCellValue()
                );

                account.setAccountType(
                        row.getCell(2)
                                .getStringCellValue()
                );

                account.setBalance(
                        row.getCell(3)
                                .getNumericCellValue()
                );


                accounts.add(account);
            }


            return accounts;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read Excel file",
                    e
            );
        }
    }


        //Reading the file using the generics
    public <T> List<T> readExcel(
            MultipartFile file,
            Class<T> clazz
    ) {

        List<T> data = new ArrayList<>();

        try (
                InputStream inputStream = file.getInputStream();

                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet = workbook.getSheetAt(0);


            // =========================
            // READ HEADERS ONCE
            // =========================

            Row headerRow = sheet.getRow(0);

            Map<String, Integer> headerMap = new HashMap<>();


            for (Cell cell : headerRow) {

                String header = cell.getStringCellValue();

                headerMap.put(header, cell.getColumnIndex());
            }


            // =========================
            // GET JAVA FIELDS
            // =========================



            Field[] fields = clazz.getDeclaredFields();


            // =========================
            // READ DATA ROWS
            // =========================

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

                Row row = sheet.getRow(rowNum);

                if (row == null) {
                    continue;
                }


                // Create object
                T object = clazz.getDeclaredConstructor().newInstance();


                // =========================
                // READ EACH FIELD
                // =========================

                for (Field field : fields) {

                    String headerName = formatHeader(field.getName());

                    Integer columnIndex = headerMap.get(headerName);

                    Cell cell = row.getCell(columnIndex);


                    field.setAccessible(true);


                    setFieldValue(field, object, cell);
                }


                data.add(object);
            }


            return data;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read Excel file",
                    e
            );
        }
    }


    private <T> void setFieldValue(
            Field field,
            T object,
            Cell cell
    ) throws IllegalAccessException {

        Class<?> fieldType = field.getType();

        if (fieldType == String.class) {
            field.set(object, cell.getStringCellValue());
        }

        else if (fieldType == Long.class) {

            field.set(
                    object,
                    (long) cell.getNumericCellValue()
            );
        }

        else if (fieldType == Double.class) {

            field.set(
                    object,
                    cell.getNumericCellValue()
            );
        }

        else if (fieldType == Integer.class) {

            field.set(
                    object,
                    (int) cell.getNumericCellValue()
            );
        }

        else if (fieldType == Boolean.class) {

            field.set(
                    object,
                    cell.getBooleanCellValue()
            );
        }
    }

    private String formatHeader(String fieldName) {

//        ([a-z])([A-Z])
//        means:
//        Find a lowercase letter followed immediately by an uppercase letter.

        String formatted =
                fieldName.replaceAll(
                        "([a-z])([A-Z])",
                        "$1 $2"
                );

        return Character.toUpperCase(
                formatted.charAt(0)
        ) + formatted.substring(1);
    }


}