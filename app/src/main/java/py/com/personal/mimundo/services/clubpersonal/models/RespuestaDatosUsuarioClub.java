package py.com.personal.mimundo.services.clubpersonal.models;

public class RespuestaDatosUsuarioClub {

//            "codigo": 50523,
//            "nombre": null,
//            "apellido": "EMPRESA DEL CLIENTE",
//            "direccion": "KM 4  SUPER CARRETERA MCAL. LOPEZ 496 ",
//            "fechaAlta": 1331861076000,
//            "fechaBaja": null,
//            "codigoRazonSocial": "UNIVA",
//            "tipoPersona": "PERJUR",
//            "codigoOrigen": "WEB",
//            "codigoDocumento": null,
//            "codigoClaseComercial": null,
//            "numeroContrato": null,
//            "codigoAccion": "B",
//            "codigoPedido": null,
//            "usuario": {



    String codigo;
    String nombre;
    String apellido;
    String direccion;
    String fechaAlta;
    String fechaBaja;
    String codigoRazonSocial;
    String tipoPersona;
    String codigoOrigen;
    String codigoDocumento;
    String codigoClaseComercial;
    String numeroContrato;
    String codigoAccion;
    String codigoPedido;
    Usuario usuario;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getCodigoRazonSocial() {
        return codigoRazonSocial;
    }

    public void setCodigoRazonSocial(String codigoRazonSocial) {
        this.codigoRazonSocial = codigoRazonSocial;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getCodigoOrigen() {
        return codigoOrigen;
    }

    public void setCodigoOrigen(String codigoOrigen) {
        this.codigoOrigen = codigoOrigen;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getCodigoClaseComercial() {
        return codigoClaseComercial;
    }

    public void setCodigoClaseComercial(String codigoClaseComercial) {
        this.codigoClaseComercial = codigoClaseComercial;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getCodigoAccion() {
        return codigoAccion;
    }

    public void setCodigoAccion(String codigoAccion) {
        this.codigoAccion = codigoAccion;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public class Usuario{
//                "usuario": "408025-4",
//                "tipoUsuario": "TIT",
//                "lineaAsignada": "973543151",
//                "codigoCliente": 50523,
//                "nombres": "",
//                "apellidos": "EMPRESA DEL CLIENTE",
//                "email": "konectatest@gmail.com",
//                "numeroLinea": "973543151"



        String usuario;
        String tipoUsuario;
        String lineaAsignada;
        String codigoCliente;
        String nombres;
        String apellidos;
        String email;
        String numeroLinea;

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getTipoUsuario() {
            return tipoUsuario;
        }

        public void setTipoUsuario(String tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
        }

        public String getLineaAsignada() {
            return lineaAsignada;
        }

        public void setLineaAsignada(String lineaAsignada) {
            this.lineaAsignada = lineaAsignada;
        }

        public String getCodigoCliente() {
            return codigoCliente;
        }

        public void setCodigoCliente(String codigoCliente) {
            this.codigoCliente = codigoCliente;
        }

        public String getNombres() {
            return nombres;
        }

        public void setNombres(String nombres) {
            this.nombres = nombres;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNumeroLinea() {
            return numeroLinea;
        }

        public void setNumeroLinea(String numeroLinea) {
            this.numeroLinea = numeroLinea;
        }

    }
}
