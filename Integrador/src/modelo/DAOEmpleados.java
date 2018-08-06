package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOEmpleados {
	private int num_empleado;
	private String nombre, apeMat, apePat, calle, avenida, colonia, cp, ciudad, area;
	private String contrasena;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOEmpleados> lista;
	private ControladorBitacora ce;

	public DAOEmpleados() {
	this.num_empleado=0;
	this.nombre="";
	this.apePat="";
	this.apeMat="";
	this.contrasena="";
	this.area="";
	this.con=new DAOConexion();
	this.lista=FXCollections.observableArrayList();
	this.ce = new ControladorBitacora();
	}


	public int getNum_empleado(){
		return num_empleado;
	}

	public void setNum_empleado(int num_empleado) {
		this.num_empleado =num_empleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApePat(String apePat) {
		this.apePat = apePat;
	}

	public String getApePat() {
		return apePat;
	}

	public void setApeMat(String apeMat) {
		this.apeMat = apeMat;
	}

	public String getApeMat() {
		return apeMat;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area= area;
	}

	public String getCalle() {
		return calle;
	}


	public void setCalle(String calle) {
		this.calle = calle;
	}


	public String getAvenida() {
		return avenida;
	}


	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}


	public String getColonia() {
		return colonia;
	}


	public void setColonia(String colonia) {
		this.colonia = colonia;
	}


	public String getCp() {
		return cp;
	}


	public void setCp(String cp) {
		this.cp = cp;
	}


	public String getCiudad() {
		return ciudad;
	}


	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	public DAOEmpleados validarDatos(){
		DAOEmpleados oEmpleado =null;
		ResultSet rs=null;
		try{
			if(con.conectar()){
				String sql="select * from empleado where nombre = '"+this.nombre+"' and contrasena = '" +this.contrasena+ "'";
				comando = con.getConexion().prepareStatement(sql);
				rs= comando.executeQuery();
				//Para recuperar información
				while(rs.next()){
					oEmpleado=new DAOEmpleados();
					oEmpleado.num_empleado=rs.getInt("num_empleado");
					oEmpleado.nombre = rs.getString("nombre");
					oEmpleado.area=rs.getString("area_empleado");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.desconectar();
		}
		return oEmpleado;
	}
	public ObservableList<DAOEmpleados>consultar(String consulta){
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando= con.getConexion().prepareStatement(consulta);
				rs=comando.executeQuery();
				while(rs.next()){
					DAOEmpleados oEmpleados=new DAOEmpleados();
					oEmpleados.setNum_empleado(rs.getInt("num_empleado"));
					oEmpleados.setNombre(rs.getString("nombre"));
					oEmpleados.setApePat(rs.getString("apellido_paterno"));
					oEmpleados.setApeMat(rs.getString("apellido_materno"));
					oEmpleados.setCalle(rs.getString("calle"));
					oEmpleados.setAvenida(rs.getString("avenida"));
					oEmpleados.setColonia(rs.getString("colonia"));
					oEmpleados.setCp(rs.getString("cp"));
					oEmpleados.setCiudad(rs.getString("ciudad"));
					oEmpleados.setArea(rs.getString("area_empleado"));
					//System.out.println(oEmpleados.getNum_empleado() +" "+oEmpleados.getNombre()+ " " + oEmpleados.getApePat()  +" " + oEmpleados.getApeMat() +" " + " " + oEmpleados.getId_are_empleado());
					lista.add(oEmpleados);
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
				String sql="SELECT * FROM inEmpleado(?,?,?,?,?,?,?,?,?,'1');";
				comando = con.getConexion().prepareStatement(sql);
				comando.setString(1, this.nombre);
				comando.setString(2, this.apePat);
				comando.setString(3, this.apeMat);
				comando.setString(4, this.calle);
				comando.setString(5, this.avenida);
				comando.setString(6, this.colonia);
				comando.setString(7, this.cp);
				comando.setString(8, this.ciudad);
				comando.setString(9, this.area);
				comando.execute();
				ce.imprimirAccion("Agregar en: ", "Catalogo empleados");
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
				String sql ="UPDATE empleado SET estatus = '0' WHERE num_empleado = ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.num_empleado);
				comando.execute();
				ce.imprimirAccion("Eliminar en: ", "Catalogo empleados");
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

	public boolean editar(){
		try{
			con.conectar();
			String sql ="UPDATE empleado SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, calle = ?, avenida = ?, colonia = ?, cp = ?, ciudad = ?, area_empleado = ? where num_empleado = ?";
			comando= con.getConexion().prepareStatement(sql);
			comando.setString(1, this.nombre);
			comando.setString(2, this.apePat);
			comando.setString(3, this.apeMat);
			comando.setString(4, this.calle);
			comando.setString(5, this.avenida);
			comando.setString(6, this.colonia);
			comando.setString(7, this.cp);
			comando.setString(8, this.ciudad);
			comando.setString(9, this.area);
			comando.setInt(10, this.num_empleado);
			comando.execute();
			//ce.imprimirAccion("Editar en: ", "Catalogo Empleados");
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
				String sql ="UPDATE empleado SET estatus = '1' WHERE num_empleado = ?";
				comando=con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.num_empleado);
				comando.execute();
				ce.imprimirAccion("Reactivar", "Catalogo empleados");
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
	
	public int consultariD(String consulta) throws SQLException{
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando= con.getConexion().prepareStatement(consulta);
				rs=comando.executeQuery();
				while(rs.next()){
					DAOEmpleados oEmpleados=new DAOEmpleados();
					oEmpleados.setNum_empleado(rs.getInt("num_empleado"));
					return rs.getInt("num_empleado");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		finally{
			con.desconectar();
		}
		return num_empleado;
		
	}
}
