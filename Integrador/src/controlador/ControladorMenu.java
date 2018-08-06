package controlador;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import modelo.DAOConexion;
import modelo.DAOEmpleados;
import modelo.DAOUsuarios;

public class ControladorMenu implements Initializable{

	private ControladorVentanas instancia;
	private DAOConexion con;
	@FXML Label lblMensaje, lblHora;
	@FXML Button btnCliente, btnEmpleados, btnProducto, btnVentas,btnConfiguracion, btnReportes, btnProveedor;


	public ControladorMenu(){
		instancia=ControladorVentanas.getInstancia();
		con = new DAOConexion();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Recuperar información del usuario logueado
		DAOUsuarios usuario=(DAOUsuarios)instancia.getPrimaryStage().getUserData();
		//lblMensaje.setText("Usuario: " + usuario.getUsuario());
		lblHora.setText("Hora de acceso: "+(new Date()).toString());

		switch(usuario.getTipo()){

			case "Administrador":
				btnCliente.setDisable(false);
				btnEmpleados.setDisable(false);
				btnProducto.setDisable(false);
				btnVentas.setDisable(false);
				btnReportes.setDisable(false);

				break;
			case "invitado":
				btnCliente.setDisable(true);
				btnEmpleados.setDisable(true);
				btnProducto.setDisable(true);
				btnVentas.setDisable(false);
				btnReportes.setDisable(true);

				break;
			default:
				btnCliente.setDisable(true);
				btnEmpleados.setDisable(true);
				btnProducto.setDisable(true);
				btnVentas.setDisable(true);
				btnReportes.setDisable(true);

				break;
		}
	}

	@FXML public void clickUsuarios(){
		instancia.asignarModal("../vistas/Clientes.fxml", "Clientes");
	}
	@FXML public void clickVentas(){
		instancia.asignarModal("../vistas/Ventas.fxml", "Ventas");
	}
	@FXML public void clickEmpleados(){
		instancia.asignarModal("../vistas/Empleados.fxml", "Empleados");
	}
	@FXML public void clickProductos(){
		instancia.asignarModal("../vistas/Productos.fxml", "Productos");
	}
	@FXML public void clickReportes(){
		instancia.asignarModal("../vistas/Reportes.fxml", "Reportes");
	}
	@FXML public void clickProveedores(){
		instancia.asignarModal("../vistas/Proveedores.fxml", "Proveedores");
	}
}
