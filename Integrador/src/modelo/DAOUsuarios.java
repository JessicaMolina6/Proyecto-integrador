package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOUsuarios {
	private String usuario, contrasena, tipo;
	private int numEmpleado;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOUsuarios> lista;
	private ControladorBitacora ce;

	public DAOUsuarios(){
		this.numEmpleado=0;
		this.usuario="";
		this.contrasena="";
		this.tipo="";
		this.con=new DAOConexion();
		this.lista=FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();

	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumEmpleado() {
		return numEmpleado;
	}

	public void setNumEmpleado(int numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public DAOUsuarios validarDatos(){
		DAOUsuarios oUsuarios =null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="SELECT * FROM  tipo_usuario WHERE usuario  = '" + this.usuario+"' AND contrasena = '" + this.contrasena + "'";
				comando = con.getConexion().prepareStatement(sql);
				rs=comando.executeQuery();
				while(rs.next()){
					oUsuarios=new DAOUsuarios();
					oUsuarios.numEmpleado=rs.getInt("num_empleado");
					oUsuarios.usuario=rs.getString("usuario");
					oUsuarios.tipo=rs.getString("nombre_tipo_usuario");
				}

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			con.desconectar();
		}
		return oUsuarios;
	}

	public ObservableList<DAOUsuarios> consultar(String consulta){
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs  = comando.executeQuery();
				while(rs.next()){
					DAOUsuarios oUsuarios = new DAOUsuarios();
					oUsuarios.setNumEmpleado(rs.getInt("num_empleado"));
					oUsuarios.setUsuario(rs.getString("usuario"));
					oUsuarios.setContrasena(rs.getString("contrasena"));
					oUsuarios.setTipo(rs.getString("nombre_tipo_empleado"));

					lista.add(oUsuarios);
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


	public boolean agregar() throws SQLException{
		try{
			if(con.conectar()){
				String sql="SELECT * FROM  inUsuarios(?, ?, ?, ?);";
				comando =con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.numEmpleado);
				comando.setString(2, this.tipo);
				comando.setString(3, this.usuario);
				comando.setString(4, this.contrasena);
				comando.execute();
				return true;
			}
			else{
				return false;
			}
		}
		catch(PSQLException e ){
			editar();
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
			String sql="UPDATE tipo_usuario SET usuario = ?, contrasena = ?, nombre_tipo_usuario = ? WHERE num_empleado=?";
			comando = con.getConexion().prepareStatement(sql);
			comando.setString(1, this.usuario);
			comando.setString(2, this.contrasena);
			comando.setString(3, this.tipo);
			comando.setInt(4, this.numEmpleado);
			comando.execute();
			//ce.imprimirAccion("Editar", "Catalogo");
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

	public boolean eliminar(){
		try{
			if(con.conectar()){
				String sql = "UPDATE tipo_usuario SET estatus = '0' WHERE num_empleado = ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.numEmpleado);
				comando.execute();

			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally {
			con.desconectar();
		}
	}
	public boolean reactivar(){
		try{
			if(con.conectar()){
				String sql = "UPDATE tipo_usuario SET estatus = '1' WHERE num_empleado = ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.numEmpleado);
				comando.execute();

			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally {
			con.desconectar();
		}
	}
}
