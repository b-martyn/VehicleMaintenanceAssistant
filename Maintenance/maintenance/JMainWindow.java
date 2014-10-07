package maintenance;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class JMainWindow {

	private JFrame frame;
	private JCustomerList customerPanel;
	private JCarList carPanel;
	private JRecordList recordPanel;
	private RecordBook recordBook;
	private ArrayList<RecordBook> allRecordBooks = new ArrayList<RecordBook>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMainWindow window = new JMainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 623, 554);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane mainSplitPane = new JSplitPane();
		mainSplitPane.setBounds(0, 0, 607, 516);
		mainSplitPane.setDividerLocation(135);
		mainSplitPane.setDividerSize(5);
		
		customerPanel = new JCustomerList();
		JPanel panelCustomers = customerPanel.getMainPanel();
		customerPanel.addPropertyChangeListener("customerSelected", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				paintCustomerPanel((JCustomerList) pce.getSource());
			}
		});
		customerPanel.addPropertyChangeListener("customerDeleted", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				paintCustomerPanel((JCustomerList) pce.getSource());
				recordBook.write();
			}
		});
		customerPanel.addPropertyChangeListener("customerEdited", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				JCustomerList source = (JCustomerList) pce.getSource();
				Customer customer = source.getCustomerSelected();
				recordBook.setCustomer(customer);
				recordBook.write();
			}
		});
		customerPanel.addPropertyChangeListener("print", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				PDFMain pdf = new PDFMain(recordBook);
				pdf.createPDF();
			}
		});
		mainSplitPane.setLeftComponent(panelCustomers);
		panelCustomers.setMinimumSize(new Dimension(135, panelCustomers.getParent().getHeight()));
		
		JSplitPane dataSplitPane = new JSplitPane();
		dataSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		dataSplitPane.setDividerLocation(182);
		mainSplitPane.setRightComponent(dataSplitPane);
		
		carPanel = new JCarList();
		carPanel.addPropertyChangeListener("carSelected", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				if(carPanel.getCarSelected().isActive()){
					recordPanel.setListModel(recordBook.getServiceRecords(RecordType.OILCHANGE), carPanel.getCarSelected());
					recordPanel.setTitle(RecordType.OILCHANGE);
				}else{
					recordPanel.clearListModel();
				}
			}
		});
		carPanel.addPropertyChangeListener("carDeleted", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce) {
				recordBook.write();
			}
		});
		carPanel.addPropertyChangeListener("carEdited", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				carPanel.setListModel(recordBook.getCustomer().getCars(), recordBook.getCustomer().getId());
				recordPanel.clearListModel();
				recordBook.write();
			}
		});
		carPanel.addPropertyChangeListener("carCreated", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent pce){
				JCarList carList = (JCarList) pce.getSource();
				Car newCar = (Car) pce.getNewValue();
				boolean isConfirmed = (boolean) pce.getOldValue();
				if(isConfirmed){
					if(recordBook != null){
						carList.clearCarSelected();
						recordPanel.clearListModel();
						Customer customer = recordBook.getCustomer();
						customer.addCar(newCar);
						carList.setListModel(customer.getCars(), customer.getId());
						recordBook.write();
					}
				}
			}
		});
		JPanel panelCars = carPanel.getMainPanel();
		dataSplitPane.setTopComponent(panelCars);
		panelCars.setMinimumSize(new Dimension(50,85));
		
		recordPanel = new JRecordList();
		JPanel panelRecords = recordPanel.getMainPanel();
		dataSplitPane.setBottomComponent(panelRecords);
		panelRecords.setMinimumSize(new Dimension(50,85));
		recordPanel.addPropertyChangeListener("serviceRecordCreated", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce){
				ServiceRecord serviceRecord = (ServiceRecord) pce.getNewValue();
				serviceRecord.setActive(true);
				Car carSelected = carPanel.getCarSelected();
				boolean isValid = (boolean) pce.getOldValue();
				if(isValid){
					if(carSelected != null){
						recordPanel.clearListModel();
						serviceRecord.setCarId(carSelected.getId());
						recordBook.addServiceRecord(serviceRecord);
						recordPanel.setListModel(recordBook.getServiceRecords(RecordType.OILCHANGE), carPanel.getCarSelected());
						recordBook.write();
					}
				}
			}
		});
		recordPanel.addPropertyChangeListener("serviceRecordEdited", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce){
				boolean isValid = (boolean) pce.getOldValue();
				if(isValid){
					recordPanel.clearListModel();
					recordPanel.setListModel(recordBook.getServiceRecords(RecordType.OILCHANGE), carPanel.getCarSelected());
					recordBook.write();
				}
			}
		});
		recordPanel.addPropertyChangeListener("serviceRecordDeleted", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce){
				recordBook.write();
			}
		});
		
		frame.getContentPane().add(mainSplitPane);
	}
	private void paintCustomerPanel(JCustomerList jCustomerList){
		recordPanel.clearListModel();
		recordPanel.removeCarId();
		recordPanel.setTitle();
		carPanel.clearListModel();
		carPanel.clearCarSelected();
		Customer customer = jCustomerList.getCustomerSelected();
		if(customer != null){
			if(allRecordBooks.size() != 0){
				for(RecordBook rBook : allRecordBooks){
					if(rBook.getCustomer().toString().equals(customer.toString())){
						recordBook = rBook;
						break;
					}
				}
				if(recordBook.getCustomer().toString().equals(customer.toString())){
					//Do nothing;
				}else{
					recordBook = customer.createCustomerData();
					allRecordBooks.add(recordBook);
				}
			}else{
				recordBook = customer.createCustomerData();
				allRecordBooks.add(recordBook);
			}
			carPanel.setListModel(recordBook.getCustomer().getCars(), recordBook.getCustomer().getId());
			if(carPanel.getCarSelected() != null){
				recordPanel.setListModel(recordBook.getServiceRecords(RecordType.OILCHANGE), carPanel.getCarSelected());
			}
		}
	}
}
