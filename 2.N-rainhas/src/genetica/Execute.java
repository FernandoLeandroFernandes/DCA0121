package genetica;

import java.util.ArrayList;
import java.util.Random;

import GUI.Frame;
import evolucao.Individuo;
import evolucao.Populacao;

public class Execute {

	private static int N;
	private static double taxaDeCrossover;
	private static double taxaDeMutacao;
	private static int numeroMaximoGeracoes;
	private static int tamanhoPopulacao;
	private static boolean elitismo;

	public static void AG(Frame frame) {
		new Thread(new ExecutaAG(frame)).start();
	}

	// -------------------------------------------------------------------------------
	// Metodos que gera uma nova geracao a partir de uma populacao anterior
	public static Populacao novaGeracao(Populacao populacao) {

		Random r;
		Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao(), false);

		if (isElitismo()) {
			novaPopulacao.setIndividuo(populacao.getIndividuo(0));
		}

		while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {

			Individuo pais[] = new Individuo[2];
			Individuo filhos[] = new Individuo[2];

			pais[0] = selecaoTorneio(populacao);
			pais[1] = selecaoTorneio(populacao);

			r = new Random();
			if (r.nextDouble() <= taxaDeCrossover) {
				filhos = Execute.crossover(pais[0], pais[1]);
				filhos[0].geraAptidao();
				filhos[1].geraAptidao();
			} else {
				filhos[0] = pais[0];
				filhos[1] = pais[1];
			}

			novaPopulacao.setIndividuo(filhos[0]);
			novaPopulacao.setIndividuo(filhos[1]);

		}
		novaPopulacao.ordenaPopulacao();
		return novaPopulacao;
	}

	// -------------------------------------------------------------------------------
	// Metodo de recombinacao ou crossover
	public static Individuo[] crossover(Individuo pai1, Individuo pai2) {
		Random r = new Random();
		Individuo filhos[] = new Individuo[2];

		filhos[0] = new Individuo(false);
		filhos[1] = new Individuo(false);

		int pontoCorte = r.nextInt(Execute.N);

		for (int i = 0; i < Execute.N; i++) {
			if (i < pontoCorte) {
				filhos[0].addRainha(i, pai1.getPosicoesY()[i]);
				filhos[1].addRainha(i, pai2.getPosicoesY()[i]);
			} else {
				filhos[0].addRainha(i, pai2.getPosicoesY()[i]);
				filhos[1].addRainha(i, pai1.getPosicoesY()[i]);
			}
		}

		filhos[0].geraAptidao();
		filhos[1].geraAptidao();

		return filhos;
	}

	// -------------------------------------------------------------------------------
	// Metodo de selecao do individuo torneio/roleta
	public static Individuo selecaoTorneio(Populacao populacao) {
		Random r = new Random();
		Populacao populacaoIntermediaria = new Populacao(2, false);

		populacaoIntermediaria.setIndividuo(populacao.getIndividuo(r.nextInt(populacao.getTamPopulacao())));
		r = new Random();
		populacaoIntermediaria.setIndividuo(populacao.getIndividuo(r.nextInt(populacao.getTamPopulacao())));

		populacaoIntermediaria.ordenaPopulacao();

		int pos;

		r = new Random();
		if (r.nextDouble() < 0.9) {
			pos = 0;
		} else {
			pos = 1;
		}

		return populacaoIntermediaria.getIndividuo(pos);
	}

	public static Individuo[] selecaoRodaDaRoleta(Populacao populacao) {
		Individuo[] individuosEscolhidos = new Individuo[2];

		ArrayList<Double> individuosTemp = new ArrayList<Double>();

		double aptidaoAcumulada = 0;

		for (int i = 0; i < populacao.getNumIndividuos(); i++) {
			if (i == 0) {
				aptidaoAcumulada = populacao.getIndividuo(i).getAptidao();
			} else {
				aptidaoAcumulada += populacao.getIndividuo(i).getAptidao();
			}

			individuosTemp.add(i, aptidaoAcumulada);
		}

		Random r = new Random();
		double ponteiro = r.nextDouble() * aptidaoAcumulada;

		for (int i = 0; i < individuosTemp.size(); i++) {
			if (individuosTemp.get(i) > ponteiro) {
				individuosEscolhidos[0] = populacao.getIndividuo(i);
				break;
			}
		}

		r = new Random();
		ponteiro = r.nextDouble() * aptidaoAcumulada;

		for (int i = 0; i < individuosTemp.size(); i++) {
			if (individuosTemp.get(i) > ponteiro) {
				individuosEscolhidos[1] = populacao.getIndividuo(i);
				break;
			}
		}

		return individuosEscolhidos;
	}

	// -------------------------------------------------------------------------------
	// Metodo de taxa de ocorrencia de crossover
	public static double getTaxaDeCrossover() {
		return taxaDeCrossover;
	}

	public static void setTaxaDeCrossover(double taxaDeCrossover) {
		Execute.taxaDeCrossover = taxaDeCrossover;
	}

	// -------------------------------------------------------------------------------
	// Metodo que regula a taxa de mutacao dos individuos
	public static double getTaxaDeMutacao() {
		return taxaDeMutacao;
	}

	public static void setTaxaDeMutacao(double taxaDeMutacao) {
		Execute.taxaDeMutacao = taxaDeMutacao;
	}

	// -------------------------------------------------------------------------------
	// Metodos que define m�ximo de gera��es analisadas
	public static int getNumeroMaximoGeracoes() {
		return numeroMaximoGeracoes;
	}

	public static void setNumeroMaximoGeracoes(int numeroMaximoGeracoes) {
		Execute.numeroMaximoGeracoes = numeroMaximoGeracoes;
	}

	// -------------------------------------------------------------------------------
	// Metodo que define a quantidade de rainhas
	public static int getN() {
		return N;
	}

	public static void setN(int N) {
		Execute.N = N;
	}

	// -------------------------------------------------------------------------------
	// Metodos que define tamanho m�ximo da populacao
	public static int getTamanhoPopulacao() {
		return tamanhoPopulacao;
	}

	public static void setTamanhoPopulacao(int tamanhoPopulacao) {
		Execute.tamanhoPopulacao = tamanhoPopulacao;
	}

	// -------------------------------------------------------------------------------
	// Metodos que define ou n�o elitismo
	public static boolean isElitismo() {
		return elitismo;
	}

	public static void setElitismo(boolean elitismo) {
		Execute.elitismo = elitismo;
	}

	// -------------------------------------------------------------------------------
	// Metodos que define o texto na tela a ser exibido em formato de log.
	private static class ExecutaAG implements Runnable {

		private Frame frame;

		public ExecutaAG(Frame frame) {
			this.frame = frame;
		}

		@Override
		public void run() {

			frame.setEstadoIniciarBotao(false);

			int geracao = 1;

			Populacao populacao = new Populacao(getTamanhoPopulacao(), true);
			populacao.ordenaPopulacao();
			frame.setLog("Geracao " + geracao + ":\n" + "Melhor Aptidao: " + populacao.getIndividuo(0).getAptidao() + " (Colisoes: "
					+ populacao.getIndividuo(0).getColisoes() + ")" + "\n" + "Media Aptidao: " + populacao.getMediaAptidao()
					+ "\n" + "Pior Aptidao: " + populacao.getIndividuo(populacao.getNumIndividuos() - 1).getAptidao() + " (Colisoes: "
					+ populacao.getIndividuo(populacao.getNumIndividuos() - 1).getColisoes() + ")" + "\n"
					+ "-------------------------------------");

			frame.getTabuleiroVisual().setTabuleiro(populacao.getIndividuo(0).getTabuleiro());

			while (geracao < getNumeroMaximoGeracoes()) {
				geracao++;

				populacao = Execute.novaGeracao(populacao);

				frame.setLog("Geracao " + geracao + ":\n" + "Melhor Aptidao: " + populacao.getIndividuo(0).getAptidao() + " (Colisoes: "
						+ populacao.getIndividuo(0).getColisoes() + ")" + "\n" + "Media Aptidao: " + populacao.getMediaAptidao()
						+ "\n" + "Pior Aptidao: " + populacao.getIndividuo(populacao.getNumIndividuos() - 1).getAptidao() + " (Colisoes: "
						+ populacao.getIndividuo(populacao.getNumIndividuos() - 1).getColisoes() + ")" + "\n"
						+ "-------------------------------------");

				if (geracao % (getNumeroMaximoGeracoes() / 1000) == 0) {
					frame.getChart().update(geracao, populacao.getIndividuo(0).getAptidao(),
							populacao.getMediaAptidao(),
							populacao.getIndividuo(populacao.getNumIndividuos() - 1).getAptidao());
				}

				if (populacao.getIndividuo(0).getColisoes() == 0) {
					frame.setLog("SOLUCAO ENCONTRADA!");
					frame.getChart().update(geracao, populacao.getIndividuo(0).getAptidao(),
							populacao.getMediaAptidao(),
							populacao.getIndividuo(populacao.getNumIndividuos() - 1).getAptidao());
					break;
				}

			}
			
			if (geracao >= getNumeroMaximoGeracoes()) {
				frame.setLog("SOLUCAO NÃO ENCONTRADA!");
			}
			
			frame.getTabuleiroVisual().setTabuleiro(populacao.getIndividuo(0).getTabuleiro());

			frame.setEstadoIniciarBotao(true);

		}
	}
}