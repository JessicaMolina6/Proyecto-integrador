package modelo;

import java.sql.Connection;
import java.sql.DriverManager;


public class DAOConexion
{
	private String servidor, usuario, contrasena, puerto, base_datos;
	private Connection conexion;

	public DAOConexion()
	{
		this.servidor="localhost";//127.0.0.1
		this.usuario="postgres";
		this.contrasena="admin";
		this.puerto="5432";
		this.base_datos="ImpresionesLeo1";
	}
	//Para conectar a la base de datos
	public boolean conectar()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
			conexion = DriverManager.getConnection("jdbc:postgresql://"+servidor+":"+puerto+"/"+base_datos,usuario,contrasena);
			System.out.println("conectado a: "+conexion.getCatalog());
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("No conecto");
			return false;
		}
	}
	//Para cerrar la conexion a la base de datos
	public boolean desconectar()
	{
		try
		{
			conexion.close();
			System.out.println("Conexión cerrada");
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	//Para recuperar la conexión a la base de datos
	public Connection getConexion()
	{
		return conexion;
	}

}

