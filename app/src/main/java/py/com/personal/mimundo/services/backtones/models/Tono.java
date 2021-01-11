package py.com.personal.mimundo.services.backtones.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Tono implements Parcelable {

    private Integer idTono;
    private String artista;
    private String tema;
    private String urlTono;
    private String urlImagen;
    private Boolean esParaTodasLasLlamadas;
    private Boolean esAutorenovable;

    public Tono() {
    }

    public Tono(Integer idTono, String artista, String tema, String urlTono,String urlImagen,
                Boolean esParaTodasLasLlamadas, Boolean esAutorenovable) {
        this.idTono = idTono;
        this.artista = artista;
        this.tema = tema;
        this.urlTono = urlTono;
        this.urlImagen = urlImagen;
        this.esParaTodasLasLlamadas = esParaTodasLasLlamadas;
        this.esAutorenovable = esAutorenovable;
    }

    public Integer getIdTono() {
        return idTono;
    }

    public void setIdTono(Integer idTono) {
        this.idTono = idTono;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getUrlTono() {
        return urlTono;
    }

    public void setUrlTono(String urlTono) {
        this.urlTono = urlTono;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Boolean getEsParaTodasLasLlamadas() {
        return esParaTodasLasLlamadas;
    }

    public void setEsParaTodasLasLlamadas(Boolean esParaTodasLasLlamadas) {
        this.esParaTodasLasLlamadas = esParaTodasLasLlamadas;
    }

    public Boolean getEsAutorenovable() {
        return esAutorenovable;
    }

    public void setEsAutorenovable(Boolean esAutorenovable) {
        this.esAutorenovable = esAutorenovable;
    }

    public static final Parcelable.Creator<Tono> CREATOR = new Creator<Tono>() {
        public Tono createFromParcel(Parcel source) {
            Tono tono = new Tono();
            tono.idTono = source.readInt();
            tono.artista = source.readString();
            tono.tema = source.readString();
            tono.urlTono = source.readString();
            tono.urlImagen = source.readString();
            tono.esAutorenovable = source.readByte() != 0;
            tono.esParaTodasLasLlamadas = source.readByte() != 0;
            return tono;
        }

        public Tono[] newArray(int size) {
            return new Tono[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idTono != null)
            parcel.writeInt(idTono);
        if (artista != null)
            parcel.writeString(artista);
        if (tema != null)
            parcel.writeString(tema);
        if (urlTono != null)
            parcel.writeString(urlTono);
        if (urlImagen != null)
            parcel.writeString(urlImagen);
        if (esAutorenovable != null)
            parcel.writeByte((byte) (esAutorenovable ? 1 : 0));
        if (esParaTodasLasLlamadas != null)
            parcel.writeByte((byte) (esParaTodasLasLlamadas ? 1 : 0));
    }

}
