package modelo.clases.celdas;
import modelo.clases.Laser;
import modelo.interfazes.Bloque;
import modelo.interfazes.Celda;

import java.util.ArrayList;
import java.util.List;

public class CeldaConPiso implements Celda{
    private Bloque bloque;

    public CeldaConPiso (){this.bloque = null;}

    public CeldaConPiso(Bloque bloque) {
        this.bloque = bloque;
    }

    @Override
    public List<Laser> procesarLazer(Laser laser) {
        List<Laser> lasers = new ArrayList<>();
        if (bloque != null){
            lasers = bloque.interactuarConLaser(laser);
        }else{
            lasers.add(laser.generarProximoLaser());
        }
        return lasers;
    }

    @Override
    public Laser validarLaserCelda(Laser laser) {
        if (bloque != null){
            laser = bloque.validarLazerBloque(laser);
        }
        return laser;
    }

    @Override
    public boolean recibirBloque(Bloque bloque) {
        boolean recibido = false;
        if (this.bloque == null){
            this.bloque = bloque;
            recibido = true;
        }
        return recibido;
    }

    @Override
    public boolean enviarBloque(Celda celda) {
        boolean enviado = false;
        if (bloque != null) {
            if (celda.recibirBloque(bloque)){
                bloque = null;
                enviado = true;
            }
        }
        return enviado;
    }

    @Override
    public Bloque getBloque() {
        return bloque;
    }
}
