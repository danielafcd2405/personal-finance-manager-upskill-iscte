package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;

public class AfterDateSelector implements Selector<StatementLine> {
    private final Date date;
    private final boolean inclusive;

    public AfterDateSelector(Date date, boolean inclusive) {
        this.date = date;
        this.inclusive = inclusive;
    }

    @Override
    public boolean isSelected(StatementLine item) {
        if (inclusive) {
            return item.getDate().compareTo(date) >= 0;
        } else {
            return item.getDate().compareTo(date) > 0;
        }
    }
}
