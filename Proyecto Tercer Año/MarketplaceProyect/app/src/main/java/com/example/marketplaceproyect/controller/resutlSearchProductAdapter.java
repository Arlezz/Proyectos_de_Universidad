package com.example.marketplaceproyect.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.interfaces.UsuarioApi;
import com.example.marketplaceproyect.modelos.Products;
import com.example.marketplaceproyect.modelos.goActivities;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;

public class resutlSearchProductAdapter extends RecyclerView.Adapter<resutlSearchProductAdapter.resutlSearchProductHolder>{

    List<Products> producto;
    Context contexto;

    DecimalFormatSymbols symbols;
    DecimalFormat precioFormat;


    public resutlSearchProductAdapter(List<Products> producto, Context contexto) {
        this.producto = producto;
        this.contexto = contexto;
    }

    @NotNull
    @Override
    public resutlSearchProductHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_search_products_list,parent,false);
        return new resutlSearchProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull resutlSearchProductHolder holder, int position) {

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        precioFormat = new DecimalFormat("###,###.##", symbols);


        Products product = producto.get(position);
        List<CarouselItem> photo = new ArrayList<>();
        for(int i = 0; i < product.getProductImage().length; i++){
            photo.add(new CarouselItem(RetrofitClientInstance.URL_IMAGES_P+product.getProductImage()[i],"image "+(i+1)));
        }

        holder.carouselResultSearch.setData(photo);
        holder.tittleResultSerach.setText(product.getTitulo());
        holder.estadoResultSearch.setText(product.getCondicion());
        if(product.getPrecio()== 0){
            holder.priceSearchResult.setText("Gratis");
        }else{
            holder.priceSearchResult.setText("$ "+precioFormat.format(product.getPrecio()));
        }
        if(product.getPrecioEnvio() == 0){
            holder.shippingPriceSearchResult.setText("Gratis");
        }else {
            holder.shippingPriceSearchResult.setText("$ "+precioFormat.format(product.getPrecioEnvio()));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("owner",product.getPropietario());
                new goActivities(contexto).goProductDetail(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return producto.size();
    }


    public static class resutlSearchProductHolder extends RecyclerView.ViewHolder{

        ImageCarousel carouselResultSearch;
        TextView tittleResultSerach;
        TextView priceSearchResult;
        TextView estadoResultSearch;
        TextView shippingPriceSearchResult;
        CircleIndicator2 indicator;

        public resutlSearchProductHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            carouselResultSearch = itemView.findViewById(R.id.carouselResultSearch);
            tittleResultSerach = itemView.findViewById(R.id.tittleSearchResult);
            priceSearchResult = itemView.findViewById(R.id.priceSearchResult);
            estadoResultSearch = itemView.findViewById(R.id.estadoResultSearch);
            shippingPriceSearchResult = itemView.findViewById(R.id.shippingPriceSearchResult);
            indicator = itemView.findViewById(R.id.indicatorResulSearch);
            carouselResultSearch.setIndicator(indicator);
        }
    }
}
