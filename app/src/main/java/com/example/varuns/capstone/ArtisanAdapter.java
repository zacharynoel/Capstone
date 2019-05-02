package com.example.varuns.capstone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.varuns.capstone.model.Artisan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ArtisanAdapter extends BaseAdapter implements Filterable {
    List<Artisan> artisans;
    List<Artisan> filteredArtisans;
    private ArtisanFilter artisanFilter;
    private Context context;

    private class AlphabeticSort implements Comparator<Artisan> {
        public int compare(Artisan a1, Artisan a2) {
            return (a1.getFirstName() + a1.getLastName())
                    .compareTo(a2.getFirstName() + a2.getLastName());
        }
    }

    private class ItemCountSort implements Comparator<Artisan> {
        public int compare(Artisan a1, Artisan a2) {
            return a2.getArtisanItems().size() - a1.getArtisanItems().size();
        }
    }

    private class ProductCountSort implements Comparator<Artisan> {
        public int compare(Artisan a1, Artisan a2) {
            return a2.getSoldItems().size() - a1.getSoldItems().size();
        }
    }

    private class DateAddedSort implements Comparator<Artisan> {
        public int compare(Artisan a1, Artisan a2) {
            //TODO - this works because artisan Id's are incremental,
            // if this were to change this would break
            return a1.getArtisanId() - a2.getArtisanId();
        }
    }

    public List<Artisan> simulateFilter(CharSequence constraint) {
        return getArtisanFilter().simulateFiltering(constraint);
    }

    private class ArtisanFilter extends Filter {

        public List<Artisan> simulateFiltering(CharSequence constraint) {
            return (List<Artisan>) performFiltering(constraint).values;
        }
        @Override
        public FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            //case: user has queried a value
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Artisan> filtered = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                String[] separated = ((String) constraint).split(" ");

                // search content in friend list
                for (Artisan a : artisans) {
                    String first = a.getFirstName().toLowerCase();
                    String last = a.getLastName().toLowerCase();

                    //user only has searched whitespace
                    if (separated.length == 0)
                        break;

                    boolean firstValid = true, lastValid = true;
                    for (int i = 0; i < separated[0].length(); i++) {
                        //check first whitespace separated query against first name
                        if (first.length() <= i ||
                                (separated[0].charAt(i) != first.charAt(i))) {
                            firstValid = false;
                        }

                        //check first whitespace separated query against last name
                        if (last.length() <= i ||
                                (separated[0].charAt(i) != last.charAt(i))) {
                            lastValid = false;
                        }
                    }

                    //user searched 2 words - this assumes second word is last name
                    if (separated.length >= 2) {
                        lastValid = true;
                        for (int i = 0; i < separated[1].length(); i++) {
                            //check first whitespace separated query against last name
                            if (last.length() <= i ||
                                    (separated[1].charAt(i) != last.charAt(i))) {
                                lastValid = false;
                            }
                        }
                    }

                    if (firstValid || lastValid) {
                        filtered.add(a);
                    }
                }

                filterResults.count = filtered.size();
                filterResults.values = filtered;
            }

            //case: search query is empty
            else {
                filterResults.count = artisans.size();
                filterResults.values = artisans;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredArtisans = (ArrayList<Artisan>) results.values;
            notifyDataSetChanged();
        }
    }

    @SuppressLint("NewApi")
    public void sortAlphabetically() {
        filteredArtisans.sort(new AlphabeticSort());
        notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    public void sortByDate() {
        filteredArtisans.sort(new DateAddedSort());
        notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    public void sortByNumberOfItems() {
        filteredArtisans.sort(new ItemCountSort());
        notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    public void sortByProductsSold() {
        filteredArtisans.sort(new ProductCountSort());
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (artisanFilter == null) {
            artisanFilter = new ArtisanFilter();
        }

        return artisanFilter;
    }

    public ArtisanFilter getArtisanFilter() {
        if (artisanFilter == null) {
            artisanFilter = new ArtisanFilter();
        }

        return artisanFilter;
    }

    public ArtisanAdapter(Context context, List<Artisan> artisans) {
        this.artisans = artisans;
        this.filteredArtisans = artisans;
        this.context = context;
        getFilter();
    }

    public void addArtisan(Artisan a) {
        artisans.add(a);
    }

    public List<Artisan> getArtisans() {
        return artisans;
    }

    public int getCount() {
        return filteredArtisans.size();
    }

    public Artisan getItem(int i) {
        return filteredArtisans.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.artisan_list_layout, null);
        ImageView artisanImage = (ImageView)view.findViewById(R.id.artisanImage);

        TextView artisanName = (TextView)view.findViewById(R.id.artisanName);
        artisanName.setText(filteredArtisans.get(i).getFirstName() + " "
                + filteredArtisans.get(i).getLastName());

        int ran_seed = Math.abs((filteredArtisans.get(i).getFirstName()).hashCode());
        System.out.println(ran_seed);
        artisanImage.setImageResource(((menu_activity)context).artisanImages[ran_seed%3]);

        return view;
    }
}