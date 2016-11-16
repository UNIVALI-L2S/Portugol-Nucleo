package br.univali.portugol.nucleo.execucao.gerador;

import br.univali.portugol.nucleo.asa.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Elieser
 */
public class PreCompilador extends VisitanteNulo
{

    private final Map<TipoDado, List<NoDeclaracaoVariavel>> declaracoes = new HashMap<>();
    private final Set<NoDeclaracaoFuncao> funcoesInvocadas = new HashSet<>(); // guarda apenas as funções que foram invocadas, as funções que não são invocadas não serão geradas no código Java
    
    private static long seedNomes = System.currentTimeMillis();
    
    @Override
    public Void visitar(NoChamadaFuncao chamadaFuncao) throws ExcecaoVisitaASA
    {
        chamadaFuncao.setNome(geraNomeValido(chamadaFuncao.getNome()));
        
        NoDeclaracaoFuncao declaracaoFuncao = chamadaFuncao.getOrigemDaReferencia();
        if (!funcoesInvocadas.contains(declaracaoFuncao))
        {
            funcoesInvocadas.add(declaracaoFuncao);
        }
        
        List<NoExpressao> parametrosPassados = chamadaFuncao.getParametros();
        List<NoDeclaracaoParametro> parametrosEsperados = Collections.EMPTY_LIST;
        if (declaracaoFuncao != null)
        {
            parametrosEsperados = declaracaoFuncao.getParametros();
        }

        for (int i = 0; i < parametrosPassados.size(); i++)
        {
            NoExpressao parametroPassado = parametrosPassados.get(i);
            
            parametroPassado.aceitar(this);
            
            NoDeclaracaoParametro parametroEsperado = (i < parametrosEsperados.size()) ? parametrosEsperados.get(i) : null;
            if (parametroPassado instanceof NoReferenciaVariavel)
            {
                if (parametroEsperado != null && parametroEsperado.getModoAcesso() == ModoAcesso.POR_REFERENCIA && chamadaFuncao.getEscopo() == null)
                {
                    NoReferenciaVariavel referencia = (NoReferenciaVariavel) parametroPassado;
                    if (referencia.getOrigemDaReferencia() instanceof NoDeclaracaoVariavel)
                    {
                        NoDeclaracaoVariavel origemReferencia = (NoDeclaracaoVariavel)referencia.getOrigemDaReferencia();
                    
                        TipoDado tipoOrigem = origemReferencia.getTipoDado();
                        if (!declaracoes.containsKey(tipoOrigem)) // verifica se é necessário criar uma lista para guardar as variáveis do tipo do nó de origem
                        {
                            declaracoes.put(tipoOrigem, new ArrayList<NoDeclaracaoVariavel>());
                        }
                    
                        List<NoDeclaracaoVariavel> variaveis = declaracoes.get(tipoOrigem);
                        if (!variaveis.contains(origemReferencia))
                        {
                            int indice = variaveis.size();
                            referencia.setIndiceReferencia(indice);
                            origemReferencia.setIndiceReferencia(indice);
                            variaveis.add(origemReferencia);
                            for (NoReferencia ref : origemReferencia.getReferencias())
                            {
                                NoReferenciaVariavel origem = (NoReferenciaVariavel) ref;
                                origem.setIndiceReferencia(indice);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Object visitar(NoDeclaracaoFuncao no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoReferenciaVariavel no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoReferenciaVetor no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoReferenciaMatriz no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoDeclaracaoVariavel no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoDeclaracaoVetor no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoDeclaracaoMatriz no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoDeclaracaoParametro no) throws ExcecaoVisitaASA
    {
        no.setNome(geraNomeValido(no.getNome()));
        return super.visitar(no);
    }
    
    @Override
    public Object visitar(NoVetor noVetor) throws ExcecaoVisitaASA
    {
        for (Object valor : noVetor.getValores())
        {
            if (valor instanceof NoExpressao)
            {
                ((NoExpressao)valor).aceitar(this);
            }
        }
        return null;
    }

    public Map<TipoDado, List<NoDeclaracaoVariavel>> getVariaveisPassadasPorReferencia()
    {
        return declaracoes;
    }

    public Set<NoDeclaracaoFuncao> getFuncoesQuerForamInvocadas()
    {
        return funcoesInvocadas;
    }
    
    @Override
    public Object visitar(ASAPrograma asa) throws ExcecaoVisitaASA
    {
        for (NoDeclaracao declaracao : asa.getListaDeclaracoesGlobais())
        {
            declaracao.aceitar(this);
        }
        return null;
    }
    
    private static final String[] NOMES_PROIBIDOS = {"inicializar", "executar", "concatena"};
    
    public static String geraNomeValido(String nomeAtual)
    {
        Set<String> termosProibidos = new HashSet<>(Arrays.asList(PreCompilador.NOMES_PROIBIDOS));
        
        if (!ehUmaPalavraReservadaNoJava(nomeAtual) && !termosProibidos.contains(nomeAtual))
        {
            return nomeAtual;
        }

        return nomeAtual + "_" + String.valueOf(seedNomes);
    }

    public static void setSeedGeracaoNomesValidos(long seed) // usado para poder setar um seed conhecido e escrever testes unitários onde é possível prever o nome das variáveis
    {
        seedNomes = seed;
    }
    
    public static long getSeedGeracaoNomesValidos()
    {
        return seedNomes;
    }
    
    private static boolean ehUmaPalavraReservadaNoJava(String nome)
    {
        return (Arrays.binarySearch(PALAVRAS_RESERVADAS_JAVA, nome) >= 0);
    }

    // lista de palavras reservadas java 'roubadas' da wikipedia e ordenadasalfabéticamente para possibilitar uma busca binária
    private static final String[] PALAVRAS_RESERVADAS_JAVA =
    {
        "assert",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "do",
        "double",
        "else",
        "enum",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "import",
        "instanceof",
        "interface",
        "int",
        "long",
        "native",
        "new",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "strictfp",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "try",
        "void",
        "volatile",
        "while",
        "false",
        "null",
        "true"
    };
}
