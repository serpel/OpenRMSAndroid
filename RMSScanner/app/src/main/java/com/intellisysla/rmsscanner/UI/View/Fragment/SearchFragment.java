package com.intellisysla.rmsscanner.UI.View.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;
import com.intellisysla.rmsscanner.UI.View.Activity.ItemListActivity;
import com.intellisysla.rmsscanner.UI.View.Activity.SearchActivity;
import com.intellisysla.rmsscanner.UI.View.Activity.SearchListActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String ARG_ITEM = "item";
    public static final String TAG = "SearchFragment";

    EditText search_edit, new_quantity_edit, offer_edit;
    Button save_button;
    TextView description_text, itemcode_text, quantity_text, fisic_quantity, price_text, search_text,
            store_text, offer_text, begin_text, end_text, department_text;
    LinearLayout item_menu, item_list_menu, search_menu;
    String code;
    Store store;
    ImageView imageView;
    ProgressBar pbHeaderProgress;
    public static Item foundItem;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item Item 1.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(Item item) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            foundItem = (Item) getArguments().getSerializable(ARG_ITEM);
        }

        SharedPreferences preferences = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        store = new Store(
                preferences.getString("server", ""),
                preferences.getString("db", ""),
                preferences.getString("user", ""),
                preferences.getString("pass", ""),
                preferences.getString("storeName", ""),
                preferences.getString("username", ""),
                preferences.getString("location", ""));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        pbHeaderProgress = (ProgressBar) rootView.findViewById(R.id.pbHeaderProgress);
        imageView = (ImageView) rootView.findViewById(R.id.back_button);
        item_menu = (LinearLayout) rootView.findViewById(R.id.item_menu);
        item_list_menu = (LinearLayout) rootView.findViewById(R.id.item_list_menu);
        search_menu = (LinearLayout) rootView.findViewById(R.id.search_menu);

        search_edit = (EditText) rootView.findViewById(R.id.seach_edit);
        search_text = (TextView) rootView.findViewById(R.id.search_text);
        save_button = (Button) rootView.findViewById(R.id.save_button);
        itemcode_text = (TextView) rootView.findViewById(R.id.itemcode_text);
        description_text = (TextView) rootView.findViewById(R.id.description_text);
        quantity_text = (TextView) rootView.findViewById(R.id.quantity_text);
        fisic_quantity = (TextView) rootView.findViewById(R.id.fisicquantity_text);
        new_quantity_edit = (EditText) rootView.findViewById(R.id.new_quantity_edit);
        //department_spinner = (Spinner) rootView.findViewById(R.id.department_spinner);
        department_text = (TextView) rootView.findViewById(R.id.department) ;
        store_text = (TextView) rootView.findViewById(R.id.store_text);
        price_text = (TextView) rootView.findViewById(R.id.price_text);
        offer_text = (TextView) rootView.findViewById(R.id.offer_text);
        begin_text = (TextView) rootView.findViewById(R.id.begin_text);
        end_text = (TextView) rootView.findViewById(R.id.end_text);

        if(store_text != null){
            store_text.setText(store.getName());
        }

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    Search();
                    return true;
                }
                return false;
            }
        });


        final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Dialog)).create();

        alertDialog.setTitle(getString(R.string.dialog_name));
        alertDialog.setMessage(getString(R.string.dialog_message));

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int qty = Integer.valueOf(new_quantity_edit.getText().toString());

                        if(foundItem != null && qty > -99){
                            Save(qty);
                            ClearUI();
                        }
                    }
                });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        new_quantity_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    if(foundItem != null){

                        try {
                            String value = new_quantity_edit.getText().toString();

                            if (value.length() < 4) {
                                int qty = Integer.valueOf(value);
                                if (qty > -99 && qty <= 300) {
                                    Save(qty);
                                    ClearUI();
                                } else if (qty > 300 && qty <= 999) {
                                    alertDialog.show();
                                } else if (qty > 999) {
                                    Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e ){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(foundItem != null){

                    try {
                        String value = new_quantity_edit.getText().toString();

                        if (value.length() < 4) {
                            int qty = Integer.valueOf(value);
                            if (qty > -99 && qty <= 300) {
                                Save(qty);
                                ClearUI();
                            } else if (qty > 300 && qty < 999) {
                                alertDialog.show();
                            } else if (qty > 999) {
                                Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(getContext(), "No puedes agregar esta cantidad", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });

        item_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "item", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(getContext(), SearchActivity.class);
                //startActivity(i);
            }
        });

        item_list_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "item_list", Toast.LENGTH_SHORT).show();
                if(foundItem != null){
                    Intent i = new Intent(getContext(),ItemListActivity.class);
                    i.putExtra("itemlookup", foundItem.getItem_code());
                    startActivity(i);
                }
            }
        });

        search_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SearchActivity.this, "fragment_search3", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), SearchListActivity.class);
                i.putExtra("store", store_text.getText().toString());
                startActivity(i);
            }
        });

        if(foundItem != null){
            fillData();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearUI();
            }
        });

        return rootView;
    }

    private boolean Search(){
        code = search_edit.getText().toString().trim();

        if(!code.isEmpty()){
            SearchBackground searchBackground = new SearchBackground();
            searchBackground.execute(store);

            new_quantity_edit.requestFocus();

            return true;
        }

        return false;
    }

    private void fillData(){

        itemcode_text.setText(foundItem.getItem_code());
        description_text.setText(foundItem.getDescription());
        quantity_text.setText(String.valueOf(foundItem.getQuantity()));
        fisic_quantity.setText(String.valueOf(foundItem.getFisic_quatity()));
        offer_text.setText(String.valueOf(foundItem.getOffer()));

        if(foundItem.isOfferActive()){
            offer_text.setTextColor(Color.GREEN);
        }else{
            offer_text.setTextColor(Color.RED);
        }
        price_text.setText(String.valueOf(foundItem.getPrice()));
        begin_text.setText(String.valueOf(foundItem.getSalesStartDate()));
        end_text.setText(String.valueOf(foundItem.getSalesEndDate()));
        department_text.setText(foundItem.getDepartmentName());

        new_quantity_edit.requestFocus();

        SearchActivity activity = (SearchActivity) getActivity();
        activity.passDataToActivity(foundItem);
    }

    private void Save(int quantity)
    {
        int q = foundItem.getFisic_quatity() + quantity;

        if(q > 0){
            ExecuteQuery executeQuery = new ExecuteQuery(store);
            int result = executeQuery.updateQuantity(foundItem.getItem_code(), quantity);

            if(result > 0){
                executeQuery.insertInput(foundItem.getItem_code(), store.getLocation(), quantity, store.getUsername());
                Toast.makeText(getContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Cantidad: " + q + " invalida", Toast.LENGTH_LONG).show();
        }

        search_edit.requestFocus();
    }

    private void ClearUI(){
        foundItem = null;
        search_edit.setText("");
        //department_spinner.setSelection(0);
        department_text.setText("");
        itemcode_text.setText("");
        description_text.setText("");
        quantity_text.setText("0");
        fisic_quantity.setText("0");
        new_quantity_edit.setText("");
        price_text.setText("");
        offer_text.setText("");
        begin_text.setText("");
        end_text.setText("");
        search_edit.requestFocus();

        SearchActivity activity = (SearchActivity) getActivity();
        activity.Clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class SearchBackground extends AsyncTask<Store, Void, Void> {

        private static final String TAG = "SearchBackground";

        @Override
        protected void onPreExecute() {
            pbHeaderProgress.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Store... params) {

            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0]);

                foundItem = executeQuery.getItem(code);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            pbHeaderProgress.setVisibility(View.GONE);

            if(foundItem == null){
                ClearUI();
                Toast.makeText(getContext(), "Codigo no encontrado", Toast.LENGTH_LONG).show();
            }else{
                fillData();
            }
        }
    }

}
