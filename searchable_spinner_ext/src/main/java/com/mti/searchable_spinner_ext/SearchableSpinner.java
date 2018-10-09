/*
 * Created by Tareq Islam on 9/8/18 4:14 PM
 *
 *  Last modified 1/16/18 7:34 PM
 */

package com.mti.searchable_spinner_ext;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;


import android.widget.SpinnerAdapter;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinner extends android.support.v7.widget.AppCompatSpinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem {


    private Context _context;
    private List _items;
    private SearchableListDialog _searchableListDialog;

    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;

    private boolean _isFromInit; //this is true on initialization


    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);

            }
        }
        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }


    public void setCusBackground(Drawable resourceId){
        if(resourceId==null)
          setBackground(getResources().getDrawable(R.drawable.ss_dropdown)); //for changing Spinner background
        else
            setBackground(resourceId);
    }

    private void init() {

            _items = new ArrayList();
        _searchableListDialog = SearchableListDialog.newInstance(_items);

        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();

    }

    //On touch we will show the search list dialog
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (_searchableListDialog.isAdded()) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (null != _arrayAdapter) {

                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                _items.clear();
                for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                    _items.add(_arrayAdapter.getItem(i));
                }
                // Change end.

                _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");

            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {

       _arrayAdapter = (ArrayAdapter) adapter;

        if(item_to_select!=-1){ //if user want to show any item on initialization
            super.setAdapter(adapter);
            setSelection(item_to_select);



        }else if (!TextUtils.isEmpty(_strHintText) && !_isDirty) { // if there is a hint  and not dirty means first time

                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, R.layout
                        .searchable_spinner_item_default, new String[]{_strHintText});

                super.setAdapter(arrayAdapter);
       }
       else { //if second time using

                super.setAdapter(adapter);
       }


    }

    public static String selectedItem=null;
    @Override
    public void onSearchableItemClicked(Object item, int position) {
        try {
            if(position>=0) {
                setSelection(position);
                selectedItem = item.toString();
            }
        } catch (Exception e) {
            Toast.makeText(_context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(_context, ""+mSearchableArraylist.indexOf(item)+"-"+item.toString(), Toast.LENGTH_SHORT).show();

        }

        if (!_isDirty ) {

            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(position);
        }
    }


    public void setPositiveButton(String strPositiveButtonText) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText);
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText, onClickListener);
    }

    public void setOnSearchTextChangedListener(SearchableListDialog.OnSearchTextChanged onSearchTextChanged) {
        _searchableListDialog.setOnSearchTextChangedListener(onSearchTextChanged);
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return item_to_select;
        } else {
            if(!selectedItem.equals("none")&& selectedItem!=null)
                return  mSearchableArraylist.indexOf(selectedItem);
            else
                return item_to_select;
           // return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {

        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return "none";
        } else {
            return selectedItem;
        }
    }


    private   ArrayList<String> mSearchableArraylist;
    public void setArrayList_String( ArrayList<String> searchableArrayList){


        mSearchableArraylist=searchableArrayList;


// Create an ArrayAdapter using the string array and a default spinner layout
        SearchableSpinnerAdapter adapter=SearchableSpinnerAdapter.getInstance(_context,R.layout.searchable_spinner_item,mSearchableArraylist);



// Apply the adapter to the spinner
        setAdapter(adapter);



    }

    public void setTitle(String strTitle) {
        _searchableListDialog.setTitle(strTitle);
    }

    public void setTitle(String strTitle, int strTitleColor){
        _searchableListDialog.setTitle(strTitle,strTitleColor);

    }


    private  int item_to_select=-1; //the pos of Item selected
public void setItemSelected(int pos){
        item_to_select=pos;
}

}
