package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.categories.Category;

import java.io.File;
import java.util.*;

public class PersonalFinanceManager {
    private final Map<Long, Account> accounts = new HashMap<>();
    private final List<Category> categories = new ArrayList<>();

    public Map<Long, Account> getAccounts() {
        return accounts;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public PersonalFinanceManager() {
        readAccountInfoFiles();
        readStatementsFiles();

        for (Long key : accounts.keySet()) {
            // Ordenar cronologicamente os statements adicionados
            Collections.sort(accounts.get(key).getStatements());
            // Remover os statements incoerentes
            accounts.get(key).removeIncoherentStatements();
            // Categorizar automaticamente
            accounts.get(key).autoCategorizeStatements(categories);
        }

    }

    private void readAccountInfoFiles() {
        // Listar todos os nomes dos ficheiros na pasta account_info
        File[] files = null;
        try {
            File f = new File("account_info");
            files = f.listFiles();
        } catch (Exception e) {
            System.out.println("Erro ao ler ficheiros na pasta 'account_info'");
        }

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("categories")) {
                    // Para cada ficheiro, criar uma nova conta e adicionar à lista de accounts
                    Account account = Account.newAccount(file);
                    accounts.put(account.getId(), account);
                } else {
                    // Ler o ficheiro das categorias
                    categories.addAll(Category.readCategories(file));
                }
            }
        }
    }

    private void readStatementsFiles() {
        // Ler ficheiros da pasta statements
        File[] files = null;
        try {
            File f = new File("statements");
            files = f.listFiles();
        } catch (Exception e) {
            System.out.println("Erro ao ler ficheiros na pasta 'statements'");
        }

        if (files != null) {
            for (File file : files) {
                if (accounts.containsKey(readAccountID(file))) {
                    // Adicionar os statements à conta correspondente
                    accounts.get(readAccountID(file)).addAllNewStatements(Account.readAccountStatements(file));
                } else {
                    // Se a conta não existir, criar uma conta nova
                    Account account = Account.newAccount(file);
                    accounts.put(account.getId(), account);
                }
            }
        }
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
            scanner.close();
        } catch (Exception e) {
            System.out.println("Não foi possível ler o ficheiro dos movimentos de conta");
        }
        return id;
    }


}
