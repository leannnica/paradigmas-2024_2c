package modelo.clases.bloques;

import modelo.clases.Laser;
import modelo.interfazes.Bloque;

import java.util.ArrayList;
import java.util.List;

public class BloqueVidrio implements Bloque {

    @Override
    public List<Laser> interactuarConLaser(Laser laser) {
        List <Laser> lasers= new ArrayList<>();
        lasers.add(laser.generarProximoLaser());
        lasers.add(laser.generarProximoLazerEspejado());
        return lasers;
    }

    @Override
    public Laser validarLazerBloque(Laser laser) { return laser; }

}
