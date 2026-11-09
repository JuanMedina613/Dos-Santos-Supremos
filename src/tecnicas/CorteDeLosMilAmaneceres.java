package tecnicas;

import combatientes.Combatiente;

public class CorteDeLosMilAmaneceres extends Tecnica {
    public CorteDeLosMilAmaneceres() { super("Corte de los Mil Amaneceres", 15); }

    @Override
    public void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        int danio = ejecutor.calcularDanio(25);
        objetivo.restarVida(objetivo.recibirDanio(danio));
    }
}