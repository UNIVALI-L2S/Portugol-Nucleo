package br.univali.portugol.nucleo.execucao.gerador.helpers;

import br.univali.portugol.nucleo.asa.*;
import br.univali.portugol.nucleo.asa.VisitanteASA;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Elieser
 */
public class GeradorOperacao
{
    private static final Map<Class, String> OPERADORES = new HashMap<>();

    public void gera(NoOperacao no, PrintWriter saida, VisitanteASA visitor) throws ExcecaoVisitaASA
    {
        if (no.estaEntreParenteses())
        {
            saida.append("(");
        }

        boolean operandosSaoStrings = operandosSaoStrings(no.getOperandoEsquerdo(), no.getOperandoDireito());
        boolean usaOperadorPadrao = usaOperadorPadrao(no, operandosSaoStrings);

        boolean precisaDeNegacao = !usaOperadorPadrao && (no instanceof NoOperacaoLogicaDiferenca);
        if (precisaDeNegacao)
        {
            saida.append("!"); // not equals
        }

        no.getOperandoEsquerdo().aceitar(visitor);

        if (usaOperadorPadrao)
        {
            String operador = OPERADORES.get(no.getClass());
            assert (operador != null);
            saida.format(" %s ", operador);
        }
        else
        {
            saida.append(".equals(");
        }

        no.getOperandoDireito().aceitar(visitor);

        if (!usaOperadorPadrao)
        {
            saida.append(")"); // fecha o parênteses do .equals()
        }

        if (no.estaEntreParenteses())
        {
            saida.append(")");
        }
    }

    private static boolean operandosSaoStrings(NoExpressao a, NoExpressao b)
    {
        return a.getTipoResultante() == TipoDado.CADEIA
                && b.getTipoResultante() == TipoDado.CADEIA;
    }

    private static boolean usaOperadorPadrao(NoOperacao no, boolean operandosSaoStrings)
    {
        if (no instanceof NoOperacaoLogicaIgualdade || no instanceof NoOperacaoLogicaDiferenca)
        {
            return !operandosSaoStrings;
        }

        return true;
    }

    static
    {
        OPERADORES.put(NoOperacaoAtribuicao.class, "=");
        OPERADORES.put(NoOperacaoDivisao.class, "/");
        OPERADORES.put(NoOperacaoModulo.class, "%");
        OPERADORES.put(NoOperacaoMultiplicacao.class, "*");
        OPERADORES.put(NoOperacaoSoma.class, "+");
        OPERADORES.put(NoOperacaoSubtracao.class, "-");

        OPERADORES.put(NoMenosUnario.class, "-");

        OPERADORES.put(NoOperacaoLogicaDiferenca.class, "!=");
        OPERADORES.put(NoOperacaoLogicaIgualdade.class, "==");
        OPERADORES.put(NoOperacaoLogicaMaior.class, ">");
        OPERADORES.put(NoOperacaoLogicaMaiorIgual.class, ">=");
        OPERADORES.put(NoOperacaoLogicaMenor.class, "<");
        OPERADORES.put(NoOperacaoLogicaMenorIgual.class, "<=");
        OPERADORES.put(NoOperacaoLogicaOU.class, "||");
        OPERADORES.put(NoOperacaoLogicaE.class, "&&");

        OPERADORES.put(NoOperacaoBitwiseE.class, "&");
        OPERADORES.put(NoOperacaoBitwiseOu.class, "|");
        OPERADORES.put(NoOperacaoBitwiseXOR.class, "^");
        OPERADORES.put(NoOperacaoBitwiseLeftShift.class, "<<");
        OPERADORES.put(NoOperacaoBitwiseRightShift.class, ">>");
    }

}