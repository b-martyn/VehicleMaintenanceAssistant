package maintenance;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

import java.awt.FlowLayout;

import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JRecordList extends JAbstractModelObject {
	
	private String[] columnNames = {"Date","Mileage","Expenses","Type"};
	private DefaultTableModel records = new DefaultTableModel();
	private int carId = -1;
	private JTable table;
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private JLabel lblRecords;
	private JPanel panel_1;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmAddExpenses;
	private JMenuItem mntmDelete;
	private JMenu mnView;
	private JMenu mnShow;
	private JCheckBoxMenuItem mntmOilChanges;
	private JCheckBoxMenuItem mntmAll;
	private JButton btnNewButton;
	private List<ServiceRecord> recordList;
	private ServiceRecord recordSelected;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
	private JCheckBoxMenuItem chckbxShowDeleted;
	
	public JRecordList() {
		mainPanel = new JPanel();
		initialize();
	}
	
	@SuppressWarnings("serial")
	private void initialize(){
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{336, 0};
		gbl_mainPanel.rowHeights = new int[]{35, 45, 220, 0};
		gbl_mainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		mainPanel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		menuBar = new JMenuBar();
		panel_1.add(menuBar);
		
		btnNewButton = new JButton("New");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(carId != -1){
					final JRecordDialog dialog = new JRecordDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							firePropertyChange("serviceRecordCreated", dialog.isValid(), dialog.getServiceRecord());
						}
					});
				}
			}
		});
		menuBar.add(btnNewButton);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmAddExpenses = new JMenuItem("Add Expenses");
		mntmAddExpenses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(recordSelected != null){
					final JAddExpensesDialog dialog = new JAddExpensesDialog(recordSelected);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							firePropertyChange("serviceRecordEdited", dialog.isValid(), dialog.getServiceRecord());
						}
					});
				}
			}
		});
		mnFile.add(mntmAddExpenses);
		
		mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServiceRecord serviceRecord = getRecordSelected();
				if(serviceRecord != null){
					serviceRecord.setActive(false);
					firePropertyChange("serviceRecordDeleted", true, false);
					if(chckbxShowDeleted.getState()){
						setRecords(false);
					}else{
						setRecords(true);
					}
				}
			}
		});
		mnFile.add(mntmDelete);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		mnShow = new JMenu("Show");
		mnView.add(mnShow);
		
		mntmAll = new JCheckBoxMenuItem("All");
		mntmAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCheckBoxes((JCheckBoxMenuItem) e.getSource());
			}
		});
		mnShow.add(mntmAll);
		
		mntmOilChanges = new JCheckBoxMenuItem("Oil Changes");
		mntmOilChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCheckBoxes((JCheckBoxMenuItem) e.getSource());
			}
		});
		mnShow.add(mntmOilChanges);
		
		chckbxShowDeleted = new JCheckBoxMenuItem("Show Deleted");
		chckbxShowDeleted.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
				if(checkBox.getState()){
					setRecords(false);
				}else{
					setRecords(true);
				}
			}
		});
		mnView.add(chckbxShowDeleted);
		
		lblRecords = new JLabel("Records");
		lblRecords.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRecords.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCars = new GridBagConstraints();
		gbc_lblCars.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCars.insets = new Insets(0, 0, 5, 0);
		gbc_lblCars.gridx = 0;
		gbc_lblCars.gridy = 1;
		mainPanel.add(lblRecords, gbc_lblCars);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), null));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		mainPanel.add(scrollPane, gbc_scrollPane);
		
		table = new JTable(){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = table.getSelectedRow();
				if(selectedRow != -1){
					setRecordSelected((String) table.getValueAt(selectedRow, 0));
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2){
					if(!recordSelected.isActive()){
						recordSelected.setActive(true);
						firePropertyChange("serviceRecordDeleted", true, false);
						if(chckbxShowDeleted.getState()){
							setRecords(false);
						}else{
							setRecords(true);
						}
					}
				}
			}
		});
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				KeyStroke kbDeleteButton = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
				mntmDelete.setAccelerator(kbDeleteButton);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				mntmDelete.setAccelerator(null);
			}
		});
	}
	
	public void setListModel(List<ServiceRecord> serviceRecordList, Car car){
		recordList = serviceRecordList;
		carId = car.getId();
		if(chckbxShowDeleted.getState()){
			setRecords(false);
		}else{
			setRecords(true);
		}
	}
	
	public void clearListModel(){
		table.setModel(new DefaultTableModel());
	}
	
	public JPanel getMainPanel(){
		return mainPanel;
	}
	
	public void setTitle(RecordType recordType){
		StringBuilder sb = new StringBuilder();
		String recordTypeString = "";
		if(recordType == RecordType.OILCHANGE){
			recordTypeString = "Oil Change";
		}
		sb.append(recordTypeString);
		sb.append(" ");
		sb.append("Records");
		this.lblRecords.setText(sb.toString());
	}
	
	public void setTitle(){
		this.lblRecords.setText("Records");
	}
	
	public ServiceRecord getRecordSelected(){
		return recordSelected;
	}
	
	public void removeCarId(){
		carId = -1;
	}
	
	private void setRecordSelected(String dateString){
		String value = null;
		if(dateString.startsWith("inactive")){
			value = dateString.replace("inactive", "");
		}else{
			value = dateString;
		}
		for(ServiceRecord sRecord : recordList){
			if(SDF.format(sRecord.getDate()).equals(value)){
				recordSelected = sRecord;
			}
		}
	}
	
	private void setRecords(boolean activeOnly){
		if(activeOnly){
			ArrayList<ServiceRecord> newModel = new ArrayList<ServiceRecord>();
			for(ServiceRecord serviceRecord : recordList){
				if(serviceRecord.isActive()){
					newModel.add(serviceRecord);
				}
			}
			showRecordList(newModel);
		}else{
			showRecordList(recordList);
		}
	}
	
	private void showRecordList(List<ServiceRecord> serviceRecordList){
		if(carId != -1){
			ArrayList<ArrayList<String>> dataArray = new ArrayList<ArrayList<String>>();
			for(ServiceRecord serviceRecord : serviceRecordList){
				if(serviceRecord.getCarId() == carId){
					if(serviceRecord.isActive()){
						ArrayList<String> recordData = new ArrayList<String>();
						recordData.add(SDF.format(serviceRecord.getDate()));
						recordData.add(String.valueOf(serviceRecord.getMileage()));
						recordData.add(String.valueOf(serviceRecord.getExpenses()));
						recordData.add(serviceRecord.getType().toString());
						dataArray.add(recordData);
					}else{
						ArrayList<String> recordData = new ArrayList<String>();
						recordData.add("inactive" + SDF.format(serviceRecord.getDate()));
						recordData.add("inactive" + String.valueOf(serviceRecord.getMileage()));
						recordData.add("inactive" + String.valueOf(serviceRecord.getExpenses()));
						recordData.add("inactive" + serviceRecord.getType().toString());
						dataArray.add(recordData);
					}
				}
			}
			String[][] data = new String[dataArray.size()][columnNames.length];
			for(int i = 0; i < dataArray.size(); i++){
				ArrayList<String> row = dataArray.get(i);
				for(int j = 0; j < columnNames.length; j++){
					data[i][j] = row.get(j);
				}
			}
			records = new DefaultTableModel(data, columnNames);
			table.setModel(records);
			for(int i = 0; i < table.getColumnCount(); i++){
				table.getColumnModel().getColumn(i).setCellRenderer(new RecordListRenderer());
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class RecordListRenderer extends DefaultTableCellRenderer{
		
		public RecordListRenderer(){
			setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		@Override
		public void setValue(Object value){
			if(((String) value).startsWith("inactive")){
				setForeground(Color.GRAY);
				StringBuilder sb = new StringBuilder((String) value);
				sb.replace(0, 8, "");
				super.setText(sb.toString());
			}else{
				setForeground(Color.BLACK);
				super.setText((String) value);
			}
		}
	}
	
	private void clearCheckBoxes(JCheckBoxMenuItem itemSelected){
		for(int i = 0; i < mnShow.getItemCount(); i++){
			JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) mnShow.getItem(i);
			System.out.println(checkBox.getState());
			checkBox.setState(false);
		}
		itemSelected.setState(true);
	}
}
