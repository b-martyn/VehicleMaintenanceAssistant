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

import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class JCarDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldOilChangeInterval;
	private JTextField textFieldMake;
	private JTextField textFieldModel;
	private JTextField textFieldYear;
	private JTextField textFieldOilFilter;
	private JTextField textFieldOilType;
	private JLabel lblYearError;
	private Car car;
	private Border defaultBorder;
	private boolean isConfirmed = false;
	
	public JCarDialog(Car car){
		this.car = car;
		initialize();
	}

	public JCarDialog(){
		this.car = new Car();
		car.setMake("");
		car.setModel("");
		car.setYear((short)0);
		car.setOilChangeInterval((short)0);
		car.setOilFilter("");
		car.setOilType("");
		initialize();
	}
	
	
	public void initialize() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(77dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblMake = new JLabel("Make");
			contentPanel.add(lblMake, "2, 2, right, default");
		}
		{
			textFieldMake = new JTextField();
			textFieldMake.setText(car.getMake());
			defaultBorder = textFieldMake.getBorder();
			contentPanel.add(textFieldMake, "4, 2, fill, default");
			textFieldMake.setColumns(10);
		}
		{
			JLabel lblModel = new JLabel("Model");
			contentPanel.add(lblModel, "2, 4, right, default");
		}
		{
			textFieldModel = new JTextField();
			textFieldModel.setText(car.getModel());
			contentPanel.add(textFieldModel, "4, 4, fill, default");
			textFieldModel.setColumns(10);
		}
		{
			lblYearError = new JLabel("Please enter valid year (19xx or 20xx)");
			lblYearError.setForeground(Color.RED);
			lblYearError.setVisible(false);
			lblYearError.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblYearError, "4, 6");
		}
		{
			JLabel lblYear = new JLabel("Year");
			contentPanel.add(lblYear, "2, 8, right, default");
		}
		{
			textFieldYear = new JTextField();
			if(car.getYear() == 0){
				textFieldYear.setText("");
			}else{
				textFieldYear.setText(String.valueOf(car.getYear()));
			}
			contentPanel.add(textFieldYear, "4, 8, fill, default");
			textFieldYear.setColumns(10);
		}
		{
			JLabel lblOilFilter = new JLabel("Oil Filter");
			contentPanel.add(lblOilFilter, "2, 10, right, default");
		}
		{
			textFieldOilFilter = new JTextField();
			textFieldOilFilter.setText(car.getOilFilter());
			contentPanel.add(textFieldOilFilter, "4, 10, fill, default");
			textFieldOilFilter.setColumns(10);
		}
		{
			JLabel lblOilType = new JLabel("Oil Type");
			contentPanel.add(lblOilType, "2, 12, right, default");
		}
		{
			textFieldOilType = new JTextField();
			textFieldOilType.setText(car.getOilType());
			contentPanel.add(textFieldOilType, "4, 12, fill, default");
			textFieldOilType.setColumns(10);
		}
		{
			JLabel lblOilChangeInterval = new JLabel("Oil Change Interval");
			contentPanel.add(lblOilChangeInterval, "2, 14, right, default");
		}
		{
			textFieldOilChangeInterval = new JTextField();
			if(car.getOilChangeInterval() == 0){
				textFieldOilChangeInterval.setText("");
			}else{
				textFieldOilChangeInterval.setText(String.valueOf(car.getOilChangeInterval()));
			}
			contentPanel.add(textFieldOilChangeInterval, "4, 14, fill, default");
			textFieldOilChangeInterval.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String makeFieldValue = validateFeild(textFieldMake);
						if(makeFieldValue.contains("required")){
							textFieldMake.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									textFieldMake.setText("");
									textFieldMake.setForeground(Color.BLACK);
									textFieldMake.removeFocusListener(this);
								}
							});
						}else{
							car.setMake(makeFieldValue);
						}
						String modelFieldValue = validateFeild(textFieldModel);
						if(modelFieldValue.contains("required")){
							textFieldModel.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									textFieldModel.setText("");
									textFieldModel.setForeground(Color.BLACK);
									textFieldModel.removeFocusListener(this);
								}
							});
						}else{
							car.setModel(modelFieldValue);
						}
						boolean yearIsValid = validateYear();
						if(yearIsValid){
							car.setYear(Short.valueOf(textFieldYear.getText()));
							textFieldYear.setBorder(defaultBorder);
							lblYearError.setVisible(false);
						}
						car.setOilFilter(textFieldOilFilter.getText());
						car.setOilType(textFieldOilType.getText());
						boolean intervalIsValid = validateIntervalFeild();
						if(intervalIsValid){
							textFieldOilChangeInterval.setBorder(defaultBorder);
							short interval = (short)0;
							if(textFieldOilChangeInterval.getText().isEmpty()){
								car.setOilChangeInterval(interval);
							}else{
								try{
									car.setOilChangeInterval(Short.valueOf(textFieldOilChangeInterval.getText()));
								}catch(NumberFormatException nfe){
									textFieldOilChangeInterval.setText("please enter a valid interval (1000 - 32767)");
									textFieldOilChangeInterval.setForeground(Color.RED);
									textFieldOilChangeInterval.setBorder(addErrorBorder());
									textFieldOilChangeInterval.addFocusListener(new FocusAdapter() {
										@Override
										public void focusGained(FocusEvent e) {
											textFieldOilChangeInterval.setText("");
											textFieldOilChangeInterval.setForeground(Color.BLACK);
											textFieldOilChangeInterval.removeFocusListener(this);
										}
									});
									return;
								}
							}
						}else{
							textFieldOilChangeInterval.setText("please enter a valid interval (1000 - 32767)");
							textFieldOilChangeInterval.setForeground(Color.RED);
							textFieldOilChangeInterval.setBorder(addErrorBorder());
							textFieldOilChangeInterval.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									textFieldOilChangeInterval.setText("");
									textFieldOilChangeInterval.setForeground(Color.BLACK);
									textFieldOilChangeInterval.removeFocusListener(this);
								}
							});
						}
						if(!textFieldMake.getText().contains("required") && !textFieldModel.getText().contains("required") && yearIsValid){
							car.setActive(true);
							setConfirmed(true);
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
	
	public Car getCar(){
		return car;
	}
	
	public boolean isConfirmed() {
		return isConfirmed;
	}

	private void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	private Border addErrorBorder(){
		Border border = BorderFactory.createLineBorder(Color.RED, 1);
		return border;
	}
	
	private String validateFeild(JTextField jtextField){
		jtextField.setBorder(defaultBorder);
		if(jtextField.getText().equals("") || jtextField.getText().contains("required")){
			jtextField.setBorder(addErrorBorder());
			jtextField.setText("This field is required");
			jtextField.setForeground(Color.RED);
		}
		return jtextField.getText();
	}
	
	private boolean validateYear(){
		String value = textFieldYear.getText();
		if(value.length() != 4 || !value.startsWith("19")){
			if(!value.startsWith("20")){
				textFieldYear.setBorder(addErrorBorder());
				textFieldYear.setForeground(Color.RED);
				lblYearError.setVisible(true);
				textFieldYear.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						textFieldYear.setForeground(Color.BLACK);
						textFieldYear.removeFocusListener(this);
					}
				});
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	
	private boolean validateIntervalFeild(){
		String value = textFieldOilChangeInterval.getText();
		if(value.isEmpty()){
			return true;
		}else if(value.length() < 4 || value.length() > 5){
			return false;
		}else{
			return true;
		}
	}
}
