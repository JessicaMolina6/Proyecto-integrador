package controlador;


import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.sun.prism.impl.Disposer.Record;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOEmpleados;
import modelo.DAOUsuarios;


public class ControladorEmpleados implements Initializable{

	@FXML TableView<DAOEmpleados>tvEmpleados;
	@FXML TextField txtNombre,txtApellido_p,txtApellido_m,txtCalle,txtAvenida,txtColonia,txtCp,txtCiudad, pfContrasena, txtUsuario;
	@FXML Button btnNuevo, btnGuardar, btnEditar, btnCancelar, btnReporte, btnSalir, btnEliminar;
	@FXML TextField txtBuscador;
	@FXML DatePicker dpFecha;
	@FXML ComboBox<String> cbTipo;
	@FXML ComboBox<String> cbArea;
	@FXML CheckBox ckbInactivos,ckbUsuarios;

	int a=0;

	private ObservableList<DAOEmpleados>lista;
	private ObservableList<DAOUsuarios>lista1;
	private ObservableList<String> tipos;
	private ObservableList<String> areas;
	private FilteredList<DAOEmpleados>listaBusqueda;
	private DAOEmpleados empleado;
	private DAOUsuarios usuarios;

	public ControladorEmpleados()
	{
		empleado=new DAOEmpleados();
		usuarios=new DAOUsuarios();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

		tipos=FXCollections.observableArrayList();
		tipos.add("Ninguno");
		tipos.add("Administrador");
		tipos.add("Invitado");
		cbTipo.setItems(tipos);
		cbTipo.getSelectionModel().select(0);

		areas=FXCollections.observableArrayList();
		areas.add("Mostrador");
		areas.add("Diseño");
		areas.add("Serigrafía");
		areas.add("Ayudante");
		cbArea.setItems(areas);
		cbArea.getSelectionModel().select(0);

		bloquear();
		actualizarTabla();
	}

