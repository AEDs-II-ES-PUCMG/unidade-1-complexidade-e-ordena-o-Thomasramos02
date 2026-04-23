import java.util.Comparator;

/**
 * Critério B - Forma de Pagamento.
 * Desempate 1: pedidos pagos à vista recebem um desconto percentual sobre o valor total
 * Desempate 2: Código Identificador do pedido.
 */

//utilizei o enunciado do PDF
public class ComparadorCriterioB implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        int resultado = Integer.compare(o1.getFormaDePagamentoAvista(), o2.getFormaDePagamentoParcelado());
        if (resultado != 0) {
            return resultado;
        }

        resultado = Double.compare(o1.valorFinal(), o2.valorFinal());
        if (resultado != 0) {
            return resultado;
        }

        return Integer.compare(o1.getIdPedido(), o2.getIdPedido());
    }
}
