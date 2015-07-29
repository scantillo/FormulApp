package reportsas.com.formulapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import mobi.pdf417.demo.R;
import java.util.List;

import modelo.Opcion;

/**
 * Created by rytscc on 03/05/2015.
 */
public class OpcionAdadpter extends ArrayAdapter<Opcion> {

    public OpcionAdadpter(Context context, List<Opcion> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.image_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView titulo = (TextView)listItemView.findViewById(R.id.text1);
         ImageView categoria = (ImageView)listItemView.findViewById(R.id.category);


        //Obteniendo instancia de la Tarea en la posición actual
        Opcion item = getItem(position);

        titulo.setText(item.getTitulo());
        categoria.setImageResource(item.getImagen());

        //Devolver al ListView la fila creada
        return listItemView;

    }

}
