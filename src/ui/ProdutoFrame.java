package ui;

import javax.swing.*;

import entities.Produto;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import repository.ProdutoRepository;

public class ProdutoFrame extends JFrame {

    private DefaultListModel<Produto> listaModel;
    private JList<Produto> listaProdutos;
    private JTextField campoDescricao, campoPreco, campoEstoque;
    private ProdutoRepository repository = new ProdutoRepository();

    
    public ProdutoFrame() {
        super("Cadastro de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        //Listagem
        listaModel = new DefaultListModel<>();
        listaProdutos = new JList<>(listaModel);
        add(new JScrollPane(listaProdutos), BorderLayout.CENTER);

        //Formulário
        JPanel form = new JPanel(new GridLayout(3, 2));
        form.add(new JLabel("Descrição:"));
        campoDescricao = new JTextField();
        form.add(campoDescricao);

        form.add(new JLabel("Preço:"));
        campoPreco = new JTextField();
        form.add(campoPreco);

        form.add(new JLabel("Estoque:"));
        campoEstoque = new JTextField();
        form.add(campoEstoque);

        add(form, BorderLayout.NORTH);

        //Botões
        JPanel botoes = new JPanel();
        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCarregar = new JButton("Carregar");
        JButton botaoRepor = new JButton("Repor o Estoque");

        botoes.add(botaoAdicionar);
        botoes.add(botaoEditar);
        botoes.add(botaoRemover);
        botoes.add(botaoSalvar);
        botoes.add(botaoCarregar);
        botoes.add(botaoRepor);
        

        add(botoes, BorderLayout.SOUTH);

        //Ações dos botões
        botaoAdicionar.addActionListener(e -> {

            try{
                String descricao = campoDescricao.getText().trim();
                double preco = Double.parseDouble(campoPreco.getText().trim());
                int estoque = Integer.parseInt(campoEstoque.getText().trim());

                if (!descricao.isEmpty()) {
                    int id = listaModel.size() + 1; // Simple ID generation
                    listaModel.addElement(new Produto(id, descricao, preco, estoque));
                    campoDescricao.setText("");
                    campoPreco.setText("");
                    campoEstoque.setText("");
                }else {
                    JOptionPane.showMessageDialog(this, "Descrição é obrigatória.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço e Estoque devem ser numéricos.");
            }    

        });

        botaoEditar.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index != -1) {

                Produto p = listaModel.getElementAt(index);
                String novaDescricao = JOptionPane.showInputDialog(this, "Nova Descrição:", p.getDescricao());
                String novoPrecoStr = JOptionPane.showInputDialog(this, "Novo Preço:", p.getPreco());
                String novoEstoqueStr = JOptionPane.showInputDialog(this, "Novo Estoque:", p.getEstoque());

                try {
                    if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
                        p.setDescricao(novaDescricao.trim());

                    }
                    if (novoPrecoStr != null) {
                        p.setPreco(Double.parseDouble(novoPrecoStr));
                    }
                    if (novoEstoqueStr != null) {
                        p.setEstoque(Integer.parseInt(novoEstoqueStr));
                    }
                    listaProdutos.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valores inválidos ao editar o produto: " + ex.getMessage());
                }
            }
        });


        botaoRemover.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if(index != -1) {
                listaModel.remove(index);
            }
        });

        botaoRepor.addActionListener(e -> {
            int index = listaProdutos.getSelectedIndex();
            if (index != -1) {
                Produto p = listaModel.getElementAt(index);
                String quantidadeStr = JOptionPane.showInputDialog(this, "Quantidade a adicionar ao estoque:");

                try {
                    int quantidade = Integer.parseInt(quantidadeStr);
                    if (quantidade > 0) {
                        p.reposicaoEstoque(quantidade);
                        listaProdutos.repaint();
                    }else {
                        JOptionPane.showMessageDialog(this, "Quantidade deve ser positiva.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valor inválido para quantidade: " );
                }
            }
        });

        botaoSalvar.addActionListener(e -> {
            
            try {
                List<Produto> produtos = new ArrayList<>();
                for (int i = 0; i < listaModel.size(); i++) {
                    produtos.add(listaModel.getElementAt(i));
                }
                repository.salvar(produtos);
                JOptionPane.showMessageDialog(this, "Produtos salvos com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar produtos: " + ex.getMessage());
            }
        });

        botaoCarregar.addActionListener(e -> {
            try{
                listaModel.clear();
                List<Produto> produtos = repository.carregar();
                produtos.forEach(listaModel::addElement);
                JOptionPane.showMessageDialog(this, "Produtos carregados com sucesso!");
            }catch(IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());

            }
        });

        setVisible(true);
    }
}
