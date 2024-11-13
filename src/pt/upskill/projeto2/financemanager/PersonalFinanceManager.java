package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.categories.Category;

import java.io.File;
import java.util.*;

public class PersonalFinanceManager {
	// TODO
    private Map<Long, Account> accounts = new HashMap<>();
    private List<Category> categories = new ArrayList<>();


    public PersonalFinanceManager() {
        // TODO organizar e dividir em métodos mais pequenos
        // Ler os ficheiros da pasta account_info
        File[] files = null;
        try {
            File f = new File("account_info");
            // Listar todos os nomes dos ficheiros na pasta account_info
            files = f.listFiles();
        }
        catch (Exception e) {
            System.out.println("Erro ao ler ficheiros na pasta 'account_info'");
        }

        // Para cada ficheiro, criar uma nova conta e adicionar à lista de accounts
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("categories")) {
                    Account account = Account.newAccount(file);
                    accounts.put(account.getId(), account);
                    System.out.println(file.getName());
                } else {
                    // Ler o ficheiro das categorias
                    categories.addAll(Category.readCategories(file));
                }
            }
        }


        // TODO apagar
        imprimirContas();
        imprimirCategorias();
        System.out.println();
        System.out.println("-------------------------");

        // Ler ficheiros da pasta statements
        File[] filesStatements = null;
        try {
            File f = new File("statements");
            // Listar todos os nomes dos ficheiros na pasta statements
            filesStatements = f.listFiles();
        }
        catch (Exception e) {
            System.out.println("Erro ao ler ficheiros na pasta 'statements'");
        }

        System.out.println("Statements");
        if (filesStatements != null) {
            for (File file2 : filesStatements) {
                if (accounts.containsKey(readAccountID(file2))) {
                    // Adicionar os statements à conta correspondente
                    accounts.get(readAccountID(file2)).getStatements().addAll(Account.readAccountStatements(file2));
                } else {
                    // Se a conta não existir, criar uma conta nova
                    Account account = Account.newAccount(file2);
                    accounts.put(account.getId(), account);
                }
                System.out.println(file2.getName());
            }
        }


        // TODO apagar
        System.out.println();
        imprimirContas();


        for (Long key : accounts.keySet()) {
            // Ordenar os statements adicionados
            Collections.sort(accounts.get(key).getStatements());
            // Remover os statements duplicados
            accounts.get(key).removeDuplicatedStatements();
        }

        // TODO apagar
        System.out.println();
        System.out.println("Movimentos reordenados");
        imprimirContas();



    }


    private long readAccountID(File file) {
        long id = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Account") && line.split(";").length > 1) {
                    id = Long.parseLong(line.split(";")[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Não foi possível ler o ficheiro dos movimentos de conta");
        }
        return id;
    }



    public void imprimirContas() {
        // TODO apagar
        for (Long key : accounts.keySet()) {
            System.out.println("Account: " + accounts.get(key).getId() + " - " + accounts.get(key).getName());
            LongStatementFormat longStatementFormat = new LongStatementFormat();
            System.out.println(longStatementFormat.fields());
            for (StatementLine statementLine : accounts.get(key).getStatements()) {
                System.out.println(longStatementFormat.format(statementLine));
            }
            System.out.println();
            System.out.println();
        }
    }


    public void imprimirCategorias() {
        // TODO apagar
        System.out.println("Categorias");
        for (Category category : categories) {
            System.out.println(category.getName());
            System.out.println("Tags em - " + category.getName());
            for (String tag : category.getTags()) {
                System.out.println(tag);
            }
            System.out.println("--------------");
        }
    }

}
