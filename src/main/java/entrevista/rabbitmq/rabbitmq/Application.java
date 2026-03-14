package entrevista.rabbitmq.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import entrevista.rabbitmq.rabbitmq.service.MessageProducer;
import entrevista.rabbitmq.rabbitmq.swingapp.PedidoApp;

import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		MessageProducer producer = context.getBean(MessageProducer.class);

		// Verifica se o ambiente suporta GUI
		if (!GraphicsEnvironment.isHeadless()) {
			// Agora chamamos a nova tela com dois campos
			javax.swing.SwingUtilities.invokeLater(() -> new PedidoApp().setVisible(true));
		} else {
			System.err.println("Ambiente headless detectado. A interface gráfica não será inicializada.");
		}
	}
}