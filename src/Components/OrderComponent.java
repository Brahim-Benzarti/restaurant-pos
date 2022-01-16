/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Models.ProductModel;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author benza
 */
public class OrderComponent extends javax.swing.JPanel {

    /**
     * Creates new form OrderComponent
     */
    public OrderComponent(ProductModel product, int quantity, XYDataset trend) {
        initComponents();
        this.productPic.setIcon(new ImageIcon(new ImageIcon(product.pictureurl).getImage().getScaledInstance(70,69,Image.SCALE_AREA_AVERAGING)));
        this.productName.setText(product.name);
        this.productUnitPrice.setText(String.valueOf(product.price));
        this.productCount.setText(String.valueOf(quantity));
        this.productTotalPrice.setText(String.valueOf(product.price*quantity));
        ChartPanel panel = new ChartPanel(createChart(trend));
        panel.setSize(250,80);
        panel.setVisible(true);
        productTrendChart.add(panel);
        productTrendChart.setBackground(new Color(41,43,55));
    }
    
    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(41,43,55));
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productPic = new javax.swing.JLabel();
        productName = new javax.swing.JLabel();
        productTotalPrice = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        productUnitPrice = new javax.swing.JLabel();
        productCount = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        chartFrame = new javax.swing.JPanel();
        productTrendChart = new javax.swing.JPanel();

        setBackground(new java.awt.Color(41, 43, 55));
        setMaximumSize(new java.awt.Dimension(656, 90));
        setMinimumSize(new java.awt.Dimension(656, 90));

        productName.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        productName.setText("Produt Name");

        productTotalPrice.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        productTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productTotalPrice.setText("0");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("DT");

        productUnitPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productUnitPrice.setText("0");

        productCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productCount.setText("0");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("x");

        chartFrame.setMaximumSize(new java.awt.Dimension(216, 69));
        chartFrame.setMinimumSize(new java.awt.Dimension(216, 69));
        chartFrame.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout productTrendChartLayout = new javax.swing.GroupLayout(productTrendChart);
        productTrendChart.setLayout(productTrendChartLayout);
        productTrendChartLayout.setHorizontalGroup(
            productTrendChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        productTrendChartLayout.setVerticalGroup(
            productTrendChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        chartFrame.add(productTrendChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 250, 80));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(productPic, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chartFrame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(productUnitPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productCount, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(productTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productPic, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productCount)
                            .addComponent(jLabel4)
                            .addComponent(productUnitPrice)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(chartFrame, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(productName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        productPic.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartFrame;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel productCount;
    private javax.swing.JLabel productName;
    private javax.swing.JLabel productPic;
    private javax.swing.JLabel productTotalPrice;
    private javax.swing.JPanel productTrendChart;
    private javax.swing.JLabel productUnitPrice;
    // End of variables declaration//GEN-END:variables
}
