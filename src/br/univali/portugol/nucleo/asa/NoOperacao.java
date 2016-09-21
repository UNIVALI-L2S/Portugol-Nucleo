package br.univali.portugol.nucleo.asa;

/**
 * Representa uma operação no código fonte.
 * <p>
 * No Portugol existem dois tipos de operação: operações lógicas e operações aritméticas.
 * Ambos os tipos de operação possuem um operando esquerdo, um operando direito, um operador
 * e retornam um valor ao serem avaliadas.
 * <p>
 * Esta classe implementa todas as operações lógicas e aritméticas do Portugol com exceção das 
 * seguintes operações: incremento, decremento, menos unário e negação lógica.
 * 
 * @author Luiz Fernando Noschang
 * @version 1.0
 * @see NoDecremento
 * @see NoIncremento
 * @see NoMenosUnario
 * @see NoNao
 * @see Operacao
 */
public abstract class NoOperacao extends NoExpressao
{ 
    private NoExpressao operandoEsquerdo;
    private NoExpressao operandoDireito;
    private TrechoCodigoFonte trechoCodigoFonteOperador;

    /**
     * 
     * @param operacao             valor indicando a operação que está sendo executada.
     * @param operandoEsquerdo     a expressão à esquerda do operador.
     * @param operandoDireito      a expressão à direita do operador.
     * @since 1.0
     */
    public NoOperacao(NoExpressao operandoEsquerdo, NoExpressao operandoDireito)
    {        
        this.operandoEsquerdo = operandoEsquerdo;
        this.operandoDireito = operandoDireito;
    }

    /**
     * Obtém a expressão à esquerda do operador. 
     * 
     * @return     a expressão à esquerda do operador.
     * @since 1.0
     */
    public NoExpressao getOperandoEsquerdo()
    {
        return operandoEsquerdo;
    }

    
    /**
     * Obtém a expressão à direita do operador. 
     * 
     * @return     a expressão à direita do operador.
     * @since 1.0
     */
    public NoExpressao getOperandoDireito()
    {
        return operandoDireito;
    }
    
    /**
     * Define o trecho do código fonte onde o operador se encontra.
     * 
     * @param trechoCodigoFonteOperador     Define o trecho do código fonte onde o operador se encontra.
     * @since 1.0
     */
    public void setTrechoCodigoFonteOperador(TrechoCodigoFonte trechoCodigoFonteOperador)
    {
        this.trechoCodigoFonteOperador = trechoCodigoFonteOperador;
    }

    /**
     * Obtém o trecho do código fonte onde o operador se encontra.
     * 
     * @return     o trecho do código fonte onde o operador se encontra.
     * @since 1.0
     */
    public TrechoCodigoFonte getTrechoCodigoFonteOperador()
    {
        return trechoCodigoFonteOperador;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected TrechoCodigoFonte montarTrechoCodigoFonte()
    {
        TrechoCodigoFonte trechoFonteEsq = operandoEsquerdo.getTrechoCodigoFonte();
        TrechoCodigoFonte trechoFonteDir = operandoDireito.getTrechoCodigoFonte();
        if (trechoFonteDir == null || trechoFonteEsq == null)
        {
            return TRECHO_NULO;
        }
        int linha = trechoFonteEsq.getLinha();
        int inicioOpEsquerdo = trechoFonteEsq.getColuna();
        
        int colunaOpDireito = trechoFonteDir.getColuna();
        int tamanhoOpDireito = trechoFonteDir.getTamanhoTexto();
        int terminoOpDireito = colunaOpDireito + tamanhoOpDireito;
        
        int tamanhoTexto = terminoOpDireito - inicioOpEsquerdo;

        return new TrechoCodigoFonte(linha, inicioOpEsquerdo, tamanhoTexto);
    }
}
