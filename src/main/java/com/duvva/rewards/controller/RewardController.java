package com.duvva.rewards.controller;

import com.duvva.rewards.model.Customer;
import com.duvva.rewards.model.Transaction;
import com.duvva.rewards.repository.DataRepository;
import com.duvva.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {


    private final DataRepository repository;
    private final RewardService rewardService;

    @Autowired
    public RewardController(DataRepository repository, RewardService rewardService) {
        this.repository = repository;
        this.rewardService = rewardService;
    }

    @GetMapping
    public List<Map<String, Object>> getRewards() {
        List<Customer> customers = repository.getAllCustomers();
        List<Transaction> transactions = repository.getAllTransactions();

        // Calculate the earliest date to include (start of the month, 2 months ago)
        YearMonth currentMonth = YearMonth.now();
        YearMonth threeMonthsAgo = currentMonth.minusMonths(2); // includes current, -1, -2

        Map<Long, Map<String, Integer>> customerMonthlyPoints = new HashMap<>();

        for (Transaction tx : transactions) {
            YearMonth txMonth = YearMonth.from(tx.getTransactionDate());
            if (txMonth.isBefore(threeMonthsAgo)) {
                continue; // skip transactions older than 3 months
            }
            int points = rewardService.calculatePoints(tx.getAmount());
            String month = tx.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            customerMonthlyPoints
                    .computeIfAbsent(tx.getCustomerId(), k -> new HashMap<>())
                    .merge(month, points, Integer::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Customer customer : customers) {
            Map<String, Integer> monthly = customerMonthlyPoints.getOrDefault(customer.getId(), new HashMap<>());
            int total = monthly.values().stream().mapToInt(Integer::intValue).sum();

            Map<String, Object> customerResult = new HashMap<>();
            customerResult.put("customerId", customer.getId());
            customerResult.put("customerName", customer.getName());
            customerResult.put("monthlyRewards", monthly);
            customerResult.put("totalRewards", total);

            result.add(customerResult);
        }

        return result;
    }

    @PostMapping("/customers")
    public void addCustomer(@RequestBody Customer customer) throws IOException {
        repository.addCustomer(customer);
    }

    @PostMapping("/transactions")
    public void addTransaction(@RequestBody Transaction transaction) throws IOException {
        repository.addTransaction(transaction);
    }
}
