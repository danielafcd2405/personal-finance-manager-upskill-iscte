package pt.upskill.projeto2.utils;

import javax.swing.*;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Menu {

    public static boolean yesOrNoInput(String message) {
        return JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
    }

    public static String requestInput(String message) {
        return JOptionPane.showInputDialog(message);
    }

    public static String requestSelection(String name, String[] options) {
        String option = ((String) JOptionPane.showInputDialog(null,
                "Escolha uma opção", name, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]));
        return option;
    }

    public static void showMessage(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }
    

}
