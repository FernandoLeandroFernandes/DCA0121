package ufrn.ia.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

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

public class Pathfinder {

	public static JFrame graphFrame;

	// Classe principal
	public static void main(String[] args) {
		int width = 693;
		int height = 545;
		graphFrame = new JFrame("Algoritmos I.A. - Busca com Informação");
		graphFrame.setContentPane(new GraphPanel(width, height));
		graphFrame.pack();
		graphFrame.setResizable(false);

		// pra ficar localizado no centro da tela
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double ScreenHeight = screenSize.getHeight();
		int x = ((int) screenWidth - width) / 2;
		int y = ((int) ScreenHeight - height) / 2;

		graphFrame.setLocation(x, y);
		graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		graphFrame.setVisible(true);
	} // end main()

	public static class GraphPanel extends JPanel {

		// Classes dentro de MazePanel

		// Classe base do nó
		private class Cell {
			int row; // o numero de linhas(5 a 50)
			int col; // o numero de colunas (5 a 50)
			int g; // valor da função g para o A* e a busca gulosa
			int h; // valor da função h para o A* e a busca gulosa
			int f; // valor da função f para o A* e a busca gulosa
			int dist;// a distância da célula a partir da posição inicial do nó
						// atual

			Cell prev;

			// Cada estado corresponde a um nó e cada
			// estado tem um antecessor, que
			// é armazenado nesta variável

			public Cell(int row, int col) {
				this.row = row;
				this.col = col;
			}
		} // end classe cell

		// Classe auxiliar que especifica que os nós
		// serão classificados de acordo com seu campo 'f'
		private class CellComparatorByF implements Comparator<Cell> {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return cell1.f - cell2.f;
			}
		} // end class CellComparatorByF

		// A classe auxiliar que especifica que os nós
		// serão classificados de acordo com seu campo 'dist'
		private class CellComparatorByDist implements Comparator<Cell> {
			@Override
			public int compare(Cell cell1, Cell cell2) {
				return cell1.dist - cell2.dist;
			}
		} // end class CellComparatorByDist

		// Classe que controla os movimentos do mouse como nós a "pintar",
		// Obstáculos ou mover o nó atual e/ou de destino.
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