	public void clickTabla(){
		if(tvEmpleados.getSelectionModel().getSelectedItem() != null){
			empleado=tvEmpleados.getSelectionModel().getSelectedItem();
			txtNombre.setText(empleado.getNombre());
			txtApellido_p.setText(empleado.getApePat());
			txtApellido_m.setText(empleado.getApeMat());
			txtCalle.setText(empleado.getCalle());
			txtAvenida.setText(empleado.getAvenida());
			txtColonia.setText(empleado.getColonia());
			txtCp.setText(empleado.getCp());
			txtCiudad.setText(empleado.getCiudad());
			cbArea.getSelectionModel().select(empleado.getArea());

			btnEditar.setDisable(false);
			btnEliminar.setDisable(false);
			btnCancelar.setDisable(false);
			activar();
			btnNuevo.setDisable(true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Seleccione un usuario de la tabla");
		}
	}

	public void actualizarTabla()
	{
		tvEmpleados.getItems().clear();
		lista=empleado.consultar("SELECT * FROM empleado WHERE estatus='1'");
		tvEmpleados.setItems(lista);
		tvEmpleados.refresh();
		listaBusqueda=new FilteredList<DAOEmpleados>(lista);


	}

	@FXML public void clickNuevo() throws SQLException{
		//empleado.num();
		System.out.println(empleado.consultariD("SELECT num_empleado FROM empleado ORDER BY num_empleado DESC LIMIT 1;"));
		activar();
		btnNuevo.setDisable(true);
		btnGuardar.setDisable(false);
		btnCancelar.setDisable(false);
	}

	@FXML public void clickGuardar(){
		try{
			if(txtNombre.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtCalle.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtCp.getText().trim().isEmpty() || txtCiudad.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
			}
			else{
				empleado.setNombre(txtNombre.getText());
				empleado.setApePat(txtApellido_p.getText());
				empleado.setApeMat(txtApellido_m.getText());
				empleado.setContrasena(pfContrasena.getText());
				empleado.setCalle(txtCalle.getText());
				empleado.setAvenida(txtAvenida.getText());
				empleado.setColonia(txtColonia.getText());
				empleado.setCp(txtCp.getText());
				empleado.setCiudad(txtCiudad.getText());
				empleado.setArea(cbArea.getSelectionModel().getSelectedItem());

				bloquear();
				clear();
				btnNuevo.setDisable(false);
				if(empleado.agregar()){
					if(!cbTipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("Ninguno")){
					//	empleado.consultar("SELECT num_empleado FROM empleado ORDER BY num_empleado DESC LIMIT 1;");
						this.usuarios.setUsuario(txtUsuario.getText());
						this.usuarios.setContrasena(pfContrasena.getText());
						this.usuarios.setTipo(cbTipo.getSelectionModel().getSelectedItem());
						this.usuarios.setNumEmpleado(empleado.consultariD("SELECT num_empleado FROM empleado ORDER BY num_empleado DESC LIMIT 1;"));
						System.out.println(empleado.getNum_empleado());
						usuarios.agregar();
					}
					JOptionPane.showMessageDialog(null, "Agregado");
					actualizarTabla();
				}
				else{
					JOptionPane.showMessageDialog(null, "Error");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@FXML public void clickEditar() throws HeadlessException, SQLException{
		if(txtNombre.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtApellido_p.getText().trim().isEmpty() || txtCalle.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtCp.getText().trim().isEmpty() || txtCiudad.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
		}
		else{
			this.empleado.setNombre(txtNombre.getText());
			this.empleado.setApePat(txtApellido_p.getText());
			this.empleado.setApeMat(txtApellido_m.getText());
			this.empleado.setContrasena(pfContrasena.getText());
			this.empleado.setCalle(txtCalle.getText());
			this.empleado.setAvenida(txtAvenida.getText());
			this.empleado.setColonia(txtColonia.getText());
			this.empleado.setCp(txtCp.getText());
			this.empleado.setCiudad(txtCiudad.getText());
			this.empleado.setArea(cbArea.getSelectionModel().getSelectedItem());
			if(!cbTipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("Ninguno")){
				System.out.println(txtUsuario.getText());
				this.usuarios.setUsuario(txtUsuario.getText());
				this.usuarios.setContrasena(pfContrasena.getText());
				this.usuarios.setTipo(cbTipo.getSelectionModel().getSelectedItem());
				this.usuarios.setNumEmpleado(empleado.getNum_empleado());
				System.out.println(empleado.getNum_empleado());
			}

			if(empleado.editar() && usuarios.agregar()){
				JOptionPane.showMessageDialog(null, "Modificación Realizada.");
				actualizarTabla();
				clear();
				bloquear();
			}
			else{
				JOptionPane.showMessageDialog(null, "¡Error! \nOcurrió un error");
			}
		}
	}


	@FXML public void clickCancelar(){
		bloquear();
		clear();
		btnNuevo.setDisable(false);
	}

	@FXML public void clickEliminar(){
		if(empleado.getNum_empleado()>0){
			empleado.eliminar();
			actualizarTabla();
		}
		else{
			JOptionPane.showMessageDialog(null, "Error");
		}
		clear();
		bloquear();
	}
	@FXML public void clickSalir(){
		ControladorVentanas cv = ControladorVentanas.getInstancia();
		cv.cerrarAcceso();
	}

	public void bloquear(){
		txtNombre.setDisable(true);
		txtApellido_p.setDisable(true);
		txtApellido_m.setDisable(true);
		txtCalle.setDisable(true);
		txtAvenida.setDisable(true);
		txtColonia.setDisable(true);
		txtCp.setDisable(true);
		txtCiudad.setDisable(true);
		cbTipo.setDisable(true);
		dpFecha.setDisable(true);
		cbArea.setDisable(true);
		btnGuardar.setDisable(true);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(true);
		txtUsuario.setDisable(true);
		pfContrasena.setDisable(true);
	}

	public void clear(){
		txtNombre.clear();
		txtApellido_p.clear();
		txtApellido_m.clear();
		txtCalle.clear();
		txtAvenida.clear();
		txtColonia.clear();
		txtCp.clear();
		txtCiudad.clear();
		cbTipo.getSelectionModel().select(1);
		pfContrasena.clear();

	}

	public void activar(){
		txtNombre.setDisable(false);
		txtApellido_p.setDisable(false);
		txtApellido_m.setDisable(false);
		txtCalle.setDisable(false);
		txtAvenida.setDisable(false);
		txtColonia.setDisable(false);
		txtCp.setDisable(false);
		txtCiudad.setDisable(false);
		cbTipo.setDisable(false);
		dpFecha.setDisable(false);
		cbArea.setDisable(false);
	}

	@SuppressWarnings("unchecked")
	@FXML public void clickInactivos(){
		clear();
		try{
			tvEmpleados.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista=empleado.consultar("SELECT * FROM empleado WHERE estatus = '0'");
				listaBusqueda=new FilteredList<DAOEmpleados>(lista);

				//Agragar una columna al tableView para restaurar el dato inactivo
				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvEmpleados.getColumns().add(0,columnaRestaurar);
				columnaRestaurar.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>(){
							public ObservableValue<Boolean> call(CellDataFeatures<Record, Boolean>param){
								return new SimpleBooleanProperty(param.getValue()!=null);
							}

							@Override
							public ObservableValue<Boolean> call(
									javafx.scene.control.TableColumn.CellDataFeatures<Record, Boolean> param) {
								// TODO Auto-generated method stub
								return null;
							}
						});
				columnaRestaurar.setCellFactory(
						new Callback<TableColumn<Record, Boolean>, TableCell<Record,Boolean>>(){
							@Override
							public TableCell<Record,Boolean> call(TableColumn<Record, Boolean>param){
								return new BotonActivar();
							}
						});

			}
			else{
				//Si está desactivado mustran los activos
				if(tvEmpleados.getColumns().size()>2){
					tvEmpleados.getColumns().remove(0);
				}
				lista=empleado.consultar("select * from empleado where estatus='1'");
				listaBusqueda=new FilteredList<DAOEmpleados>(lista);
			}
			tvEmpleados.setItems(lista);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private class BotonActivar extends TableCell<Record, Boolean>{
		Button cellButton;
		Image imagen;
		ImageView contenedor;

		BotonActivar(){
			imagen =new Image("vistas/Img/restaurar.png");
			contenedor=new ImageView(imagen);
			contenedor.setFitHeight(15);
			contenedor.setFitWidth(15);
			cellButton=new Button("",contenedor);

			cellButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					empleado=(DAOEmpleados) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(empleado.reactivar()){
						lista=empleado.consultar("SELECT * FROM empleado WHERE estatus = '0'");
						tvEmpleados.setItems(lista);
						tvEmpleados.refresh();
					}

				}
			});
		}
		@Override
		protected void updateItem(Boolean t, boolean empty){
			super.updateItem(t, empty);
			if(!empty){
				setGraphic(cellButton);
			}
		}
	}
	@FXML public void textChange_busqueda(){
		if(txtBuscador.getText().trim().isEmpty()){
			tvEmpleados.refresh();
			tvEmpleados.setItems(lista);

		}
		else{
			try{
				listaBusqueda.setPredicate(DAOEmpleados->{
					if(DAOEmpleados.getNombre().toLowerCase().contains(txtBuscador.getText().toLowerCase())){
						return true;
					}
					else{
						return false;
					}
				});
				tvEmpleados.refresh();
				tvEmpleados.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@FXML public void activarCamUsuario(){
		if(!cbTipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("Ninguno")){
			txtUsuario.setDisable(false);
			pfContrasena.setDisable(false);
			a++;
		}
		else{
			txtUsuario.setDisable(true);
			pfContrasena.setDisable(true);
			a=0;
		}
	}

	@SuppressWarnings("unchecked")
	@FXML public void clickUsers(){
		clear();
		try{
			tvEmpleados.getItems().clear();
			if(ckbUsuarios.isSelected()){
				lista1=usuarios.consultar("SELECT * FROM empleado WHERE estatus = '1'");
				listaBusqueda=new FilteredList<DAOEmpleados>(lista);

				//Agragar una columna al tableView para restaurar el dato inactivo
				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvEmpleados.getColumns().add(0,columnaRestaurar);
				columnaRestaurar.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>(){
							public ObservableValue<Boolean> call(CellDataFeatures<Record, Boolean>param){
								return new SimpleBooleanProperty(param.getValue()!=null);
							}

							@Override
							public ObservableValue<Boolean> call(
									javafx.scene.control.TableColumn.CellDataFeatures<Record, Boolean> param) {
								// TODO Auto-generated method stub
								return null;
							}
						});
				columnaRestaurar.setCellFactory(
						new Callback<TableColumn<Record, Boolean>, TableCell<Record,Boolean>>(){
							@Override
							public TableCell<Record,Boolean> call(TableColumn<Record, Boolean>param){
								return new BotonActivar();
							}
						});

			}
			else{
				//Si está desactivado mustran los activos
				if(tvEmpleados.getColumns().size()>2){
					tvEmpleados.getColumns().remove(0);
				}
				lista=empleado.consultar("select * from empleado where estatus='1'");
				listaBusqueda=new FilteredList<DAOEmpleados>(lista);
			}
			tvEmpleados.setItems(lista);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}//Cierra la clase