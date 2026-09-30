import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class Buscaminas extends JFrame {
    // Variables dinámicas (ya no son final ni static)
    private int filas = 10;
    private int columnas = 10;
    private int totalMinas = 15;
    private static final int MINA = -1;

    // Matrices dinámicas
    private int[][] tablero;
    private Boton[][] botones;

    // Control de estado de la partida
    private int casillasDestapadas = 0;
    private boolean partidaTerminada = false;

    // Componentes de los paneles
    private JPanel panelCentral;
    private JLabel lblMinas;
    private JLabel lblEstado;
    private JButton btnReiniciar;
    private JComboBox<String> comboDificultad;

    public Buscaminas() {
        setTitle("Buscaminas");
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 1. Añadimos la barra de menú
        crearMenu();

        // 2. Montamos los tres paneles
        panelSuperior();
        panelCentral();
        panelInferior();

        // 3. Arrancamos la primera partida
        finPartida();
    }

    // --- MENÚS (JMenuBar, JMenu, JMenuItem) ---
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú 1: Juego
        JMenu menuJuego = new JMenu("Juego");
        JMenuItem itemReiniciar = new JMenuItem("Reiniciar partida");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemReiniciar.addActionListener(e -> finPartida());
        itemSalir.addActionListener(e -> System.exit(0));

        menuJuego.add(itemReiniciar);
        menuJuego.addSeparator();
        menuJuego.add(itemSalir);

        // Menú 2: Tamaño del tablero
        JMenu menuTamano = new JMenu("Tamaño");
        JMenuItem item8x8 = new JMenuItem("Pequeño (8 x 8)");
        JMenuItem item10x10 = new JMenuItem("Estándar (10 x 10)");
        JMenuItem item12x12 = new JMenuItem("Grande (12 x 12)");

        item8x8.addActionListener(e -> cambiarTamano(8, 8));
        item10x10.addActionListener(e -> cambiarTamano(10, 10));
        item12x12.addActionListener(e -> cambiarTamano(12, 12));

        menuTamano.add(item8x8);
        menuTamano.add(item10x10);
        menuTamano.add(item12x12);

        // Menú 3: Ayuda (Abre el segundo JFrame)
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemInstrucciones = new JMenuItem("Cómo jugar");
        itemInstrucciones.addActionListener(e -> new VentanaInformativa().setVisible(true));

        menuAyuda.add(itemInstrucciones);

        menuBar.add(menuJuego);
        menuBar.add(menuTamano);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    private void panelSuperior() {
        JPanel panelSuperior = new JPanel(new GridLayout(3, 1));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblTitulo = new JLabel("BUSCAMINAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        lblMinas = new JLabel("Minas en el tablero: " + totalMinas, SwingConstants.CENTER);
        lblMinas.setFont(new Font("Arial", Font.PLAIN, 14));

        // Selector JComboBox con botón para aplicar dificultad
        JPanel panelDificultad = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelDificultad.add(new JLabel("Dificultad:"));

        String[] opciones = {"Fácil (10 minas)", "Medio (15 minas)", "Difícil (20 minas)"};
        comboDificultad = new JComboBox<>(opciones);
        comboDificultad.setSelectedIndex(1); // Medio por defecto

        JButton btnAplicarDificultad = new JButton("Aplicar");
        btnAplicarDificultad.setFocusable(false);
        btnAplicarDificultad.addActionListener(e -> {
            aplicarDificultad();
            finPartida();
        });

        panelDificultad.add(comboDificultad);
        panelDificultad.add(btnAplicarDificultad);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblMinas);
        panelSuperior.add(panelDificultad);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void panelCentral() {
        panelCentral = new JPanel(new GridBagLayout());
        add(panelCentral, BorderLayout.CENTER);
        recontruirTablero();
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

    private void cambiarTamano(int nuevasFilas, int nuevasColumnas) {
        this.filas = nuevasFilas;
        this.columnas = nuevasColumnas;
        recontruirTablero();
        finPartida();
    }

    private void aplicarDificultad() {
        int indice = comboDificultad.getSelectedIndex();
        if (indice == 0) totalMinas = 10;
        else if (indice == 1) totalMinas = 15;
        else if (indice == 2) totalMinas = 20;

        lblMinas.setText("Minas en el tablero: " + totalMinas);
    }

    // --- RECONSTRUCCIÓN DINÁMICA DEL TABLERO ---
    private void recontruirTablero() {
        panelCentral.removeAll(); // Borramos botones anteriores

        botones = new Boton[filas][columnas];
        tablero = new int[filas][columnas];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(1, 1, 1, 1);

        ActionListener accionPulsar = e -> {
            if (partidaTerminada) return;
            Boton btn = (Boton) e.getSource();
            pulsarBoton(btn.getFila(), btn.getColumna());
        };

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Boton btn = new Boton(i, j);
                gbc.gridx = j;
                gbc.gridy = i;
                botones[i][j] = btn;

                btn.addActionListener(accionPulsar);
                panelCentral.add(btn, gbc);
            }
        }

        // Obligatorio para refrescar la vista en Swing
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    public void pulsarBoton(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return;
        }
        Boton btn = botones[fila][columna];
        int valor = tablero[fila][columna];

        if (!btn.isEnabled()) {
            return;
        }

        btn.setEnabled(false);

        // Caso derrota
        if (valor == MINA) {
            partidaTerminada = true;
            lblEstado.setText("¡Has perdido!");
            revelarMinas();
            JOptionPane.showMessageDialog(this, "¡BOOM! Has pisado una mina.");
            return;
        }

        // Casilla segura
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

        // Caso victoria dinámico
        if (casillasDestapadas == (filas * columnas) - totalMinas && !partidaTerminada) {
            partidaTerminada = true;
            lblEstado.setText("¡Has ganado!");
            revelarMinas();
            JOptionPane.showMessageDialog(this, "¡Enhorabuena! Has ganado.");
        }
    }

    public void finPartida() {
        partidaTerminada = false;
        casillasDestapadas = 0;
        if (lblEstado != null) {
            lblEstado.setText("Partida en curso");
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = 0;
                botones[i][j].setText("");
                botones[i][j].setEnabled(true);
                botones[i][j].setBackground(null);
            }
        }
        colocarMinas();
        calcularMinasCercanas();
    }

    public void revelarMinas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == MINA) {
                    botones[i][j].setText("💣");
                    botones[i][j].setBackground(Color.RED);
                }
            }
        }
    }

    public void colocarMinas() {
        Random rand = new Random();
        int colocadas = 0;

        while (colocadas < totalMinas) {
            int columna = rand.nextInt(columnas);
            int fila = rand.nextInt(filas);

            if (tablero[fila][columna] != MINA) {
                tablero[fila][columna] = MINA;
                colocadas++;
            }
        }
    }

    public void calcularMinasCercanas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == MINA) {
                    for (int df = -1; df <= 1; df++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int vecinoFila = i + df;
                            int vecinoColumna = j + dc;
                            if ((vecinoFila >= 0 && vecinoFila < filas) && (vecinoColumna >= 0 && vecinoColumna < columnas)) {
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