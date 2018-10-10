package patientrx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import danfulea.utils.ExampleFileFilter;
import danfulea.utils.ScanDiskLFGui;
import danfulea.utils.TimeUtilities;
import danfulea.db.DatabaseAgent;
import danfulea.db.DatabaseAgentSupport;
//import jdf.db.AdvancedSelectPanel;
//import jdf.db.DBConnection;
//import jdf.db.DBOperation;
import danfulea.math.Convertor;
import danfulea.phys.XRay;
import danfulea.phys.XRaySpectrum;
import danfulea.utils.FrameUtilities;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.XyBarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.XyLineChartBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.FillerBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.jasperreports.view.JasperViewer;

/**
 * This program tries to help radiologists to keep track of patients which receive 
 * radiation dose (free in air) from radiodiagnostic examinations. It performs 
 * computation of KAP (kerma-area-product if it is not recorded 
 * by a dedicated DAP-meter) and record the patient information.<br>
 * The following parameters are required: tube kilovoltage kV, exposure time, 
 * tube current mA or tube load mAs, X field dimensions, focus-field distance, 
 * focus-patient entrance surface distance (usually, this is focus-field distance - 
 * patient thickness which is for standard patients of about 20 cm for AP,PA 
 * projections or 40 cm for LAT projections), and kerma area product KAP.<br> 
 * If KAP is not measured by a DAP-meter, it can be evaluated using the following 
 * data: anode material, anode angle, total tube filtration and voltage waveform 
 * ripple. These data can be taken from device manual or from specific measurements 
 * (HVL, etc.). X field evaluation can be measured directly on exposed radiological 
 * film. If the film does not present unexposed margins then actual X field dimension 
 * cannot be inferred. A better method, in case of appropriate field alignment 
 * (X-light field alignment), is the measurement of light field dimensions on 
 * radiological table. Focus field distance can therefore be the focus-film distance 
 * or focus-table distance. Focus-patient entrance surface distance is the difference 
 * between the focus-field distance and all other distances form entrance surface 
 * and measured field such as patient thickness and Bucky distance (if appropiate). 
 * KAP = kerma x area = const. If electronic equilibrum (which is often the case 
 * for common Rx examinations) then kerma (free in air) equals dose (free in air).
 * Dose = KAP/entrance surface area (at skin). Dose rate= Dose/exposure time.
 * 
 * 
 * @author Dan Fulea, 14 Jun. 2015
 */
@SuppressWarnings("serial")
public class PatientRx extends JFrame implements ActionListener, ItemListener{
	private final Dimension PREFERRED_SIZE = new Dimension(990, 750);
	private final Dimension tableDimension = new Dimension(700, 200);
	private static final Dimension sizeCb = new Dimension(55,21);
	public static String lang="";//".RO";//"";
	public static Color bkgColor = new Color(230, 255, 210, 255);//Linux mint green alike
	public static Color foreColor = Color.black;//Color.white;
	public static Color textAreaBkgColor = Color.white;//Color.black;
	public static Color textAreaForeColor = Color.black;//Color.yellow;
	public static boolean showLAF=true;
	
	private static final String BASE_RESOURCE_CLASS = "resources.Resources";
	public ResourceBundle resources = ResourceBundle
			.getBundle(BASE_RESOURCE_CLASS);
	
	private String command = null;
	private static final String EXIT_COMMAND = "EXIT";
    private static final String ABOUT_COMMAND = "ABOUT";
    //private static final String SAVE_COMMAND = "SAVE";//in BD
    private static final String LOOKANDFEEL_COMMAND = "LOOKANDFEEL";
    
    private static final String REPORT_COMMAND = "REPORT";
    private static final String INFO_COMMAND = "INFO";
    private static final String SAVEUNIT_COMMAND = "SAVEUNIT";
    private static final String LOADUNIT_COMMAND = "LOADUNIT";
    
    private static final String ADD_COMMAND = "ADD";
    private static final String DELETE_COMMAND = "DELETE";
    private static final String DELETEALL_COMMAND = "DELETEALL";
    private static final String TODAY_COMMAND = "TODAY";
    
    private static final String LANGUAGE_COMMAND = "LANGUAGE";
    
    protected String patientDB;
	protected String patientTable;
	
	//protected AdvancedSelectPanel asp = null;
	protected JPanel suportSp = new JPanel(new BorderLayout());
	private int IDPATIENT = 0;
	protected JLabel recordLabel=null;
	@SuppressWarnings("unused")
	private int maxUniqueID=0;
	
	protected double filtrared=0.0;
	protected double masd=0.0;
	protected double fsdd=0.0;
    protected double campxd=0.0;
    protected double campyd=0.0;
    protected double ud=0.0;	
	protected double uanodd=0.0;
	protected boolean saveBoo;
	
	@SuppressWarnings("rawtypes")
	protected JComboBox dayCb, monthCb = null;
	protected JTextField yearTf = new JTextField(5);
	protected JTextField unitateTf=new JTextField(18);
    protected JTextField adresaTf=new JTextField(30);
    protected JTextField contactTf=new JTextField(18);
    protected JTextField aparatTf=new JTextField(18);
    protected JTextField notesTf=new JTextField(18);
    protected JTextField numeTf=new JTextField(18);
    protected JTextField prenumeTf=new JTextField(18);
    protected JTextField cnpTf=new JTextField(18);
    protected JTextField examenTf=new JTextField(18);
    protected JTextField campxTf=new JTextField(5);
    protected JTextField campyTf=new JTextField(5);
    protected JTextField dfocusfilmTf=new JTextField(5);
    protected JTextField dpacientfilmTf=new JTextField(5);
    protected JTextField filtrareTf=new JTextField(5);
    protected JTextField maTf=new JTextField(5);
    protected JTextField tTf=new JTextField(5);
    protected JTextField masTf=new JTextField(5);
    protected JTextField dapTf=new JTextField(5);
    protected JTextField contorTf=new JTextField(5);
    
    private JCheckBox showPlot;
	//private String examinationDate="";
	
	@SuppressWarnings("rawtypes")
	protected JComboBox uCb;
    @SuppressWarnings("rawtypes")
	protected JComboBox sarcinaCb;
    @SuppressWarnings("rawtypes")
	protected JComboBox uanodCb;
    @SuppressWarnings("rawtypes")
	protected JComboBox rippleCb;
    @SuppressWarnings("rawtypes")
	protected JComboBox matanodCb;
    protected JTabbedPane tabs;
    
    private Connection patientdbcon = null;
    private String orderbyS = "ID";
    private DatabaseAgentSupport dbagent;
    private JComboBox<String> orderbyCb;
	private final Dimension sizeOrderCb = new Dimension(200, 21);
    
