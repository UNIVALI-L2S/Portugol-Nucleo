package br.univali.portugol.nucleo.asa;

/**
 * Representa um valor do tipo {@link TipoDado#CARACTER} no código fonte.
 * 
 * @author Luiz Fernando Noschang
 * @version 1.0
 * 
 * @see TipoDado
 */
public final class NoCaracter extends NoValor<Character>
{
    /**
     * 
     * @param valor     o caracter representado por este nó da árvore
     * @since 1.0
     */
    public NoCaracter(char valor)
    {
        super(valor);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof NoCaracter)
        {
            return ((NoCaracter) obj).getValor().equals(getValor());
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
        return "'" + getValor() + "'";
    }
}