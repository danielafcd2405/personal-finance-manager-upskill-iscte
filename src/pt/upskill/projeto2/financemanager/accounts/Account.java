package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.accounts.formats.FileAccountFormat;
import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.filters.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public abstract class Account {

    private final long id;
    private String name;
    private String additionalInfo = "";
    private String currency;
    private String accountType;

    protected List<StatementLine> statements = new ArrayList<>();

    public Account(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Account(long id, String name, String additionalInfo, List<StatementLine> statements, String currency, String accountType) {
        this.id = id;
        this.name = name;
        this.additionalInfo = additionalInfo;
        this.statements = statements;
        this.currency = currency;
        this.accountType = accountType;
    }

    public static Account newAccount(File file) {
        long id = 0;
        String name = "";
        String additionalInfo = "";
        String accountType = "";
        String currrency = "";

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Account") && line.split(";").length > 1) {
                    String[] accountLineInfo = line.split(";");
                    id = Long.parseLong(accountLineInfo[1].trim());
                    currrency = accountLineInfo[2].trim();
                    name = accountLineInfo[3].trim();
                    accountType = accountLineInfo[4].trim();
                    if (accountLineInfo.length == 6) {
                        additionalInfo = accountLineInfo[5];
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Não foi possível ler o ficheiro da conta");
        }

        Account account;
        List<StatementLine> statements = readAccountStatements(file);
        // Criar a conta de acordo com o tipo de conta que é
        if (accountType.equals(DraftAccount.ACCOUNT_TYPE)) {
            account = new DraftAccount(id, name, additionalInfo, statements, currrency, accountType);
        } else {
            account = new SavingsAccount(id, name, additionalInfo, statements, currrency, accountType);
        }

        return account;
    }

    public static List<StatementLine> readAccountStatements(File file) {
        List<StatementLine> statements = new ArrayList<>();
        List<String> statementLines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.split(";").length == 7 && !line.startsWith("Date")) {
                    statementLines.add(line);
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Não foi possível ler os movimentos da conta");
        }

        for (String statementInfo : statementLines) {
            statements.add(StatementLine.newStatementLine(statementInfo));
        }

        return statements;
    }

    public void addAllNewStatements(List<StatementLine> newStatements) {
        for (StatementLine newStatement : newStatements) {
            if (!isDuplicatedStatement(newStatement)) {
                statements.add(newStatement);
            }
        }
    }

    public boolean isDuplicatedStatement(StatementLine statementLine) {
        for (StatementLine statement : statements) {
            if (statement.equals(statementLine)) {
                return true;
            }
        }
        return false;
    }

    public static void writeAccountInfo(Account account) {
        try {
            PrintWriter printWriter = new PrintWriter("account_info/" + account.getId() + ".csv");
            FileAccountFormat fileAccountFormat = new FileAccountFormat();
            printWriter.println(fileAccountFormat.format(account));
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Não foi possível criar o ficheiro na pasta 'account_info'");
        }
    }


    public void removeIncoherentStatements() {
        ListIterator<StatementLine> iterator = statements.listIterator();
        while (iterator.hasNext()) {
            StatementLine statementLine = iterator.next();
            if (!isCoherentWithPreviousStatement(statementLine)) {
                iterator.remove();
            }
        }
    }

    private boolean isCoherentWithPreviousStatement(StatementLine statementLine) {
        boolean isCoherent = true;
        for (int i = 1; i < statements.size(); i++) {
            StatementLine statement = statements.get(i);
            if (statement.equals(statementLine)) {
                StatementLine previousStatement = statements.get(i - 1);
                double predictedAccountingBalance = Math.round((previousStatement.getAccountingBalance() + (statement.getDraft() + statement.getCredit())) * 100) / 100.00;
                isCoherent = statement.getAccountingBalance() == predictedAccountingBalance;
            }
        }
        return isCoherent;
    }

    public long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
            endDate = this.statements.get(this.statements.size() - 1).getDate();
        }
        return endDate;
    }

    public abstract double getInterestRate();

    public void addStatementLine(StatementLine statementLine) {
        statements.add(statementLine);
    }

    public void removeStatementLinesBefore(Date date) {
        Filter<StatementLine, BeforeDateSelector> filter = new Filter<>(new BeforeDateSelector(date));
        statements.removeAll(filter.apply(statements));
    }

    public double totalDraftsForCategorySince(Category category, Date date) {
        List<StatementLine> statementsSince = new ArrayList<>(new Filter<>(new AfterDateSelector(date, true)).apply(statements));
        List<StatementLine> statementsForCategorySince = new ArrayList<>(new Filter<>(new CategorySelector(category)).apply(statementsSince));
        double totalDrafts = 0.0;
        for (StatementLine statementLine : statementsForCategorySince) {
            totalDrafts += statementLine.getDraft();
        }
        return totalDrafts;
    }

    public double totalDraftsNoCategorySince(Date date) {
        List<StatementLine> statementsSince = new ArrayList<>(new Filter<>(new AfterDateSelector(date, true)).apply(statements));
        List<StatementLine> statementsNoCategorySince = new ArrayList<>(new Filter<>(new NoCategorySelector()).apply(statementsSince));
        double totalDrafts = 0.0;
        for (StatementLine statementLine : statementsNoCategorySince) {
            totalDrafts += statementLine.getDraft();
        }
        return totalDrafts;
    }

    public double totalForMonth(int month, int year) {
        // Somar os drafts do mês correspondente
        List<StatementLine> monthStatements = new ArrayList<>(new Filter<>(new MonthSelector(month, year)).apply(statements));
        double totalForMonth = 0;
        for (StatementLine statementLine : monthStatements) {
            totalForMonth += statementLine.getDraft();
        }
        return totalForMonth;
    }

    public double totalForMonthCredit(int month, int year) {
        // Somar os credits do mês correspondente
        List<StatementLine> monthStatements = new ArrayList<>(new Filter<>(new MonthSelector(month, year)).apply(statements));
        double totalForMonthCredit = 0;
        for (StatementLine statementLine : monthStatements) {
            totalForMonthCredit += statementLine.getCredit();
        }
        return totalForMonthCredit;
    }

    public void autoCategorizeStatements(List<Category> categories) {
        // Se a description do statement for igual à tag de alguma categoria, essa categoria é adicionada ao statement
        for (StatementLine statementLine : statements) {
            for (Category category : categories) {
                for (String tag : category.getTags()) {
                    if (statementLine.getDescription().equals(tag)) {
                        statementLine.setCategory(category);
                    }
                }
            }
        }
    }

    public void removeCategoryFromStatement(String tag, String categoryName) {
        if (tag != null) {
            // Se for uma tag que foi removida
            for (StatementLine statementLine : statements) {
                if (statementLine.getDescription().equals(tag)) {
                    statementLine.setCategory(null);
                }
            }
        } else if (categoryName != null) {
            // Se foi uma categoria que foi removida
            for (StatementLine statementLine : statements) {
                if (statementLine.getCategory() != null && statementLine.getCategory().getName().equals(categoryName)) {
                    statementLine.setCategory(null);
                }
            }
        }
    }


}
