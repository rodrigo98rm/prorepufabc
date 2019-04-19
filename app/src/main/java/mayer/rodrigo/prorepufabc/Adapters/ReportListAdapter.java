package mayer.rodrigo.prorepufabc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.R;


/**
 * Tutorial de implementacao: https://guides.codepath.com/android/using-the-recyclerview
 */
public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {

    private ArrayList<Report> reports;

    public ReportListAdapter(ArrayList<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reportView = inflater.inflate(R.layout.report_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(reportView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListAdapter.ViewHolder holder, int position) {
        Report report = reports.get(position);

        TextView txtName = holder.txtName;
        txtName.setText(report.getTitle());

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.textView_name_ReportItem);

        }
    }

}
