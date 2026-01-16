package kth.chem.ke_thermal_printer.core.exceptions;

import androidx.annotation.NonNull;

public class ConnectionException extends Exception {
    private final static long serialVersionUID = 1L;
    private String message;

    public ConnectionException(String message) {
        super(message);
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return "ConnectionException{" +
                "message='" + message + '\'' +
                '}';
    }
}
