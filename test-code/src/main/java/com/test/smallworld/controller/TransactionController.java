package com.test.smallworld.controller;

import com.test.smallworld.dto.TransactionDto;
import com.test.smallworld.service.TransactionDataFetcher;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Value("${message.administrator}")
    private String administratorMessage;

    @Value("${message.invalid-sender-name}")
    private String senderNameMessage;


    @Autowired
    private TransactionDataFetcher fetcher;


    @GetMapping("/v1/api/total")
    public ResponseEntity getTotalTransactionAmount() {
        ResponseEntity response;
        Double totalTransactionAmount = 0.0;
        try {
            totalTransactionAmount = fetcher.getTotalTransactionAmount();
            response = new ResponseEntity<>(totalTransactionAmount, HttpStatus.OK);
        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/v1/api/transaction/{clientName}")
    public ResponseEntity getTotalTransactionAmountByClient(@PathVariable("clientName") String senderName) {
        ResponseEntity response;
        Double totalAmount = 0.0;
        try {
            totalAmount = fetcher.getTotalTransactionAmountSentBy(senderName);
            if (totalAmount == 0.0) {
                response = new ResponseEntity<>(senderNameMessage + " " + senderName, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(totalAmount, HttpStatus.OK);

            }

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/v1/api/max")
    public ResponseEntity getMaxTransactionAmount() {
        ResponseEntity response;
        Double maxAmount = 0.0;
        try {
            maxAmount = fetcher.getMaxTransactionAmount();
            response = new ResponseEntity<>(maxAmount, HttpStatus.OK);


        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/v1/api/unique")
    public ResponseEntity countUniqueClients() {
        ResponseEntity response;
        Long uniqueClient = 0L;
        try {
            uniqueClient = fetcher.countUniqueClients();
            response = new ResponseEntity<>(uniqueClient, HttpStatus.OK);


        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }


    @GetMapping("/v1/api/compliance/{clientName}")
    public ResponseEntity hasOpenComplianceIssues(@PathVariable("clientName") String senderName) {
        ResponseEntity response;
        boolean isComplianceOpen = false;
        try {
            isComplianceOpen = fetcher.hasOpenComplianceIssues(senderName);
            response = new ResponseEntity<>(isComplianceOpen, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }


    @GetMapping("/v1/api/unresolved-issues")
    public ResponseEntity unResolvedIssues() {
        ResponseEntity response;

        try {
            Set<Long> unResolvedIssues = fetcher.getUnsolvedIssueIds();
            response = new ResponseEntity<>(unResolvedIssues, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/v1/api/beneficiaries")
    public ResponseEntity getTransactionsByBeneficiaryName() {
        ResponseEntity response;

        try {
            Map<String, Integer>  unResolvedIssues = fetcher.getTransactionsByBeneficiaryName();
            response = new ResponseEntity<>(unResolvedIssues, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @GetMapping("/v1/api/solved-messages")
    public ResponseEntity allSolvedIssueMessages() {
        ResponseEntity response;

        try {
            List<String> allSolvedIssueMessages = fetcher.getAllSolvedIssueMessages();
            response = new ResponseEntity<>(allSolvedIssueMessages, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }


    @GetMapping("/v1/api/top-three")
    public ResponseEntity topThreeTransactions() {
        ResponseEntity response;

        try {
            List<TransactionDto> topThreeTransactions = fetcher.getTop3TransactionsByAmount();
            response = new ResponseEntity<>(topThreeTransactions, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
    @GetMapping("/v1/api/top-sender")
    public ResponseEntity getTopSender() {
        ResponseEntity response;

        try {
            String topSenderName = fetcher.getTopSender();
            response = new ResponseEntity<>(topSenderName, HttpStatus.OK);

        } catch (IOException | ParseException e) {
            response = new ResponseEntity<>(administratorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
