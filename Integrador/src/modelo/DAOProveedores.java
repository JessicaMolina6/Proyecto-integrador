package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOProveedores {

	private  int id_proveedor;
	private String compania_proveedor, telefono, correo;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOProveedores> lista;
	private ControladorBitacora ce;

	public DAOProveedores(){
		this.id_proveedor=0;
		this.compania_proveedor="";
		this.telefono="";
		this.correo="";
		this.con =new DAOConexion();
		this.lista = FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();
	}

	public int getId_proveedor() {
		return id_proveedor;
	}

	public void setId_proveedor(int id_proveedor) {
		this.id_proveedor = id_proveedor;
	}

	public String getCompania_proveedor() {
		return compania_proveedor;
	}

	public void setCompania_proveedor(String compania_proveedor) {
		this.compania_proveedor = compania_proveedor;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public ObservableList<DAOProveedores> consultar(String consulta){
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while(rs.next()){
					DAOProveedores oProveedores= new DAOProveedores();
					oProveedores.setId_proveedor(rs.getInt("id_proveedor"));
					oProveedores.setCompania_proveedor(rs.getString("compania_proveedor"));
					oProveedores.setTelefono(rs.getString("telefono"));
					oProveedores.setCorreo(rs.getString("correo"));


					lista.add(oProveedores);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return lista;
	}

	public boolean agregar(){
		try{
			if(con.conectar()){
				String sql ="INSERT INTO Proveedor  VALUES(default, ?,?,'1',?);";
				comando = con.getConexion().prepareStatement(sql);
				comando.setString(1, this.compania_proveedor);
				comando.setString(2, this.telefono);
				comando.setString(3, this.correo);
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
				String sql = "UPDATE Proveedor SET estatus = '0' WHERE id_proveedor = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.id_proveedor);
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
			String sql ="UPDATE Proveedor SET compania_proveedor = ?, telefono = ?, correo = ? where id_proveedor = ?";
			comando =con.getConexion().prepareStatement(sql);
			comando = con.getConexion().prepareStatement(sql);
			comando.setString(1, this.compania_proveedor);
			comando.setString(2, this.telefono);
			comando.setString(3, this.correo);
			comando.setInt(4, this.id_proveedor);
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
				String sql = "UPDATE Proveedor SET estatus = '1' WHERE id_proveedor = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.id_proveedor);
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
