package pl.edu.pw.wsd.agency.charts;

import java.util.HashMap;

import org.joda.time.DateTime;

import pl.edu.pw.wsd.agency.message.content.AgentStatus;

public class ChartsManager 
{
   private HashMap<String, Chart> charts;
   private DateTime startDate;
   
   public ChartsManager( )
   {
	   startDate = DateTime.now( );
	   charts = new HashMap<>();
   }
   
   public void handleNewStatus( AgentStatus status )
   {
	   if (charts.containsKey(status.getSenderId()))
		   charts.get(status.getSenderId()).addStatus(status);
	   else
	   {
		   Chart newChart = new Chart( status.getSenderId(), startDate );
		   charts.put(status.getSenderId(), newChart );
		   charts.get(status.getSenderId()).addStatus(status);
	   }
   }
}