package br.univali.portugol.nucleo.execucao.operacoes.logicas;

import br.univali.portugol.nucleo.execucao.operacoes.Operacao;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Elieser
 */

public abstract class OperacaoLogicaIgualdade<A, B> extends OperacaoLogica<A, B>
{
    private final static Map<Class, Map<Class, Operacao>> MAPA;
    
    public static OperacaoLogica getOperacao(Object operandoEsquerdo, Object operandoDireito)
    {
        return (OperacaoLogica) Operacao.getOperacao(operandoEsquerdo, operandoDireito, MAPA);
    }

    static {
        
        OperacaoLogicaIgualdade operacaoParaTiposIguais = new OperacaoLogicaIgualdade<Object, Object>()
        {
            @Override
            public Boolean executar(Object a, Object b)
            {
                return a.equals(b);
            }
        };
        
        OperacaoLogicaIgualdade operacaoParaInteiroComReal = new OperacaoLogicaIgualdade<Integer, Double>()
        {
            @Override
            public Boolean executar(Integer a, Double b)
            {
                return a.equals(b.intValue());
            }
        };
        
        OperacaoLogicaIgualdade operacaoParaRealComInteiro = new OperacaoLogicaIgualdade<Double, Integer>()
        {
            @Override
            public Boolean executar(Double a, Integer b)
            {
                return a.equals(b.doubleValue());
            }
        };
        
        MAPA = new HashMap<>();
        
        // cria instâncias dos mapas usando o tipo do operando esquerdo como chave
        MAPA.put(Integer.class,     new HashMap<Class, Operacao>());
        MAPA.put(Double.class,      new HashMap<Class, Operacao>());
        MAPA.put(Character.class,   new HashMap<Class, Operacao>());
        MAPA.put(Boolean.class,     new HashMap<Class, Operacao>());
        MAPA.put(String.class,      new HashMap<Class, Operacao>());
        
        // faz o mapeamento das combinações de tipos do operando esquerdo e direito com a respectiva operação
        MAPA.get(Integer.class).put(Integer.class,      operacaoParaTiposIguais);
        MAPA.get(Integer.class).put(Double.class,       operacaoParaInteiroComReal);
        
        MAPA.get(Double.class).put(Double.class,        operacaoParaTiposIguais);
        MAPA.get(Double.class).put(Integer.class,       operacaoParaRealComInteiro);
        
        MAPA.get(Character.class).put(Character.class,  operacaoParaTiposIguais);
        
        MAPA.get(Boolean.class).put(Boolean.class,      operacaoParaTiposIguais);
        
        MAPA.get(String.class).put(String.class,        operacaoParaTiposIguais);
    }

}
