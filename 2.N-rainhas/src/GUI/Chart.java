package GUI;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class Chart {

    private JFreeChart jFreeChart;
    private XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries xyBestAllTime = new XYSeries("Solucao");
    XYSeries xyBestOfGen = new XYSeries("Melhor da Geracao");
    XYSeries xyWorseOfGen = new XYSeries("Pior da Geracao");
    XYSeries xyAVG = new XYSeries("Media");
    private ChartPanel img;
    private int height, width;

    // Dimensao da janela do grafico
    public Chart(int width, int height) {
        this.height = height;
        this.width = width;
        draw();
    }

    // Metodo de atualizacao na interface (log)
    public void update(int actualGeneration, double melhor, double media, double pior) {
        xyBestAllTime.add(actualGeneration, 0);
        xyBestOfGen.add(actualGeneration, melhor);
        xyWorseOfGen.add(actualGeneration, pior);
        xyAVG.add(actualGeneration, media);
        jFreeChart.fireChartChanged();
    }

    public void clear() {
        xyBestAllTime.clear();
        xyBestOfGen.clear();
        xyWorseOfGen.clear();
        xyAVG.clear();
    }

    public JPanel getImage() {
        return img;
    }

    // O metodo draw() desenha o grafico na tela
    private void draw() {
        jFreeChart = ChartFactory.createXYLineChart("N-Generation", "Numero de Geracoes", "Numero de Colisoes", dataset, PlotOrientation.VERTICAL, true, true, true);
        img = new ChartPanel(jFreeChart);
        img.setPreferredSize(new java.awt.Dimension(width - 30, height - 40));
        img.setMinimumSize(new java.awt.Dimension(width - 30, height - 40));
        img.setMaximumSize(new java.awt.Dimension(width - 30, height - 40));
        img.setMouseZoomable(true);
        img.updateUI();
        dataset.addSeries(xyWorseOfGen);
        dataset.addSeries(xyBestAllTime);
        dataset.addSeries(xyBestOfGen);
        dataset.addSeries(xyAVG);
    }
}