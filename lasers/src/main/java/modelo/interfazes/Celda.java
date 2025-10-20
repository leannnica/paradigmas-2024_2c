package modelo.interfazes;
import modelo.clases.Laser;

import java.util.List;

public interface Celda {
    public List<Laser> procesarLazer(Laser laser);
    public Laser validarLaserCelda(Laser laser);
    public boolean recibirBloque(Bloque bloque);
    public boolean enviarBloque(Celda celda);
    public Bloque getBloque();
}


