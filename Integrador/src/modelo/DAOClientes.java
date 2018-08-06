package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controlador.ControladorBitacora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DAOClientes {

	private  int codUnico;
	private String  nombre, aPaterno, aMaterno, calle, avenida, colonia, cp, ciudad, rfc, correo, telefono, idTipo;
	private DAOConexion con;
	private PreparedStatement comando;
	private ObservableList<DAOClientes> lista;
	private ControladorBitacora ce;

	public DAOClientes(){
		this.codUnico=0;
		this.nombre="";
		this.aPaterno="";
		this.aMaterno="";
		this.calle="";
		this.avenida="";
		this.colonia="";
		this.cp="";
		this.ciudad="";
		this.rfc="";
		this.correo="";
		this.telefono="";
		this.con =new DAOConexion();
		this.lista = FXCollections.observableArrayList();
		this.ce=new ControladorBitacora();

	}

	public int getCodUnico() {
		return codUnico;
	}

	public void setCodUnico(int codUnico) {
		this.codUnico = codUnico;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getaPaterno() {
		return aPaterno;
	}

	public void setaPaterno(String aPaterno) {
		this.aPaterno = aPaterno;
	}

	public String getaMaterno() {
		return aMaterno;
	}

	public void setaMaterno(String aMaterno) {
		this.aMaterno = aMaterno;
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

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public ObservableList<DAOClientes> consultar(String consulta){
		ResultSet rs = null;
		try{
			if(con.conectar()){
				comando = con.getConexion().prepareStatement(consulta);
				rs = comando.executeQuery();
				while(rs.next()){
					DAOClientes oClientes = new DAOClientes();
					oClientes.setCodUnico(rs.getInt("cod_unico"));
					oClientes.setNombre(rs.getString("nombre_cliente"));
					oClientes.setaPaterno(rs.getString("apellido_paterno"));
					oClientes.setaMaterno(rs.getString("apellido_materno"));
					oClientes.setCalle(rs.getString("calle"));
					oClientes.setAvenida(rs.getString("avenida"));
					oClientes.setColonia(rs.getString("colonia"));
					oClientes.setCp(rs.getString("cp"));
					oClientes.setCiudad(rs.getString("ciudad"));
					oClientes.setRfc(rs.getString("rfc"));
					oClientes.setCorreo(rs.getString("correo_e"));
					oClientes.setTelefono(rs.getString("telefono"));
					oClientes.setIdTipo(rs.getString("id_tipo_cliente"));
					lista.add(oClientes);
					System.out.println(oClientes.getaPaterno());
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
				String sql ="SELECT * FROM inCliente(?,?,?,?,?,?,?,?,?,?,?,?,'1');";
				comando = con.getConexion().prepareStatement(sql);
				comando.setString(1, this.idTipo);
				comando.setString(2, this.nombre);
				comando.setString(3, this.aPaterno);
				comando.setString(4, this.aMaterno);
				comando.setString(5, this.calle);
				comando.setString(6, this.avenida);
				comando.setString(7, this.colonia);
				comando.setString(8, this.cp);
				comando.setString(9, this.ciudad);
				comando.setString(10, this.rfc);
				comando.setString(11, this.correo);
				comando.setString(12, this.telefono);
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
				String sql = "UPDATE Cliente SET estatus = '0' WHERE cod_unico = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.codUnico);
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
			String sql ="UPDATE Cliente SET id_tipo_cliente = ?, nombre_cliente = ?, apellido_paterno = ?, apellido_materno = ?, calle = ?, avenida = ?, colonia = ?, cp = ?, ciudad = ?, rfc = ?, correo_e = ?, telefono = ? where cod_unico = ?";
			comando =con.getConexion().prepareStatement(sql);
			comando.setString(1, this.idTipo);
			comando.setString(2, this.nombre);
			comando.setString(3, this.aPaterno);
			comando.setString(4, this.aMaterno);
			comando.setString(5, this.calle);
			comando.setString(6, this.avenida);
			comando.setString(7, this.colonia);
			comando.setString(8, this.cp);
			comando.setString(9, this.ciudad);
			comando.setString(10, this.rfc);
			comando.setString(11, this.correo);
			comando.setString(12, this.telefono);
			comando.setInt(13, this.codUnico);
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
				String sql = "UPDATE Cliente SET estatus = '1' WHERE cod_unico = ?";
				comando = con.getConexion().prepareStatement(sql);
				comando.setInt(1, this.codUnico);
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
