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
 * Created by R Ankit on 23-03-2017.
 */

public class TableViewerAdapter extends RecyclerView.Adapter<TableViewerAdapter.VH> {

    private List<TableAttribute> tableAttributes;
    private Context context;

    public TableViewerAdapter(List<TableAttribute> tableAttributes, Context context) {
        this.tableAttributes = tableAttributes;
        this.context = context;
    }

    @Override
    public TableViewerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_table_attribute, parent, false);
        return new TableViewerAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(TableViewerAdapter.VH holder, int position) {

        holder.attributeName.setText(tableAttributes.get(position).getName());
        holder.attributeType.setText(tableAttributes.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return tableAttributes.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView attributeName;
        TextView attributeType;

        VH(View itemView) {
            super(itemView);

            attributeName = (TextView) itemView.findViewById(R.id.name);
            attributeType = (TextView) itemView.findViewById(R.id.type);

        }
    }
}
