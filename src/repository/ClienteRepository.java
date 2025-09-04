package repository;

import entities.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ClienteRepository {

    private static final String ARQUIVO = "data/clientes.txt";

    public void salvar(List<Cliente> clientes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Cliente c : clientes) {
                writer.write(c.getNome() + "," + c.getCpfCnpj() + "," + c.getEndereco());
                writer.newLine();
            }
        }
    }

    public List<Cliente> carregar() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        File file = new File(ARQUIVO);

        if (!file.exists())
            return clientes;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    clientes.add(new Cliente(parts[0], parts[1], parts[2]));
                }
            }


        }
        return clientes;
        
        

    }

}
