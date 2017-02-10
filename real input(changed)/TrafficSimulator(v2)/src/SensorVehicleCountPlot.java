import javax.swing.JFrame;
import javax.swing.JPanel;
import org.math.plot.*;
public class SensorVehicleCountPlot {


  // create your PlotPanel (you can use it as a JPanel)
  Plot2DPanel plot = new Plot2DPanel();

  public SensorVehicleCountPlot(double[] x)
  {
      // Adding Plots
      plot.addBarPlot("Count Plot", x);
      // put the PlotPanel in a JFrame, as a JPanel
      JFrame frame = new JFrame("Vehicle Count");
      frame.setSize(600, 600);
      frame.setContentPane(plot);
      frame.setVisible(true);
  }
}




