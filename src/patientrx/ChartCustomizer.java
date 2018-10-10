package patientrx;

import java.io.Serializable;

import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

/**
 * Class designed for customizing charts <br>
 * 
 * 
 * @author Dan Fulea, 14 Jun. 2015
 */
@SuppressWarnings("serial")
public class ChartCustomizer implements DRIChartCustomizer, Serializable{
	public void customize(JFreeChart chart, ReportParameters reportParameters) {  
	   
		//BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();  
	    //CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
		 //rotate axis values:
	    //domainAxis.setCategoryLabelPositions(
	    	//	CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 18.0));
	    
	    //create only integer values on x-axis:		
		NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    
	   
	  }  
}
