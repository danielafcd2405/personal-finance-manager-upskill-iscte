package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;

import java.util.List;

public class SavingsAccount extends Account{
    // TODO

    public static Category savingsCategory = new Category("SAVINGS");

    public SavingsAccount(long id, String name) {
        super(id, name);
        setAccountType("SavingsAccount");
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

    @Override
    public void autoCategorizeStatements(List<Category> categories) {
        // As contas poupança categorizam automaticamente todos os seus movimentos como "SAVINGS"
        for (StatementLine statementLine : getStatements()) {
            statementLine.setCategory(savingsCategory);
            savingsCategory.addTag(statementLine.getDescription());
        }
    }

    @Override
    public void addStatementLine(StatementLine statementLine) {
        super.addStatementLine(statementLine);
        autoCategorizeStatements(null);
    }
}
