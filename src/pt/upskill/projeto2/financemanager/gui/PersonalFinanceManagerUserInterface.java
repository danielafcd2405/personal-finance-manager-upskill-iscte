package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.PersonalFinanceManager;
import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.utils.Menu;

import java.io.File;
import java.util.*;

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
    public static final String OPT_ACCOUNT_STATEMENT = "Movimentos de Conta";
    public static final String OPT_LIST_CATEGORIES = "Listar Categorias";
    private static final String OPT_ANALISE = "Análise";
    private static final String OPT_EXIT = "Sair";

    private static final String OPT_MONTHLY_SUMMARY = "Evolução global por mês";
    private static final String OPT_PREDICTION_PER_CATEGORY = "Previsão gastos totais do mês por categoria";
    private static final String OPT_ANUAL_INTEREST = "Previsão juros anuais";

    private static final String OPT_ADD_TAG = "Adicionar tag";
    private static final String OPT_DELETE_TAG = "Eliminar tag";
    private static final String OPT_DELETE_CATEGORY = "Eliminar esta categoria";

    private static final String OPT_ADD_CATEGORY = "Adicionar categoria";
    private static final String OPT_EDIT_CATEGORIES = "Editar categorias";

    private static final String[] OPTIONS_ANALYSIS = {OPT_MONTHLY_SUMMARY, OPT_PREDICTION_PER_CATEGORY,
            OPT_ANUAL_INTEREST};
    private static final String[] OPTIONS = {OPT_GLOBAL_POSITION,
            OPT_ACCOUNT_STATEMENT, OPT_LIST_CATEGORIES, OPT_ANALISE, OPT_EXIT};
    private static final String[] OPTIONS_EDIT_CATEGORY = {OPT_ADD_TAG, OPT_DELETE_TAG, OPT_DELETE_CATEGORY};

    public static final Scanner scanner = new Scanner(System.in);

    private final PersonalFinanceManager personalFinanceManager;


    public void execute() {
        mainMenu(personalFinanceManager.getAccounts(), personalFinanceManager.getCategories());
    }

    public static void mainMenu(Map<Long, Account> accounts, List<Category> categories) {
        String option = Menu.requestSelection("Menu Inicial", OPTIONS);
        if (option != null) {
            switch (option) {
                case OPT_GLOBAL_POSITION:
                    Views.globalPositionView(accounts, categories);
                    break;
                case OPT_ACCOUNT_STATEMENT:
                    accountStatementChooseAccountMenu(accounts, categories);
                    break;
                case OPT_LIST_CATEGORIES:
                    Views.listCategoriesView(accounts, categories);
                    break;
                case OPT_ANALISE:
                    analysisMenu(accounts, categories);
                    break;
                case OPT_EXIT:
                    saveChangesConfirmationBox(accounts, categories);
                    break;
            }
        }
    }

    public static void saveChangesConfirmationBox(Map<Long, Account> accounts, List<Category> categories) {
        if (Menu.yesOrNoInput("Deseja guardar as alterações realizadas?")) {
            Category.writeCategories(new File("account_info/categories"), categories);
            for (Long key : accounts.keySet()) {
                Account.writeAccountInfo(accounts.get(key));
            }
        }
    }

    public static void accountStatementChooseAccountMenu(Map<Long, Account> accounts, List<Category> categories) {
        String option = Menu.requestSelection(OPT_ACCOUNT_STATEMENT, createOptionsAccountId(accounts));
        if (option!= null && !option.isEmpty()) {
            String[] s = option.split(" - ");
            long key = Long.parseLong(s[0].trim());
            Views.accountStatementsView(key, accounts, categories);
        } else {
            mainMenu(accounts, categories);
        }
    }

    public static void analysisMenu(Map<Long, Account> accounts, List<Category> categories) {
        String option = Menu.requestSelection(OPT_ANALISE, OPTIONS_ANALYSIS);
        if (option != null) {
            switch (option) {
                case OPT_MONTHLY_SUMMARY:
                    monthlySummaryChooseAccountMenu(accounts, categories);
                    break;
                case OPT_PREDICTION_PER_CATEGORY:
                    predictionPerCategoryChooseAccountMenu(accounts, categories);
                    break;
                case OPT_ANUAL_INTEREST:
                    Views.annualInterestView(accounts, categories);
                    break;
            }
        } else {
            mainMenu(accounts, categories);
        }
    }

    public static void monthlySummaryChooseAccountMenu(Map<Long, Account> accounts, List<Category> categories) {
        String option = Menu.requestSelection(OPT_MONTHLY_SUMMARY, createOptionsAccountId(accounts));
        if (option != null && !option.isEmpty()) {
            String[] s = option.split(" - ");
            long key = Long.parseLong(s[0].trim());
            Views.monthlySummaryView(accounts, categories, accounts.get(key));
        } else {
            analysisMenu(accounts, categories);
        }
    }

    public static void predictionPerCategoryChooseAccountMenu(Map<Long, Account> accounts, List<Category> categories) {
        String option = Menu.requestSelection(OPT_PREDICTION_PER_CATEGORY, createOptionsAccountId(accounts));
        if (option != null && !option.isEmpty()) {
            String[] s = option.split(" - ");
            long key = Long.parseLong(s[0].trim());
            Views.predictionPerCategoryView(accounts, categories, accounts.get(key));
        }else {
            analysisMenu(accounts, categories);
        }
    }

    public static void editCategoriesChooseCategoryMenu(Map<Long, Account> accounts, List<Category> categories, String callingView, long key) {
        String option = Menu.requestSelection(OPT_EDIT_CATEGORIES, createOptionsCategory(categories));
        if (option != null && option.equals(OPT_ADD_CATEGORY)) {
            addCategoryInputBox(accounts, categories, callingView, key);
        } else if (option != null) {
            for (Category category : categories) {
                if (category.getName().equals(option)) {
                    editCategoryMenu(accounts, categories, option, callingView, key);
                }
            }
        } else {
            if (callingView.equals(OPT_ACCOUNT_STATEMENT)) {
                Views.accountStatementsView(key, accounts, categories);
            } else if (callingView.equals(OPT_LIST_CATEGORIES)) {
                Views.listCategoriesView(accounts, categories);
            }
        }
    }

    public static void editCategoryMenu(Map<Long, Account> accounts, List<Category> categories, String categoryName, String callingView, long key) {
        String option = Menu.requestSelection("Editar categoria", OPTIONS_EDIT_CATEGORY);
        if (option != null) {
            switch (option) {
                case OPT_ADD_TAG:
                    addTagInputBox(accounts, categories, categoryName, callingView, key);
                    break;
                case OPT_DELETE_TAG:
                    deleteTagSelectionBox(accounts, categories, categoryName, callingView, key);
                    break;
                case OPT_DELETE_CATEGORY:
                    deleteCategoryConfirmationBox(accounts, categories, categoryName, callingView, key);
                    break;
            }
        } else {
            editCategoriesChooseCategoryMenu(accounts, categories, callingView, key);
        }
    }

    public static void addTagInputBox(Map<Long, Account> accounts, List<Category> categories, String categoryName, String callingView, long key) {
        String input = Menu.requestInput("Introduza a tag que deseja adicionar:");
        if (input != null) {
            String tag = input.toUpperCase();
            if (hasTagInCategoryList(categories, tag)) {
                // Não pode adicionar uma tag que já está a ser usada
                Menu.showMessage("Erro", "Essa tag já está a ser utilizada noutro sítio.");
            } else {
                for (Category category : categories) {
                    if (category.getName().equals(categoryName)) {
                        category.addTag(tag);
                    }
                }
            }
            // Atualizar as statementLines com a tag adicionada
            for (Long accountKey : accounts.keySet()) {
                accounts.get(accountKey).autoCategorizeStatements(categories);
            }
        }
        editCategoryMenu(accounts, categories, categoryName, callingView, key);
    }

    public static boolean hasTagInCategoryList(List<Category> categories, String tag) {
        for (Category category : categories) {
            if (category.hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteTagSelectionBox(Map<Long, Account> accounts, List<Category> categories, String categoryName, String callingView, long key) {
        String option = Menu.requestSelection("Eliminar tag", createOptionsTagsSelection(categories, categoryName));
        if (option != null && !option.isEmpty()) {
            deleteTagConfirmationBox(accounts, categories, categoryName, callingView, key, option);
        } else {
            editCategoryMenu(accounts, categories, categoryName, callingView, key);
        }
    }

    public static void deleteTagConfirmationBox(Map<Long, Account> accounts, List<Category> categories, String categoryName, String callingView, long key, String option) {
        if (Menu.yesOrNoInput("Tem a certeza que deseja eliminar a tag " + option + "?")) {
            for (Category category : categories) {
                if (category.getName().equals(categoryName)) {
                    category.getTags().remove(option);
                }
            }
            // Atualizar as categorias das statementLines
            for (Long accountKey : accounts.keySet()) {
                accounts.get(accountKey).removeCategoryFromStatement(option, null);
            }
            editCategoryMenu(accounts, categories, categoryName, callingView, key);
        } else {
            deleteTagSelectionBox(accounts, categories, categoryName, callingView, key);
        }
    }

    public static void deleteCategoryConfirmationBox(Map<Long, Account> accounts, List<Category> categories, String categoryName, String callingView, long key) {
        if (Menu.yesOrNoInput("Tem a certeza que deseja eliminar esta categoria?")) {
            Iterator<Category> iterator = categories.iterator();
            while (iterator.hasNext()) {
                Category category = iterator.next();
                if (category.getName().equals(categoryName)) {
                    iterator.remove();
                }
            }
            // Atualizar as categorias das statementLines
            for (Long accountKey : accounts.keySet()) {
                accounts.get(accountKey).removeCategoryFromStatement(null, categoryName);
            }
            editCategoriesChooseCategoryMenu(accounts, categories, callingView, key);
        } else {
            editCategoryMenu(accounts, categories, categoryName, callingView, key);
        }
    }

    public static void addCategoryInputBox(Map<Long, Account> accounts, List<Category> categories, String callingView, long key) {
        String input = Menu.requestInput("Introduza o nome da categoria que deseja adicionar:");
        if (input != null) {
            String newCategory = input.toUpperCase();
            if (!categoryExists(categories, newCategory)) {
                categories.add(new Category(newCategory));
            } else {
                Menu.showMessage("Erro", "Já existe uma categoria com esse nome.");
            }
        }
        editCategoriesChooseCategoryMenu(accounts, categories, callingView, key);
    }

    public static boolean categoryExists(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public static String[] createOptionsTagsSelection(List<Category> categories, String categoryName) {
        int numberOfTagsInCategory = 0;
        List<String> tagsInCategory = new ArrayList<>();
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                tagsInCategory = category.getTags();
                numberOfTagsInCategory = tagsInCategory.size();
            }
        }
        String[] optionsTags;
        if (!tagsInCategory.isEmpty()) {
            optionsTags = new String[numberOfTagsInCategory];
            for (int i = 0; i < numberOfTagsInCategory; i++) {
                optionsTags[i] = tagsInCategory.get(i);
            }
        } else {
            optionsTags = new String[1];
            optionsTags[0] = "";
        }
        return optionsTags;
    }

    public static String[] createOptionsCategory(List<Category> categories) {
        int numberOfCategories = categories.size();
        String[] optionsCategory = new String[numberOfCategories + 1];
        for (Category category : categories) {
            optionsCategory[categories.indexOf(category)] = category.getName();
        }
        optionsCategory[optionsCategory.length - 1] = OPT_ADD_CATEGORY;
        return optionsCategory;
    }

    public static String[] createOptionsAccountId(Map<Long, Account> accounts) {
        int numberOfAccounts = accounts.size();
        String[] optionsAccountId;
        if (numberOfAccounts > 0) {
            optionsAccountId = new String[numberOfAccounts];
            int i = 0;
            for (Long key : accounts.keySet()) {
                String accountOption = key.toString() + " - " + accounts.get(key).getName();
                optionsAccountId[i] = accountOption;
                i++;
            }
        } else {
            optionsAccountId = new String[1];
            optionsAccountId[0] = "";
        }
        return optionsAccountId;
    }



}
