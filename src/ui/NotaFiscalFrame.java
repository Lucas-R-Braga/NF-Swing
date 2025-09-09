package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import entities.Cliente;
import entities.ItemNota;
import entities.NotaFiscal;
import entities.Produto;
import repository.ClienteRepository;
import repository.NotaFiscalRepository;
import repository.ProdutoRepository;

public class NotaFiscalFrame extends JFrame {

    private JComboBox<Cliente> comboClientes = new JComboBox<>();
    private JComboBox<Produto> comboProdutos = new JComboBox<>();
    private JTextField campoQuantidade = new JTextField();

    private DefaultListModel<ItemNota> listaModelItens;
    private JList<ItemNota> listaItens;
    private JLabel labelTotal;

    private ClienteRepository clienteRepo = new ClienteRepository();
    private ProdutoRepository produtoRepo = new ProdutoRepository();
    private NotaFiscalRepository notaRepo = new NotaFiscalRepository();

    private NotaFiscal notaAtual;
    private List<NotaFiscal> notasEmitidas = new ArrayList<>();

    public NotaFiscalFrame() {
        super("Emissão de Nota Fiscal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        //Painel seleção
        JPanel painelSelecao = new JPanel(new GridLayout(3, 2, 5, 5));
        painelSelecao.add(new JLabel("Cliente:"));
        painelSelecao.add(comboClientes);
        painelSelecao.add(new JLabel("Produto:"));
        painelSelecao.add(comboProdutos);
        painelSelecao.add(new JLabel("Quantidade:"));
        painelSelecao.add(campoQuantidade);
        add(painelSelecao, BorderLayout.NORTH);

        //Painel itens
        listaModelItens = new DefaultListModel<>();
        listaItens = new JList<>(listaModelItens);
        add(new JScrollPane(listaItens), BorderLayout.CENTER);

        //Rodapé
        JPanel painelRodape = new JPanel(new BorderLayout());
        labelTotal = new JLabel("Total: R$ 0.00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        painelRodape.add(labelTotal, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel();
        JButton botaoAdicionar = new JButton("Adicionar Item");
        JButton botaoEmitir = new JButton("Emitir Nota");
        JButton botaoSalvar = new JButton("Salvar Notas");
        JButton botaoCarregar = new JButton("Histórico de Notas");
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEmitir);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCarregar);
        painelRodape.add(painelBotoes, BorderLayout.EAST);
        add(painelRodape, BorderLayout.SOUTH);

        // ==== AÇÕES ====
        botaoAdicionar.addActionListener(e -> adicionarItem());
        botaoEmitir.addActionListener(e -> emitirNota());
        botaoSalvar.addActionListener(e -> salvarNotas());
        botaoCarregar.addActionListener(e -> carregarNotas());

        // ==== RECARREGAR SEMPRE QUE ABRIR ====
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                carregarDados();
            }
        });

        setLocationRelativeTo(null);
    }

    // ==== Novo método para carregar clientes e produtos ====
    private void carregarDados() {
        comboClientes.removeAllItems();
        comboProdutos.removeAllItems();
        try {
            clienteRepo.carregar().forEach(comboClientes::addItem);
            produtoRepo.carregar().forEach(comboProdutos::addItem);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }

    // ==== Lógica dos botões ====
    private void adicionarItem() {
        try {
            Cliente cliente = (Cliente) comboClientes.getSelectedItem();
            Produto produto = (Produto) comboProdutos.getSelectedItem();
            int quantidade = Integer.parseInt(campoQuantidade.getText());

            if (cliente == null || produto == null || quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione cliente, produto e informe uma quantidade válida.");
                return;
            }

            if (notaAtual == null || !notaAtual.getCliente().equals(cliente)) {
                notaAtual = new NotaFiscal(notasEmitidas.size() + 1, cliente);
                listaModelItens.clear();
            }

            notaAtual.adicionarItem(produto, quantidade);
            listaModelItens.addElement(new ItemNota(produto, quantidade));
            atualizarTotal();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
        }
    }

   private void emitirNota() {
    if (notaAtual != null && !notaAtual.getItens().isEmpty()) {
        notasEmitidas.add(notaAtual);

        // Abre o NotaFiscalViewer para exibir detalhes
        NotaFiscalViewer viewer = new NotaFiscalViewer(this, notaAtual);
        viewer.setVisible(true);

        JOptionPane.showMessageDialog(this, "Nota emitida com sucesso!");
        notaAtual = null;
        listaModelItens.clear();
        atualizarTotal();
    } else {
        JOptionPane.showMessageDialog(this, "Nenhum item para emitir a nota.");
    }
}


    private void salvarNotas() {
        try {
            notaRepo.salvar(notasEmitidas);
            JOptionPane.showMessageDialog(this, "Notas salvas com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar notas: " + ex.getMessage());
        }
    }

    private void carregarNotas() {
        try {
            notasEmitidas = notaRepo.carregar(produtoRepo);
            JOptionPane.showMessageDialog(this, "Notas carregadas com sucesso! Total: " + notasEmitidas.size());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + ex.getMessage());
        }
    }

    private void atualizarTotal() {
        if (notaAtual != null) {
            double total = notaAtual.getTotal();
            labelTotal.setText("Total: R$ " + String.format("%.2f", total));
        } else {
            labelTotal.setText("Total: R$ 0.00");
        }
    }
}
