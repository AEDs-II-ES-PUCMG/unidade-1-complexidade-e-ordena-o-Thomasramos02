import java.util.Comparator;

public class ComparadorPorValor implements Comparator<Pedido> {

	@Override
	public int compare(Pedido o1, Pedido o2) {
		int porValorFinal = Double.compare(o1.valorFinal(), o2.valorFinal());
		if (porValorFinal != 0) {
			return porValorFinal;
		}
		return Integer.compare(o1.getIdPedido(), o2.getIdPedido());
	}
}
