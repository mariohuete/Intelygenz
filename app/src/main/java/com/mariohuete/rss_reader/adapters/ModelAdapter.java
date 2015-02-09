package com.mariohuete.rss_reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.mariohuete.rss_reader.MainActivity;
import com.mariohuete.rss_reader.R;
import com.mariohuete.rss_reader.models.Model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mariobama on 09/02/15.
 */
public class ModelAdapter extends ArrayAdapter<Model> implements Filterable {

    //ATTRIBUTES:
    private List<Model> modelList;
    private Context context;
    private Filter modelFilter;
    private List<Model> origModelList;
    private String URL_PHOTO;

    //METHODS:
    public ModelAdapter(List<Model> list, Context ctx) {
        super(ctx, R.layout.img_row_layout, list);
        this.modelList = list;
        this.context = ctx;
        this.origModelList = list;
        URL_PHOTO = ctx.getString(R.string.end_point)+ctx.getString(R.string.photos);
    }

    public int getCount() {
        return modelList.size();
    }

    public Model getItem(int position) {
        return modelList.get(position);
    }

    public long getItemId(int position) {
        return modelList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ModelHolder holder = new ModelHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.img_row_layout, null);
            // Now we can fill the layout with the right values
            TextView tv = (TextView) v.findViewById(R.id.name);
            TextView distView = (TextView) v.findViewById(R.id.dist);
            ImageView img = (ImageView) v.findViewById(R.id.img);

            holder.modelNameView = tv;
            holder.distView = distView;
            holder.image = img;

            v.setTag(holder);
        }
        else
            holder = (ModelHolder) v.getTag();

        Model p = modelList.get(position);
        holder.modelNameView.setText(p.getName());
        holder.distView.setText("" + p.getInstructions());
        Ion.with(context)
                .load(URL_PHOTO + p.getPhoto())
                .withBitmap()
                /*.placeholder(R.drawable.placeholder_image)*/
                .error(R.mipmap.ic_launcher)
                /*.animateLoad(spinAnimation)
                .animateIn(fadeInAnimation)*/
                .intoImageView(holder.image);

        return v;
    }

    public void resetData() {
        modelList = origModelList;
    }

    /* *********************************
     * We use the holder pattern
     * It makes the view faster and avoid finding the component
     * **********************************/
    private static class ModelHolder {
        public TextView modelNameView;
        public TextView distView;
        public ImageView image;
    }

	/*
	 * We create our filter
	 */

    @Override
    public Filter getFilter() {
        if (modelFilter == null)
            modelFilter = new ModelFilter();

        return modelFilter;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origModelList;
                results.count = origModelList.size();
            }
            else {
                // We perform filtering operation
                List<Model> nModelList = new ArrayList<Model>();

                for (Model p : modelList) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nModelList.add(p);
                }

                results.values = nModelList;
                results.count = nModelList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                modelList = (List<Model>) results.values;
                notifyDataSetChanged();
            }
        }

    }
}