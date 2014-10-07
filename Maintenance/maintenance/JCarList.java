package maintenance;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JCarList extends JAbstractModelObject {
	
	private String[] columnNames = {"Make","Model","Year"};
	private DefaultTableModel carsModel = new DefaultTableModel();
	private List<Car> cars = new ArrayList<Car>();
	private Car carSelected;
	private int customerId = -1;
	private JTable table;
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private JPanel panel_1;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNew;
	private JMenuItem mntmEdit;
	private JMenuItem mntmDelete;
	private JMenu mnView;
	private JCheckBoxMenuItem chckbxmntmShowDeleted;
	
	public JCarList() {
		mainPanel = new JPanel();
		initialize();
	}
	
	@SuppressWarnings("serial")
	private void initialize(){
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{336, 0};
		gbl_mainPanel.rowHeights = new int[]{36, 36, 104, 0};
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
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customerId != -1){
					final JCarDialog dialog = new JCarDialog();
					
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							firePropertyChange("carCreated", dialog.isConfirmed(), dialog.getCar());
						}
					});
				}
			}
		});
		mnFile.add(mntmNew);
		
		mntmEdit = new JMenuItem("Edit");
		mntmEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(carSelected != null){
					if(carSelected.isActive()){
						final JCarDialog dialog = new JCarDialog(carSelected);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						dialog.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent arg0) {
								firePropertyChange("carEdited", false, dialog.getCar());
							}
						});
					}
				}
			}
		});
		mnFile.add(mntmEdit);
		
		mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Car car = getCarSelected();
				if(car != null){
					car.setActive(false);
					firePropertyChange("carDeleted", true, car);
					if(chckbxmntmShowDeleted.getState()){
						setCars(false);
					}else{
						setCars(true);
					}
				}
			}
		});
		mnFile.add(mntmDelete);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		chckbxmntmShowDeleted = new JCheckBoxMenuItem("Show Deleted");
		chckbxmntmShowDeleted.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
				if(checkBox.getState()){
					setCars(false);
				}else{
					setCars(true);
				}
			}
		});
		mnView.add(chckbxmntmShowDeleted);
		
		JLabel lblCars = new JLabel("Cars");
		lblCars.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCars.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCars = new GridBagConstraints();
		gbc_lblCars.fill = GridBagConstraints.BOTH;
		gbc_lblCars.insets = new Insets(0, 0, 5, 0);
		gbc_lblCars.gridx = 0;
		gbc_lblCars.gridy = 1;
		mainPanel.add(lblCars, gbc_lblCars);
		
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
				if(table.getSelectedRow() != -1){
					Car oldValue = carSelected;
					int selectedRow = table.getSelectedRow();
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < table.getColumnCount(); i++){
						sb.append(table.getValueAt(selectedRow, i) + " ");
					}
					setCarSelected(sb.toString());
					firePropertyChange("carSelected", oldValue, carSelected);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2){
					if(!carSelected.isActive()){
						carSelected.setActive(true);
						firePropertyChange("carDeleted", true, false);
						if(chckbxmntmShowDeleted.getState()){
							setCars(false);
						}else{
							setCars(true);
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
	
	public void setListModel(List<Car> carList, int customerId){
		cars = carList;
		this.customerId = customerId;
		
		if(chckbxmntmShowDeleted.getState()){
			setCars(false);
		}else{
			setCars(true);
		}
	}
	
	public void clearListModel(){
		table.setModel(new DefaultTableModel());
	}
	
	public JPanel getMainPanel(){
		return mainPanel;
	}
	
	public Car getCarSelected(){
		return carSelected;
	}
	
	public void clearCarSelected(){
		carSelected = null;
	}
	
	public void clearCustomerId(){
		customerId = -1;
	}
	
	private void showCarList(List<Car> carList){
		ArrayList<ArrayList<String>> dataArray = new ArrayList<ArrayList<String>>();
		for(Car car : carList){
			ArrayList<String> carData = new ArrayList<String>();
			if(car.isActive()){
				carData.add(car.getMake());
				carData.add(car.getModel());
				carData.add(String.valueOf(car.getYear()));
			}else{
				carData.add("inactive" + car.getMake());
				carData.add("inactive" + car.getModel());
				carData.add("inactive" + String.valueOf(car.getYear()));
			}
			dataArray.add(carData);
		}
		String[][] data = new String[dataArray.size()][columnNames.length];
		for(int i = 0; i < dataArray.size(); i++){
			ArrayList<String> row = dataArray.get(i);
			for(int j = 0; j < columnNames.length; j++){
				data[i][j] = row.get(j);
			}
		}
		carsModel = new DefaultTableModel(data, columnNames);
		table.setModel(carsModel);
		for(int i = 0; i < table.getColumnCount(); i++){
			table.getColumnModel().getColumn(i).setCellRenderer(new CarListRenderer());
		}
	}
	
	private void setCarSelected(String listValue){
		String[] fields = listValue.split(" ");
		for(int i = 0; i < fields.length; i++){
			fields[i] = fields[i].replace("inactive", "");
		}
		for(Car car : cars){
			if(car.getMake().equals(fields[0]) && car.getModel().equals(fields[1]) && String.valueOf(car.getYear()).equals(fields[2])){
				carSelected = car;
			}
		}
	}
	
	private void setCars(boolean activeOnly){
		if(activeOnly){
			ArrayList<Car> newModel = new ArrayList<Car>();
			for(Car car : cars){
				if(car.isActive()){
					newModel.add(car);
				}
			}
			showCarList(newModel);
		}else{
			showCarList(cars);
		}
	}
	
	@SuppressWarnings("serial")
	private class CarListRenderer extends DefaultTableCellRenderer{
		
		public CarListRenderer(){
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
}
