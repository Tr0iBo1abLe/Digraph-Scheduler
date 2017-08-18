package GUI;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class SysInfolPieChart extends ApplicationFrame implements Runnable{

    private static SysInfolPieChart sysInfolPieChart = null;

    private DefaultPieDataset cpuDataset;

    private DefaultPieDataset memDataset;

    public static SysInfolPieChart getInstance(){
        if (sysInfolPieChart == null){
            sysInfolPieChart = new SysInfolPieChart("CPU Usage");
            return sysInfolPieChart;
        }
        return sysInfolPieChart;
    }

    private SysInfolPieChart(String title ) {
        super( title );
        JPanel sysInfoPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        sysInfoPanel.add(createCPUPanel( ));
        sysInfoPanel.add(createMemPanel( ));
        setContentPane(sysInfoPanel);
    }

    private PieDataset createCpuDataset( ) {
        cpuDataset = new DefaultPieDataset( );
        cpuDataset.setValue( "Total CPU usage" , new Double( 0 ) );
        cpuDataset.setValue( "Free space" , new Double( 100 ) );
        return cpuDataset;
    }

    private PieDataset createMemDataset( ) {
        memDataset = new DefaultPieDataset( );
        memDataset.setValue( "Total Memory usage" , new Double( 0 ) );
        memDataset.setValue( "Unused Memory" , new Double( 100 ) );
        return memDataset;
    }

    public void updateCPUChart(double cpuUsage, double memUsage){
        cpuDataset.setValue( "Total CPU usage" , cpuUsage );
        cpuDataset.setValue( "Free space" , (double)100 - cpuUsage );
        memDataset.setValue( "Total Memory usage" , memUsage );
        memDataset.setValue( "Unused Memory" , (double)100 - memUsage );
    }

    private JFreeChart createCpuChart(PieDataset dataset ) {
        JFreeChart chart = ChartFactory.createPieChart(
                "CPU Usage",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);
        chart.getTitle().setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        return chart;
    }

    private JFreeChart createMemChart(PieDataset dataset ) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Memory Usage",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);
        chart.getTitle().setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        return chart;
    }

    public JPanel createCPUPanel( ) {
        JFreeChart chart = createCpuChart(createCpuDataset( ) );
        return new ChartPanel( chart );
    }

    public JPanel createMemPanel( ) {
        JFreeChart chart = createMemChart(createMemDataset( ) );
        return new ChartPanel( chart );
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        SysInfolPieChart sysInfolPieChart = SysInfolPieChart.getInstance();
        sysInfolPieChart.setPreferredSize(new Dimension( 560 , 840 ));
        sysInfolPieChart.pack();
        RefineryUtilities.centerFrameOnScreen( sysInfolPieChart );
        sysInfolPieChart.setVisible( true );
    }
}
