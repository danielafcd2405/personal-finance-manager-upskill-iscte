package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.filters.AfterDateSelector;
import pt.upskill.projeto2.financemanager.filters.BeforeDateSelector;
import pt.upskill.projeto2.financemanager.filters.Filter;

import java.util.ArrayList;
import java.util.List;

public class DraftAccount extends Account{

    public static String ACCOUNT_TYPE = "DraftAccount";

    public DraftAccount(long id, String name) {
        super(id, name);
        setAccountType(ACCOUNT_TYPE);
    }

    public DraftAccount(long id, String name, String additionalInfo, List<StatementLine> statements, String currency, String accountType) {
        super(id, name, additionalInfo, statements, currency, accountType);
    }

    @Override
    public double estimatedAverageBalance() {
        double estimatedAverageBalance = 0.0;

        if (!statements.isEmpty()) {
            // Considero o dia de hoje como sendo a data do último statement
            Date today = statements.get(statements.size() - 1).getDate();
            Date beginningOfYear = new Date(1, 1, today.getYear());

            // Lista de movimentos anteriores ao início do ano
            List<StatementLine> previousYearStatements = new ArrayList<>(new Filter<>(new BeforeDateSelector(beginningOfYear)).apply(statements));
            // Lista de movimentos deste ano
            List<StatementLine> currentYearStatements = new ArrayList<>(new Filter<>(new AfterDateSelector(beginningOfYear, true)).apply(statements));

            double sumAccountingBalance = 0;

            for (int i = 0; i < currentYearStatements.size() - 1; i++) {
                if (i == 0) {
                    if (!previousYearStatements.isEmpty()) {
                        // Accounting balance na conta desde o início do ano, se este não for o primeiro ano de existência desta conta
                        double startingAccountingBalance = previousYearStatements.get(previousYearStatements.size()-1).getAccountingBalance();
                        int diffInDays = currentYearStatements.get(i).getDate().diffInDays(beginningOfYear);
                        sumAccountingBalance += startingAccountingBalance * diffInDays;
                    }
                    double accountingBalance = currentYearStatements.get(i).getAvailableBalance();
                    int diffInDays = currentYearStatements.get(i).getDate().diffInDays(currentYearStatements.get(i + 1).getDate());
                    sumAccountingBalance += accountingBalance * diffInDays;
                } else if (i != currentYearStatements.size() - 1){
                    double accountingBalance = currentYearStatements.get(i).getAvailableBalance();
                    int diffInDays = currentYearStatements.get(i).getDate().diffInDays(currentYearStatements.get(i + 1).getDate());
                    sumAccountingBalance += accountingBalance * diffInDays;
                } else {
                    // Para o último statement
                    sumAccountingBalance += currentYearStatements.get(i).getAccountingBalance();
                }
            }

            int totalDays = today.diffInDays(beginningOfYear);
            estimatedAverageBalance = sumAccountingBalance/totalDays;
        }

        return estimatedAverageBalance;
    }

    @Override
    public double getInterestRate() {
        return BanksConstants.normalInterestRate();
    }


}
