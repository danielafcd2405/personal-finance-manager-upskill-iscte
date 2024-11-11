package pt.upskill.projeto2.financemanager.categories;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Category implements Serializable{

    private String name;
    private List<String> tags = new ArrayList<>();
    private static final long serialVersionUID = -9107819223195202547L;

    public Category(String name) {
        this.name = name;
    }

    /**
     * Função que lê o ficheiro categories e gera uma lista de {@link Category} (métod fábrica)
     * Deve ser utilizada a desserialização de objetos para ler o ficheiro binário categories.
     *
     * @param file - Ficheiro onde estão apontadas as categorias possíveis iniciais, numa lista serializada (por defeito: /account_info/categories)
     * @return uma lista de categorias, geradas ao ler o ficheiro
     */
    public static List<Category> readCategories(File file) {
        List<Category> categories = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            categories = (ArrayList<Category>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println("Não foi possível ler o ficheiro das categorias");
        }

        return categories;
    }

    /**
     * Função que grava no ficheiro categories (por defeito: /account_info/categories) a lista de {@link Category} passada como segundo argumento
     * Deve ser utilizada a serialização dos objetos para gravar o ficheiro binário categories.
     *
     * @param file
     * @param categories
     */
    public static void writeCategories(File file, List<Category> categories) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(categories);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar a lista de categorias");
        }

    }

    public boolean hasTag(String tag) {
        for (String t : tags) {
            if (t.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public void addTag(String tag) {
        this.getTags().add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

}
