package py.com.personal.mimundo.security.client.dao;

/**
 * Created by Konecta on 24/10/2014.
 */
public class ErrorOauth {

    private String error;
    private String error_description;
    private String error_uri;
    private String state;

    public ErrorOauth(){
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError_uri() {
        return error_uri;
    }

    public void setError_uri(String error_uri) {
        this.error_uri = error_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
