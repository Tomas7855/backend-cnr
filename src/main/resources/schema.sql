-- ============================================================================
-- Script SQL para Oracle Database
-- Sistema de Gestión de Expedientes - Centro Nacional de Registros
-- ============================================================================

-- Eliminar tabla si existe (para desarrollo)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE EXPEDIENTE_CIUDADANO CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

-- Eliminar secuencia si existe
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE SEQ_EXPEDIENTE';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

-- ============================================================================
-- Crear Secuencia para autogeneración de IDs
-- ============================================================================
CREATE SEQUENCE SEQ_EXPEDIENTE
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================================================
-- Crear Tabla Principal: EXPEDIENTE_CIUDADANO
-- ============================================================================
CREATE TABLE EXPEDIENTE_CIUDADANO (
    -- Campos principales
    id                      NUMBER(19) NOT NULL,
    nombre_completo         VARCHAR2(200) NOT NULL,
    dui_documento           VARCHAR2(10) NOT NULL,
    direccion               VARCHAR2(500),
    telefono                VARCHAR2(9),
    fecha_nacimiento        DATE NOT NULL,
    estado_expediente       VARCHAR2(20) NOT NULL,
    fecha_registro          TIMESTAMP(6) NOT NULL,
    
    -- Campos de auditoría - Creación
    usuario_creacion        VARCHAR2(100) NOT NULL,
    fecha_creacion          TIMESTAMP(6) NOT NULL,
    
    -- Campos de auditoría - Modificación
    usuario_modificacion    VARCHAR2(100),
    fecha_modificacion      TIMESTAMP(6),
    
    -- Campos de auditoría - Eliminación
    usuario_eliminacion     VARCHAR2(100),
    fecha_eliminacion       TIMESTAMP(6),
    
    -- Soft Delete
    eliminado               NUMBER(1) DEFAULT 0 NOT NULL,
    
    -- Constraints
    CONSTRAINT PK_EXPEDIENTE PRIMARY KEY (id),
    CONSTRAINT UK_DUI_DOCUMENTO UNIQUE (dui_documento),
    CONSTRAINT CHK_ELIMINADO CHECK (eliminado IN (0, 1)),
    CONSTRAINT CHK_ESTADO CHECK (estado_expediente IN ('ACTIVO', 'INACTIVO', 'EN_REVISION', 'ARCHIVADO'))
);

-- ============================================================================
-- Crear Índices para optimizar consultas
-- ============================================================================
CREATE INDEX IDX_NOMBRE_COMPLETO ON EXPEDIENTE_CIUDADANO(nombre_completo);
CREATE INDEX IDX_ELIMINADO ON EXPEDIENTE_CIUDADANO(eliminado);
CREATE INDEX IDX_ESTADO_EXPEDIENTE ON EXPEDIENTE_CIUDADANO(estado_expediente);
CREATE INDEX IDX_FECHA_CREACION ON EXPEDIENTE_CIUDADANO(fecha_creacion);

-- ============================================================================
-- Crear Trigger para autogeneración de ID
-- ============================================================================
CREATE OR REPLACE TRIGGER TRG_EXPEDIENTE_ID
BEFORE INSERT ON EXPEDIENTE_CIUDADANO
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT SEQ_EXPEDIENTE.NEXTVAL INTO :NEW.id FROM DUAL;
    END IF;
END;
/

-- ============================================================================
-- Comentarios en la tabla y columnas (Documentación)
-- ============================================================================
COMMENT ON TABLE EXPEDIENTE_CIUDADANO IS 'Tabla principal para gestión de expedientes de ciudadanos';

COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.id IS 'Identificador único autogenerado';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.nombre_completo IS 'Nombre completo del ciudadano';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.dui_documento IS 'DUI o documento único de identificación (formato: 12345678-9)';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.direccion IS 'Dirección de residencia del ciudadano';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.telefono IS 'Número telefónico (formato: 1234-5678)';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.fecha_nacimiento IS 'Fecha de nacimiento del ciudadano';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.estado_expediente IS 'Estado actual del expediente (ACTIVO, INACTIVO, EN_REVISION, ARCHIVADO)';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.fecha_registro IS 'Fecha y hora de registro del expediente';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.usuario_creacion IS 'Usuario que creó el registro';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.fecha_creacion IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.usuario_modificacion IS 'Usuario que realizó la última modificación';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.fecha_modificacion IS 'Fecha y hora de la última modificación';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.usuario_eliminacion IS 'Usuario que eliminó el registro';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.fecha_eliminacion IS 'Fecha y hora de eliminación del registro';
COMMENT ON COLUMN EXPEDIENTE_CIUDADANO.eliminado IS 'Indica si el registro está eliminado lógicamente (0=No, 1=Sí)';

