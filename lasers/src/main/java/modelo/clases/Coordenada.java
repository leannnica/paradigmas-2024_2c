package modelo.clases;

import java.util.Objects;

public class Coordenada {
    private int x;
    private int y;

    public Coordenada(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordenada sumar(Coordenada coordenada) {
        x += coordenada.getX();
        y += coordenada.getY();
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada coordenada = (Coordenada) o;
        return x == coordenada.x && y == coordenada.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}