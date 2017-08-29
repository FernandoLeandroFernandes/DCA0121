package ufrn.ia.fuzzy.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Fuzzyfier {

	private String name;
	
	private ArrayList<Term> terms;
	
	public Fuzzyfier(String name) {
		this.name = name;
	}
	
	public Fuzzyfier(String name, Term ... terms) {
		
		this.name = name;
		
		if (terms != null && terms.length >0) {
			this.terms.addAll(Arrays.asList(terms));
		}
	}
	
	public void addTerm(Term term) {
		
		if (term != null) {
			this.terms.add(term);
		}
	}
	
	public void removeTerm(Term term) {
		
		if (term != null) {
			this.terms.remove(term);
		}
	}
	
	@Override
	public String toString() {
		
		String print = "\nFUZZIFY ".concat(this.name).concat("\n");
		for (Term term: terms) {
			print.concat("\t").concat(term.toString());
		}
		print.concat("\nEND_FUZZIFY\n");
		
		return print;	}
}
