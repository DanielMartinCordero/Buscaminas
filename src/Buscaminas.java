import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Buscaminas extends JFrame {
    private static final int FILAS = 10;
    static final int COLUMNAS = 10;
    private final Boton[][] botones = new Boton[FILAS][COLUMNAS];
    static final int TOTAL_MINAS = 15;
    static final int MINA = -1;

    static final int[][] tablero = new int[FILAS][COLUMNAS];

    //Tablero
    public Buscaminas() {
        setTitle("Buscaminas");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(1, 1, 1, 1);

        //Función lambda para obtener el botón exacto que fue pulsado
        ActionListener accionPulsar = e -> {
            Boton btn = (Boton) e.getSource();
            pulsarBoton(btn.getFila(), btn.getColumna());
        };

        for(int i = 0; i<FILAS; i++){
            for(int j = 0; j<COLUMNAS; j++){
                Boton btn = new Boton(i, j);

                gbc.gridx=j;
                gbc.gridy=i;
                botones[i][j]= btn;

                //Le decimos a cada botón que debe hacer si es pulsado
                btn.addActionListener(accionPulsar);

                add(btn, gbc);

            }
        }
        colocarMinas();
        calcularMinasCercanas();

        for(int i = 0; i<FILAS; i++){

            for(int j = 0; j<COLUMNAS; j++){
                System.out.print(tablero[i][j]);
            }
            System.out.println();
        }
    }


    public void pulsarBoton(int fila, int columna){
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
            return;
        }
        Boton btn = botones[fila][columna];
        int valor = tablero[fila][columna];

        if (!btn.isEnabled()) {
            return;
        }

        btn.setEnabled(false);

        if (valor == -1){
            btn.setText("💣");
            btn.setBackground(Color.red);
            // Mostramos una ventana emergente de aviso
            JOptionPane.showMessageDialog(this, "¡BOOM! Has pisado una mina. Reiniciando...");
            revelarMinas();
            // Llamamos al reinicio
            finPartida();
            return; // Salimos del método para no ejecutar nada más
        }
        if (valor > 0) {
            btn.setText(String.valueOf(valor));
        } else {
            btn.setText(""); // Si es 0, la dejamos sin texto
            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (df != 0 || dc != 0) { // Evita llamarse a sí misma de nuevo
                        pulsarBoton(fila + df, columna + dc);
                    }
                }
            }
        }
    }
    public void finPartida(){
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = 0;           // Vuelve a estar vacía en memoria
                botones[i][j].setText("");    // Borramos números o bombas
                botones[i][j].setEnabled(true); // Permitimos clics de nuevo
                botones[i][j].setBackground(null); // Restauramos el color gris por defecto
            }
        }
        colocarMinas();
        calcularMinasCercanas();
    }

    public void revelarMinas(){
        for(int i = 0; i<FILAS; i++){
            for(int j = 0; j<COLUMNAS; j++){
                if(tablero[i][j] == MINA){
                    botones[i][j].setText("💣");
                    botones[i][j].setBackground(Color.red);
                }
            }
        }
    }
    public void colocarMinas(){
        Random rand = new Random();
        int colocadas = 0;

        while(colocadas<TOTAL_MINAS){
            int columna = rand.nextInt(COLUMNAS);
            int fila = rand.nextInt(FILAS);

            if(tablero[fila][columna]!=MINA) {
                tablero[fila][columna] = MINA;
                colocadas++;
            }
        }
    }

    public void calcularMinasCercanas(){
        for(int i = 0; i<FILAS;i++){
            for(int j = 0; j<COLUMNAS;j++){
                if(tablero[i][j]==MINA){
                    for(int df = -1; df<=1; df++){
                        for(int dc = -1; dc<=1; dc++){
                            int vecinoFila = i + df;
                            int vecinoColumna = j + dc;
                            if((vecinoFila >=0 && vecinoFila < FILAS) && (vecinoColumna >=0 && vecinoColumna < COLUMNAS)){
                                tablero[vecinoFila][vecinoColumna] =
                                        tablero[vecinoFila][vecinoColumna] == MINA ?
                                                MINA : ++tablero[vecinoFila][vecinoColumna];
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Buscaminas().setVisible(true);
        });

    }

}
