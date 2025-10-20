package modelo.clases.posicionables;

import modelo.clases.Coordenada;
import modelo.clases.DireccionLaser;
import modelo.clases.Laser;
import modelo.interfazes.Posicionable;

public class Emisor implements Posicionable {
    private final Coordenada coordenada;
    private final String origen;
    private final String direccion;

    public Emisor(Coordenada coordenada, String origen, String direccion) {
        this.coordenada = coordenada;
        this.origen = origen;
        this.direccion = direccion;
    }

    public String getOrigen() {
        return origen;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public Laser emitirLaser() {
        switch (origen){
            case "N":
                switch (direccion){
                    case "SE":
                        return new Laser(DireccionLaser.NaE, coordenada);
                    case "SW":
                        return new Laser(DireccionLaser.NaO, coordenada);
                    case "NE":
                        return new Laser(DireccionLaser.SaE, new Coordenada(-1,0).sumar(coordenada));
                    case "NW":
                        return new Laser(DireccionLaser.SaO, new Coordenada(-1,0).sumar(coordenada));
                    default:
                        return null;
                }
            case "O":
                switch (direccion){
                    case "SE":
                        return new Laser(DireccionLaser.OaS, coordenada);
                    case "NE":
                        return new Laser(DireccionLaser.OaN, coordenada);
                    case "SW":
                        return new Laser(DireccionLaser.EaS, new Coordenada(0,-1).sumar(coordenada));
                    case "NW":
                        return new Laser(DireccionLaser.EaN, new Coordenada(0,-1).sumar(coordenada));
                    default:
                        return null;
                }
            case "S":
                switch (direccion){
                    case "NE":
                        return new Laser(DireccionLaser.SaE,coordenada);
                    case "NW":
                        return new Laser(DireccionLaser.SaO,coordenada);
                    default:
                        return null;
                }
            case "E":
                switch (direccion){
                    case "SW":
                        return new Laser(DireccionLaser.EaS,coordenada);
                    case "NW":
                        return new Laser(DireccionLaser.EaN,coordenada);
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
}
