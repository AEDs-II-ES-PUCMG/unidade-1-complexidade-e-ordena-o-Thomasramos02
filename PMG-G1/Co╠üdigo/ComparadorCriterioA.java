import java.util.Comparator;

/**
 * Critério A - Valor Final do Pedido (crescente).
 * Desempate 1: Volume Total de Itens (quantProdutos).
 * Desempate 2: Código Identificador do primeiro item do pedido.
 */
public class ComparadorCriterioA implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        int porValorFinal = Double.compare(o1.valorFinal(), o2.valorFinal());
        if (porValorFinal != 0) {
            return porValorFinal;
        }

        int porVolumeTotal = Integer.compare(o1.getTotalItens(), o2.getTotalItens());
        if (porVolumeTotal != 0) {
            return porVolumeTotal;
        }

        return Integer.compare(o1.getIdPrimeiroProduto(), o2.getIdPrimeiroProduto());
    }
}
