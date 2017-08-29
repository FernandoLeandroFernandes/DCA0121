package ufrn.ia.fuzzy.model;

public class Output {

	private String name;
	
	public Output(String name) {
		this.name = name;
	}	
	
	public String name() {
		return this.name;
	}

	public String type() {
		return "REAL";
	}

	@Override
	public String toString() {
		return this.name.concat(" : ").concat(this.type()).concat(";\n");
	}
	
}