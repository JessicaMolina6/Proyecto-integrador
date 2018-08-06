package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import modelo.DAOConexion;

public class ControladorProductos implements Initializable{

	private ControladorVentanas instancia;
	private DAOConexion con;

	@FXML Button btnNuevo, btnEliminar, btnEditar, btnCancelar, btnSalir;

	public ControladorProductos(){
		instancia=ControladorVentanas.getInstancia();
		con=new DAOConexion();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	@FXML public void clickBoton(){
		instancia.asignarModal("../vistas/AgragarProducto.fxml", "Productos");
	}
}
