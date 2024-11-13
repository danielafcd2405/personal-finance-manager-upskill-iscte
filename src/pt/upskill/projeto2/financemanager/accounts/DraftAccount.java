package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.date.Date;

import java.util.ArrayList;
import java.util.List;

public class DraftAccount extends Account{
    //TODO

    public DraftAccount(long id, String name) {
        super(id, name);
        setAccountType("DraftAccount");
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
            List<StatementLine> previousYearStatements = new ArrayList<>();
            for (StatementLine statementLine : statements) {
                if (statementLine.getDate().compareTo(beginningOfYear) < 0) {
                    previousYearStatements.add(statementLine);
                }
            }

            // Available balance na conta desde o início do ano

            double sumAvailableBalance = 0;
            for (int i = 0; i < statements.size() - 1; i++) {
                if (statements.get(i).getDate().compareTo(beginningOfYear) >= 0 && statements.get(i).getDate().compareTo(today) <= 0) {
                    double availableBalance = statements.get(i).getAvailableBalance();
                    int diffInDays = statements.get(i).getDate().diffInDays(statements.get(i + 1).getDate());
                    sumAvailableBalance += availableBalance * diffInDays;
                }
            }

            int totalDays = today.diffInDays(beginningOfYear);
            estimatedAverageBalance = sumAvailableBalance/totalDays;
        }

        return estimatedAverageBalance;
    }

    @Override
    public double getInterestRate() {
        return BanksConstants.normalInterestRate();
    }


}
