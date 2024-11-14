package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.PersonalFinanceManager;
import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.utils.Menu;

import java.util.Map;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class PersonalFinanceManagerUserInterface {

    public PersonalFinanceManagerUserInterface(
            PersonalFinanceManager personalFinanceManager) {
        this.personalFinanceManager = personalFinanceManager;
    }

    private static final String OPT_GLOBAL_POSITION = "Posição Global";
    private static final String OPT_ACCOUNT_STATEMENT = "Movimentos de Conta";
    private static final String OPT_LIST_CATEGORIES = "Listar Categorias";
    private static final String OPT_ANALISE = "Análise";
    private static final String OPT_EXIT = "Sair";

    private static final String OPT_MONTHLY_SUMMARY = "Evolução global por mês";
    private static final String OPT_PREDICTION_PER_CATEGORY = "Previsão gastos totais do mês por categoria";
    private static final String OPT_ANUAL_INTEREST = "Previsão juros anuais";

    private static final String OPT_RETURN_MAIN_MENU = "Regressar ao Menu Inicial";

    private static final String[] OPTIONS_ANALYSIS = {OPT_MONTHLY_SUMMARY, OPT_PREDICTION_PER_CATEGORY,
            OPT_ANUAL_INTEREST, OPT_RETURN_MAIN_MENU};
    private static final String[] OPTIONS = {OPT_GLOBAL_POSITION,
            OPT_ACCOUNT_STATEMENT, OPT_LIST_CATEGORIES, OPT_ANALISE, OPT_EXIT};

    private PersonalFinanceManager personalFinanceManager;


    public void execute() {

        // TODO
        mainMenu(personalFinanceManager.getAccounts());

    }

    private void mainMenu(Map<Long, Account> accounts) {
        String option = Menu.requestSelection("Menu Inicial", OPTIONS);
        if (option != null) {
            switch (option) {
                case OPT_GLOBAL_POSITION:
                    Views.globalPositionView(accounts);
                    break;
                case OPT_ACCOUNT_STATEMENT:
                    accountStatementMenu(accounts);
                    break;
                case OPT_LIST_CATEGORIES:
                    // TODO
                    break;
                case OPT_ANALISE:
                    analysisMenu(accounts);
                    break;
                case OPT_EXIT:
                    // TODO mostrar mensagem de despedida
                    break;
            }
        }
    }

    private void accountStatementMenu(Map<Long, Account> accounts) {
        String option = Menu.requestSelection(OPT_ACCOUNT_STATEMENT, createOptionsAccountId(accounts));
        if (option!= null && !option.equals(OPT_RETURN_MAIN_MENU)) {
            String[] s = option.split(" - ");
            long key = Long.parseLong(s[0].trim());
            Views.accountStatementsView(accounts.get(key));
        } else {
            mainMenu(accounts);
        }
    }

    private void analysisMenu(Map<Long, Account> accounts) {
        String option = Menu.requestSelection(OPT_ANALISE, OPTIONS_ANALYSIS);
        if (option != null) {
            switch (option) {
                case OPT_MONTHLY_SUMMARY:
                    Views.monthlySummary(accounts);
                    break;
                case OPT_PREDICTION_PER_CATEGORY:
                    predictionPerCategoryMenu(accounts);
                    break;
                case OPT_ANUAL_INTEREST:
                    // TODO
                    break;
                case OPT_RETURN_MAIN_MENU:
                    mainMenu(accounts);
                    break;
            }
        } else {
            mainMenu(accounts);
        }
    }

    private void predictionPerCategoryMenu(Map<Long, Account> accounts) {
        String option = Menu.requestSelection(OPT_PREDICTION_PER_CATEGORY, createOptionsAccountId(accounts));
        if (option != null && !option.equals(OPT_RETURN_MAIN_MENU)) {
            String[] s = option.split(" - ");
            long key = Long.parseLong(s[0].trim());
            Views.predictionPerCategoryView(accounts.get(key));
        } else {
            analysisMenu(accounts);
        }
    }

    private String[] createOptionsAccountId(Map<Long, Account> accounts) {
        int numberOfAccounts = accounts.size();
        String[] OPTIONS_ACCOUNT_ID = new String[numberOfAccounts + 1];
        int i = 0;
        for (Long key : accounts.keySet()) {
            String accountOption = key.toString() + " - " + accounts.get(key).getName();
            OPTIONS_ACCOUNT_ID[i] = accountOption;
            i++;
        }
        OPTIONS_ACCOUNT_ID[OPTIONS_ACCOUNT_ID.length - 1] = OPT_RETURN_MAIN_MENU;
        return OPTIONS_ACCOUNT_ID;
    }


}
