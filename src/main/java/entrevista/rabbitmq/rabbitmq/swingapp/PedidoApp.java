package entrevista.rabbitmq.rabbitmq.swingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PedidoApp extends JFrame {
    private final JTextField produtoField;
    private final JTextField quantidadeField;
    private final JTextArea pedidosArea;
    private final ConcurrentHashMap<UUID, String> pedidosStatus;
    private final ScheduledExecutorService scheduler;

    public PedidoApp() {
        setTitle("Envio de Pedidos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // 3 linhas, 2 colunas
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Linha 1: Produto
        inputPanel.add(new JLabel("Produto:"));
        produtoField = new JTextField();
        inputPanel.add(produtoField);

        // Linha 2: Quantidade
        inputPanel.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        inputPanel.add(quantidadeField);

        // Linha 3: Botão (ocupa as duas colunas)
        JButton enviarButton = new JButton("Enviar Pedido");
        inputPanel.add(enviarButton);
        inputPanel.add(new JLabel()); // célula vazia para alinhar

        add(inputPanel, BorderLayout.NORTH);

        // Área de exibição
        pedidosArea = new JTextArea();
        pedidosArea.setEditable(false);
        add(new JScrollPane(pedidosArea), BorderLayout.CENTER);

        pedidosStatus = new ConcurrentHashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);

        enviarButton.addActionListener(e -> enviarPedido());
        iniciarPolling();
    }

    private void enviarPedido() {
        String produto = produtoField.getText();
        String quantidadeStr = quantidadeField.getText();

        if (produto.isEmpty() || quantidadeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            UUID id = UUID.randomUUID();
            PedidoRequest pedido = new PedidoRequest(id, produto, quantidade);

            // Enviar pedido ao backend
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(pedido);

            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/pedidos")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(PedidoApp.this, "Erro ao enviar pedido!", "Erro", JOptionPane.ERROR_MESSAGE));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        pedidosStatus.put(id, "ENVIADO, AGUARDANDO PROCESSO");
                        SwingUtilities.invokeLater(() ->
                                pedidosArea.append("Pedido ID: " + id + " | Produto: " + produto + " | Quantidade: " + quantidade + " - ENVIADO, AGUARDANDO PROCESSO\n"));
                    } else {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(PedidoApp.this, "Erro ao enviar pedido!", "Erro", JOptionPane.ERROR_MESSAGE));
                    }
                }
            });
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarPolling() {
        scheduler.scheduleAtFixedRate(() -> {
            pedidosStatus.forEach((id, status) -> {
                if (status.contains("AGUARDANDO PROCESSO")) {
                    verificarStatusPedido(id);
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void verificarStatusPedido(UUID id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/pedidos/status/" + id)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(PedidoApp.this, "Erro ao verificar status!", "Erro", JOptionPane.ERROR_MESSAGE));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String novoStatus = response.body().string();
                    pedidosStatus.put(id, novoStatus);
                    SwingUtilities.invokeLater(() ->
                            pedidosArea.append("Pedido ID: " + id + " - " + novoStatus + "\n"));
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoApp().setVisible(true));
    }

    // Classe interna para representar o pedido
    private static class PedidoRequest {
        private final UUID id;
        private final String produto;
        private final int quantidade;

        public PedidoRequest(UUID id, String produto, int quantidade) {
            this.id = id;
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public UUID getId() {
            return id;
        }

        public String getProduto() {
            return produto;
        }

        public int getQuantidade() {
            return quantidade;
        }
    }
}