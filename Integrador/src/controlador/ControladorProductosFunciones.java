package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.DAOProductos;

public class ControladorProductosFunciones implements Initializable{

	@FXML TableView<DAOProductos>tvProductos;
	@FXML TextField txtNombre, txtClaveProducto, txtDescripcion, txtPrecioMayoreo, txtPrecioMenoreo,txtBuscador,txtRuta;
	@FXML Button btnNuevo, btnEditar, btnGuardar, btnEliminar, btnCancelar, btnSalir,btnSeleccionarImagen;
	@FXML  ComboBox<String>cbTipoProducto;
	@FXML CheckBox cbkInactivos;
	@FXML ImageView imgProducto;
	@FXML TableView<ImageView> images;

	private ObservableList<DAOProductos>lista;
	private ObservableList<String> tipo;
	private FilteredList<DAOProductos>listaBusqueda;
	private DAOProductos producto;

	public ControladorProductosFunciones() {
		producto= new DAOProductos();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tipo = FXCollections.observableArrayList();
		tipo.add("Vasos");
		tipo.add("Tazas");
		tipo.add("Bolsas");
		tipo.add("Gorras");
		cbTipoProducto.setItems(tipo);
		cbTipoProducto.getSelectionModel().select(1);
		actualizarTabla();
		bloquear();
	}

	public void actualizarTabla(){
		tvProductos.getItems().clear();
		lista=producto.consultar("SELECT * FROM Producto WHERE estatus = '1'");
		tvProductos.setItems(lista);
		tvProductos.refresh();
		listaBusqueda=new FilteredList<DAOProductos>(lista);
		for(int i=0; i<lista.size(); i++){
			ImageView eti =new ImageView("file:"+producto.getRuta());
			eti.setFitHeight(20);
			eti.setFitWidth(20);
			images.getItems().add(eti);
			System.out.println(eti);
		}
	}

