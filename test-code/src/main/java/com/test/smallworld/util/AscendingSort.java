package com.test.smallworld.util;

import com.test.smallworld.dto.TransactionDto;

import java.util.Comparator;

public class AscendingSort implements Comparator<TransactionDto> {

    @Override
    public int compare(TransactionDto o1, TransactionDto o2) {
        return (int)(o2.getAmount() - o1.getAmount());
    }
}
