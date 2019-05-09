package mayer.rodrigo.prorepufabc.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.R;

public class ReportPhotosAdapter extends BaseAdapter {

    private ArrayList<String> photos;
    private ArrayList<Uri> photoUris;
    private Context context;

    public ReportPhotosAdapter(ArrayList<String> photos, Context context){
        this.photos = photos;
        this.context = context;
    }

    public ReportPhotosAdapter(Context context, ArrayList<Uri> photoUris){
        this.photoUris = photoUris;
        this.context = context;
    }

    @Override
    public int getCount() {

        if(photos != null){
            return photos.size();
        }

        return photoUris.size();


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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.report_photo_grid_item, null);
        }

        final ImageView imageView = view.findViewById(R.id.imageView_reportPhoto_PhotosAdapter);

        if(photos != null){
            Picasso.with(context).load(photos.get(i)).into(imageView);
        }else{
            imageView.setImageBitmap(compressBitmap(getBitmapFromUri(photoUris.get(i))));
        }

        return view;
    }

    private Bitmap getBitmapFromUri(Uri imageUri){
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Bitmap compressBitmap(Bitmap old){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Reduce image dimensions
        Bitmap tempImg = Bitmap.createScaledBitmap(old, old.getWidth()/3, old.getHeight()/3, true);

        //Reduce image quality
        tempImg.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayOutputStream);

        byte[] out = byteArrayOutputStream.toByteArray();

        Bitmap newImg = BitmapFactory.decodeByteArray(out, 0, out.length);

        old.recycle();
        tempImg.recycle();

        return newImg;
    }
}
