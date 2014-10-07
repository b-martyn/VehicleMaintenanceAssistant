package maintenance;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.FlowLayout;

import javax.swing.JCheckBoxMenuItem;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JCustomerList extends JAbstractModelObject {
	
	private JPanel mainPanel;
	private Customer customerSelected;
	private DefaultListModel<Customer> customers = new DefaultListModel<Customer>();
	private JList<Customer> list;
	private JCheckBoxMenuItem chckbxmntmShowDeleted;
	private JMenuItem mntmDelete;
	
	public JCustomerList() {
		mainPanel = new JPanel();
		setCustomers(true);
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{135, 0};
		gbl_mainPanel.rowHeights = new int[]{41, 31, 396, 0};
		gbl_mainPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		mainPanel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel_1.add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Segoe UI", Font.BOLD, 12));
		mnFile.setHorizontalAlignment(SwingConstants.CENTER);
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JCustomerDialog dialog = new JCustomerDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				dialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent arg0) {
						RecordBook rBook = new RecordBook();
						Customer customer = dialog.getCustomer();
						CustomerList customerList = new CustomerList();
						customerList.addCustomer(customer);
						rBook.setCustomer(customer);
						rBook.write();
						if(chckbxmntmShowDeleted.getState()){
							setCustomers(false);
						}else{
							setCustomers(true);
						}
					}
				});
			}
		});
		mntmNew.setMnemonic('N');
		mnFile.add(mntmNew);
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		mntmEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customerSelected != null){
					if(customerSelected.isActive()){
						final JCustomerDialog dialog = new JCustomerDialog(customerSelected);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						dialog.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent arg0) {
								firePropertyChange("customerEdited", false, dialog.getCustomer());
							}
						});
					}
				}
			}
		});
		mntmEdit.setMnemonic('E');
		mnFile.add(mntmEdit);
		
		mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer customer = getCustomerSelected();
				if(customer != null){
					customer.setActive(false);
					firePropertyChange("customerDeleted", true, false);
					if(chckbxmntmShowDeleted.getState()){
						setCustomers(false);
					}else{
						setCustomers(true);
					}
				}
			}
		});
		mnFile.add(mntmDelete);
		
		JMenuItem mntmPrintToPdf = new JMenuItem("Print to Pdf");
		mntmPrintToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(customerSelected != null){
					firePropertyChange("print", false, customerSelected);
				}
			}
		});
		mnFile.add(mntmPrintToPdf);
		
		JMenu mnView = new JMenu("View");
		mnView.setFont(new Font("Segoe UI", Font.BOLD, 12));
		menuBar.add(mnView);
		
		chckbxmntmShowDeleted = new JCheckBoxMenuItem("Show Deleted");
		chckbxmntmShowDeleted.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
				if(checkBox.getState()){
					setCustomers(false);
				}else{
					setCustomers(true);
				}
			}
		});
		mnView.add(chckbxmntmShowDeleted);
		
		JLabel lblCustomers = new JLabel("Customers");
		lblCustomers.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCustomers.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCustomers = new GridBagConstraints();
		gbc_lblCustomers.anchor = GridBagConstraints.SOUTH;
		gbc_lblCustomers.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCustomers.insets = new Insets(0, 0, 5, 0);
		gbc_lblCustomers.gridx = 0;
		gbc_lblCustomers.gridy = 1;
		mainPanel.add(lblCustomers, gbc_lblCustomers);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		mainPanel.add(scrollPane, gbc_scrollPane);
		
		list = new JList<Customer>(customers);
		list.addFocusListener(new FocusAdapter() {
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
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2){
					if(!customerSelected.isActive()){
						customerSelected.setActive(true);
						firePropertyChange("customerDeleted", true, false);
						if(!chckbxmntmShowDeleted.getState()){
							setCustomers(true);
						}else{
							setCustomers(false);
						}
					}
				}
			}
		});
		ListCellRenderer<Customer> renderer = new CustomerListRenderer();
		list.setCellRenderer(renderer);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				Customer oldValue = customerSelected;
				customerSelected = list.getSelectedValue();
				firePropertyChange("customerSelected", oldValue, customerSelected);
			}
		});
		
	}
	
	public Customer getCustomerSelected(){
		return customerSelected;
	}
	
	public JPanel getMainPanel(){
		return mainPanel;
	}

	private void setCustomers(boolean activeOnly){
		customers.removeAllElements();
		CustomerList customerList = new CustomerList();
		for(Customer customer : customerList.getCustomers()){
			if(activeOnly){
				if(customer.isActive()){
					customers.addElement(customer);
				}
			}else{
				customers.addElement(customer);
			}
		}
	}
	
	private class CustomerListRenderer implements ListCellRenderer<Customer> {
		
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Customer> jList,
				Customer value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jList, value, index, isSelected, cellHasFocus);
			if (renderer != null){
				if(value.isActive()){
					renderer.setForeground(Color.BLACK);
				}else{
					renderer.setForeground(Color.GRAY);
				}
				renderer.setText(value.getFirstName() + " " + value.getLastName());
			}
			return renderer;
		}
	}
}
