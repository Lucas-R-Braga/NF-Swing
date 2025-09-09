package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotaFiscal {

    private int id;
    private Cliente cliente;
    private LocalDateTime data;
    private List<ItemNota> itens;
    


    public NotaFiscal(int id, Cliente cliente, LocalDateTime data, List<ItemNota> itens) {
        this.id = id;
        this.cliente = cliente;
        this.data = LocalDateTime.now();
        this.itens = new ArrayList<>();
    }

    public NotaFiscal(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public LocalDateTime getData() {
        return data;
    }

    public List<ItemNota> getItens() {
        return itens;
    }
    

    public void adicionarItem(Produto produto, int quantidade) {
        ItemNota item = new ItemNota(produto, quantidade);
        itens.add(item);
    }


    public double getTotal() {
        return itens.stream().mapToDouble(ItemNota::getSubTotal).sum();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== Nota Fiscal ========== \n");
        sb.append("NF: ").append(id).append("\n");
        sb.append("Cliente: ").append(cliente.getNome());
        sb.append(" | CPF/CNPJ: ").append(cliente.getCpfCnpj()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-5s %-20s %-8s %-10s %-10s\n",
                "ID", "Descrição", "Quantidade", "Preço", "Subtotal"));
        sb.append("----------------------------------------\n");
        
        for(ItemNota item : itens){
            Produto p = item.getProduto();
            sb.append(String.format("%-5d %-20s %-8d %-10.2f %-10.2f\n", p.getId(), p.getDescricao(),
                    item.getQuantidade(), p.getPreco(), item.getSubTotal()));
        };
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL: R$ %.2f\n", getTotal()));
        sb.append("========================================\n");

        return sb.toString();
        
    }
}
