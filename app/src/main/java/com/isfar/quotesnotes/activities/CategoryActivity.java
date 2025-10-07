package com.isfar.quotesnotes.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.isfar.quotesnotes.Quote;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.SharedPreferencesManager;
import com.isfar.quotesnotes.monetization.Admob;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    FilterQuoteAdapter filterQuoteAdapter;
    RecyclerView FilterRecyclerView;
    MaterialToolbar categoryToolbar;
    List<Quote> filteredQuotes = new ArrayList<>();
    String selectedCategory;
    LinearLayout adContainerLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_category), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //=================================================
        FilterRecyclerView = findViewById(R.id.FilterRecyclerView);
        categoryToolbar = findViewById(R.id.category_toolbar);
        adContainerLinear = findViewById(R.id.ad_container_linear);
        //=================================================

        //============== Back =================
        categoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        // banner Ad....
        Admob.setBannerAd(adContainerLinear, CategoryActivity.this);


        //============ set Adapter =========================================
        FilterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterQuoteAdapter = new FilterQuoteAdapter(filteredQuotes);
        FilterRecyclerView.setAdapter(filterQuoteAdapter);


        // Get selected category from Intent
        selectedCategory = getIntent().getStringExtra("category");
        categoryToolbar.setTitle(selectedCategory);


        // Fetch quotes
        fetchQuotesByCategory();


    }//onCreate End...


//==================================================================================================
//================= Fetch Quotes By Category ============================================

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;

    }//loading json end...

    private void fetchQuotesByCategory() {

        // Load JSON from assets
        String json = loadJSONFromAsset("quotes_by_category.json");
        JsonObject rootObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray quotesArray = rootObject.getAsJsonArray("quotes");

        // Parse JSON into a List of Quote objects
        Gson gson = new Gson();
        Type type = new TypeToken<List<Quote>>() {}.getType();
        List<Quote> quotes = gson.fromJson(quotesArray, type);

        // Shuffle the list
        Collections.shuffle(quotes);

        // Filter quotes by category
        for (Quote quote : quotes) {
            if (quote.getCategory().equalsIgnoreCase(selectedCategory)) {
                filteredQuotes.add(quote);
            }
        }

    }//fetch quotes end

//================= Fetch Quotes By Category =============================================
//==================================================================================================

//==================================================================================================
//================= FILTERED QUOTES ADAPTER START =========================================
//==================================================================================================
    public class FilterQuoteAdapter extends RecyclerView.Adapter<FilterQuoteAdapter.MyViewHolder> {

        private final List<Quote> quotes;

        public FilterQuoteAdapter(List<Quote> quotes) {
            this.quotes = quotes;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Quote quote = quotes.get(position);
            String quotetext = quote.getQuote();
            String authorname = quote.getAuthor();

            holder.quote_text.setText(quotetext);
            holder.quote_author.setText("-" + authorname + "-");

            Random rnd = new Random();
            int color = Color.argb(255,
                    rnd.nextInt(128) + 128,
                    rnd.nextInt(128) + 128,
                    rnd.nextInt(128) + 128);
            holder.bgLinearLayout.setBackgroundColor(color);

            // SharedPreferencesManager instance
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(holder.itemView.getContext());
            List<HashMap<String, String>> savedQuotes = sharedPreferencesManager.getSavedQuotes();


            // Save/Delete Button Functionality
            holder.btn_save.setOnClickListener(v -> {
                HashMap<String, String> quoteToSave = new HashMap<>();
                quoteToSave.put("quote", quotetext);
                quoteToSave.put("author", authorname);

                // Save quote using SharedPreferences
                SharedPreferencesManager manager = new SharedPreferencesManager(CategoryActivity.this);
                manager.saveQuote(quoteToSave);

                Toast.makeText(CategoryActivity.this, "Quote saved!", Toast.LENGTH_SHORT).show();
            });

            // Share Button Functionality
            holder.btn_share.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "'" + quotetext + "'" + " - " + authorname);
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share Quote"));
            });

            // Copy Button Functionality
            holder.btn_copy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Quote", "'" + quotetext + "'" + " - " + authorname);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "Quote copied to clipboard!", Toast.LENGTH_SHORT).show();
            });

        }

        @Override
        public int getItemCount() {
            return quotes.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout bgLinearLayout, btn_share, btn_copy, btn_save;
            TextView quote_text, quote_author, tvSave;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                bgLinearLayout = itemView.findViewById(R.id.bgLinearLayout);
                quote_text = itemView.findViewById(R.id.quote_text);
                quote_author = itemView.findViewById(R.id.quote_author);
                btn_share = itemView.findViewById(R.id.btn_share);
                btn_copy = itemView.findViewById(R.id.btn_copy);
                btn_save = itemView.findViewById(R.id.btn_save);
                tvSave = itemView.findViewById(R.id.tvSave);

            }
        }
    }
//==================================================================================================
//================= FILTERED QUOTES ADAPTER START =========================================
//==================================================================================================

}//Main End...