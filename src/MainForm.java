import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import com.toedter.calendar.JYearChooser;

import net.proteanit.sql.DbUtils;
//import ttest.ComboBoxItem.Item;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.im.InputContext;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.ServerError;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;

import com.mysql.fabric.xmlrpc.base.Value;
import com.toedter.calendar.JDateChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toedter.components.JSpinField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SwingConstants;

public class MainForm {

	private JFrame frame;
	private JTextField txt_emp_id;
	private JTextField txtName;
	private JTextField textSal;
	private JTable table;
	String URL="jdbc:mysql://localhost/final";
	Connection con=null;
	PreparedStatement pst=null;
	ResultSet rs=null;
	DefaultTableModel dm= new DefaultTableModel();
	 String xString="";
	 String search="";
	String path="";
	String ContratType="";
	String TextPath="";
	String back="";
	String Dep_id="";
	
	 Vector<Item> model = new Vector<Item>();
	 private JTextField textSearch;
	/**
	 * Launch the application.
	 */
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private   DefaultTableModel getdata() {
		dm.addColumn("id");
		dm.addColumn("name");
		dm.addColumn("BirthDate");
		dm.addColumn("StartDate");
		dm.addColumn("Salary");
		dm.addColumn("Period");
		dm.addColumn("Dep_name");
		dm.addColumn("TypeName");
		
		try {
			con =DriverManager.getConnection("jdbc:mysql://localhost/final","root","root");
			String sql = "SELECT  employee.id,employee.Name,employee.BirthDate,employee.StartDate,employee.Salary,employee.Period,"
					+ "department.Dep_name ,Contrat.TypeName From employee  LEFT OUTER JOIN department ON employee.Department_id = department.id "
					+ "LEFT OUTER JOIN Contrat ON employee.Contrat_id = Contrat.id";
			//String sql = "SELECT * FROM employee ";
			pst = con.prepareStatement(sql);
			rs=pst.executeQuery(sql);
			//table.setModel(getdata());
		       while(rs.next())
		        {
		    	   String id=rs.getString(1);
		    	   String name=rs.getString(2);
		    	   String BirthDate=rs.getString(3);
		    	   String StartDate=rs.getString(4);
		    	   String Salary=rs.getString(5);
		    	   String Period=rs.getString(6);
		    	   String Dep_name=rs.getString(7);
		    	   String TypeName=rs.getString(8);
		    	   String rowData[]= {id,name,BirthDate,StartDate,Salary,Period,Dep_name,TypeName};
		    	   dm.addRow(rowData);
			}
		       return dm;
		} catch (Exception ex) 
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		return null;
		
	}
	
	
	
