package repository;

import entities.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ClienteRepository {

    private static final String FILE_PATH = "data/clientes.txt";

    //  Salva toda a lista de clientes (reescreve o arquivo)
    public void salvar(List<Cliente> clientes) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Cliente c : clientes) {
                bw.write(c.getId() + ";" + c.getNome() + ";" + c.getCpfCnpj() + ";" + c.getEndereco());
                bw.newLine();
            }
        }
    }

    //  Carrega todos os clientes do arquivo
    public List<Cliente> carregar() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return clientes; // retorna uma lista vazia se não existir

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    Cliente c = new Cliente(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3]
                    );
                    clientes.add(c);
                }
            }
        }
        return clientes;
    }
}
