package com.duvva.rewards.repository;

import com.duvva.rewards.model.Customer;
import com.duvva.rewards.model.Transaction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataRepository {
    private List<Customer> customers = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private static final String DATA_FILE = "data.json";
    private final ObjectMapper mapper;

    public DataRepository() throws IOException {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        loadData();
    }

    public void loadData() throws IOException {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            Map<String, List<?>> data = mapper.readValue(file, new TypeReference<>() {});
            customers = mapper.convertValue(data.get("customers"), new TypeReference<List<Customer>>() {});
            transactions = mapper.convertValue(data.get("transactions"), new TypeReference<List<Transaction>>() {});
        } else {
            customers = new ArrayList<>(List.of(
                    new Customer(1L, "John Doe"),
                    new Customer(2L, "Jane Smith")
            ));;
            transactions = new ArrayList<>(List.of(
                    new Transaction(1L, 1L, 120, LocalDateTime.of(2025, 4, 10, 10, 0)),
                    new Transaction(2L, 1L, 80, LocalDateTime.of(2025, 5, 5, 14, 0)),
                    new Transaction(3L, 2L, 200, LocalDateTime.of(2025, 6, 20, 16, 0)),
                    new Transaction(4L, 2L, 50, LocalDateTime.of(2025, 4, 15, 12, 0))
            ));;
            saveData();
        }
    }

    public void saveData() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("customers", customers);
        data.put("transactions", transactions);
        mapper.writeValue(new File(DATA_FILE), data);
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void addCustomer(Customer customer) throws IOException {
        customers.add(customer);
        saveData();
    }

    public void addTransaction(Transaction transaction) throws IOException {
        transactions.add(transaction);
        saveData();
    }
}
