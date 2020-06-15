package com.nasande.pharmaciemayamaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.nasande.pharmaciemayamaya.data.ProductMockData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.recyclerView = (RecyclerView) findViewById(R.id.product_recycle);
        this.recyclerView.setHasFixedSize(false);



        this.productList = new ArrayList<>();
        ProductMockData productMockData = new ProductMockData();
        this.productList = productMockData.Data;

        for(Product produit: this.productList){
            Log.d("Donnees",produit.getTitle() + " / " + produit.getPriceNumber());
        }

        this.layoutManager = new LinearLayoutManager(this);


        this.productAdapter = new ProductAdapter(this.productList);

        recyclerView.setAdapter(this.productAdapter);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.productAdapter.notifyDataSetChanged();
        this.recyclerView.stopScroll();

    }


}