	private String getCellValue(int x,int y) {
		return dm.getValueAt(x, y).toString();
	}
	private void writeToExecl() {
	XSSFWorkbook wb=new XSSFWorkbook();
	XSSFSheet ws=wb.createSheet();
	TreeMap<String, Object[]> data=new TreeMap<>();
	
	data.put("-1", new Object[] {dm.getColumnName(0),
			dm.getColumnName(1),
			dm.getColumnName(2),
			dm.getColumnName(3)
			,dm.getColumnName(4),
			dm.getColumnName(5),
			dm.getColumnName(6),
			dm.getColumnName(7)});
	
	for (int i = 0; i < dm.getRowCount(); i++) {
		data.put(Integer.toString(i), new Object[]{getCellValue(i,0)
				,getCellValue(i,1)
				,getCellValue(i,2)
				,getCellValue(i,3)
				,getCellValue(i,4)
				,getCellValue(i,5)
				,getCellValue(i,6)
				,getCellValue(i,7)
			//	,getCellValue(i,8)
				});
	}
	
	Set<String> ids=data.keySet();
	XSSFRow row;
	int Rowid=0;
	for (String key: ids) {
		row=ws.createRow(Rowid++);
		Object[] values=data.get(key);
		
		int CellId=0;
		for (Object o:values) {
			Cell cell=row.createCell(CellId++);
			cell.setCellValue(o.toString());
			
		}
	}
	
	
	try {
		FileOutputStream fos=new FileOutputStream(new File(path));
		wb.write(fos);
		fos.close();
		JOptionPane.showMessageDialog(null, "Data Exported To Excel File");
	}
	catch (FileNotFoundException ex) {
	//	Logger.getLogger(Workbook.class.getName()).log(level, null, ex);
	}catch (IOException ex) {
		// TODO: handle exception
	}
	
	
	
	}	
	
	
	private void ShowTableData() {
		dm.addColumn("name");
		dm.addColumn("id");
		// TODO Auto-generated method stub
		try {
			con =DriverManager.getConnection("jdbc:mysql://localhost/final","root","root");
		//	String sql = "SELECT * FROM employee";
		//	String sql = "SELECT e.id,e.name,e.BirthDate,e.StartDate,e.Salary,e.Period as employee_department FROM employee e JOIN department d ON e.Department_id =d.id ";
			//String sql="select * from department inner join employee on department.id = employee.Department_id; ";
			
//			SELECT Orders.OrderID, Customers.CustomerName
//			FROM Orders
//			INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID;
//			
			String name=textSearch.getText();
			String sql = "SELECT  employee.id,employee.Name,employee.BirthDate,employee.StartDate,employee.Salary,employee.Period,"
					+ "department.Dep_name ,Contrat.TypeName From employee  LEFT OUTER JOIN department ON employee.Department_id = department.id "
					+ "LEFT OUTER JOIN Contrat ON employee.Contrat_id = Contrat.id "+search;
			
			//String sql = "SELECT * FROM employee ";
			pst = con.prepareStatement(sql);
			if (search=="") {
				rs=pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));
			}else if (back=="back") {
				rs=pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));
			}else {
				pst.setString(1, name);
				rs=pst.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));	
			}

			//table.setModel(getdata());
			
		} catch (Exception ex) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, ex);
		}
		
		
	}
	
