package acontecimientos;

import torneo.Pareja;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CronistaDeLaSecta implements EscuchadorAcontecimientos {
    private final List<Acontecimiento> historial;
    private final Map<Pareja, PosicionTabla> registrosPorPareja;

    public CronistaDeLaSecta() {
        this.historial = new ArrayList<>();
        this.registrosPorPareja = new HashMap<>();
    }

    @Override
    public void notificar(Acontecimiento acontecimiento) {
        if (acontecimiento == null) {
            throw new IllegalArgumentException("El acontecimiento no puede ser nulo.");
        }
        historial.add(acontecimiento);

        if (acontecimiento instanceof ResultadoCombate resultado) {
            actualizarRegistro(
                    resultado.getPareja1(),
                    resultado.getRonda(),
                    resultado.getVidaTotalPareja1());
            actualizarRegistro(
                    resultado.getPareja2(),
                    resultado.getRonda(),
                    resultado.getVidaTotalPareja2());
        }
    }

    private void actualizarRegistro(Pareja pareja, int ronda, int vidaTotal) {
        PosicionTabla previo = registrosPorPareja.get(pareja);
        if (previo == null || ronda >= previo.getUltimaRonda()) {
            registrosPorPareja.put(pareja, new PosicionTabla(pareja, ronda, vidaTotal));
        }
    }

    public List<Acontecimiento> obtenerHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public List<PosicionTabla> generarTablaPosiciones() {
        List<PosicionTabla> tabla = new ArrayList<>(registrosPorPareja.values());
        Collections.sort(tabla);
        return Collections.unmodifiableList(tabla);
    }
}
