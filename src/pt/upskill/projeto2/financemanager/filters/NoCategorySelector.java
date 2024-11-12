package pt.upskill.projeto2.financemanager.filters;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.categories.Category;

public class NoCategorySelector implements Selector<StatementLine>{
    // TODO

    @Override
    public boolean isSelected(StatementLine item) {
        // TODO
        return item.getCategory() == null;
    }




}
