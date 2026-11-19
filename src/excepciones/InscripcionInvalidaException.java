package excepciones;

public class InscripcionInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InscripcionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
