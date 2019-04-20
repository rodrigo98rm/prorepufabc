package mayer.rodrigo.prorepufabc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import mayer.rodrigo.prorepufabc.Activities.ReportDetailsActivity;
import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.R;


/**
 * Tutorial de implementacao: https://guides.codepath.com/android/using-the-recyclerview
 */
public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {

    private ArrayList<Report> reports;
    private Context context;

    public ReportListAdapter(ArrayList<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reportView = inflater.inflate(R.layout.report_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(reportView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListAdapter.ViewHolder holder, int position) {
        Report report = reports.get(position);

        //Views
        View clickableBox;
        TextView txtTitle, txtUserName, txtDate, txtUpvotes;
        CircularImageView imgUser;
        GridView photosGrid = holder.photosGrid;

        clickableBox = holder.clickableBox;
        txtTitle = holder.txtTitle;
        txtUserName = holder.txtUserName;
        txtDate = holder.txtDate;
        txtUpvotes = holder.txtUpvotes;
        imgUser = holder.imgUser;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");

        txtTitle.setText(report.getTitle());
        txtUserName.setText(report.getUser().getName());
        txtDate.setText("• " + formatter.format(new Date(report.getTimestamp())));
        txtUpvotes.setText(String.valueOf(report.getUpvotes()));
        Picasso.with(context).load(report.getUser().getImgUrl()).into(imgUser);

        //Photos Grid View
        ReportPhotosAdapter photosAdapter = new ReportPhotosAdapter(report.getPhotosUrls(), context);
        photosGrid.setAdapter(photosAdapter);

        clickableBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReportDetailsActivity.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View clickableBox;
        private TextView txtTitle, txtUserName, txtDate, txtUpvotes;
        private CircularImageView imgUser;
        private GridView photosGrid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clickableBox = itemView.findViewById(R.id.view_clickableBox_ReportItem);
            txtTitle = itemView.findViewById(R.id.textView_title_ReportItem);
            txtUserName = itemView.findViewById(R.id.textView_user_ReportItem);
            txtDate = itemView.findViewById(R.id.textView_date_ReportItem);
            txtUpvotes = itemView.findViewById(R.id.textView_upvotes_ReportItem);
            imgUser = itemView.findViewById(R.id.circularImageView_userImg_ReportItem);
            photosGrid = itemView.findViewById(R.id.gridView_photos_ReportItem);

        }
    }

}
