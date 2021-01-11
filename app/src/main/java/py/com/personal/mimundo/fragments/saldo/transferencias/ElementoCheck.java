package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.widget.CheckBox;
import android.widget.TextView;

import py.com.personal.mimundo.services.lineas.models.Linea;

/**
 * Created by Konecta on 30/07/2014.
 */
public class ElementoCheck {

    Linea linea;
    TextView textViewTitle;
    CheckBox checkBox;

    public Linea getLinea() {
        return linea;
    }

    public void setLinea(Linea linea) {
        this.linea = linea;
    }


    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public void setTextViewTitle(TextView textViewTitle) {
        this.textViewTitle = textViewTitle;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}