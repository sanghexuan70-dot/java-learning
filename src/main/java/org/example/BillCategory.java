package org.example;

public enum BillCategory {
    FOOD("餐饮"),
    TRANSPORT("交通"),
    SHOPPING("购物"),
    HOUSING("住房"),
    ENTERTAINMENT("娱乐"),
    MEDICAL("医疗"),
    SALARY("工资"),
    BONUS("奖金"),
    OTHER("其他");

    private final String label;

    BillCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
