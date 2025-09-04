package entities;

public class Cliente {

    private String nome;
    private String cpfCnpj;
    private String endereco;

    public Cliente(String nome, String cpfCnpj, String endereco) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }
    public String getCpfCnpj() {
        return cpfCnpj;
    }
    public String getEndereco() {
        return endereco;
    }
    
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String toString() {
        return nome + " | " + cpfCnpj + " | " + endereco;
    }

}