	public void clickTabla(){
		if(tvProductos.getSelectionModel().getSelectedItem() != null){
			producto=tvProductos.getSelectionModel().getSelectedItem();
			txtNombre.setText(producto.getNombre());
			//txtClaveProducto.setText(producto.getClave_producto());
			txtDescripcion.setText(producto.getDescripcion());
			txtPrecioMayoreo.setText(String.valueOf(producto.getPrecioMayoreo()));
			txtPrecioMenoreo.setText(String.valueOf(producto.getPrecioMenoreo()));
			cbTipoProducto.getSelectionModel().select(producto.getTipo_producto());
			txtRuta.setText(producto.getRuta());

			Image img=new Image("file:" +producto.getRuta());
			imgProducto.setImage(img);
			System.out.println("file:" +producto.getRuta());
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
			if(txtNombre.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
			}
			else{
				producto.setNombre(txtNombre.getText());
				//producto.setClave_producto(txtClaveProducto.getText());
				producto.setDescripcion(txtDescripcion.getText());
				producto.setPrecioMayoreo(Double.parseDouble(txtPrecioMayoreo.getText()));
				producto.setPrecioMenoreo(Double.parseDouble(txtPrecioMenoreo.getText()));
				producto.setRuta(txtRuta.getText());
				if(cbTipoProducto.getSelectionModel().getSelectedItem().equalsIgnoreCase("Vasos")){
					producto.setClave_tipo_producto("vas");

				}
				bloquear();
				clear();
				btnNuevo.setDisable(false);
				if(producto.agregar()){

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
		if(txtNombre.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Todos lo campos son obligatorios");
		}
		else{
			producto.setNombre(txtNombre.getText());
			//producto.setClave_producto(txtClaveProducto.getText());
			producto.setDescripcion(txtDescripcion.getText());
			producto.setPrecioMayoreo(Double.parseDouble(txtPrecioMayoreo.getText()));
			producto.setPrecioMenoreo(Double.parseDouble(txtPrecioMenoreo.getText()));
			producto.setTipo_producto(cbTipoProducto.getSelectionModel().getSelectedItem());
			producto.setRuta(txtRuta.getText());
			if(producto.editar()){
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

		File fichero = new File(txtRuta.getText());
		fichero.delete();
	}

	@FXML public void clickEliminar(){
		if(producto.getClave_producto()>0){
			producto.eliminar();
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
		txtDescripcion.setDisable(true);

		txtPrecioMayoreo.setDisable(true);
		txtPrecioMenoreo.setDisable(true);
		cbTipoProducto.setDisable(true);


		btnGuardar.setDisable(true);
		btnEliminar.setDisable(true);
		btnEditar.setDisable(true);
		btnCancelar.setDisable(true);
	}

	public void clear(){
		txtNombre.clear();
		txtDescripcion.clear();
		//txtClaveProducto.clear();
		txtPrecioMayoreo.clear();
		txtPrecioMenoreo.clear();
		cbTipoProducto.getSelectionModel().select(0);


	}

	public void activar(){
		txtNombre.setDisable(false);
		txtDescripcion.setDisable(false);
		//txtClaveProducto.setDisable(false);
		txtPrecioMayoreo.setDisable(false);
		txtPrecioMenoreo.setDisable(false);
		cbTipoProducto.setDisable(false);

	}

	@SuppressWarnings("unchecked")
	@FXML public void clickInactivos(){
		clear();
		try{
			tvProductos.getItems().clear();
			if(cbkInactivos.isSelected()){
				lista=producto.consultar("SELECT * FROM Producto WHERE estatus = '0'");
				listaBusqueda=new FilteredList<DAOProductos>(lista);

				//Agragar una columna al tableView para restaurar el dato inactivo
				@SuppressWarnings("rawtypes")
				TableColumn columnaRestaurar=new TableColumn<>();
				tvProductos.getColumns().add(0,columnaRestaurar);
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
				if(tvProductos.getColumns().size()>2){
					tvProductos.getColumns().remove(0);
				}
				lista=producto.consultar("SELECT * FROM Producto WHERE estatus = '1'");
				listaBusqueda=new FilteredList<DAOProductos>(lista);
			}
			tvProductos.setItems(lista);
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
					producto=(DAOProductos) BotonActivar.this.getTableView().getItems().get(BotonActivar.this.getIndex());
					if(producto.reactivar()){
						lista=producto.consultar("SELECT * FROM Producto WHERE estatus = '0'");
						tvProductos.setItems(lista);
						tvProductos.refresh();
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
			tvProductos.refresh();
			tvProductos.setItems(lista);

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
				tvProductos.refresh();
				tvProductos.setItems(listaBusqueda);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@FXML public void clickSeleccionar(){
		try{
			FileChooser fileChooser = new FileChooser();
			//Método para abrir un tipo específico de archivos
			FileChooser.ExtensionFilter extension=new FileChooser.ExtensionFilter("IMG files (*.jpg)","*.jpg","*.png");
			//Se agrega la extensión al método
			fileChooser.getExtensionFilters().add(extension);
			File file = fileChooser.showOpenDialog(new Stage());
			FileInputStream fis = new FileInputStream(file); //inFile -> Archivo a copiar
			FileOutputStream fos = new FileOutputStream("F:/Archivos/Documentos/DAI/Integrador/src/ImagenesProductos/"+file.getName()); //outFile -> Copia del archivo
			FileChannel inChannel = fis.getChannel();
			FileChannel outChannel = fos.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			fis.close();
			fos.close();
			cargarImagen(file.getName());
			System.out.println(file);
			txtRuta.setText("F:/Archivos/Documentos/DAI/Integrador/src/ImagenesProductos/"+file.getName());
			//lblRuta.setText("Ruta del archivo: " + file + "\nNombre del archivo: " + file.getName());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void cargarImagen(String imagen){
		try{
			Image img=new Image("file:F:/Archivos/Documentos/DAI/Integrador/src/ImagenesProductos/"+imagen);
			imgProducto.setImage(img);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
