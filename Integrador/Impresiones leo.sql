--CREATE DATABASE Impresiones_Leo;

CREATE TABLE Tipo_empleado
(
	id_area_empleado CHAR(3) NOT NULL PRIMARY KEY,
	nombre_area_empleado VARCHAR(20) NOT NULL
);
CREATE TABLE Tipo_cliente
(
	id_tipo_cliente VARCHAR(5) NOT NULL PRIMARY KEY,
	area_tipo_cliente VARCHAR(15) NOT NULL
);
CREATE TABLE Tipo_producto
(
	clave_tipo_producto CHAR(5) NOT NULL PRIMARY KEY,
	nombre_producto VARCHAR(30) NOT NULL
);
CREATE TABLE Empleado
(
	num_empleado SERIAL NOT NULL PRIMARY KEY,
	id_area_empleado CHAR(3) NOT NULL,
	nombre VARCHAR(40) NOT NULL,
	apellido_paterno VARCHAR(40) NOT NULL,
	apellido_materno VARCHAR(40) NULL,
	calle VARCHAR(20) NOT NULL,
	avenida VARCHAR(20) NOT NULL,
	colonia VARCHAR(20) NOT NULL,
	cp CHAR(5) NOT NULL,
	ciudad VARCHAR(20) NOT NULL,
	fecha_contra DATE DEFAULT now() NOT NULL,
	estatus BIT NOT NULL,
	FOREIGN KEY (id_area_empleado) REFERENCES Tipo_empleado (id_area_empleado)
);
CREATE TABLE Cliente
(
	cod_unico SERIAL NOT NULL PRIMARY KEY,
	id_tipo_cliente VARCHAR(5) NOT NULL,
	nombre_cliente VARCHAR(20) NOT NULL,
	apellido_paterno VARCHAR(40) NOT NULL,
	apellido_materno VARCHAR(40) NULL,
	calle VARCHAR(20) NOT NULL,
	Avenida VARCHAR(20) NOT NULL,
	colonia VARCHAR(20) NOT NULL,
	cp CHAR(5) NOT NULL,
	ciudad VARCHAR(20) NOT NULL,
	rfc VARCHAR(13) NOT NULL,
	correo_e VARCHAR(20) NOT NULL,
	estatus BIT NOT NULL,
	FOREIGN KEY (id_tipo_cliente) REFERENCES Tipo_cliente (id_tipo_cliente)
);
CREATE TABLE telefono
(
	clave_tel SERIAL NOT NULL PRIMARY KEY,
	cod_unico SERIAL NOT NULL,
	num_empleado SERIAL NOT NULL,
	numero CHAR(10) NOT NULL,
	FOREIGN KEY (cod_unico) REFERENCES Cliente (cod_unico),
	FOREIGN KEY (num_empleado) REFERENCES Empleado (num_empleado)
);
CREATE TABLE Pedido
(
	folio SERIAL NOT NULL PRIMARY KEY,
	num_empleado SERIAL NOT NULL,
	cod_unico SERIAL NOT NULL,
	fecha_entrega DATE NOT NULL,
	fecha_pedido DATE NOT NULL,
	cantidad INTEGER NOT NULL,
	descripcion_pedido VARCHAR(255) NOT NULL,
	anticipo FLOAT (10) NOT NULL,
	estatus BIT NOT NULL,
	total INTEGER NOT NULL,
	FOREIGN KEY (num_empleado) REFERENCES Empleado (num_empleado),
	FOREIGN KEY (cod_unico) REFERENCES Cliente(cod_unico)
);
CREATE TABLE Surte
(
	num_empleado SERIAL NOT NULL,
	folio SERIAL NOT NULL,
	fecha DATE NOT NULL,
	observaciones VARCHAR(200) NULL,
	PRIMARY KEY(num_empleado,folio),
	FOREIGN KEY (num_empleado) REFERENCES Empleado (num_empleado),
	FOREIGN KEY (folio) REFERENCES Pedido (folio)
);
CREATE TABLE Producto
(
	clave_producto VARCHAR(5) NOT NULL PRIMARY KEY,
	clave_tipo_producto CHAR(5) NOT NULL,
	descripcion_producto VARCHAR(40) NOT NULL,
	precio_mayoreo FLOAT(10) NOT NULL,
	precio_menoreo FLOAT(10) NOT NULL,
	estatus BIT NOT NULL,
	FOREIGN KEY (clave_tipo_producto) REFERENCES Tipo_producto (clave_tipo_producto)
);
CREATE TABLE Detalle_producto
(
	folio SERIAL NOT NULL,
	clave_producto VARCHAR (5) NOT NULL,
	cantidad INTEGER NOT NULL,
	estatus BIT NOT NULL,
	observaciones VARCHAR(150),
	PRIMARY KEY(folio,clave_producto),
	FOREIGN KEY (folio) REFERENCES Pedido (folio),
	FOREIGN KEY (clave_producto) REFERENCES Producto (clave_producto)
);
CREATE TABLE Proveedor
(
	id_proveedor SERIAL NOT NULL PRIMARY KEY,
	compania_proveedor VARCHAR(30) NOT NULL,
	calle VARCHAR(20) NOT NULL,
	avenida VARCHAR(20) NOT NULL,
	colonia VARCHAR(20) NULL,
	codigo_postal CHAR(5) NOT NULL,
	ciudad VARCHAR(20) NOT NULL,
	telefono VARCHAR(15) NOT NULL,
	estatus BIT NOT NULL
);
CREATE TABLE Compra
(
	id_compra SERIAL NOT NULL PRIMARY KEY,
	id_proveedor SERIAL NOT NULL,
	FOREIGN KEY (id_proveedor) REFERENCES Proveedor (id_proveedor)
);
CREATE TABLE Detalle_Compra
(
	clave_producto VARCHAR(5) NOT NULL,
	id_compra SERIAL NOT NULL,
	cantidad INTEGER NOT NULL,
	precio_compra INTEGER NOT NULL,
	estatus BIT NOT NULL,
	PRIMARY KEY(clave_producto,id_compra),
	FOREIGN KEY (clave_producto) REFERENCES Producto (clave_producto),
	FOREIGN KEY (id_compra) REFERENCES Compra (id_compra)
);

