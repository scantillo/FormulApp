package reportsas.com.formulapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.pdf417.demo.R;
import modelo.Encuesta;
import modelo.Opcion;

/**
 * Created by rytscc on 10/05/2015.
 */
public class FormsAdapter extends ArrayAdapter<Encuesta> {

    public FormsAdapter(Context context, List<Encuesta> encuestas) {
        super(context, R.layout.form_list_item, encuestas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.form_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView titulo = (TextView) listItemView.findViewById(R.id.titulo);
        TextView descrip = (TextView) listItemView.findViewById(R.id.descripcion);
        TextView idEn = (TextView) listItemView.findViewById(R.id.codigo);


        //Obteniendo instancia de la Tarea en la posición actual
        Encuesta item = getItem(position);

        titulo.setText(item.getTitulo());
        descrip.setText(item.getDescripcion());
        idEn.setText(item.getIdEncuesta());



        //Devolver al ListView la fila creada
        return listItemView;
    }

}
