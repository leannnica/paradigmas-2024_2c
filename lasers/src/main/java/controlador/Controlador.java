package controlador;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import modelo.clases.Coordenada;
import modelo.clases.LeectorNivel;
import modelo.clases.Tablero;
import modelo.clases.posicionables.Emisor;
import modelo.clases.posicionables.Objetivo;
import vista.Vista;

import java.util.*;

public class Controlador {
    private final Map<Rectangle, Coordenada> indices = new HashMap<>();
    private final Vista vista;
    private static final Paint COLOR_CELDA_VACIA = Color.WHITE;
    private boolean victoria;
    private Tablero tablero;
    private List<Emisor> emisores;
    private List<Objetivo> objetivos;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.victoria = false;
    }
    private static final Set<Paint> objetosMobibles = new HashSet<>(
            Arrays.asList(Color.rgb(90, 90, 90), Color.TEAL, Color.SKYBLUE, Color.DARKTURQUOISE));

    public void registrarArrastre(StackPane contenedorCelda, Rectangle celda, int fila, int columna, BorderPane raiz) {
        indices.put(celda, new Coordenada(fila, columna));

        EventHandler<MouseEvent> mouseEntra = event -> {
            if(!victoria) {
                Paint color = celda.getFill();
                if (objetosMobibles.contains(color)) {
                    celda.setCursor(Cursor.HAND);
                }
            } else {
                celda.setCursor(Cursor.DEFAULT);
            }

            event.consume();
        };
        celda.setOnMouseEntered(mouseEntra);


         EventHandler<MouseEvent> arrastroMouse = event -> {
             if(!victoria) {
                 Paint color = celda.getFill();
                 if (objetosMobibles.contains(color)) {
                     var dragboard = celda.startDragAndDrop(TransferMode.COPY);
                     ClipboardContent contenido = new ClipboardContent();
                     contenido.putString("Attastrada");
                     dragboard.setContent(contenido);
                 }
             }
             event.consume();
         };
        celda.setOnDragDetected(arrastroMouse);


        EventHandler<DragEvent> arrastrarSobre = event -> {
            if(!victoria) {
                if (celda.getFill() == Color.WHITE) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
            }
            event.consume();
        };
        contenedorCelda.setOnDragOver(arrastrarSobre);


        EventHandler<DragEvent> soltarArrastre = event -> {
            if(!victoria) {
                if (celda.getFill() == COLOR_CELDA_VACIA) {
                    Rectangle celdaArrastrada = (Rectangle) event.getGestureSource();
                    Paint color = celdaArrastrada.getFill();
                    if (color.equals(Color.rgb(90, 90, 90))) {
                        celdaArrastrada.setFill(COLOR_CELDA_VACIA);
                        celda.setFill(color);
                    } else if (color.equals(Color.TEAL)) {
                        celdaArrastrada.setFill(COLOR_CELDA_VACIA);
                        celda.setFill(color);
                    } else if (color == Color.SKYBLUE) {
                        celdaArrastrada.setFill(COLOR_CELDA_VACIA);
                        celda.setFill(color);
                    } else if (color == Color.DARKTURQUOISE) {
                        celdaArrastrada.setFill(COLOR_CELDA_VACIA);
                        celda.setFill(color);
                    }
                    event.setDropCompleted(actualizarTablero(celdaArrastrada, celda));
                } else {
                    event.setDropCompleted(false);
                }
            }
            event.consume();
        };
        contenedorCelda.setOnDragDropped(soltarArrastre);

        EventHandler<DragEvent> soltarExitosamente = event -> {
            if(!victoria) {
                tablero.emitirLasers();
                victoria = tablero.victoriaAlcanzada();
                vista.actualizarNivel(tablero.getLasers(),indices);
                if (victoria) {
                    vista.victoria();
                }
            }
            event.consume();
        };
        contenedorCelda.setOnDragDone(soltarExitosamente);
    }

    private boolean actualizarTablero(Rectangle origen, Rectangle destino) {
        return tablero.moverBloque(indices.get(origen), indices.get(destino));
    }

    public void registrarEventoSeleccionNivel(ListView<String> listaNiveles, BorderPane raiz) {
        listaNiveles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String nombreArchivoNivel = obtenerNombreArchivo(newValue);
                generarTablero(nombreArchivoNivel);
                emisores = tablero.getEmisores();
                objetivos = tablero.getObjetivos();
                vista.cargarNivel(tablero.getMatriz(),new ArrayList<>(emisores),new ArrayList<>(objetivos),tablero.getLasers(), raiz, this);
            }
        });
    }

    private String obtenerNombreArchivo(String nivelSeleccionado) {
        switch (nivelSeleccionado) {
            case "Level 1": return "level1.dat";
            case "Level 2": return "level2.dat";
            case "Level 3": return "level3.dat";
            case "Level 4": return "level4.dat";
            case "Level 5": return "level5.dat";
            case "Level 6": return "level6.dat";
            case "Snake": return "level7.dat";
            case "LAZER MAYHEM": return "level8.dat";
            default: return "level1.dat";
        }
    }

    private void generarTablero(String nombreArchivo) {
        LeectorNivel leectorNivel = new LeectorNivel(nombreArchivo);
        tablero = leectorNivel.leerNivel();
        tablero.emitirLasers();
        victoria = tablero.victoriaAlcanzada();
        if(victoria){
            vista.victoria();
        }
    }
}



