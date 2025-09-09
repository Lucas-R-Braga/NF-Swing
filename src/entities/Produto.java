package entities;

public class Produto {

    private int id;
    private String descricao;
    private double preco;
    private int estoque; //quantidade total em estoque

    public Produto(int id, String descricao, double preco, int estoque) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }
    public String getDescricao() {
        return descricao;
    }
    public double getPreco() {
        return preco;
    }
    public int getEstoque() {
        return estoque;
    }
    

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    
    
    public void baixaEstoque(int quantidade) {
        if (quantidade <= estoque) {
            estoque -= quantidade;
        } else {
            throw new IllegalArgumentException("Estoque insuficiente");
        }
    }

    public void reposicaoEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade de reposição deve ser positiva");
        }
        
        estoque += quantidade;
        
    }

    @Override
    public String toString() {
        return String.format("%s - R$ %.2f (Estoque: %d)", descricao, preco, estoque);
    }


}
