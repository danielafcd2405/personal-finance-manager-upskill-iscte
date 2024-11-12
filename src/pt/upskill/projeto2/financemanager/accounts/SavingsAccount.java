package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.util.List;

public class SavingsAccount extends Account{
    // TODO

    public static Category savingsCategory;

    public SavingsAccount(long id, String name) {
        super(id, name);
    }

    public SavingsAccount(long id, String name, String additionalInfo, List<StatementLine> statements) {
        super(id, name, additionalInfo, statements);
    }

    @Override
    public double estimatedAverageBalance() {
        return currentBalance();
    }

    @Override
    public double getInterestRate() {
        return BanksConstants.savingsInterestRate();
    }


}
