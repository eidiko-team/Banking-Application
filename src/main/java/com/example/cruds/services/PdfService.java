package com.example.cruds.services;

import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.dto.CustomerResponseDTO;
import com.example.cruds.dto.KycResponseDTO;
import com.example.cruds.dto.LoanProductResponseDTO;
import com.example.cruds.dto.LoanResponseDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    private static final float MARGIN = 50;
    private static final float START_Y = 790;
    private static final float BOTTOM_MARGIN = 50;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();

    public byte[] generateCustomerPdf(CustomerResponseDTO customer)
            throws IOException {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            PdfContext context = createNewPage(document);

            // PDF TITLE

            context = writeTitle(
                    context,
                    "BANKING APPLICATION"
            );

            context = writeTitle(
                    context,
                    "CUSTOMER DETAILS REPORT"
            );

            context.yPosition -= 15;


            // =====================================
            // CUSTOMER INFORMATION
            // =====================================

            context = ensureSpace(
                    document,
                    context,
                    150
            );

            context = writeHeading(
                    context,
                    "CUSTOMER INFORMATION"
            );

            context = writeLine(
                    context,
                    "Customer ID : " + safe(customer.getCustomerId())
            );

            context = writeLine(
                    context,
                    "Name : " + safe(customer.getName())
            );

            context = writeLine(
                    context,
                    "Email : " + safe(customer.getEmail())
            );

            context = writeLine(
                    context,
                    "Phone : " + safe(customer.getPhone())
            );

            context = writeLine(
                    context,
                    "Address : " + safe(customer.getAddress())
            );


            // =====================================
            // ACCOUNT DETAILS
            // =====================================

            context.yPosition -= 15;

            context = ensureSpace(
                    document,
                    context,
                    80
            );

            context = writeHeading(
                    context,
                    "ACCOUNT DETAILS"
            );

            context = drawAccountTable(
                    document,
                    context,
                    customer.getAccounts()
            );


            // =====================================
            // KYC DETAILS
            // =====================================

            context.yPosition -= 20;

            context = ensureSpace(
                    document,
                    context,
                    120
            );

            context = writeHeading(
                    context,
                    "KYC DETAILS"
            );

            KycResponseDTO kyc = customer.getKyc();

            if (kyc != null) {

                context = writeLine(
                        context,
                        "Aadhaar Number : "
                                + maskAadhaar(
                                kyc.getAadhaarNumber()
                        )
                );

                context = writeLine(
                        context,
                        "PAN Number : "
                                + maskPan(
                                kyc.getPanNumber()
                        )
                );

                context = writeLine(
                        context,
                        "Verification Status : "
                                + safe(
                                kyc.getVerificationStatus()
                        )
                );

            } else {

                context = writeLine(
                        context,
                        "KYC details not available"
                );
            }


            // =====================================
            // LOAN OFFERS
            // =====================================

            context.yPosition -= 20;

            context = ensureSpace(
                    document,
                    context,
                    80
            );

            context = writeHeading(
                    context,
                    "LOAN OFFERS"
            );

            context = drawLoanOfferTable(
                    document,
                    context,
                    customer.getLoanOffers()
            );


            // =====================================
            // LOAN DETAILS
            // =====================================

            context.yPosition -= 20;

            context = ensureSpace(
                    document,
                    context,
                    80
            );

            context = writeHeading(
                    context,
                    "LOAN DETAILS"
            );

            context = drawLoanTable(
                    document,
                    context,
                    customer.getLoans()
            );


            // CLOSE CURRENT CONTENT STREAM

            context.contentStream.close();

            // SAVE PDF

            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }


    // =====================================
    // CREATE NEW PAGE
    // =====================================

    private PdfContext createNewPage(
            PDDocument document
    ) throws IOException {

        PDPage page = new PDPage(PDRectangle.A4);

        document.addPage(page);

        PDPageContentStream contentStream =
                new PDPageContentStream(
                        document,
                        page
                );

        return new PdfContext(
                page,
                contentStream,
                START_Y
        );
    }


    // =====================================
    // CHECK SPACE
    // =====================================

    private PdfContext ensureSpace(
            PDDocument document,
            PdfContext context,
            float requiredSpace
    ) throws IOException {

        if (context.yPosition - requiredSpace
                < BOTTOM_MARGIN) {

            context.contentStream.close();

            context = createNewPage(document);
        }

        return context;
    }


    // =====================================
    // ACCOUNT TABLE
    // =====================================

    private PdfContext drawAccountTable(
            PDDocument document,
            PdfContext context,
            List<AccountResponseDTO> accounts
    ) throws IOException {

        if (accounts == null || accounts.isEmpty()) {

            return writeLine(
                    context,
                    "No accounts found"
            );
        }

        float rowHeight = 25;

        float[] columnWidths = {
                45,
                135,
                90,
                100,
                75
        };

        String[] headers = {
                "ID",
                "Account Number",
                "Type",
                "Balance",
                "Status"
        };


        // DRAW HEADER

        context = ensureSpace(
                document,
                context,
                rowHeight * 2
        );

        context = drawTableHeader(
                context,
                headers,
                columnWidths,
                rowHeight
        );


        // DRAW ACCOUNT ROWS

        for (AccountResponseDTO account : accounts) {

            context = ensureSpace(
                    document,
                    context,
                    rowHeight
            );

            // If a new page was created,
            // print table header again

            if (context.yPosition == START_Y) {

                context = drawTableHeader(
                        context,
                        headers,
                        columnWidths,
                        rowHeight
                );
            }

            String[] row = {
                    safe(account.getAccountId()),
                    safe(account.getAccountNumber()),
                    safe(account.getAccountType()),
                    safe(account.getBalance()),
                    safe(account.getStatus())
            };

            context = drawTableRow(
                    context,
                    row,
                    columnWidths,
                    rowHeight
            );
        }

        return context;
    }


    // =====================================
    // LOAN OFFER TABLE
    // =====================================

    private PdfContext drawLoanOfferTable(
            PDDocument document,
            PdfContext context,
            List<LoanProductResponseDTO> products
    ) throws IOException {

        if (products == null || products.isEmpty()) {

            return writeLine(
                    context,
                    "No loan offers available"
            );
        }

        float rowHeight = 25;

        float[] columnWidths = {
                60,
                180,
                100,
                110
        };

        String[] headers = {
                "ID",
                "Product Name",
                "Interest %",
                "Max Tenure"
        };


        // DRAW HEADER

        context = ensureSpace(
                document,
                context,
                rowHeight * 2
        );

        context = drawTableHeader(
                context,
                headers,
                columnWidths,
                rowHeight
        );


        // DRAW PRODUCT ROWS

        for (LoanProductResponseDTO product : products) {

            context = ensureSpace(
                    document,
                    context,
                    rowHeight
            );

            if (context.yPosition == START_Y) {

                context = drawTableHeader(
                        context,
                        headers,
                        columnWidths,
                        rowHeight
                );
            }

            String[] row = {
                    safe(product.getProductId()),
                    safe(product.getProductName()),
                    safe(product.getInterestRate()) + "%",
                    safe(product.getMaxTenureMonths()) + " Months"
            };

            context = drawTableRow(
                    context,
                    row,
                    columnWidths,
                    rowHeight
            );
        }

        return context;
    }


    // =====================================
    // LOAN TABLE
    // =====================================

    private PdfContext drawLoanTable(
            PDDocument document,
            PdfContext context,
            List<LoanResponseDTO> loans
    ) throws IOException {

        if (loans == null || loans.isEmpty()) {

            return writeLine(
                    context,
                    "No loans found"
            );
        }

        float rowHeight = 25;

        float[] columnWidths = {
                45,
                100,
                95,
                80,
                80,
                70
        };

        String[] headers = {
                "ID",
                "Loan Type",
                "Amount",
                "Interest",
                "Tenure",
                "Status"
        };


        // DRAW HEADER

        context = ensureSpace(
                document,
                context,
                rowHeight * 2
        );

        context = drawTableHeader(
                context,
                headers,
                columnWidths,
                rowHeight
        );


        // DRAW LOAN ROWS

        for (LoanResponseDTO loan : loans) {

            context = ensureSpace(
                    document,
                    context,
                    rowHeight
            );

            if (context.yPosition == START_Y) {

                context = drawTableHeader(
                        context,
                        headers,
                        columnWidths,
                        rowHeight
                );
            }

            String[] row = {
                    safe(loan.getLoanId()),
                    safe(loan.getLoanType()),
                    safe(loan.getLoanAmount()),
                    safe(loan.getInterestRate()) + "%",
                    safe(loan.getTenureMonths()) + " Months",
                    safe(loan.getStatus())
            };

            context = drawTableRow(
                    context,
                    row,
                    columnWidths,
                    rowHeight
            );
        }

        return context;
    }


    // =====================================
    // DRAW TABLE HEADER
    // =====================================

    private PdfContext drawTableHeader(
            PdfContext context,
            String[] headers,
            float[] columnWidths,
            float rowHeight
    ) throws IOException {

        return drawTableRow(
                context,
                headers,
                columnWidths,
                rowHeight,
                true
        );
    }


    // =====================================
    // DRAW NORMAL TABLE ROW
    // =====================================

    private PdfContext drawTableRow(
            PdfContext context,
            String[] values,
            float[] columnWidths,
            float rowHeight
    ) throws IOException {

        return drawTableRow(
                context,
                values,
                columnWidths,
                rowHeight,
                false
        );
    }


    // =====================================
    // DRAW TABLE ROW
    // =====================================

    private PdfContext drawTableRow(
            PdfContext context,
            String[] values,
            float[] columnWidths,
            float rowHeight,
            boolean isHeader
    ) throws IOException {

        float xPosition = MARGIN;

        for (int i = 0; i < values.length; i++) {

            context.contentStream.addRect(
                    xPosition,
                    context.yPosition - rowHeight,
                    columnWidths[i],
                    rowHeight
            );

            context.contentStream.stroke();

            context.contentStream.beginText();

            if (isHeader) {

                context.contentStream.setFont(
                        PDType1Font.HELVETICA_BOLD,
                        9
                );

            } else {

                context.contentStream.setFont(
                        PDType1Font.HELVETICA,
                        9
                );
            }

            context.contentStream.newLineAtOffset(
                    xPosition + 5,
                    context.yPosition - 16
            );

            context.contentStream.showText(
                    truncate(
                            safe(values[i]),
                            getMaxLength(columnWidths[i])
                    )
            );

            context.contentStream.endText();

            xPosition += columnWidths[i];
        }

        context.yPosition -= rowHeight;

        return context;
    }


    // =====================================
    // WRITE TITLE
    // =====================================

    private PdfContext writeTitle(
            PdfContext context,
            String text
    ) throws IOException {

        context.contentStream.beginText();

        context.contentStream.setFont(
                PDType1Font.HELVETICA_BOLD,
                18
        );

        context.contentStream.newLineAtOffset(
                MARGIN,
                context.yPosition
        );

        context.contentStream.showText(
                safe(text)
        );

        context.contentStream.endText();

        context.yPosition -= 30;

        return context;
    }


    // =====================================
    // WRITE SECTION HEADING
    // =====================================

    private PdfContext writeHeading(
            PdfContext context,
            String text
    ) throws IOException {

        context.contentStream.beginText();

        context.contentStream.setFont(
                PDType1Font.HELVETICA_BOLD,
                14
        );

        context.contentStream.newLineAtOffset(
                MARGIN,
                context.yPosition
        );

        context.contentStream.showText(
                safe(text)
        );

        context.contentStream.endText();

        context.yPosition -= 25;

        return context;
    }


    // =====================================
    // WRITE NORMAL LINE
    // =====================================

    private PdfContext writeLine(
            PdfContext context,
            String text
    ) throws IOException {

        context.contentStream.beginText();

        context.contentStream.setFont(
                PDType1Font.HELVETICA,
                11
        );

        context.contentStream.newLineAtOffset(
                MARGIN,
                context.yPosition
        );

        context.contentStream.showText(
                truncate(
                        safe(text),
                        90
                )
        );

        context.contentStream.endText();

        context.yPosition -= 20;

        return context;
    }


    // =====================================
    // MASK AADHAAR
    // =====================================

    private String maskAadhaar(String aadhaar) {

        if (aadhaar == null || aadhaar.length() < 4) {
            return "N/A";
        }

        String lastFour =
                aadhaar.substring(
                        aadhaar.length() - 4
                );

        return "XXXX XXXX " + lastFour;
    }


    // =====================================
    // MASK PAN
    // =====================================

    private String maskPan(String pan) {

        if (pan == null || pan.length() < 4) {
            return "N/A";
        }

        return pan.substring(0, 3)
                + "******"
                + pan.substring(
                pan.length() - 1
        );
    }


    // =====================================
    // SAFE NULL HANDLING
    // =====================================

    private String safe(Object value) {

        return value == null
                ? "N/A"
                : String.valueOf(value);
    }


    // =====================================
    // TEXT LENGTH BASED ON COLUMN WIDTH
    // =====================================

    private int getMaxLength(
            float columnWidth
    ) {

        return (int) (columnWidth / 5);
    }


    // =====================================
    // TRUNCATE TEXT
    // =====================================

    private String truncate(
            String text,
            int maxLength
    ) {

        if (text == null) {
            return "N/A";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        if (maxLength <= 3) {
            return text.substring(
                    0,
                    maxLength
            );
        }

        return text.substring(
                0,
                maxLength - 3
        ) + "...";
    }


    // =====================================
    // PDF CONTEXT
    // =====================================

    private static class PdfContext {

        private PDPage page;

        private PDPageContentStream contentStream;

        private float yPosition;

        public PdfContext(
                PDPage page,
                PDPageContentStream contentStream,
                float yPosition
        ) {

            this.page = page;
            this.contentStream = contentStream;
            this.yPosition = yPosition;
        }
    }
}