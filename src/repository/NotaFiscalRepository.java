package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Cliente;
import entities.ItemNota;
import entities.NotaFiscal;
import entities.Produto;

public class NotaFiscalRepository {
    private static final String FILE_PATH = "data/notas_fiscais.txt";

    //Salva todas as notas fiscais em formato legivel
    public void salvar(List<NotaFiscal> notas) throws IOException {
        
        // Implementar lógica para salvar notas fiscais
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (NotaFiscal nf : notas) {
                bw.write("NF" + nf.getId() + ";" + nf.getCliente().getNome() + ";" + nf.getCliente().getCpfCnpj());
                bw.newLine();

                for (ItemNota item : nf.getItens()) {
                    Produto p = item.getProduto();
                    bw.write("Item;  " + p.getId() + ";" + p.getDescricao() + ";" + item.getQuantidade() + ";"
                            + p.getPreco()
                            + ";" + item.getSubTotal());
                    bw.newLine();
                }
                
                bw.write("Total; R$ " + nf.getTotal());
                bw.newLine();
                bw.write("--------------------------------------------------");
                bw.newLine();
            }
        }
    }

    public List<NotaFiscal> carregar(ProdutoRepository produtoRepo) throws IOException {

        // Implementar lógica para carregar notas fiscais
        List<NotaFiscal> notas = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists())
            return notas; // retorna uma lista vazia se não existir    

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            NotaFiscal notaAtual = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("NF")) {
                    String[] parts = line.split(";");
                    int id = Integer.parseInt(parts[0].substring(2));
                    String nomeCliente = parts[1];
                    String cpfCnpj = parts[2];

                    Cliente cliente = new Cliente(nomeCliente, cpfCnpj);
                    notaAtual = new NotaFiscal(id, cliente);
                    notas.add(notaAtual);

                } else if (line.startsWith("Item") && notaAtual != null) {
                    String[] parts = line.split(";");
                    int produtoId = Integer.parseInt(parts[1].trim());
                    String descricao = parts[2].trim();
                    int quantidade = Integer.parseInt(parts[3].trim());
                    double preco = Double.parseDouble(parts[4].trim());
                    //double subtotal = Double.parseDouble(parts[5].trim());                    

                    Produto produto = new Produto(produtoId, descricao, preco, 0);

                    notaAtual.adicionarItem(produto, quantidade);

                }
            }
        }
        return notas;
    }
    
    public void exportarParaPDF(List<NotaFiscal> notas) throws IOException {
        String pdfPath = "data/notas_fiscais.pdf";

        try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);
            document.open();

            com.itextpdf.text.Font tituloFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font cabecalhoFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font textoFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11);

        for (NotaFiscal nf : notas) {
            // Título centralizado
            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph("Nota Fiscal Nº " + nf.getId(), tituloFont);
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new com.itextpdf.text.Paragraph(" ")); // espaço em branco

            // Dados do cliente
            document.add(new com.itextpdf.text.Paragraph("Cliente: " + nf.getCliente().getNome(), textoFont));
            document.add(new com.itextpdf.text.Paragraph("CPF/CNPJ: " + nf.getCliente().getCpfCnpj(), textoFont));
            document.add(new com.itextpdf.text.Paragraph(" "));

            // Tabela de itens
            com.itextpdf.text.pdf.PdfPTable tabela = new com.itextpdf.text.pdf.PdfPTable(4); // 4 colunas
            tabela.setWidthPercentage(100);
            tabela.setSpacingBefore(10f);
            tabela.setSpacingAfter(10f);

            // Cabeçalhos
            tabela.addCell(new com.itextpdf.text.Phrase("Produto", cabecalhoFont));
            tabela.addCell(new com.itextpdf.text.Phrase("Quantidade", cabecalhoFont));
            tabela.addCell(new com.itextpdf.text.Phrase("Preço Unitário", cabecalhoFont));
            tabela.addCell(new com.itextpdf.text.Phrase("Subtotal", cabecalhoFont));

            // Linhas
            for (ItemNota item : nf.getItens()) {
                tabela.addCell(new com.itextpdf.text.Phrase(item.getProduto().getDescricao(), textoFont));
                tabela.addCell(new com.itextpdf.text.Phrase(String.valueOf(item.getQuantidade()), textoFont));
                tabela.addCell(new com.itextpdf.text.Phrase("R$ " + String.format("%.2f", item.getProduto().getPreco()), textoFont));
                tabela.addCell(new com.itextpdf.text.Phrase("R$ " + String.format("%.2f", item.getSubTotal()), textoFont));
            }

            document.add(tabela);

            // Total da nota
            com.itextpdf.text.Paragraph total = new com.itextpdf.text.Paragraph("Total: R$ " + String.format("%.2f", nf.getTotal()), cabecalhoFont);
            total.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(total);

            // Linha separadora
            document.add(new com.itextpdf.text.Paragraph("--------------------------------------------------"));
            document.add(new com.itextpdf.text.Paragraph(" "));
        }

            document.close();
        } catch (Exception e) {
        throw new IOException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }


}
