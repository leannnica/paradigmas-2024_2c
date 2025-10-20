package vista;
import controlador.Controlador;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import modelo.clases.*;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;

import modelo.clases.bloques.BloqueEspejo;
import modelo.clases.bloques.BloqueOpacoFijo;
import modelo.clases.bloques.BloqueOpacoMovil;
import modelo.clases.bloques.BloqueVidrio;
import modelo.clases.celdas.CeldaConPiso;
import modelo.clases.celdas.CeldaVacia;
import modelo.clases.posicionables.Emisor;
import modelo.clases.posicionables.Objetivo;

import modelo.interfazes.Bloque;
import modelo.interfazes.Celda;
import modelo.interfazes.Posicionable;
import modelo.clases.DireccionLaser;

import java.net.URL;
import java.util.*;

public class Vista {
    private Pane gameArea;
    private GridPane grilla;
    public void mostrarVentana(Stage stage, Controlador controlador) {
        BorderPane raiz = new BorderPane();

        // ZONA DE BOTONES
        ObservableList<String> niveles = FXCollections.observableArrayList("Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6","Snake", "LAZER MAYHEM");
        ListView<String> listaNiveles = new ListView<>(niveles);
        listaNiveles.setPrefWidth(120);
        raiz.setLeft(listaNiveles);

        // ZONA DEL JUEGO
        gameArea = new Pane();
        gameArea.setStyle("-fx-background-color: lightgrey;");
        gameArea.setPrefSize(300, 400);
        raiz.setCenter(gameArea);

        controlador.registrarEventoSeleccionNivel(listaNiveles, raiz);


        Scene escena = new Scene(raiz, 640, 480);
        stage.setTitle("Tablero del Nivel");
        stage.setScene(escena);
        stage.show();
    }


    public void cargarNivel(Celda[][] matriz,List<Emisor> emisores,List<Objetivo> objetivos,List<Laser> lasers, BorderPane raiz, Controlador controlador) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        gameArea.getChildren().clear();

