package torneo;

import acontecimientos.EscuchadorAcontecimientos;
import acontecimientos.InicioCombate;
import acontecimientos.ResultadoCombate;
import combate.Combate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cruce {
    private final int ronda;
    private final int orden;
    private final Pareja pareja1;
    private final Pareja pareja2;
    private final List<EscuchadorAcontecimientos> escuchadores = new ArrayList<>();
    private Combate combate;
    private Pareja ganadora;

    public Cruce(int ronda, Pareja pareja1, Pareja pareja2) { this(ronda, 1, pareja1, pareja2); }

    public Cruce(int ronda, int orden, Pareja pareja1, Pareja pareja2) {
        if (ronda <= 0 || orden <= 0) throw new IllegalArgumentException("Ronda y orden deben ser positivos.");
        this.pareja1 = Objects.requireNonNull(pareja1, "La primera pareja no puede ser nula.");
        this.pareja2 = Objects.requireNonNull(pareja2, "La segunda pareja no puede ser nula.");
        if (pareja1.equals(pareja2)) throw new IllegalArgumentException("Una pareja no puede enfrentarse consigo misma.");
        this.ronda = ronda;
        this.orden = orden;
    }

    public void agregarEscuchador(EscuchadorAcontecimientos escuchador) {
        Objects.requireNonNull(escuchador, "El escuchador no puede ser nulo.");
        if (!escuchadores.contains(escuchador)) escuchadores.add(escuchador);
    }

    private void notificar(acontecimientos.Acontecimiento acontecimiento) {
        for (EscuchadorAcontecimientos e : new ArrayList<>(escuchadores)) e.notificar(acontecimiento);
    }

    public Pareja ejecutar() {
        if (ganadora != null) throw new IllegalStateException("El cruce ya fue ejecutado.");
        notificar(new InicioCombate(ronda, pareja1, pareja2));
        combate = new Combate(pareja1, pareja2);
        for (EscuchadorAcontecimientos e : escuchadores) combate.agregarEscuchador(e);
        ganadora = combate.ejecutar();
        notificar(new ResultadoCombate(ronda, pareja1, pareja1.getVidaTotal(), pareja2, pareja2.getVidaTotal(), ganadora));
        return ganadora;
    }

    public int getRonda() { return ronda; }
    public int getOrden() { return orden; }
    public Pareja getPareja1() { return pareja1; }
    public Pareja getPareja2() { return pareja2; }
    public Combate getCombate() { return combate; }
    public Pareja getGanadora() { return ganadora; }
    public boolean fueEjecutado() { return ganadora != null; }
}