--Funcion para agregar empleado
CREATE OR REPLACE FUNCTION inEmpleado(IN eid_area_empleado CHAR(3),
					 enombre VARCHAR(40),
					 eapellido_paterno VARCHAR(40),
					 eapellido_materno VARCHAR(40),
					 ecalle VARCHAR(20),
					 eavenida VARCHAR(20),
					 ecolonia VARCHAR(20),
					 ecp CHAR(5),
					 eciudad VARCHAR(20),
					 eestatus BIT)
RETURNS TABLE(id_area_empleado CHAR(3),
	      nombre VARCHAR(40),
	      apellido_paterno VARCHAR(40),
	      apellido_materno VARCHAR(40),
	      calle VARCHAR(20),
	      avenida VARCHAR(20),
	      colonia VARCHAR(20),
	      cp CHAR(5),
	      ciudad VARCHAR(20),
	      fecha_contra DATE,
	      estatus BIT)
AS $$
DECLARE existe INTEGER :=0;
BEGIN
	SELECT COUNT(*) INTO existe 
	FROM Empleado e
	WHERE e.nombre=enombre and e.apellido_paterno=eapellido_paterno and e.apellido_materno=eapellido_paterno;
	IF existe=1 THEN 
		RAISE EXCEPTION 'Ya existe un actor con ese nombre->%', enombre||''||eapellido_paterno||''||eapellido_materno
				USING HINT='Verifica la informacion';
	ELSE
		INSERT INTO Empleado(id_area_empleado,nombre,apellido_paterno,apellido_materno,calle,avenida,colonia,cp,ciudad,fecha_contra,estatus)
		VALUES (eid_area_empleado,enombre,eapellido_paterno,eapellido_materno,ecalle,eavenida,ecolonia,ecp,eciudad,now(),eestatus);
		RETURN QUERY(SELECT e.id_area_empleado,e.nombre,e.apellido_paterno,e.apellido_materno,e.calle,e.avenida,e.colonia,e.cp,e.ciudad,e.fecha_contra,e.estatus
			      FROM Empleado e
			      WHERE e.num_empleado=LASTVAL()
			      );
	END IF;
	END;
	$$
	LANGUAGE plpgsql;


SELECT * FROM inEmpleado('Mos','Emilio','Utrera','Hernandez','EmilianoZapata','Santa Julia','Obrera Campesina','94732','Orizaba','1');

INSERT INTO Tipo_empleado VALUES ('Mos','Mostrador');

SELECT *
FROM Empleado