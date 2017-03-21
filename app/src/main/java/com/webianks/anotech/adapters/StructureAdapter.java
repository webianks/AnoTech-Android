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

public class StructureAdapter extends RecyclerView.Adapter<StructureAdapter.VH> {

    private List<String> tableNames;
    private Context context;

    public StructureAdapter(Context context, List<String> tableNames) {
        this.context = context;
        this.tableNames = tableNames;
    }

    @Override
    public StructureAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_structure_item, parent, false);
        StructureAdapter.VH vh = new StructureAdapter.VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(StructureAdapter.VH holder, int position) {
        holder.tableName.setText(tableNames.get(position));
    }

    @Override
    public int getItemCount() {
        return tableNames.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView tableName;

        public VH(View itemView) {
            super(itemView);

            tableName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
