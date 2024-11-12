package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.date.Date;

import java.util.List;

public class DraftAccount extends Account{
    //TODO

    public DraftAccount(long id, String name) {
        super(id, name);
    }

    public DraftAccount(long id, String name, String additionalInfo, List<StatementLine> statements) {
        super(id, name, additionalInfo, statements);
    }

    @Override
    public double estimatedAverageBalance() {
        // TODO aplicar o cálculo da média ponderada
        return currentBalance();
    }

    @Override
    public double getInterestRate() {
        return BanksConstants.normalInterestRate();
    }


}
