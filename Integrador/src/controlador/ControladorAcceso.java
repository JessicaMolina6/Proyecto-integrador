package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.DAOEmpleados;
import modelo.DAOUsuarios;


public class ControladorAcceso implements Initializable{

	@FXML TextField txtUsuario;
	@FXML PasswordField pfContrasena;

	private DAOUsuarios usuario;
	private ControladorVentanas cv;
	int a=5;


	public ControladorAcceso(){
		usuario =new DAOUsuarios();
		cv=ControladorVentanas.getInstancia();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtUsuario.textProperty().addListener((observable, oldValue, newValue)->{
			//Se asigna al valor anterior
			if(!newValue.matches("[a-zA-Z0-9]{0,20}") || newValue.length()>20){
				((StringProperty) observable).setValue(oldValue);
			}else{
				//Se asigna el nuevo valor, porque si coincide con la expresión
				((StringProperty)observable).setValue(newValue);
			}
		});
	}

	@FXML public void clickIniciarSesion(){
		try{
			if(txtUsuario.getText().trim().isEmpty() || pfContrasena.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Existen campos vacíos");
			}else{
				usuario.setUsuario(txtUsuario.getText());
				usuario.setContrasena(pfContrasena.getText());
				DAOUsuarios temp= usuario.validarDatos();
				if(temp!= null){
					//Valida si el usuario está validado
					cv.cerrarAcceso();
					cv.asignarMenu("../vistas/Menu.fxml", "Bienvenido" + temp.getUsuario(), temp);
				}else{//Si el usuario no está registrado
					JOptionPane.showMessageDialog(null, "Imagen capturada, llamando a la policía");
					txtUsuario.clear();
					pfContrasena.clear();

				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
