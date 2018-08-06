package controlador;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.DAOEmpleados;
import modelo.DAOUsuarios;

public class ControladorVentanas {

	private static ControladorVentanas instancia;
	private Stage primaryStage, escenario2, escenarioProgreso;
	private Scene escena;
	private AnchorPane contenedorMenu, subcontenedor;
	private ProgressIndicator progress;

	private ControladorVentanas(){
		progress=new ProgressIndicator();
		progress.setPrefHeight(100);
		progress.setPrefWidth(100);
	}

	//Patrón Singleton: Permite asegurar que solo exista un único objeto de una clase}
	public static ControladorVentanas getInstancia(){
		if(instancia==null)
			instancia=new ControladorVentanas();
		return instancia;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	//Asignar el escenario principal
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	//Asignar menú principal
	public void asignarMenu(String ruta, String titulo, DAOUsuarios temp){
		try{
			primaryStage.setUserData(temp);
			FXMLLoader parent =new FXMLLoader(getClass().getResource(ruta));
			contenedorMenu=(AnchorPane) parent.load();
			escena =new Scene(contenedorMenu);
			escena.getStylesheets().add("vistas/css/TemaLupe.css");
			primaryStage.setScene(escena);
			primaryStage.setTitle(titulo);
			primaryStage.setResizable(false);
			primaryStage.show();
			escenario2.setResizable(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void asignarModal(String ruta, String titulo){
		try{
			FXMLLoader vista=new FXMLLoader(getClass().getResource(ruta));
			subcontenedor=(AnchorPane)vista.load();
			escena = new Scene(subcontenedor);
			escenario2=new Stage();
			escenario2.setScene(escena);
			escenario2.setTitle(titulo);
			escenario2.centerOnScreen();
			escenario2.initModality(Modality.WINDOW_MODAL);
			escenario2.initOwner(primaryStage);
			escenario2.setResizable(false);
			//escenario2.show();
			cargando();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void cerrarAcceso(){
		escenario2.close();
	}

	public void cargando(){
		VBox updatePane = new VBox();
		ColorAdjust ajuste=new ColorAdjust();
		ajuste.setHue(-0.4);
		progress.setEffect(ajuste);
		updatePane.getChildren().add(progress);
		updatePane.setStyle("-fx-background-insets: 50");
		escenarioProgreso=new Stage(StageStyle.TRANSPARENT);
		escena=new Scene(updatePane);
		escena.setFill(Color.TRANSPARENT);
		escenarioProgreso.setScene(escena);
		escenarioProgreso.show();
		Task<Void> tarea=new Task<Void>(){
			//Clase anónima
			@Override
			protected Void call() throws Exception{
				int max=10;
				for(int i=1; i<=max; i++){
					if(isCancelled()){
						break;
					}
					updateProgress(i, max);
					updateMessage(String.valueOf(i));
					Thread.sleep(50); //5 Segundos
				}
				return null;
			}
		};
		tarea.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
			@Override
			public void handle(WorkerStateEvent event){
				escenarioProgreso.hide();
				escenario2.show();
			}
		});
		//Para sincronizar Propiedades
		progress.progressProperty().bind(tarea.progressProperty());

		new Thread(tarea).start();
	}
}