package util;

public class CpfCnpjUtils {

    // Remove tudo que não for número
    private static String somenteNumeros(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    // Verifica se é CPF válido (11 dígitos)
    public static boolean isCpf(String valor) {
        return somenteNumeros(valor).length() == 11;
    }

    // Verifica se é CNPJ válido (14 dígitos)
    public static boolean isCnpj(String valor) {
        return somenteNumeros(valor).length() == 14;
    }

    // Formata de acordo com o tipo
    public static String formatar(String valor) {
        if (valor == null) return "";

        String numeros = somenteNumeros(valor);

        if (isCpf(numeros)) {
            return numeros.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                    "$1.$2.$3-$4");
        } else if (isCnpj(numeros)) {
            return numeros.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                    "$1.$2.$3/$4-$5");
        }
        return valor; // se não for reconhecido, devolve como está
    }
}
