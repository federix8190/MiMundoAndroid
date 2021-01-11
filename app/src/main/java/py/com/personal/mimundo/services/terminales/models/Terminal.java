package py.com.personal.mimundo.services.terminales.models;

/**
 * Created by Konecta on 24/07/2014.
 */
public class Terminal {

    private String codigo;
    private String marca;
    private String modelo;
    private String tecnologia;
    private String bandaGsm;
    private String camara;
    private String java;
    private Boolean mms;
    private Boolean wap;
    private Boolean streaming;
    private Boolean pantallaColor;
    private Boolean mp3;
    private Boolean reproductorAac;
    private Boolean polifonico;
    private Boolean gprs;
    private String imei;

    public Terminal(){
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public String getBandaGsm() {
        return bandaGsm;
    }

    public void setBandaGsm(String bandaGsm) {
        this.bandaGsm = bandaGsm;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public Boolean getMms() {
        return mms;
    }

    public void setMms(Boolean mms) {
        this.mms = mms;
    }

    public Boolean getWap() {
        return wap;
    }

    public void setWap(Boolean wap) {
        this.wap = wap;
    }

    public Boolean getStreaming() {
        return streaming;
    }

    public void setStreaming(Boolean streaming) {
        this.streaming = streaming;
    }

    public Boolean getPantallaColor() {
        return pantallaColor;
    }

    public void setPantallaColor(Boolean pantallaColor) {
        this.pantallaColor = pantallaColor;
    }

    public Boolean getMp3() {
        return mp3;
    }

    public void setMp3(Boolean mp3) {
        this.mp3 = mp3;
    }

    public Boolean getReproductorAac() {
        return reproductorAac;
    }

    public void setReproductorAac(Boolean reproductorAac) {
        this.reproductorAac = reproductorAac;
    }

    public Boolean getPolifonico() {
        return polifonico;
    }

    public void setPolifonico(Boolean polifonico) {
        this.polifonico = polifonico;
    }

    public Boolean getGprs() {
        return gprs;
    }

    public void setGprs(Boolean gprs) {
        this.gprs = gprs;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
