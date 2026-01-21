-- 1. EMPLEADOS (para hacer login)

INSERT INTO empleados (id_empleado, nombre, email, contrasena) VALUES 
('EMP001', 'María García López', 'maria.garcia@ecoembes.com', 'admin123'),
('EMP002', 'Juan Pérez Sánchez', 'juan.perez@ecoembes.com', 'admin123'),
('EMP003', 'Ana Martínez Ruiz', 'ana.martinez@ecoembes.com', 'admin123');


-- 2. PLANTAS DE PROCESAMIENTO

INSERT INTO plantas (planta_id, nombre, capacidad_disponible, tipo, tipo_puerta_de_enlace, anfitrion, puerto) VALUES 
('P001', 'Planta Madrid Centro', 5000.0, 'PROCESAMIENTO', 'REST', 'localhost', 8081),
('P002', 'Planta Barcelona Nord', 4500.0, 'PROCESAMIENTO', 'REST', 'localhost', 8081),
('P003', 'Planta Valencia Sur', 3000.0, 'PROCESAMIENTO', 'SOCKET', 'localhost', 4444);


-- 3. CONTENEDORES (con estado)


-- Contenedores Madrid (Código Postal 28XXX)
INSERT INTO contenedor (contenedor_id, localizacion, codigo_postal, capacidad, nivel_de_llenado, numero_contenedor, fecha_creacion) VALUES 
('C001', 'Calle Mayor 123, Madrid', '28013', 1000.0, 'amarillo', 1, CURRENT_TIMESTAMP - 100),
('C002', 'Av. Castellana 45, Madrid', '28046', 1200.0, 'verde', 1, CURRENT_TIMESTAMP - 95),
('C003', 'Plaza España 7, Madrid', '28008', 800.0, 'rojo', 1, CURRENT_TIMESTAMP - 90),
('C004', 'C/ Gran Vía 89, Madrid', '28013', 1000.0, 'verde', 1, CURRENT_TIMESTAMP - 85);

-- Contenedores Barcelona (Código Postal 08XXX)
INSERT INTO contenedor (contenedor_id, localizacion, codigo_postal, capacidad, nivel_de_llenado, numero_contenedor, fecha_creacion) VALUES 
('C005', 'Passeig de Gràcia 12, Barcelona', '08007', 1500.0, 'rojo', 2, CURRENT_TIMESTAMP - 80),
('C006', 'Rambla Catalunya 34, Barcelona', '08008', 1000.0, 'verde', 1, CURRENT_TIMESTAMP - 75),
('C007', 'Av. Diagonal 156, Barcelona', '08018', 900.0, 'rojo', 1, CURRENT_TIMESTAMP - 70);

-- Contenedores Valencia (Código Postal 46XXX)
INSERT INTO contenedor (contenedor_id, localizacion, codigo_postal, capacidad, nivel_de_llenado, numero_contenedor, fecha_creacion) VALUES 
('C008', 'Calle Colón 78, Valencia', '46004', 1100.0, 'amarillo', 1, CURRENT_TIMESTAMP - 65),
('C009', 'Av. Reino de Valencia 23, Valencia', '46005', 950.0, 'amarillo', 1, CURRENT_TIMESTAMP - 60);


-- 4. HISTORIAL DE USO 


-- Usos de la última semana para diferentes contenedores
INSERT INTO uso (id, contenedor_contenedor_id, fecha, nivel_llenado, peso_total) VALUES 
-- C001
(1, 'C001', CURRENT_DATE - 7, 50.0, 500.0),
(2, 'C001', CURRENT_DATE - 6, 60.0, 600.0),
(3, 'C001', CURRENT_DATE - 5, 70.0, 700.0),
(4, 'C001', CURRENT_DATE - 4, 75.0, 750.0),
(5, 'C001', CURRENT_DATE - 3, 80.0, 800.0),

-- C002
(6, 'C002', CURRENT_DATE - 7, 30.0, 360.0),
(7, 'C002', CURRENT_DATE - 5, 50.0, 600.0),
(8, 'C002', CURRENT_DATE - 3, 65.0, 780.0),

-- C003
(9, 'C003', CURRENT_DATE - 6, 60.0, 480.0),
(10, 'C003', CURRENT_DATE - 4, 80.0, 640.0),
(11, 'C003', CURRENT_DATE - 2, 90.0, 720.0),

-- C005
(12, 'C005', CURRENT_DATE - 7, 55.0, 825.0),
(13, 'C005', CURRENT_DATE - 5, 70.0, 1050.0),
(14, 'C005', CURRENT_DATE - 3, 85.0, 1275.0),

-- C007
(15, 'C007', CURRENT_DATE - 6, 70.0, 630.0),
(16, 'C007', CURRENT_DATE - 4, 85.0, 765.0),

-- C008
(17, 'C008', CURRENT_DATE - 5, 60.0, 660.0),

-- C009
(18, 'C009', CURRENT_DATE - 4, 55.0, 522.5);


-- 5. TAREAS DE ASIGNACIÓN (historial)


INSERT INTO tarea (id, contenedor_contenedor_id, empleado_id_empleado, planta_planta_id, fecha_creacion, estado) VALUES 
(1, 'C001', 'EMP001', 'P001', CURRENT_TIMESTAMP - 7, 'COMPLETADA'),
(2, 'C002', 'EMP002', 'P002', CURRENT_TIMESTAMP - 5, 'COMPLETADA'),
(3, 'C005', 'EMP003', 'P003', CURRENT_TIMESTAMP - 3, 'PENDIENTE');