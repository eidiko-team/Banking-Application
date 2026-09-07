package com.example.cruds.controller;

import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.dto.CustomerRequestDTO;
import com.example.cruds.dto.CustomerResponseDTO;
import com.example.cruds.exceptions.CustomerAlreadyExistsException;
import com.example.cruds.exceptions.ErrorResponse1;
import com.example.cruds.services.CustomerService;
import com.example.cruds.services.ExcelService;
import com.example.cruds.services.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer Controller", description = "Customer Management APIs")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExcelService excelService;

    //testing the @value here
    @Value("${bank.name}")
    private String bankName;

    @GetMapping("/bankName")
    public String getBankName() {
        return bankName;
    }

    //Testing the @ConfigProperties here
    @GetMapping("/BankInfo")
    public String getBankInformation(){
        return customerService.getBankInformation();
    }


    @Operation(
            summary = "Add Customer",
            description = "Creates a new customer in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer added successfully"
    )
    @PostMapping("/addCustomer")
    public ResponseEntity<CustomerResponseDTO> addCustomer(
            @Valid @RequestBody CustomerRequestDTO request) {

        CustomerResponseDTO response =
                customerService.addCustomer(request);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get Customer",
            description = "Fetch customer details using customer ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Customer not found"
    )
    @GetMapping("/get")
    public ResponseEntity<CustomerResponseDTO> getCustomer(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id) {

        CustomerResponseDTO response =
                customerService.getCustomer(id);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Delete Customer",
            description = "Deletes a customer by ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer deleted successfully"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id) {

        String response = customerService.delete(id);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Update Customer",
            description = "Updates customer information."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer updated successfully"
    )
    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDTO> update(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id,
            @Valid @RequestBody CustomerRequestDTO request) {

        CustomerResponseDTO response =
                customerService.update(id, request);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get All Customers",
            description = "Returns all customers."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customers retrieved successfully"
    )
    @GetMapping("/getAll")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {

        List<CustomerResponseDTO> response =
                customerService.getAll();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/getAccounts")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsOfCustomer(
            @RequestParam Long id) {

        List<AccountResponseDTO> accounts =
                customerService.getAllAccountsOfCustomer(id);

        return ResponseEntity.ok(accounts);
    }



//    controller level handling or Using @ExceptionHandler Annotation
    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse1 handleNoSuchElementException(NoSuchElementException ex) {
        return new ErrorResponse1(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }


    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse1 handleCustomerAlreadyExists(
            CustomerAlreadyExistsException ex) {

        return new ErrorResponse1(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
    }

//    @ExceptionHandler(CustomerAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse1> handleCustomerAlreadyExists(
//            CustomerAlreadyExistsException ex) {
//
//        ErrorResponse1 error = new ErrorResponse1(
//                HttpStatus.CONFLICT.value(),
//                ex.getMessage()
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(error);
//    }

//----------------------------------------------------------------------------------------------------------
//    Handling the Exception using the Try-catch in the controller any way using the global Handler is good
//    because the controller layer stays clean
    //---------------------------------------------------------------------------------------------------
//    @PostMapping("/addCustomer")
//    public ResponseEntity<?> addCustomer(
//            @Valid @RequestBody CustomerRequestDTO request) {
//
//        try {
//
//            CustomerResponseDTO response =
//                    customerService.addCustomer(request);
//
//            return ResponseEntity.ok(response);
//
//        } catch (CustomerAlreadyExistsException ex) {
//
//            ErrorResponse1 error = new ErrorResponse1(
//                    HttpStatus.CONFLICT.value(),
//                    ex.getMessage()
//            );
//
//            return ResponseEntity
//                    .status(HttpStatus.CONFLICT)
//                    .body(error);
//        }
//    }


//    @GetMapping("/{customerId}/pdf")
//    public ResponseEntity<byte[]> downloadCustomerPdf(
//            @PathVariable Long customerId
//    ) throws IOException {
//
//        // Step 1: Get customer data
//        CustomerResponseDTO customer =
//                customerService.getCustomer(customerId);
//
//        // Step 2: Convert DTO to PDF
//        byte[] pdf =
//                pdfService.generateCustomerPdf(customer);
//
//        // Step 3: Return PDF
//        return ResponseEntity.ok()
//                .header(
//                        HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=customer_"
//                                + customerId
//                                + ".pdf"
//                )
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdf);
//    }

    //generic excel call
    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadCustomersExcel() {

        List<CustomerResponseDTO> customers =
                customerService.getAll();

        byte[] excel =
                excelService.generateExcel(
                        customers,
                        "Customers"
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=customers.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(excel);
    }


}