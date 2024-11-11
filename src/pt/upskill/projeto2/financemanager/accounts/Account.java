package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.io.File;
import java.util.List;

public class Account {
    //TODO
    // abstract ??

    private long id;
    private String name;
    private Date startDate;
    private Date endDate;

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
        // TODO
        return null;
    }

    public double currentBalance() {
        // TODO
        return 0.0;
    }

    public double estimatedAverageBalance() {
        // TODO
        return 0.0;
    }

    public Object getStartDate() {
        return startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public double getInterestRate() {
        // TODO
        return 0.0;
    }


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
