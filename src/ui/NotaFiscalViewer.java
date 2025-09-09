package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entities.ItemNota;
import entities.NotaFiscal;
import util.CpfCnpjUtils;

public class NotaFiscalViewer extends JDialog {

    public NotaFiscalViewer(JFrame parent, NotaFiscal nota) {

        super(parent, "Nota Fiscal", true);
        setSize(400, 600);
        setLocationRelativeTo(parent);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder sb = new StringBuilder();
        sb.append("SUPERMERCADO DO GALEGO LTDA\n");
        sb.append("CNPJ: 00.000.000/0001-00\n\n");

        sb.append("Cliente: ").append(nota.getCliente().getNome()).append("\n");
        sb.append("CPF/CNPJ: ").append(CpfCnpjUtils.formatar(nota.getCliente().getCpfCnpj())).append("\n\n");

        sb.append("----------------------------------------\n");
        sb.append(String.format(" %-15s %5s %7s %9s\n", "Produto", "Quantidade", "Unitario", "Subtotal"));
        sb.append("----------------------------------------\n");


        for (ItemNota item : nota.getItens()) {
            sb.append(String.format("%-15s %5d %7.2f %9.2f\n", item.getProduto().getDescricao(), item.getQuantidade(),
                    item.getProduto().getPreco(), item.getSubTotal()));

        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL: R$ %.2f\n", nota.getTotal()));
        sb.append("----------------------------------------\n");

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton fecharBtn = new JButton("Fechar");
        fecharBtn.addActionListener(e -> dispose());
        add(fecharBtn, BorderLayout.SOUTH);

    }

}