		// Quando o usuário pressiona um botão executa a funcionalidade
		// correspondente
		private class ActionHandler implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent evt) {

				String cmd = evt.getActionCommand();

				if (cmd.equals("Limpar")) {
					fillGrid();
					aStar.setEnabled(true);
					guloso.setEnabled(true);
					diagonal.setEnabled(true);

				} else if (cmd.equals("Step-by-Step") && !found && !endOfSearch) {

					searching = true;
					message.setText(msgSelectStepByStepEtc);
					aStar.setEnabled(false);
					guloso.setEnabled(false);
					diagonal.setEnabled(false);
					timer.stop();

					// Aqui nós decidimos se podemos continuar
					// a procura no modo "Passo-a-Passo" ou não.

					// No caso de A* e algoritmo guloso, temos a segunda etapa:
					// 2. Se conjunto aberto = [], em seguida, encerrar. Não há
					// solução.

					if ((graph.isEmpty()) || (openSet.isEmpty())) {
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

					searching = true;
					message.setText(msgSelectStepByStepEtc);
					aStar.setEnabled(false);
					guloso.setEnabled(false);
					diagonal.setEnabled(false);
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
				// No caso de A* e algoritmo guloso, temos a segunda etapa:
				// 2. Se conjunto aberto = [], em seguida, encerrar. Não há uma
				// solução.

				if (openSet.isEmpty()) {
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

		private class RandomMaze {
			private int dimensionX, dimensionY; // dimensão do labirinto
			private int gridDimensionX, gridDimensionY; // dimensão da saida do
														// grid

			private char[][] mazeGrid; // saida do grid
			private Cell[][] cells; // 2d array para células
			private Random random = new Random(); // Objeto random

			public RandomMaze(int aDimension) {
				this(aDimension, aDimension);
			}

			// constructor
			public RandomMaze(int xDimension, int yDimension) {
				dimensionX = xDimension;
				dimensionY = yDimension;
				gridDimensionX = xDimension * 2 + 1;
				gridDimensionY = yDimension * 2 + 1;
				mazeGrid = new char[gridDimensionX][gridDimensionY];
				init();
				generateMaze();
			}

			private void init() {
				// criando nó
				cells = new Cell[dimensionX][dimensionY];
				for (int x = 0; x < dimensionX; x++) {
					for (int y = 0; y < dimensionY; y++) {
						cells[x][y] = new Cell(x, y, false);
					}
				}
			}

			// classe interna representando a nó
			private class Cell {
				int x, y; // coordenadas

				ArrayList<Cell> neighbors = new ArrayList<>();
				// nó intransponível
				boolean wall = true;
				// Se true, ainda tem de ser usado na geração
				boolean open = true;

				// contrutor nó (x,y)
				Cell(int x, int y) {
					this(x, y, true);
				}

				// construir nó em x,y se de fato é isWall
				Cell(int x, int y, boolean isWall) {
					this.x = x;
					this.y = y;
					this.wall = isWall;
				}

				// Adiciona um vizinho para esse nó, e este nó como um
				// vizinho do outro
				void addNeighbor(Cell other) {
					if (!this.neighbors.contains(other)) { // evitar duplicatas
						this.neighbors.add(other);
					}
					if (!other.neighbors.contains(this)) { // evitar duplicatas
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
					// Método hashcode aleatório projetado para ser geralmente
					// único
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
				startAt.open = false; // Indica nó fechado para a geração
				ArrayList<Cell> cellsList = new ArrayList<>();
				cellsList.add(startAt);

				while (!cellsList.isEmpty()) {
					Cell cell;

					// Isso é reduzir, mas não eliminar completamente o número
					// De caminhos "enrolados" longos, mais fácil de detectar
					// ramos que resulta em labirintos fáceis
					if (random.nextInt(10) == 0)
						cell = cellsList.remove(random.nextInt(cellsList.size()));
					else
						cell = cellsList.remove(cellsList.size() - 1);
					ArrayList<Cell> neighbors = new ArrayList<>();
					// nós que poderiam ser vizinhos
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
					// gerar um nó random
					Cell selected = neighbors.get(random.nextInt(neighbors.size()));

					selected.open = false; // indica nó fechado para geração
					cell.addNeighbor(selected);
					cellsList.add(cell);
					cellsList.add(selected);
				}
				updateGrid();
			}

			// Usado para obter um nó em x,y;
			// Retorna nulo fora dos limites
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

				// Criamos um GRID limpo ...
				searching = false;
				endOfSearch = false;
				fillGrid();
				// ... e copiar nele as posições dos obstáculos
				// criado pelo algoritmo de construção labirinto
				for (int x = 0; x < gridDimensionX; x++) {
					for (int y = 0; y < gridDimensionY; y++) {
						if (mazeGrid[x][y] == wallChar && grid[x][y] != ROBOT && grid[x][y] != TARGET) {
							grid[x][y] = OBST;
						}
					}
				}
			}
		} // end class RandomMaze

		// constantes da classe GraphPanel
		public final static int INFINITY = Integer.MAX_VALUE, EMPTY = 0, // nó
																			// vazio
				OBST = 1, // nó com obstaculo
				ROBOT = 2, // posição do nó atual
				TARGET = 3, // posição do destino
				FRONTIER = 4, // nó da fronteira
				CLOSED = 5, // nó do caminho fechado
				ROUTE = 6; // nó que formam o caminho origem-destino

		// mensagens ao usuário
		private final static String msgDrawAndSelect = "\"Pinte\" os obstáculos, depois click em 'Passo-a-Passo' ou em 'Iniciar'",
				msgSelectStepByStepEtc = "Click 'Passo-a-Passo' em 'Iniciar' ou em 'Limpar'",
				msgNoSolution = "Não há caminho para o destino selecionado!";

		// cores
		private static final Color ROBOT_COLOR = Color.GREEN;

		private static final Color TARGET_COLOR = Color.RED;

		private static final Color OBST_COLOR = Color.BLACK;

		private static final Color EMPTY_COLOR = Color.WHITE;

		private static final Color CLOSED_COLOR = Color.YELLOW;

		private static final Color ROUTE_COLOR = Color.BLUE;

		private static final Color FRONTIER_COLOR = Color.ORANGE;

		// variáveis da class GraphPanel
		JTextField rowsField, columnsField;

		int rows = 20; // linhas do GRID (default)
		int columns = 20; // colunas do GRID (default)
		int squareSize = 500 / rows; // tamanho dos nós em pixels

		// Referencia para o nó predecessor
		ArrayList<Cell> openSet = new ArrayList(); // lista aberta
		ArrayList<Cell> closedSet = new ArrayList();// lista fechada
		ArrayList<Cell> graph = new ArrayList();

		Cell robotStart; // posição inicial do nó atual
		Cell targetPos; // posição do destino

		JLabel message; // mensagem ao usuário

		// botoes de seleção de algoritmo
		JRadioButton aStar, guloso;

		// slider de velocidade de animação
		JSlider slider;

		// movimentos na diagonal
		JCheckBox diagonal;

		// desenha seta para o predecessor
		// JCheckBox drawArrows;

		int[][] grid; // the grid
		boolean found; // flag no encrontrado
		boolean searching; // flag pesquisa em progresso
		boolean endOfSearch;// flag busca chegou ao fim
		int delay; // tempo de delay da animação (in msec)
		int expanded; // numero de nos expandidos

		// objeto de controle da animação
		RepaintAction repainter = new RepaintAction();

		// o temporizador que regula a velocidade de execução da animação
		Timer timer;

		/**
		 * Classe MazePanel
		 * 
		 * @param width
		 *            a largura do panel.
		 * @param height
		 *            a altura do panel.
		 */
		public GraphPanel(int width, int height) {

			setLayout(null);

			MouseHandler listener = new MouseHandler();
			addMouseListener(listener);
			addMouseMotionListener(listener);

			setPreferredSize(new Dimension(width, height));

			grid = new int[rows][columns];

			// criando o conteudo do panel

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

			slider = new JSlider(0, 1000, 500); // valor inicial do slider em
												// 500 msec
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

			// ButtonGroup que agrupa os botões de rádio
			// escolha do algoritmo, de modo que apenas uma
			// pode ser selecionada a qualquer momento

			ButtonGroup algoGroup = new ButtonGroup();

			aStar = new JRadioButton("A*");
			aStar.setToolTipText("Algoritmo A Estrela ");
			aStar.setSelected(true);
			algoGroup.add(aStar);
			aStar.addActionListener(new ActionHandler());

			guloso = new JRadioButton("Guloso");
			guloso.setToolTipText("Algoritmo de Busca Gulosa");
			algoGroup.add(guloso);
			guloso.addActionListener(new ActionHandler());

			JPanel algoPanel = new JPanel();
			algoPanel.setBorder(
					javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),
							"Algoritmos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.TOP, new java.awt.Font("Helvetica", 0, 14)));

			aStar.setSelected(true); // A* selecionado como inicial

			diagonal = new JCheckBox("Movimentos Diagonais");
			diagonal.setToolTipText("Movimentos diagonais também estão autorizados");
			diagonal.setSelected(true);

			JLabel robot = new JLabel("INICIO", JLabel.LEFT);
			robot.setForeground(ROBOT_COLOR);
			robot.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel target = new JLabel("ALVO", JLabel.LEFT);
			target.setForeground(TARGET_COLOR);
			target.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel frontier = new JLabel("FRONTEIRA", JLabel.LEFT);
			frontier.setForeground(FRONTIER_COLOR);
			frontier.setFont(new Font("Helvetica", Font.BOLD, 14));

			JLabel closed = new JLabel("FECHADOS", JLabel.LEFT);
			closed.setForeground(CLOSED_COLOR);
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

			// adicionando conteudo ao Panel
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
			add(aStar);
			add(guloso);
			add(algoPanel);
			add(diagonal);
			add(robot);
			add(target);
			add(frontier);
			add(closed);
			add(aboutButton);

			// regulando posição dos componentes
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
			aStar.setBounds(530, 295, 70, 25);
			guloso.setBounds(600, 295, 85, 25);
			algoPanel.setLocation(520, 250);
			algoPanel.setSize(170, 100);
			diagonal.setBounds(520, 355, 170, 25);
			robot.setBounds(530, 380, 80, 25);
			target.setBounds(530, 400, 80, 25);
			frontier.setBounds(530, 420, 80, 25);
			closed.setBounds(530, 440, 80, 25);
			aboutButton.setBounds(520, 515, 170, 25);

			// criando timer
			timer = new Timer(delay, repainter);

			// nós ligamos às células nos valores iniciais de grade.
			// Aqui é os primeiros passos dos algoritmos
			fillGrid();

		} // end constructor

		private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
			initializeGrid(false);
		} // end resetButtonActionPerformed()

		private void mazeButtonActionPerformed(java.awt.event.ActionEvent evt) {
			initializeGrid(true);
		} // end mazeButtonActionPerformed()

		/**
		 * Cria um grid novo ou um novo labirinto
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

			// o labirinto deve ter um número ímpar as linhas e colunas
			if (makeMaze && rows % 2 == 0) {
				rows -= 1;
			}
			if (makeMaze && columns % 2 == 0) {
				columns -= 1;
			}
			grid = new int[rows][columns];
			robotStart = new Cell(rows - 2, 1);
			targetPos = new Cell(1, columns - 2);
			aStar.setEnabled(true);
			guloso.setEnabled(true);
			diagonal.setSelected(false);
			diagonal.setEnabled(true);

			slider.setValue(500);
			if (makeMaze) {
				RandomMaze maze = new RandomMaze(rows / 2, columns / 2);
			} else {
				fillGrid();
			}
		} // end

		private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {
			AboutBox aboutBox = new AboutBox(graphFrame, true);
			aboutBox.setVisible(true);
		} // end aboutButtonActionPerformed()

		/**
		 * Expande um nó e cria seus sucessores
		 */
		private void expandNode() {

			Cell current;

			Collections.sort(openSet, new CellComparatorByF());
			current = openSet.remove(0);

			// ... adiciona-o ao CONJUNTO FECHADO.
			closedSet.add(0, current);

			// atualiza a cor do nó
			grid[current.row][current.col] = CLOSED;

			// Se o nó selecionado eh o alvo ...
			if (current.row == targetPos.row && current.col == targetPos.col) {
				// ... entao finaliza
				Cell last = targetPos;
				last.prev = current.prev;
				closedSet.add(last);
				found = true;
				return;
			}

			// conta os nós que foram expandidos.
			expanded++;

			// cria o sucessor de Si, baseado nas acoes
			// que podem ser implementadas em Si.
			// Cada sucessor tem um ponteiro para o Si, como seu predecessor.
			ArrayList<Cell> succesors;
			succesors = createSuccesors(current, false);

			// Para cada sucessor de Si, ...
			for (Cell cell : succesors) {

				// ... Calcula o valor de f(Sj) dos algoritmos ...
				int dxg = current.col - cell.col;
				int dyg = current.row - cell.row;
				int dxh = targetPos.col - cell.col;
				int dyh = targetPos.row - cell.row;

				if (diagonal.isSelected()) {

					// com movimentos diagonais
					// calcula 1000 vezes a distancia Euclideana
					if (guloso.isSelected()) {
						// especialmente para a Busca Gulosa ...
						cell.g = 0;
					} else {
						cell.g = current.g + (int) ((double) 1000 * Math.sqrt(dxg * dxg + dyg * dyg));
					}
					cell.h = (int) ((double) 1000 * Math.sqrt(dxh * dxh + dyh * dyh));

				} else {

					// sem movimentos diagonais
					// calcula distancias Manhattan
					if (guloso.isSelected()) {
						// especialmente para a Busca Gulosa ...
						cell.g = 0;
					} else {
						cell.g = current.g + Math.abs(dxg) + Math.abs(dyg);
					}
					cell.h = Math.abs(dxh) + Math.abs(dyh);
				}
				cell.f = cell.g + cell.h;

				// ... Se Sj nao estiver nem no CONJUNTO FECHADO nem no CONJUNTO
				// ABERTO ...
				int openIndex = isInList(openSet, cell);
				int closedIndex = isInList(closedSet, cell);

				if (openIndex == -1 && closedIndex == -1) {

					// ... adiciona Sj no CONJUNTO FECHADO ...
					// ... avaliado como f(Sj)
					openSet.add(cell);

					// Atualiza a cor da celula
					grid[cell.row][cell.col] = FRONTIER;

				} else {

					// ... se ja pertence ao CONJUNTO ABERTO ...
					if (openIndex > -1) {
						// ... compara o novo valor do recurso com o anterior
						// Se anterior <= novo ...
						if (openSet.get(openIndex).f > cell.f) {
							// ... remove o elemento (Sj, anterior) da lista
							// a que ele pertence ...
							openSet.remove(openIndex);
							// ... e adiciona o item (Sj, novo) ao CONJUNTO
							// ABERTO.
							openSet.add(cell);
							// Atualiza a cor do nó
							grid[cell.row][cell.col] = FRONTIER;
						}

						// ... se ja pertence ao CONJUNTO FECHADO...
					} else {

						// ... compara o novo valor do recurso com o anterior
						// Se anterior > novo ...
						if (closedSet.get(closedIndex).f > cell.f) {
							// ... remove o elemento (Sj, anterior) da lista
							// a que ele pertence ...
							closedSet.remove(closedIndex);
							// ... e adiciona o item (Sj, novo) ao CONJUNTO
							// ABERTO.
							openSet.add(cell);
							// Atualiza a cor do nó
							grid[cell.row][cell.col] = FRONTIER;
						}
					}
				}
			}
		} // end expandNode()

		/**
		 * Cria o sucessor nó/estado
		 * 
		 * @param current
		 *            a nó para a qual buscamos sucessores
		 * @param makeConnected
		 *            flag que indica que estamos interessados apenas nas
		 *            coordenadas dos nós
		 * @return lista com os sucessores do nó
		 */
		private ArrayList<Cell> createSuccesors(Cell current, boolean makeConnected) {

			int r = current.row;
			int c = current.col;

			// Criamos uma lista vazia para os sucessores do nó corrente.
			ArrayList<Cell> temp = new ArrayList<>();

			// com movimentos diagonais a prioridade eh:
			// 1: Up 2: Up-right 3: Right 4: Down-right
			// 5: Down 6: Down-left 7: Left 8: Up-left

			// sem movimentos diagonais a prioridade eh:
			// 1: Up 2: Right 3: Down 4: Left

			// Se nao estamos no limite superior do grid
			// e o nó criado nao eh um obstaculo ...
			if (r > 0 && grid[r - 1][c] != OBST && ((aStar.isSelected() || guloso.isSelected()) ? true
					: isInList(openSet, new Cell(r - 1, c)) == -1 && isInList(closedSet, new Cell(r - 1, c)) == -1)) {
				Cell cell = new Cell(r - 1, c);

				// ... Atualizar o ponteiro do nó de cabeça para que ele
				// aponte para o atual ...
				cell.prev = current;

				// ... e adicione o nó de cabeça aos sucessores do atual.
				temp.add(cell);
			}

			if (diagonal.isSelected()) {

				// Se estamos não estamos no nível superior nem na fronteira
				// mais à direita Do grid e o nó do lado-superior-direita não é
				// um
				// obstáculo ...
				if (r > 0 && c < columns - 1 && grid[r - 1][c + 1] != OBST &&

						((aStar.isSelected() || guloso.isSelected()) ? true
								: isInList(openSet, new Cell(r - 1, c + 1)) == -1
										&& isInList(closedSet, new Cell(r - 1, c + 1)) == -1)) {
					Cell cell = new Cell(r - 1, c + 1);

					// ... Atualizar o ponteiro de nó-lateral-direito para
					// Aponta o atual ...
					cell.prev = current;
					// ... E adicione a nó-superior-direito aos sucessores
					// do atual.
					temp.add(cell);
				}
			}
			// Se não for no limite mais à direita de grade
			// Este nó do lado-direito não é um obstáculo ...
			if (c < columns - 1 && grid[r][c + 1] != OBST &&

					((aStar.isSelected() || guloso.isSelected()) ? true
							: isInList(openSet, new Cell(r, c + 1)) == -1
									&& isInList(closedSet, new Cell(r, c + 1)) == -1)) {
				Cell cell = new Cell(r, c + 1);

				// ... Atualizar o ponteiro faz a célula do lado-direito para
				// que ele aponte para o atual ...
				cell.prev = current;
				// ... E adicione a célula do lado-direito aos sucessores do
				// Atual.
				temp.add(cell);
			}
			if (diagonal.isSelected()) {
				// Se não estamos mesmo no mais inferior nem na mais à direita
				// Fronteira do GRID o nó do lado-direita-baixo não é um
				// obstáculo ...
				if (r < rows - 1 && c < columns - 1 && grid[r + 1][c + 1] != OBST &&

						((aStar.isSelected() || guloso.isSelected()) ? true
								: isInList(openSet, new Cell(r + 1, c + 1)) == -1
										&& isInList(closedSet, new Cell(r + 1, c + 1)) == -1)) {
					Cell cell = new Cell(r + 1, c + 1);

					// ... Atualizar o ponteiro do nó lado-direita-baixo,
					// assim ele aponta o atual ...
					cell.prev = current;
					// ... E adicione o nó lado-direita-baixo até o
					// Sucessores do atual.
					temp.add(cell);
				}
			}
			// Se não for no limite baixo de grade
			// E a célula-lado baixo não é um obstáculo ...
			if (r < rows - 1 && grid[r + 1][c] != OBST &&

					((aStar.isSelected() || guloso.isSelected()) ? true
							: isInList(openSet, new Cell(r + 1, c)) == -1
									&& isInList(closedSet, new Cell(r + 1, c)) == -1)) {
				Cell cell = new Cell(r + 1, c);

				// ... Atualizar o ponteiro de célula-lado para baixo para que
				// ele aponta
				// O atual ...
				cell.prev = current;
				// ... E adicione a célula lateral para baixo para os sucessores
				// de o Atual.
				temp.add(cell);
			}

			if (diagonal.isSelected()) {

				// Se não estamos mesmo no mais baixo, nem no mais à esquerda
				// Fronteira do grid e a célula-esquerda voltada para baixo não
				// é um obstáculo ...
				if (r < rows - 1 && c > 0 && grid[r + 1][c - 1] != OBST &&

						((aStar.isSelected() || guloso.isSelected()) ? true
								: isInList(openSet, new Cell(r + 1, c - 1)) == -1
										&& isInList(closedSet, new Cell(r + 1, c - 1)) == -1)) {
					Cell cell = new Cell(r + 1, c - 1);

					// ... Atualizar o ponteiro da célula-left-side para baixo
					// de modo
					// aponta o atual ...
					cell.prev = current;
					// ... E adicione a célula lado esquerdo para baixo para os
					// sucessores
					// De o atual.
					temp.add(cell);
				}
			}

			// Se não for no limite mais à esquerda de grade
			// E o nó do lado esquerdo não é um obstáculo ...
			if (c > 0 && grid[r][c - 1] != OBST &&

					((aStar.isSelected() || guloso.isSelected()) ? true
							: isInList(openSet, new Cell(r, c - 1)) == -1
									&& isInList(closedSet, new Cell(r, c - 1)) == -1)) {
				Cell cell = new Cell(r, c - 1);

				// ... Atualizar o ponteiro de nó do lado esquerdo para que
				// ele aponta
				// O atual ...
				cell.prev = current;
				// ... E adicione a nó do lado esquerdo para os sucessores
				// de o atual

				temp.add(cell);
			}

			if (diagonal.isSelected()) {

				// Se estamos nem mesmo no nível superior nem na fronteira mais
				// à esquerda
				// Do grid E a nó-esquerda-superior não é um obstáculo ...
				if (r > 0 && c > 0 && grid[r - 1][c - 1] != OBST &&

						((aStar.isSelected() || guloso.isSelected()) ? true
								: isInList(openSet, new Cell(r - 1, c - 1)) == -1
										&& isInList(closedSet, new Cell(r - 1, c - 1)) == -1)) {
					Cell cell = new Cell(r - 1, c - 1);

					// ... Atualizar o ponteiro de nó lateral se por isso
					// pontos que o atual ...
					cell.prev = current;
					// ... E adicione o nó lateral superior esquerda para os
					// sucessores
					// Do atual.
					temp.add(cell);
				}
			}

			return temp;
		} // end createSuccesors()

		/**
		 * Returna o indice do nó 'current' na lista 'list'
		 *
		 * @param list
		 *            a lista na qual procuramos
		 * @param current
		 *            o nó que estamos procurando
		 * @return o indice do nó na lista se o nó nao for encontrado returns -1
		 */
		private int isInList(ArrayList<Cell> list, Cell current) {

			for (int i = 0; i < list.size(); i++) {
				if (current.row == list.get(i).row && current.col == list.get(i).col) {
					return i;
				}
			}
			return -1;
		} // end isInList()

		/**
		 * Retorna o predecessor de célula 'atual' na lista de 'list'
		 *
		 * @param list
		 *            a lista na qual procuramos
		 * @param current
		 *            a célula que estamos procurando
		 * @return o antecessor de célula 'atual'
		 */
		private Cell findPrev(ArrayList<Cell> list, Cell current) {
			int index = isInList(list, current);
			return list.get(index).prev;
		} // end findPrev()

		/**
		 * Retorna a distância entre duas células
		 *
		 * @param u
		 *            a primeira celula
		 * @param v
		 *            a outra celula
		 * @return retorna a distancia entre as duas
		 */
		private int distBetween(Cell u, Cell v) {
			int dist;
			int dx = u.col - v.col;
			int dy = u.row - v.row;
			if (diagonal.isSelected()) {
				// com movimentos diagonais
				// calcula 1000 vezes a distancia euclidiana
				dist = (int) ((double) 1000 * Math.sqrt(dx * dx + dy * dy));
			} else {
				// sem mvimentos diagonais
				// calcula a distancia Manhattan
				dist = Math.abs(dx) + Math.abs(dy);
			}
			return dist;
		} // end distBetween()

		/**
		 * Calcula o caminho do alvo a partir da posição inicial do robo
		 * contando os passos correspondentes e medidndo as distancias
		 * percorridas   
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
			msg = String.format("Nós expandidos: %d, Passos: %d, Distância: %.2f", expanded, steps, distance);
			message.setText(msg);

		} // end plotRoute()

		/**
		 * Primeiro click de CLEAR ele remove as rotas traçadas o segundo click
		 * ele limpa os obstáculos no caminho     
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

			// Primeira etapa dos algoritmos é aqui
			// 1. CONJUNTO ABERTO: = [So], CONJUNTO FECHADO: = []
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
		 * Adicionar lista contendo os nós do grafico apenas as celulas
		 * pertencentes ao memso componente conectado com o nó v.         
		 * 
		 * @param v
		 *            é a celula inicial     
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
		 * pinta o grid
		 */
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g); // background cor

			g.setColor(Color.DARK_GRAY);
			g.fillRect(10, 10, columns * squareSize + 1, rows * squareSize + 1);

			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					if (grid[r][c] == EMPTY) {
						g.setColor(EMPTY_COLOR);
					} else if (grid[r][c] == ROBOT) {
						g.setColor(ROBOT_COLOR);
					} else if (grid[r][c] == TARGET) {
						g.setColor(TARGET_COLOR);
					} else if (grid[r][c] == OBST) {
						g.setColor(OBST_COLOR);
					} else if (grid[r][c] == FRONTIER) {
						g.setColor(FRONTIER_COLOR);
					} else if (grid[r][c] == CLOSED) {
						g.setColor(CLOSED_COLOR);
					} else if (grid[r][c] == ROUTE) {
						g.setColor(ROUTE_COLOR);
					}
					g.fillRect(11 + c * squareSize, 11 + r * squareSize, squareSize - 1, squareSize - 1);
				}
			}

		} // end paintComponent()

	} // end nested classs GraphPanel

} // end class Pathfinder