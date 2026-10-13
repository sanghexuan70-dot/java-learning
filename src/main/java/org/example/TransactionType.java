package org.example;

public enum TransactionType {

    INCOME("收入"),
    EXPENSE("支出");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
