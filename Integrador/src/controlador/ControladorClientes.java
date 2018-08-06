package controlador;

import java.awt.HeadlessException;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOClientes;
import modelo.DAOEmpleados;

public class ControladorClientes  implements Initializable{

	@FXML TableView<DAOClientes> tvClientes;
	@FXML Button btnNuevo, btnGuardar, btnEliminar, btnEditar, btnCancelar;
	@FXML TextField txtNombre, txtApePat, txtApeMat, txtCalle, txtAvenida, txtColonia, txtCp, txtCiudad, txtRfc, txtCorreo, txtTelefono,txtBuscador;
	@FXML ComboBox<String> cbTipo;
	@FXML CheckBox ckbInactivos;

	private ObservableList<DAOClientes> lista;
	private ObservableList<String> tipos;
	private DAOClientes cliente;
	private FilteredList<DAOClientes> listaBusqueda;

	 public ControladorClientes() {
		cliente=new DAOClientes();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tipos = FXCollections.observableArrayList();
		tipos.add("Pref");
		tipos.add("Comùn");
		cbTipo.setItems(tipos);
		cbTipo.getSelectionModel().select(1);
		actualizarTabla();
		bloquear();

	}

	public void actualizarTabla(){
		tvClientes.getItems().clear();
		lista=cliente.consultar("SELECT * FROM Cliente WHERE estatus = '1'");
		tvClientes.setItems(lista);
		tvClientes.refresh();
		listaBusqueda=new FilteredList<DAOClientes>(lista);
	}


	public void clickTabla(){
		if(tvClientes.getSelectionModel().getSelectedItem() != null){
			cliente=tvClientes.getSelectionModel().getSelectedItem();
			txtNombre.setText(cliente.getNombre());
			txtApePat.setText(cliente.getaPaterno());
			txtApeMat.setText(cliente.getaMaterno());
			txtCalle.setText(cliente.getCalle());
			txtAvenida.setText(cliente.getAvenida());
			txtColonia.setText(cliente.getColonia());
			txtCp.setText(cliente.getCp());
			txtCiudad.setText(cliente.getCiudad());
			txtRfc.setText(cliente.getRfc());
			txtCorreo.setText(cliente.getCorreo());
			txtTelefono.setText(cliente.getTelefono());
			cbTipo.getSelectionModel().select(cliente.getIdTipo());

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

	@FXML public void clickNuevo(){
		//empleado.num();
		//System.out.println(empleado.consultariD("SELECT num_empleado FROM empleado ORDER BY num_empleado DESC LIMIT 1;"));
		activar();
		btnNuevo.setDisable(true);
		btnGuardar.setDisable(false);
		btnCancelar.setDisable(false);
	}

	@FXML public void clickGuardar(){
		try{
			if(txtNombre.getText().trim().isEmpty() || txtApePat.getText().trim().isEmpty() || txtApeMat.getText().trim().isEmpty() ||  txtCalle.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtCp.getText().trim().isEmpty() || txtCiudad.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
			}
			else{
				cliente.setNombre(txtNombre.getText());
				cliente.setaPaterno(txtApePat.getText());
				cliente.setaMaterno(txtApeMat.getText());
				cliente.setCalle(txtCalle.getText());
				cliente.setAvenida(txtAvenida.getText());
				cliente.setColonia(txtColonia.getText());
				cliente.setCp(txtCp.getText());
				cliente.setCiudad(txtCiudad.getText());
				cliente.setIdTipo(cbTipo.getSelectionModel().getSelectedItem());
				cliente.setTelefono(txtTelefono.getText());
				bloquear();
				clear();
				btnNuevo.setDisable(false);
				if(cliente.agregar()){

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


	@FXML public void clickEditar(){
		if(txtNombre.getText().trim().isEmpty() || txtApePat.getText().trim().isEmpty() || txtApeMat.getText().trim().isEmpty() ||  txtCalle.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtAvenida.getText().trim().isEmpty() || txtColonia.getText().trim().isEmpty() || txtCp.getText().trim().isEmpty() || txtCiudad.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
		}
		else{
			cliente.setNombre(txtNombre.getText());
			cliente.setaPaterno(txtApePat.getText());
			cliente.setaMaterno(txtApeMat.getText());
			cliente.setCalle(txtCalle.getText());
			cliente.setAvenida(txtAvenida.getText());
			cliente.setColonia(txtColonia.getText());
			cliente.setCp(txtCp.getText());
			cliente.setCiudad(txtCiudad.getText());
			cliente.setIdTipo(cbTipo.getSelectionModel().getSelectedItem());
			cliente.setTelefono(txtTelefono.getText());

			if(cliente.editar()){
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
		if(cliente.getCodUnico()>0){
			cliente.eliminar();
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
		txtApePat.setDisable(true);
		txtApeMat.setDisable(true);
		txtCalle.setDisable(true);
		txtAvenida.setDisable(true);
		txtColonia.setDisable(true);
		txtCp.setDisable(true);
		txtCiudad.setDisable(true);
		txtRfc.setDisable(true);
		txtCorreo.setDisable(true);
		txtTelefono.setDisable(true);
		cbTipo.setDisable(true);

		btnGuardar.setDisable(true);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(true);
	}

	public void clear(){
		txtNombre.clear();
		txtApePat.clear();
		txtApeMat.clear();
		txtCalle.clear();
		txtAvenida.clear();
		txtColonia.clear();
		txtCp.clear();
		txtCiudad.clear();
		txtCorreo.clear();
		txtTelefono.clear();
		cbTipo.getSelectionModel().select(1);
	}

	public void activar(){
		txtNombre.setDisable(false);
		txtApePat.setDisable(false);
		txtApeMat.setDisable(false);
		txtCalle.setDisable(false);
		txtAvenida.setDisable(false);
		txtColonia.setDisable(false);
		txtCp.setDisable(false);
		txtCiudad.setDisable(false);
		txtCorreo.setDisable(false);
		txtTelefono.setDisable(false);
		cbTipo.setDisable(false);
	}





	@SuppressWarnings("unchecked")
	@FXML public void clickInactivos(){
		clear();
		try{
			tvClientes.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista=cliente.consultar("SELECT * FROM Cliente WHERE estatus = '0'");
				listaBusqueda=new FilteredList<DAOClientes>(lista);

				//Agragar una columna al tableView para restaurar el dato inactivo
				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvClientes.getColumns().add(0,columnaRestaurar);
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
				if(tvClientes.getColumns().size()>2){
					tvClientes.getColumns().remove(0);
				}
				lista=cliente.consultar("select * from Cliente where estatus='1'");
				listaBusqueda=new FilteredList<DAOClientes>(lista);
			}
			tvClientes.setItems(lista);
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
					cliente=(DAOClientes) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(cliente.reactivar()){
						lista=cliente.consultar("SELECT * FROM empleado WHERE estatus = '0'");
						tvClientes.setItems(lista);
						tvClientes.refresh();
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
			tvClientes.refresh();
			tvClientes.setItems(lista);

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
				tvClientes.refresh();
				tvClientes.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
