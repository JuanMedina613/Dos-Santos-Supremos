package acontecimientos;

import java.util.List;

public class HeraldoImperial implements EscuchadorAcontecimientos {

    @Override
    public void notificar(Acontecimiento acontecimiento) {
        if (acontecimiento == null) {
            throw new IllegalArgumentException("El acontecimiento no puede ser nulo.");
        }
        System.out.println(acontecimiento);
    }

    public void anunciar(String mensaje) {
        if (mensaje != null && !mensaje.isBlank()) {
            System.out.println(mensaje);
        }
    }

    public void presentarTablaPosiciones(List<PosicionTabla> tabla) {
        if (tabla == null) {
            throw new IllegalArgumentException("La tabla de posiciones no puede ser nula.");
        }
        System.out.println("\n=== TABLA DE POSICIONES DEL TORNEO DE LOS INMORTALES ===");
        int puesto = 1;
        for (PosicionTabla fila : tabla) {
            System.out.println(puesto + "° puesto: " + fila);
            puesto++;
        }
        System.out.println("========================================================\n");
    }
}