
package br.univali.portugol.nucleo;

import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.bibliotecas.base.GerenciadorBibliotecas;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz Fernando Noschang
 * 
 */

public final class Portugol
{    
    public static final String QUEBRA_DE_LINHA = "\n";
    
    private static final Logger LOGGER = Logger.getLogger(Portugol.class.getName());
    
    private static final ExecutorService servico = Executors.newCachedThreadPool();
    
    private static Programa compilar(String codigo, boolean paraExecucao) throws ErroCompilacao
    {
        Compilador compilador = new Compilador();
        
        long start = System.currentTimeMillis();
        
        Programa programa = compilador.compilar(codigo, paraExecucao);
        
        long tempoCompilacao = System.currentTimeMillis() - start;
        
        String mensagem = String.format("compilação para %s em %d ms - tamanho código: %d", 
                (paraExecucao ? "execução": "análise"), tempoCompilacao, codigo.length());
        
        LOGGER.log(Level.INFO, mensagem);
        
        return programa;
    }
    
    public static Programa compilarParaAnalise(String codigo) throws ErroCompilacao
    {
        return compilar(codigo, false);
    }
    
    public static Future<Programa> compilarParaExecucao(final String codigo) throws ErroCompilacao
    {
        Callable<Programa> tarefa = new Callable<Programa>()
        {
            @Override
            public Programa call() throws Exception
            {
                LOGGER.log(Level.INFO, "Invocando compilação para execução na thread {0}", Thread.currentThread().getName());
                return compilar(codigo, true);
            }
        };
        return servico.submit(tarefa);
    }
    
    public static String renomearSimbolo(String programa, int linha, int coluna, String novoNome) throws ErroAoRenomearSimbolo
    {
        return new RenomeadorDeSimbolos().renomearSimbolo(programa, linha, coluna, novoNome);
    }
    
    public static NoDeclaracao obterDeclaracaoDoSimbolo(String programa, int linha, int coluna) throws ErroAoTentarObterDeclaracaoDoSimbolo
    {
        return new RenomeadorDeSimbolos().obterDeclaracaoDoSimbolo(programa, linha, coluna);
    }
    
    public static GerenciadorBibliotecas getGerenciadorBibliotecas()
    {
        return GerenciadorBibliotecas.getInstance();
    }
}