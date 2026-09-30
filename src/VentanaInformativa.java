import javax.swing.*;
import java.awt.*;

public class VentanaInformativa extends JFrame {

    public VentanaInformativa() {
        setTitle("Instrucciones del Buscaminas");
        setSize(360, 220);
        setLocationRelativeTo(null);
        // IMPORTANTE: DISPOSE para cerrar solo esta ventana sin matar el juego
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTexto = new JLabel("<html><h3>Reglas básicas:</h3>"
                + "1. Haz clic en una casilla para destaparla.<br>"
                + "2. Los números indican cuántas bombas la rodean.<br>"
                + "3. Si pisas una bomba (💣), pierdes la partida.<br>"
                + "4. Si destapas todas las casillas libres, ¡ganas!</html>");

        lblTexto.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lblTexto, BorderLayout.CENTER);

        add(panel);
    }
}