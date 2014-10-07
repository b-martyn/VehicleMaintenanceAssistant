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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SuppressWarnings("serial")
public class JCustomerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldFirstName;
	private JTextField textFieldLastName;
	private JTextField textFieldEmail;
	private Customer customer;
	
	public JCustomerDialog(Customer customer){
		this.customer = customer;
		initialize();
	}
	
	public JCustomerDialog() {
		this.customer = new Customer();
		customer.setFirstName("");
		customer.setLastName("");
		customer.setEmail("");
		initialize();
	}
	
	public void initialize(){
		setBounds(100, 100, 407, 170);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(46dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(67dlu;default):grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblFirstName = new JLabel("FirstName");
			contentPanel.add(lblFirstName, "2, 2, right, default");
		}
		{
			textFieldFirstName = new JTextField();
			textFieldFirstName.setText(customer.getFirstName());
			contentPanel.add(textFieldFirstName, "4, 2, fill, default");
			textFieldFirstName.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "2, 4, right, default");
		}
		{
			textFieldLastName = new JTextField();
			textFieldLastName.setText(customer.getLastName());
			contentPanel.add(textFieldLastName, "4, 4, fill, default");
			textFieldLastName.setColumns(10);
		}
		{
			JLabel lblEmail = new JLabel("Email");
			contentPanel.add(lblEmail, "2, 6, right, default");
		}
		{
			textFieldEmail = new JTextField();
			textFieldEmail.setText(customer.getEmail());
			contentPanel.add(textFieldEmail, "4, 6, fill, default");
			textFieldEmail.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						customer.setActive(true);
						if(textFieldFirstName.getText().equals("")){
							textFieldFirstName.setBorder(addErrorBorder());
							textFieldFirstName.setText("First Name is required");
							textFieldFirstName.setForeground(Color.RED);
							textFieldFirstName.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									textFieldFirstName.setText("");
									textFieldFirstName.setForeground(Color.BLACK);
									textFieldFirstName.removeFocusListener(this);
								}
							});
						}else{	
							customer.setFirstName(textFieldFirstName.getText());
						}
						if(textFieldLastName.getText().equals("")){
							textFieldLastName.setBorder(addErrorBorder());
							textFieldLastName.setText("Last Name is required");
							textFieldLastName.setForeground(Color.RED);
							textFieldLastName.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									textFieldLastName.setText("");
									textFieldLastName.setForeground(Color.BLACK);
									textFieldLastName.removeFocusListener(this);
								}
							});
						}else{
							customer.setLastName(textFieldLastName.getText());
						}
						customer.setEmail(textFieldEmail.getText());
						if(!textFieldFirstName.getText().contains("required") && !textFieldLastName.getText().contains("required")){
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
	
	private Border addErrorBorder(){
		Border border = BorderFactory.createLineBorder(Color.RED, 1);
		return border;
	}
	
	public Customer getCustomer(){
		return customer;
	}
}
