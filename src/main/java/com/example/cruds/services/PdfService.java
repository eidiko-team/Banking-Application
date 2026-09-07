package com.example.cruds.services;

import com.example.cruds.dto.AccountResponseDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {


//    Read the entire PDF
    public String readPdf(
            MultipartFile file
    ) throws IOException {

        try (
                PDDocument document = PDDocument.load(file.getInputStream())
        ) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }



//    Read Page by Page
    public List<String> readPdfPageByPage(
            MultipartFile file
    ) throws IOException {

        List<String> pagesText = new ArrayList<>();

        try (
                PDDocument document = PDDocument.load(file.getInputStream())
        ) {

            int totalPages = document.getNumberOfPages();

            PDFTextStripper pdfTextStripper = new PDFTextStripper();


            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {

                pdfTextStripper.setStartPage(
                        pageNumber
                );

                pdfTextStripper.setEndPage(
                        pageNumber
                );

                String pageText = pdfTextStripper.getText(
                                document
                        );

                pagesText.add(
                        pageText
                );
            }
        }

        return pagesText;
    }





//    Read Accounts Pdf and Convert to Object
    public List<AccountResponseDTO> readAccountsPdfAndconvertToObject(
            MultipartFile file
    ) throws IOException {

        List<AccountResponseDTO> accounts = new ArrayList<>();

        try (
                PDDocument document = PDDocument.load(file.getInputStream())
        ) {

            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            String extractedText = pdfTextStripper.getText(document);

            System.out.println(extractedText);


            // Split PDF text into lines
            String[] lines = extractedText.split("\\r?\\n");


            // Start from 1 to skip header
            for (int i = 1; i < lines.length; i++) {

                String line = lines[i].trim();


                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }


                // Split row values
                String[] values = line.split("\\s+");


                AccountResponseDTO account = new AccountResponseDTO();


                account.setAccountId(
                        Long.valueOf(values[0])
                );

                account.setAccountNumber(
                        values[1]
                );

                account.setAccountType(
                        values[2]
                );

                account.setBalance(
                        Double.parseDouble(values[3])
                );

                account.setStatus(
                        values[4]
                );

                account.setCustomerId(
                        Long.valueOf(values[5])
                );


                accounts.add(account);
            }
        }

        return accounts;
    }




//    simple format to write into pdf
    public byte[] generateAccountPdf(AccountResponseDTO account)
            throws IOException {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            // Create A4 page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Open content stream
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

//            Font
            PDType1Font font = PDType1Font.HELVETICA;

            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;


            // =========================
            // TITLE
            // =========================

            contentStream.beginText();
            contentStream.setFont(boldFont, 20);
            contentStream.newLineAtOffset(180, 750);
            contentStream.showText("BANKING APPLICATION");
            contentStream.endText();


            // =========================
            // ACCOUNT DETAILS
            // =========================

            contentStream.beginText();
            contentStream.setFont(boldFont, 16);
            contentStream.newLineAtOffset(200, 700);
            contentStream.showText("ACCOUNT DETAILS");
            contentStream.endText();


            // Account ID
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText(
                    "Account ID : " + account.getAccountId()
            );
            contentStream.endText();


            // Account Number
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 620);
            contentStream.showText(
                    "Account Number : " + account.getAccountNumber()
            );
            contentStream.endText();


            // Account Type
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 590);
            contentStream.showText(
                    "Account Type : " + account.getAccountType()
            );
            contentStream.endText();


            // Balance
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 560);
            contentStream.showText(
                    "Balance : " + account.getBalance()
            );
            contentStream.endText();


            // Customer ID
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 530);
            contentStream.showText(
                    "Customer ID : " + account.getCustomerId()
            );
            contentStream.endText();


            // Close content stream
            contentStream.close();

            // Convert PDF to byte[]
            document.save(outputStream);

            return outputStream.toByteArray();
        }
}


    //    Converting to Table mostly manual
