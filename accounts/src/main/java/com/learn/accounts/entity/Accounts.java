package com.learn.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity {
    
    private Long customerId;

    @Id
    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
