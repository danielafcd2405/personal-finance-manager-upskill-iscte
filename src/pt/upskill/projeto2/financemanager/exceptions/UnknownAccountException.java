package pt.upskill.projeto2.financemanager.exceptions;

public class UnknownAccountException extends RuntimeException {
    public UnknownAccountException(String message) {
        super(message);
    }
}
