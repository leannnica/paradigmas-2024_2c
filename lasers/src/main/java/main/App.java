package main;

import controlador.Controlador;
import javafx.application.Application;

import javafx.stage.Stage;

import vista.Vista;


public class App extends Application {
    Vista vista = new Vista();
    Controlador controlador = new Controlador(vista);

    @Override
    public void start(Stage stage) throws Exception {
        vista.mostrarVentana(stage, controlador);
    }
}


