package evolucao;

import java.util.Random;

import genetica.Execute;

//Classe que define uma possivel solucao
public class Individuo {

	private double aptidao;
	private int[] posicoesY;
	private Tabuleiro tabuleiro;
	private int colisoes;

	public Individuo(boolean rainhasAleatorias) {
		posicoesY = new int[Execute.getN()];
		tabuleiro = new Tabuleiro(Execute.getN());

		for (int i = 0; i < posicoesY.length; i++) {
			posicoesY[i] = -1;
		}

		for (int i = 0; i < posicoesY.length; i++) {
			if (rainhasAleatorias) {
				posicoesY[i] = this.gerarYAleatorioExclusivo();
				tabuleiro.atualizaTabuleiro(posicoesY);
			}
		}

		if (rainhasAleatorias) {
			geraAptidao();
		}
	}

	// Gera aleatoriamente um valor de Y sem colisoes
	public int gerarYAleatorioExclusivo() {
		int y;
		Random r;
		boolean encontrou;

		do {
			r = new Random();
			y = r.nextInt(Execute.getN());
			encontrou = false;

			for (int i = 0; i < Execute.getN(); i++) {
				if (posicoesY[i] == y) {
					encontrou = true;
					break;
				}
			}

		} while (encontrou);

		return y;
	}

	// Gera a aptidao baseado no numero de colisoes
	public void geraAptidao() {
		this.colisoes = geraColisoes();
		this.aptidao = colisoes;
	}

	// Adiciona uma rainha no tabuleiro
	public void addRainha(int x, int y) {
		Random r = new Random();
		if (r.nextDouble() < Execute.getTaxaDeMutacao()) {
			y = gerarYAleatorioExclusivo();
		}
		posicoesY[x] = y;
		tabuleiro.atualizaTabuleiro(posicoesY);
	}

	public double getAptidao() {
		return aptidao;
	}

	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public void setTabuleiro(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public int getColisoes() {
		return colisoes;
	}

	public int geraColisoes() {
		int x = 0;
		int y = 0;
		int tempx = 0;
		int tempy = 0;

		int _colisoes = 0;
		int dx[] = new int[] { -1, 1, -1, 1 };
		int dy[] = new int[] { -1, 1, 1, -1 };
		boolean done;

		// Checa as colisoes na horizontal
		for (int i = 0; i < Execute.getN(); i++) {
			y = posicoesY[i];
			if (y != -1) {
				for (int j = 0; j < Execute.getN(); j++) {
					if (posicoesY[j] == y && j != i) {
						_colisoes++;
					}
				}
			}
		}

		// Checa as colisoes nas diagonais
		for (int i = 0; i < Execute.getN(); i++) {
			x = i;
			y = this.posicoesY[i];
			if (y != -1) {
				for (int j = 0; j <= 3; j++) {
					tempx = x;
					tempy = y;
					done = false;
					while (!done) {
						tempx += dx[j];
						tempy += dy[j];
						if ((tempx < 0 || tempx >= Execute.getN()) || (tempy < 0 || tempy >= Execute.getN())) {
							done = true;
						} else {
							if (tabuleiro.getTabuleiro()[tempx][tempy]) {
								_colisoes++;
							}
						}
					}
				}
			}
		}

		return _colisoes;
	}

	public int[] getPosicoesY() {
		return posicoesY;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < Execute.getN(); i++) {
			s += "[" + i + "," + posicoesY[i] + "] ";
		}
		return s;
	}
}