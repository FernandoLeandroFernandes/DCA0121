package jFuzzy.GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jFuzzy.Main.MainClass;
import ufrn.ia.fuzzy.model.Function;
import ufrn.ia.fuzzy.model.Input;
import ufrn.ia.fuzzy.model.Output;
import ufrn.ia.fuzzy.model.Rule;
import ufrn.ia.fuzzy.model.RuleBlock;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JList;
import java.awt.Font;

public class Interface {

	private JFrame MainTela;
	private JTextField textInput;
	private JTextField textOutput;
	private JTextField textParametros;
	private JTextField textRules1;
	private JTextField textRules2;
	private JTextField textRules3;
	private JTextField textInter1;
	private JTextField textInter2;
	private JTextField textInter3;
	private JTextField textInter4;
	private JList<String> list;

	
	private Function fuzzy = new Function("RISCO");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.MainTela.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		MainTela = new JFrame();
		MainTela.setResizable(false);
		MainTela.setTitle("jFuzzyApp");
		MainTela.setBounds(100, 100, 592, 300);
		MainTela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainTela.getContentPane().setLayout(null);

		textInput = new JTextField();
		textInput.setBounds(452, 7, 97, 20);
		MainTela.getContentPane().add(textInput);
		textInput.setColumns(10);

		textOutput = new JTextField();
		textOutput.setBounds(10, 80, 90, 20);
		MainTela.getContentPane().add(textOutput);
		textOutput.setColumns(10);

		final JTextArea textLog = new JTextArea();

		textLog.setColumns(20);
		textLog.setRows(5);
		textLog.setToolTipText("Log de eventos do programa");
		textLog.setEditable(false);
		textLog.setBounds(165, 160, 269, 100);

		MainTela.getContentPane().add(textLog);

		final JLabel labelInputs = new JLabel("Entradas");
		labelInputs.setToolTipText("Define variaveis de entrada");
		labelInputs.setBounds(10, 10, 80, 14);
		MainTela.getContentPane().add(labelInputs);

		JLabel labelOutputs = new JLabel("Saidas");
		labelOutputs.setToolTipText("Define variaveis de saida");
		labelOutputs.setBounds(10, 60, 60, 14);
		MainTela.getContentPane().add(labelOutputs);

