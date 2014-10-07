package maintenance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
public class JRecordDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldMileage;
	private JTextField textFieldExpenses;
	private JComboBox<RecordType> comboBoxType;
	private ServiceRecord serviceRecord;
	private Border defaultBorder;
	private boolean valid = false;

	/**
	 * Launch the application.
	 */
	public JRecordDialog() {
		initialize();
	}

	/**
	 * Create the dialog.
	 */
	private void initialize() {
		setBounds(100, 100, 365, 165);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(58dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(11dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblMileage = new JLabel("Milage");
			contentPanel.add(lblMileage, "2, 2, right, default");
		}
		{
			textFieldMileage = new JTextField();
			defaultBorder = textFieldMileage.getBorder();
			contentPanel.add(textFieldMileage, "4, 2, 3, 1, fill, default");
			textFieldMileage.setColumns(10);
		}
		{
			JLabel lblExpenses = new JLabel("Expenses");
			contentPanel.add(lblExpenses, "2, 4, right, default");
		}
		{
			JLabel label = new JLabel("$");
			contentPanel.add(label, "4, 4, right, default");
		}
		{
			textFieldExpenses = new JTextField();
			contentPanel.add(textFieldExpenses, "6, 4, fill, default");
			textFieldExpenses.setColumns(10);
		}
		{
			JLabel lblType = new JLabel("Type");
			contentPanel.add(lblType, "2, 6, right, default");
		}
		{
			comboBoxType = new JComboBox<RecordType>();
			comboBoxType.setModel(new DefaultComboBoxModel<RecordType>(RecordType.values()));
			contentPanel.add(comboBoxType, "4, 6, 3, 1, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						validateMileage();
						validateExpenses();
						if(!textFieldMileage.getForeground().equals(Color.RED) && !textFieldExpenses.getForeground().equals(Color.RED)){
							serviceRecord = new ServiceRecord();
							serviceRecord.setMileage(Integer.valueOf(textFieldMileage.getText()));
							serviceRecord.setType((RecordType) comboBoxType.getSelectedItem());
							serviceRecord.addExpenses(Float.valueOf(textFieldExpenses.getText()));
							valid = true;
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public ServiceRecord getServiceRecord(){
		return serviceRecord;
	}
	
	public boolean isValid(){
		return valid;
	}
	
	private void validateMileage(){
		String value = textFieldMileage.getText();
		if(!value.isEmpty()){
			try{
				Integer.valueOf(value);
			}catch(NumberFormatException nfe){
				textFieldMileage.setBorder(addErrorBorder());
				textFieldMileage.setForeground(Color.RED);
				textFieldMileage.setText("Not a valid number");
				textFieldMileage.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent arg0) {
						textFieldMileage.setText("");
						textFieldMileage.setForeground(Color.BLACK);
						textFieldMileage.setBorder(defaultBorder);
						textFieldMileage.removeFocusListener(this);
					}
				});
			}
		}else{
			textFieldMileage.setBorder(addErrorBorder());
			textFieldMileage.setForeground(Color.RED);
			textFieldMileage.setText("Please Enter the Current Mileage");
			textFieldMileage.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent arg0) {
					textFieldMileage.setText("");
					textFieldMileage.setForeground(Color.BLACK);
					textFieldMileage.setBorder(defaultBorder);
					textFieldMileage.removeFocusListener(this);
				}
			});
		}
	}
	
	private void validateExpenses(){
		String value = textFieldExpenses.getText();
		if(value.isEmpty()){
			textFieldExpenses.setText("0");
		}else{
			try{
				Float.valueOf(value);
			}catch(NumberFormatException nfe){
				textFieldExpenses.setBorder(addErrorBorder());
				textFieldExpenses.setForeground(Color.RED);
				textFieldExpenses.setText("Not a valid amount (123.45)");
				textFieldExpenses.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent arg0) {
						textFieldExpenses.setText("");
						textFieldExpenses.setForeground(Color.BLACK);
						textFieldExpenses.setBorder(defaultBorder);
						textFieldExpenses.removeFocusListener(this);
					}
				});
			}
		}
	}

	private Border addErrorBorder(){
		Border border = BorderFactory.createLineBorder(Color.RED, 1);
		return border;
	}
}
