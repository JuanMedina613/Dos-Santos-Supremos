package acontecimientos;

import torneo.Pareja;

public class InicioCombate extends Acontecimiento {
	private final int ronda;
	private final Pareja pareja1;
	private final Pareja pareja2;
	
	public InicioCombate(int ronda, Pareja pareja1, Pareja pareja2) {
        super(formatearDescripcion(ronda, pareja1, pareja2));
        this.ronda = ronda;
        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
    }
	
	private static String formatearDescripcion(int ronda, Pareja pareja1, Pareja pareja2) {
        if (pareja1 == null || pareja2 == null) {
            throw new IllegalArgumentException("Las parejas del combate no pueden ser nulas.");
        }
        return "Ronda " + ronda + " - Comienza el combate entre " + pareja1 + " y " + pareja2 + ".";
    }
	
    public int getRonda() {
        return ronda;
    }

    public Pareja getPareja1() {
        return pareja1;
    }

    public Pareja getPareja2() {
        return pareja2;
    }
}
