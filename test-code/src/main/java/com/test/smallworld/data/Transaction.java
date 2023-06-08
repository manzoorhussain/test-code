package com.test.smallworld.data;


import com.test.smallworld.dto.TransactionDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Transaction {

    @Value("${file.name}")
    private String fileName;


    private JSONArray initializeData() throws IOException, ParseException {


        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(fileName));
        JSONArray jsonObjectArray = (JSONArray) obj;
        return jsonObjectArray;


    }

    public List<TransactionDto> fetchTransactionData() throws IOException, ParseException {
        JSONArray jsonData = initializeData();
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Object data : jsonData) {
            JSONObject transactionObject = (JSONObject) data;
            Long mtn = (Long) transactionObject.get("mtn");
            Double amount = (Double) transactionObject.get("amount");
            String senderFullName = (String) transactionObject.get("senderFullName");
            Long senderAge = (Long) transactionObject.get("senderAge");
            String beneficiaryFullName = (String) transactionObject.get("beneficiaryFullName");
            Long beneficiaryAge = (Long) transactionObject.get("beneficiaryAge");
            Long issueId = (Long) transactionObject.get("issueId");
            boolean issueSolved = (boolean) transactionObject.get("issueSolved");
            String issueMessage = (String) transactionObject.get("issueMessage");

            TransactionDto transactionDto = TransactionDto.builder().mtn(mtn).amount(amount).senderFullName(senderFullName)
                    .senderAge(senderAge).beneficiaryFullName(beneficiaryFullName).beneficiaryAge(beneficiaryAge)
                    .issueId(issueId).issueSolved(issueSolved).issueMessage(issueMessage)
                    .build();

            transactionDtos.add(transactionDto);


        }//end for

        return transactionDtos;
    }

}
