package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.sun.prism.impl.Disposer.Record;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.DAOClientes;
import modelo.DAOProveedores;

public class ControladorProveedores implements Initializable{


	@FXML TableView<DAOProveedores> tvProveedores;
	@FXML Button btnNuevo, btnGuardar, btnEliminar, btnEditar, btnCancelar;
	@FXML TextField txtNombre, txtTelefono, txtCorreo, txtBuscador;
	@FXML CheckBox ckbVer, ckbInactivos;

	private ObservableList<DAOProveedores> lista;
	private ObservableList<String> tipos;
	private DAOProveedores proveedor;
	private FilteredList<DAOProveedores> listaBusqueda;

	public ControladorProveedores() {
		proveedor =new DAOProveedores();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		actualizarTabla();
		bloquear();
	}


	public void actualizarTabla(){
		tvProveedores.getItems().clear();
		lista=proveedor.consultar("SELECT * FROM Proveedor WHERE estatus = '1'");
		tvProveedores.setItems(lista);
		tvProveedores.refresh();
		listaBusqueda=new FilteredList<DAOProveedores>(lista);
	}

	public void clickTabla(){
		if(tvProveedores.getSelectionModel().getSelectedItem() != null){
			proveedor=tvProveedores.getSelectionModel().getSelectedItem();
			txtNombre.setText(proveedor.getCompania_proveedor());
			txtCorreo.setText(proveedor.getCorreo());
			txtTelefono.setText(proveedor.getTelefono());


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
			if(txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
			}
			else{
				proveedor.setCompania_proveedor(txtNombre.getText());
				proveedor.setTelefono(txtTelefono.getText());
				proveedor.setCorreo(txtCorreo.getText());
				bloquear();
				clear();
				btnNuevo.setDisable(false);
				if(proveedor.agregar()){

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
		if(txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
		}
		else{
			proveedor.setCompania_proveedor(txtNombre.getText());
			proveedor.setTelefono(txtTelefono.getText());
			proveedor.setCorreo(txtCorreo.getText());

			if(proveedor.editar()){
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
		if(proveedor.getId_proveedor()>0){
			proveedor.eliminar();
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
		txtCorreo.setDisable(true);
		txtTelefono.setDisable(true);


		btnGuardar.setDisable(true);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(true);
	}

	public void clear(){
		txtNombre.clear();
		txtCorreo.clear();
		txtTelefono.clear();

	}

	public void activar(){
		txtNombre.setDisable(false);
		txtCorreo.setDisable(false);
		txtTelefono.setDisable(false);
	}


	@SuppressWarnings("unchecked")
	@FXML public void clickInactivos(){
		clear();
		try{
			tvProveedores.getItems().clear();
			if(ckbInactivos.isSelected()){
				lista=proveedor.consultar("SELECT * FROM Proveedor WHERE estatus = '0'");
				listaBusqueda=new FilteredList<DAOProveedores>(lista);

				//Agragar una columna al tableView para restaurar el dato inactivo
				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvProveedores.getColumns().add(0,columnaRestaurar);
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
				if(tvProveedores.getColumns().size()>2){
					tvProveedores.getColumns().remove(0);
				}
				lista=proveedor.consultar("select * from Proveedor where estatus='1'");
				listaBusqueda=new FilteredList<DAOProveedores>(lista);
			}
			tvProveedores.setItems(lista);
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
					proveedor=(DAOProveedores) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(proveedor.reactivar()){
						lista=proveedor.consultar("SELECT * FROM Proveedor WHERE estatus = '0'");
						tvProveedores.setItems(lista);
						tvProveedores.refresh();
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
			tvProveedores.refresh();
			tvProveedores.setItems(lista);

		}
		else{
			try{
				listaBusqueda.setPredicate(DAOEmpleados->{
					if(DAOEmpleados.getCompania_proveedor().toLowerCase().contains(txtBuscador.getText().toLowerCase())){
						return true;
					}
					else{
						return false;
					}
				});
				tvProveedores.refresh();
				tvProveedores.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
