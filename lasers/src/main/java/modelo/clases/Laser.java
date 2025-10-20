package modelo.clases;

import java.util.List;
import java.util.Objects;

public class Laser {
    private final DireccionLaser direccionLaser;
    private final Coordenada coordenadaLaser;
    public Laser(DireccionLaser direccionLaser , Coordenada coordenadaLaser) {
        this.direccionLaser = direccionLaser;
        this.coordenadaLaser = coordenadaLaser;
    }

    public Coordenada getCoordenadaLaser() {
        return coordenadaLaser;
    }

    public void sumarCoordenada(Coordenada coordenada) {
        this.coordenadaLaser.sumar(coordenada);
    }
    public DireccionLaser getDireccionLaser() {
        return direccionLaser;
    }

    public Laser generarProximoLaser(){
        Laser nuevoLaser = this.direccionLaser.calcularLaser();
        nuevoLaser.sumarCoordenada(this.coordenadaLaser);
        return nuevoLaser;
    }

    public Laser generarProximoLazerEspejado(){
        Laser nuevoLaser = this.direccionLaser.calcularLazerEspejado();
        nuevoLaser.sumarCoordenada(this.coordenadaLaser);
        return nuevoLaser;
    }

    public List<Laser> generarProximoLaserAtravezado() {
        List<Laser> nuevosLasers = this.direccionLaser.calcularLaserAtravezado();
        if (!nuevosLasers.isEmpty()) {
            nuevosLasers.get(0).sumarCoordenada(this.coordenadaLaser);
            nuevosLasers.get(1).sumarCoordenada(this.coordenadaLaser);
        }
        return nuevosLasers;
    }
    @Override
    public String toString() {
        return String.format("Laser: dirreccion %s coordenada (%d, %d)", this.direccionLaser, this.coordenadaLaser.getX(), this.coordenadaLaser.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laser laser = (Laser) o;
        return direccionLaser == laser.direccionLaser && coordenadaLaser.equals(laser.coordenadaLaser);
    }
    @Override
    public int hashCode() {
        return Objects.hash(direccionLaser, coordenadaLaser);
    }
}
