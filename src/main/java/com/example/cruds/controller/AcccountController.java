package com.example.cruds.controller;

import com.example.cruds.dto.AccountRequestDTO;
import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.services.AccountService;
import com.example.cruds.services.ExcelService;
import com.example.cruds.services.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account")
public class AcccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExcelService excelService;

    @PostMapping("/add")
    public ResponseEntity<AccountResponseDTO> add(
            @RequestBody AccountRequestDTO request) {


        AccountResponseDTO response = accountService.add(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AccountResponseDTO>> getAll() {

        List<AccountResponseDTO> accounts = accountService.getAll();

        return ResponseEntity.ok(accounts);
    }



    @GetMapping("/allaccountspdf")
    public ResponseEntity<byte[]> generateAllAccountsPdf() throws IllegalAccessException,IOException{

        List<AccountResponseDTO> accounts = accountService.getAll();



        byte[] pdf =
                pdfService.generateAllAccountsPdf(accounts);

        return ResponseEntity.ok()
                .header(
                        //This tells the browser: "This is a file attachment.
                        // Download it, and use account.pdf as the filename."
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=account.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(
            @PathVariable Long id) throws IOException {

//        String concatenation
//        log.debug("Customer ID: " + customerId);
//        Even if the DEBUG log level is disabled, Java may still construct the string.

//                Placeholder
//        log.debug("Customer ID: {}", customerId);
//        The logging framework can avoid unnecessary message formatting when that log level is disabled.

//        TIME | LEVEL | THREAD | CLASS | MESSAGE  pattern of log

        log.info("Received request to add the Account with id {}",id);
        log.trace("******************************");


        String requestId = MDC.get("requestId"); //Accessing the id from the mdc which is set in the mdc filter
        String method = MDC.get("method");
        String uri = MDC.get("uri");

        System.out.println("Accessing the request from the Mdc"+ requestId);


        AccountResponseDTO account =
                accountService.getAccount(id);


        return ResponseEntity.ok().body(account);

    }

    @GetMapping("/accounts/{id}/pdf")
    public ResponseEntity<byte[]> generateAccountPdf(
            @PathVariable Long id) throws IOException {

        AccountResponseDTO account =
                accountService.getAccount(id);

        byte[] pdf =
                pdfService.generateAccountPdf(account);

        return ResponseEntity.ok()
                .header(
                        //This tells the browser: "This is a file attachment.
                        // Download it, and use account.pdf as the filename."
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=account.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }



      //Used for the large applications directly sending stream instead of byte[]
    //____________________________________________________________________________________
//    @GetMapping("/accounts/{id}/pdf")
//    public void generateAccountPdf(
//            @PathVariable Long id,
//            HttpServletResponse response
//    ) throws IOException {
//
//        AccountResponseDTO account =
//                accountService.getAccount(id);
//
//        response.setContentType("application/pdf");
//
//        response.setHeader(
//                "Content-Disposition",
//                "attachment; filename=account.pdf"
//        );
//
//        pdfService.generateAccountPdf(
//                account,
//                response.getOutputStream()
//        );
//    }


//      consumes -> "This API expects the incoming request to contain multipart/form-data."

//      MultiPart ->
//      We don't use multipart because JSON is text and PDF is bytes. Both are transmitted as bytes.
//      We use multipart because it provides a standard way to upload a file as a named part,
//      especially when we may also send other data in the same request.

//    We commonly use multipart because the client is uploading a file, and multipart/form-data is a
//    standard format designed to send files and optionally other fields in the same HTTP request.
        @PostMapping(value = "pdf/read",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public String readPdf(
                @RequestParam("file") MultipartFile file
        ) throws IOException {

            return pdfService.readPdf(file);
        }

    @PostMapping(value = "pdf/read/convertoobject",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AccountResponseDTO>  readAccountsPdfAndconvertToObject(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return pdfService.readAccountsPdfAndconvertToObject(file);
    }



    @PostMapping(
            value = "/pdf/read-pages",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<String> readPdfPageByPage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return pdfService.readPdfPageByPage(file);
    }


    ///Exel apis
    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadAccountsExcel() {

        // Get accounts from database
        List<AccountResponseDTO> accounts =
                accountService.getAll();

        // Generate Excel
        byte[] excelData =
                excelService.generateExcel(accounts);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=accounts.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excelData);
    }

    @GetMapping("/generic/excel")
    public ResponseEntity<byte[]> generateExcel() {

        List<AccountResponseDTO> accounts =
                accountService.getAll();

        byte[] excel =
                excelService.generateExcel(
                        accounts,
                        "Accounts"
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=accounts.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excel);
    }



    @PostMapping(
            value = "/upload-excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<AccountResponseDTO>>
    uploadExcel(
            @RequestParam("file") MultipartFile file
    ) {

        List<AccountResponseDTO> accounts =
                excelService.readExcel(file);



        return ResponseEntity.ok(accounts);
    }


    @PostMapping(
            value = "/generic/upload-excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<AccountResponseDTO>>
    uploadExcel1(
            @RequestParam("file") MultipartFile file
    ) {

        List<AccountResponseDTO> accounts =
                excelService.readExcel(
                        file,
                        AccountResponseDTO.class
                );

        return ResponseEntity.ok(accounts);
    }



}
