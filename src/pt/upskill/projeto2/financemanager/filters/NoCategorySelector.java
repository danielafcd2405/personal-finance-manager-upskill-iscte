package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.categories.Category;

public class NoCategorySelector implements Selector<StatementLine>{

    @Override
    public boolean isSelected(StatementLine item) {
        return item.getCategory() == null;
    }




}
