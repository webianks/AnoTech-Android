package com.webianks.anotech.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webianks.anotech.R;


import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class AnomaliesAdapter extends RecyclerView.Adapter<AnomaliesAdapter.VH> {

    private List<Anomaly> anomalies;
    private Context context;
    private ItemClickListener itemClickListener;

    public AnomaliesAdapter(Context context, List<Anomaly> anomalies) {
        this.context = context;
        this.anomalies = anomalies;
    }

    @Override
    public AnomaliesAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_activity_happened_style, parent, false);
        AnomaliesAdapter.VH vh = new AnomaliesAdapter.VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AnomaliesAdapter.VH holder, int position) {
       holder.target.setText(anomalies.get(position).getType());
    }



    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        return anomalies.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView target;

        VH(View itemView) {
            super(itemView);

            target = (TextView) itemView.findViewById(R.id.target);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (itemClickListener!=null)
                        itemClickListener.itemClicked(anomalies.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ItemClickListener{
        void itemClicked(Anomaly anomaly);
    }


}
