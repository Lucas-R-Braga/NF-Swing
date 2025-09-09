package repository;

import entities.Produto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private static final String FILE_PATH = "data/produtos.txt";

    //  Salva toda a lista de produtos (reescreve o arquivo)
    public void salvar(List<Produto> produtos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Produto p : produtos) {
                bw.write(p.getId() + ";" + p.getDescricao() + ";" + p.getPreco() + ";" + p.getEstoque());
                bw.newLine();
            }
        }
    }

    //  Carrega todos os produtos do arquivo
    public List<Produto> carregar() throws IOException {
        List<Produto> produtos = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists())
            return produtos; // retorna uma lista vazia se não existir

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    Produto p = new Produto(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Integer.parseInt(parts[3]));
                    produtos.add(p);
                }
            }
        }
        return produtos;
    }
    
    public Produto buscarPorID(int id) throws IOException {
        List<Produto> produtos = carregar();
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

}
