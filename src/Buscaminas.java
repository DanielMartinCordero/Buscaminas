import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class Buscaminas extends JFrame {
    private static final int FILAS = 10;
    static final int COLUMNAS = 10;
    private final Boton[][] botones = new Boton[FILAS][COLUMNAS];
    static final int TOTAL_MINAS = 15;
    static final int MINA = -1;

    static final int[][] tablero = new int[FILAS][COLUMNAS];

    // Control de estado de la partida
    private int casillasDestapadas = 0;
    private boolean partidaTerminada = false;

    // Componentes de los paneles
    private JLabel lblMinas;
    private JLabel lblEstado;
    private JButton btnReiniciar;

    public Buscaminas() {
        setTitle("Buscaminas");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Tres métodos de paneles
        panelSuperior();
        panelCentral();
        panelInferior();

        colocarMinas();
        calcularMinasCercanas();

        // Comprobación por consola
        for(int i = 0; i < FILAS; i++){
            for(int j = 0; j < COLUMNAS; j++){
                System.out.print(tablero[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private void panelSuperior() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("BUSCAMINAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        lblMinas = new JLabel("Minas en el tablero: " + TOTAL_MINAS, SwingConstants.CENTER);
        lblMinas.setFont(new Font("Arial", Font.PLAIN, 14));

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblMinas);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void panelCentral() {
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(1, 1, 1, 1);

        ActionListener accionPulsar = e -> {
            if (partidaTerminada) return; // Si terminó la partida, no responde a clics
            Boton btn = (Boton) e.getSource();
            pulsarBoton(btn.getFila(), btn.getColumna());
        };

        for(int i = 0; i < FILAS; i++){
            for(int j = 0; j < COLUMNAS; j++){
                Boton btn = new Boton(i, j);

                gbc.gridx = j;
                gbc.gridy = i;
                botones[i][j] = btn;

                btn.addActionListener(accionPulsar);
                panelCentral.add(btn, gbc);
            }
        }

        add(panelCentral, BorderLayout.CENTER);
    }

    private void panelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnReiniciar = new JButton("Nueva partida");
        btnReiniciar.setFocusable(false);
        btnReiniciar.addActionListener(e -> finPartida());

        lblEstado = new JLabel("Partida en curso");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));

        panelInferior.add(btnReiniciar);
        panelInferior.add(lblEstado);

        add(panelInferior, BorderLayout.SOUTH);
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

        // Caso derrota
        if (valor == MINA){
            partidaTerminada = true;
            lblEstado.setText("¡Has perdido!");
            revelarMinas(); // Se pintan todas las bombas antes del diálogo
            JOptionPane.showMessageDialog(this, "¡BOOM! Has pisado una mina.");
            return;
        }

        // Casilla segura descubierta
        casillasDestapadas++;

        if (valor > 0) {
            btn.setText(String.valueOf(valor));
        } else {
            btn.setText(""); // Si es 0
            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (df != 0 || dc != 0) {
                        pulsarBoton(fila + df, columna + dc);
                    }
                }
            }
        }

        // Caso victoria: 100 - 15 = 85 casillas
        if (casillasDestapadas == (FILAS * COLUMNAS) - TOTAL_MINAS && !partidaTerminada) {
            partidaTerminada = true;
            lblEstado.setText("¡Has ganado!");
            revelarMinas();
            JOptionPane.showMessageDialog(this, "¡Enhorabuena! Has despejado el tablero.");
        }
    }

    public void finPartida(){
        partidaTerminada = false;
        casillasDestapadas = 0;
        if (lblEstado != null) {
            lblEstado.setText("Partida en curso");
        }

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = 0;
                botones[i][j].setText("");
                botones[i][j].setEnabled(true);
                botones[i][j].setBackground(null);
            }
        }
        colocarMinas();
        calcularMinasCercanas();
    }

    public void revelarMinas(){
        for(int i = 0; i < FILAS; i++){
            for(int j = 0; j < COLUMNAS; j++){
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

        while(colocadas < TOTAL_MINAS){
            int columna = rand.nextInt(COLUMNAS);
            int fila = rand.nextInt(FILAS);

            if(tablero[fila][columna] != MINA) {
                tablero[fila][columna] = MINA;
                colocadas++;
            }
        }
    }

    public void calcularMinasCercanas(){
        for(int i = 0; i < FILAS; i++){
            for(int j = 0; j < COLUMNAS; j++){
                if(tablero[i][j] == MINA){
                    for(int df = -1; df <= 1; df++){
                        for(int dc = -1; dc <= 1; dc++){
                            int vecinoFila = i + df;
                            int vecinoColumna = j + dc;
                            if((vecinoFila >= 0 && vecinoFila < FILAS) && (vecinoColumna >= 0 && vecinoColumna < COLUMNAS)){
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