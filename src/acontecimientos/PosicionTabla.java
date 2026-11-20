package acontecimientos;

import torneo.Pareja;

public class PosicionTabla implements Comparable<PosicionTabla> {
    private final Pareja pareja;
    private final int ultimaRonda;
    private final int vidaTotalFinal;

    public PosicionTabla(Pareja pareja, int ultimaRonda, int vidaTotalFinal) {
        if (pareja == null) {
            throw new IllegalArgumentException("La pareja no puede ser nula.");
        }
        this.pareja = pareja;
        this.ultimaRonda = ultimaRonda;
        this.vidaTotalFinal = vidaTotalFinal;
    }

    public Pareja getPareja() {
        return pareja;
    }

    public int getUltimaRonda() {
        return ultimaRonda;
    }

    public int getVidaTotalFinal() {
        return vidaTotalFinal;
    }

    @Override
    public int compareTo(PosicionTabla otra) {
        int cmpRonda = Integer.compare(otra.ultimaRonda, this.ultimaRonda);
        if (cmpRonda != 0) {
            return cmpRonda;
        }
        int cmpVida = Integer.compare(otra.vidaTotalFinal, this.vidaTotalFinal);
        if (cmpVida != 0) {
            return cmpVida;
        }
        return Integer.compare(this.pareja.getId(), otra.pareja.getId());
    }

    @Override
    public String toString() {
        return pareja + " | Última ronda: " + ultimaRonda + " | Vida total final: " + vidaTotalFinal;
    }
}
