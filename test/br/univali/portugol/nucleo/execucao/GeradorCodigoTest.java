package br.univali.portugol.nucleo.execucao;

import br.univali.portugol.nucleo.analise.AnalisadorAlgoritmo;
import br.univali.portugol.nucleo.asa.ASAPrograma;
import java.io.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Elieser
 */
public class GeradorCodigoTest
{
    private final AnalisadorAlgoritmo analisador = new AnalisadorAlgoritmo();
    private final GeradorCodigoJava gerador = new GeradorCodigoJava();
    
    @Test
    public void testGeracaoFuncaoRetornandoArrays() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "	funcao inteiro retornaInteiro() {                       \n"
                + "	}                                                       \n"                
                + "	funcao inteiro[] retornaArray() {                       \n"
                + "	}                                                       \n"                
                + "	funcao inteiro[][] retornaMatriz() {                    \n"
                + "	}                                                       \n"                                
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "   private int retornaInteiro()                               \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "   private int[] retornaArray()                              \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "   private int[][] retornaMatriz()                           \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testGeracaoFuncaoComParametrosQueSaoArrays() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "	funcao teste(inteiro x[], real c[][]) {                 \n"
                + "	}                                                       \n"                
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "   private void teste(int x[], double c[][])                 \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testGeracaoFuncaoComParametros() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "	funcao testando(inteiro x, real y) {                    \n"
                + "	}                                                       \n"                
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "   private void testando(int x, double y)                    \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testGeracaoFuncoesSimples() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "	funcao testando() {                                     \n"
                + "	}                                                       \n"                
                + "	funcao maisUma() {                                      \n"
                + "	}                                                       \n"                                
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "   private void testando()                                   \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "   private void maisUma()                                     \n"
                + "   {                                                         \n"
                + "   }                                                         \n"                
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testInclusaoBibliotecasComESemAliases() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	inclua biblioteca Graficos --> g                        \n"
                + "	inclua biblioteca Mouse                                 \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Graficos;      \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Mouse;         \n"                
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private Graficos g = new Graficos();                      \n"
                + "   private Mouse Mouse = new Mouse();                        \n"
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testInclusaoBibliotecasComAliases() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	inclua biblioteca Graficos --> g                        \n"
                + "	inclua biblioteca Mouse --> m                           \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Graficos;      \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Mouse;         \n"                
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private Graficos g = new Graficos();                      \n"
                + "   private Mouse m = new Mouse();                            \n"
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testInclusaoBibliotecasSemAliases() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	inclua biblioteca Graficos                              \n"
                + "	inclua biblioteca Mouse                                 \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = ""
                + "package programas;                                           \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Graficos;      \n"
                + "import br.univali.portugol.nucleo.bibliotecas.Mouse;         \n"                
                + "                                                             \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private Graficos Graficos = new Graficos();               \n"
                + "   private Mouse Mouse = new Mouse();                        \n"
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testDeclaracaoConstantes() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	const inteiro i = 10                                    \n"
                + "	const cadeia c = \"teste\"                              \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = "package programas;                         \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private final int i =  10;                                \n"
                + "   private final String c = \"teste\";                       \n"                
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testVariaveisGlobaisInicializadasComExpressoes() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	cadeia c = \"teste\" + \" concatenacao\"                \n"
                + "	inteiro i = ((10 + 2 * 4/1) << 1)                       \n"
                + "     logico l = verdadeiro e verdadeiro ou falso            \n"
                + "     real r = 53.23 + 0.01                                   \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = "package programas;                         \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private String c = \"teste\" + \" concatenacao\";         \n"
                + "   private int i = ((10 + 2 * 4/1) << 1);                    \n"
                + "   private boolean l = true && true || false;                \n"
                + "   private double r = 53.23 + 0.01;                           \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testVariaveisGlobaisInicializadasComValoresSimples() throws Exception
    {
        String codigoPortugol = "programa {                                     \n"
                + "	inteiro i = 10                                          \n"
                + "	cadeia c = \"teste\"                                    \n"
                + "     logico l = verdadeiro                                   \n"
                + "     caracter c = 'a'                                        \n"
                + "     real r = 53.23                                          \n"
                + "	funcao inicio() {                                       \n"
                + "	}                                                       \n"
                + "}";

        String codigoJavaEsperado = "package programas;                         \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private int i =  10;                                      \n"
                + "   private  String c = \"teste\";                            \n"                
                + "   private  boolean l = true;                                \n"                
                + "   private  char c = 'a';                                    \n"
                + "   private  double r = 53.23;                                \n"                
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testDeclaracaoVariaveisGlobaisComoAtributosDoPrograma() throws Exception
    {
        String codigoPortugol = "programa {         \n"
                + "	inteiro a, b                \n"
                + "	cadeia c                    \n"
                + "     logico d                    \n"
                + "     caracter e1                 \n"
                + "     real f                      \n"
                + "	funcao inicio() {           \n"
                + "	}\n                         \n"
                + "}";

        String codigoJavaEsperado = "package programas;                         \n"
                + "import br.univali.portugol.nucleo.Programa;                  \n"
                + "public class ProgramaTeste extends Programa                  \n"
                + "{                                                            \n"
                + "   private int a;                                            \n"
                + "   private int b;                                            \n"
                + "   private  String c;                                        \n"                
                + "   private  boolean d;                                       \n"                
                + "   private  char e1;                                         \n"
                + "   private  double f;                                        \n"                
                + "                                                             \n"
                + "   @override                                                 \n"
                + "   protected void executar() throws ErroExecucao             \n"
                + "   {                                                         \n"
                + "   }                                                         \n"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }
    
    @Test
    public void testProgramaVazio() throws Exception
    {
        String codigoPortugol = "programa {"
                + "	funcao inicio() {"
                + "	}\n"
                + "}";

        String codigoJavaEsperado = "package programas;"  
                + "import br.univali.portugol.nucleo.Programa;"

                + "public class ProgramaTeste extends Programa"
                + "{"
                + "   @override"
                + "   protected void executar() throws ErroExecucao"
                + "   {"
                + "   }"
                + "}";

        comparaCodigos(codigoPortugol, codigoJavaEsperado);
    }

    private void comparaCodigos(String codigoPortugol, String codigoJavaEsperado) throws Exception
    {
        analisador.analisar(codigoPortugol);
        ASAPrograma asa = (ASAPrograma) analisador.getASA();

        // gera o código e escreve em um ByteArrayOutputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        gerador.gera(asa, bos, "ProgramaTeste");

        String codigoGerado = bos.toString();
        //System.out.println(codigoGerado); // escreve o código gerado antes de remover a formatação        

        codigoGerado = codigoGerado.replaceAll("\\s+", ""); //remove todos os espaços e caracteres não visíveis
        codigoJavaEsperado = codigoJavaEsperado.replaceAll("\\s+", "");
        
        System.out.println(codigoJavaEsperado);
        System.out.println(codigoGerado);
        System.out.println();
        
        assertEquals("Os códigos não são iguais!", codigoJavaEsperado, codigoGerado);
    }
}