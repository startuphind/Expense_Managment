package com.iconcile.project.ExpenseManager.ExpenseService;




import com.iconcile.project.ExpenseManager.Entity.Expense;
import com.iconcile.project.ExpenseManager.Entity.VendorCategory;
import com.iconcile.project.ExpenseManager.Repository.ExpenseRepository;
import com.iconcile.project.ExpenseManager.Repository.VendorCategoryRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final VendorCategoryRepository vendorRepo;

    public Expense addExpense(Expense expense) {
        prepareExpense(expense);
        return expenseRepo.save(expense);
    }

    public List<Expense> uploadCsv(MultipartFile file) throws Exception {
        List<Expense> saved = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            csvReader.readNext(); // skip header
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line.length < 4) continue;
                Expense e = new Expense();
                e.setDate(LocalDate.parse(line[0].trim()));
                e.setAmount(Double.parseDouble(line[1].trim()));
                e.setVendorName(line[2].trim());
                e.setDescription(line[3].trim());
                prepareExpense(e);
                saved.add(expenseRepo.save(e));
            }
        }
        return saved;
    }

    private void prepareExpense(Expense expense) {
        // Auto category from mapping
        String cat = vendorRepo.findByVendorNameIgnoreCase(expense.getVendorName())
                .map(VendorCategory::getCategory)
                .orElse("Other");
        expense.setCategory(cat);

        // Anomaly check
        long count = expenseRepo.countByCategory(cat);
        Double avg = expenseRepo.getAverageAmountByCategory(cat);
        boolean anomaly = count > 0 && expense.getAmount() > 3 * avg;
        expense.setIsAnomaly(anomaly);
    }

    public List<Expense> getAll() {
        return expenseRepo.findAll();
    }


    public Map<String, Object> getDashboard() {
        List<Expense> all = expenseRepo.findAll();

        // Monthly category totals
        Map<String, Map<String, Double>> monthlyMap = all.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        e -> e.getDate().getYear() + "-" + String.format("%02d", e.getDate().getMonthValue()),
                        java.util.stream.Collectors.groupingBy(Expense::getCategory,
                                java.util.stream.Collectors.summingDouble(Expense::getAmount))
                ));

        List<Map<String, Object>> monthlyList = new ArrayList<>();
        monthlyMap.forEach((month, catMap) -> catMap.forEach((cat, total) -> {
            Map<String, Object> m = new HashMap<>();
            m.put("month", month);
            m.put("category", cat);
            m.put("total", total);
            monthlyList.add(m);
        }));
        monthlyList.sort((a, b) -> ((String)b.get("month")).compareTo((String)a.get("month")));

        // Top 5 vendors
        Map<String, Double> vendorMap = all.stream()
                .collect(java.util.stream.Collectors.groupingBy(Expense::getVendorName,
                        java.util.stream.Collectors.summingDouble(Expense::getAmount)));
        List<Map<String, Object>> topList = vendorMap.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("vendorName", e.getKey());
                    m.put("total", e.getValue());
                    return m;
                })
                .sorted((a, b) -> Double.compare((Double)b.get("total"), (Double)a.get("total")))
                .limit(5)
                .toList();

        List<Expense> anomalies = expenseRepo.findByIsAnomalyTrue();

        Map<String, Object> result = new HashMap<>();
        result.put("monthlyCategoryTotals", monthlyList);
        result.put("topVendors", topList);
        result.put("anomalyCount", anomalies.size());
        result.put("anomalies", anomalies);
        return result;
    }

    // Seed data
    public void seedMappings() {
        if (vendorRepo.count() == 0) {
            vendorRepo.save(new VendorCategory(null, "Swiggy", "Food"));
            vendorRepo.save(new VendorCategory(null, "Zomato", "Food"));
            vendorRepo.save(new VendorCategory(null, "Amazon", "Shopping"));
            vendorRepo.save(new VendorCategory(null, "Flipkart", "Shopping"));
            vendorRepo.save(new VendorCategory(null, "Uber", "Transport"));
            vendorRepo.save(new VendorCategory(null, "Ola", "Transport"));
            vendorRepo.save(new VendorCategory(null, "Netflix", "Entertainment"));
        }
    }
}
