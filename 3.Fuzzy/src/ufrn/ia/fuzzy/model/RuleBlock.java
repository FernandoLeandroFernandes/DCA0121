package ufrn.ia.fuzzy.model;

import java.util.ArrayList;

public class RuleBlock {

	private String name;
	
	private ArrayList<Rule> rules = new ArrayList<>();
	
	private String andFunction;
	
	private String actFunction;
	
	private String accuFunction;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAndFunction() {
		return andFunction;
	}

	public void setAndFunction(String andFunction) {
		this.andFunction = andFunction;
	}

	public String getActFunction() {
		return actFunction;
	}

	public void setActFunction(String actFunction) {
		this.actFunction = actFunction;
	}

	public String getAccuFunction() {
		return accuFunction;
	}

	public void setAccuFunction(String accuFunction) {
		this.accuFunction = accuFunction;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	public void removeRule(Rule rule) {
		rules.remove(rule);
	}
	
	@Override
	public String toString() {
		
		String print = "\nRULEBLOCK ".concat(this.name != null ? this.name : "default" ).concat("\n");
		
		if (andFunction != null && !andFunction.isEmpty()) {
			print.concat("\t").concat("\nAND : ").concat(andFunction).concat(";");
		}
		
		if (actFunction != null && !actFunction.isEmpty()) {
			print.concat("\t").concat("\nACT : ").concat(actFunction).concat(";");
		}
		
		if (accuFunction != null && !accuFunction.isEmpty()) {
			print.concat("\t").concat("\nACCU : ").concat(accuFunction).concat(";");
		}
		
		for (Rule rule: rules) {
			print.concat("\t").concat(rule.toString());
		}
		print.concat("\nEND_RULEBLOCK\n");
		
		return print;	
	}

	public int ruleCount() {
		return this.rules.size();
	}
}