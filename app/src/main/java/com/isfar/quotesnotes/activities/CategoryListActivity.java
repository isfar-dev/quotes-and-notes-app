package com.isfar.quotesnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.isfar.quotesnotes.Category;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.monetization.Admob;
import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    RecyclerView catListRecyclerView;
    MaterialToolbar categoryListToolbar;
    List<Category> categories = new ArrayList<>();
    LinearLayout adContainerLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_linear), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //================================================
        catListRecyclerView = findViewById(R.id.catListRecyclerView);
        catListRecyclerView.setLayoutManager(new GridLayoutManager(CategoryListActivity.this, 2));
        categoryListToolbar = findViewById(R.id.categorylist_toolbar);
        adContainerLinear = findViewById(R.id.ad_container_linear);
        //================================================


        //============== Back =================
        categoryListToolbar.setNavigationOnClickListener(v -> finish());


        // banner Ad....
        Admob.setBannerAd(adContainerLinear, CategoryListActivity.this);


        // list of categories...
        categoryList();


        //============ set Adapter(Category Wise) ==================================
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, category -> {
            Admob.showInterstitialAdinThreeClick(CategoryListActivity.this);
            Intent intent = new Intent(CategoryListActivity.this, CategoryActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        catListRecyclerView.setAdapter(categoryAdapter);



    }//onCreate End...


//==============================================================================================
// Category Item List ====================================================
    private void categoryList() {

        categories = new ArrayList<>();
        categories.add(new Category("Motivational", R.drawable.motivational));
        categories.add(new Category("Life", R.drawable.life));
        categories.add(new Category("Love", R.drawable.love));
        categories.add(new Category("Happy", R.drawable.happy));
        categories.add(new Category("Sadness", R.drawable.sadness));
        categories.add(new Category("Attitude", R.drawable.attitude));
        categories.add(new Category("People", R.drawable.people));
        categories.add(new Category("Time", R.drawable.time));
        categories.add(new Category("Positive Thinking", R.drawable.positive_thinking));
        categories.add(new Category("Productivity", R.drawable.productivity));
        categories.add(new Category("Focus", R.drawable.focus));
        categories.add(new Category("Confidence", R.drawable.confidence));
        categories.add(new Category("Wisdom", R.drawable.wisdom));
        categories.add(new Category("Ambition", R.drawable.ambition));
        categories.add(new Category("Overthinking", R.drawable.overthinking));
        categories.add(new Category("Stress", R.drawable.stress));
        categories.add(new Category("Depression", R.drawable.depression));
        categories.add(new Category("Gratitude", R.drawable.gratitude));
        categories.add(new Category("Affirmations", R.drawable.affirmations));
        categories.add(new Category("Compassion", R.drawable.compassion));
        categories.add(new Category("Success", R.drawable.success));
        categories.add(new Category("Truth", R.drawable.truth));
        categories.add(new Category("Self-development", R.drawable.self_development));
        categories.add(new Category("Money", R.drawable.money));
        categories.add(new Category("Fitness", R.drawable.fitness));
        categories.add(new Category("Kindness", R.drawable.kindness));
        categories.add(new Category("Regret", R.drawable.regret));
        categories.add(new Category("Growth", R.drawable.growth));
        categories.add(new Category("Faith", R.drawable.faith));
        categories.add(new Category("Parents", R.drawable.parents));
        categories.add(new Category("Leadership", R.drawable.leadership));
        categories.add(new Category("Accomplishment", R.drawable.accomplishment));
        categories.add(new Category("Opportunity", R.drawable.opportunity));
        categories.add(new Category("Friends", R.drawable.friends));

    }
// Category Item List ====================================================
//==============================================================================================

//==================================================================================================
//================= CATEGORY ADAPTER START ===================================================
//==================================================================================================
    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        private final List<Category> categories;
        private final OnCategoryClickListener clickListener;

        public interface OnCategoryClickListener {
            void onCategoryClick(String categoryName);
        }

        public CategoryAdapter(List<Category> categories, OnCategoryClickListener clickListener) {
            this.categories = categories;
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = categories.get(position);
            holder.categoryName.setText(category.getName());
            holder.categoryImage.setImageResource(category.getImageResId());

            holder.itemView.setOnClickListener(v -> clickListener.onCategoryClick(category.getName()));

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public static class CategoryViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryImage;

            public CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.category_name);
                categoryImage = itemView.findViewById(R.id.category_image);
            }
        }
    }
//==================================================================================================
//================= CATEGORY ADAPTER END ====================================================
//==================================================================================================

}//Main End...