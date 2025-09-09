package ui;


import entities.Cliente;
import repository.ClienteRepository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ClienteFrame extends JFrame {

    private DefaultListModel<Cliente> listaModel;
    private JList<Cliente> listaClientes;
    private JTextField campoNome, campoCpfCnpj, campoEndereco;
    private ClienteRepository repository = new ClienteRepository();

    public ClienteFrame() {
        super("Cadastro de Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        //Listagem
        listaModel = new DefaultListModel<>();
        listaClientes = new JList<>(listaModel);
        add(new JScrollPane(listaClientes), BorderLayout.CENTER);



        //Formulário
        JPanel form = new JPanel(new GridLayout(3, 2));
        form.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        form.add(campoNome);

        form.add(new JLabel("CPF/CNPJ:"));
        campoCpfCnpj = new JTextField();
        form.add(campoCpfCnpj);

        form.add(new JLabel("Endereço:"));
        campoEndereco = new JTextField();
        form.add(campoEndereco);

        add(form, BorderLayout.NORTH);

        //Botões
        JPanel botoes = new JPanel();
        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCarregar = new JButton("Carregar");

        botoes.add(botaoAdicionar);
        botoes.add(botaoEditar);
        botoes.add(botaoRemover);
        botoes.add(botaoSalvar);
        botoes.add(botaoCarregar);

        add(botoes, BorderLayout.SOUTH);

        //Ações dos botões
        botaoAdicionar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String cpfCnpj = campoCpfCnpj.getText().trim();
            String endereco = campoEndereco.getText().trim();


            if (!nome.isEmpty() && !cpfCnpj.isEmpty() && !endereco.isEmpty()) {
               
                int id = listaModel.size() + 1; // Simple ID generation
                listaModel.addElement(new Cliente( id, nome, cpfCnpj, endereco));
                campoNome.setText("");
                campoCpfCnpj.setText("");
                campoEndereco.setText("");

            }else {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            }
        });

        botaoEditar.addActionListener(e -> {
            
            int index = listaClientes.getSelectedIndex();
            
            if (index != -1) {
                Cliente c = listaModel.getElementAt(index);
                String novoEndereco = JOptionPane.showInputDialog(this, "Novo Endereço:", c.getEndereco());

                if(novoEndereco != null && !novoEndereco.trim().isEmpty()) {
                    c.setEndereco(novoEndereco);
                    listaClientes.repaint();
                }
            }
        });

        botaoRemover.addActionListener(e -> {
            
            int index = listaClientes.getSelectedIndex();

            if(index != -1) {
                listaModel.remove(index);
            }
        });

        botaoSalvar.addActionListener(e -> {

            try {
                List<Cliente> clientes = new ArrayList<>();
                for(int i = 0; i < listaModel.size(); i++){
                    clientes.add(listaModel.getElementAt(i));
                }
                repository.salvar(clientes);
                JOptionPane.showMessageDialog(this, "Clientes salvos com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar clientes: " + ex.getMessage());
            }
        });


        botaoCarregar.addActionListener(e -> {

            try{
                listaModel.clear();
                List<Cliente> clientes = repository.carregar();
                clientes.forEach(listaModel::addElement);
                JOptionPane.showMessageDialog(this, "Clientes carregados com sucesso!");
            }catch(IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage());

            }
        });

        setVisible(true);
    }


}
