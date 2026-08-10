package com.example.cruds.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Kyc kyc;


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



//| Annotation        | Purpose                                                    |
//        | ----------------- | ---------------------------------------------------------- |
//        | `@Entity`         | Maps class to a table                                      |
//        | `@Table`          | Specifies the table name                                   |
//        | `@Id`             | Marks the primary key                                      |
//        | `@GeneratedValue` | Auto-generates primary key values                          |
//        | `@Column`         | Maps a field to a column and customizes it                 |
//        | `@Transient`      | Excludes a field from persistence                          |
//        | `@Lob`            | Maps large text or binary data                             |
//        | `@Enumerated`     | Stores enum values                                         |
//        | `@OneToOne`       | One-to-one relationship                                    |
//        | `@OneToMany`      | One parent, many children                                  |
//        | `@ManyToOne`      | Many children, one parent                                  |
//        | `@ManyToMany`     | Many-to-many relationship                                  |
//        | `@JoinColumn`     | Defines the foreign key column                             |
//        | `@JoinTable`      | Defines the join table for many-to-many                    |
//        | `mappedBy`        | Specifies the inverse side of a bidirectional relationship |
//        | `cascade`         | Propagates operations to related entities                  |
//        | `fetch`           | Controls when related entities are loaded                  |
//        | `orphanRemoval`   | Deletes child entities removed from a relationship         |
//        | `@Embedded`       | Embeds an object into the same table                       |
//        | `@Embeddable`     | Marks a class that can be embedded                         |
//        | `@EmbeddedId`     | Defines a composite primary key                            |

