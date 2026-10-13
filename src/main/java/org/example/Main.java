package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Bill> bills = new ArrayList<>();

    private static void listBills() {
        if (bills.isEmpty()) {
            System.out.println("暂无账单，请先添加！");
            return;
        }

        bills.forEach(bill -> {
            System.out.println("账单名称：" + bill.title());
            System.out.println("金额：" + bill.amount());
            System.out.println("类型：" + bill.type().getLabel());
            System.out.println("分类：" + bill.category().getLabel());
            System.out.println("日期：" + bill.date());
            System.out.println("------------------------------");
        });
    }

    private static void addBill() {
        System.out.print("请输入账单名称：");
        String title = scanner.nextLine().trim();

        // 写个循环判断 避免用户输入金额不对
        BigDecimal amount;
        while (true) {
            System.out.print("请输入账单金额：");
            String amountText = scanner.nextLine().trim();

            try {
                amount = new BigDecimal(amountText);
                if (amount.signum() <= 0) {
                    System.out.println("金额必须大于 0，请重新输入！");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("金额格式不正确，请输入有效的数字！");
            }

        }

        System.out.println("请选择类型：1. 收入  2. 支出");
        String typeChoice = scanner.nextLine().trim();

        TransactionType type;
        if ("1".equals(typeChoice)) {
            type = TransactionType.INCOME;
        } else if ("2".equals(typeChoice)) {
            type = TransactionType.EXPENSE;
        } else {
            System.out.println("类型选择错误，添加失败！");
            return;
        }

        System.out.println("请选择分类：");
        BillCategory[] categories = BillCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + "：" + categories[i].getLabel());
        }

        System.out.print("请输入分类编号：");
        String categoryChoice = scanner.nextLine().trim();
        int categoryIndex;
        try {
            categoryIndex = Integer.parseInt(categoryChoice) - 1;
        } catch (NumberFormatException e) {
            System.out.println("分类编号必须是数字，添加失败！");
            return;
        }

        if (categoryIndex < 0 || categoryIndex >= categories.length) {
            System.out.println("分类编号不存在，添加失败！");
            return;
        }


        Bill bill = new Bill(title, amount, type, categories[categoryIndex], LocalDate.now());
        bills.add(bill);
        System.out.println("添加成功！");
    }

    public static void main(String[] args) {
        while (true) {
            printMenu();
            System.out.println("请输入你的选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addBill();
                case "2" -> listBills();
                case "0" -> {
                    System.out.println("记账本已退出，再见！");
                    return; // 直接结束 main 方法
                }
                default -> System.out.println("无效的选项，请重新输入！");
            }
        }


    }

    // 显示菜单
    private static void printMenu() {
        System.out.println("========== 我的记账本 ==========");
        System.out.println("1. 添加账单");
        System.out.println("2. 查看账单");
        System.out.println("0. 退出");
        System.out.println("================================");
    }
}