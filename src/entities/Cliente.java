package entities;

import util.CpfCnpjUtils;

public class Cliente {

    private int id;
    private String nome;
    private String cpfCnpj;
    private String endereco;

    public Cliente(int id, String nome, String cpfCnpj, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.endereco = endereco;
    }

    public Cliente(String nomeCliente, String cpfCnpj) {
        this.nome = nomeCliente;
        this.cpfCnpj = cpfCnpj;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return String.format("%s (CPF/CNPJ: %s) - Endereço: %s",
                nome,
                CpfCnpjUtils.formatar(cpfCnpj),
                (endereco != null && !endereco.isEmpty() ? endereco : "Não informado"));
    }
}
