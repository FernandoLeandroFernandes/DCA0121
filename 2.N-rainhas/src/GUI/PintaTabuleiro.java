package GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import evolucao.Tabuleiro;
import genetica.Execute;

public class PintaTabuleiro extends JPanel {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int tamanhoQuadrado = 50;
    private Tabuleiro tabuleiro;
    private int larguraComp;

    public PintaTabuleiro(Tabuleiro tabuleiro, int w) {
        this.tabuleiro = tabuleiro;
        this.larguraComp = w;
        tamanhoQuadrado = (int) larguraComp / Execute.getN();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // setando a malha quadriculada do tabuleiro
        int cont = 0;
        for (int y = 0; y < Execute.getN(); y++) {
            cont++;
            for (int x = 0; x < Execute.getN(); x++) {
                if (cont % 2 == 0) {
                    g.setColor(Color.DARK_GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }
                cont++;
                g.fillRect(tamanhoQuadrado * x, tamanhoQuadrado * y, tamanhoQuadrado, tamanhoQuadrado);
            }
        }
        // pintando de azul a posicao das rainhas
        for (int y = 0; y < Execute.getN(); y++) {
            for (int x = 0; x < Execute.getN(); x++) {
                if (tabuleiro.getTabuleiro()[x][y]) {
                    g.setColor(Color.blue);
                    g.fillOval(tamanhoQuadrado * x, tamanhoQuadrado * y, tamanhoQuadrado, tamanhoQuadrado);
                }
            }
        }
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        tamanhoQuadrado = (int) larguraComp / Execute.getN();
        paintComponent(super.getGraphics());
    }
}
