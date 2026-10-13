package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record Bill(String title, BigDecimal amount, TransactionType type, BillCategory category, LocalDate date) {
    public Bill {
        Objects.requireNonNull(title, "账单名称不能为空");
        Objects.requireNonNull(amount, "金额不能为空");
        Objects.requireNonNull(type, "收支类型不能为空");
        Objects.requireNonNull(category, "账单分类不能为空");
        Objects.requireNonNull(date, "账单日期不能为空");

        if (title.isBlank()) {
            throw new IllegalArgumentException("账单名称不能为空");
        }

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("金额必须大于 0");
        }
    }

}
