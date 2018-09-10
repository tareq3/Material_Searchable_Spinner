/*
 * Created by Tareq Islam on 9/9/18 1:03 AM
 *
 *  Last modified 9/9/18 1:03 AM
 */

package com.mti.searchable_spinner_ext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/***
 * Created by Tareq on 09,September,2018.
 */
public class SearchableSpinnerAdapter  extends ArrayAdapter<String> implements Filterable{

    public static  SearchableSpinnerAdapter instance=null; //we need that to be static so that nobody can try to access this class before intializing instance


    ArrayList<String> mArrayList=new ArrayList<>();

    //Creating a single Instance so that we can create only one object out of that class
    public static SearchableSpinnerAdapter getInstance(Context context,int resource, ArrayList<String> modelArrayList){

        if(instance==null){
            instance=new SearchableSpinnerAdapter(context,resource,modelArrayList);
            instance.mArrayList=modelArrayList;


            return instance;

        }else{
            return instance;
        }
    }

    public SearchableSpinnerAdapter(@NonNull Context context,int resource,  ArrayList<String> arrayList) {
        super(context,resource);
        mArrayList = arrayList;
    }










    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public String getItem(int position) {
        return mArrayList.get(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO: inflate your view HERE ...

        return super.getView(position,convertView,parent);
    }


    private DataFilter m_DataFilter;

    @Override
    public Filter getFilter() {
        if (m_DataFilter == null)
            m_DataFilter = new DataFilter();
        return m_DataFilter;
    }
}

//Filter
class DataFilter extends Filter {


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        // Create a FilterResults object
        FilterResults results = new FilterResults();

        // If the constraint (search string/pattern) is null
        // or its length is 0, i.e., its empty then
        // we just set the `values` property to the
        // original contacts list which contains all of them
        if (constraint == null || constraint.length() == 0) {
            results.values = SearchableSpinner.mSearchableArraylist;
            results.count = SearchableSpinner.mSearchableArraylist.size();
        } else { // Some search constraint has been passed
            // so let's filter accordingly
            ArrayList<String> filteredContacts = new ArrayList<>();

            // We'll go through all the contacts and see
            // if they contain the supplied string
            for (String c :  SearchableSpinnerAdapter.instance.mArrayList) {
                if (c.toUpperCase().contains(constraint.toString().toUpperCase())) {
                    // if `contains` == true then add it
                    // to our filtered list
                    filteredContacts.add(c);
                }
            }
            if(filteredContacts.size()<=0){


                filteredContacts.add(0,"none");
            }
            // Finally set the filtered values and size/count
            results.values = filteredContacts;
            results.count = filteredContacts.size();

        }
// Return our FilterResults object
        return results;
    }


    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        SearchableSpinnerAdapter.instance.mArrayList = (ArrayList<String>) results.values;
        SearchableSpinnerAdapter.instance.notifyDataSetChanged();
    }
}