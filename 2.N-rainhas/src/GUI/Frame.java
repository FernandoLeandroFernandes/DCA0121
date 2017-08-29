package GUI;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import evolucao.Tabuleiro;
import genetica.Execute;

public class Frame extends javax.swing.JFrame {

	/**
	 * Classe principal do programa.
	 */
	private static final long serialVersionUID = 1L;
	private PintaTabuleiro tabuleiroVisual;
	private Chart chart;

	private javax.swing.JButton IniciarBotao;
	private javax.swing.JButton AboutBotao;

	private javax.swing.JCheckBox jCheckBoxElitismo;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabelTaxaCrossover;
	private javax.swing.JLabel jLabelTaxaMutation;
	private javax.swing.JLabel jLabelNumDeGeneracoes;
	private javax.swing.JScrollPane jScrollPanel;
	private javax.swing.JSpinner jSpinnerMaximoGeracoes;
	private javax.swing.JSpinner jSpinnerN;
	private javax.swing.JSpinner jSpinnerTamPopulacao;
	private javax.swing.JSpinner jSpinnerTaxaCrossover;
	private javax.swing.JSpinner jSpinnerTaxaMutacao;
	private javax.swing.JSplitPane jSplitPanel;
	private javax.swing.JTextArea logArea;
	private javax.swing.JTabbedPane painelAbas;
	private javax.swing.JPanel painelControles;
	private javax.swing.JPanel painelGrafico;
	private javax.swing.JPanel painelTabuleiro;

	public Frame() {
		super("Algoritmos I.A.- N-Rainhas");
		initComponents();

		this.setLocationRelativeTo(null);

		painelAbas.setTitleAt(0, "Tabuleiro");
		painelAbas.setTitleAt(1, "Grafico");

		chart = new Chart(painelGrafico.getWidth(), painelGrafico.getHeight());
		addGraphic(chart.getImage());

		SpinnerNumberModel spnmMutationRate = new SpinnerNumberModel(0.01, 0.01, 1, 0.01);
		jSpinnerTaxaMutacao.setModel(spnmMutationRate);

		SpinnerNumberModel spnmCrossoverRate = new SpinnerNumberModel(0.9, 0.01, 1, 0.1);
		jSpinnerTaxaCrossover.setModel(spnmCrossoverRate);

		SpinnerNumberModel spnmNGen = new SpinnerNumberModel(10000, 1, 100000, 10);
		jSpinnerMaximoGeracoes.setModel(spnmNGen);

		SpinnerNumberModel spnmN = new SpinnerNumberModel(8, 4, 40, 4);
		jSpinnerN.setModel(spnmN);

		SpinnerNumberModel spnmTamPop = new SpinnerNumberModel(100, 10, 1000, 50);
		jSpinnerTamPopulacao.setModel(spnmTamPop);

		Execute.setN(8);

		tabuleiroVisual = new PintaTabuleiro(new Tabuleiro(Execute.getN()), painelTabuleiro.getWidth());

		painelTabuleiro.setLayout(new BorderLayout());
		painelTabuleiro.add(tabuleiroVisual, BorderLayout.CENTER);
	}

