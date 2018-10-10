package patientrx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import danfulea.utils.FrameUtilities;

import org.jfree.ui.RefineryUtilities;

/**
 * Class designed for setting the program language <br>
 * 
 * 
 * @author Dan Fulea, 14 Jun. 2015
 */
@SuppressWarnings("serial")
public class Language extends JFrame implements ActionListener{
	private final Dimension PREFERRED_SIZE = new Dimension(600, 400);
	private static final Dimension sizeCb = new Dimension(100,21);
	@SuppressWarnings("unused")
	private PatientRx mf;
	private static final String BASE_RESOURCE_CLASS = "resources.Resources";
	private ResourceBundle resources;
	
	@SuppressWarnings("rawtypes")
	private JComboBox langCb;
	
	private String command = null;
	private static final String SET_COMMAND = "EXIT";
	
	private String filename = "Language.lng";	
	
	/**
	 * Constructor
	 * @param mf, the PatientRx object
	 */
	public Language(PatientRx mf){
		resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
		this.setTitle(resources.getString("Language.NAME"));//+PatientRx.lang));
		this.setResizable(false);

		this.mf = mf;
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
	        return PREFERRED_SIZE;
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
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void creareGUI()
		{
			String[] sarc = (String[])resources.getObject("language.Cb");
			langCb=new JComboBox(sarc);
			langCb.setMaximumRowCount(5);
			langCb.setSelectedItem((Object)sarc[1]);//RO
			langCb.setPreferredSize(sizeCb);
		    
			Character mnemonic = null;
			JButton button = null;
			JLabel label = null;
			String buttonName = "";
			String buttonToolTip = "";
			String buttonIconName = "";
			
			JPanel pp3 = new JPanel();
	    	pp3.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));	    	
	    	pp3.add(langCb);
	    	buttonName = resources.getString("language.button.set");
			buttonToolTip = resources.getString("language.button.set.toolTip");
			buttonIconName = resources.getString("img.set");
			button = FrameUtilities.makeButton(buttonIconName, SET_COMMAND,
					buttonToolTip, buttonName, this, this);
			mnemonic = (Character) resources.getObject("language.button.set.mnemonic");
			button.setMnemonic(mnemonic.charValue());
			pp3.add(button);
	    	pp3.setBackground(PatientRx.bkgColor);
	    	
	    	JPanel pp4 = new JPanel();
	    	pp4.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));	 
	    	label = new JLabel("Restart application for changes to take effect!");
	    	pp4.add(label);
	    	pp4.setBackground(PatientRx.bkgColor);
	    	
	    	JPanel unitP=new JPanel();
		    BoxLayout bl99 = new BoxLayout(unitP,BoxLayout.Y_AXIS);
		    unitP.setLayout(bl99);
		    unitP.add(pp3,null);
		    unitP.add(pp4,null);
		    unitP.setBackground(PatientRx.bkgColor);
		    
			JPanel content = new JPanel(new BorderLayout());
			//content.setBorder(STANDARD_BORDER);
			//String label=mf.resources.getString("help.text"+PatientRx.lang);
			//JTextArea area = new JTextArea(label);
			//area.setLineWrap(true);//impartirea daca scrisul e prea lung si iese din cadru
			//area.setWrapStyleWord(true);//liniile se firteaza at word boundaries (whitespace) if they are too long to fit within the allocated width
			//area.setCaretPosition(0);//Sets the position of the text insertion caret for the TextComponent
			//area.setEditable(false);//needitabil
			//content.add(new JScrollPane(area));//scrolabil
			
			content.add(unitP, BorderLayout.CENTER);
			
			setContentPane(new JScrollPane(content));
			content.setOpaque(true); //content panes must be opaque
			pack();
		}
		
	/**
	 * Setting up actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		command = e.getActionCommand();
		if (command.equals(SET_COMMAND)) {
			set();
		}
	}
	
	/*private String asciiToStr(int i) {
		char a[] = new char[1];
		a[0] = (char) i;
		return (new String(a)); // char to string
	}*/
	
	/**
	 * Set the language
	 */
	private void set(){
		try {
			String lnfName = (String) langCb.getSelectedItem();
			
			String fileSeparator = System.getProperty("file.separator");
			String curentDir = System.getProperty("user.dir");
			String filename = curentDir + fileSeparator + this.filename;
			
			File f = new File(filename);
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write(lnfName);
			fw.close();
			
		} catch (Exception exc) {
			exc.printStackTrace();
			
		}
	}

}
