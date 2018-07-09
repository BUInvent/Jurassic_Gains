package com.buinvent.jurassic_gains;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> weekButtonsText = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> buttons, Context context){
        weekButtonsText = buttons;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int weekNum) {

        System.out.println("weekNum = " + weekNum);
        holder.mButton.setText(weekButtonsText.get(weekNum));

//        if(weekNum < 1) {
//            holder.mButton.setEnabled(true);
//            holder.checkBox.setEnabled(true);
//        }

//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("hi");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return weekButtonsText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
//        ArrayList<CheckBox> mCheckBoxes = new ArrayList<>();
        Button mButton;
//        ArrayList<Button> mButtons = new ArrayList<>();
        LinearLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
//            mCheckBoxes.add(itemView.findViewById(R.id.checkbox));
            mButton = itemView.findViewById(R.id.button);
//            mButtons.add(itemView.findViewById(R.id.button));
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }

}
