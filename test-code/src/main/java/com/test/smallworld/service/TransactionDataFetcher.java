package com.test.smallworld.service;


import com.test.smallworld.data.Transaction;
import com.test.smallworld.dto.TransactionDto;
import com.test.smallworld.util.CommonUtil;
import com.test.smallworld.util.AscendingSort;
import com.test.smallworld.util.DesendingSort;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionDataFetcher {

    @Autowired
    private Transaction transactionData;
    @Autowired
    private CommonUtil commonUtil;

    public List<TransactionDto> fetchTransactionData() throws IOException, ParseException {
        return transactionData.fetchTransactionData();
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        double totalTransactionAmount = transactionDtos.stream().mapToDouble(o -> o.getAmount()).sum();
        return totalTransactionAmount;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<TransactionDto> senders = transactionDtos.stream().filter(client -> client.getSenderFullName().equalsIgnoreCase(senderFullName)).collect(Collectors.toList());
        double totalAmount = senders.stream().mapToDouble(o -> o.getAmount()).sum();
        return totalAmount;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<Double> amounts = transactionDtos.stream().map(TransactionDto::getAmount).collect(Collectors.toList());
        Double maxTransactionAmount = amounts.stream().mapToDouble(v -> v).max().getAsDouble();
        return maxTransactionAmount;

    }


    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<TransactionDto> distinctClients = transactionDtos.stream().filter(commonUtil.distinctByKey(cust -> cust.getMtn())).collect(Collectors.toList());
        return distinctClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) throws IOException, ParseException {
        boolean isComplainOpen = false;
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<TransactionDto> senders = transactionDtos.stream().filter(client -> client.getSenderFullName().equalsIgnoreCase(clientFullName)).collect(Collectors.toList());
        if (Optional.ofNullable(senders).isPresent()) {
            for (TransactionDto sender : senders) {
                if (!sender.getIssueSolved()) {
                    isComplainOpen = true;
                    break;
                }
            }
        }

        return isComplainOpen;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Integer> getTransactionsByBeneficiaryName() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < transactionDtos.size(); i++) {
            TransactionDto transactionDto = transactionDtos.get(i);
            map.put(transactionDto.getBeneficiaryFullName(), i);
        }
        return map;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<TransactionDto> transactions = transactionDtos.stream().filter(client -> client.getIssueSolved() == false).collect(Collectors.toList());
        Set<Long> unSolvedIssueIds = transactions.stream().map(TransactionDto::getIssueId).collect(Collectors.toSet());
        return unSolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        List<TransactionDto> transactions = transactionDtos.stream().filter(client -> client.getIssueSolved() == true && client.getIssueMessage() != null).collect(Collectors.toList());
        List<String> allSolvedMessage = transactions.stream().map(TransactionDto::getIssueMessage).collect(Collectors.toList());
        return allSolvedMessage;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<TransactionDto> getTop3TransactionsByAmount() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        Collections.sort(transactionDtos, new DesendingSort());
        List<TransactionDto> topThreeTransactions = transactionDtos.subList(transactionDtos.size() - 3, transactionDtos.size());
        return topThreeTransactions;
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public String getTopSender() throws IOException, ParseException {
        List<TransactionDto> transactionDtos = fetchTransactionData();
        Collections.sort(transactionDtos, new AscendingSort());
        return transactionDtos.get(0).getSenderFullName();
    }

}
