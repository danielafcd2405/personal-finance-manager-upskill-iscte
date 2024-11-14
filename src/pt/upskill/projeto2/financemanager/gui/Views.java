package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.date.Month;

import java.util.Map;

public class Views {
    // Methods static para mostrar as informações pedidas no terminal

    public static void globalPositionView(Map<Long, Account> accounts) {
        // Header
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
        System.out.println("Pressione 'ESC' para regressar ao Menu Inicial");
        // TODO
    }


    public static void accountStatementsView(Account account) {
        // Header
        System.out.println("Movimentos de conta");
        System.out.println("Número de conta: " + account.getId() + " - " + account.getName() + " - " + account.getAccountType());

        // Data de início e de fim
        System.out.println();
        System.out.println("Data de início: " + account.getStartDate());
        System.out.println("Data de fim: " + account.getEndDate());

        // Movimentos
        System.out.println();
        LongStatementFormat longStatementFormat = new LongStatementFormat();
        System.out.println(longStatementFormat.fields());
        for (StatementLine statementLine : account.getStatements()) {
            System.out.println(longStatementFormat.format(statementLine));
        }

        System.out.println();
        System.out.println("Pressione 'T' para adicionar um movimento a uma categoria existente");
        System.out.println("Pressione 'C' para adicionar uma nova categoria");
        System.out.println("Pressione 'ESC' para regressar ao Menu Movimentos de Conta");
        // TODO
    }


    public static void monthlySummary(Map<Long, Account> accounts) {
        System.out.println("Evolução global por mês");
        System.out.println();

        for (Long key : accounts.keySet()) {
            Account account = accounts.get(key);
            // Identificação da conta
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Número da conta: " + account.getId() + " - " + account.getName());

            Date startDate = account.getStartDate();
            int startYear = startDate.getYear();
            int startMonth = startDate.getMonth().ordinal();
            Date endDate = account.getEndDate();
            int endYear = endDate.getYear();
            int endMonth = endDate.getMonth().ordinal();
            for (int year = startYear; year <= endYear; year++) {
                System.out.println("Ano: " + year);
                System.out.println("Mês \tDébitos \tCréditos \tBalanço mensal");
                for (int month = 1; month <= 12; month ++) {
                    if ((year == startYear && month < startMonth) || (year == endYear && month > endMonth)) {
                        continue;
                    }
                    double totalDrafts = account.totalForMonth(month, year);
                    double totalCredits = account.totalForMonthCredit(month, year);
                    System.out.println(Month.values()[month] + " \t" + totalDrafts + " \t" + totalCredits + " \t" + (totalDrafts + totalCredits));

                }
            }
            System.out.println("-------------------------------------------------------------------");

            // TODO arredondar resultado do balanço mensal
            // Alterar cores: vermelho para débitos e verde para créditos

            System.out.println();
            System.out.println("Pressione 'ESC' para regressar ao Menu Análise");
            // TODO

        }
    }


    public static void predictionPerCategoryView(Account account) {
        // TODO
    }

}
