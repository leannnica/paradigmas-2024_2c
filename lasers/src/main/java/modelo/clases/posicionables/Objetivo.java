package modelo.clases.posicionables;

import modelo.clases.Coordenada;
import modelo.clases.DireccionLaser;
import modelo.clases.Laser;
import modelo.interfazes.Posicionable;

import java.util.ArrayList;
import java.util.List;

public class Objetivo implements Posicionable {
    private final Coordenada coordenada;
    private final String origen;

    public Objetivo(Coordenada coordenada, String origen) {
        this.coordenada = coordenada;
        this.origen = origen;
    }

    public String getOrigen() {
        return origen;
    }

    public Coordenada getCoordenada() { return coordenada; }

    public List<Laser> lasersQueLoAlcanzan() {
        List<Laser> lasers = new ArrayList<>();
        switch (origen){
            case "N":
                lasers.add(new Laser(DireccionLaser.V,coordenada));
                lasers.add(new Laser(DireccionLaser.OaN,coordenada));
                lasers.add(new Laser(DireccionLaser.EaN,coordenada));
                lasers.add(new Laser(DireccionLaser.V,new Coordenada(-1,0).sumar(coordenada)));
                lasers.add(new Laser(DireccionLaser.EaS,new Coordenada(-1,0).sumar(coordenada)));
                lasers.add(new Laser(DireccionLaser.OaS,new Coordenada(-1,0).sumar(coordenada)));
                return lasers;
            case "O":
                lasers.add(new Laser(DireccionLaser.H,coordenada));
                lasers.add(new Laser(DireccionLaser.SaO,coordenada));
                lasers.add(new Laser(DireccionLaser.NaO,coordenada));
                lasers.add(new Laser(DireccionLaser.H,new Coordenada(0,-1).sumar(coordenada)));
                lasers.add(new Laser(DireccionLaser.SaE,new Coordenada(0,-1).sumar(coordenada)));
                lasers.add(new Laser(DireccionLaser.NaE,new Coordenada(0,-1).sumar(coordenada)));
                return lasers;
            case "S":
                lasers.add(new Laser(DireccionLaser.V,coordenada));
                lasers.add(new Laser(DireccionLaser.OaS,coordenada));
                lasers.add(new Laser(DireccionLaser.EaS,coordenada));
                return lasers;
            case "E":
                lasers.add(new Laser(DireccionLaser.H,coordenada));
                lasers.add(new Laser(DireccionLaser.SaE,coordenada));
                lasers.add(new Laser(DireccionLaser.NaE,coordenada));
                return lasers;
            default:
                return lasers;
        }
    }
}
