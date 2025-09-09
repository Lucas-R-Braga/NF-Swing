package ui;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {

    public MenuFrame() {
        super("Menu Principal - Sistema de Notas Fiscais");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Sistema de Notas Fiscais", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel botoesPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton btnClientes = new JButton("Cadastro de Clientes");
        JButton btnProdutos = new JButton("Cadastro de Produtos");
        JButton btnNotaFiscal = new JButton("Emissão de Nota Fiscal");
        JButton btnHistorico = new JButton("Histórico de Notas");

        botoesPanel.add(btnClientes);
        botoesPanel.add(btnProdutos);
        botoesPanel.add(btnNotaFiscal);
        botoesPanel.add(btnHistorico);

        add(botoesPanel, BorderLayout.CENTER);

        // Ações dos botões
        btnClientes.addActionListener(e -> new ClienteFrame().setVisible(true));
        btnProdutos.addActionListener(e -> new ProdutoFrame().setVisible(true));
        btnNotaFiscal.addActionListener(e -> new NotaFiscalFrame().setVisible(true));
        btnHistorico.addActionListener(e -> new HistoricoNotasFrame().setVisible(true));

        setLocationRelativeTo(null);
        setVisible(true);
    }
}