package pl.edu.pw.wsd.agency.charts;

import org.jfree.chart.ChartPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.joda.time.DateTime;

import pl.edu.pw.wsd.agency.message.content.AgentStatistics;
import pl.edu.pw.wsd.agency.message.content.AgentStatus;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart extends ApplicationFrame 
{
   private XYSeries received;
   private XYSeries sent;   
   private DateTime startDate ;

   public Chart( String title, DateTime startDate )
   {
      super("Statistics");
      
      received = new XYSeries( "Received" );
      sent = new XYSeries( "Sent" );
      received.add(0.0, 0.0);
      sent.add(0.0, 0.0);
      this.startDate = startDate;
      
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         title ,
         "Time [s]" ,
         "Number of messages" ,
         createDataset() ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 1120 , 734 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0, Color.RED );
      renderer.setSeriesPaint( 1, Color.GREEN );
      renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
      renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
      
      this.pack( );          
      RefineryUtilities.centerFrameOnScreen( this );          
      this.setVisible( true ); 

   }
   
   private XYDataset createDataset( )
   {
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( received );          
      dataset.addSeries( sent );          
      return dataset;
   }
   
   public void addDataset( AgentStatus status )
   {
	   double timeInMilliseconds = (status.getTimestamp().getMillis() - startDate.getMillis()) / 1000.0;
	   received.add(timeInMilliseconds, status.getStatistics().getMessagesReceived());
	   sent.add(timeInMilliseconds, status.getStatistics().getMessagesSent());
	   
	      this.pack( );          
	      RefineryUtilities.centerFrameOnScreen( this );          
	      this.setVisible( true ); 
   }

   public static void main( String[ ] args ) 
   {
	   Chart chart = new Chart( "T_2", DateTime.now() );
	   AgentStatus status = new AgentStatus( );
	   status.setTimestamp(DateTime.now().plus(3000));
	   AgentStatistics statistics = new AgentStatistics();
	   statistics.setMessagesReceived(12);
	   statistics.setMessagesSent(10);
	   status.setStatistics(statistics);
	   chart.addDataset(status);
   }
}