package com.isfar.quotesnotes.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.isfar.quotesnotes.Category;
import com.isfar.quotesnotes.Quote;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.SharedPreferencesManager;
import com.isfar.quotesnotes.activities.AllQuotesActivity;
import com.isfar.quotesnotes.activities.CategoryActivity;
import com.isfar.quotesnotes.activities.CategoryListActivity;
import com.isfar.quotesnotes.monetization.Admob;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    QuoteAdapter quoteAdapter;
    RecyclerView categoriesRecyclerView, quotesRecyclerView;
    LinearLayout linearShowAllCategories;
    TextView tvSeeAll;
    List<Category> categories = new ArrayList<>();
    List<Quote> homeQuotes = new ArrayList<>();
    ImageSlider imageSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //=======================================================
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        quotesRecyclerView = view.findViewById(R.id.quotes_recycler_view);
        imageSlider = view.findViewById(R.id.image_slider);
        linearShowAllCategories = view.findViewById(R.id.linear_show_all_categories);
        tvSeeAll = view.findViewById(R.id.tv_see_all);
        //=======================================================


        //Image Slider=========================================================
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.quote1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote3, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote4, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote5, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote6, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote7, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote8, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote9, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote10, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.quote11, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(imageList);
        //Image Slider=========================================================



        //All Category onClick ==============================
        linearShowAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded()) {
                    Admob.showInterstitialAdinThreeClick(requireActivity());
                }
                Intent intent = new Intent(getContext(), CategoryListActivity.class);
                startActivity(intent);
            }
        });

        //All Quotes onClick ==============================
        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded()) {
                    Admob.showInterstitialAdinThreeClick(requireActivity());
                }
                Intent intent = new Intent(getContext(), AllQuotesActivity.class);
                startActivity(intent);
            }
        });


        // Categories in Home =================================
        categoryList();


        //============ set Adapter(Category) =====================================
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        CategoryListActivity.CategoryAdapter categoryAdapter = new CategoryListActivity.CategoryAdapter(categories, category -> {
            if (isAdded()) {
                Admob.showInterstitialAdinThreeClick(requireActivity());
            }
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        categoriesRecyclerView.setAdapter(categoryAdapter);


        // Quotes For Home =============================
        fetchQuotes();

        // set Adapter For Home Quotes ============================
        quotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        quoteAdapter = new QuoteAdapter(homeQuotes);
        quotesRecyclerView.setAdapter(quoteAdapter);



        return view;


    }//onCreate End...


//==================================================================================================
//================= LIST OF CATEGORIES IN HOME =========================================
    private void categoryList() {

        categories = new ArrayList<>();
        categories.add(new Category("Motivational", R.drawable.motivational));
        categories.add(new Category("Life", R.drawable.life));
        categories.add(new Category("Love", R.drawable.love));
        categories.add(new Category("Happy", R.drawable.happy));
        categories.add(new Category("Sadness", R.drawable.sadness));
        categories.add(new Category("Attitude", R.drawable.attitude));

    }
//================= LIST OF CATEGORIES IN HOME END ==================================
//==================================================================================================

//==================================================================================================
//================= Fetch Quotes ===================================================
    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;

    }// load json end.......

    private void fetchQuotes() {

        // Load JSON from assets
        String json = loadJSONFromAsset("home_quotes.json");
        JsonObject rootObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray quotesArray = rootObject.getAsJsonArray("quotes");

        // Parse JSON into a List of Quote objects
        Gson gson = new Gson();
        Type type = new TypeToken<List<Quote>>() {}.getType();
        List<Quote> quotes = gson.fromJson(quotesArray, type);

        // Shuffle the list...
        Collections.shuffle(quotes);
        homeQuotes.addAll(quotes);

    }//fetch quotes end...

//================= Fetch Quotes ===================================================
//==================================================================================================


//==================================================================================================
//================= QUOTES ADAPTER START =====================================================
//==================================================================================================
    public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.myViewHolder> {
        List<Quote> quotes;

        public QuoteAdapter(List<Quote> quotes) {
            this.quotes = quotes;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.item_quote, parent, false);

            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

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
                SharedPreferencesManager manager = new SharedPreferencesManager(requireContext());
                manager.saveQuote(quoteToSave);

                Toast.makeText(requireContext(), "Quote saved!", Toast.LENGTH_SHORT).show();
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

        public class myViewHolder extends RecyclerView.ViewHolder {
            LinearLayout bgLinearLayout, btn_share, btn_copy, btn_save;
            TextView quote_text, quote_author, tvSave;
            ImageView imageSave;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                bgLinearLayout = itemView.findViewById(R.id.bgLinearLayout);
                quote_text = itemView.findViewById(R.id.quote_text);
                quote_author = itemView.findViewById(R.id.quote_author);
                btn_share = itemView.findViewById(R.id.btn_share);
                btn_copy = itemView.findViewById(R.id.btn_copy);
                btn_save = itemView.findViewById(R.id.btn_save);
                tvSave = itemView.findViewById(R.id.tvSave);
                imageSave = itemView.findViewById(R.id.imageSave);

            }
        }

    }
//==================================================================================================
//================= QUOTES ADAPTER END =======================================================
//==================================================================================================


}//Main End...