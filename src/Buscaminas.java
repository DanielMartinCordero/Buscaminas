import javax.swing.*;
import java.awt.*;


public class Buscaminas extends JFrame {
    private static final int FILAS = 10;
    static final int COLUMNAS = 10;
    static final JButton[][] botones = new JButton[FILAS][COLUMNAS];
    static final int TOTAL_MINAS = 15;

    static final int[][] tablero = new int[FILAS][COLUMNAS];

    //Tablero
    public Buscaminas() {
        setTitle("Buscaminas");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(1, 1, 1, 1);

        for(int i = 0; i<10; i++){
            for(int j = 0; j<10; j++){
                JButton jb = new JButton();

                gbc.gridx=j;
                gbc.gridy=i;
                botones[i][j]=jb;

                add(jb, gbc);

            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Buscaminas().setVisible(true);
        });
    }

}
