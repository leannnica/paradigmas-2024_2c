package modelo.clases.bloques;

import modelo.clases.DireccionLaser;
import modelo.clases.Laser;
import modelo.interfazes.Bloque;

import java.util.List;

public class BloqueCristal implements Bloque {

    @Override
    public List<Laser> interactuarConLaser(Laser laser) {return laser.generarProximoLaserAtravezado();}

    @Override
    public Laser validarLazerBloque(Laser laser) {
        DireccionLaser direccionLaser = laser.getDireccionLaser();
        switch (direccionLaser){
            case H:
            case V:
                return laser;
            default:
                return null;
        }
    }
}
