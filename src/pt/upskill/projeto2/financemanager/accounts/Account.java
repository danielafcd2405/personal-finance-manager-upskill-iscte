package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    //TODO
    // abstract ???

    private long id;
    private String name;
    private String additionalInfo = "";
    private Date startDate = new Date();
    private Date endDate = new Date();
    private double currentBalance = 0.0;
    private double estimatedAverageBalance = 0.0;
    private double interestRate = getInterestRate();

    private List<StatementLine> statements = new ArrayList<>();

    public Account(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Account newAccount(File file) {
        // TODO
        // função fábrica

        return null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String additionalInfo() {
        return additionalInfo;
    }

    public double currentBalance() {
        // TODO
        return currentBalance;
    }

    public double estimatedAverageBalance() {
        // TODO
        return estimatedAverageBalance;
    }

    public Object getStartDate() {
        return startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public abstract double getInterestRate();

    public void addStatementLine(StatementLine description) {
        // TODO
    }

    public void removeStatementLinesBefore(Date date) {
        // TODO
    }

    public double totalDraftsForCategorySince(Category category, Date date) {
        // TODO
        return 0.0;
    }

    public double totalForMonth(int i, int i1) {
        // TODO
        return 0.0;
    }

    public void autoCategorizeStatements(List<Category> categories) {
        // TODO
    }
}
