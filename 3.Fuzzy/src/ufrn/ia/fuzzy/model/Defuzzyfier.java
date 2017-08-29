package ufrn.ia.fuzzy.model;

import static java.lang.Float.NaN;

import java.util.ArrayList;
import java.util.Arrays;

public class Defuzzyfier {

	private String name;
	
	private ArrayList<Term> terms;
	
	private String method;
	
	private float defaultValue = NaN;
	
	public Defuzzyfier(String name) {
		this.name = name;
	}
	
	public Defuzzyfier(String name, Term ... terms) {
		
		this.name = name;
		
		if (terms != null && terms.length >0) {
			this.terms.addAll(Arrays.asList(terms));
		}
	}

	public String name() {
		return name;
	}

	public void name(String name) {
		this.name = name;
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
	
	public String method() {
		return method;
	}

	public void method(String method) {
		this.method = method;
	}

	public float defaultValue() {
		return defaultValue;
	}

	public void defaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		
		String print = "\nDEFUZZIFY ".concat(this.name).concat("\n");
		for (Term term: terms) {
			print.concat("\t").concat(term.toString());
		}
		
		if (method != null && !method.isEmpty()) {
			print.concat("\t").concat("\nMETHOD : ").concat(method).concat(";");
		}
		
		if (defaultValue != NaN) {
			print.concat("\t").concat(String.format("\nDEFAULT := %.1f;", defaultValue));
		}
		
		print.concat("\nEND_DEFUZZIFY\n");
		
		return print;	
	}

}