//    public byte[] generateAllAccountsPdf(List<AccountResponseDTO> accounts)
//            throws IOException {
//
//        try (
//                PDDocument document = new PDDocument();
//                ByteArrayOutputStream outputStream =
//                        new ByteArrayOutputStream()
//        ) {
//
//            // Create A4 page
//            PDPage page = new PDPage(PDRectangle.A4);
//            document.addPage(page);
//
//            // Open content stream
//            PDPageContentStream contentStream =
//                    new PDPageContentStream(document, page);
//
////            Font
//            PDType1Font font = PDType1Font.HELVETICA;
//
//            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;
//
//
//            int startX = 40;
//            int startY = 600;
//            int rowHeight = 40;
//
//            //calculate no of rows
//            int rows = accounts.size()+1;
//
//            //calculate the table widths
//            int columns = accounts.get(0).getClass().getDeclaredFields().length;
//            System.out.println("COLUMNS********** "+columns);
//            int width = 80;
//            int totalWidth = columns*width;
//            System.out.println("TOTAL WIDTH********** "+totalWidth);
//
//
//
//
//            for(int i=0;i<=rows;i++){
//                int y = startY-(i*rowHeight);
//
//                contentStream.moveTo(startX,y);
//                contentStream.lineTo(startX+totalWidth,y);
//            }
//
//            int x = 0;
//            for(int i=0;i<columns;i++){
//                x = startX+(i*width);
//                contentStream.moveTo(x,startY);
//                contentStream.lineTo(x,startY-(rows*rowHeight));
//            }
//
//            contentStream.moveTo(x+width,startY);
//            contentStream.lineTo(x+width,startY-(rows*rowHeight));
//
//
//            // Draw all the lines
//            contentStream.stroke();
//
//            //Write Headers
//
//            contentStream.beginText();
//
//            contentStream.setFont(
//                    PDType1Font.HELVETICA_BOLD,
//                    12
//            );
//
//            String[] headers = {
//                    "Account ID",
//                    "Account Number",
//                    "Account Type",
//                    "Balance",
//                    "Status",
//                    "Customer ID"
//            };
//
//           for(int i=0;i<headers.length;i++){
//               int textX = startX+(i*width)+5;
//               int textY = startY-20;
//               contentStream.newLineAtOffset(textX,textY);
//               contentStream.showText(headers[i]);
//
//               contentStream.newLineAtOffset(-textX,-textY);
//           }
//
//           contentStream.endText();
//
//
//            contentStream.beginText();
//
//            contentStream.setFont(
//                    PDType1Font.HELVETICA,
//                    10
//            );
//
//            for (int row = 0; row < accounts.size(); row++) {
//
//                AccountResponseDTO account = accounts.get(row);
//
//                String[] values = {
//                        String.valueOf(account.getAccountId()),
//                        String.valueOf(account.getAccountNumber()),
//                        String.valueOf(account.getAccountType()),
//                        String.valueOf(account.getBalance()),
//                        String.valueOf(account.getStatus()),
//                        String.valueOf(account.getCustomerId())
//                };
//
//                for (int column = 0; column < values.length; column++) {
//
//                    float textX = startX + (column * width) + 5;
//
//                    // +1 because row 0 is the header
//                    float textY = startY
//                            - ((row + 1) * rowHeight)
//                            - 25;
//
//                    contentStream.newLineAtOffset(textX, textY);
//
//                    contentStream.showText(values[column]);
//
//                    // Return to the original text position
//                    contentStream.newLineAtOffset(-textX, -textY);
//                }
//            }
//
//            contentStream.endText();
//
//
//
//
//            contentStream.close();
//
//            // Save PDF
//            document.save(outputStream);
//
//            return outputStream.toByteArray();
//        }
//    }



