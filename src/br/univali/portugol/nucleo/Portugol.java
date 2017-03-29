
package br.univali.portugol.nucleo;

import br.univali.portugol.nucleo.asa.NoDeclaracao;
import br.univali.portugol.nucleo.bibliotecas.base.GerenciadorBibliotecas;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    
    private static Programa compilar(String codigo, boolean paraExecucao, File classPath, String caminhoJavac) throws ErroCompilacao
    {
        Compilador compilador = new Compilador();
        
        long start = System.currentTimeMillis();
        
        Programa programa = compilador.compilar(codigo, paraExecucao, classPath, caminhoJavac);
        
        long tempoCompilacao = System.currentTimeMillis() - start;
        
        String mensagem = String.format("compilação para %s em %d ms - tamanho código: %d", 
                (paraExecucao ? "execução": "análise"), tempoCompilacao, codigo.length());
        
        LOGGER.log(Level.INFO, mensagem);
        
        return programa;
    }
    
    public static Programa compilarParaAnalise(String codigo) throws ErroCompilacao
    {
        return compilar(codigo, false, null, null);
    }
    
    public static Programa compilarParaExecucao(String codigo, File classPath, String caminhoJavac) throws ErroCompilacao
    {
        return compilar(codigo, true, classPath, caminhoJavac);
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