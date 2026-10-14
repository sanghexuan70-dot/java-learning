package org.example;

import java.io.IOException;
import java.util.List;

public interface BillRepository {
    void save(Bill bill) throws IOException;

    List<Bill> findAll() throws IOException;
}
