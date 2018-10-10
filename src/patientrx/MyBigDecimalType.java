package patientrx;

import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;

/**
 * Class designed for setting the custom BigDecimalType <br>
 * 
 * 
 * @author Dan Fulea, 14 Jun. 2015
 */
public class MyBigDecimalType extends BigDecimalType{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getPattern() {
		return "####.00";
	}
	
}
