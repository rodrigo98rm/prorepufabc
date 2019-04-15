package mayer.rodrigo.prorepufabc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.R;

public class ReportPhotosAdapter extends BaseAdapter {

    private ArrayList<String> photos;
    private Context context;

    public ReportPhotosAdapter(ArrayList<String> photos, Context context){
        this.photos = photos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String photoUrl = photos.get(i);

        if(view == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.report_photo_grid_item, null);
        }

        ImageView imageView = view.findViewById(R.id.imageView_reportPhoto_PhotosAdapter);
        Picasso.with(context).load(photoUrl).into(imageView);

        return view;
    }
}
