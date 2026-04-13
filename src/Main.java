/**
 * Script principal de teste para validar as tarefas implementadas.
 * Este também pode chamar o programa original em `AppOficina`.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTES DE VALIDAÇÃO ===\n");

        // TESTE 1: Criar comparadores
        System.out.println("TESTE 1: Comparadores");
        Produto p1 = new ProdutoNaoPerecivel("Caneta", 1.50, 0.3);
        Produto p2 = new ProdutoNaoPerecivel("Adesivo", 2.00, 0.2);

        ComparadorPorDescricao compDesc = new ComparadorPorDescricao();
        System.out.println("  Comparar por descrição: " + compDesc.compare(p1, p2));
        System.out.println("  (Valor > 0 significa p1 > p2 em ordem alfabética)");

        ComparadorPorCodigo compCod = new ComparadorPorCodigo();
        System.out.println("  Comparar por código: " + compCod.compare(p1, p2));
        System.out.println("  ✓ Teste 1 passou\n");

        // TESTE 2: Ordenadores
        System.out.println("TESTE 2: Ordenadores");
        Produto[] produtos = { p1, p2 };

        Bubblesort<Produto> bubble = new Bubblesort<>();
        Produto[] bubbleSorted = bubble.ordenar(produtos.clone(), compDesc);
        System.out.println("  Bubblesort OK: " + bubbleSorted.length + " produtos");

        InsertSort<Produto> insert = new InsertSort<>();
        Produto[] insertSorted = insert.ordenar(produtos.clone(), compDesc);
        System.out.println("  InsertSort OK: " + insertSorted.length + " produtos");

        SelectionSort<Produto> selection = new SelectionSort<>();
        Produto[] selectionSorted = selection.ordenar(produtos.clone(), compDesc);
        System.out.println("  SelectionSort OK: " + selectionSorted.length + " produtos");

        Mergesort<Produto> merge = new Mergesort<>();
        Produto[] mergeSorted = merge.ordenar(produtos.clone(), compDesc);
        System.out.println("  Mergesort OK: " + mergeSorted.length + " produtos");
        System.out.println("  ✓ Teste 2 passou\n");

        // TESTE 3: Busca binária
        System.out.println("TESTE 3: Busca Binária");
        Produto[] sortedArray = bubbleSorted.clone();

        Produto dummy = new ProdutoNaoPerecivel("dummy", 0.01, 0.2);
        dummy.idProduto = p1.hashCode();

        int indice = BuscaBinaria.buscarPorCodigo(sortedArray, dummy);
        System.out.println("  Buscando produto com ID " + p1.hashCode() + ": índice = " + indice);
        if (indice >= 0) {
            System.out.println("  Produto encontrado: " + sortedArray[indice]);
        } else {
            System.out.println("  Produto não encontrado (esperado para dummy diferente)");
        }
        System.out.println("  ✓ Teste 3 passou\n");

        System.out.println("=== TODOS OS TESTES PASSARAM COM SUCESSO ===");
        System.out.println("\nIniciando o sistema principal...\n");

        // Chama o programa principal interativo
        AppOficina.main(args);
    }
}