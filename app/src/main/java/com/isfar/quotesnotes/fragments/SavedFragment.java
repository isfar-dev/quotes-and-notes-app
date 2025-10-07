package com.isfar.quotesnotes.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.SharedPreferencesManager;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SavedFragment extends Fragment {

    RecyclerView savedRecyclerView;
    TextView tvNoSaved;
    SavedQuotesAdapter savedQuotesAdapter;
    List<HashMap<String, String>> savedQuotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        //==============================================
        tvNoSaved = view.findViewById(R.id.tv_no_saved_quotes);
        savedRecyclerView = view.findViewById(R.id.saved_recycler_view);
        //==============================================


        // save qoutes in sharedPreferences....
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        savedQuotes = sharedPreferencesManager.getSavedQuotes();

        // set adapter.....
        if (savedQuotes.isEmpty()) {
            tvNoSaved.setVisibility(View.VISIBLE);
        } else {
            savedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            savedQuotesAdapter = new SavedQuotesAdapter(savedQuotes, sharedPreferencesManager);
            savedRecyclerView.setAdapter(savedQuotesAdapter);
            tvNoSaved.setVisibility(View.GONE);
        }


        return view;


    }//onCreate End...


//==================================================================================================
//================= SavedQuotesAdapter Start ==========================================
//==================================================================================================
    public class SavedQuotesAdapter extends RecyclerView.Adapter<SavedQuotesAdapter.SavedViewHolder> {

        List<HashMap<String, String>> quotes;
        SharedPreferencesManager sharedPreferencesManager;

        public SavedQuotesAdapter(List<HashMap<String, String>> quotes, SharedPreferencesManager manager) {
            this.quotes = quotes;
            this.sharedPreferencesManager = manager;
        }

        @NonNull
        @Override
        public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
            return new SavedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
            HashMap<String, String> quote = quotes.get(position);
            String quoteText = quote.get("quote");
            String author = quote.get("author");

            holder.quote_text.setText(quoteText);
            holder.quote_author.setText("- " + author + " -");

            Random rnd = new Random();
            int color = Color.argb(255,
                    rnd.nextInt(128) + 128,
                    rnd.nextInt(128) + 128,
                    rnd.nextInt(128) + 128);
            holder.bgLinearLayout.setBackgroundColor(color);

            // Update Delete Button
            holder.tvSave.setText("Delete");
            holder.imageSave.setImageResource(R.drawable.delete);

            // Delete button functionality
            holder.btn_save.setOnClickListener(v -> {
                HashMap<String, String> quoteToDelete = savedQuotes.get(position);

                // Remove quote using SharedPreferences
                SharedPreferencesManager manager = new SharedPreferencesManager(requireContext());
                manager.deleteQuote(quoteToDelete);

                // Update RecyclerView
                savedQuotes.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, savedQuotes.size());

                Toast.makeText(requireContext(), "Quote deleted!", Toast.LENGTH_SHORT).show();
            });

            // Share Button Functionality
            holder.btn_share.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "'" + quote + "'" + " - " + author);
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share Quote"));
            });

            // Copy Button Functionality
            holder.btn_copy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Quote", "'" + quote + "'" + " - " + author);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "Quote copied to clipboard!", Toast.LENGTH_SHORT).show();
            });

        }

        @Override
        public int getItemCount() {
            return quotes.size();
        }

        public class SavedViewHolder extends RecyclerView.ViewHolder {
            LinearLayout bgLinearLayout, btn_share, btn_copy, btn_save;
            TextView quote_text, quote_author, tvSave;
            ImageView imageSave;

            public SavedViewHolder(@NonNull View itemView) {
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
//================= SavedQuotesAdapter End ==========================================
//==================================================================================================

}//Main End...