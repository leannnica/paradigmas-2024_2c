package modelo.interfazes;

import modelo.clases.Laser;
import java.util.List;

public interface Bloque {
    public List<Laser> interactuarConLaser(Laser laser);
    Laser validarLazerBloque(Laser laser);
}
