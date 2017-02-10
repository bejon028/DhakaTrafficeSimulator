
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */

public class AccidentCountPlot {
    Plot2DPanel plot = new Plot2DPanel();
    public AccidentCountPlot(double[] x)
    {
      // Adding Plots
      plot.addBarPlot("Accident Count Plot", x);
      // put the PlotPanel in a JFrame, as a JPanel
      JFrame frame = new JFrame("Accident Count");
      frame.setSize(600, 600);
      frame.setContentPane(plot);
      frame.setVisible(true);   
    }
}
