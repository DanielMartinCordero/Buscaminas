import javax.swing.*;

public class Boton extends JButton {
    private int fila = 0;
    private int columna = 0;

    public Boton(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila(){
        return fila;
    }

    public int getColumna(){
        return columna;
    }
}