	/**
	 * Constructor
	 */
    public PatientRx(){
    	//DBConnection.startDerby();
		
		this.setTitle(resources.getString("Application.NAME"+lang));
		
		patientDB = resources.getString("main.db");
		patientTable = resources.getString("main.db.Table");
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				attemptExit();
			}
		});
		
		JMenuBar menuBar = createMenuBar(resources);
		setJMenuBar(menuBar);
		//====================================================
		DatabaseAgent.ID_CONNECTION = DatabaseAgent.DERBY_CONNECTION;
    	String datas = resources.getString("data.load");// "Data";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas;
		opens = opens + file_sep + patientDB;
		patientdbcon = DatabaseAgent.getConnection(opens, "", "");
		
		dbagent = new DatabaseAgentSupport(patientdbcon,"UNIQUE_ID", patientTable);
		dbagent.setHasValidAIColumn(false);
		//=====================================================
		//createRadQC_DB();
		performQueryDb();
		createGUI();
		
		setDefaultLookAndFeelDecorated(true);
		FrameUtilities.createImageIcon(
				this.resources.getString("form.icon.url"), this);

		FrameUtilities.centerFrameOnScreen(this);

		setVisible(true);
    }
    
    /**
	 * Setting up the frame size.
	 */
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}

	/**
	 * Program close!
	 */
	private void attemptExit() {
		try{
			if (patientdbcon != null)
				patientdbcon.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		DatabaseAgent.shutdownDerby();//DBConnection.shutdownDerby();
		dispose();
		System.exit(0);
		
	}
	
	/**
	 * Create GUI
	 */
	private void createGUI() {
		JPanel content = new JPanel(new BorderLayout());
		tabs = createTabs();

		content.add(tabs, BorderLayout.CENTER);

        setContentPane(new JScrollPane(content));
        content.setOpaque(true); //content panes must be opaque
        pack();
        numeTf.requestFocusInWindow();
	}
	
	/**
	 * Create GUI tabs
	 * @return the tabs
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JTabbedPane createTabs(){
		JTabbedPane tabs = new JTabbedPane();
		
		//DATA PANEL
		JPanel pp1 = new JPanel();
    	pp1.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	JLabel lbel=new JLabel(resources.getString("MainForm.gui.unitL"+lang));
    	pp1.add(lbel);
    	pp1.add(unitateTf);
    	pp1.setBackground(PatientRx.bkgColor);

    	//JPanel pp2 = new JPanel();
    	//pp2.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	lbel=new JLabel(resources.getString("MainForm.gui.adressL"+lang));
    	pp1.add(lbel);
    	pp1.add(adresaTf);
    	//pp2.setBackground(PatientRx.bkgColor);

    	JPanel pp3 = new JPanel();
    	pp3.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	lbel=new JLabel(resources.getString("MainForm.gui.labL"+lang));
    	pp3.add(lbel);
    	pp3.add(contactTf);
    	pp3.setBackground(PatientRx.bkgColor);

    	//JPanel pp4 = new JPanel();
    	//pp4.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	lbel=new JLabel(resources.getString("MainForm.gui.deviceL"+lang));
    	pp3.add(lbel);
    	pp3.add(aparatTf);
    	lbel=new JLabel(resources.getString("MainForm.gui.postL"+lang));
    	pp3.add(lbel);
    	pp3.add(notesTf);
    	//pp4.setBackground(PatientRx.bkgColor);

    	Character mnemonic = null;
		JButton button = null;
		JLabel label = null;
		String buttonName = "";
		String buttonToolTip = "";
		String buttonIconName = "";
		//=====================
		orderbyCb = dbagent.getOrderByComboBox();
		orderbyCb.setMaximumRowCount(5);
		orderbyCb.setPreferredSize(sizeOrderCb);
		orderbyCb.addItemListener(this);
		JPanel orderP = new JPanel();
		orderP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(resources.getString("sort.by"+lang));//"Sort by: ");
		label.setForeground(PatientRx.foreColor);
		orderP.add(label);
		orderP.add(orderbyCb);
		orderP.setBackground(PatientRx.bkgColor);
		label = new JLabel(resources.getString("records.count"+lang));//"Records count: ");
		label.setForeground(PatientRx.foreColor);
		orderP.add(label);
		orderP.add(dbagent.getRecordsLabel());				
		//=================
		String[] sarray = new String[31];
		for (int i = 1; i <= 31; i++) {
			if (i < 10)
				sarray[i - 1] = "0" + i;
			else
				sarray[i - 1] = Convertor.intToString(i);
		}
		dayCb = new JComboBox(sarray);
		dayCb.setMaximumRowCount(5);
		dayCb.setPreferredSize(sizeCb);

		sarray = new String[12];
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				sarray[i - 1] = "0" + i;
			else
				sarray[i - 1] = Convertor.intToString(i);
		}
		monthCb = new JComboBox(sarray);
		monthCb.setMaximumRowCount(5);
		monthCb.setPreferredSize(sizeCb);
		// ...
		today();
		
		showPlot=new JCheckBox(resources.getString("main.showPlot"+lang),true);
		
		JPanel dateP = new JPanel();
		dateP.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
		dateP.setBorder(FrameUtilities.getGroupBoxBorder(
				resources.getString("main.date.border"+lang), foreColor));
		label = new JLabel(resources.getString("main.date.day"+lang));
		label.setForeground(foreColor);
		dateP.add(label);
		dateP.add(dayCb);
		label = new JLabel(resources.getString("main.date.month"+lang));
		label.setForeground(foreColor);
		dateP.add(label);
		dateP.add(monthCb);
		label = new JLabel(resources.getString("main.date.year"+lang));
		label.setForeground(foreColor);
		dateP.add(label);
		dateP.add(yearTf);
		buttonName = resources.getString("main.button.today"+lang);
		buttonToolTip = resources.getString("main.button.today.toolTip"+lang);
		buttonIconName = resources.getString("img.today");
		button = FrameUtilities.makeButton(buttonIconName, TODAY_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.today.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
		dateP.add(button);
		dateP.setBackground(bkgColor);
		label.setForeground(foreColor);
		//=======================
		
    	JPanel pp5 = new JPanel();
    	pp5.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	buttonName = resources.getString("main.button.save"+lang);
		buttonToolTip = resources.getString("main.button.save.toolTip"+lang);
		buttonIconName = resources.getString("img.save.database");
		button = FrameUtilities.makeButton(buttonIconName, SAVEUNIT_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.save.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	pp5.add(button);    	
    	buttonName = resources.getString("main.button.load"+lang);
		buttonToolTip = resources.getString("main.button.load.toolTip"+lang);
		buttonIconName = resources.getString("img.open.file");
		button = FrameUtilities.makeButton(buttonIconName, LOADUNIT_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.load.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	pp5.add(button);
    	pp5.setBackground(PatientRx.bkgColor);

	    JPanel unitP=new JPanel();
	    BoxLayout bl99 = new BoxLayout(unitP,BoxLayout.Y_AXIS);
	    unitP.setLayout(bl99);
	    unitP.add(pp1,null);
	    //unitP.add(pp2,null);
	    unitP.add(pp3,null);
	    //unitP.add(pp4,null);
	    unitP.add(pp5,null);
	    unitP.setBackground(PatientRx.bkgColor);
	    unitP.setBorder(FrameUtilities.getGroupBoxBorder(
	    		resources.getString("unit.border"+lang),foreColor));

    	JPanel p1 = new JPanel();
    	p1.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.nameL"+lang));
    	p1.add(label);
    	p1.add(numeTf);
    	p1.setBackground(PatientRx.bkgColor);

    	JPanel p2 = new JPanel();
    	p2.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.surnameL"+lang));
    	p2.add(label);
    	p2.add(prenumeTf);
    	p2.setBackground(PatientRx.bkgColor);

    	JPanel p3 = new JPanel();
    	p3.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.cnpL"+lang));
    	p3.add(label);
    	p3.add(cnpTf);
    	p3.setBackground(PatientRx.bkgColor);

    	JPanel p4 = new JPanel();
    	p4.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.examinareL"+lang));
    	p4.add(label);
    	p4.add(examenTf);
    	p4.setBackground(PatientRx.bkgColor);

    	String[] sarc = (String[])resources.getObject("MainForm.sarcinaCb"+lang);
		sarcinaCb=new JComboBox(sarc);
		sarcinaCb.setMaximumRowCount(5);
		sarcinaCb.setSelectedItem((Object)sarc[1]);//Nu
	    sarcinaCb.setPreferredSize(sizeCb);
    	JPanel p5 = new JPanel();
    	p5.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.sarcinaL"+lang));
    	p5.add(label);
    	p5.add(sarcinaCb);
    	p5.setBackground(PatientRx.bkgColor);
	//Personal data panel
	    JPanel personP=new JPanel();
	    BoxLayout bl = new BoxLayout(personP,BoxLayout.Y_AXIS);
	    personP.setLayout(bl);
	    personP.add(p1,null);
	    personP.add(p2,null);
	    personP.add(p3,null);
	    personP.add(p4,null);
	    personP.add(p5,null);
	    personP.setBackground(PatientRx.bkgColor);
	    personP.setBorder(FrameUtilities.getGroupBoxBorder(
	    		resources.getString("person.border"+lang),foreColor));
	//==================================================================================
    	JPanel p11 = new JPanel();
    	p11.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.campxL"+lang));
    	p11.add(label);
    	p11.add(campxTf);
    	p11.setBackground(PatientRx.bkgColor);

    	JPanel p12 = new JPanel();
    	p12.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.campyL"+lang));
    	p12.add(label);
    	p12.add(campyTf);
    	p12.setBackground(PatientRx.bkgColor);

    	JPanel p13 = new JPanel();
    	p13.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.fsdL"+lang));
    	p13.add(label);
    	p13.add(dfocusfilmTf);
    	p13.setBackground(PatientRx.bkgColor);

    	JPanel p14 = new JPanel();
    	p14.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.esdL"+lang));
    	p14.add(label);
    	p14.add(dpacientfilmTf);//ESD here
    	p14.setBackground(PatientRx.bkgColor);

    	//============
    	String[] ua = new String[22];
	    for(int i=6; i<=23; i++)
	       ua[i-6]=Integer.toString(i);
	    uanodCb=new JComboBox(ua); //String s="17";
	    uanodCb.setSelectedItem((Object)ua[11]);//((Object)s);
	    uanodCb.setMaximumRowCount(5);
	    uanodCb.setPreferredSize(sizeCb);

	    String[] ua1 ={"W","Mo","Rh"};
	    matanodCb=new JComboBox(ua1);
	    matanodCb.setSelectedItem((Object)ua1[0]);
	    matanodCb.setMaximumRowCount(5);
	    matanodCb.setPreferredSize(sizeCb);

	    String[] ua2 = {"0","5","10","15","20","25","30"};//ripple
	    rippleCb=new JComboBox(ua2);
	    rippleCb.setSelectedItem((Object)ua2[0]);
	    rippleCb.setMaximumRowCount(5);
	    rippleCb.setPreferredSize(sizeCb);

	    String[] ua4 = new String[126];
        for(int i=25; i<=150; i++)
	       ua4[i-25]=Integer.toString(i);
	    uCb=new JComboBox(ua4);
	    uCb.setSelectedItem((Object)ua4[55]);//80
	    uCb.setMaximumRowCount(5);
	    uCb.setPreferredSize(sizeCb);

	    campxTf.setToolTipText(resources.getString("MainForm.gui.camp.ToolTip"+lang));
	    campyTf.setToolTipText(resources.getString("MainForm.gui.camp.ToolTip"+lang));
	    dapTf.setToolTipText(resources.getString("MainForm.gui.dap.ToolTip"+lang));
	    masTf.setToolTipText(resources.getString("MainForm.gui.mas.ToolTip"+lang));
	    maTf.setToolTipText(resources.getString("MainForm.gui.ma.ToolTip"+lang));
	    rippleCb.setToolTipText(resources.getString("MainForm.gui.ripple.ToolTip"+lang));
	    dpacientfilmTf.setToolTipText(resources.getString("MainForm.gui.patient.ToolTip"+lang));

        //contorTf.setText("0");
        contorTf.setEditable(false);

    	//==========
	//CAMP data panel
	    JPanel campP=new JPanel();
	    BoxLayout bl1 = new BoxLayout(campP,BoxLayout.Y_AXIS);
	    campP.setLayout(bl1);
	    campP.add(p11,null);
	    campP.add(p12,null);
	    campP.add(p13,null);
	    campP.add(p14,null);
	    campP.setBackground(PatientRx.bkgColor);
	    campP.setBorder(FrameUtilities.getGroupBoxBorder(
	    		resources.getString("camp.border"+lang),foreColor));
	//==================================================================================
    	JPanel pc = new JPanel();
    	pc.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	pc.add(personP);
    	pc.add(campP);
    	pc.setBackground(PatientRx.bkgColor);
    //=============PERSON + FIELD
    	JPanel p21 = new JPanel();
    	p21.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.uL"+lang));
    	p21.add(label);
    	p21.add(uCb);
    	p21.setBackground(PatientRx.bkgColor);

    	JPanel p22 = new JPanel();
    	p22.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.iL"+lang));
    	p22.add(label);
    	p22.add(maTf);
    	p22.setBackground(PatientRx.bkgColor);

    	JPanel p23 = new JPanel();
    	p23.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.tL"+lang));
    	p23.add(label);
    	p23.add(tTf);
    	p23.setBackground(PatientRx.bkgColor);

    	JPanel p24 = new JPanel();
    	p24.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.itL"));
    	p24.add(label);
    	p24.add(masTf);
    	p24.setBackground(PatientRx.bkgColor);

    	JPanel p25 = new JPanel();
    	p25.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.dapL"));
    	p25.add(label);
    	p25.add(dapTf);
    	p25.setBackground(PatientRx.bkgColor);

	//PARAM data panel
	    JPanel paramP=new JPanel();
	    BoxLayout bl2 = new BoxLayout(paramP,BoxLayout.Y_AXIS);
	    paramP.setLayout(bl2);
	    paramP.add(p21,null);
	    paramP.add(p22,null);
	    paramP.add(p23,null);
	    paramP.add(p24,null);
	    paramP.add(p25,null);
	    paramP.setBackground(PatientRx.bkgColor);
	    paramP.setBorder(FrameUtilities.getGroupBoxBorder(
	    		resources.getString("param.border"+lang),foreColor));
	//==================================================================================

    	JPanel p31 = new JPanel();
    	p31.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.matanodL"+lang));
    	p31.add(label);
    	p31.add(matanodCb);
    	p31.setBackground(PatientRx.bkgColor);

    	JPanel p32 = new JPanel();
    	p32.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.uanodL"+lang));
    	p32.add(label);
    	p32.add(uanodCb);
    	p32.setBackground(PatientRx.bkgColor);

    	JPanel p33 = new JPanel();
    	p33.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.rippleL"+lang));
    	p33.add(label);
    	p33.add(rippleCb);
    	p33.setBackground(PatientRx.bkgColor);

    	JPanel p34 = new JPanel();
    	p34.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	label=new JLabel(resources.getString("MainForm.gui.filtrareL"+lang));
    	p34.add(label);
    	p34.add(filtrareTf);
    	p34.setBackground(PatientRx.bkgColor);

	//AUX data panel
	    JPanel auxP=new JPanel();
	    BoxLayout bl3 = new BoxLayout(auxP,BoxLayout.Y_AXIS);
	    auxP.setLayout(bl3);
	    auxP.add(p31,null);
	    auxP.add(p32,null);
	    auxP.add(p33,null);
	    auxP.add(p34,null);
	    auxP.setBackground(PatientRx.bkgColor);
	    auxP.setBorder(FrameUtilities.getGroupBoxBorder(
	    		resources.getString("aux.border"+lang),foreColor));
	//==================================================================================

    	JPanel pa = new JPanel();
    	pa.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	pa.add(paramP);
    	pa.add(auxP);
    	pa.setBackground(PatientRx.bkgColor);
    //=========== param + aux
    	JPanel p44 = new JPanel();
    	p44.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	buttonName = resources.getString("main.button.add"+lang);
		buttonToolTip = resources.getString("main.button.add.toolTip"+lang);
		buttonIconName = resources.getString("img.insert");
		button = FrameUtilities.makeButton(buttonIconName, ADD_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.add.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	p44.add(button);
    	//p44.add(mf.addB);
    	p44.setBackground(PatientRx.bkgColor);

	    JPanel tat=new JPanel();
	    BoxLayout bl4 = new BoxLayout(tat,BoxLayout.Y_AXIS);
	    tat.setLayout(bl4);
	    tat.add(unitP,null);
	    tat.add(dateP,null);
	    tat.add(pc,null);
	    tat.add(pa,null);
	    tat.add(p44,null);
		tat.setBackground(PatientRx.bkgColor);
		//END DATA PANEL
		//========================
		String str=Convertor.intToString(IDPATIENT);
		contorTf.setText(str);
		
		JPanel p100 = new JPanel();
		p100.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
    	buttonName = resources.getString("main.button.delete"+lang);
		buttonToolTip = resources.getString("main.button.delete.toolTip"+lang);
		buttonIconName = resources.getString("img.delete");
		button = FrameUtilities.makeButton(buttonIconName, DELETE_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.delete.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	p100.add(button);
    	buttonName = resources.getString("main.button.deleteAll"+lang);
		buttonToolTip = resources.getString("main.button.deleteAll.toolTip"+lang);
		buttonIconName = resources.getString("img.delete.all");
		button = FrameUtilities.makeButton(buttonIconName, DELETEALL_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.deleteAll.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	p100.add(button);
    	buttonName = resources.getString("main.button.report"+lang);
		buttonToolTip = resources.getString("main.button.report.toolTip"+lang);
		buttonIconName = resources.getString("img.report");
		button = FrameUtilities.makeButton(buttonIconName, REPORT_COMMAND,
				buttonToolTip, buttonName, this, this);
		mnemonic = (Character) resources.getObject("main.button.report.mnemonic"+lang);
		button.setMnemonic(mnemonic.charValue());
    	p100.add(button);
    	p100.add(showPlot);
    	label=new JLabel(resources.getString("records.label"+lang));
    	//p100.add(label);/////////////////////////////////////
    	//p100.add(contorTf);//////////////////////////////
    	p100.setBackground(PatientRx.bkgColor);
    	
		suportSp.setPreferredSize(tableDimension);
		//==================================
		JScrollPane scrollPane = new JScrollPane(dbagent.getMainTable());
		dbagent.getMainTable().setFillsViewportHeight(true);
		suportSp.add(scrollPane);
		//=========================
		
		JPanel p5P = new JPanel();
		BoxLayout blp5P = new BoxLayout(p5P, BoxLayout.Y_AXIS);
		p5P.setLayout(blp5P);
		p5P.setBorder(FrameUtilities.getGroupBoxBorder(
				resources.getString("records.border"+lang),
				PatientRx.foreColor));
		p5P.add(orderP);////////////////////////////////////
		p5P.add(suportSp);
		p5P.add(p100);
		p5P.setBackground(PatientRx.bkgColor);
		
		//============		
        tabs.add(resources.getString("MainForm.tab.data"+lang), tat);
        tabs.add(resources.getString("MainForm.tab.results"+lang), p5P);
		return tabs;
	}
	
	/**
	 * Create the menu bar
	 * @param resources resources
	 * @return the menu bar
	 */
	private JMenuBar createMenuBar(ResourceBundle resources)
    {

        // create the menus
        JMenuBar menuBar = new JMenuBar();

        String label;
        Character mnemonic;
        ImageIcon img;
		String imageName = "";
		
        // first the file menu
        label = resources.getString("menu.file");
        mnemonic = (Character) resources.getObject("menu.file.mnemonic");
        JMenu fileMenu = new JMenu(label, true);
        fileMenu.setMnemonic(mnemonic.charValue());

        imageName = resources.getString("img.report");
		img = FrameUtilities.getImageIcon(imageName, this);
        label = resources.getString("menu.file.report"+lang);
        mnemonic = (Character) resources.getObject("menu.file.report.mnemonic"+lang);
        JMenuItem reportItem = new JMenuItem(label, mnemonic.charValue());
        reportItem.setActionCommand(REPORT_COMMAND);
        reportItem.addActionListener(this);
        reportItem.setIcon(img);
        reportItem.setToolTipText(resources.getString("menu.file.report.toolTip"+lang));
        fileMenu.add(reportItem);

		fileMenu.addSeparator();

		//==============
		label = resources.getString("menu.file.lang"+lang);
		mnemonic = (Character) resources.getObject("menu.file.lang.mnemonic"+lang);
		JMenuItem langItem = new JMenuItem(label, mnemonic.charValue());
		langItem.setActionCommand(LANGUAGE_COMMAND);
		langItem.addActionListener(this);
		langItem.setToolTipText(resources.getString("menu.file.lang.toolTip"+lang));
		fileMenu.add(langItem);
		//=================
		
		imageName = resources.getString("img.close");
		img = FrameUtilities.getImageIcon(imageName, this);
		label = resources.getString("menu.file.exit"+lang);
		mnemonic = (Character) resources.getObject("menu.file.exit.mnemonic"+lang);
		JMenuItem exitItem = new JMenuItem(label, mnemonic.charValue());
		exitItem.setActionCommand(EXIT_COMMAND);
		exitItem.addActionListener(this);
		exitItem.setIcon(img);
		exitItem.setToolTipText(resources.getString("menu.file.exit.toolTip"+lang));
		fileMenu.add(exitItem);
		
        // then the help menu
        label = resources.getString("menu.help");
        mnemonic = (Character) resources.getObject("menu.help.mnemonic");
        JMenu helpMenu = new JMenu(label);
        helpMenu.setMnemonic(mnemonic.charValue());

        label = resources.getString("menu.help.LF");
		mnemonic = (Character) resources.getObject("menu.help.LF.mnemonic");
		JMenuItem lfItem = new JMenuItem(label, mnemonic.charValue());
		lfItem.setActionCommand(LOOKANDFEEL_COMMAND);
		lfItem.addActionListener(this);
		lfItem.setToolTipText(resources.getString("menu.help.LF.toolTip"));
		
		if(showLAF){
			helpMenu.add(lfItem);
			helpMenu.addSeparator();
		}

        imageName = resources.getString("img.about");
		img = FrameUtilities.getImageIcon(imageName, this);
        label = resources.getString("menu.help.info"+lang);
        mnemonic = (Character) resources.getObject("menu.help.info.mnemonic"+lang);
        JMenuItem infoItem = new JMenuItem(label, mnemonic.charValue());
        infoItem.setActionCommand(INFO_COMMAND);
        infoItem.addActionListener(this);
		infoItem.setIcon(img);
		infoItem.setToolTipText(resources.getString("menu.help.info.toolTip"+lang));
        helpMenu.add(infoItem);
        
		imageName = resources.getString("img.about");
		img = FrameUtilities.getImageIcon(imageName, this);
		label = resources.getString("menu.help.about"+lang);
		mnemonic = (Character) resources.getObject("menu.help.about.mnemonic"+lang);
		JMenuItem aboutItem = new JMenuItem(label, mnemonic.charValue());
		aboutItem.setActionCommand(ABOUT_COMMAND);
		aboutItem.addActionListener(this);
		aboutItem.setIcon(img);
		aboutItem.setToolTipText(resources.getString("menu.help.about.toolTip"+lang));		
        helpMenu.add(aboutItem);
        //helpMenu.addSeparator();        

        // finally, glue together the menu and return it
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }
	
	/**
	 * Initialize database
	 */
	private void performQueryDb(){
		dbagent.init();
		orderbyS = dbagent.getPrimaryKeyColumnName();//mainTablePrimaryKey;
		
		JTable mainTable = dbagent.getMainTable();
		ListSelectionModel rowSM = mainTable.getSelectionModel();
		rowSM.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		
		IDPATIENT = mainTable.getRowCount();// last ID
		
		if (mainTable.getRowCount() > 0) {
			// always display last row!
			mainTable.setRowSelectionInterval(mainTable.getRowCount() - 1,
					mainTable.getRowCount() - 1); // last ID
			
			Integer intg=(Integer)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
			String maxUniqueIDs=intg.toString();//(String)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
			maxUniqueID=Convertor.stringToInt(maxUniqueIDs);

			//retrieveInformation();				
		} 
		/*try {
			//String datas = resources.getString("data.load");
			//String currentDir = System.getProperty("user.dir");
			//String file_sep = System.getProperty("file.separator");
			//String opens = currentDir + file_sep + datas;
			//String dbName = patientDB;
			//opens = opens + file_sep + dbName;

			//int dummy=1;
			String s = "select * from " + patientTable+//" where Unique_ID = "+dummy;
			" order by Unique_ID";

			Connection con1 = DBConnection.getDerbyConnection(opens, "", "");
			DBOperation.select(s, con1);

			asp = new AdvancedSelectPanel();
			suportSp.add(asp, BorderLayout.CENTER);

			JTable mainTable = asp.getTab();

			ListSelectionModel rowSM = mainTable.getSelectionModel();
			rowSM.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
			//rowSM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			//rowSM.addListSelectionListener(new ListSelectionListener() {
			//	public void valueChanged(ListSelectionEvent e) {
				//	if (e.getValueIsAdjusting())
					//	return; // Don't want to handle intermediate selections

					//updateDetailTable();
				//}
			//});

			IDPATIENT = mainTable.getRowCount();// last ID
									
			if (mainTable.getRowCount() > 0) {
				// always display last row!
				mainTable.setRowSelectionInterval(mainTable.getRowCount() - 1,
						mainTable.getRowCount() - 1); // last ID
				
				Integer intg=(Integer)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
				String maxUniqueIDs=intg.toString();//(String)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
				maxUniqueID=Convertor.stringToInt(maxUniqueIDs);

				//retrieveInformation();				
			} 

			if (con1 != null)
				con1.close();

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Return the String representing the date and time as today
	 * @return the result
	 */
	private String dateTimeToday(){
		String s = "";
		
		Calendar cal=Calendar.getInstance();
		int iday=cal.get(Calendar.DAY_OF_MONTH);
		int imonth=cal.get(Calendar.MONTH)+1;//0 index; January=0
		int iyear=cal.get(Calendar.YEAR);
		
		int ihour=cal.get(Calendar.HOUR_OF_DAY);
		int imin=cal.get(Calendar.MINUTE);
		int isec=cal.get(Calendar.SECOND);
		
		String idayS=Convertor.intToString(iday);
		if (iday<10)
			idayS="0"+idayS;

		String imonthS=Convertor.intToString(imonth);
		if (imonth<10)
			imonthS="0"+imonthS;

		String iyearS=Convertor.intToString(iyear);
		
		String ihourS=Convertor.intToString(ihour);
		if (ihour<10)
			ihourS="0"+ihourS;
		String iminS=Convertor.intToString(imin);
		if (imin<10)
			iminS="0"+iminS;
		String isecS=Convertor.intToString(isec);
		if (isec<10)
			isecS="0"+isecS;
		
		s= iyearS 
		+"-"+
		imonthS+"-"+
		idayS+", "+ihourS+":"+iminS+":"+isecS;
		
		return s;
	}
	
	/**
	 * Set date as today
	 */
	private void today() {

		String s = null;
		//TimeUtilities.today();
		TimeUtilities todayTu = new TimeUtilities();
		s = Convertor.intToString(todayTu.getDay());//TimeUtilities.iday);
		if (todayTu.getDay() < 10)//TimeUtilities.iday < 10)
			s = "0" + s;
		dayCb.setSelectedItem((Object) s);
		s = Convertor.intToString(todayTu.getMonth());//TimeUtilities.imonth);
		if (todayTu.getMonth() < 10)//TimeUtilities.imonth < 10)
			s = "0" + s;
		monthCb.setSelectedItem((Object) s);
		s = Convertor.intToString(todayTu.getYear());//TimeUtilities.iyear);
		yearTf.setText(s);
	}
	
	/**
	 * Changing the look and feel can be done here. Also display some gadgets.
	 */
	private void lookAndFeel() {
		setVisible(false);
		new ScanDiskLFGui(this);
	}
	
	/**
	 * Shows the about window!
	 */
	private void about() {
		new AboutFrame(this);
	}
	
	/**
	 * Setting up actions!
	 * @param e e
	 */	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		command = e.getActionCommand();
		if (command.equals(ABOUT_COMMAND)) {
			about();
		} else if (command.equals(EXIT_COMMAND)) {
			attemptExit();
		} else if (command.equals(LOOKANDFEEL_COMMAND)) {
			lookAndFeel();
		} else if (command.equals(TODAY_COMMAND)) {
			today();
		} else if (command.equals(SAVEUNIT_COMMAND)) {
			saveUnit();
		} else if (command.equals(LOADUNIT_COMMAND)) {
			loadUnit();
		} else if (command.equals(ADD_COMMAND)) {
			add();
		} else if (command.equals(INFO_COMMAND)) {
			info();
		} else if (command.equals(REPORT_COMMAND)) {
			report();
		} else if (command.equals(DELETE_COMMAND)) {
			delete();
		} else if (command.equals(DELETEALL_COMMAND)) {
			deleteAll();
		} else if (command.equals(LANGUAGE_COMMAND)) {
			language();
		}
	}
	
	/**
	 * JCombobox actions are set here
	 */
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == orderbyCb) {
			sort();
		}
	}
	
	/**
	 * Sorts data from main table
	 */
	private void sort() {
		orderbyS = (String) orderbyCb.getSelectedItem();
		dbagent.performSelection(orderbyS);
	}
	
	/**
	 * Go to set the language
	 */
	private void language(){
		new Language(this);
	}
	
	/**
	 * Return the KAP from XRay spectra in uGy * m2
	 * @return the result
	 */
	private double getSpectraDAP()
	{
		double dap=0.0;
		validateComputationMode();
		if (!saveBoo){return dap;}
		
       	XRay.reset();//HERE WE RESET!!!!!!!!!!!!!!!!!!!!!!
       	int is=1;//icalcCb.getSelectedIndex();
       	XRay.ICALC=is;
       	is=matanodCb.getSelectedIndex();
       	XRay.ianod=is;
       	String s=(String)rippleCb.getSelectedItem();
       	is=Convertor.stringToInt(s);
       	XRay.iripple=is;

		//XRay.PLANE=20.0;
		String filesname="AL";
		XRay.readAttCoef(filesname,1);
		XRay.TMM[0]=filtrared;

		XRaySpectrum xrs=new XRaySpectrum(ud,filtrared,uanodd);
		double kexit=1.0E-03*xrs.getAirKerma();//in mGy/mAs, orig:in microGy/mAs
//System.out.println(" k mgy/mas "+kexit);
		kexit=kexit*masd/1.0E-03;//=>back in uGy
		double exitz=75.0;//at 75cm

    	if(fsdd!=0.0)
    		dap=kexit*exitz*exitz/(fsdd*fsdd);
    	else
    		dap=kexit;
		dap=dap*campxd*campyd/10000.0;//cm2->m2
		return dap;//uGy * m2
	}

	/**
	 * Based on SRS78 database for simulating XRay spectra, 
	 * checks if all is set correctly for further computation.
	 */
	public void validateComputationMode()
	{
		int itemp=0;
		//double d=0.0;
		saveBoo=true;
		itemp=1;
		XRay.ICALC=itemp;
		itemp=matanodCb.getSelectedIndex();
		XRay.ianod=itemp;
		itemp=rippleCb.getSelectedIndex();
		XRay.iripple=itemp;
		String s="";

		if (XRay.ICALC==0)//old, never here
		{
			s=(String)uanodCb.getSelectedItem();
			itemp=Convertor.stringToInt(s);//isi.intValue();

			if (itemp<7 || itemp>27)
				saveBoo=false;

			s=(String)uCb.getSelectedItem();
			itemp=Convertor.stringToInt(s);//isi.intValue();

			if (itemp<30 || itemp>150)
				saveBoo=false;

			//======================================================
			if (XRay.ianod!=0 || XRay.iripple!=0)
				saveBoo=false;//not allowed anything but W (ianod 0) and ripple 0!!
			//=====================================================
		}
		else if (XRay.ICALC==1)//always
		{
			if (XRay.ianod==0)//W
			{
				s=(String)uanodCb.getSelectedItem();
				itemp=Convertor.stringToInt(s);//isi.intValue();

				if (itemp<6 || itemp>22)
					saveBoo=false;

				s=(String)uCb.getSelectedItem();
				itemp=Convertor.stringToInt(s);//isi.intValue();
				int icav=itemp;
				if (itemp<30 || itemp>150)
					saveBoo=false;

				//alloewd ripple
				if (XRay.iripple!=0)
				{
					if (icav!=55 && icav!=60 && icav!=65 && icav!=70 && icav!=75
					&& icav!=80 && icav!=85 && icav!=90)
					{
						saveBoo=false;
					}
				}

			}
			else//Mo,Rh
			{
				s=(String)uanodCb.getSelectedItem();
				itemp=Convertor.stringToInt(s);

				if (itemp<9 || itemp>23)
					saveBoo=false;

				s=(String)uCb.getSelectedItem();
				itemp=Convertor.stringToInt(s);//isi.intValue();

				if (itemp<25 || itemp>32)
					saveBoo=false;

				//======================================================
				if (XRay.iripple!=0)
					saveBoo=false;//not allowed anything but ripple 0!!
				//=====================================================
			}
		}

		if (!saveBoo)
		{
		    String title =resources.getString("number.error.title"+lang);
			String message =resources.getString("dialog.ripple"+lang);
		    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		}

	}

	/*private void selectTable(){
		try {
			
			String datas = resources.getString("data.load");
			String currentDir = System.getProperty("user.dir");
			String file_sep = System.getProperty("file.separator");
			String opens = currentDir + file_sep + datas;
			String dbName = patientDB;
			opens = opens + file_sep + dbName;

			String s = "select * from " + patientTable+" order by Unique_ID";

			Connection con1 = DBConnection.getDerbyConnection(opens, "", "");
			con1.setAutoCommit(false);
			
			DBOperation.select(s, con1);
			suportSp.remove(asp);//remove first
			asp = new AdvancedSelectPanel();
			suportSp.add(asp, BorderLayout.CENTER);

			JTable mainTable = asp.getTab();

			ListSelectionModel rowSM = mainTable.getSelectionModel();
			rowSM.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
			//rowSM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			IDPATIENT = mainTable.getRowCount();
						
			if (mainTable.getRowCount() > 0) {
				// always display last row!
				mainTable.setRowSelectionInterval(mainTable.getRowCount() - 1,
						mainTable.getRowCount() - 1); // last ID
				//populate some field
				Integer intg=(Integer)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
				String maxUniqueIDs=intg.toString();//(String)mainTable.getValueAt(mainTable.getRowCount() - 1, 0);
				maxUniqueID=Convertor.stringToInt(maxUniqueIDs);
				
				//retrieveInformation();
				
			} else {
				maxUniqueID=0;//reset counter
			}
						
			con1.commit();
			
			if (con1 != null)
				con1.close();
			
			String str=Convertor.intToString(IDPATIENT);
			contorTf.setText(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//===========
		validate();
	}*/
	
	/**
	 * Save patient data
	 */
	private void add(){
		boolean b=true;
        boolean dapb=false;
        String numes=numeTf.getText();//numes=splitString(numes);
        String prenumes=prenumeTf.getText();//prenumes=splitString(prenumes);
        String cnps=cnpTf.getText();//cnps=splitString(cnps);
        String examens=examenTf.getText();//examens=splitString(examens);
        String sarcinas=(String)sarcinaCb.getSelectedItem();
        //==================================================
        String campxs=campxTf.getText();
        String campys=campyTf.getText();
        String fsds=dfocusfilmTf.getText();
        String dpacientfilms=dpacientfilmTf.getText();
        String us=(String)uCb.getSelectedItem();
        String mas=maTf.getText();
        String ts=tTf.getText();
        String mass=masTf.getText();
        String daps=dapTf.getText();
        //String matanods=(String)matanodCb.getSelectedItem();
        String uanods=(String)uanodCb.getSelectedItem();
        //String ripples=(String)rippleCb.getSelectedItem();
        String filtrares=filtrareTf.getText();
        //String esds="";
        //String esdrates="";

        //get measurement date
		boolean nulneg = false;
		int year = 0;
		int day =0;
		int month =0;
		
		try {
			day = Convertor.stringToInt((String) dayCb.getSelectedItem());
			month = Convertor.stringToInt((String) monthCb.getSelectedItem());
			year = Convertor.stringToInt(yearTf.getText());
			if (year < 0 || month < 0 || day < 0)
				nulneg = true;
		} catch (Exception e) {
			String title = resources.getString("number.error.title"+lang);
			String message = resources.getString("number.error"+lang);
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
			return;
		}
		
		if (nulneg) {
			String title = resources.getString("number.error.title"+lang);
			String message = resources.getString("number.error"+lang);
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//TimeUtilities.setDate(day, month, year);
		//String excaminationDate=TimeUtilities.formatDate();
		TimeUtilities tu = new TimeUtilities(day, month, year);
		String excaminationDate = tu.formatDate();
		////======================================
        /*
         * protected double filtrared=0.0;
	protected double masd=0.0;
	protected double fsdd=0.0;
    protected double campxd=0.0;
    protected double campyd=0.0;
    
    protected double ud=0.0;	
	protected double uanodd=0.0;
         */
        campxd=0.0;
        campyd=0.0;
        fsdd=0.0;//here, focus field distance
        double dpacientfilmd=0.0;
        ud=Convertor.stringToDouble(us);
        double mad=0.0;
        double td=0.0;
        masd=0.0;
        double dapd=0.0;
        uanodd=Convertor.stringToDouble(uanods);
        //double rippled=Convertor.stringToDouble(ripples);
        filtrared=0.0;
        double esdd=0.0;
        double esdrated=0.0;

        try
        {
		    campxd=Convertor.stringToDouble(campxs);if (campxd<=0.0)b=false;
		    campyd=Convertor.stringToDouble(campys);if (campyd<=0.0)b=false;
		    fsdd=Convertor.stringToDouble(fsds);if (fsdd<=0.0)b=false;
		    dpacientfilmd=Convertor.stringToDouble(dpacientfilms);if (dpacientfilmd<=0.0)b=false;

		    td=Convertor.stringToDouble(ts);if (td<=0.0)b=false;//must be done

		    if (mass.compareTo("")!=0)//given
		    {
				masd=Convertor.stringToDouble(mass);if (masd<=0.0)b=false;
				mad=1000.0*masd/td;if (mad<=0.0)b=false;
			}
			else
			{
				mad=Convertor.stringToDouble(mas);if (mad<=0.0)b=false;
				//td=>in ms; mad in mA
				masd=mad*td/1000.0;if (masd<=0.0)b=false;
			}

		    if (daps.compareTo("")!=0)//given
		    {
				dapd=Convertor.stringToDouble(daps);if (dapd<=0.0)b=false;
			}
			else
			{
				dapb=true;//must compute
				filtrared=Convertor.stringToDouble(filtrares);if (filtrared<=0.0)b=false;
			}
		}
		catch(Exception e)
		{
			b=false;
		    String title =resources.getString("number.error.title"+lang);
		    String message =resources.getString("number.error"+lang);
		    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		    return;
		}

		if(!b)
		{
		    String title =resources.getString("number.error.title"+lang);
		    String message =resources.getString("number.error"+lang);
		    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		    return;
	    }

		if(dapb)
		{
			dapd=getSpectraDAP();
			if (!saveBoo)return;
		}

		esdd=10000.0*dapd/(campxd*campyd);//at film cm=>m
		//dpacientfilmd
    	if(dpacientfilmd!=0.0)//here, it is focus-patient entrance!!!
    		esdd=esdd*fsdd*fsdd/(dpacientfilmd*dpacientfilmd);
		esdrated=1000.0*esdd/td;//ms=>s

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumFractionDigits(2);//default e 2 oricum!!
    	nf.setMaximumFractionDigits(2);//default e 2 oricum!!
    	nf.setGroupingUsed(false);//no 4,568.02 but 4568.02

    	String campxys=campxs+" x "+campys;
    	String madf=nf.format(mad);
    	String masdf=nf.format(masd);
    	String dapdf=nf.format(dapd);
    	String esddf=nf.format(esdd);
    	String esdratedf=nf.format(esdrated);
    	//===========================================================
    	String[] data = new String[dbagent.getUsefullColumnCount()];
		int kCol = 0;
		data[kCol] = prenumes+" "+numes;
		kCol++;
		data[kCol] = cnps;
		kCol++;
		data[kCol] = examens;
		kCol++;
		data[kCol] = sarcinas;
		kCol++;
		data[kCol] = excaminationDate;
		kCol++;
		data[kCol] = campxys;
		kCol++;
		data[kCol] = fsds;
		kCol++;
		data[kCol] = us;
		kCol++;
		data[kCol] = madf;
		kCol++;
		data[kCol] = ts;
		kCol++;
		data[kCol] = masdf;
		kCol++;
		data[kCol] = dapdf;
		kCol++;
		data[kCol] = esddf;
		kCol++;
		data[kCol] = esdratedf;
		kCol++;
		
		dbagent.insert(data);
		
		dbagent.performSelection(orderbyS);
		
    	/*try {
			
			// prepare db query data
			String datas = resources.getString("data.load");
			String currentDir = System.getProperty("user.dir");
			String file_sep = System.getProperty("file.separator");
			String opens = currentDir + file_sep + datas;
			String dbName = patientDB;
			opens = opens + file_sep + dbName;
			// make a connection
			Connection con1 = DBConnection.getDerbyConnection(opens, "", "");

			PreparedStatement psInsert = null;
			//-------------------------
			psInsert = con1.prepareStatement("insert into "
					+ patientTable + " values " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int id = maxUniqueID + 1;//Unique ID
			psInsert.setString(1, Convertor.intToString(id));
			psInsert.setString(2, prenumes+" "+numes);//surname+name
			psInsert.setString(3, cnps);
			psInsert.setString(4, examens);
			psInsert.setString(5, sarcinas);
			psInsert.setString(6, excaminationDate);
			psInsert.setString(7, campxys);
			psInsert.setString(8, fsds);
			psInsert.setString(9, us);
			psInsert.setString(10, madf);
			psInsert.setString(11, ts);					
			psInsert.setString(12, masdf);
			psInsert.setString(13, dapdf);
			psInsert.setString(14, esddf);
			psInsert.setString(15, esdratedf);
			psInsert.executeUpdate();
			
			//---------
			if (psInsert != null)
				psInsert.close();
			if (con1 != null)
				con1.close();
						
			selectTable();
			
		}  catch (Exception ex) {
			ex.printStackTrace();
			return;
		}*/
		
		tabs.setSelectedIndex(1);
	}
	
	/**
	 * Delete patient data
	 */
	private void delete(){
		int[] selIDs= new int[0];
		JTable aspTable = dbagent.getMainTable();//
		int[] selRows = aspTable.getSelectedRows();
		if (selRows.length==0){
			return;//nothing to delete
		}
		
		selIDs=new int[selRows.length];
		for (int i=0;i<selRows.length; i++){
			selIDs[i] = (Integer) aspTable.getValueAt(selRows[i], 
					dbagent.getPrimaryKeyColumnIndex());//0);
			dbagent.delete(Convertor.intToString(selIDs[i]));
		}
		dbagent.performSelection(orderbyS);
		
		/*String datas = resources.getString("data.load");
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas;
		String dbName = patientDB;
		opens = opens + file_sep + dbName;
		
		try {
			// prepare db query data			
			Connection con1 = DBConnection.getDerbyConnection(opens, "", "");

			//----------------------------------
			JTable aspTable = asp.getTab();

			//int selID = 0;// NO ZERO ID
			//int selRow = aspTable.getSelectedRow();
			int[] selRows = aspTable.getSelectedRows();
			if (selRows.length==0){
				//System.out.println("FIRE!!!!!!!!!");
				
				if (con1 != null)
						con1.close();

				return;//nothing to delete
			}
			//if (selRow != -1) {
			//	selID = (Integer) aspTable.getValueAt(selRow, 0);
			//	IDLINK1=selID;//@@
			//} else {
			//	if (con1 != null)
			//		con1.close();
				
			//	return;// nothing to delete
			//}
			//---------------------------------
			selIDs=new int[selRows.length];
			for (int i=0;i<selRows.length; i++){
				selIDs[i] = (Integer) aspTable.getValueAt(selRows[i], 0);
			}
			//-----------------
			Statement s=null;
			ResultSet res=null;
			//for (int i=0;i<selRows.length; i++){
			s = con1.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);
			res = s.executeQuery("SELECT * FROM " + patientTable);
			//PreparedStatement psUpdate = null;
			while (res.next()) {
				int id = res.getInt("Unique_ID");//("ID");
				//if (id == selID) {
				for (int i=0;i<selRows.length; i++){
				if (id == selIDs[i]) {
					res.deleteRow();
				} //else if (id > selID) {
										
				//}
				}//end for
			}
			//}//==================
			selectTable();
			
			if (res != null)
				res.close();
			if (s != null)
				s.close();
			//if (psUpdate != null)
			//	psUpdate.close();
			if (con1 != null)
				con1.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}*/
	}
	
	/**
	 * Delete all records
	 */
	private void deleteAll(){
		
		dbagent.deleteAll();
		dbagent.performSelection(orderbyS);
		
		/*String datas = resources.getString("data.load");
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas;
		String dbName = patientDB;
		opens = opens + file_sep + dbName;
		
		try {
			// prepare db query data			
			Connection con1 = DBConnection.getDerbyConnection(opens, "", "");

			//----------------------------------
			//JTable aspTable = asp.getTab();
			//int[] selRows = aspTable.getSelectedRows();
			//if (selRows.length==0){
				//System.out.println("FIRE!!!!!!!!!");
				
			//	if (con1 != null)
			//			con1.close();

			//	return;//nothing to delete
			//}
			
			String s = "delete from "+patientTable;
			PreparedStatement pstmt = con1.prepareStatement(s);
			pstmt.executeUpdate();
			   
			if (pstmt != null)
				pstmt.close();
			if (con1 != null)
				con1.close();
			
			selectTable();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}*/
	}
	
	/**
	 * Display some useful hints.
	 */
	private void info(){
		new DAPHelp(this);
	}
	
	/**
	 * Generate report
	 */
	private void report(){
		//Connection con1 = null;

		String datas = resources.getString("data.load");
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas;
		
		//String reportPath=opens;
		
		//String dbName = resources.getString("main.db");// "radqc";
		//opens = opens + file_sep + dbName;
		//String protocol = "jdbc:derby:";
		try {			
			//con1 = DBConnection.getDerbyConnection(opens, "", "");
			//reportPath=reportPath+file_sep + "report5.jrxml";
			//JasperReport jr = JasperCompileManager.compileReport(reportPath);
			//JasperPrint jp = JasperFillManager.fillReport(jr, null, con1);
			//JasperViewer.viewReport(jp);
			//JasperViewer.viewReport(jp,false);//to not close app on exit!!
			
			//=================================================================
			
			// dynamic report and datasource
			/*
			 ( Unique_ID integer, "
				+ "Patient VARCHAR(200), CNP VARCHAR(100), "
				+ "Examination VARCHAR(200), "
				+ "Pregnancy VARCHAR(50), "
				+ "Date VARCHAR(100), " +

				"Field VARCHAR(100), " +
				"FFD DOUBLE PRECISION"+
				", U_kV DOUBLE PRECISION, "+
				"I_mA DOUBLE PRECISION, "+
				"T_ms DOUBLE PRECISION, " +
				"IxT_mAs DOUBLE PRECISION, " +
				"KAP_uGyxm2 DOUBLE PRECISION, " +
				"ESAK_uGy DOUBLE PRECISION, " +
				"Rate_uGyPerSec DOUBLE PRECISION" +
			 */
			String s="select Patient, CNP, Examination, Pregnancy, Field, " +
					"FFD, U_kV, I_mA, T_ms, IxT_mAs, KAP_uGyxm2, ESAK_uGy, Rate_uGyPerSec" +
					" from " + patientTable;//+" where Unique_ID = "+selID;
			PreparedStatement stmt = patientdbcon.prepareStatement(s);//con1.prepareStatement(s);
			ResultSet resultSet = stmt.executeQuery();
			//resultSet.next();
			String medicalUnitS=unitateTf.getText();
			String addressS=adresaTf.getText();
			String contactS=contactTf.getText();
			String deviceS=aparatTf.getText();
			String notesS=notesTf.getText();						
			//=======================================================			
			
			JasperReportBuilder report = DynamicReports.report();
			
			// styles
			StyleBuilder boldStyle = DynamicReports.stl.style().bold();
			StyleBuilder boldCentered = 
				DynamicReports.stl.style(boldStyle)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
			StyleBuilder columnHeaderStyle = DynamicReports.stl.
				style(boldCentered).setBorder(DynamicReports.stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY)
				.setFontSize(9);
				;
			
			StyleBuilder columnStyle = DynamicReports.stl.
			style().setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(8);//SansSerif 10 is default
			
			//page orientation
			report.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE);
			

			// add title plus some useful information
			
			// get image from resources..company logo or whatever			
		    opens=currentDir+file_sep+"header.png";
			InputStream stream = new FileInputStream(opens); 
				//PatientRx.class.getResourceAsStream("/resources/purchase_order.png");
			ImageBuilder img = DynamicReports.cmp.image(stream)
			.setFixedDimension(80, 80).setStyle(DynamicReports.stl.style()
			.setHorizontalAlignment(HorizontalAlignment.LEFT));
			//date and time
			String datetime=dateTimeToday();
			//title
			TextFieldBuilder<String> title = DynamicReports.cmp
				.text(resources.getString("report.title"+lang)+"\n"+datetime);//"Raportare pacienti");
			title.setStyle(boldCentered)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);//RIGHT);
			
			FillerBuilder filler = DynamicReports.cmp.filler()//nice line
				.setStyle(DynamicReports.stl.style()
				.setTopBorder(DynamicReports.stl.pen2Point()))
				.setFixedHeight(2);
			
			report.title(img,title,DynamicReports.cmp.verticalGap(10),filler,
					DynamicReports.cmp.verticalList(
							DynamicReports.cmp.text(resources.getString("report.MU"+lang)+medicalUnitS+
									resources.getString("report.address"+lang)+addressS).setStyle(boldStyle),
							DynamicReports.cmp.text(resources.getString("report.contact"+lang)+contactS+
									resources.getString("report.device"+lang)+deviceS+
									resources.getString("report.notes"+lang)+notesS).setStyle(boldStyle),
							filler,DynamicReports.cmp.verticalGap(10)
					
					)		
			
			);
			// end title plus other info
			
			// add tables
			// add columns
			TextColumnBuilder<String> patientColumn = Columns.column(
					resources.getString("report.patient"+lang), "Patient", DynamicReports.type.stringType());
			TextColumnBuilder<String> cnpColumn = Columns.column(
					resources.getString("report.NIN"+lang), "CNP", DynamicReports.type.stringType());
			TextColumnBuilder<String> examColumn = Columns.column(
					resources.getString("report.exam"+lang), "Examination", DynamicReports.type.stringType());
			TextColumnBuilder<String> pregnancyColumn = Columns.column(
					//"Pregnancy"+"\n"+"number", "Pregnancy", DynamicReports.type.stringType());
					resources.getString("report.pregnancy"+lang)+"\n"+
					resources.getString("report.yesno"+lang), "Pregnancy", DynamicReports.type.stringType());
			TextColumnBuilder<String> fieldColumn = Columns.column(
					resources.getString("report.field"+lang)+"\n"+"[cm x cm]", "Field", DynamicReports.type.stringType());
			TextColumnBuilder<String> ffdColumn = Columns.column(
					resources.getString("report.distance1"+lang)+"\n"+
					resources.getString("report.distance2"+lang), "FFD", DynamicReports.type.stringType());
			TextColumnBuilder<String> uColumn = Columns.column(
					"U"+"\n"+"[kV]", "U_kV", DynamicReports.type.stringType());
			TextColumnBuilder<String> iColumn = Columns.column(
					"I"+"\n"+"[mA]", "I_mA", DynamicReports.type.stringType());
			TextColumnBuilder<String> tColumn = Columns.column(
					"T"+"\n"+"[ms]", "T_ms", DynamicReports.type.stringType());
			TextColumnBuilder<String> ixtColumn = Columns.column(
					"I x T"+"\n"+"[mAs]", "IxT_mAs", DynamicReports.type.stringType());
			TextColumnBuilder<BigDecimal> kapColumn = Columns.column(
					resources.getString("report.KAP"+lang)+"\n"+"[uGy x m^2]", "KAP_uGyxm2", new MyBigDecimalType());//DynamicReports.type.stringType());
			TextColumnBuilder<BigDecimal> esakColumn = Columns.column(
					resources.getString("report.dose1"+lang)+"\n"+
					resources.getString("report.dose2"+lang), "ESAK_uGy", new MyBigDecimalType());//DynamicReports.type.bigDecimalType());
			TextColumnBuilder<String> debitColumn = Columns.column(
					resources.getString("report.doserate1"+lang)+"\n"+
					resources.getString("report.doserate2"+lang), "Rate_uGyPerSec", DynamicReports.type.stringType());
			
			// row num
			TextColumnBuilder<Integer> rowNumColumn = Columns.reportRowNumberColumn(
					resources.getString("report.no1"+lang)+"\n"+resources.getString("report.no2"+lang)).
			setFixedColumns(3).setHorizontalAlignment(HorizontalAlignment.CENTER);
			//adding columns and their relative width
			report.columns(rowNumColumn,patientColumn.setWidth(10), cnpColumn.setWidth(8), examColumn.setWidth(13), pregnancyColumn.setWidth(7),
					fieldColumn.setWidth(7), ffdColumn.setWidth(9), uColumn.setWidth(3), iColumn.setWidth(4), tColumn.setWidth(3), 
					ixtColumn.setWidth(4), kapColumn.setWidth(8),
					esakColumn.setWidth(7), debitColumn.setWidth(8));

			// column title style
			report.setColumnTitleStyle(columnHeaderStyle);
			report.setColumnStyle(columnStyle);
			// add data source
			report.setDataSource(resultSet);//s,con1);//resultSet);
			
			//mark ESAK (no BSF) exceeding maximum DRL (with BSF)
			ConditionalStyleBuilder condition1 = DynamicReports.stl.conditionalStyle(
					DynamicReports.cnd.greater(esakColumn, 40000)//40mGy
					)			
				   .setBackgroundColor(Color.RED);
			StyleBuilder esakStyle = DynamicReports.stl.style().conditionalStyles(condition1)
				.setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(8);//, condition4);			
			esakColumn.setStyle(esakStyle);
			
			// footer
			report.pageFooter(DynamicReports.cmp.pageXofY());
			
			// higligh rows
			report.highlightDetailEvenRows();
			
			/*FontBuilder boldFont = DynamicReports.stl.fontArialBold().setFontSize(10);
			report.summary(DynamicReports.cht.barChart()		
		                  .setTitle("Bar chart")		
		                  .setTitleFont(boldFont)		
		                  .setCategory(rowNumColumn)		
		                  .series(		
		                		  DynamicReports.cht.serie(esakColumn))//, cht.serie(unitPriceColumn))		
		                  .setCategoryAxisFormat(		
		                		  DynamicReports.cht.axisFormat().setLabel(resources.getString("report.no1"+lang)+"\n"+resources.getString("report.no2"+lang))));
			 */
			/*
			Bar3DChartBuilder chart1 = DynamicReports.cht.bar3DChart()
			.setTitle("Dose").setCategory
			(
			//		resources.getString("report.no1"+lang)+
			//		"\n"+resources.getString("report.no2"+lang), String.class)
			patientColumn)
			.setTitleFont(DynamicReports.stl.fontArialBold().setFontSize(10))
			.series(DynamicReports.cht.serie(esakColumn))//,...))
			//.setUseSeriesAsCategory(true)
			.setValueAxisFormat(DynamicReports.cht.axisFormat().setTickLabelMask("#.00"))
			.addCustomizer(new ChartCustomizer())
			;
			//.setCategoryAxisFormat(DynamicReports.cht.axisFormat().setLabel("No."));
			
								
			));
			
			report.summary(chart1);
			*/
			
			//add chart
			XyBarChartBuilder chart1 = DynamicReports.cht.xyBarChart() 
			//.setTitle("Plot")
			//.setTitleFont(DynamicReports.stl.fontArialBold().setFontSize(10))
            .setXValue(rowNumColumn)
            .series(
            		DynamicReports.cht.xySerie(esakColumn))
            .setYAxisFormat(DynamicReports.cht.axisFormat().setTickLabelMask("#.00")//remove decimal separator ","
            		.setLabel("ESAK [uGy]"))	
            .addCustomizer(new ChartCustomizer())//to display only integers on x-axis
            ;
			
			 XyLineChartBuilder chart2 = DynamicReports.cht.xyLineChart() 
			//.setTitle("Plot")
			//.setTitleFont(DynamicReports.stl.fontArialBold().setFontSize(10))
            .setXValue(rowNumColumn)
            .series(
            		DynamicReports.cht.xySerie(kapColumn))
            .setYAxisFormat(DynamicReports.cht.axisFormat().setTickLabelMask("#.00")//remove decimal separator ","
            		.setLabel("KAP [uGy x m^2]"))	
            .addCustomizer(new ChartCustomizer())//to display only integers on x-axis		
            ;
			
			if (showPlot.isSelected()){
			report.summary(DynamicReports.cmp.horizontalFlowList(
					//chart2
					DynamicReports.cht.multiAxisChart(chart2,chart1)
					.setTitle("ESAK (Entrance Surface Air Kerma, no backscatter), KAP (Kerma Area Product)")
					.setTitleFont(DynamicReports.stl.fontArialBold().setFontSize(10))
			));
			}
			
			//showing the report
			//report.show();//this also close the main application, so we circumvent this by doing the following:
			JasperViewer jrViewer = new JasperViewer(report.toJasperPrint(),false);//Here, "false" means close report window only!!
			jrViewer.setTitle("Report");
			jrViewer.setVisible(true);
			jrViewer.setFitWidthZoomRatio();//fit to screen
			
			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
			//con1.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Save medical unit information
	 */
	private void saveUnit(){
		String ext=resources.getString("loadUnit.ext");
		String pct=".";
		String description=resources.getString("loadUnit.description");
		ExampleFileFilter jpgFilter =new ExampleFileFilter(ext, description);
		String datas=resources.getString("data.loadUnit");//"Data";
	    String filename="";
	    String currentDir=System.getProperty("user.dir");
	    String file_sep=System.getProperty("file.separator");
	    String opens=currentDir+file_sep+datas;
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showSaveDialog(this);//parent=this frame
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			filename= chooser.getSelectedFile().toString();
			int fl=filename.length();
			String test=filename.substring(fl-4);//exstension lookup!!
			String ctest=pct+ext;
			if (test.compareTo(ctest)!=0)
				filename=chooser.getSelectedFile().toString()+pct+ext;

			String s="";

			String uS=unitateTf.getText();//"NoName";
			if(uS.compareTo("")==0)
				uS="NoName";
			String aS=adresaTf.getText();//"NoName";
			if(aS.compareTo("")==0)
				aS="NoName";
			String lS=contactTf.getText();//"NoName";
			if(lS.compareTo("")==0)
				lS="NoName";
			String tS=aparatTf.getText();//"NoName";
			if(tS.compareTo("")==0)
				tS="NoName";
			String pS=notesTf.getText();//"NoName";
			if(pS.compareTo("")==0)
				pS="NoName";
			s=s+splitString(uS)+" "+splitString(aS)+" "+splitString(lS)
			+" "+splitString(tS)+" "+splitString(pS)+" ";

			try
			{
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			}
			catch (Exception ex)
			{

			}
		}		
	}
	
	/**
	 * Load medical unit information
	 */
	private void loadUnit()
	{
		String ext=resources.getString("loadUnit.ext");
		String pct=".";
		String description=resources.getString("loadUnit.description");
		ExampleFileFilter jpgFilter =
			new ExampleFileFilter(ext, description);
		String datas=resources.getString("data.loadUnit");//"Data";
	    String filename="";
	    String currentDir=System.getProperty("user.dir");
	    String file_sep=System.getProperty("file.separator");
	    String opens=currentDir+file_sep+datas;
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//-----------------
		//char lineSep = '\n';//System.getProperty("line.separator").charAt(0);
		int i =0;
		@SuppressWarnings("unused")
		int lnr =0;//line number
		StringBuffer desc=new StringBuffer();
		//String line="";
		boolean haveData=false;
		//--------------
		//String ss="";
		boolean unB=false;boolean adB=false;boolean laB=false;boolean tuB=false;boolean poB=false;

		int returnVal = chooser.showOpenDialog(this);//parent=this frame
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			filename= chooser.getSelectedFile().toString();
			int fl=filename.length();
			String test=filename.substring(fl-4);//exstension lookup!!
			String ctest=pct+ext;
			if (test.compareTo(ctest)!=0)
				filename=chooser.getSelectedFile().toString()+pct+ext;
			//resetData();
			try
			{
				FileInputStream in = new FileInputStream(filename);
        	   	while ((i = in.read()) != -1)
        	   	{
					if (!Character.isWhitespace((char)i))
					{
			   			desc.append((char)i);
			   			haveData=true;
					}
					else
					{
						if (haveData)
						{
							haveData=false;//reset
							lnr++;//-------------------------------------------
							if (!unB)
							{
								String s=desc.toString();
								unitateTf.setText(unsplitString(s));
								unB=true;//1 time always true from now.
							}
							else if (!adB)
							{
								String s=desc.toString();
								adresaTf.setText(unsplitString(s));
								adB=true;//1 time always true from now.
							}
							else if (!laB)
							{
								String s=desc.toString();
								contactTf.setText(unsplitString(s));
								laB=true;//1 time always true from now.
							}
							else if (!tuB)
							{
								String s=desc.toString();
								aparatTf.setText(unsplitString(s));
								tuB=true;//1 time always true from now.
							}
							else if (!poB)
							{
								String s=desc.toString();
								notesTf.setText(unsplitString(s));
								poB=true;//1 time always true from now.
							}

						}//have data

						desc=new StringBuffer();

					}
        	   	}
				in.close();
			}
			catch (Exception e)
			{

			}

		}

	}

	/**
	 * Internally used. Replace all white spaces with underscore for convenient medical unit file operations.
	 * @param s s
	 * @return the result
	 */
	private String splitString(String s)
	{
		String rs=s;
		String[] as=s.split("\\s");//white space split
		if (as.length>1)
		{
			rs="";
			for (int i=0; i<as.length; i++)
			{
				if(i<as.length-1)
				 rs=rs+as[i]+"_";//underscore
				else
				 rs=rs+as[i];
			}
		}
		return rs;
	}

	/**
	 * Internally used. The inverse of splitString.
	 * @param s s
	 * @return the result
	 */
	private String unsplitString(String s)
	{
		String rs="";

		for(int k=0; k<s.length(); k++)
		{
			char[] ch={s.charAt(k)};
			String sk=new String(ch);
			if (!sk.equalsIgnoreCase("_"))
			{
				rs=rs+s.charAt(k);
			}
			else
			{
				rs=rs+" ";
			}

		}
		return rs;
	}
	
	/**
	 * Create the database
	 */
	@SuppressWarnings("unused")
	private void createRadQC_DB() {
		Connection conng = null;

		String datas = resources.getString("data.load");
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas;
		
		String dbName = resources.getString("main.db");
		
		opens = opens + file_sep + dbName;
		String protocol = "jdbc:derby:";

		Statement s = null;

		try {
			String driver = "org.apache.derby.jdbc.EmbeddedDriver";
			// disable log file!
			System.setProperty("derby.stream.error.method",
					"jdf.db.DBConnection.disableDerbyLogFile");

			Class.forName(driver).newInstance();
			
			//first time creation:
			conng = DriverManager.getConnection(protocol + opens
					+ ";create=true", "", "");

			//for creation of more tables into an existed DB:
			//conng = DBConnection.getDerbyConnection(opens, "", "");
			
			String str = "";
			// ------------------
			conng.setAutoCommit(false);
			s = conng.createStatement();
			
			//s.execute("drop table " + resources.getString("main.db.Table"));
			str = "create table "
				+ resources.getString("main.db.Table")
				+ " ( Unique_ID integer, "
				+ "Patient VARCHAR(200), CNP VARCHAR(100), "
				+ "Examination VARCHAR(200), "
				+ "Pregnancy VARCHAR(50), "
				+ "Date VARCHAR(100), " +

				"Field VARCHAR(100), " +
				"FFD DOUBLE PRECISION"+
				", U_kV DOUBLE PRECISION, "+
				"I_mA DOUBLE PRECISION, "+
				"T_ms DOUBLE PRECISION, " +
				"IxT_mAs DOUBLE PRECISION, " +
				"KAP_uGyxm2 DOUBLE PRECISION, " +
				"ESAK_uGy DOUBLE PRECISION, " +
				"Rate_uGyPerSec DOUBLE PRECISION" +
				")"
				;
		   //s.execute(str);
			
			conng.commit();//if here then FIRE EXECUTE ALL COMMANDS!!
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {

			// Connection
			try {
				if (conng != null) {
					conng.close();
					conng = null;
				}
			} catch (Exception sqle) {
				sqle.printStackTrace();
			}
		}
	}
}
