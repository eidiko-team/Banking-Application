package com.example.cruds.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Builder
@Schema(description = "Customer ID", example = "1")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Schema(description = "Customer Name", example = "John Doe")
    @NotNull(message = "should be given")
    @Size(min = 5, max = 40, message = "Name must be between the 3 and the 40")
    private String name;
    @Email(message = "Email must be required")
    private String email;
    @Size(max = 10)
    private String phone;
    @NotBlank(message = "Address is required")
    @Size(min = 10 , max = 100, message = "Address must be between 10 and 100 characters")
    private String address;

    public Customer() {
    }

    public Customer(Long customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void displayy(){
        System.out.println("88888888888888888888888888888*************************");
    }
}











/*First, make sure you have:

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

Then import annotations from:

import jakarta.validation.*;
Common validation annotations
Annotation	Purpose	Example
@NotNull	Value cannot be null	@NotNull
@NotBlank	String cannot be null, empty, or only spaces	@NotBlank
@NotEmpty	String/collection cannot be null or empty	@NotEmpty
@Size	Validates length/size	@Size(min = 3, max = 50)
@Min	Minimum numeric value	@Min(18)
@Max	Maximum numeric value	@Max(100)
@Positive	Number must be greater than 0	@Positive
@PositiveOrZero	Number must be 0 or greater	@PositiveOrZero
@Negative	Number must be less than 0	@Negative
@Email	Valid email format	@Email
@Pattern	Validates using regex	@Pattern(...)
@Past	Date must be in the past	@Past
@PastOrPresent	Date cannot be in the future	@PastOrPresent
@Future	Date must be in the future	@Future
@FutureOrPresent	Date cannot be in the past	@FutureOrPresent
@Digits	Validates integer and decimal digits	@Digits(integer = 10, fraction = 2)
* */
