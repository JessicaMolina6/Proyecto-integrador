package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOProductos {

	private int clave_producto;
	private String clave_tipo_producto, tipo_producto, descripcion, nombre, ruta;
	private double precioMayoreo, precioMenoreo;

	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOProductos> lista;
	private ControladorBitacora ce;

	public DAOProductos(){
		this.clave_producto = 0;
		this.clave_tipo_producto = "";
		this.tipo_producto="";
		this.descripcion="";
		this.precioMayoreo=0;
		this.precioMenoreo=0;
		this.ruta="";
		this.nombre="";

		this.con=new DAOConexion();
		this.lista = FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getClave_producto() {
		return clave_producto;
	}

	public void setClave_producto(int clave_producto) {
		this.clave_producto = clave_producto;
	}

	public String getClave_tipo_producto() {
		return clave_tipo_producto;
	}

	public void setClave_tipo_producto(String clave_tipo_producto) {
		this.clave_tipo_producto = clave_tipo_producto;
	}

	public String getTipo_producto() {
		return tipo_producto;
	}

	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecioMayoreo() {
		return precioMayoreo;
	}

	public void setPrecioMayoreo(double precioMayoreo) {
		this.precioMayoreo = precioMayoreo;
	}

	public double getPrecioMenoreo() {
		return precioMenoreo;
	}

	public void setPrecioMenoreo(double precioMenoreo) {
		this.precioMenoreo = precioMenoreo;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta= ruta;
	}


	public ObservableList<DAOProductos> consultar(String consulta){
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while(rs.next()){
					DAOProductos oProductos = new DAOProductos();
					oProductos.setClave_producto(rs.getInt("clave_producto"));
					oProductos.setClave_tipo_producto(rs.getString("clave_tipo_producto"));
					oProductos.setDescripcion(rs.getString("descripcion_producto"));
					oProductos.setPrecioMayoreo(rs.getDouble("precio_mayoreo"));
					oProductos.setPrecioMenoreo(rs.getDouble("precio_menoreo"));
					oProductos.setNombre(rs.getString("nombre_producto"));
					oProductos.setRuta(rs.getString("ruta_imagen"));

					lista.add(oProductos);
					//System.out.println(oClientes.getaPaterno());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return lista;
	}

	public boolean agregar(){
		try{
			if(con.conectar()){
				String sql ="INSERT INTO Producto VALUES(default,?,?,?,?,'1',?,?);";
				comando = con.getConexion().prepareStatement(sql);
				//comando.setInt(1, this.clave_producto);
				comando.setString(1, this.clave_tipo_producto);
				comando.setString(2, this.descripcion);
				comando.setDouble(3, this.precioMayoreo);
				comando.setDouble(4, this.precioMenoreo);
				comando.setString(5, this.nombre);
				comando.setString(6, this.ruta);
				comando.execute();
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			con.desconectar();
		}
	}

	public boolean eliminar(){
		try{
			if(con.conectar()){
				String sql = "UPDATE Producto SET estatus = '0' WHERE clave_producto = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_producto);
				comando.execute();
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			con.desconectar();
		}
	}

	public boolean editar(){
		try{
			con.conectar();
			String sql ="UPDATE Producto SET clave_tipo_producto = ?, descripcion_producto = ?, precio_mayoreo = ?, precio_menoreo = ?, ruta_imagen=?, nombre_producto=? where clave_producto = ?";
			comando = con.getConexion().prepareStatement(sql);
			comando.setString(1, this.clave_tipo_producto);
			comando.setString(2, this.descripcion);
			comando.setDouble(3, this.precioMayoreo);
			comando.setDouble(4, this.precioMenoreo);
			comando.setString(5, this.ruta);
			comando.setString(6, this.nombre);
			comando.setInt(7, this.clave_producto);
			comando.execute();
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			con.desconectar();
		}
	}

	public boolean reactivar(){
		try{
			if(con.conectar()){
				String sql = "UPDATE Producto SET estatus = '1' WHERE clave_producto = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.clave_producto);
				comando.execute();
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			con.desconectar();
		}
	}
}
