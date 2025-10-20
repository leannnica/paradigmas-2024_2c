package modelo.clases.bloques;

import modelo.clases.Laser;
import modelo.interfazes.Bloque;

import java.util.ArrayList;
import java.util.List;

public class BloqueOpacoFijo implements Bloque {

    @Override
    public List<Laser> interactuarConLaser(Laser laser) {
        return new ArrayList<>();
    }

    @Override
    public Laser validarLazerBloque(Laser laser) { return null; }
}