//same method above but dynamic except width
public byte[] generateAllAccountsPdf(
        List<AccountResponseDTO> accounts
) throws IOException, IllegalAccessException {

    try (
            PDDocument document = new PDDocument();
            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream()
    ) {

        // Create A4 page
        PDPage page = new PDPage(PDRectangle.A4);

        //to know the size of the page rest in the sping doc in the doc section 72points equals to 1 inch
        float pageWidth = PDRectangle.A4.getWidth();
        float pageHeight = PDRectangle.A4.getHeight();

        document.addPage(page);


        // Open content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);


        // =========================
        // TABLE SETTINGS
        // =========================

        int startX = 40;
        int startY = 600;
        int rowHeight = 40;


        // =========================
        // GET FIELDS DYNAMICALLY
        // =========================

        Field[] fields =
                accounts
                        .get(0)
                        .getClass()
                        .getDeclaredFields();


        // Number of columns
        int columns = fields.length;


        // Number of rows
        int rows = accounts.size() + 1;


        // Width of each column
        int width = 80;


        // Total table width
        int totalWidth = columns * width;


        // =========================
        // DRAW HORIZONTAL LINES
        // =========================

        for (int i = 0; i <= rows; i++) {

            int y = startY - (i * rowHeight);

            contentStream.moveTo(startX, y);

            contentStream.lineTo(
                    startX + totalWidth,
                    y
            );
        }


        // =========================
        // DRAW VERTICAL LINES
        // =========================

        int x = startX;

        for (int i = 0; i <= columns; i++) {

            x = startX + (i * width);

            contentStream.moveTo(x, startY);

            contentStream.lineTo(x, startY - (rows * rowHeight)
            );
        }


        // Draw all lines
        contentStream.stroke();


        // =========================
        // WRITE HEADERS DYNAMICALLY
        // =========================

        contentStream.beginText();

        contentStream.setFont(
                PDType1Font.HELVETICA_BOLD,
                10
        );


        for (int column = 0; column < fields.length; column++) {

            Field field = fields[column];

            String header = formatHeader(field.getName());

            float textX = startX + (column * width) + 5;

            float textY = startY - 25;

            contentStream.newLineAtOffset(
                    textX,
                    textY
            );

            contentStream.showText(
                    header
            );

            // Move back
            //newLineAtOffset(-textX, -textY) moves the text position back by the opposite offset,
            //effectively undoing the previous movement.
            contentStream.newLineAtOffset(
                    -textX,
                    -textY
            );
        }

        contentStream.endText();

        // =========================
        // WRITE DATA DYNAMICALLY
        // =========================

        contentStream.beginText();

        contentStream.setFont(
                PDType1Font.HELVETICA,
                10
        );

        for (int row = 0; row < accounts.size(); row++) {

            AccountResponseDTO account = accounts.get(row);

            for (int column = 0; column < fields.length; column++) {

                Field field = fields[column];

                // Allow access to private field
                field.setAccessible(true);

                // Get field value dynamically
                Object value = field.get(account);

                String text = value != null ? value.toString() : "";

                float textX = startX + (column * width) + 5;

                float textY = startY - ((row + 1) * rowHeight) - 25;

                contentStream.newLineAtOffset(
                        textX,
                        textY
                );

                contentStream.showText(
                        text
                );

                // Move back to origin
                contentStream.newLineAtOffset(
                        -textX,
                        -textY
                );
            }
        }

        contentStream.endText();

        contentStream.close();

        // Save PDF
        document.save(outputStream);

        return outputStream.toByteArray();
    }
}

    private String formatHeader(
            String fieldName
    ) {

        return fieldName
                .replaceAll(
                        "([a-z])([A-Z])",
                        "$1 $2"
                )
                .toUpperCase();
    }




    public byte[] generateMultiPageAccountsPdf(
            List<AccountResponseDTO> accounts
    ) throws IOException {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            float margin = 50;
            float startY = 790;
            float bottomMargin = 50;
            float rowHeight = 30;

            PDPage page = new PDPage(PDRectangle.A4);

            document.addPage(page);

            PDPageContentStream contentStream =
                    new PDPageContentStream(
                            document,
                            page
                    );

            float yPosition = startY;


            // =========================
            // WRITE ALL ACCOUNTS
            // =========================

            for (AccountResponseDTO account : accounts) {

                // Check if there is space
                if (yPosition - rowHeight < bottomMargin) {

                    // Close current page stream
                    contentStream.close();


                    // Create new page
                    page = new PDPage(PDRectangle.A4);

                    document.addPage(page);


                    // Create new content stream
                    contentStream =
                            new PDPageContentStream(
                                    document,
                                    page
                            );


                    // Reset Y position
                    yPosition = startY;
                }


                // =========================
                // WRITE ACCOUNT
                // =========================

                contentStream.beginText();

                contentStream.setFont(
                        PDType1Font.HELVETICA,
                        12
                );

                contentStream.newLineAtOffset(
                        margin,
                        yPosition
                );

                contentStream.showText(
                        "Account ID: "
                                + account.getAccountId()
                                + " | "
                                + "Account Number: "
                                + account.getAccountNumber()
                );

                contentStream.endText();


                // Move down for next account
                yPosition -= rowHeight;
            }


            // Close final page stream
            contentStream.close();


            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }
}


