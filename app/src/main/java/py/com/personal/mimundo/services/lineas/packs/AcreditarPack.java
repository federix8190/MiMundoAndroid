package py.com.personal.mimundo.services.lineas.packs;

/**
 * Created by Konecta on 18/09/2014.
 */
public class AcreditarPack {

    private String codigoPack;
    private String codigoOperacion;
    private String numeroLineaBeneficiaria;
    private String canal;

    public AcreditarPack(){
    }

    public String getCodigoPack() {
        return codigoPack;
    }

    public void setCodigoPack(String codPack) {
        this.codigoPack = codPack;
    }

    public String getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(String codOperacion) {
        this.codigoOperacion = codOperacion;
    }

    public String getNumeroLineaBeneficiaria() {
        return numeroLineaBeneficiaria;
    }

    public void setNumeroLineaBeneficiaria(String numeroLineaBeneficiaria) {
        this.numeroLineaBeneficiaria = numeroLineaBeneficiaria;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

}
