package com.test.smallworld.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private Long mtn;
    private Double amount;
    private String senderFullName;
    private Long senderAge;
    private String beneficiaryFullName;
    private Long beneficiaryAge;
    private Long issueId;
    private Boolean issueSolved;
    private String issueMessage;
}
