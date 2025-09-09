package ui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.util.List;

import java.awt.Font;
import java.io.IOException;

import entities.NotaFiscal;
import repository.NotaFiscalRepository;
import repository.ProdutoRepository;

public class HistoricoNotasFrame extends JFrame {

    private DefaultListModel<NotaFiscal> listaModel;
    private JList<NotaFiscal> listaNotas;

    private NotaFiscalRepository notaRepo = new NotaFiscalRepository();
    private ProdutoRepository produtoRepo = new ProdutoRepository();

    public HistoricoNotasFrame() {
        super("Histórico de Notas Fiscais");
        setSize(600, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listaModel = new DefaultListModel<>();
        listaNotas = new JList<>(listaModel);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaNotas.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Duplo clique para abrir os detalhes da NF
        listaNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    NotaFiscal nf = listaNotas.getSelectedValue();
                    if (nf != null) {
                        NotaFiscalViewer viewer = new NotaFiscalViewer(HistoricoNotasFrame.this, nf);
                        viewer.setVisible(true);
                    }
                }
            }
        });

        add(new JScrollPane(listaNotas), BorderLayout.CENTER);

        JButton btnRecarregar = new JButton("Carregar Notas");
        btnRecarregar.addActionListener(e -> carregarNotas());
        add(btnRecarregar, BorderLayout.SOUTH);

        carregarNotas(); // já carrega ao abrir
        setLocationRelativeTo(null);
    }

    private void carregarNotas() {
        listaModel.clear();
        try {
            List<NotaFiscal> notas = notaRepo.carregar(produtoRepo);
            if (notas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma nota fiscal encontrada.");
            } else {
                notas.forEach(listaModel::addElement);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + ex.getMessage());
        }
    }
}