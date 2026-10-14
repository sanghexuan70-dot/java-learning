package org.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static BillRepository repository;

    public static void main(String[] args) throws IOException {
        // 1. 初始化账单仓库，程序启动时加载已有账单
        repository = new FileBillRepository(
                Path.of("data", "bills.txt")
        );

        while (true) {
            printMenu();
            System.out.println("请输入你的选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addBill();
                case "2" -> listBills();
                case "3" -> classifiedStatistic();
                case "0" -> {
                    System.out.println("记账本已退出，再见！");
                    return; // 直接结束 main 方法
                }
                default -> System.out.println("无效的选项，请重新输入！");
            }
        }


    }

    private static void listBills() throws IOException {
        List<Bill> bills = repository.findAll();

        if (bills.isEmpty()) {
            System.out.println("暂无账单，请先添加！");
            return;
        }

        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);

            System.out.println("账单编号：" + (i + 1));
            System.out.println("名称：" + bill.title());
            System.out.println("金额：" + bill.amount());
            System.out.println("类型：" + bill.type().getLabel());
            System.out.println("分类：" + bill.category().getLabel());
            System.out.println("日期：" + bill.date());
            System.out.println("------------------------------");
        }
    }

    private static void addBill() throws IOException {
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
        repository.save(bill);
        System.out.println("添加成功！");
    }

    private static void classifiedStatistic() throws IOException {
        Map<BillCategory, BigDecimal> totals = repository.findAll().stream()
                .filter(bill -> bill.type() == TransactionType.EXPENSE)   // 只统计支出
                .collect(Collectors.groupingBy(
                        Bill::category,                                    // 按什么分组（方法引用，验收点✓）
                        Collectors.reducing(BigDecimal.ZERO, Bill::amount, BigDecimal::add)));
        totals.forEach((category, sum) ->
                System.out.println(category.getLabel() + " 支出合计：" + sum + " 元")
        );

    }


    // 显示菜单
    private static void printMenu() {
        System.out.println("========== 我的记账本 ==========");
        System.out.println("1. 添加账单");
        System.out.println("2. 查看账单");
        System.out.println("3. 按分类统计列出每个分类的支出总额");
        System.out.println("0. 退出");
        System.out.println("================================");
    }
}