        grilla = new GridPane();
        grilla.setAlignment(Pos.CENTER);
        grilla.setStyle("-fx-background-color: lightgrey;");
        boolean tieneCruz = false;
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                StackPane contenedorCelda = new StackPane();
                int TAMANIO_CELDA = 50;
                Rectangle celdaVista = new Rectangle(TAMANIO_CELDA, TAMANIO_CELDA);
                Celda celda = matriz[fila][col];
                celdaVista.setStroke(Color.BLACK);
                if (celda instanceof CeldaVacia) {
                    celdaVista.setStroke(Color.LIGHTGRAY);
                    celdaVista.setFill(Color.LIGHTGRAY);
                } else if (celda instanceof CeldaConPiso) {
                    Bloque bloque = celda.getBloque();
                    if(bloque == null) {
                        celdaVista.setFill(Color.WHITE);
                    } else if(bloque instanceof BloqueOpacoFijo){
                        tieneCruz = true;
                        celdaVista.setFill(Color.rgb(91, 91, 91));
                    } else if(bloque instanceof BloqueOpacoMovil){
                        celdaVista.setFill(Color.rgb(90, 90, 90));
                    } else if(bloque instanceof BloqueEspejo){
                        celdaVista.setFill(Color.TEAL);
                    } else if(bloque instanceof BloqueVidrio){
                        celdaVista.setFill(Color.SKYBLUE);
                    } else {
                        celdaVista.setFill(Color.DARKTURQUOISE);
                    }
                }
                contenedorCelda.getChildren().add(celdaVista);
                if (tieneCruz){
                    dibujarCruz(contenedorCelda);
                    tieneCruz = false;
                }
                iterarPosisionables(emisores.iterator(), contenedorCelda, Color.RED, fila, col);
                iterarPosisionables(objetivos.iterator(), contenedorCelda, Color.BLUE, fila, col);
                contenedorCelda.getChildren().addAll(iterarLazers(lasers.iterator(), fila, col));
                controlador.registrarArrastre(contenedorCelda,celdaVista,fila, col,raiz);
                grilla.add(contenedorCelda, col, fila);
            }
        }
        raiz.setCenter(grilla);
    }

    public void actualizarNivel(List<Laser> lasers, Map<Rectangle,Coordenada> indices){
        for(Node stackPane : grilla.getChildren()){
            List<Node> lasersAEliminar = new ArrayList<>();
            List<Line> lasersADibujar = new ArrayList<>();
            StackPane contenedorCelda = (StackPane) stackPane;
            for(Node nodo : contenedorCelda.getChildren()){
                if (nodo instanceof Line){
                    if (((Line) nodo).getStroke().equals(Color.RED)){
                        lasersAEliminar.add(nodo);
                    }
                }else if (nodo instanceof Rectangle) {
                    Rectangle celda = (Rectangle) nodo;
                    Coordenada coordenada = indices.get(celda);
                    lasersADibujar = iterarLazers(lasers.iterator(), coordenada.getX(), coordenada.getY());
                }
            }
            contenedorCelda.getChildren().addAll(lasersADibujar);
            contenedorCelda.getChildren().removeAll(lasersAEliminar);
        }
    }

    private List<Line> iterarLazers(Iterator<Laser> iterador, int fila, int col) {
        Laser laser;
        Coordenada coordenada;
        List<Line> laserDibujados = new ArrayList<>();
        while (iterador.hasNext()) {
            laser = iterador.next();
            coordenada = laser.getCoordenadaLaser();
            if(coordenada.getX() == fila && coordenada.getY() == col) {
                laserDibujados.add(dibujarLaser(laser.getDireccionLaser()));
                iterador.remove();
            }
        }
        return laserDibujados;
    }

    private void iterarPosisionables(Iterator<? extends Posicionable> iterador, Pane contenedorCelda, Color color, int fila, int col) {
        Posicionable posicionable;
        Coordenada coordenada;
        while (iterador.hasNext()) {
            posicionable = iterador.next();
            coordenada = posicionable.getCoordenada();
            if(coordenada.getX() == fila && coordenada.getY() == col) {
                dibujarCirculo(posicionable.getOrigen(), color, contenedorCelda);
                iterador.remove();
            }
        }
    }

    private void dibujarCruz (Pane contenedorCelda) {
        double TAMANIO_CELDA = 50;
        Line diagonal1 = new Line(0, 0, TAMANIO_CELDA-2, TAMANIO_CELDA-2);
        Line diagonal2 = new Line(0, TAMANIO_CELDA-2, TAMANIO_CELDA-2, 0);

        diagonal1.setStroke(Color.BLACK);
        diagonal1.setStrokeWidth(2);
        diagonal2.setStroke(Color.BLACK);
        diagonal2.setStrokeWidth(2);
        contenedorCelda.getChildren().addAll(diagonal1, diagonal2);
    }

    private void dibujarCirculo (String origen, Color color, Pane contenedorCelda) {
        Circle circle = new Circle(5, color);
        switch (origen) {
            case "O":
                circle.setTranslateX(-25);
                break;
            case "E":
                circle.setTranslateX(+25);
                break;
            case "N":
                circle.setTranslateY(-25);
                break;
            case "S":
                circle.setTranslateY(+25);
                break;
            default:
                break;
        }
        contenedorCelda.getChildren().add(circle);

    }
    private Line dibujarLaser(DireccionLaser direccionLaser) {
        Line laser = new Line();
        double TAMANIO_CELDA = 50;
        laser.setStroke(Color.RED);
        switch (direccionLaser) {
            case NaE:
            case EaN:
                laser.setStartX(0);
                laser.setStartY(0);
                laser.setEndX(TAMANIO_CELDA / 2);
                laser.setEndY(TAMANIO_CELDA / 2);
                laser.setTranslateX(+12.5);
                laser.setTranslateY(-12.5);
                break;
            case SaO:
            case OaS:
                laser.setStartX(0);
                laser.setStartY(0);
                laser.setEndX(TAMANIO_CELDA / 2);
                laser.setEndY(TAMANIO_CELDA / 2);
                laser.setTranslateX(-12.5);
                laser.setTranslateY(+12.5);
                break;

            case EaS:
            case SaE:
                laser.setStartX(TAMANIO_CELDA / 2);
                laser.setStartY(0);
                laser.setEndX(0);
                laser.setEndY(TAMANIO_CELDA / 2);
                laser.setTranslateX(+12.5);
                laser.setTranslateY(+12.5);
                break;
            case NaO:
            case OaN:
                laser.setStartX(TAMANIO_CELDA / 2);
                laser.setStartY(0);
                laser.setEndX(0);
                laser.setEndY(TAMANIO_CELDA / 2);
                laser.setTranslateX(-12.5);
                laser.setTranslateY(-12.5);
                break;
            case H:
                laser.setStartX(0);
                laser.setStartY(24);
                laser.setEndX(48);
                laser.setEndY(24);
                break;
            case V:
                laser.setStartX(24);
                laser.setStartY(0);
                laser.setEndX(24);
                laser.setEndY(48);
                break;
            default:
                throw new IllegalArgumentException();
        }
        laser.setStrokeWidth(3);
        return laser;
    }

    public void victoria() {

        URL soundURL = getClass().getResource("/audio/victori.wav");
        if (soundURL != null) {
            AudioClip sound = new AudioClip(soundURL.toString());
            sound.play();
        } else {
            System.err.println("No se encontró el archivo de sonido");
        }
        grilla.setStyle("-fx-background-color: lightgreen;");
    }
}

