package com.iconcile.project.ExpenseManager.Controller;


import com.iconcile.project.ExpenseManager.Entity.Expense;
import com.iconcile.project.ExpenseManager.ExpenseService.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping("/expenses")
    public Expense add(@RequestBody Expense expense) {
        return service.addExpense(expense);
    }

    @PostMapping("/expenses/upload")
    public List<Expense> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return service.uploadCsv(file);
    }

    @GetMapping("/expenses")
    public List<Expense> getAll() {
        return service.getAll();
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.getDashboard();
    }

    // Seed mappings on first run
    @GetMapping("/seed")
    public String seed() {
        service.seedMappings();
        return "Mappings seeded!";
    }
}