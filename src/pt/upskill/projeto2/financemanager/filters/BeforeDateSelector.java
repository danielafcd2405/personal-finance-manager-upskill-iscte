package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;

public class BeforeDateSelector implements Selector<StatementLine>{
    private Date date;

    public BeforeDateSelector(Date date) {
        this.date = date;
    }

    @Override
    public boolean isSelected(StatementLine item) {
        return item.getDate().compareTo(date) < 0;
    }
}
