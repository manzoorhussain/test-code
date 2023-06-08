package com.test.smallworld.util;

import com.test.smallworld.dto.TransactionDto;

import java.util.Comparator;

public class DesendingSort implements Comparator<TransactionDto> {

    @Override
    public int compare(TransactionDto o1, TransactionDto o2) {
        return (int)(o1.getAmount() - o2.getAmount());
    }
}
