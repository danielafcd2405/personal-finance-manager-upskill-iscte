package pt.upskill.projeto2.financemanager.categories;

import java.io.File;
import java.util.List;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Category {

    private String name;
    private List<String> tags;
    private static final long serialVersionUID = -9107819223195202547L;

    public Category(String name) {
        this.name = name;
    }

    /**
     * Função que lê o ficheiro categories e gera uma lista de {@link Category} (método fábrica)
     * Deve ser utilizada a desserialização de objetos para ler o ficheiro binário categories.
     *
     * @param file - Ficheiro onde estão apontadas as categorias possíveis iniciais, numa lista serializada (por defeito: /account_info/categories)
     * @return uma lista de categorias, geradas ao ler o ficheiro
     */
    public static List<Category> readCategories(File file) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Função que grava no ficheiro categories (por defeito: /account_info/categories) a lista de {@link Category} passada como segundo argumento
     * Deve ser utilizada a serialização dos objetos para gravar o ficheiro binário categories.
     * @param file
     * @param categories
     */
    public static void writeCategories(File file, List<Category> categories) {
        // TODO completar o código da função
    }

    public boolean hasTag(String tag) {
        // TODO Auto-generated method stub
        return false;
    }

    public void addTag(String tag) {
        // TODO acho que é desta forma
        this.getTags().add(this.getName());
    }

    public List<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

}
