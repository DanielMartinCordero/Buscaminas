import javax.swing.*;

public class Boton extends JButton {
    //Una vez establecidas las coordenadas, nunca cambia
    private final int fila;
    private final int columna;

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
