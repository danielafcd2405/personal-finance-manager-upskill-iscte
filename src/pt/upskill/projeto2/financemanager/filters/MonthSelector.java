package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;

public class MonthSelector implements Selector<StatementLine> {

    private final int month;
    private final int year;
    private final Date startOfMonth;
    private final Date endOfMonth;

    public MonthSelector(int month, int year) {
        this.month = month;
        this.year = year;
        startOfMonth = new Date(1, month, year);
        endOfMonth = Date.endOfMonth(startOfMonth);
    }

    @Override
    public boolean isSelected(StatementLine item) {
        return item.getDate().compareTo(startOfMonth) >= 0 && item.getDate().compareTo(endOfMonth) <= 0;
    }
}
