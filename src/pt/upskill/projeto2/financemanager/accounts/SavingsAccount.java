package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.util.List;

public class SavingsAccount extends Account{

    public static Category savingsCategory = new Category("SAVINGS");
    public static String ACCOUNT_TYPE = "SavingsAccount";

    public SavingsAccount(long id, String name) {
        super(id, name);
        setAccountType(ACCOUNT_TYPE);
    }

    public SavingsAccount(long id, String name, String additionalInfo, List<StatementLine> statements, String currency, String accountType) {
        super(id, name, additionalInfo, statements, currency, accountType);
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
