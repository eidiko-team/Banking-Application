package com.example.cruds.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Marks a Java class as a database table.
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Account") //specifies the table name
public class Account {

    @Id //Marks the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true,name = "accountNumber") //Maps a field to a column if field and the column in the table are different
    private String accountNumber;

    private String accountType;

    private Double balance;
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String status;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
