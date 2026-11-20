package acontecimientos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
	
public abstract class Acontecimiento {
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String fechaHora;
    private final String descripcion;

    protected Acontecimiento(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripcion del acontecimiento no puede estar vacia");
        }
        this.fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
        this.descripcion = descripcion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + descripcion;
    }
}