//	private void writeToExecl() {
//		XSSFWorkbook wb=new XSSFWorkbook();
//		XSSFSheet ws=wb.createSheet();
//		TreeMap<String, Object[]> data=new TreeMap<>();
//		data.put("0", new Object() {table.});
//	}	
	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
		ShowTableData();
		getdata();
		txt_emp_id.setText("0");
	}

	/**
	 * Initialize the contents of the frame.
	 * @param <ExcelExport>
	 */
	@SuppressWarnings("unchecked")
	private <ExcelExport> void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 710, 505);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lbl_Id = new JLabel("Employee ID");
		lbl_Id.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\credit-card.png"));
		lbl_Id.setBounds(32, 25, 104, 25);
		frame.getContentPane().add(lbl_Id);
		
		txt_emp_id = new JTextField();
		txt_emp_id.setBounds(138, 27, 116, 20);
		frame.getContentPane().add(txt_emp_id);
		txt_emp_id.disable();
		
		txt_emp_id.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Birth Date");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\calendar.png"));
		lblNewLabel.setBounds(32, 61, 85, 30);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Department");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\fire-station.png"));
		lblNewLabel_1.setBounds(32, 102, 99, 28);
		frame.getContentPane().add(lblNewLabel_1);		
		
		//@SuppressWarnings("rawtypes")
		
	
         
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection con=DriverManager.getConnection(URL, "root", "root");
//			String query="Select * from department";
//			Statement stm=con.createStatement();
//			
//			ResultSet RES=stm.executeQuery(query);
//		       while(RES.next())
//		        {
//		    	    xString=RES.getString(2);
//		    	    Dep.addItem(xString); 
////		    	   Dep.addItem(RES.getString(1)); 
//	    	   //Dep.addItem(RES.getString(2)); 
//		    	    System.out.println(xString);
//			}
//		
//			
//		} catch (ClassNotFoundException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(URL, "root", "root");
			String query="Select * from department";
			Statement stm=con.createStatement();
			 
			ResultSet RES=stm.executeQuery(query);
			model.addElement(null);
		       while(RES.next())
		        {
		    	String   name=RES.getString(2);
		    	int   id=RES.getInt(1);
		           model.addElement( new Item(id, name ) );
			}
		
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  JComboBox<Item> Dep;
			 

	        Dep = new JComboBox<Item>( model );
	        Dep.setSelectedItem( xString);
	        Dep.addActionListener(new ActionListener() {
	        	@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent e) {
	        		  @SuppressWarnings("rawtypes")
					JComboBox Dep = (JComboBox)e.getSource();
	        		
	      	        Item item = (Item)Dep.getSelectedItem();
	      	      int intid=item.getId();
	      	      Dep_id=String.valueOf(intid);
	      	    //    System.out.println( intid + " : " + item.getDescription() );
	        	}
	        });
	        Dep.setEnabled(true);
	     //   Dep.addActionListener( (ActionListener) this );
	      // getContentPane().add(Dep, BorderLayout.NORTH );
			
	       
			
			//JComboBox Dep = new JComboBox();
	        class ItemRenderer extends BasicComboBoxRenderer
	        {
	            public Component getListCellRendererComponent(
	                JList list, Object value, int index,
	                boolean isSelected, boolean cellHasFocus)
	            {
	                super.getListCellRendererComponent(list, value, index,
	                    isSelected, cellHasFocus);
	     
	                if (value != null)
	                {
	                    Item item = (Item)value;
	                    setText( item.getDescription().toUpperCase() );
	                }
	     
	                if (index == -1)
	                {
	                    Item item = (Item)value;
	                    setText( "" + item.getId() );
	                }
	     
	     
	                return this;
	            }
	        }
			Dep.setBounds(138, 106, 116, 20);
			frame.getContentPane().add(Dep);
		
		

		JSpinner Syear = new JSpinner();
		Syear.setModel(new SpinnerNumberModel(0, 0, 80, 1));
		Syear.setBounds(287, 213, 42, 19);
		frame.getContentPane().add(Syear);
		frame.setVisible(true);        
		
		JSpinner SMonth = new JSpinner();
		SMonth.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		SMonth.setBounds(395, 213, 42, 20);
		frame.getContentPane().add(SMonth);
		
		JDateChooser dCBirth = new JDateChooser();
		dCBirth.setToolTipText("");
		dCBirth.setBounds(138, 61, 116, 20);
		frame.getContentPane().add(dCBirth);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\id-card.png"));
		lblName.setBounds(409, 25, 79, 24);
		frame.getContentPane().add(lblName);
		
		txtName = new JTextField();
		txtName.setBounds(511, 23, 110, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		JLabel lblStratDate = new JLabel("Strat Date");
		lblStratDate.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\19.png"));
		lblStratDate.setBounds(409, 61, 92, 20);
		frame.getContentPane().add(lblStratDate);
		
		JDateChooser dCStart = new JDateChooser();
		dCStart.setBounds(511, 61, 110, 20);
		frame.getContentPane().add(dCStart);
		
		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\money.png"));
		lblSalary.setBounds(409, 101, 66, 31);
		frame.getContentPane().add(lblSalary);
		
		textSal = new JTextField();
		textSal.setBounds(511, 106, 110, 20);
		frame.getContentPane().add(textSal);
		textSal.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.activeCaption);
		separator.setBounds(46, 326, 674, 14);
		frame.getContentPane().add(separator);
		
		JLabel lblNewLabel_2 = new JLabel("category");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\diagram.png"));
		lblNewLabel_2.setBounds(173, 161, 79, 30);
		frame.getContentPane().add(lblNewLabel_2);
		
		JRadioButton r1=new JRadioButton("Full Time");    
		JRadioButton r2=new JRadioButton("Part Time");    
		r1.setBounds(354,161,85,30);    
		r2.setBounds(255,161,85,30);    
		ButtonGroup bg=new ButtonGroup();    
		bg.add(r1);bg.add(r2);    
		frame.getContentPane().add(r1);frame.getContentPane().add(r2);      
		frame.setSize(741,719);    
		frame.getContentPane().setLayout(null);    
		
		JButton btnAdd = new JButton("New Employee ");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	txt_emp_id.setText("1");
