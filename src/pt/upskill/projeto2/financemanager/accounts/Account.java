package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Account {

    private long id;
    private String name;
    private String additionalInfo = "";

    private List<StatementLine> statements = new ArrayList<>();

    public Account(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Account(long id, String name, String additionalInfo, List<StatementLine> statements) {
        this.id = id;
        this.name = name;
        this.additionalInfo = additionalInfo;
        this.statements = statements;
    }

    public static Account newAccount(File file) {
        // TODO separar em métodos mais pequenos
        List<String> statementLineInfo = new ArrayList<>();

        long id = 0;
        String name = "";
        String additionalInfo = "";
        String accountType = "";
        List<StatementLine> statements = new ArrayList<>();

        Account account = null;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Account  ;")) {
                    String[] accountLineInfo = line.split(";");
                    id = Long.parseLong(accountLineInfo[1].trim());
                    name = accountLineInfo[3].trim();
                    accountType = accountLineInfo[4].trim();
                    if (accountLineInfo.length == 6) {
                        additionalInfo = accountLineInfo[5];
                    }
                } else if (line.split(";").length >= 7 && !line.startsWith("Date")) {
                    statementLineInfo.add(line);
                }

            }

        } catch (Exception e) {
            System.out.println("Não foi possível ler o ficheiro da conta");
            e.printStackTrace();
        }

        // Adicionar os statements à conta
        for (String statementInfo : statementLineInfo) {
            String[] info = statementInfo.split(";");

            // date
            String[] DateInfo = info[0].trim().split("-");
            int day = Integer.parseInt(DateInfo[0]);
            int month = Integer.parseInt(DateInfo[1]);
            int year = Integer.parseInt(DateInfo[2]);
            Date date = new Date(day, month, year);

            // valueDate
            String[] valueDateInfo = info[1].trim().split("-");
            int dayValue = Integer.parseInt(valueDateInfo[0]);
            int monthValue = Integer.parseInt(valueDateInfo[1]);
            int yearValue = Integer.parseInt(valueDateInfo[2]);
            Date valueDate = new Date(dayValue, monthValue, yearValue);

            String description = info[2].trim();
            double draft = Double.parseDouble(info[3].trim());
            double credit = Double.parseDouble(info[4].trim());
            double accountingBalance = Double.parseDouble(info[5].trim());
            double availableBalance = Double.parseDouble(info[6].trim());

            Category category = null;
            if (info.length == 8) {
                category = new Category(info[7].trim());
            }

            statements.add(new StatementLine(date, valueDate, description, draft, credit, accountingBalance, availableBalance, category));
        }


        // Criar a conta de acordo com o tipo de conta que é
        if (accountType.equals("DraftAccount")) {
            account = new DraftAccount(id, name, additionalInfo, statements);
        } else {
            account = new SavingsAccount(id, name, additionalInfo, statements);
        }


        return account;
    }

    public long getId() {
        return id;
    }

    public List<StatementLine> getStatements() {
        return statements;
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
        // O currentBalance é igual ao availableBalance do último statement da lista
        double currentBalance = 0.0;
        if (!statements.isEmpty()) {
            currentBalance = this.statements.get(statements.size() - 1).getAvailableBalance();
        }
        return currentBalance;
    }

    public abstract double estimatedAverageBalance();

    public Date getStartDate() {
        Date startDate = null;
        if (!statements.isEmpty()) {
            startDate = this.statements.get(0).getDate();
        }
        return startDate;
    }

    public Date getEndDate() {
        Date endDate = null;
        if (!statements.isEmpty()) {
            endDate = this.statements.get(this.statements.size()-1).getDate();
        }
        return endDate;
    }

    public abstract double getInterestRate();

    public void addStatementLine(StatementLine statementLine) {
        statements.add(statementLine);
    }

    public void removeStatementLinesBefore(Date date) {
        List<StatementLine> toRemove = new ArrayList<>();
        for (StatementLine statementLine : statements) {
            if (statementLine.getDate().compareTo(date) < 0) {
                toRemove.add(statementLine);
            }
        }
        statements.removeAll(toRemove);
    }

    public double totalDraftsForCategorySince(Category category, Date date) {
        double totalDrafts = 0.0;
        for (StatementLine statementLine : statements) {
            if (statementLine.getCategory() != null) {
                if (statementLine.getCategory().getName().equals(category.getName()) && statementLine.getDate().compareTo(date) >= 0) {
                    totalDrafts += statementLine.getDraft();
                }
            }
        }
        return totalDrafts;
    }

    public double totalForMonth(int month, int year) {
        // Somar os drafts do mês correspondente
        double totalForMonth = 0;
        Date startOfMonth = new Date(1, month, year);
        Date endOfMonth = Date.endOfMonth(startOfMonth);
        for (StatementLine statementLine : statements) {
            if (statementLine.getDate().compareTo(startOfMonth) >= 0 && statementLine.getDate().compareTo(endOfMonth) <= 0) {
                totalForMonth += statementLine.getDraft();
            }
        }
        return totalForMonth;
    }

    public void autoCategorizeStatements(List<Category> categories) {
        // Se a description do statement for igual à tag de alguma categoria, essa categoria é adicionada ao statement
        for (StatementLine statementLine : statements) {
            for (Category category : categories) {
                List<String> tags = category.getTags();
                for (String tag : tags) {
                    if (statementLine.getDescription().equals(tag)) {
                        statementLine.setCategory(category);
                    }
                }
            }
        }
    }



}
