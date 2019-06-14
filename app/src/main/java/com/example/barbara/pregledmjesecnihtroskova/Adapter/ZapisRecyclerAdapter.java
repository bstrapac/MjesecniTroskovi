package com.example.barbara.pregledmjesecnihtroskova.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.pregledmjesecnihtroskova.Model.Zapis;
import com.example.barbara.pregledmjesecnihtroskova.R;
import com.example.barbara.pregledmjesecnihtroskova.listeners.ZapisListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ZapisRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public List<Zapis> dataList;
    public ZapisListener mClick;
    public static String dateFormat = "dd.MM.yyyy";
    public static String dateToShow;

    public ZapisRecyclerAdapter(List<Zapis> zapisList, ZapisListener mClick)
    {
        this.dataList = zapisList;
        this.mClick = mClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.zapis_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position)
    {
        Object list= dataList.get(position);
        final Zapis zapis =  (Zapis) list;
        Long date = zapis.getCreatedDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date_show = new Date(date);
        dateToShow = simpleDateFormat.format(date_show);
        String description = zapis.getDescription() + " " + zapis.getInput()+ "kn";
        ((ViewHolder)viewHolder).tvOpis.setText(description);
        ((ViewHolder)viewHolder).tvDatum.setText(dateToShow);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mClick.onClick(zapis);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDatum;
        TextView tvOpis;
        CardView cvZapis;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvDatum = itemView.findViewById(R.id.tvDatum);
            tvOpis = itemView.findViewById(R.id.tvOpis);
            cvZapis = itemView.findViewById(R.id.cardView);
        }
    }
}
