package ufrn.ia.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AStarORIGINAL {

	public static JFrame mazeFrame;

	public static void main(String[] args) {
		int width = 693;
		int height = 545;
		mazeFrame = new JFrame("Algoritmos I.A. - Busca com Informação");
		mazeFrame.setContentPane(new MazePanel(width, height));
		mazeFrame.pack();
		mazeFrame.setResizable(false);

		// pra ficar localizado no centro da tela
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double ScreenHeight = screenSize.getHeight();
		int x = ((int) screenWidth - width) / 2;
		int y = ((int) ScreenHeight - height) / 2;

		mazeFrame.setLocation(x, y);
		mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mazeFrame.setVisible(true);
	} // end main()

	public static class MazePanel extends JPanel {

		/*
		 **********************************************************
		 * Classes dentro de MazePanel
		 **********************************************************
		 */
		private class Cell {
			int row; // o numero de linhas(0 a 50)
			int col; // o numero de colunas (0 a 50)
			int g; // valor da função g para o A* e a busca gulosa
			int h; // valor da função h para o A* e a busca gulosa
			int f; // valor da função f para o A* e a busca gulosa
			int dist; // a distância da célula a partir da posição inicial do nó
					  // atual

			Cell prev;
			// Cada estado corresponde a uma célula
			// E cada estado tem um antecessor, que
			// É armazenado nesta variável

			public Cell(int row, int col) {
				this.row = row;
				this.col = col;
			}
		} // end classe cell

		// Classe auxiliar que especifica que as células
		// serão classificados de acordo com seu campo 'f'

		private class CellComparatorByF implements Comparator<Cell> {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return cell1.f - cell2.f;
			}
		} // end class CellComparatorByF

		// A classe auxiliar que especifica que as células serão classificados
		// De acordo com seu campo 'dist'

		private class CellComparatorByDist implements Comparator<Cell> {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return cell1.dist - cell2.dist;
			}
		} // end class CellComparatorByDist

		// Classe que controla os movimentos do mouse como nós a "pintar"
		// Obstáculos ou mover o nó atual e / ou de destino.

		private class MouseHandler implements MouseListener, MouseMotionListener {
			private int cur_row, cur_col, cur_val;

			@Override
			public void mousePressed(MouseEvent evt) {
				int row = (evt.getY() - 10) / squareSize;
				int col = (evt.getX() - 10) / squareSize;
				if (row >= 0 && row < rows && col >= 0 && col < columns && !searching && !found) {
					cur_row = row;
					cur_col = col;
					cur_val = grid[row][col];
					if (cur_val == EMPTY) {
						grid[row][col] = OBST;
					}
					if (cur_val == OBST) {
						grid[row][col] = EMPTY;
					}
				}
				repaint();
			}

			@Override
			public void mouseDragged(MouseEvent evt) {
				int row = (evt.getY() - 10) / squareSize;
				int col = (evt.getX() - 10) / squareSize;
				if (row >= 0 && row < rows && col >= 0 && col < columns && !searching && !found) {
					if ((row * columns + col != cur_row * columns + cur_col)
							&& (cur_val == ROBOT || cur_val == TARGET)) {
						int new_val = grid[row][col];
						if (new_val == EMPTY) {
							grid[row][col] = cur_val;
							if (cur_val == ROBOT) {
								robotStart.row = row;
								robotStart.col = col;
							} else {
								targetPos.row = row;
								targetPos.col = col;
							}
							grid[cur_row][cur_col] = new_val;
							cur_row = row;
							cur_col = col;
							if (cur_val == ROBOT) {
								robotStart.row = cur_row;
								robotStart.col = cur_col;
							} else {
								targetPos.row = cur_row;
								targetPos.col = cur_col;
							}
							cur_val = grid[row][col];
						}
					} else if (grid[row][col] != ROBOT && grid[row][col] != TARGET) {

						if (cur_val == EMPTY) {
							grid[row][col] = OBST;
						}
						if (cur_val == OBST) {
							grid[row][col] = EMPTY;
						}
						
					}
				}
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent evt) {
			}

			@Override
			public void mouseEntered(MouseEvent evt) {
			}

			@Override
			public void mouseExited(MouseEvent evt) {
			}

			@Override
			public void mouseMoved(MouseEvent evt) {
			}

			@Override
			public void mouseClicked(MouseEvent evt) {
			}

		} // end class MouseHandler

		// Quando o usuário pressiona um botão executa a funcionalidade correspondente

		private class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent evt) {
				String cmd = evt.getActionCommand();
				if (cmd.equals("Limpar")) {
					fillGrid();
					dfs.setEnabled(true);
					bfs.setEnabled(true);
					aStar.setEnabled(true);
					guloso.setEnabled(true);
					dijkstra.setEnabled(true);
					diagonal.setEnabled(true);
					drawArrows.setEnabled(true);
				} else if (cmd.equals("Step-by-Step") && !found && !endOfSearch) {

					if (!searching && dijkstra.isSelected()) {
						initializeDijkstra();
					}
					searching = true;
					message.setText(msgSelectStepByStepEtc);
					dfs.setEnabled(false);
					bfs.setEnabled(false);
					aStar.setEnabled(false);
					guloso.setEnabled(false);
					dijkstra.setEnabled(false);
					diagonal.setEnabled(false);
					drawArrows.setEnabled(false);
					timer.stop();

					// Aqui nós decidimos se podemos continuar
					// a procura no modo "Passo-a-Passo" ou não.
					
					// No caso de DFS, BFS, A * e algoritmo guloso
					// Aqui temos a segunda etapa:
					// 2. Se conjunto aberto = [], em seguida, encerrar. Não há solução.

					if ((dijkstra.isSelected() && graph.isEmpty()) || (!dijkstra.isSelected() && openSet.isEmpty())) {
						endOfSearch = true;
						grid[robotStart.row][robotStart.col] = ROBOT;
						message.setText(msgNoSolution);
					} else {
						expandNode();
						if (found) {
							plotRoute();
						}
					}
					repaint();
				} else if (cmd.equals("INICIAR") && !endOfSearch) {
					if (!searching && dijkstra.isSelected()) {
						initializeDijkstra();
					}
					searching = true;
					message.setText(msgSelectStepByStepEtc);
					dfs.setEnabled(false);
					bfs.setEnabled(false);
					aStar.setEnabled(false);
					guloso.setEnabled(false);
					dijkstra.setEnabled(false);
					diagonal.setEnabled(false);
					drawArrows.setEnabled(false);
					timer.setDelay(delay);
					timer.start();
				}
			}
		} // end class ActionHandler

		// A classe que é responsável pela animação
		
		private class RepaintAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent evt) {

				// Aqui nós decidir se podemos ou não continuar
				// a pesquisa com 'Animação'.
				// No caso de DFS, BFS, A * e algoritmo guloso
				// aqui temos a segunda etapa:
				// 2. Se conjunto aberto = [], em seguida, encerrar. Não há uma
				// solução.

				if ((dijkstra.isSelected() && graph.isEmpty()) || (!dijkstra.isSelected() && openSet.isEmpty())) {
					endOfSearch = true;
					grid[robotStart.row][robotStart.col] = ROBOT;
					message.setText(msgNoSolution);
				} else {
					expandNode();
					if (found) {
						timer.stop();
						endOfSearch = true;
						plotRoute();
					}
				}
				repaint();
			}
		} // end class RepaintAction

		// Cria um labirinto aleatório (sem ciclos).
		// Adaptado de stackoverflow.com:
		// http://stackoverflow.com/questions/18396364/maze-generation-Índice de
		// matriz fora dos limites de exceção

		private class MyMaze {
			private int dimensionX, dimensionY; // dimensão do labirinto
			private int gridDimensionX, gridDimensionY; // dimensão da saida do grid
														
			private char[][] mazeGrid; // saida do grid
			private Cell[][] cells;    // 2d array para células
			private Random random = new Random(); // Objeto random

			public MyMaze(int aDimension) {
				this(aDimension, aDimension);
			}

			// constructor
			public MyMaze(int xDimension, int yDimension) {
				dimensionX = xDimension;
				dimensionY = yDimension;
				gridDimensionX = xDimension * 2 + 1;
				gridDimensionY = yDimension * 2 + 1;
				mazeGrid = new char[gridDimensionX][gridDimensionY];
				init();
				generateMaze();
			}

			private void init() {
				// criando celulas
				cells = new Cell[dimensionX][dimensionY];
				for (int x = 0; x < dimensionX; x++) {
					for (int y = 0; y < dimensionY; y++) {
						cells[x][y] = new Cell(x, y, false);
					}
				}
			}

			// classe interna representando a célula
			private class Cell {
				int x, y; // coordenadas
						  // cells this cell is connected to
				ArrayList<Cell> neighbors = new ArrayList<>();
				// célula intransponível
				boolean wall = true;
				// if true, has yet to be used in generation
				boolean open = true;

				// contrutor célula (x,y)
				Cell(int x, int y) {
					this(x, y, true);
				}

				// construir célula em x, y se de fato é isWall
				Cell(int x, int y, boolean isWall) {
					this.x = x;
					this.y = y;
					this.wall = isWall;
				}

				// Adiciona um vizinho para essa célula, e esta célula como um vizinho
				// o outro
				void addNeighbor(Cell other) {
					if (!this.neighbors.contains(other)) { // avoid duplicates
						this.neighbors.add(other);
					}
					if (!other.neighbors.contains(this)) { // avoid duplicates
						other.neighbors.add(this);
					}
				}

				// usado em updateGrid()
				boolean isCellBelowNeighbor() {
					return this.neighbors.contains(new Cell(this.x, this.y + 1));
				}

				// usado em updateGrid()
				boolean isCellRightNeighbor() {
					return this.neighbors.contains(new Cell(this.x + 1, this.y));
				}

				// Celular útil equivalente
				@Override
				public boolean equals(Object other) {
					if (!(other instanceof Cell))
						return false;
					Cell otherCell = (Cell) other;
					return (this.x == otherCell.x && this.y == otherCell.y);
				}

				// Deve ser sobrescrito com iguais
				@Override
				public int hashCode() {
					// Método hashcode aleatório projetado para ser geralmente único
					return this.x + this.y * 256;
				}
			}

			// Gerar a partir da esquerda (no cálculo do Y aumenta para baixo
			// frequentemente)
			private void generateMaze() {
				generateMaze(0, 0);
			}

			// Gerar o labirinto de coordenadas x, y
			private void generateMaze(int x, int y) {
				generateMaze(getCell(x, y)); // Gerar a partir da celula
			}

			private void generateMaze(Cell startAt) {
				if (startAt == null)
					return;
				startAt.open = false; // Indica célula fechada para a geração
				ArrayList<Cell> cellsList = new ArrayList<>();
				cellsList.add(startAt);

				while (!cellsList.isEmpty()) {
					Cell cell;
					
					// Isso é reduzir, mas não eliminar completamente o número
					// De caminhos "enrolados" longos, mais fácil de detectar ramos
					// O que resulta em labirintos fáceis
					if (random.nextInt(10) == 0)
						cell = cellsList.remove(random.nextInt(cellsList.size()));
					else
						cell = cellsList.remove(cellsList.size() - 1);
					ArrayList<Cell> neighbors = new ArrayList<>();
					// células que poderiam ser vizinhos
					Cell[] potentialNeighbors = new Cell[] { getCell(cell.x + 1, cell.y), getCell(cell.x, cell.y + 1),
							getCell(cell.x - 1, cell.y), getCell(cell.x, cell.y - 1) };
					for (Cell other : potentialNeighbors) {
						// ignorar se é uma parede ou não está aberto
						if (other == null || other.wall || !other.open)
							continue;
						neighbors.add(other);
					}
					if (neighbors.isEmpty())
						continue;
					// gerar célular random
					Cell selected = neighbors.get(random.nextInt(neighbors.size()));
					
					selected.open = false;  // Indica célula fechada para
											// geração
					cell.addNeighbor(selected);
					cellsList.add(cell);
					cellsList.add(selected);
				}
				updateGrid();
			}

			// Usado para obter um celular em x, y; retorna nulo fora dos limites
			public Cell getCell(int x, int y) {
				try {
					return cells[x][y];
				} catch (ArrayIndexOutOfBoundsException e) { // fora dos limites
					return null;
				}
			}

			// desenhando labirinto
			public void updateGrid() {
				char backChar = ' ', wallChar = 'X', cellChar = ' ';
				// preenchendo fundo
				for (int x = 0; x < gridDimensionX; x++) {
					for (int y = 0; y < gridDimensionY; y++) {
						mazeGrid[x][y] = backChar;
					}
				}
				// construindo paredes
				for (int x = 0; x < gridDimensionX; x++) {
					for (int y = 0; y < gridDimensionY; y++) {
						if (x % 2 == 0 || y % 2 == 0)
							mazeGrid[x][y] = wallChar;
					}
				}
				// fazer representação significativa
				for (int x = 0; x < dimensionX; x++) {
					for (int y = 0; y < dimensionY; y++) {
						Cell current = getCell(x, y);
						int gridX = x * 2 + 1, gridY = y * 2 + 1;
						mazeGrid[gridX][gridY] = cellChar;
						if (current.isCellBelowNeighbor()) {
							mazeGrid[gridX][gridY + 1] = cellChar;
						}
						if (current.isCellRightNeighbor()) {
							mazeGrid[gridX + 1][gridY] = cellChar;
						}
					}
				}

				// Criamos uma grade limpa ...
				searching = false;
				endOfSearch = false;
				fillGrid();
				// ... e copiar nele as posições dos obstáculos
				//criado pelo algoritmo de construção labirinto
				for (int x = 0; x < gridDimensionX; x++) {
					for (int y = 0; y < gridDimensionY; y++) {
						if (mazeGrid[x][y] == wallChar && grid[x][y] != ROBOT && grid[x][y] != TARGET) {
							grid[x][y] = OBST;
						}
					}
				}
			}
		} // end class MyMaze

		
		/*
		 **********************************************************
		 * Constantes da classe MazePanel
		 **********************************************************
		 */

		public final static int INFINITY = Integer.MAX_VALUE, 
				EMPTY = 0,	 // celula vazia
				OBST = 1, 	 // celula com obstaculo
				ROBOT = 2, 	 // posição do no atual
				TARGET = 3,  // posição do destino
				FRONTIER = 4,// celulas da fronteira
				CLOSED = 5,  // celular do caminho fechado
				ROUTE = 6;   // celulas que formam o caminho origem-destino

		// mensagens ao usuário
		private final static String msgDrawAndSelect = "\"Pinte\" os obstáculos, depois click em 'Passo-a-Passo' ou em 'Iniciar'",
				msgSelectStepByStepEtc = "Click 'Passo-a-Passo' em 'Iniciar' ou em 'Limpar'",
				msgNoSolution = "Não há caminho para o destino selecionado!";

		/*
		 **********************************************************
		 * Variáveis da class MazePanel
		 **********************************************************
		 */

		JTextField rowsField, columnsField;

		int rows = 10, // linhas da grade default
				columns = 10, // colunas da grade default
				squareSize = 500 / rows; // tamanho da celulas em pixels

		int arrowSize = squareSize / 2; // the size of the tip of the arrow
										// pointing the predecessor cell
		ArrayList<Cell> openSet = new ArrayList();  // lista aberta
		ArrayList<Cell> closedSet = new ArrayList();// lista fechada
		ArrayList<Cell> graph = new ArrayList();

		Cell robotStart; // posição inicial do nó atual
		Cell targetPos;  // posição do destino

		JLabel message; // mensagem ao usuário

		// botoes de seleção de algoritmo
		JRadioButton dfs, bfs, aStar, guloso, dijkstra;
		// slider de velocidade de animação
		JSlider slider;
		// movimentos na diagonal
		JCheckBox diagonal;
		// desenha seta para o predecessor
		JCheckBox drawArrows;

		int[][] grid; 		// the grid
		boolean found;      // flag no encrontrado
		boolean searching;  // flag pesquisa em progresso
		boolean endOfSearch;// flag busca chegou ao fim
		int delay; 			// tempo de delay da animação (in msec)
		int expanded;		// numero de nos expandidos

		// objeto de controle da animação
		RepaintAction action = new RepaintAction();

		// o temporizador que regula a velocidade de execução da animação
		Timer timer;

		/**
		 * The creator of the panel
		 * 
		 * @param width
		 *            the width of the panel.
		 * @param height
		 *            the height of the panel.
		 */
		public MazePanel(int width, int height) {

			setLayout(null);

			MouseHandler listener = new MouseHandler();
			addMouseListener(listener);
			addMouseMotionListener(listener);

			setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.blue));

			setPreferredSize(new Dimension(width, height));

			grid = new int[rows][columns];

			// Criando o conteudo do panel

			message = new JLabel(msgDrawAndSelect, JLabel.CENTER);
			message.setForeground(Color.blue);
			message.setFont(new Font("Helvetica", Font.PLAIN, 16));

			JLabel rowsLbl = new JLabel("Nº Linhas (5-50):", JLabel.RIGHT);
			rowsLbl.setFont(new Font("Helvetica", Font.PLAIN, 13));

			rowsField = new JTextField();
			rowsField.setText(Integer.toString(rows));

			JLabel columnsLbl = new JLabel("Nº Colunas (5-50):", JLabel.RIGHT);
			columnsLbl.setFont(new Font("Helvetica", Font.PLAIN, 13));

			columnsField = new JTextField();
			columnsField.setText(Integer.toString(columns));

			JButton resetButton = new JButton("Novo GRID");
			resetButton.addActionListener(new ActionHandler());
			resetButton.setBackground(Color.lightGray);
			resetButton
					.setToolTipText("Limpa e redesenha o GRID de acordo com o número de linhas e colunas estipulado.");
			resetButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					resetButtonActionPerformed(evt);
				}
			});

			JButton mazeButton = new JButton("Gerar Labirinto");
			mazeButton.addActionListener(new ActionHandler());
			mazeButton.setBackground(Color.lightGray);
			mazeButton.setToolTipText("Cria um labirinto aleatório");
			mazeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					mazeButtonActionPerformed(evt);
				}
			});

			JButton clearButton = new JButton("Limpar");
			clearButton.addActionListener(new ActionHandler());
			clearButton.setBackground(Color.lightGray);
			clearButton.setToolTipText("Primeiro click: Limpa as buscas, Segundo click: Limpa obstáculos");

			JButton stepButton = new JButton("Passo-a-Passo");
			stepButton.addActionListener(new ActionHandler());
			stepButton.setBackground(Color.lightGray);
			stepButton.setToolTipText("A busca é realizada passo-a-passo pelo click");

			JButton animationButton = new JButton("INICIAR");
			animationButton.addActionListener(new ActionHandler());
			animationButton.setBackground(Color.lightGray);
			animationButton.setToolTipText("A pesquisa é realizada automaticamente");

			JLabel velocity = new JLabel("Velocidade", JLabel.CENTER);
			velocity.setFont(new Font("Helvetica", Font.PLAIN, 10));

			slider = new JSlider(0, 1000, 500); // valor inicial do slider em 500 msec
			slider.setToolTipText("Regula o atraso para cada etapa (0 to 1 sec)");

			delay = 1000 - slider.getValue();
			slider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {
					JSlider source = (JSlider) evt.getSource();
					if (!source.getValueIsAdjusting()) {
						delay = 1000 - source.getValue();
					}
				}
			});

			// ButtonGroup que sincroniza os cinco botões de rádio 
			// escolha do algoritmo, de modo que apenas uma 
			// pode ser selecionada a qualquer momento
			
			ButtonGroup algoGroup = new ButtonGroup();

			dfs = new JRadioButton("DFS");
			dfs.setToolTipText("Depth First Search algorithm");
			algoGroup.add(dfs);
			dfs.addActionListener(new ActionHandler());

			bfs = new JRadioButton("BFS");
			bfs.setToolTipText("Breadth First Search algorithm");
			algoGroup.add(bfs);
			bfs.addActionListener(new ActionHandler());

			aStar = new JRadioButton("A*");
			aStar.setToolTipText("Algoritmo A Estrela ");
			aStar.setSelected(true);
			algoGroup.add(aStar);
			aStar.addActionListener(new ActionHandler());

			guloso = new JRadioButton("Guloso");
			guloso.setToolTipText("Algoritmo de Busca Gulosa");
			algoGroup.add(guloso);
			guloso.addActionListener(new ActionHandler());

			dijkstra = new JRadioButton("Dijkstra");
			dijkstra.setToolTipText("Dijkstra's algorithm");
			algoGroup.add(dijkstra);
			dijkstra.addActionListener(new ActionHandler());

			JPanel algoPanel = new JPanel();
			algoPanel.setBorder(
					javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),
							"Algoritmos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.TOP, new java.awt.Font("Helvetica", 0, 14)));

			aStar.setSelected(true); // A* selecionado como inicial

			diagonal = new JCheckBox("Movimentos Diagonais");
			diagonal.setToolTipText("Movimentos diagonais também estão autorizados");
			diagonal.setSelected(true);

			drawArrows = new JCheckBox("Setas para antecessores");
			drawArrows.setToolTipText("Desenha setas para os antecessores");

			JLabel robot = new JLabel("INICIO", JLabel.LEFT);
			robot.setForeground(Color.red);
			robot.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel target = new JLabel("ALVO", JLabel.LEFT);
			target.setForeground(Color.green);
			target.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel frontier = new JLabel("FRONTEIRA", JLabel.LEFT);
			frontier.setForeground(Color.blue);
			frontier.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel closed = new JLabel("FECHADOS", JLabel.LEFT);
			closed.setForeground(Color.cyan);
			closed.setFont(new Font("Helvetica", Font.BOLD, 14));

			JButton aboutButton = new JButton("Sobre");
			aboutButton.addActionListener(new ActionHandler());
			aboutButton.setBackground(Color.lightGray);
			aboutButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					aboutButtonActionPerformed(evt);
				}
			});

			//Adicionando conteudo ao Panel
			add(message);
			add(rowsLbl);
			add(rowsField);
			add(columnsLbl);
			add(columnsField);
			add(resetButton);
			add(mazeButton);
			add(clearButton);
			add(stepButton);
			add(animationButton);
			add(velocity);
			add(slider);
			add(dfs);
			add(bfs);
			add(aStar);
			add(guloso);
			add(dijkstra);
			add(algoPanel);
			add(diagonal);
			// add(drawArrows);
			add(robot);
			add(target);
			add(frontier);
			add(closed);
			add(aboutButton);

			// Regulando posição dos componentes
			message.setBounds(0, 515, 500, 23);
			rowsLbl.setBounds(520, 5, 140, 25);
			rowsField.setBounds(665, 5, 25, 25);
			columnsLbl.setBounds(520, 35, 140, 25);
			columnsField.setBounds(665, 35, 25, 25);
			resetButton.setBounds(520, 65, 170, 25);
			mazeButton.setBounds(520, 95, 170, 25);
			clearButton.setBounds(520, 125, 170, 25);
			stepButton.setBounds(520, 155, 170, 25);
			animationButton.setBounds(520, 185, 170, 25);
			velocity.setBounds(520, 215, 170, 10);
			slider.setBounds(520, 225, 170, 25);
			// dfs.setBounds(530, 270, 70, 25);
			// bfs.setBounds(600, 270, 70, 25);
			aStar.setBounds(530, 295, 70, 25);
			guloso.setBounds(600, 295, 85, 25);
			// dijkstra.setBounds(530, 320, 85, 25);
			algoPanel.setLocation(520, 250);
			algoPanel.setSize(170, 100);
			diagonal.setBounds(520, 355, 170, 25);
			// drawArrows.setBounds(520, 380, 170, 25);
			robot.setBounds(530, 380, 80, 25);
			target.setBounds(530, 400, 80, 25);
			frontier.setBounds(530, 420, 80, 25);
			closed.setBounds(530, 440, 80, 25);
			aboutButton.setBounds(520, 515, 170, 25);

			// Criando timer
			timer = new Timer(delay, action);

			// Nós ligamos às células nos valores iniciais de grade.
			// Aqui é os primeiros passos dos algoritmos
			fillGrid();

		} // end constructor

		/**
		 * Function executed if the user presses the button "New Grid"
		 */
		private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
			initializeGrid(false);
		} // end resetButtonActionPerformed()

		/**
		 * Function executed if the user presses the button "Maze"
		 */
		private void mazeButtonActionPerformed(java.awt.event.ActionEvent evt) {
			initializeGrid(true);
		} // end mazeButtonActionPerformed()

		/**
		 * Creates a new clean grid or a new maze
		 */
		private void initializeGrid(Boolean makeMaze) {
			int oldRows = rows;
			int oldColumns = columns;
			try {
				if (!rowsField.getText().isEmpty()) {
					rows = Integer.parseInt(rowsField.getText());
				} else {
					JOptionPane.showMessageDialog(this, "O Número de Linhas tem que ser respeitado! (5~50)", "Erro",
							JOptionPane.ERROR_MESSAGE);
					rows = oldRows;
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "O Número de Linhas tem que ser respeitado! (5~50)", "Erro",
						JOptionPane.ERROR_MESSAGE);
				rows = oldRows;
				return;
			}
			if (rows < 5 || rows > 50) {
				JOptionPane.showMessageDialog(this, "O Número de Linhas tem que ser respeitado! (5~50)", "Erro",
						JOptionPane.ERROR_MESSAGE);
				rows = oldRows;
				return;
			}
			try {
				if (!columnsField.getText().isEmpty()) {
					columns = Integer.parseInt(columnsField.getText());
				} else {
					JOptionPane.showMessageDialog(this, "O Número de Colunas tem que ser respeitado! (5~50)", "Erro",
							JOptionPane.ERROR_MESSAGE);
					columns = oldColumns;
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "O Número de Colunas tem que ser respeitado! (5~50)", "Erro",
						JOptionPane.ERROR_MESSAGE);
				columns = oldColumns;
				return;
			}
			if (columns < 5 || columns > 50) {
				JOptionPane.showMessageDialog(this, "O Número de Colunas tem que ser respeitado! (5~50)", "Erro",
						JOptionPane.ERROR_MESSAGE);
				columns = oldColumns;
				return;
			}
			squareSize = 500 / (rows > columns ? rows : columns);
			arrowSize = squareSize / 2;
			// the maze must have an odd number of rows and columns
			if (makeMaze && rows % 2 == 0) {
				rows -= 1;
			}
			if (makeMaze && columns % 2 == 0) {
				columns -= 1;
			}
			grid = new int[rows][columns];
			robotStart = new Cell(rows - 2, 1);
			targetPos = new Cell(1, columns - 2);
			dfs.setEnabled(true);
			dfs.setSelected(true);
			bfs.setEnabled(true);
			aStar.setEnabled(true);
			guloso.setEnabled(true);
			dijkstra.setEnabled(true);
			diagonal.setSelected(false);
			diagonal.setEnabled(true);
			drawArrows.setSelected(false);
			drawArrows.setEnabled(true);
			slider.setValue(500);
			if (makeMaze) {
				MyMaze maze = new MyMaze(rows / 2, columns / 2);
			} else {
				fillGrid();
			}
		} // end
			// initializeGrid()

		/**
		 * Function executed if the user presses the button "Sobre"
		 */
		private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {
			AboutBox aboutBox = new AboutBox(mazeFrame, true);
			aboutBox.setVisible(true);
		} // end aboutButtonActionPerformed()

		/**
		 * Expands a node and creates his successors
		 */
		private void expandNode() {
			// Dijkstra's algorithm to handle separately
			if (dijkstra.isSelected()) {
				Cell u;
				// 11: while Q is not empty:
				if (graph.isEmpty()) {
					return;
				}
				// 12: u := vertex in Q (graph) with smallest distance in dist[]
				// ;
				// 13: remove u from Q (graph);
				u = graph.remove(0);
				// Add vertex u in closed set
				closedSet.add(u);
				// If target has been found ...
				if (u.row == targetPos.row && u.col == targetPos.col) {
					found = true;
					return;
				}
				// Counts nodes that have expanded.
				expanded++;
				// Update the color of the cell
				grid[u.row][u.col] = CLOSED;
				// 14: if dist[u] = infinity:
				if (u.dist == INFINITY) {
					// ... then there is no solution.
					// 15: break;
					return;
					// 16: end if
				}
				// Create the neighbors of u
				ArrayList<Cell> neighbors = createSuccesors(u, false);
				// 18: for each neighbor v of u:
				for (Cell v : neighbors) {
					// 20: alt := dist[u] + dist_between(u, v) ;
					int alt = u.dist + distBetween(u, v);
					// 21: if alt < dist[v]:
					if (alt < v.dist) {
						// 22: dist[v] := alt ;
						v.dist = alt;
						// 23: previous[v] := u ;
						v.prev = u;
						// Update the color of the cell
						grid[v.row][v.col] = FRONTIER;
						// 24: decrease-key v in Q;
						// (sort list of nodes with respect to dist)
						Collections.sort(graph, new CellComparatorByDist());
					}
				}
				// The handling of the other four algorithms
			} else {
				Cell current;
				if (dfs.isSelected() || bfs.isSelected()) {
					// Here is the 3rd step of the algorithms DFS and BFS
					// 3. Remove the first state, Si, from OPEN SET ...
					current = openSet.remove(0);
				} else {
					// Here is the 3rd step of the algorithms A* and Greedy
					// 3. Remove the first state, Si, from OPEN SET,
					// for which f(Si) ≤ f(Sj) for all other
					// open states Sj ...
					// (sort first OPEN SET list with respect to 'f')
					Collections.sort(openSet, new CellComparatorByF());
					current = openSet.remove(0);
				}
				// ... and add it to CLOSED SET.
				closedSet.add(0, current);
				// Update the color of the cell
				grid[current.row][current.col] = CLOSED;
				// If the selected node is the target ...
				if (current.row == targetPos.row && current.col == targetPos.col) {
					// ... then terminate etc
					Cell last = targetPos;
					last.prev = current.prev;
					closedSet.add(last);
					found = true;
					return;
				}
				// Count nodes that have been expanded.
				expanded++;
				// Here is the 4rd step of the algorithms
				// 4. Create the successors of Si, based on actions
				// that can be implemented on Si.
				// Each successor has a pointer to the Si, as its predecessor.
				// In the case of DFS and BFS algorithms, successors should not
				// belong neither to the OPEN SET nor the CLOSED SET.
				ArrayList<Cell> succesors;
				succesors = createSuccesors(current, false);
				// Here is the 5th step of the algorithms
				// 5. For each successor of Si, ...
				for (Cell cell : succesors) {
					// ... if we are running DFS ...
					if (dfs.isSelected()) {
						// ... add the successor at the beginning of the list
						// OPEN SET
						openSet.add(0, cell);
						// Update the color of the cell
						grid[cell.row][cell.col] = FRONTIER;
						// ... if we are runnig BFS ...
					} else if (bfs.isSelected()) {
						// ... add the successor at the end of the list OPEN SET
						openSet.add(cell);
						// Update the color of the cell
						grid[cell.row][cell.col] = FRONTIER;
						// ... if we are running A* or Greedy algorithms (step 5
						// of A* algorithm) ...
					} else if (aStar.isSelected() || guloso.isSelected()) {
						// ... calculate the value f(Sj) ...
						int dxg = current.col - cell.col;
						int dyg = current.row - cell.row;
						int dxh = targetPos.col - cell.col;
						int dyh = targetPos.row - cell.row;
						if (diagonal.isSelected()) {
							// with diagonal movements
							// calculate 1000 times the Euclidean distance
							if (guloso.isSelected()) {
								// especially for the Greedy ...
								cell.g = 0;
							} else {
								cell.g = current.g + (int) ((double) 1000 * Math.sqrt(dxg * dxg + dyg * dyg));
							}
							cell.h = (int) ((double) 1000 * Math.sqrt(dxh * dxh + dyh * dyh));
						} else {
							// without diagonal movements
							// calculate Manhattan distances
							if (guloso.isSelected()) {
								// especially for the Greedy ...
								cell.g = 0;
							} else {
								cell.g = current.g + Math.abs(dxg) + Math.abs(dyg);
							}
							cell.h = Math.abs(dxh) + Math.abs(dyh);
						}
						cell.f = cell.g + cell.h;
						// ... If Sj is neither in the OPEN SET nor in the
						// CLOSED SET states ...
						int openIndex = isInList(openSet, cell);
						int closedIndex = isInList(closedSet, cell);
						if (openIndex == -1 && closedIndex == -1) {
							// ... then add Sj in the OPEN SET ...
							// ... evaluated as f(Sj)
							openSet.add(cell);
							// Update the color of the cell
							grid[cell.row][cell.col] = FRONTIER;
							// Else ...
						} else {
							// ... if already belongs to the OPEN SET, then ...
							if (openIndex > -1) {
								// ... compare the new value assessment with the
								// old one.
								// If old <= new ...
								if (openSet.get(openIndex).f <= cell.f) {
									// ... then eject the new node with state
									// Sj.
									// (ie do nothing for this node).
									// Else, ...
								} else {
									// ... remove the element (Sj, old) from the
									// list
									// to which it belongs ...
									openSet.remove(openIndex);
									// ... and add the item (Sj, new) to the
									// OPEN SET.
									openSet.add(cell);
									// Update the color of the cell
									grid[cell.row][cell.col] = FRONTIER;
								}
								// ... if already belongs to the CLOSED SET,
								// then ...
							} else {
								// ... compare the new value assessment with the
								// old one.
								// If old <= new ...
								if (closedSet.get(closedIndex).f <= cell.f) {
									// ... then eject the new node with state
									// Sj.
									// (ie do nothing for this node).
									// Else, ...
								} else {
									// ... remove the element (Sj, old) from the
									// list
									// to which it belongs ...
									closedSet.remove(closedIndex);
									// ... and add the item (Sj, new) to the
									// OPEN SET.
									openSet.add(cell);
									// Update the color of the cell
									grid[cell.row][cell.col] = FRONTIER;
								}
							}
						}
					}
				}
			}
		} // end expandNode()

		/**
		 * Creates the successors of a state/cell
		 * 
		 * @param current
		 *            the cell for which we ask successors
		 * @param makeConnected
		 *            flag that indicates that we are interested only on the
		 *            coordinates of cells and not on the label 'dist' (concerns
		 *            only Dijkstra's)
		 * @return the successors of the cell as a list
		 */
		private ArrayList<Cell> createSuccesors(Cell current, boolean makeConnected) {
			int r = current.row;
			int c = current.col;
			// We create an empty list for the successors of the current cell.
			ArrayList<Cell> temp = new ArrayList<>();
			// With diagonal movements priority is:
			// 1: Up 2: Up-right 3: Right 4: Down-right
			// 5: Down 6: Down-left 7: Left 8: Up-left

			// Without diagonal movements the priority is:
			// 1: Up 2: Right 3: Down 4: Left

			// If not at the topmost limit of the grid
			// and the up-side cell is not an obstacle ...
			if (r > 0 && grid[r - 1][c] != OBST &&
					// ... and (only in the case are not running the A* or
					// Greedy)
					// not already belongs neither to the OPEN SET nor to the
					// CLOSED SET ...
			((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
					: isInList(openSet, new Cell(r - 1, c)) == -1 && isInList(closedSet, new Cell(r - 1, c)) == -1)) {
				Cell cell = new Cell(r - 1, c);
				// In the case of Dijkstra's algorithm we can not append to
				// the list of successors the "naked" cell we have just created.
				// The cell must be accompanied by the label 'dist',
				// so we need to track it down through the list 'graph'
				// and then copy it back to the list of successors.
				// The flag makeConnected is necessary to be able
				// the present method createSuccesors() to collaborate
				// with the method findConnectedComponent(), which creates
				// the connected component when Dijkstra's initializes.
				if (dijkstra.isSelected()) {
					if (makeConnected) {
						temp.add(cell);
					} else {
						int graphIndex = isInList(graph, cell);
						if (graphIndex > -1) {
							temp.add(graph.get(graphIndex));
						}
					}
				} else {
					// ... update the pointer of the up-side cell so it points
					// the current one ...
					cell.prev = current;
					// ... and add the up-side cell to the successors of the
					// current one.
					temp.add(cell);
				}
			}
			if (diagonal.isSelected()) {
				// If we are not even at the topmost nor at the rightmost border
				// of the grid
				// and the up-right-side cell is not an obstacle ...
				if (r > 0 && c < columns - 1 && grid[r - 1][c + 1] != OBST &&
						// ... and one of the upper side or right side cells are
						// not obstacles ...
						// (because it is not reasonable to allow
						// the robot to pass through a "slot")
//				(grid[r - 1][c] != OBST || grid[r][c + 1] != OBST) &&
						// ... and (only in the case are not running the A* or
						// Greedy)
						// not already belongs neither to the OPEN SET nor
						// CLOSED SET ...
				((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
						: isInList(openSet, new Cell(r - 1, c + 1)) == -1
								&& isInList(closedSet, new Cell(r - 1, c + 1)) == -1)) {
					Cell cell = new Cell(r - 1, c + 1);
					if (dijkstra.isSelected()) {
						if (makeConnected) {
							temp.add(cell);
						} else {
							int graphIndex = isInList(graph, cell);
							if (graphIndex > -1) {
								temp.add(graph.get(graphIndex));
							}
						}
					} else {
						// ... update the pointer of the up-right-side cell so
						// it points the current one ...
						cell.prev = current;
						// ... and add the up-right-side cell to the successors
						// of the current one.
						temp.add(cell);
					}
				}
			}
			// If not at the rightmost limit of the grid
			// and the right-side cell is not an obstacle ...
			if (c < columns - 1 && grid[r][c + 1] != OBST &&
					// ... and (only in the case are not running the A* or
					// Greedy)
					// not already belongs neither to the OPEN SET nor to the
					// CLOSED SET ...
			((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
					: isInList(openSet, new Cell(r, c + 1)) == -1 && isInList(closedSet, new Cell(r, c + 1)) == -1)) {
				Cell cell = new Cell(r, c + 1);
				if (dijkstra.isSelected()) {
					if (makeConnected) {
						temp.add(cell);
					} else {
						int graphIndex = isInList(graph, cell);
						if (graphIndex > -1) {
							temp.add(graph.get(graphIndex));
						}
					}
				} else {
					// ... update the pointer of the right-side cell so it
					// points the current one ...
					cell.prev = current;
					// ... and add the right-side cell to the successors of the
					// current one.
					temp.add(cell);
				}
			}
			if (diagonal.isSelected()) {
				// If we are not even at the lowermost nor at the rightmost
				// border of the grid
				// and the down-right-side cell is not an obstacle ...
				if (r < rows - 1 && c < columns - 1 && grid[r + 1][c + 1] != OBST &&
						// ... and one of the down-side or right-side cells are
						// not obstacles ...
//				(grid[r + 1][c] != OBST || grid[r][c + 1] != OBST) &&
						// ... and (only in the case are not running the A* or
						// Greedy)
						// not already belongs neither to the OPEN SET nor to
						// the CLOSED SET ...
				((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
						: isInList(openSet, new Cell(r + 1, c + 1)) == -1
								&& isInList(closedSet, new Cell(r + 1, c + 1)) == -1)) {
					Cell cell = new Cell(r + 1, c + 1);
					if (dijkstra.isSelected()) {
						if (makeConnected) {
							temp.add(cell);
						} else {
							int graphIndex = isInList(graph, cell);
							if (graphIndex > -1) {
								temp.add(graph.get(graphIndex));
							}
						}
					} else {
						// ... update the pointer of the downr-right-side cell
						// so it points the current one ...
						cell.prev = current;
						// ... and add the down-right-side cell to the
						// successors of the current one.
						temp.add(cell);
					}
				}
			}
			// If not at the lowermost limit of the grid
			// and the down-side cell is not an obstacle ...
			if (r < rows - 1 && grid[r + 1][c] != OBST &&
					// ... and (only in the case are not running the A* or
					// Greedy)
					// not already belongs neither to the OPEN SET nor to the
					// CLOSED SET ...
			((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
					: isInList(openSet, new Cell(r + 1, c)) == -1 && isInList(closedSet, new Cell(r + 1, c)) == -1)) {
				Cell cell = new Cell(r + 1, c);
				if (dijkstra.isSelected()) {
					if (makeConnected) {
						temp.add(cell);
					} else {
						int graphIndex = isInList(graph, cell);
						if (graphIndex > -1) {
							temp.add(graph.get(graphIndex));
						}
					}
				} else {
					// ... update the pointer of the down-side cell so it points
					// the current one ...
					cell.prev = current;
					// ... and add the down-side cell to the successors of the
					// current one.
					temp.add(cell);
				}
			}
			if (diagonal.isSelected()) {
				// If we are not even at the lowermost nor at the leftmost
				// border of the grid
				// and the down-left-side cell is not an obstacle ...
				if (r < rows - 1 && c > 0 && grid[r + 1][c - 1] != OBST &&
						// ... and one of the down-side or left-side cells are
						// not obstacles ...
//				(grid[r + 1][c] != OBST || grid[r][c - 1] != OBST) &&
						// ... and (only in the case are not running the A* or
						// Greedy)
						// not already belongs neither to the OPEN SET nor to
						// the CLOSED SET ...
				((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
						: isInList(openSet, new Cell(r + 1, c - 1)) == -1
								&& isInList(closedSet, new Cell(r + 1, c - 1)) == -1)) {
					Cell cell = new Cell(r + 1, c - 1);
					if (dijkstra.isSelected()) {
						if (makeConnected) {
							temp.add(cell);
						} else {
							int graphIndex = isInList(graph, cell);
							if (graphIndex > -1) {
								temp.add(graph.get(graphIndex));
							}
						}
					} else {
						// ... update the pointer of the down-left-side cell so
						// it points the current one ...
						cell.prev = current;
						// ... and add the down-left-side cell to the successors
						// of the current one.
						temp.add(cell);
					}
				}
			}
			// If not at the leftmost limit of the grid
			// and the left-side cell is not an obstacle ...
			if (c > 0 && grid[r][c - 1] != OBST &&
					// ... and (only in the case are not running the A* or
					// Greedy)
					// not already belongs neither to the OPEN SET nor to the
					// CLOSED SET ...
			((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
					: isInList(openSet, new Cell(r, c - 1)) == -1 && isInList(closedSet, new Cell(r, c - 1)) == -1)) {
				Cell cell = new Cell(r, c - 1);
				if (dijkstra.isSelected()) {
					if (makeConnected) {
						temp.add(cell);
					} else {
						int graphIndex = isInList(graph, cell);
						if (graphIndex > -1) {
							temp.add(graph.get(graphIndex));
						}
					}
				} else {
					// ... update the pointer of the left-side cell so it points
					// the current one ...
					cell.prev = current;
					// ... and add the left-side cell to the successors of the
					// current one.
					temp.add(cell);
				}
			}
			if (diagonal.isSelected()) {
				// If we are not even at the topmost nor at the leftmost border
				// of the grid
				// and the up-left-side cell is not an obstacle ...
				if (r > 0 && c > 0 && grid[r - 1][c - 1] != OBST &&
						// ... and one of the up-side or left-side cells are not
						// obstacles ...
//				(grid[r - 1][c] != OBST || grid[r][c - 1] != OBST) &&
						// ... and (only in the case are not running the A* or
						// Greedy)
						// not already belongs neither to the OPEN SET nor to
						// the CLOSED SET ...
				((aStar.isSelected() || guloso.isSelected() || dijkstra.isSelected()) ? true
						: isInList(openSet, new Cell(r - 1, c - 1)) == -1
								&& isInList(closedSet, new Cell(r - 1, c - 1)) == -1)) {
					Cell cell = new Cell(r - 1, c - 1);
					if (dijkstra.isSelected()) {
						if (makeConnected) {
							temp.add(cell);
						} else {
							int graphIndex = isInList(graph, cell);
							if (graphIndex > -1) {
								temp.add(graph.get(graphIndex));
							}
						}
					} else {
						// ... update the pointer of the up-left-side cell so it
						// points the current one ...
						cell.prev = current;
						// ... and add the up-left-side cell to the successors
						// of the current one.
						temp.add(cell);
					}
				}
			}
			// When DFS algorithm is in use, cells are added one by one at the
			// beginning of the
			// OPEN SET list. Because of this, we must reverse the order of
			// successors formed,
			// so the successor corresponding to the highest priority, to be
			// placed
			// the first in the list.
			// For the Greedy, A* and Dijkstra's no issue, because the list is
			// sorted
			// according to 'f' or 'dist' before extracting the first element
			// of.
			if (dfs.isSelected()) {
				Collections.reverse(temp);
			}
			return temp;
		} // end createSuccesors()

		/**
		 * Returns the index of the cell 'current' in the list 'list'
		 *
		 * @param list
		 *            the list in which we seek
		 * @param current
		 *            the cell we are looking for
		 * @return the index of the cell in the list if the cell is not found
		 *         returns -1
		 */
		private int isInList(ArrayList<Cell> list, Cell current) {
			int index = -1;
			for (int i = 0; i < list.size(); i++) {
				if (current.row == list.get(i).row && current.col == list.get(i).col) {
					index = i;
					break;
				}
			}
			return index;
		} // end isInList()

		/**
		 * Returns the predecessor of cell 'current' in list 'list'
		 *
		 * @param list
		 *            the list in which we seek
		 * @param current
		 *            the cell we are looking for
		 * @return the predecessor of cell 'current'
		 */
		private Cell findPrev(ArrayList<Cell> list, Cell current) {
			int index = isInList(list, current);
			return list.get(index).prev;
		} // end findPrev()

		/**
		 * Returns the distance between two cells
		 *
		 * @param u
		 *            the first cell
		 * @param v
		 *            the other cell
		 * @return the distance between the cells u and v
		 */
		private int distBetween(Cell u, Cell v) {
			int dist;
			int dx = u.col - v.col;
			int dy = u.row - v.row;
			if (diagonal.isSelected()) {
				// with diagonal movements
				// calculate 1000 times the Euclidean distance
				dist = (int) ((double) 1000 * Math.sqrt(dx * dx + dy * dy));
			} else {
				// without diagonal movements
				// calculate Manhattan distances
				dist = Math.abs(dx) + Math.abs(dy);
			}
			return dist;
		} // end distBetween()

		/**
		 *          * Calculates the path from the target to the initial
		 * position          * of the robot, counts the corresponding steps
		 *          * and measures the distance traveled.         
		 */
		private void plotRoute() {
			searching = false;
			endOfSearch = true;
			int steps = 0;
			double distance = 0;
			int index = isInList(closedSet, targetPos);
			Cell cur = closedSet.get(index);
			grid[cur.row][cur.col] = TARGET;
			do {
				steps++;
				if (diagonal.isSelected()) {
					int dx = cur.col - cur.prev.col;
					int dy = cur.row - cur.prev.row;
					distance += Math.sqrt(dx * dx + dy * dy);
				} else {
					distance++;
				}
				cur = cur.prev;
				grid[cur.row][cur.col] = ROUTE;
			} while (!(cur.row == robotStart.row && cur.col == robotStart.col));
			grid[robotStart.row][robotStart.col] = ROBOT;
			String msg;
			msg = String.format("Nós expandidos: %d, Passos: %d, Distância: %.3f", expanded, steps, distance);
			message.setText(msg);

		} // end plotRoute()

		/**
		 *         * Gives initial values ​​for the cells in the grid.         
		 * * With the first click on button 'Clear' clears the data          *
		 * of any search was performed (Frontier, Closed Set, Route)          *
		 * and leaves intact the obstacles and the robot and target positions
		 *          * in order to be able to run another algorithm          *
		 * with the same data.          * With the second click removes any
		 * obstacles also.         
		 */
		private void fillGrid() {
			if (searching || endOfSearch) {
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						if (grid[r][c] == FRONTIER || grid[r][c] == CLOSED || grid[r][c] == ROUTE) {
							grid[r][c] = EMPTY;
						}
						if (grid[r][c] == ROBOT) {
							robotStart = new Cell(r, c);
						}
						if (grid[r][c] == TARGET) {
							targetPos = new Cell(r, c);
						}
					}
				}
				searching = false;
			} else {
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						grid[r][c] = EMPTY;
					}
				}
				robotStart = new Cell(rows - 2, 1);
				targetPos = new Cell(1, columns - 2);
			}
			if (aStar.isSelected() || guloso.isSelected()) {
				robotStart.g = 0;
				robotStart.h = 0;
				robotStart.f = 0;
			}
			expanded = 0;
			found = false;
			searching = false;
			endOfSearch = false;

			// The first step of the other four algorithms is here
			// 1. OPEN SET: = [So], CLOSED SET: = []
			openSet.removeAll(openSet);
			openSet.add(robotStart);
			closedSet.removeAll(closedSet);

			grid[targetPos.row][targetPos.col] = TARGET;
			grid[robotStart.row][robotStart.col] = ROBOT;
			message.setText(msgDrawAndSelect);
			timer.stop();
			repaint();

		} // end fillGrid()

		/**
		 *           * Appends to the list containing the nodes of the graph
		 * only           * the cells belonging to the same connected component
		 * with node v.           * This is a Breadth First Search of the graph
		 * starting from node v.           *           * @param v the starting
		 * node          
		 */
		private void findConnectedComponent(Cell v) {
			Stack<Cell> stack;
			stack = new Stack();
			ArrayList<Cell> succesors;
			stack.push(v);
			graph.add(v);
			while (!stack.isEmpty()) {
				v = stack.pop();
				succesors = createSuccesors(v, true);
				for (Cell c : succesors) {
					if (isInList(graph, c) == -1) {
						stack.push(c);
						graph.add(c);
					}
				}
			}
		} // end findConnectedComponent()

		/**
		 * Initialization of Dijkstra's algorithm    When one thinks of
		 * Wikipedia pseudocode, observe that the algorithm is still looking for
		 * his target while there are still nodes in the queue Q.  Only when we
		 * run out of queue and the target has not been found, can answer that
		 * there is no solution .  As is known, the algorithm models the problem
		 * as a connected graph. It is obvious that no solution exists only when
		 * the graph is not connected and the target is in a different connected
		 * component of this initial position of the robot.  To be thus possible
		 * negative response from the algorithm, should search be made ONLY in
		 * the coherent component to which the initial position of the robot
		 * belongs.
		 */
		private void initializeDijkstra() {
			// First create the connected component
			// to which the initial position of the robot belongs.
			graph.removeAll(graph);
			findConnectedComponent(robotStart);
			// Here is the initialization of Dijkstra's algorithm
			// 2: for each vertex v in Graph;
			for (Cell v : graph) {
				// 3: dist[v] := infinity ;
				v.dist = INFINITY;
				// 5: previous[v] := undefined ;
				v.prev = null;
			}
			// 8: dist[source] := 0;
			graph.get(isInList(graph, robotStart)).dist = 0;
			// 9: Q := the set of all nodes in Graph;
			// Instead of the variable Q we will use the list
			// 'graph' itself, which has already been initialised.

			// Sorts the list of nodes with respect to 'dist'.
			Collections.sort(graph, new CellComparatorByDist());
			// Initializes the list of closed nodes
			closedSet.removeAll(closedSet);
		} // end initializeDijkstra()

		/**
		 * paints the grid
		 */
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g); // Fills the background color.

			g.setColor(Color.DARK_GRAY);
			g.fillRect(10, 10, columns * squareSize + 1, rows * squareSize + 1);

			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					if (grid[r][c] == EMPTY) {
						g.setColor(Color.WHITE);
					} else if (grid[r][c] == ROBOT) {
						g.setColor(Color.RED);
					} else if (grid[r][c] == TARGET) {
						g.setColor(Color.GREEN);
					} else if (grid[r][c] == OBST) {
						g.setColor(Color.BLACK);
					} else if (grid[r][c] == FRONTIER) {
						g.setColor(Color.BLUE);
					} else if (grid[r][c] == CLOSED) {
						g.setColor(Color.CYAN);
					} else if (grid[r][c] == ROUTE) {
						g.setColor(Color.YELLOW);
					}
					g.fillRect(11 + c * squareSize, 11 + r * squareSize, squareSize - 1, squareSize - 1);
				}
			}

			if (drawArrows.isSelected()) {
				// We draw all arrows from each open or closed state
				// to its predecessor.
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < columns; c++) {
						// If the current cell is the goal and the solution has
						// been found,
						// or belongs in the route to the target,
						// or is an open state,
						// or is a closed state but not the initial position of
						// the robot
						if ((grid[r][c] == TARGET && found) || grid[r][c] == ROUTE || grid[r][c] == FRONTIER
								|| (grid[r][c] == CLOSED && !(r == robotStart.row && c == robotStart.col))) {
							// The tail of the arrow is the current cell, while
							// the arrowhead is the predecessor cell.
							Cell head;
							if (grid[r][c] == FRONTIER) {
								if (dijkstra.isSelected()) {
									head = findPrev(graph, new Cell(r, c));
								} else {
									head = findPrev(openSet, new Cell(r, c));
								}
							} else {
								head = findPrev(closedSet, new Cell(r, c));
							}
							// The coordinates of the center of the current cell
							int tailX = 11 + c * squareSize + squareSize / 2;
							int tailY = 11 + r * squareSize + squareSize / 2;
							// The coordinates of the center of the predecessor
							// cell
							int headX = 11 + head.col * squareSize + squareSize / 2;
							int headY = 11 + head.row * squareSize + squareSize / 2;
							// If the current cell is the target
							// or belongs to the path to the target ...
							if (grid[r][c] == TARGET || grid[r][c] == ROUTE) {
								// ... draw a red arrow directing to the target.
								g.setColor(Color.RED);
								drawArrow(g, tailX, tailY, headX, headY);
								// Else ...
							} else {
								// ... draw a black arrow to the predecessor
								// cell.
								g.setColor(Color.BLACK);
								drawArrow(g, headX, headY, tailX, tailY);
							}
						}
					}
				}
			}
		} // end paintComponent()

		/**
		 * Draws an arrow from point (x2,y2) to point (x1,y1)
		 */
		private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
			Graphics2D g = (Graphics2D) g1.create();

			double dx = x2 - x1, dy = y2 - y1;
			double angle = Math.atan2(dy, dx);
			int len = (int) Math.sqrt(dx * dx + dy * dy);
			AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
			at.concatenate(AffineTransform.getRotateInstance(angle));
			g.transform(at);

			// We design an horizontal arrow 'len' in length
			// that ends at the point (0,0) with two tips 'arrowSize' in length
			// which form 20 degrees angles with the axis of the arrow ...
			g.drawLine(0, 0, len, 0);
			g.drawLine(0, 0, (int) (arrowSize * Math.sin(70 * Math.PI / 180)),
					(int) (arrowSize * Math.cos(70 * Math.PI / 180)));
			g.drawLine(0, 0, (int) (arrowSize * Math.sin(70 * Math.PI / 180)),
					-(int) (arrowSize * Math.cos(70 * Math.PI / 180)));
			// ... and class AffineTransform handles the rest !!!!!!
			// Java is admirable!!! Isn't it ?
		} // end drawArrow()

	} // end nested classs MazePanel

} // end class Maze