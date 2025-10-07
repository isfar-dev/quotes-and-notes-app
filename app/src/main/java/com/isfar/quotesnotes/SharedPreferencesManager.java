package com.isfar.quotesnotes;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "saved_quotes_prefs";
    private static final String QUOTES_KEY = "saved_quotes";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Save a quote
    public void saveQuote(HashMap<String, String> quote) {
        List<HashMap<String, String>> savedQuotes = getSavedQuotes();
        if (!savedQuotes.contains(quote)) { // Avoid duplicates
            savedQuotes.add(quote);
        }
        saveQuotesList(savedQuotes);
    }

    // Get all saved quotes
    public List<HashMap<String, String>> getSavedQuotes() {
        String json = sharedPreferences.getString(QUOTES_KEY, null);
        Type type = new TypeToken<List<HashMap<String, String>>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    // Delete a specific quote
    public void deleteQuote(HashMap<String, String> quote) {
        List<HashMap<String, String>> savedQuotes = getSavedQuotes();
        savedQuotes.remove(quote);
        saveQuotesList(savedQuotes);
    }

    // Helper to save the list back to SharedPreferences
    private void saveQuotesList(List<HashMap<String, String>> quotes) {
        String json = gson.toJson(quotes);
        sharedPreferences.edit().putString(QUOTES_KEY, json).apply();
    }

}
