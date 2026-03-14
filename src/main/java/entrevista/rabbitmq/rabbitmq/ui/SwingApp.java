package entrevista.rabbitmq.rabbitmq.ui;

        import entrevista.rabbitmq.rabbitmq.service.MessageProducer;

        import javax.swing.*;
        import java.awt.*;

        public class SwingApp extends JFrame {
            private final MessageProducer producer;

            public SwingApp(MessageProducer producer) {
                this.producer = producer;

                // Configuração da janela
                setTitle("Pedido App");
                setSize(400, 200);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLayout(new FlowLayout());

                // Adicionando o JLabel
                JLabel label = new JLabel("Insira seu pedido (Exemplo: Produto Teste, Quantidade: 10):");
                add(label);

                // Campo de entrada
                JTextField textField = new JTextField(20);
                add(textField);

                // Botão de envio
                JButton button = new JButton("Enviar");
                button.addActionListener(e -> {
                    String pedido = textField.getText();
                    if (!pedido.isEmpty()) {
                        producer.sendMessage(pedido);
                        JOptionPane.showMessageDialog(this, "Pedido enviado: " + pedido);
                        textField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Por favor, insira um pedido válido.");
                    }
                });
                add(button);

                // Tornar a janela visível
                setVisible(true);
            }
        }