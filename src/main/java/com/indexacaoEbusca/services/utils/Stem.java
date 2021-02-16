package com.indexacaoEbusca.services.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Keyword card with stem form, terms dictionary and frequency rank
 */
public class Stem implements Comparable<Stem> {
    private final String stem;
    private final Set<String> terms = new HashSet<>();
    private int frequency;

    public Stem(String stem) {
        this.stem = stem;
    }

    public void add(String term) {
        this.terms.add(term);
        this.frequency++;
    }

    @Override
    public int compareTo(Stem keyword) {
        return Integer.valueOf(keyword.frequency).compareTo(this.frequency);
    }

    
    @Override
    public int hashCode() {
        return this.getStem().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stem)) return false;

        Stem that = (Stem) o;
        return this.getStem().equals(that.getStem());
    }

    public String getStem() {
        return this.stem;
    }

    public Set<String> getTerms() {
        return this.terms;
    }

    public int getFrequency() {
        return this.frequency;
    }

	@Override
	public String toString() {
		return "\nstem=" + stem + ", terms=" + terms + ", frequency=" + frequency ;
	}
    
    
}
