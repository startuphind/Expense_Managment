package com.iconcile.project.ExpenseManager;

import com.iconcile.project.ExpenseManager.Entity.VendorCategory;
import com.iconcile.project.ExpenseManager.ExpenseService.ExpenseService;
import com.iconcile.project.ExpenseManager.Repository.VendorCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ExpenseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagerApplication.class, args);
	}

	@Autowired
	private ExpenseService expenseService;

	@EventListener(ApplicationReadyEvent.class)
	public void seedData() {
		expenseService.seedMappings();
	}
}