		JButton btnSobre = new JButton("Sobre");
		btnSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutBox sobre = new AboutBox(null, true);
				sobre.setVisible(true);
			}
		});

		btnSobre.setBounds(10, 240, 89, 23);
		MainTela.getContentPane().add(btnSobre);

		JButton plusInput = new JButton("+");
		plusInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textLog.setText("> " + "Entrada - " + textInput.getText() + "\n");
				fuzzy.addInput(new Input(textInput.getText()));
			}
		});
		plusInput.setBounds(105, 30, 50, 20);
		MainTela.getContentPane().add(plusInput);

		JButton plusOutput = new JButton("+");
		plusOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textLog.setText("> " + "Saida - " + textOutput.getText() + "\n");
				fuzzy.addOutput(new Output(textOutput.getText()));
			}
		});
		plusOutput.setBounds(105, 80, 50, 20);
		MainTela.getContentPane().add(plusOutput);

		JButton plusRules = new JButton("+");
		plusRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textLog.setText("> " + "Regra - IF " + textRules1.getText() + " AND " + textRules2.getText() + " THEN "
						+ textRules3.getText() + "\n");
				fuzzy.addRule(new Rule(fuzzy.ruleCount(), String.format("%s AND %s" , textRules1.getText(), textRules2.getText()), textRules3.getText()));
			}
		});
		plusRules.setBounds(384, 130, 50, 20);
		MainTela.getContentPane().add(plusRules);

		JLabel lblParametros = new JLabel("Parametros");
		lblParametros.setToolTipText("Fuzzificando variaveis de entrada");
		lblParametros.setBounds(165, 10, 90, 14);
		MainTela.getContentPane().add(lblParametros);

		JLabel lblRegras = new JLabel("Regras ");
		lblRegras.setToolTipText("Regras de Inferencia");
		lblRegras.setBounds(10, 110, 60, 14);
		MainTela.getContentPane().add(lblRegras);
		
		JLabel lblIntervalos = new JLabel("Intervalos (2 para rampa, 4 para trapezio)");
		lblIntervalos.setToolTipText("Delimitar a area do grafico em (x=escala de entrada, y = 0~1)");
		lblIntervalos.setBounds(165, 60, 253, 15);
		MainTela.getContentPane().add(lblIntervalos);
		
		JLabel lblIf = new JLabel("IF");
		lblIf.setBounds(10, 133, 17, 14);
		MainTela.getContentPane().add(lblIf);
		
		JLabel lblAnd = new JLabel(" AND");
		lblAnd.setBounds(125, 133, 30, 14);
		MainTela.getContentPane().add(lblAnd);

		JLabel lblThen = new JLabel(" THEN");
		lblThen.setBounds(250, 133, 40, 14);
		MainTela.getContentPane().add(lblThen);
		
		textParametros = new JTextField();
		textParametros.setColumns(10);
		textParametros.setBounds(165, 30, 90, 20);
		MainTela.getContentPane().add(textParametros);

		JButton button = new JButton("+");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textLog.setText("> " + "Parametro - " + textParametros.getText() + " ("+ textInter1.getText() + " e " + textInter2.getText() + " e " + textInter3.getText() + " e " + textInter4.getText() + ")\n");
				fuzzy.addOutput(new Output(textOutput.getText()));
			}
		});
		button.setBounds(260, 30, 50, 20);
		MainTela.getContentPane().add(button);
		
		textRules1 = new JTextField();
		textRules1.setColumns(10);
		textRules1.setBounds(30, 130, 90, 20);
		MainTela.getContentPane().add(textRules1);

		textRules2 = new JTextField();
		textRules2.setColumns(10);
		textRules2.setBounds(160, 130, 90, 20);
		MainTela.getContentPane().add(textRules2);

		textRules3 = new JTextField();
		textRules3.setColumns(10);
		textRules3.setBounds(290, 130, 90, 20);
		MainTela.getContentPane().add(textRules3);
		

		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.setToolTipText("Realizar operacoes");
		btnCalcular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textLog.setText("> Calcular!\n");
				
				fuzzy.eval();
				
				try {
					MainClass.main(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnCalcular.setBounds(10, 160, 150, 55);
		MainTela.getContentPane().add(btnCalcular);

		textInter1 = new JTextField();
		textInter1.setBounds(165, 80, 50, 20);
		MainTela.getContentPane().add(textInter1);
		textInter1.setColumns(10);

		textInter2 = new JTextField();
		textInter2.setColumns(10);
		textInter2.setBounds(220, 80, 50, 20);
		MainTela.getContentPane().add(textInter2);

		textInter3 = new JTextField();
		textInter3.setColumns(10);
		textInter3.setBounds(275, 80, 50, 20);
		MainTela.getContentPane().add(textInter3);

		textInter4 = new JTextField();
		textInter4.setColumns(10);
		textInter4.setBounds(330, 80, 50, 20);
		MainTela.getContentPane().add(textInter4);
		
		list = new JList<>();
		list.setBounds(452, 30, 134, 241);
		list.setModel(new DefaultListModel<String>());
		MainTela.getContentPane().add(list);
		
		JButton button_1 = new JButton("+");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textLog.setText("> " + "Entrada - " + textInput.getText() + "\n");
				((DefaultListModel<String>)list.getModel()).addElement(textInput.getText());
				fuzzy.addInput(new Input(textInput.getText()));
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 5));
		button_1.setBounds(552, 6, 30, 20);
		MainTela.getContentPane().add(button_1);
	}
}
