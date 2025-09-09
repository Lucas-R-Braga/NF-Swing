package entities;



public class ItemNota {
    private Produto produto;
    private int quantidade;

    public ItemNota(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");

        }
        if (quantidade > produto.getEstoque()) {
            throw new IllegalArgumentException("Quantidade maior que o estoque disponível.");

        }
        this.produto = produto;
        this.quantidade = quantidade;
        produto.baixaEstoque(quantidade); // Reduz o estoque do produto
    }
    

    public Produto getProduto() {
        return produto;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public double getSubTotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return String.format("%s x%d = R$ %.2f", 
        produto.getDescricao(), quantidade, produto.getPreco() * quantidade);
    }
}