	private void initComponents() {

		jSplitPanel = new javax.swing.JSplitPane();
		painelAbas = new javax.swing.JTabbedPane();
		painelTabuleiro = new javax.swing.JPanel();
		painelGrafico = new javax.swing.JPanel();
		painelControles = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jSpinnerN = new javax.swing.JSpinner();
		jLabelTaxaCrossover = new javax.swing.JLabel();
		jLabelTaxaMutation = new javax.swing.JLabel();
		jLabelNumDeGeneracoes = new javax.swing.JLabel();
		jSpinnerMaximoGeracoes = new javax.swing.JSpinner();
		jSpinnerTaxaMutacao = new javax.swing.JSpinner();
		jSpinnerTaxaCrossover = new javax.swing.JSpinner();
		IniciarBotao = new javax.swing.JButton();
		AboutBotao = new javax.swing.JButton();
		jScrollPanel = new javax.swing.JScrollPane();
		logArea = new javax.swing.JTextArea();
		jLabel2 = new javax.swing.JLabel();
		jSpinnerTamPopulacao = new javax.swing.JSpinner();
		jCheckBoxElitismo = new javax.swing.JCheckBox();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jSplitPanel.setDividerLocation(660);

		javax.swing.GroupLayout painelTabuleiroLayout = new javax.swing.GroupLayout(painelTabuleiro);
		painelTabuleiro.setLayout(painelTabuleiroLayout);
		painelTabuleiroLayout.setHorizontalGroup(painelTabuleiroLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 666, Short.MAX_VALUE));
		painelTabuleiroLayout.setVerticalGroup(painelTabuleiroLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 661, Short.MAX_VALUE));

		painelAbas.addTab("tab1", painelTabuleiro);

		javax.swing.GroupLayout painelGraficoLayout = new javax.swing.GroupLayout(painelGrafico);
		painelGrafico.setLayout(painelGraficoLayout);
		painelGraficoLayout.setHorizontalGroup(painelGraficoLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 666, Short.MAX_VALUE));
		painelGraficoLayout.setVerticalGroup(painelGraficoLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 661, Short.MAX_VALUE));

		painelAbas.addTab("tab2", painelGrafico);

		jSplitPanel.setLeftComponent(painelAbas);

		jLabel1.setText("N:");

		jSpinnerN.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSpinnerNStateChanged(evt);
			}
		});

		jLabelTaxaCrossover.setText("Taxa de Crossover:");

		jLabelTaxaMutation.setText("Taxa de Mutacao:");

		jLabelNumDeGeneracoes.setText("Numero de Geracoes:");

		IniciarBotao.setText("Iniciar");
		IniciarBotao.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				IniciarBotaoActionPerformed(evt);
			}
		});

		AboutBotao.setText("Sobre");
		AboutBotao.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SobreBotaoActionPerformed(evt);
			}
		});

		logArea.setEditable(false);
		logArea.setColumns(20);
		logArea.setRows(5);
		jScrollPanel.setViewportView(logArea);

		jLabel2.setText("Tamanho da Populacao:");

		jCheckBoxElitismo.setSelected(true);
		jCheckBoxElitismo.setText("Elitismo");

		javax.swing.GroupLayout painelControlesLayout = new javax.swing.GroupLayout(painelControles);
		painelControles.setLayout(painelControlesLayout);
		painelControlesLayout.setHorizontalGroup(painelControlesLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(painelControlesLayout.createSequentialGroup().addContainerGap()
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										painelControlesLayout.createSequentialGroup()
												.addGroup(painelControlesLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(painelControlesLayout.createSequentialGroup()
																.addGroup(painelControlesLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																		.addComponent(jLabelTaxaCrossover)
																		.addComponent(jLabelTaxaMutation)
																		.addComponent(jLabelNumDeGeneracoes)
																		.addComponent(jLabel1))
																.addGap(8, 8, 8))
														.addGroup(painelControlesLayout.createSequentialGroup()
																.addComponent(jLabel2).addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
												.addGroup(painelControlesLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(jSpinnerTaxaCrossover,
																javax.swing.GroupLayout.DEFAULT_SIZE, 70,
																Short.MAX_VALUE)
														.addComponent(jSpinnerN, javax.swing.GroupLayout.DEFAULT_SIZE,
																70, Short.MAX_VALUE)
														.addComponent(jSpinnerTaxaMutacao,
																javax.swing.GroupLayout.DEFAULT_SIZE, 70,
																Short.MAX_VALUE)
														.addComponent(jSpinnerMaximoGeracoes,
																javax.swing.GroupLayout.DEFAULT_SIZE, 70,
																Short.MAX_VALUE)
														.addComponent(jSpinnerTamPopulacao)))
								.addComponent(IniciarBotao, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(AboutBotao, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
								.addComponent(jCheckBoxElitismo, javax.swing.GroupLayout.Alignment.CENTER))
						.addContainerGap()));
		painelControlesLayout.setVerticalGroup(painelControlesLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(painelControlesLayout.createSequentialGroup().addContainerGap()
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1).addComponent(jSpinnerN, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jSpinnerTaxaCrossover, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabelTaxaCrossover))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabelTaxaMutation).addComponent(jSpinnerTaxaMutacao,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jSpinnerMaximoGeracoes, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabelNumDeGeneracoes))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(painelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jSpinnerTamPopulacao, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel2))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBoxElitismo)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(IniciarBotao)
						.addComponent(AboutBotao).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)));

		jSplitPanel.setRightComponent(painelControles);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 957, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE));

		pack();
	}

	private void IniciarBotaoActionPerformed(java.awt.event.ActionEvent evt) {
		chart.clear();
		Execute.setN(Integer.valueOf(jSpinnerN.getValue().toString()));
		Execute.setTaxaDeCrossover(Double.valueOf(jSpinnerTaxaCrossover.getValue().toString()));
		Execute.setTaxaDeMutacao(Double.valueOf(jSpinnerTaxaMutacao.getValue().toString()));
		Execute.setNumeroMaximoGeracoes(Integer.valueOf(jSpinnerMaximoGeracoes.getValue().toString()));
		Execute.setTamanhoPopulacao(Integer.valueOf(jSpinnerTamPopulacao.getValue().toString()));
		Execute.setElitismo(jCheckBoxElitismo.isSelected());
		logArea.setText(null);
		Execute.AG(this);
	}

	private void SobreBotaoActionPerformed(java.awt.event.ActionEvent evt) {
		AboutBox sobre = new AboutBox(null, true);
		sobre.setVisible(true);
	}

	private void jSpinnerNStateChanged(javax.swing.event.ChangeEvent evt) {
		Execute.setN(Integer.valueOf(jSpinnerN.getValue().toString()));
		tabuleiroVisual.setTabuleiro(new Tabuleiro(Execute.getN()));
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Frame().setVisible(true);
			}
		});
	}

	public void setLog(String texto) {
		logArea.append(texto + "\n");
		logArea.setCaretPosition(logArea.getText().length() - 1);
	}

	public PintaTabuleiro getTabuleiroVisual() {
		return tabuleiroVisual;
	}

	public void setEstadoIniciarBotao(boolean estado) {
		IniciarBotao.setEnabled(estado);
	}

	public Chart getChart() {
		return chart;
	}

	public void addGraphic(JPanel img) {
		painelGrafico.removeAll();
		GroupLayout mainPanelLayout = new GroupLayout(painelGrafico);
		painelGrafico.setLayout(mainPanelLayout);
		GroupLayout.SequentialGroup hGroup = mainPanelLayout.createSequentialGroup();
		hGroup.addGap(5, 5, 5);
		hGroup.addComponent(img);
		mainPanelLayout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = mainPanelLayout.createSequentialGroup();
		vGroup.addGap(5, 5, 5);
		vGroup.addComponent(img);
		mainPanelLayout.setVerticalGroup(vGroup);
		img.setVisible(true);
	}
}
