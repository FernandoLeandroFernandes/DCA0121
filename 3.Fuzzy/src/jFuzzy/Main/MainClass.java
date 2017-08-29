package jFuzzy.Main;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
		
		String filename = "regras.fcl";
		FIS fis = FIS.load(filename, true);

		// Caso o arquivo de regras nao carregue
		if (fis == null) {
			System.err.println("Nao foi possivel carregar o arquivo: '" + filename + "'");
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
	}

}
