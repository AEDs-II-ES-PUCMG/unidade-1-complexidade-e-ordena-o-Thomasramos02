
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    static Produto[] produtosPorID;
    static Produto[] produtosPorDescricao;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;

    // #region utilidades
    static Scanner teclado;

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Por descrição (Padrão)");
        System.out.println("2 - Por código (ID)");

        return lerNumero("Digite sua opção", Integer.class);
    }

    // #endregion
    static File localizarArquivoDados(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (arquivo.exists()) {
            return arquivo;
        }

        File pastaAtual = new File(".");
        File[] filhos = pastaAtual.listFiles();
        if (filhos != null) {
            for (File filho : filhos) {
                if (filho.isDirectory()) {
                    File candidato = new File(filho, nomeArquivo);
                    if (candidato.exists()) {
                        return candidato;
                    }
                }
            }
        }

        return arquivo;
    }

    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        quantProdutos = 0;
        File arquivo = localizarArquivoDados(nomeArquivo);

        try {
            dados = new Scanner(arquivo);
            int tamanho = Integer.parseInt(dados.nextLine());

            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado. Produtos não carregados");
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    /**
     * Cria duas cópias dos produtos ordenadas por ID e por descrição
     * Deve ser chamado após carregar os produtos
     */
    static void criarCopiasOrdenadas() {
        if (produtos != null && quantProdutos > 0) {
            // Criar cópia ordenada por ID
            produtosPorID = Arrays.copyOf(produtos, quantProdutos);
            Bubblesort<Produto> ordenadorID = new Bubblesort<>();
            produtosPorID = ordenadorID.ordenar(produtosPorID, new ComparadorPorCodigo());

            // Criar cópia ordenada por descrição
            produtosPorDescricao = Arrays.copyOf(produtos, quantProdutos);
            Bubblesort<Produto> ordenadorDesc = new Bubblesort<>();
            produtosPorDescricao = ordenadorDesc.ordenar(produtosPorDescricao, new ComparadorPorDescricao());

            System.out.println("Cópias ordenadas criadas com sucesso!");
        }
    }

    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        System.out.println("1 - Por ID");
        System.out.println("2 - Por descrição");
        int tipoSearch = lerNumero("Escolha o tipo de busca", Integer.class);

        Produto localizado = null;

        if (produtosPorID == null || produtosPorDescricao == null) {
            criarCopiasOrdenadas();
        }

        if (tipoSearch == 1) {
            // Busca por ID usando busca binária
            int numero = lerNumero("Digite o identificador do produto", Integer.class);
            // Criar um produto dummy para busca
            Produto dummy = new ProdutoNaoPerecivel("dummy", 0.01, 0.2);
            dummy.idProduto = numero;

            int indice = BuscaBinaria.buscarPorCodigo(produtosPorID, dummy);
            if (indice >= 0) {
                localizado = produtosPorID[indice];
            }
        } else if (tipoSearch == 2) {
            // Busca por descrição parcial usando busca binária por faixa de prefixo
            System.out.print("Digite o início da descrição do produto: ");
            String descricao = teclado.nextLine();

            int indice = BuscaBinaria.buscarPorDescricaoParcial(produtosPorDescricao, descricao);
            if (indice >= 0) {
                localizado = produtosPorDescricao[indice];
            }
        }

        return localizado;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados inválidos";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        System.out.println("Filtrando por valor máximo:");
        double valor = lerNumero("valor", Double.class);
        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() < valor)
                relatorio.append(produtos[i] + "\n");
        }
        System.out.println(relatorio.toString());
    }

    static void ordenarProdutos() {
        cabecalho();
        System.out.println("Selecionando método de ordenação e critério\n");

        // Selecionar método de ordenação
        int opcaoOrdenador = exibirMenuOrdenadores();
        if (opcaoOrdenador == 0)
            return;

        ordenador = switch (opcaoOrdenador) {
            case 1 -> new Bubblesort<>();
            case 2 -> new InsertSort<>();
            case 3 -> new SelectionSort<>();
            case 4 -> new Mergesort<>();
            default -> null;
        };

        if (ordenador == null) {
            System.out.println("Opção inválida!");
            return;
        }

        // Selecionar critério de comparação
        int opcaoComparador = exibirMenuComparadores();
        if (opcaoComparador == 0)
            return;

        // Criar cópia para ordenação
        Produto[] dadosOrdenados = Arrays.copyOf(produtos, quantProdutos);

        // Aplicar ordenação com o comparador escolhido
        if (opcaoComparador == 1) {
            // Por descrição
            dadosOrdenados = ordenador.ordenar(dadosOrdenados, new ComparadorPorDescricao());
        } else if (opcaoComparador == 2) {
            // Por código
            dadosOrdenados = ordenador.ordenar(dadosOrdenados, new ComparadorPorCodigo());
        }

        // Exibir estatísticas
        cabecalho();
        System.out.println("Ordenação realizada!");
        System.out.println("Comparações: " + ordenador.getComparacoes());
        System.out.println("Movimentações: " + ordenador.getMovimentacoes());
        System.out.printf("Tempo total: %.3f ms%n", ordenador.getTempoOrdenacao());
        System.out.println();

        // Perguntar se deseja substituir
        verificarSubstituicao(dadosOrdenados);
    }

    /**
     * Verifica com usuário se deseja sobrescrever dados originais
     * 
     * @param dadosOrdenados Array com dados ordenados
     */
    static void verificarSubstituicao(Produto[] dadosOrdenados) {
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)? ");
        String resposta = teclado.nextLine().toUpperCase();
        if (resposta.equals("S")) {
            produtos = Arrays.copyOf(dadosOrdenados, dadosOrdenados.length);
            criarCopiasOrdenadas();
            System.out.println("Dados substituídos com sucesso!");
        }
    }

    static void embaralharProdutos() {
        if (produtos != null && quantProdutos > 0) {
            Collections.shuffle(Arrays.asList(produtos));
        }
    }

    static void listarProdutos() {
        cabecalho();
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);
        if (produtos == null) {
            System.out.println("Não foi possível carregar os produtos. Encerrando aplicação.");
            teclado.close();
            return;
        }
        embaralharProdutos();
        criarCopiasOrdenadas(); // TAREFA 2: Criar cópias ordenadas

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> filtrarPorPrecoMaximo();
                case 3 -> ordenarProdutos();
                case 4 -> embaralharProdutos();
                case 5 -> listarProdutos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);
        teclado.close();
    }
}
