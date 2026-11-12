package excepciones;

public class ReglaEquipamientoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReglaEquipamientoException(String mensaje) {
        super(mensaje);
    }
}