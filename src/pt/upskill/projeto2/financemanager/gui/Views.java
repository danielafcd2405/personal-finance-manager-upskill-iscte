package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.date.Month;

import java.util.List;
import java.util.Map;

public class Views {

    private static final String separator1 = "-------------------------------------------------------------------------------------";
    private static final String separator2 = "*****************************************************";
    private static final String lineBreaks = "\n\n";

    public static void globalPositionView(Map<Long, Account> accounts, List<Category> categories) {
        // Header
        System.out.println(separator1);
        System.out.println("\tPOSIÇÃO GLOBAL");
        System.out.println(separator1);
        System.out.println();
        System.out.println("Número de conta \tSaldo");
        // Mostrar o saldo de cada conta
        double totalBalance = 0;
        for (Long key : accounts.keySet()) {
            Long id = accounts.get(key).getId();
            double currentBalance = accounts.get(key).currentBalance();
            totalBalance += currentBalance;
            System.out.println(id + " \t" + currentBalance);
        }
        System.out.println();
        System.out.println("Saldo total: \t" + totalBalance);

        System.out.println();
        System.out.println("Introduza 'Q' para regressar ao Menu Inicial");
        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
        } while (!input.equals("Q"));
        System.out.println(lineBreaks);
        PersonalFinanceManagerUserInterface.mainMenu(accounts, categories);
    }


    public static void accountStatementsView(long key, Map<Long, Account> accounts, List<Category> categories) {
        Account account = accounts.get(key);
        // Header
        System.out.println(separator1);
        System.out.println("\tMOVIMENTOS DE CONTA");
        System.out.println(separator1);
        System.out.println("Número de conta: " + account.getId() + " - " + account.getName() + " - " + account.getAccountType());

        // Data de início e de fim
        System.out.println();
        System.out.println("Data de início: " + account.getStartDate());
        System.out.println("Data de fim: " + account.getEndDate());

        // Movimentos
        System.out.println();
        LongStatementFormat longStatementFormat = new LongStatementFormat();
        System.out.println("Category \t" + longStatementFormat.fields());
        for (StatementLine statementLine : account.getStatements()) {
            String category = "        ";
            if (statementLine.getCategory() != null) {
                category = statementLine.getCategory().getName();
            }
            System.out.println(category + " \t" + longStatementFormat.format(statementLine));
        }

        System.out.println();
        System.out.println("Introduza 'E' para editar as categorias");
        System.out.println("Introduza 'Q' para regressar ao Menu Movimentos de Conta");

        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
            if (input.equals("E")) {
                System.out.println(lineBreaks);
                PersonalFinanceManagerUserInterface.editCategoriesChooseCategoryMenu(accounts, categories, PersonalFinanceManagerUserInterface.OPT_ACCOUNT_STATEMENT, key);
            } else if (input.equals("Q")){
                System.out.println(lineBreaks);
                PersonalFinanceManagerUserInterface.accountStatementChooseAccountMenu(accounts, categories);
            }
        } while (!input.equals("E") && !input.equals("Q"));

    }


    public static void monthlySummaryView(Map<Long, Account> accounts, List<Category> categories) {
        // Header
        System.out.println(separator1);
        System.out.println("\tEVOLUÇÃO GLOBAL POR MÊS");
        System.out.println(separator1);
        System.out.println();

        for (Long key : accounts.keySet()) {
            Account account = accounts.get(key);
            // Identificação da conta
            System.out.println(separator2);
            System.out.println("Número da conta: " + account.getId() + " - " + account.getName());
            System.out.println();

            Date startDate = account.getStartDate();
            int startYear = startDate.getYear();
            int startMonth = startDate.getMonth().ordinal();
            Date endDate = account.getEndDate();
            int endYear = endDate.getYear();
            int endMonth = endDate.getMonth().ordinal();
            for (int year = startYear; year <= endYear; year++) {
                System.out.println("Ano: " + year);
                System.out.println("\tMês \tDébitos \tCréditos \tBalanço mensal");
                for (int month = 1; month <= 12; month++) {
                    if ((year == startYear && month < startMonth) || (year == endYear && month > endMonth)) {
                        continue;
                    }
                    double totalDrafts = account.totalForMonth(month, year);
                    double totalCredits = account.totalForMonthCredit(month, year);
                    double monthlyBalance = Math.round((totalDrafts + totalCredits) * 100) / 100.0;
                    System.out.println("\t" + Month.values()[month] + " \t" + totalDrafts + " \t" + totalCredits + " \t" + monthlyBalance);
                }
                System.out.println();
            }
            System.out.println(separator2);

        }

        System.out.println();
        System.out.println("Introduza 'Q' para regressar ao Menu Análise");
        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
        } while (!input.equals("Q"));
        System.out.println(lineBreaks);
        PersonalFinanceManagerUserInterface.analysisMenu(accounts, categories);
    }


    public static void predictionPerCategoryView(Map<Long, Account> accounts, List<Category> categories, Account account) {
        // Mostra os drafts totais deste mês, para cada categoria
        System.out.println(separator1);
        System.out.println("\tPREVISÃO DE GASTOS TOTAIS DESTE MÊS, POR CATEGORIA");
        System.out.println(separator1);
        System.out.println();

        // Considero que este mês, é o mês do último statement da conta
        Date beginningOfMonth = Date.firstOfMonth(account.getEndDate());
        Date endOfMonth = Date.endOfMonth(account.getEndDate());
        int days = beginningOfMonth.diffInDays(account.getEndDate());
        int totalDays = beginningOfMonth.diffInDays(endOfMonth);

        System.out.println("Categoria \tDébitos até à data atual \tPrevisão de débitos até ao final do mês");
        for (Category category : categories) {
            double totalDraftsForCategory = account.totalDraftsForCategorySince(category, beginningOfMonth);
            double totalPredictedDrafts = Math.round(totalDraftsForCategory / days * totalDays * 100) / 100.0;
            System.out.println(category.getName() + " \t" + totalDraftsForCategory + " \t" + totalPredictedDrafts);
        }
        double totalDraftsNoCategory = account.totalDraftsNoCategorySince(beginningOfMonth);
        System.out.println("SEM CATEGORIA \t" + totalDraftsNoCategory);

        System.out.println();
        System.out.println("Introduza 'Q' para regressar ao Menu Seleção de Conta");
        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
        } while (!input.equals("Q"));
        System.out.println(lineBreaks);
        PersonalFinanceManagerUserInterface.predictionPerCategoryChooseAccountMenu(accounts, categories);
    }


    public static void annualInterestView(Map<Long, Account> accounts, List<Category> categories) {
        System.out.println(separator1);
        System.out.println("\tPREVISÃO DE JUROS ANUAIS");
        System.out.println(separator1);
        System.out.println();
        System.out.println("Número de conta \tSaldo médio estimado \tTaxa de juros \t Previsão de juros anuais");

        for (Long key : accounts.keySet()) {
            Account account = accounts.get(key);
            double estimatedAverageBalance = Math.round(account.estimatedAverageBalance() * 100) / 100.0;
            double interestRate = Math.round(account.getInterestRate() * 100) / 100.0;
            double interests = Math.round(estimatedAverageBalance * interestRate * 100) / 100.0;
            System.out.println(account.getId() + " \t" + estimatedAverageBalance + " \t" + interestRate + " \t" + interests);
        }
        System.out.println();
        System.out.println("Introduza 'Q' para regressar ao Menu Análise");
        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
        } while (!input.equals("Q"));
        System.out.println(lineBreaks);
        PersonalFinanceManagerUserInterface.analysisMenu(accounts, categories);

    }


    public static void listCategoriesView(Map<Long, Account> accounts, List<Category> categories) {
        System.out.println(separator1);
        System.out.println("\tLISTAR CATEGORIAS");
        System.out.println(separator1);
        System.out.println();
        System.out.print("Category: ");
        for (Category category : categories) {
            System.out.println(category.getName());
            System.out.println("Tags:");
            for (String tag : category.getTags()) {
                System.out.println("\t" + tag);
            }
            System.out.println();
        }

        System.out.println("Introduza 'E' para editar as categorias");
        System.out.println("Introduza 'Q' para regressar ao Menu Inicial");
        String input;
        do {
            input = PersonalFinanceManagerUserInterface.scanner.nextLine().toUpperCase();
            if (input.equals("E")) {
                System.out.println(lineBreaks);
                PersonalFinanceManagerUserInterface.editCategoriesChooseCategoryMenu(accounts, categories, PersonalFinanceManagerUserInterface.OPT_LIST_CATEGORIES, 0);
            } else if (input.equals("Q")) {
                System.out.println(lineBreaks);
                PersonalFinanceManagerUserInterface.mainMenu(accounts, categories);
            }
        } while (!input.equals("E") && !input.equals("Q"));
    }


}