-- ============================================================================
-- Datos de ejemplo para pruebas
-- ============================================================================
INSERT INTO EXPEDIENTE_CIUDADANO (
    id, nombre_completo, dui_documento, direccion, telefono, 
    fecha_nacimiento, estado_expediente, fecha_registro,
    usuario_creacion, fecha_creacion, eliminado
) VALUES (
    SEQ_EXPEDIENTE.NEXTVAL,
    'Juan Carlos Pérez García',
    '12345678-9',
    'Colonia Escalón, San Salvador',
    '2222-3333',
    TO_DATE('1985-03-15', 'YYYY-MM-DD'),
    'ACTIVO',
    SYSTIMESTAMP,
    'admin',
    SYSTIMESTAMP,
    0
);

INSERT INTO EXPEDIENTE_CIUDADANO (
    id, nombre_completo, dui_documento, direccion, telefono, 
    fecha_nacimiento, estado_expediente, fecha_registro,
    usuario_creacion, fecha_creacion, eliminado
) VALUES (
    SEQ_EXPEDIENTE.NEXTVAL,
    'María Elena Rodríguez López',
    '98765432-1',
    'Colonia San Benito, San Salvador',
    '2233-4444',
    TO_DATE('1990-07-22', 'YYYY-MM-DD'),
    'ACTIVO',
    SYSTIMESTAMP,
    'admin',
    SYSTIMESTAMP,
    0
);

INSERT INTO EXPEDIENTE_CIUDADANO (
    id, nombre_completo, dui_documento, direccion, telefono, 
    fecha_nacimiento, estado_expediente, fecha_registro,
    usuario_creacion, fecha_creacion, eliminado
) VALUES (
    SEQ_EXPEDIENTE.NEXTVAL,
    'José Antonio Martínez Hernández',
    '11223344-5',
    'Soyapango, San Salvador',
    '2244-5555',
    TO_DATE('1988-11-30', 'YYYY-MM-DD'),
    'EN_REVISION',
    SYSTIMESTAMP,
    'admin',
    SYSTIMESTAMP,
    0
);

INSERT INTO EXPEDIENTE_CIUDADANO (
    id, nombre_completo, dui_documento, direccion, telefono, 
    fecha_nacimiento, estado_expediente, fecha_registro,
    usuario_creacion, fecha_creacion, eliminado
) VALUES (
    SEQ_EXPEDIENTE.NEXTVAL,
    'Ana Sofía González Flores',
    '55667788-2',
    'Santa Tecla, La Libertad',
    '2255-6666',
    TO_DATE('1992-05-18', 'YYYY-MM-DD'),
    'ACTIVO',
    SYSTIMESTAMP,
    'admin',
    SYSTIMESTAMP,
    0
);

INSERT INTO EXPEDIENTE_CIUDADANO (
    id, nombre_completo, dui_documento, direccion, telefono, 
    fecha_nacimiento, estado_expediente, fecha_registro,
    usuario_creacion, fecha_creacion, eliminado
) VALUES (
    SEQ_EXPEDIENTE.NEXTVAL,
    'Carlos Eduardo Ramírez Castillo',
    '99887766-3',
    'Antiguo Cuscatlán, La Libertad',
    '2266-7777',
    TO_DATE('1987-09-25', 'YYYY-MM-DD'),
    'INACTIVO',
    SYSTIMESTAMP,
    'admin',
    SYSTIMESTAMP,
    0
);

-- Confirmar cambios
COMMIT;

-- ============================================================================
-- Verificar datos insertados
-- ============================================================================
SELECT 
    id,
    nombre_completo,
    dui_documento,
    estado_expediente,
    fecha_creacion,
    eliminado
FROM EXPEDIENTE_CIUDADANO
ORDER BY fecha_creacion DESC;
