package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.DraftAccount;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonalFinanceManager {
	// TODO
    private List<Account> accounts;


    public PersonalFinanceManager() {
        // Ler os ficheiros da pasta account_info


        File[] files = null;
        try {
            File f = new File("account_info");
            // Listar todos os nomes dos ficheiros na pasta account_info
            files = f.listFiles();
        }
        catch (Exception e) {
            System.out.println("Erro ao ler ficheiros");
        }

        /*
        accounts = new ArrayList<>();
        // Para cada ficheiro, criar uma nova conta e adicionar à lista de accounts
        for (File file : files) {
            accounts.add(Account.newAccount(file));
        }
         */

        Account acc = Account.newAccount(new File("account_info/1234567890987.csv"));

    }




}
