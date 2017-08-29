package ufrn.ia.fuzzy.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Function {

	private String name;
	
	private ArrayList<Input> inputs = new ArrayList<>();
	
	private ArrayList<Output> outputs = new ArrayList<>();
	
	private ArrayList<Fuzzyfier> fuzzyfiers = new ArrayList<>();
	
	private ArrayList<Defuzzyfier> defuzzyfiers = new ArrayList<>();
	
	private RuleBlock rules = new RuleBlock();
	
	public Function(String name) {
		this.name = name;
	}
	
	public void addInput(Input input) {
		inputs.add(input);
	}
	
	public void removeInput(Input input) {
		inputs.remove(input);
	}
	
	public void addOutput(Output output) {
		outputs.add(output);
	}
	
	public void removeOutput(Output output) {
		outputs.remove(output);
	}
	
	public void addFuzzyfiers(Fuzzyfier fuzzyfier) {
		fuzzyfiers.add(fuzzyfier);
	}
	
	public void removeFuzzyfier(Fuzzyfier fuzzyfier) {
		fuzzyfiers.remove(fuzzyfier);
	}
	
	public void addDefuzzyfier(Defuzzyfier defuzzyfier) {
		defuzzyfiers.add(defuzzyfier);
	}
	
	public void removeDefuzzyfier(Defuzzyfier defuzzyfier) {
		defuzzyfiers.remove(defuzzyfier);
	}
	
	public void addRule(Rule rule) {
		rules.addRule(rule);
	}
	
	public int ruleCount() {
		return rules.ruleCount();
	}
	
	public void removeRule(Rule rule) {
		rules.removeRule(rule);
	}
	
	
	public void eval() {
		try {

			String filename = "C:\\Users\\Pedro de Castro\\Dropbox\\I.Artificial\\Fuzzy\\regras.fcl";
			//File file = new File(filename);

			// if file doesnt exists, then create it
			//if (!file.exists()) {
				//file.createNewFile();
			//}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//bw.write(toString());
			bw.close();

			FIS fis = FIS.load(filename, true);

			// Caso o arquivo de regras nao carregue
			if (fis == null) {
				System.err.println("Nao foi possivel carregar as regras: '" + filename + "'");
				System.exit(1);
			}

			// Setando o bloco funcional padrao
			FunctionBlock fb = fis.getFunctionBlock(null);

			// Setando entradas
			fb.setVariable("dieta", 2000);
			fb.setVariable("exercicio", 3000);

			// Calcular
			fb.evaluate();

			// Graficos
			JFuzzyChart.get().chart(fb);

			// Mostra a saida da operacao
			fb.getVariable("risco").defuzzify();

			// Imprime regras de inferencia
			System.out.println(fb);
			System.out.println("Risco: " + fb.getVariable("risco").getValue());

		
			// Grafico das regras de inferencia
			Variable tip = fb.getVariable("risco");
			JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("FUNCTION_BLOCK ").append(name);

		builder.append("\nVAR_INPUT\n");
		for (Input input: inputs) {
			builder.append(input.toString());
		}
		builder.append("\nEND_VAR\n");
		
		builder.append("\nVAR_OUPUT\n");
		for (Output output: outputs) {
			builder.append(output.toString());
		}
		builder.append("\nEND_VAR\n");
		
		for (Fuzzyfier fuzzyfier: fuzzyfiers) {
			builder.append(fuzzyfier.toString());
		}

		for (Defuzzyfier defuzzyfier: defuzzyfiers) {
			builder.append(defuzzyfier.toString());
		}

		builder.append(rules.toString());

		builder.append("\nEND FUNCTION_BLOCK").append(name);
		return builder.toString();
	}
	
}