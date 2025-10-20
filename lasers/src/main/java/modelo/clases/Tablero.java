package modelo.clases;
import modelo.clases.posicionables.Emisor;
import modelo.clases.posicionables.Objetivo;
import modelo.interfazes.Celda;

import java.util.*;

public class Tablero {
    private final Celda[][] matriz;
    private final List<Laser> lasers;
    private final HashSet<Laser> lasersProcesados;
    private final List<Emisor> emisores;
    private final List<Objetivo> objetivos;

    public Tablero(int filas, int columnas) {
        objetivos = new ArrayList<>();
        emisores = new ArrayList<>();
        matriz = new Celda[filas][columnas];
        lasers = new ArrayList<>();
        lasersProcesados = new HashSet<>();
    }
    public boolean moverBloque(Coordenada origen, Coordenada destino){
        boolean movimientoRealizado = false;
        if (coordenadaValida(origen) && coordenadaValida(destino)){
            movimientoRealizado = (matriz[origen.getX()][origen.getY()].enviarBloque(matriz[destino.getX()][destino.getY()]));
        }
        return movimientoRealizado;
    }
    private void trazarLasers(Laser laser){
        if (coordenadaValida(laser.getCoordenadaLaser())){
            Laser laserATrazar = llamarValidarLazerCelda(laser, laser.getCoordenadaLaser());
            if (laserATrazar != null){
                lasers.add(laserATrazar);
            }
            lasersProcesados.add(laser);
            List<Laser> nuevosLasers = obtenerProximosLasers(laser, laser.getCoordenadaLaser());
            if (!nuevosLasers.isEmpty()) {
                for (Laser nuevoLaser : nuevosLasers) {
                    if(!lasersProcesados.contains(nuevoLaser)){
                       trazarLasers(nuevoLaser);
                    }
                }
            }
        }
    }

    private Laser llamarValidarLazerCelda(Laser laser, Coordenada coordenadaLaser) {
        return matriz[coordenadaLaser.getX()][coordenadaLaser.getY()].validarLaserCelda(laser);
    }

    private List<Laser> obtenerProximosLasers(Laser laser, Coordenada coordenadaLaser) {
        return matriz[coordenadaLaser.getX()][coordenadaLaser.getY()].procesarLazer(laser);
    }

    public void emitirLasers(){
        lasersProcesados.clear();
        Laser laser;
        for (Emisor emisor : emisores) {
            laser = emisor.emitirLaser();
            trazarLasers(laser);
        }
    }

    public boolean victoriaAlcanzada(){
        boolean victoria = true;
        boolean objetivoAlcanzado = false;
        List<Laser> lasersQueAlcanzanObjetivo;
        Iterator<Laser> iteratorLasers;
        Iterator <Objetivo> iteratorObjetivos = objetivos.iterator();
        Objetivo objetivo;
        Laser laser;
        while(victoria && iteratorObjetivos.hasNext()){
            objetivo = iteratorObjetivos.next();
            lasersQueAlcanzanObjetivo = objetivo.lasersQueLoAlcanzan();
            iteratorLasers = lasersQueAlcanzanObjetivo.iterator();
            while (!objetivoAlcanzado && iteratorLasers.hasNext()) {
                laser = iteratorLasers.next();
                objetivoAlcanzado = lasers.contains(laser);
            }
            if (objetivoAlcanzado) {
                objetivoAlcanzado = false;
            } else {
                victoria = false;
            }
        }
        return victoria;
    }

    private boolean coordenadaValida(Coordenada coordenada){
        int x = coordenada.getX();
        int y = coordenada.getY();
        return (0 <= x && x <= matriz.length - 1
                && 0 <= y && y <= matriz[0].length - 1);
    }

    public void generarCelda(Celda celda, int fila, int columna){
        matriz[fila][columna] = celda;
    }

    public void agregarObjetivo(Objetivo objetivo){
        objetivos.add(objetivo);
    }
    public void agregarEmisor(Emisor emisor){
        emisores.add(emisor);
    }
    public Celda[][] getMatriz() {
        return matriz;
    }
    public List<Laser> getLasers() {
        return lasers;
    }
    public List<Emisor> getEmisores() {
        return emisores;
    }
    public List<Objetivo> getObjetivos() {
        return objetivos;
    }
}
