CREATE DATABASE javabeans_cafe;

CREATE USER 'usuario_practica_1'@'localhost' IDENTIFIED BY '12345';

GRANT ALL PRIVILEGES ON javabeans_cafe.* TO 'usuario_practica_1'@'localhost';
FLUSH PRIVILEGES;

USE javabeans_cafe;

CREATE TABLE informacion_sucursal (
    id_sucursal INT PRIMARY KEY AUTO_INCREMENT,
    nombre_sucursal VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    ubicacion VARCHAR(255),
    completado BOOLEAN
);

CREATE TABLE empleado(
    codigo_empleado INT PRIMARY KEY AUTO_INCREMENT,
    dpi VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(225) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    jornada_laboral VARCHAR(50) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    fecha_de_contratacion DATE NOT NULL,
    activo BOOLEAN NOT NULL
);


CREATE TABLE pago_salario (
    codigo_nomina INT PRIMARY KEY AUTO_INCREMENT,
    codigo_empleado INT NOT NULL,
    fecha_hora_emision DATETIME NOT NULL,
    tipo_pago VARCHAR(50) NOT NULL,
    monto_pago DECIMAL(10, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    FOREIGN KEY (codigo_empleado) REFERENCES empleado(codigo_empleado) 
);

CREATE TABLE insumo (
    codigo_insumo INT PRIMARY KEY AUTO_INCREMENT,
    nombre_insumo VARCHAR(100) NOT NULL,
    unidad_medida VARCHAR(50) NOT NULL,
    stock_actual INT NOT NULL,
    stock_minimo INT NOT NULL,
    costo_insumo DECIMAL(10, 2) NOT NULL
);

CREATE TABLE compra (   
    codigo_compra INT PRIMARY KEY AUTO_INCREMENT,
    codigo_insumo INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    cantidad INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    contabilizado BOOLEAN NOT NULL,
    FOREIGN KEY (codigo_insumo) REFERENCES insumo(codigo_insumo)
);

CREATE TABLE producto_menu (
    codigo_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre_producto VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio_venta DECIMAL(10, 2) NOT NULL,
    imagen LONGBLOB,
    disponible BOOLEAN NOT NULL
);

CREATE TABLE insumo_producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo_insumo INT NOT NULL,
    codigo_producto INT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (codigo_insumo) REFERENCES insumo(codigo_insumo),
    FOREIGN KEY (codigo_producto) REFERENCES producto_menu(codigo_producto),
    UNIQUE (codigo_insumo, codigo_producto)
);

CREATE TABLE mesa (
    numero_mesa INT PRIMARY KEY AUTO_INCREMENT,
    capacidad INT NOT NULL,
    estado VARCHAR(15) NOT NULL
);

CREATE TABLE pedido (
    codigo_pedido INT PRIMARY KEY AUTO_INCREMENT,
    codigo_empleado INT NOT NULL,
    numero_mesa INT NOT NULL,
    fecha_hora_ocupacion DATETIME NOT NULL,
    fecha_hora_liberacion DATETIME,
    propina DECIMAL(10, 2),
    monto_pedido DECIMAL(10, 2) NOT NULL,
    estado_cuenta VARCHAR(15) NOT NULL,
    contabilizado BOOLEAN NOT NULL,
    FOREIGN KEY (codigo_empleado) REFERENCES empleado(codigo_empleado),
    FOREIGN KEY (numero_mesa) REFERENCES mesa(numero_mesa)
);

CREATE TABLE producto_pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo_producto INT NOT NULL,
    codigo_pedido INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (codigo_producto) REFERENCES producto_menu(codigo_producto),
    FOREIGN KEY (codigo_pedido) REFERENCES pedido(codigo_pedido),
    UNIQUE (codigo_producto, codigo_pedido)
);

CREATE TABLE balance_financiero (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_hora DATETIME NOT NULL,
    monto_ingresos DECIMAL(10, 2) NOT NULL,
    monto_egresos DECIMAL(10, 2) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);