package br.ufc.russas.n2s.darwin.model;

import java.util.function.Predicate;

public class EtapaPredicates {
	public static Predicate<Etapa> isNota() {
		return p -> p.getCriterioDeAvaliacao() == EnumCriterioDeAvaliacao.NOTA;
	}
	
	public static Predicate<Etapa> isAprovacaoDeferimento() {
		return p -> p.getCriterioDeAvaliacao() == EnumCriterioDeAvaliacao.APROVACAO || p.getCriterioDeAvaliacao() == EnumCriterioDeAvaliacao.DEFERIMENTO;
	}
}
