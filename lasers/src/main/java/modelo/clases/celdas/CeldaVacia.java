package modelo.clases.celdas;
import modelo.clases.Laser;
import modelo.interfazes.Bloque;
import modelo.interfazes.Celda;

import java.util.ArrayList;
import java.util.List;

public class CeldaVacia implements Celda{
    public CeldaVacia() {
    }
    @Override
    public List<Laser> procesarLazer(Laser laser){
        List <Laser> lasers = new ArrayList<>();
        lasers.add(laser.generarProximoLaser());
        return lasers;
    }

    @Override
    public Laser validarLaserCelda(Laser laser) {
        return laser;
    }

    @Override
    public boolean recibirBloque(Bloque bloque){return false; }

    @Override
    public boolean enviarBloque(Celda celda) {return false; }

    @Override
    public Bloque getBloque() {
        return null;
    }
}
