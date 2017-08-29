package evolucao;

import genetica.Execute;

//Define um tabuleiro de dimensao n
public class Tabuleiro {

	public boolean[][] tabuleiro;
	public int tamanho;

	public Tabuleiro(int tamanho) {
		this.tamanho = tamanho;
		tabuleiro = new boolean[tamanho][tamanho];
		zeraTabuleiro();
	}

	public void zeraTabuleiro() {
		for (int y = 0; y < tamanho; y++) {
			for (int x = 0; x < tamanho; x++) {
				tabuleiro[x][y] = false;
			}
		}
	}

	public void atualizaTabuleiro(int posicoesY[]) {
		zeraTabuleiro();
		for (int i = 0; i < Execute.getN(); i++) {
			if (posicoesY[i] != -1) {
				tabuleiro[i][posicoesY[i]] = true;
			}
		}
	}

	public boolean[][] getTabuleiro() {
		return tabuleiro;
	}

	// Verifica a condicao do quadrado
	public boolean estaLivre(int x, int y) {
		boolean livre = true;
		if (tabuleiro[x][y]) {
			livre = false;
		}
		return livre;
	}

	@Override
	public String toString() {
		String r = "";
		for (int y = 0; y < tamanho; y++) {
			for (int x = 0; x < tamanho; x++) {
				if (tabuleiro[x][y]) {
					r += " x";
				} else {
					r += " o";
				}
			}
			r += "\n";
		}
		return r;
	}
}