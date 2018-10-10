package patientrx;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Dimension;
//import java.util.ResourceBundle;
import org.jfree.ui.RefineryUtilities;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class for displaying some tips and hints.
 *
 *
 * @author Dan Fulea, 06 Jul. 2007
 */

@SuppressWarnings("serial")
public class DAPHelp extends JFrame
{
	protected PatientRx mf;
	private static final String BASE_RESOURCE_CLASS = "resources.Resources";
	private ResourceBundle resources;
	//private Dimension dimension;
	private String label;
	private static final Dimension prefferedSize = new Dimension(600,400);
	public static final Border STANDARD_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);

	/**
	 * Constructor
	 * @param mf the PatientRx object
	 */
	public DAPHelp(PatientRx mf)//(String title, String label, Dimension dimension)
	{
		resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
		//super("Informatii utile");//(title);
		this.setTitle(resources.getString("HowTo.NAME"+PatientRx.lang));
		this.mf=mf;
		mf.setEnabled(false);

        creareGUI();

        RefineryUtilities.centerFrameOnScreen(this);
        setDefaultLookAndFeelDecorated(true);
		createImageIcon(mf.resources.getString("form.icon.url"));

		setVisible(true);

        final PatientRx mff = mf;
        addWindowListener(new WindowAdapter()
		{
		     public void windowClosing(WindowEvent e)
		     {
			     mff.setEnabled(true);
	             dispose();
		     }
        });

    }
	
	/**
	 * Setting up the frame size.
	 */
    public Dimension getPreferredSize()
    {
        return prefferedSize;
    }

    /**
	  * Load and set image icon on the frame (window)
	  * @param URLstr URLstr
	  */
	private void createImageIcon(String URLstr)
	{
		byte[] tmp= new byte[102400];
		int size = 0;
		try
		{
			InputStream is = getClass().getResourceAsStream(URLstr);//"/Jad/images/graphic.gif");

			while (is.available() > 0)
			{
				is.read(tmp, size, 1);
				size++;
			}
			is.close();
			byte[] data = new byte[size];
			System.arraycopy(tmp, 0, data, 0, size);
			ImageIcon icon = new ImageIcon(data);
			setIconImage(icon.getImage());
		//JOptionPane.showMessageDialog(null, icon, "Size: " + data.length,
									//JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception exc)
		{
			//JOptionPane.showMessageDialog(null,exc.getMessage(),"Error.",JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * GUI creation.
	 */
	private void creareGUI()
	{
		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(STANDARD_BORDER);
		label=mf.resources.getString("help.text"+PatientRx.lang);
		JTextArea area = new JTextArea(this.label);
		area.setLineWrap(true);//impartirea daca scrisul e prea lung si iese din cadru
		area.setWrapStyleWord(true);//liniile se firteaza at word boundaries (whitespace) if they are too long to fit within the allocated width
		area.setCaretPosition(0);//Sets the position of the text insertion caret for the TextComponent
		area.setEditable(false);//needitabil
		content.add(new JScrollPane(area));//scrolabil
		setContentPane(content);

		pack();
	}
}