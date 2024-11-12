package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;

public class FileAccountFormat implements Format<Account> {

    @Override
    public String format(Account objectToFormat) {
        String nl = System.getProperty("line.separator");

        String formattedStatementLines = "";
        for (StatementLine statementLine : objectToFormat.getStatements()) {
            formattedStatementLines += statementLine.getDate().toString() + " ;" +
                    statementLine.getValueDate().toString() + " ;" +
                    statementLine.getDescription() + " ;" +
                    statementLine.getDraft() + " ;" +
                    statementLine.getCredit() + " ;" +
                    statementLine.getAccountingBalance() + " ;" +
                    statementLine.getAvailableBalance() + nl;
        }

        return "Account Info - " + new Date().toString() + nl
                + "Account  ;" + objectToFormat.getId() + " ; " + objectToFormat.getCurrency() + "  ;" + objectToFormat.getName() + " ;" + objectToFormat.getAccountType() + " ;" + nl
                + "Start Date ;" + objectToFormat.getStartDate().toString() + nl
                + "End Date ;" + objectToFormat.getEndDate().toString() + nl
                + "Date ;Value Date ;Description ;Draft ;Credit ;Accounting balance ;Available balance" + nl
                + formattedStatementLines;

    }
}