//				if (r1.isSelected()) {
//					String mm = r1.getText();
//					System.out.println(mm);	
//				}else {
//					String mm = r2.getText();
//					System.out.println(mm);	
//				}
				//String date  = ((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
				
				
			//	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

				//Calendar c = Calendar.getInstance();
				//c.set(year, month, 1); // Specify day of month

			//	String formattedDate = dateFormat.format(c.getTime());
				//System.out.println(formattedDate);	
				if (r1.isSelected()) {
					 ContratType  = "1";
			//		System.out.println(ContratType );	
				}else {
					 ContratType  = "2";
					//System.out.println(ContratType );	
				}
				String Name=txtName.getText();
				String BirthDate  = ((JTextField)dCBirth.getDateEditor().getUiComponent()).getText();
				String StartDate  = ((JTextField)dCStart.getDateEditor().getUiComponent()).getText();
				String salary=textSal.getText();
				int year=(int) Syear.getValue();
				int month=(int) SMonth.getValue();
				int Period=year*12+month;
				String Contrat_id=ContratType;
				if (Name==null||BirthDate==null||StartDate==null||Name==null||salary==null||Period==0||Contrat_id==null||Dep_id==""||Contrat_id==null) {
					JOptionPane.showMessageDialog(null, "Invalid Data");
				}else {
					
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=DriverManager.getConnection(URL, "root", "root");		
					String query="INSERT INTO employee (Name,BirthDate,StartDate,salary,Period,Department_id,Contrat_id) Values (?,?,?,?,?,?,?)";
					PreparedStatement stm=con.prepareStatement(query);
					stm.setString(1, Name);
					stm.setString(2, BirthDate);
					stm.setString(3, StartDate);
					stm.setString(4, salary);
					stm.setInt(5, Period);
					stm.setString(6, Dep_id);
					stm.setString(7, Contrat_id);
					int nb=stm.executeUpdate();
					if(nb==1) {
						JOptionPane.showMessageDialog(frame, "New Record is Inserted");
						ShowTableData();
						
						}
					else {
						JOptionPane.showMessageDialog(frame, "ERROR during Insertion");
						ShowTableData();
						getdata();
					}
				} catch (ClassNotFoundException | SQLException  e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}
			}
		});
		btnAdd.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\add-user.png"));
		btnAdd.setBounds(317, 285, 150, 30);
		frame.getContentPane().add(btnAdd);
		
		JLabel lblNewLabel_3 = new JLabel("Peirod");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\clock.png"));
		lblNewLabel_3.setBounds(173, 211, 85, 25);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblYear = new JLabel("Year");
		lblYear.setBounds(254, 216, 42, 14);
		frame.getContentPane().add(lblYear);
		
		JLabel lblMonth = new JLabel("Month");
		lblMonth.setBounds(354, 216, 46, 14);
		frame.getContentPane().add(lblMonth);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0), 0));

		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel model=(DefaultTableModel)table.getModel();
				int sec=table.getSelectedRow();
				txt_emp_id.setText(model.getValueAt(sec, 0).toString());
				
				txtName.setText(model.getValueAt(sec, 1).toString());
				String birthDate = model.getValueAt(sec, 2).toString();
				DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);;
				try {
					java.util.Date birthDate2 = format.parse(birthDate);
					dCBirth.setDate(birthDate2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String startDate = model.getValueAt(sec, 3).toString();
				DateFormat format2 = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);;
				try {
					java.util.Date startDate2 = format2.parse(startDate);
					dCStart.setDate(startDate2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textSal.setText(model.getValueAt(sec, 4).toString());
				//String mString=model.getValueAt(sec, 6).toString();
				String Period=model.getValueAt(sec, 5).toString();
				Integer intPeriod = Integer.valueOf(Period);
				SMonth.setValue(intPeriod);
				String type=model.getValueAt(sec, 7).toString();
				String check="Full Time";
				if (!type.equals(check)) {
					r2.setSelected(true);
				}else {
					r1.setSelected(true);
				}
				
			}
		});
		
		table.setModel(new DefaultTableModel());
		scrollPane.setBounds(32, 447, 668, 228);
		frame.getContentPane().add(scrollPane);
		scrollPane.setColumnHeaderView(table);
		scrollPane.setViewportView(table);
		
		JButton btnExportEx = new JButton("Export ");
		btnExportEx.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\excel.png"));
		btnExportEx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (path=="") {
					JFileChooser chooser=new JFileChooser("Choose a Excel File")	;
				     chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
					
				    chooser.addChoosableFileFilter(filter);
						if(chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
						File out=chooser.getSelectedFile();
						 path=out.getPath();
					  //   System.out.println(out.getAbsolutePath()); 
				      //    System.out.println(out.getAbsoluteFile()); 
				      //    System.out.println(out.getPath()); 
						 
						 JOptionPane.showMessageDialog(null, "press Export to Export Data to:"+path);
				        //  System.out.println(path); 
				      //    btnSs.setName("EXPORT");
				     }
				}else {
					writeToExecl();
					path="";
					
				}


			}
		});
		btnExportEx.setBounds(38, 285, 110, 30);
		frame.getContentPane().add(btnExportEx);
		
		JButton btnExpotText = new JButton("Export ");
		btnExpotText.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\txt.png"));
		btnExpotText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (TextPath=="") {
					JFileChooser chooser=new JFileChooser("Choose a Excel File")	;
				     chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
					
				    chooser.addChoosableFileFilter(filter);
						if(chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
						File out=chooser.getSelectedFile();
						 TextPath=out.getPath();
					  //   System.out.println(out.getAbsolutePath()); 
				      //    System.out.println(out.getAbsoluteFile()); 
				      //    System.out.println(out.getPath()); 
						 
						 JOptionPane.showMessageDialog(null, "press Export to Export Data to:"+path);
				        //  System.out.println(path); 
				      //    btnSs.setName("EXPORT");
				     }
				}else {				
					ExportText();
					TextPath="";
	              }
			}
			
		});

		
		btnExpotText.setBounds(605, 285, 110, 30);
		frame.getContentPane().add(btnExpotText);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent update) {
				if (r1.isSelected()) {
					 ContratType  = "1";
					//System.out.println(ContratType );	
				}else {
					 ContratType  = "2";
				//	System.out.println(ContratType );	
				}
				String Name=txtName.getText();
				String BirthDate  = ((JTextField)dCBirth.getDateEditor().getUiComponent()).getText();
				String StartDate  = ((JTextField)dCBirth.getDateEditor().getUiComponent()).getText();
				String salary=textSal.getText();
				int year=(int) Syear.getValue();
				int month=(int) SMonth.getValue();
				int Period=year*12+month;
				String Contrat_id=ContratType;
				if (BirthDate==null||StartDate==null||Name==null||salary==null||Period==0||Contrat_id==null||Dep_id==""||Contrat_id==null) {
					JOptionPane.showMessageDialog(null, "Invalid Data");
				}else {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con=DriverManager.getConnection(URL, "root", "root");

					String id=txt_emp_id.getText();
					//String query="Update INTO employee (Name,BirthDate,StartDate,salary,Period,Department_id,Contrat_id) Values (?,?,?,?,?,?,1)";		
					String query="UPDATE employee SET Name=?,BirthDate=?,StartDate=?,salary=?,Period=?,Department_id=?,Contrat_id=? WHERE id="+id;
					PreparedStatement stm=con.prepareStatement(query);
					stm.setString(1, Name);
					stm.setString(2, BirthDate);
					stm.setString(3, StartDate);
					stm.setString(4, salary);
					stm.setInt(5, Period);
					stm.setString(6, Dep_id);
					stm.setString(7, Contrat_id);
					int nb=stm.executeUpdate();
					if(nb==1) {
						JOptionPane.showMessageDialog(frame, " Record his been Update");
						ShowTableData();
						
						}
					else {
						JOptionPane.showMessageDialog(frame, "ERROR during Insertion");
						ShowTableData();
						getdata();
					}
				} catch (ClassNotFoundException | SQLException  update1) {
					// TODO Auto-generated catch block
					update1.printStackTrace();
				} 
			}
			}
		});
		btnUpdate.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\refresh.png"));
		btnUpdate.setBounds(485, 285, 110, 30);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			DeleteEmployee();	
				}
			
		});
		btnDelete.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\delete.png"));
		btnDelete.setBounds(186, 285, 110, 30);
		frame.getContentPane().add(btnDelete);
		
		JButton btnNewButton_1 = new JButton("Search ");
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton_1.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\research.png"));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	String name=textSearch.getText();
				back="";
				search="WHERE employee.Name=?";
				ShowTableData();
			}
		});
		btnNewButton_1.setBounds(335, 390, 85, 30);
		frame.getContentPane().add(btnNewButton_1);
		
		textSearch = new JTextField();
		textSearch.setBounds(264, 359, 138, 20);
		frame.getContentPane().add(textSearch);
		textSearch.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Search By Name");
		lblNewLabel_4.setBounds(292, 335, 116, 20);
		frame.getContentPane().add(lblNewLabel_4);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textSearch.setText("");
				search="";
				back="back";
				ShowTableData();
			}
		});
		btnBack.setIcon(new ImageIcon("C:\\Users\\Rasheed\\eclipse-workspace\\Fanil_Project\\Icons\\back.png"));
		btnBack.setBounds(234, 390, 85, 30);
		frame.getContentPane().add(btnBack);
		

		
 
		
	}

	protected void DeleteEmployee() {
		String id=txt_emp_id.getText();
		int CheckID = Integer.parseInt(id);
		if (CheckID==0) {
			JOptionPane.showMessageDialog(null, "Invalid Selection");
			}else {
		 int reply = JOptionPane.showConfirmDialog(null, "Are You sure You Want to Delete This Record ?","Warning",1 ,JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.YES_OPTION)
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection(URL, "root", "root");
				

				
				
				String query="DELETE FROM employee WHERE id="+id;
				PreparedStatement stm=con.prepareStatement(query);
				int nb=stm.executeUpdate();
				if(nb==1) {
					//JOptionPane.showConfirmDialog(frame, , , 1);
					
				
					ShowTableData();
					
					}
				else {
					JOptionPane.showMessageDialog(frame, "ERROR during Insertion");
					ShowTableData();
					getdata();
				}
			} catch (ClassNotFoundException | SQLException  update1) {
				// TODO Auto-generated catch block
				update1.printStackTrace();
			} 
		}
			}
		
	}

	protected void ExportText() {
        try{
            //the file path
           File file = new File(TextPath);
           //if the file not exist create one
           if(!file.exists()){
               file.createNewFile();
           }
           
           FileWriter fw = new FileWriter(file.getAbsoluteFile());
           BufferedWriter bw = new BufferedWriter(fw);
           
           //loop for jtable rows
           for(int i = 0; i < table.getRowCount(); i++){
               //loop for jtable column
               for(int j = 0; j < table.getColumnCount(); j++){
                   bw.write(table.getModel().getValueAt(i, j)+" ");
               }
               //break line at the begin 
               //break line at the end 
               bw.write("\n_________\n");
           }
           //close BufferedWriter
           bw.close();
           //close FileWriter 
           fw.close();
           JOptionPane.showMessageDialog(null, "Data Exported");
           
           }catch(Exception ex){
               ex.printStackTrace();
           }
		
	}
}