//    public void generateAccountPdf(
//            AccountResponseDTO account,
//            OutputStream outputStream
//    ) throws IOException {
//
//        try (PDDocument document = new PDDocument()) {
//
//            PDPage page = new PDPage(PDRectangle.A4);
//            document.addPage(page);
//
//            try (PDPageContentStream contentStream =
//                         new PDPageContentStream(document, page)) {
//
//                contentStream.beginText();
//
//                contentStream.setFont(
//                        PDType1Font.HELVETICA_BOLD,
//                        20
//                );
//
//                contentStream.newLineAtOffset(180, 750);
//
//                contentStream.showText("BANKING APPLICATION");
//
//                contentStream.endText();
//
//
//                contentStream.beginText();
//
//                contentStream.setFont(
//                        PDType1Font.HELVETICA,
//                        12
//                );
//
//                contentStream.newLineAtOffset(100, 650);
//
//                contentStream.showText(
//                        "Account ID : " + account.getAccountId()
//                );
//
//                contentStream.endText();
//            }
//
//            // Directly write PDF to browser response
//            document.save(outputStream);
//        }
//    }
//    private static final float MARGIN = 50;
//    private static final float START_Y = 790;
//    private static final float BOTTOM_MARGIN = 50;
//    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
//
//    public byte[] generateCustomerPdf(CustomerResponseDTO customer)
//            throws IOException {
//
//        try (
//                PDDocument document = new PDDocument();
//                ByteArrayOutputStream outputStream =
//                        new ByteArrayOutputStream()
//        ) {
//
//            PdfContext context = createNewPage(document);
//
//            // PDF TITLE
//
//            context = writeTitle(
//                    context,
//                    "BANKING APPLICATION"
//            );
//
//            context = writeTitle(
//                    context,
//                    "CUSTOMER DETAILS REPORT"
//            );
//
//            context.yPosition -= 15;
//
//
//            // =====================================
//            // CUSTOMER INFORMATION
//            // =====================================
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    150
//            );
//
//            context = writeHeading(
//                    context,
//                    "CUSTOMER INFORMATION"
//            );
//
//            context = writeLine(
//                    context,
//                    "Customer ID : " + safe(customer.getCustomerId())
//            );
//
//            context = writeLine(
//                    context,
//                    "Name : " + safe(customer.getName())
//            );
//
//            context = writeLine(
//                    context,
//                    "Email : " + safe(customer.getEmail())
//            );
//
//            context = writeLine(
//                    context,
//                    "Phone : " + safe(customer.getPhone())
//            );
//
//            context = writeLine(
//                    context,
//                    "Address : " + safe(customer.getAddress())
//            );
//
//
//            // =====================================
//            // ACCOUNT DETAILS
//            // =====================================
//
//            context.yPosition -= 15;
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    80
//            );
//
//            context = writeHeading(
//                    context,
//                    "ACCOUNT DETAILS"
//            );
//
//            context = drawAccountTable(
//                    document,
//                    context,
//                    customer.getAccounts()
//            );
//
//
//            // =====================================
//            // KYC DETAILS
//            // =====================================
//
//            context.yPosition -= 20;
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    120
//            );
//
//            context = writeHeading(
//                    context,
//                    "KYC DETAILS"
//            );
//
//            KycResponseDTO kyc = customer.getKyc();
//
//            if (kyc != null) {
//
//                context = writeLine(
//                        context,
//                        "Aadhaar Number : "
//                                + maskAadhaar(
//                                kyc.getAadhaarNumber()
//                        )
//                );
//
//                context = writeLine(
//                        context,
//                        "PAN Number : "
//                                + maskPan(
//                                kyc.getPanNumber()
//                        )
//                );
//
//                context = writeLine(
//                        context,
//                        "Verification Status : "
//                                + safe(
//                                kyc.getVerificationStatus()
//                        )
//                );
//
//            } else {
//
//                context = writeLine(
//                        context,
//                        "KYC details not available"
//                );
//            }
//
//
//            // =====================================
//            // LOAN OFFERS
//            // =====================================
//
//            context.yPosition -= 20;
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    80
//            );
//
//            context = writeHeading(
//                    context,
//                    "LOAN OFFERS"
//            );
//
//            context = drawLoanOfferTable(
//                    document,
//                    context,
//                    customer.getLoanOffers()
//            );
//
//
//            // =====================================
//            // LOAN DETAILS
//            // =====================================
//
//            context.yPosition -= 20;
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    80
//            );
//
//            context = writeHeading(
//                    context,
//                    "LOAN DETAILS"
//            );
//
//            context = drawLoanTable(
//                    document,
//                    context,
//                    customer.getLoans()
//            );
//
//
//            // CLOSE CURRENT CONTENT STREAM
//
//            context.contentStream.close();
//
//            // SAVE PDF
//
//            document.save(outputStream);
//
//            return outputStream.toByteArray();
//        }
//    }
//
//
//    // =====================================
//    // CREATE NEW PAGE
//    // =====================================
//
//    private PdfContext createNewPage(
//            PDDocument document
//    ) throws IOException {
//
//        PDPage page = new PDPage(PDRectangle.A4);
//
//        document.addPage(page);
//
//        PDPageContentStream contentStream =
//                new PDPageContentStream(
//                        document,
//                        page
//                );
//
//        return new PdfContext(
//                page,
//                contentStream,
//                START_Y
//        );
//    }
//
//
//    // =====================================
//    // CHECK SPACE
//    // =====================================
//
//    private PdfContext ensureSpace(
//            PDDocument document,
//            PdfContext context,
//            float requiredSpace
//    ) throws IOException {
//
//        if (context.yPosition - requiredSpace
//                < BOTTOM_MARGIN) {
//
//            context.contentStream.close();
//
//            context = createNewPage(document);
//        }
//
//        return context;
//    }
//
//
//    // =====================================
//    // ACCOUNT TABLE
//    // =====================================
//
//    private PdfContext drawAccountTable(
//            PDDocument document,
//            PdfContext context,
//            List<AccountResponseDTO> accounts
//    ) throws IOException {
//
//        if (accounts == null || accounts.isEmpty()) {
//
//            return writeLine(
//                    context,
//                    "No accounts found"
//            );
//        }
//
//        float rowHeight = 25;
//
//        float[] columnWidths = {
//                45,
//                135,
//                90,
//                100,
//                75
//        };
//
//        String[] headers = {
//                "ID",
//                "Account Number",
//                "Type",
//                "Balance",
//                "Status"
//        };
//
//
//        // DRAW HEADER
//
//        context = ensureSpace(
//                document,
//                context,
//                rowHeight * 2
//        );
//
//        context = drawTableHeader(
//                context,
//                headers,
//                columnWidths,
//                rowHeight
//        );
//
//
//        // DRAW ACCOUNT ROWS
//
//        for (AccountResponseDTO account : accounts) {
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    rowHeight
//            );
//
//            // If a new page was created,
//            // print table header again
//
//            if (context.yPosition == START_Y) {
//
//                context = drawTableHeader(
//                        context,
//                        headers,
//                        columnWidths,
//                        rowHeight
//                );
//            }
//
//            String[] row = {
//                    safe(account.getAccountId()),
//                    safe(account.getAccountNumber()),
//                    safe(account.getAccountType()),
//                    safe(account.getBalance()),
//                    safe(account.getStatus())
//            };
//
//            context = drawTableRow(
//                    context,
//                    row,
//                    columnWidths,
//                    rowHeight
//            );
//        }
//
//        return context;
//    }
//
//
//    // =====================================
//    // LOAN OFFER TABLE
//    // =====================================
//
//    private PdfContext drawLoanOfferTable(
//            PDDocument document,
//            PdfContext context,
//            List<LoanProductResponseDTO> products
//    ) throws IOException {
//
//        if (products == null || products.isEmpty()) {
//
//            return writeLine(
//                    context,
//                    "No loan offers available"
//            );
//        }
//
//        float rowHeight = 25;
//
//        float[] columnWidths = {
//                60,
//                180,
//                100,
//                110
//        };
//
//        String[] headers = {
//                "ID",
//                "Product Name",
//                "Interest %",
//                "Max Tenure"
//        };
//
//
//        // DRAW HEADER
//
//        context = ensureSpace(
//                document,
//                context,
//                rowHeight * 2
//        );
//
//        context = drawTableHeader(
//                context,
//                headers,
//                columnWidths,
//                rowHeight
//        );
//
//
//        // DRAW PRODUCT ROWS
//
//        for (LoanProductResponseDTO product : products) {
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    rowHeight
//            );
//
//            if (context.yPosition == START_Y) {
//
//                context = drawTableHeader(
//                        context,
//                        headers,
//                        columnWidths,
//                        rowHeight
//                );
//            }
//
//            String[] row = {
//                    safe(product.getProductId()),
//                    safe(product.getProductName()),
//                    safe(product.getInterestRate()) + "%",
//                    safe(product.getMaxTenureMonths()) + " Months"
//            };
//
//            context = drawTableRow(
//                    context,
//                    row,
//                    columnWidths,
//                    rowHeight
//            );
//        }
//
//        return context;
//    }
//
//
//    // =====================================
//    // LOAN TABLE
//    // =====================================
//
//    private PdfContext drawLoanTable(
//            PDDocument document,
//            PdfContext context,
//            List<LoanResponseDTO> loans
//    ) throws IOException {
//
//        if (loans == null || loans.isEmpty()) {
//
//            return writeLine(
//                    context,
//                    "No loans found"
//            );
//        }
//
//        float rowHeight = 25;
//
//        float[] columnWidths = {
//                45,
//                100,
//                95,
//                80,
//                80,
//                70
//        };
//
//        String[] headers = {
//                "ID",
//                "Loan Type",
//                "Amount",
//                "Interest",
//                "Tenure",
//                "Status"
//        };
//
//
//        // DRAW HEADER
//
//        context = ensureSpace(
//                document,
//                context,
//                rowHeight * 2
//        );
//
//        context = drawTableHeader(
//                context,
//                headers,
//                columnWidths,
//                rowHeight
//        );
//
//
//        // DRAW LOAN ROWS
//
//        for (LoanResponseDTO loan : loans) {
//
//            context = ensureSpace(
//                    document,
//                    context,
//                    rowHeight
//            );
//
//            if (context.yPosition == START_Y) {
//
//                context = drawTableHeader(
//                        context,
//                        headers,
//                        columnWidths,
//                        rowHeight
//                );
//            }
//
//            String[] row = {
//                    safe(loan.getLoanId()),
//                    safe(loan.getLoanType()),
//                    safe(loan.getLoanAmount()),
//                    safe(loan.getInterestRate()) + "%",
//                    safe(loan.getTenureMonths()) + " Months",
//                    safe(loan.getStatus())
//            };
//
//            context = drawTableRow(
//                    context,
//                    row,
//                    columnWidths,
//                    rowHeight
//            );
//        }
//
//        return context;
//    }
//
//
//    // =====================================
//    // DRAW TABLE HEADER
//    // =====================================
//
//    private PdfContext drawTableHeader(
//            PdfContext context,
//            String[] headers,
//            float[] columnWidths,
//            float rowHeight
//    ) throws IOException {
//
//        return drawTableRow(
//                context,
//                headers,
//                columnWidths,
//                rowHeight,
//                true
//        );
//    }
//
//
//    // =====================================
//    // DRAW NORMAL TABLE ROW
//    // =====================================
//
//    private PdfContext drawTableRow(
//            PdfContext context,
//            String[] values,
//            float[] columnWidths,
//            float rowHeight
//    ) throws IOException {
//
//        return drawTableRow(
//                context,
//                values,
//                columnWidths,
//                rowHeight,
//                false
//        );
//    }
//
//
//    // =====================================
//    // DRAW TABLE ROW
//    // =====================================
//
//    private PdfContext drawTableRow(
//            PdfContext context,
//            String[] values,
//            float[] columnWidths,
//            float rowHeight,
//            boolean isHeader
//    ) throws IOException {
//
//        float xPosition = MARGIN;
//
//        for (int i = 0; i < values.length; i++) {
//
//            context.contentStream.addRect(
//                    xPosition,
//                    context.yPosition - rowHeight,
//                    columnWidths[i],
//                    rowHeight
//            );
//
//            context.contentStream.stroke();
//
//            context.contentStream.beginText();
//
//            if (isHeader) {
//
//                context.contentStream.setFont(
//                        PDType1Font.HELVETICA_BOLD,
//                        9
//                );
//
//            } else {
//
//                context.contentStream.setFont(
//                        PDType1Font.HELVETICA,
//                        9
//                );
//            }
//
//            context.contentStream.newLineAtOffset(
//                    xPosition + 5,
//                    context.yPosition - 16
//            );
//
//            context.contentStream.showText(
//                    truncate(
//                            safe(values[i]),
//                            getMaxLength(columnWidths[i])
//                    )
//            );
//
//            context.contentStream.endText();
//
//            xPosition += columnWidths[i];
//        }
//
//        context.yPosition -= rowHeight;
//
//        return context;
//    }
//
//
//    // =====================================
//    // WRITE TITLE
//    // =====================================
//
//    private PdfContext writeTitle(
//            PdfContext context,
//            String text
//    ) throws IOException {
//
//        context.contentStream.beginText();
//
//        context.contentStream.setFont(
//                PDType1Font.HELVETICA_BOLD,
//                18
//        );
//
//        context.contentStream.newLineAtOffset(
//                MARGIN,
//                context.yPosition
//        );
//
//        context.contentStream.showText(
//                safe(text)
//        );
//
//        context.contentStream.endText();
//
//        context.yPosition -= 30;
//
//        return context;
//    }
//
//
//    // =====================================
//    // WRITE SECTION HEADING
//    // =====================================
//
//    private PdfContext writeHeading(
//            PdfContext context,
//            String text
//    ) throws IOException {
//
//        context.contentStream.beginText();
//
//        context.contentStream.setFont(
//                PDType1Font.HELVETICA_BOLD,
//                14
//        );
//
//        context.contentStream.newLineAtOffset(
//                MARGIN,
//                context.yPosition
//        );
//
//        context.contentStream.showText(
//                safe(text)
//        );
//
//        context.contentStream.endText();
//
//        context.yPosition -= 25;
//
//        return context;
//    }
//
//
//    // =====================================
//    // WRITE NORMAL LINE
//    // =====================================
//
//    private PdfContext writeLine(
//            PdfContext context,
//            String text
//    ) throws IOException {
//
//        context.contentStream.beginText();
//
//        context.contentStream.setFont(
//                PDType1Font.HELVETICA,
//                11
//        );
//
//        context.contentStream.newLineAtOffset(
//                MARGIN,
//                context.yPosition
//        );
//
//        context.contentStream.showText(
//                truncate(
//                        safe(text),
//                        90
//                )
//        );
//
//        context.contentStream.endText();
//
//        context.yPosition -= 20;
//
//        return context;
//    }
//
//
//    // =====================================
//    // MASK AADHAAR
//    // =====================================
//
//    private String maskAadhaar(String aadhaar) {
//
//        if (aadhaar == null || aadhaar.length() < 4) {
//            return "N/A";
//        }
//
//        String lastFour =
//                aadhaar.substring(
//                        aadhaar.length() - 4
//                );
//
//        return "XXXX XXXX " + lastFour;
//    }
//
//
//    // =====================================
//    // MASK PAN
//    // =====================================
//
//    private String maskPan(String pan) {
//
//        if (pan == null || pan.length() < 4) {
//            return "N/A";
//        }
//
//        return pan.substring(0, 3)
//                + "******"
//                + pan.substring(
//                pan.length() - 1
//        );
//    }
//
//
//    // =====================================
//    // SAFE NULL HANDLING
//    // =====================================
//
//    private String safe(Object value) {
//
//        return value == null
//                ? "N/A"
//                : String.valueOf(value);
//    }
//
//
//    // =====================================
//    // TEXT LENGTH BASED ON COLUMN WIDTH
//    // =====================================
//
//    private int getMaxLength(
//            float columnWidth
//    ) {
//
//        return (int) (columnWidth / 5);
//    }
//
//
//    // =====================================
//    // TRUNCATE TEXT
//    // =====================================
//
//    private String truncate(
//            String text,
//            int maxLength
//    ) {
//
//        if (text == null) {
//            return "N/A";
//        }
//
//        if (text.length() <= maxLength) {
//            return text;
//        }
//
//        if (maxLength <= 3) {
//            return text.substring(
//                    0,
//                    maxLength
//            );
//        }
//
//        return text.substring(
//                0,
//                maxLength - 3
//        ) + "...";
//    }
//
//
//    // =====================================
//    // PDF CONTEXT
//    // =====================================
//
//    private static class PdfContext {
//
//        private PDPage page;
//
//        private PDPageContentStream contentStream;
//
//        private float yPosition;
//
//        public PdfContext(
//                PDPage page,
//                PDPageContentStream contentStream,
//                float yPosition
//        ) {
//
//            this.page = page;
//            this.contentStream = contentStream;
//            this.yPosition = yPosition;
//        }
//    }

