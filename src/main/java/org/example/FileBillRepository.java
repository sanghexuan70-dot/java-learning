package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileBillRepository implements BillRepository {
    private final Path filePath;
    private final List<Bill> bills = new ArrayList<>();


    public FileBillRepository(Path filePath) throws IOException {
        this.filePath = filePath;

        // 确保保存文件的目录存在
        Files.createDirectories(filePath.toAbsolutePath().getParent());

        // 程序启动时加载已有账单
        loadAll();
    }

    // 启动时读取文件
    private void loadAll() throws IOException {
        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    bills.add(parseBill(line));
                } catch (RuntimeException e) {
                    throw new IOException(
                            "账单文件第 " + lineNumber + " 行格式错误", e
                    );
                }
            }
        }
    }

    // 将一行文本还原成 Bill
    private Bill parseBill(String line) {
        String[] parts = line.split("\t", -1);

        if (parts.length != 5) {
            throw new IllegalArgumentException("账单字段数量不正确");
        }

        String title = parts[0];
        BigDecimal amount = new BigDecimal(parts[1]);
        TransactionType type = TransactionType.valueOf(parts[2]);
        BillCategory category = BillCategory.valueOf(parts[3]);
        LocalDate date = LocalDate.parse(parts[4]);

        return new Bill(title, amount, type, category, date);
    }

    // 返回全部账单
    @Override
    public List<Bill> findAll() {
        return List.copyOf(bills);
    }

    // 保存一条账单
    @Override
    public void save(Bill bill) throws IOException {
        String line = String.join("\t",
                bill.title(),
                bill.amount().toString(),
                bill.type().name(),
                bill.category().name(),
                bill.date().toString()
        );

        // 追加写入文件，不覆盖已有内容
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(line);
            writer.newLine();
        }

        // 文件写入成功后，再更新内存缓存
        bills.add(bill);
    }
}
