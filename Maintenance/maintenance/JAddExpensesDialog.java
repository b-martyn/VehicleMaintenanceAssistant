package maintenance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;

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
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
public class JAddExpensesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldExpenses;
	private Border defaultBorder;
	private ServiceRecord serviceRecord;
	private boolean valid = false;
	
	public JAddExpensesDialog(ServiceRecord serviceRecord){
		this.serviceRecord = serviceRecord;
		initialize();
	}
	
	public void initialize() {
		setBounds(100, 100, 251, 149);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(44dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
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
			JLabel lblTitle = new JLabel("Record Date:");
			lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblTitle, "2, 2, 3, 1");
		}
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			JLabel lblRecordDate = new JLabel(sdf.format(serviceRecord.getDate()));
			contentPanel.add(lblRecordDate, "6, 2");
		}
		{
			JLabel lblCurrentExpenses = new JLabel("Current Expenses");
			contentPanel.add(lblCurrentExpenses, "2, 4, 3, 1");
		}
		{
			JLabel lblNRecordExpenses = new JLabel(String.valueOf(serviceRecord.getExpenses()));
			contentPanel.add(lblNRecordExpenses, "6, 4");
		}
		{
			JLabel lblExpenses = new JLabel("Expenses");
			lblExpenses.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblExpenses, "2, 6");
		}
		{
			JLabel labelSign = new JLabel("$");
			contentPanel.add(labelSign, "4, 6, right, default");
		}
		{
			textFieldExpenses = new JTextField();
			defaultBorder = textFieldExpenses.getBorder();
			contentPanel.add(textFieldExpenses, "6, 6, fill, default");
			textFieldExpenses.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						validateExpenses();
						if(!textFieldExpenses.getForeground().equals(Color.RED)){
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
