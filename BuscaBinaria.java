import java.util.Comparator;

public class BuscaBinaria {

    private static int lowerBoundDescricao(Produto[] dados, String chave) {
        int esquerda = 0;
        int direita = dados.length;

        while (esquerda < direita) {
            int meio = esquerda + (direita - esquerda) / 2;
            String descricao = dados[meio].descricao.toLowerCase();
            if (descricao.compareTo(chave) < 0) {
                esquerda = meio + 1;
            } else {
                direita = meio;
            }
        }

        return esquerda;
    }

    /**
     * Realiza busca binária em um array ordenado de produtos
     * 
     * @param dados      Array de produtos ordenado segundo um comparador
     * @param alvo       Produto a ser procurado
     * @param comparador Comparador utilizado para ordenar o array
     * @return Índice do produto encontrado, ou -1 se não encontrado
     */
    public static int buscar(Produto[] dados, Produto alvo, Comparator<Produto> comparador) {
        if (dados == null || dados.length == 0) {
            return -1;
        }

        int esquerda = 0;
        int direita = dados.length - 1;

        while (esquerda <= direita) {
            int meio = esquerda + (direita - esquerda) / 2;
            int comparacao = comparador.compare(alvo, dados[meio]);

            if (comparacao == 0) {
                return meio; // Encontrado
            } else if (comparacao < 0) {
                direita = meio - 1; // Procurar na metade esquerda
            } else {
                esquerda = meio + 1; // Procurar na metade direita
            }
        }

        return -1; // Não encontrado
    }

    /**
     * Realiza busca binária usando o comparador padrão do Produto
     * 
     * @param dados Array de produtos ordenado por descrição
     * @param alvo  Produto a ser procurado
     * @return Índice do produto encontrado, ou -1 se não encontrado
     */
    public static int buscarPorDescricao(Produto[] dados, Produto alvo) {
        return buscar(dados, alvo, new ComparadorPorDescricao());
    }

    /**
     * Realiza busca binária usando o comparador por código (ID)
     * 
     * @param dados Array de produtos ordenado por código
     * @param alvo  Produto a ser procurado
     * @return Índice do produto encontrado, ou -1 se não encontrado
     */
    public static int buscarPorCodigo(Produto[] dados, Produto alvo) {
        return buscar(dados, alvo, new ComparadorPorCodigo());
    }

    /**
     * Busca parcial por descrição usando pesquisa binária para localizar a faixa
     * de prefixo e varrer apenas os candidatos contíguos.
     * 
     * @param dados  Array ordenado por descrição
     * @param trecho Prefixo da descrição
     * @return Índice do primeiro produto encontrado, ou -1 se não encontrado
     */
    public static int buscarPorDescricaoParcial(Produto[] dados, String trecho) {
        if (dados == null || dados.length == 0 || trecho == null || trecho.isBlank()) {
            return -1;
        }

        String chave = trecho.toLowerCase();
        int inicioFaixa = lowerBoundDescricao(dados, chave);

        for (int i = inicioFaixa; i < dados.length; i++) {
            String descricao = dados[i].descricao.toLowerCase();

            if (descricao.startsWith(chave)) {
                return i;
            }

            // Sai da faixa assim que não houver mais possibilidade de prefixo.
            if (descricao.compareTo(chave) > 0 && !descricao.startsWith(chave)) {
                break;
            }
        }

        return -1;
    }
}
