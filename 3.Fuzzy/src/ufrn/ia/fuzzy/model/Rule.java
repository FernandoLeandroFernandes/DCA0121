package ufrn.ia.fuzzy.model;

public class Rule {

	private int order;
	
	private String antecedent;
	
	private String consequent;
	
	public Rule(int order, String antecedent, String consequent) {
		this.order = order;
		this.antecedent = antecedent;
		this.consequent = consequent;
	}
	
	public int order() {
		return order;
	}

	public void order(int order) {
		this.order = order;
	}

	public String antecedent() {
		return antecedent;
	}

	public void antecedent(String antecedent) {
		this.antecedent = antecedent;
	}

	public String consequent() {
		return consequent;
	}

	public void consequent(String consequent) {
		this.consequent = consequent;
	}

	@Override
	public String toString() {
		return String.format("RULE %d : IF %s THEN %S;", order, antecedent, consequent);
	}
}