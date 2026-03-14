package entrevista.rabbitmq.rabbitmq;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import entrevista.rabbitmq.rabbitmq.swingapp.PedidoApp;

import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Configura o Spring Boot para NÃO rodar em modo headless, permitindo o uso do Swing
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run(args);

        // Verifica se o ambiente suporta GUI
        if (!GraphicsEnvironment.isHeadless()) {
            System.out.println("Ambiente com suporte a GUI detectado. Inicializando a interface gráfica...");
            javax.swing.SwingUtilities.invokeLater(() -> {
                PedidoApp app = new PedidoApp();
                app.setVisible(true);
            });
        } else {
            System.err.println("Ambiente headless detectado. A interface gráfica não será inicializada.");
            System.err.println("Certifique-se de que não está executando em um ambiente puramente de terminal ou servidor sem X11/Desktop.");
        }
    }
}
