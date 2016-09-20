package br.univali.portugol.nucleo.asa;

/**
 * Representa um valor do tipo {@link TipoDado#LOGICO} no código fonte.
 *
 * @author Luiz Fernando Noschang
 * @version 1.0
 * @see TipoDado
 */
public final class NoLogico extends NoValor<Boolean>
{
    /**
     * @param valor o valor lógico representado por este nó da árvore
     * @since 1.0
     */
    public NoLogico(boolean valor)
    {
        super(valor);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof NoLogico)
        {
            return ((NoLogico) obj).getValor().equals(getValor());
        }

        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Object aceitar(VisitanteASA visitante) throws ExcecaoVisitaASA
    {
        return visitante.visitar(this);
    }

    @Override
    public String toString()
    {
        return Boolean.toString(getValor());
    }
}
