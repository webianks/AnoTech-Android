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

public class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.VH> {

    private List<String> testNames;
    private Context context;
    private ItemClickListener itemClickListener;

    public TestingAdapter(Context context, List<String> testNames) {
        this.context = context;
        this.testNames = testNames;
    }

    @Override
    public TestingAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_structure_item, parent, false);
        TestingAdapter.VH vh = new TestingAdapter.VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(TestingAdapter.VH holder, int position) {
        holder.tableName.setText(testNames.get(position));
    }

    @Override
    public int getItemCount() {
        return testNames.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tableName;

        VH(View itemView) {
            super(itemView);

            tableName = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (itemClickListener!=null)
                        itemClickListener.itemClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface ItemClickListener{
        void itemClicked(int position);
    }
}
