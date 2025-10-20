package modelo.clases;

import modelo.clases.bloques.*;
import modelo.clases.celdas.*;
import modelo.clases.posicionables.Emisor;
import modelo.clases.posicionables.Objetivo;
import modelo.interfazes.Celda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LeectorNivel {
    private int filas;
    private int columnas;
    private Tablero tablero;
    private final String nombre;

    public LeectorNivel(String nombreArchivo) {
        this.nombre = nombreArchivo;
    }

    public Tablero leerNivel() {
        InputStream inputStream = getClass().getResourceAsStream("/levels/" + nombre);

        if (inputStream == null) {
            System.out.println("No se pudo encontrar el archivo: " + nombre);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lineasTablero = new ArrayList<>();
            List<String> lineasEmisoresObjetivos = new ArrayList<>();
            String linea = reader.readLine();

            while (linea != null && !linea.isEmpty()) {
                lineasTablero.add(linea);
                linea = reader.readLine();
            }
            linea = reader.readLine();
            while (linea != null) {
                lineasEmisoresObjetivos.add(linea);
                linea = reader.readLine();
            }
            configurarTablero(lineasTablero);
            configurarEmisoresObjetivos(lineasEmisoresObjetivos);
            return tablero;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void configurarTablero(List<String> lineas) {
        filas = lineas.size();
        columnas = lineas.get(0).length();
        tablero = new Tablero(filas,columnas);
        for (int i = 0; i < filas; i++) {
            String linea = lineas.get(i);
            for (int j = 0; j < columnas; j++) {
                tablero.generarCelda(identificarCelda(linea.charAt(j)),i,j);
            }
        }
    }

    private void configurarEmisoresObjetivos(List<String> lineas) {

        for (String linea : lineas) {
            String[] partes = linea.split(" ");
            int columna = Integer.parseInt(partes[1]);
            boolean columnaEsPar = columna % 2 == 0;
            columna = columna / 2;
            int fila = Integer.parseInt(partes[2]) / 2;
            String origen = calcularOrigen(fila,columna,columnaEsPar);
            Coordenada coordenada = new Coordenada(fila,columna);
            coordenada = ajustarCoordenada(coordenada,origen);
            if (partes[0].equals("E")) {
                String direccion = partes[3];
                tablero.agregarEmisor(new Emisor(coordenada, calcularOrigen(fila,columna,columnaEsPar),direccion));

            } else if (partes[0].equals("G")) {
                tablero.agregarObjetivo(new Objetivo(coordenada, calcularOrigen(fila,columna,columnaEsPar)));
            }
        }
    }
    private String calcularOrigen(int fila, int columna, boolean columnaEsPar) {
        String origen;
        if (fila == filas){
            origen = "S";
        } else if (columna == columnas) {
            origen = "E";
        } else if (columnaEsPar) {
            origen = "O";
        } else {
            origen = "N";
        }
        return origen;
    }
    private Coordenada ajustarCoordenada(Coordenada coordenada, String origen) {
        switch (origen){
            case "S":
                coordenada = new Coordenada(coordenada.getX() - 1, coordenada.getY());
                return coordenada;
            case "E":
                coordenada = new Coordenada(coordenada.getX(), coordenada.getY() - 1);
                return coordenada;
            default:
            return coordenada;
        }
    }

    private Celda identificarCelda(char caracter){
        switch (caracter){
            case ' ': return new CeldaVacia();
            case '.': return new CeldaConPiso();
            case 'F': return new CeldaConPiso(new BloqueOpacoFijo());
            case 'B': return new CeldaConPiso(new BloqueOpacoMovil());
            case 'R': return new CeldaConPiso(new BloqueEspejo());
            case 'G': return new CeldaConPiso(new BloqueVidrio());
            case 'C': return new CeldaConPiso(new BloqueCristal());
            default: return null;
        }
    }

}
