package com.intellisysla.rmsscanner.UI.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AliasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AliasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AliasFragment extends Fragment {

    private String new_alias;
    private Item foundItem;
    private Button saveButton;
    private Store store;
    private TextView store_text, code_text;
    private EditText alias_edit;

    private OnFragmentInteractionListener mListener;

    public AliasFragment() {
        // Required empty public constructor
    }


    public void Clear(){

        foundItem = null;

        if(code_text != null){
            code_text.setText("");
        }

        if(alias_edit != null){
            alias_edit.setText("");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AliasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AliasFragment newInstance() {
        AliasFragment fragment = new AliasFragment();
        return fragment;
    }

    public void SetItem(Item item){
        foundItem = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_alias, container, false);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        store_text = (TextView) rootView.findViewById(R.id.store_text);
        code_text = (TextView) rootView.findViewById(R.id.itemcode_text);
        alias_edit = (EditText) rootView.findViewById(R.id.alias_edit);

        if(store_text != null ){
            store_text.setText(store.getName());
        }

        if(foundItem != null){
            if(code_text != null){
                code_text.setText(foundItem.getItem_code());
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foundItem != null){
                    new_alias = alias_edit.getText().toString();

                    UpdateTask updateTask = new UpdateTask();
                    updateTask.execute(store);
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private class UpdateTask extends AsyncTask<Store, Void, Void> {

        private static final String TAG = "UpdateTask";
        private int result = 0;
        private int isFound = 0;

        @Override
        protected Void doInBackground(Store... params) {
            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0], getContext());

                if(foundItem == null && new_alias == null)
                    return null;

                isFound = executeQuery.searchAlias(foundItem.getId(), new_alias.trim());

                if(isFound == 0){

                    Item item = executeQuery.getItem(new_alias.trim());

                    if(item == null){
                         if(executeQuery.insertAlias(foundItem.getId(), new_alias.trim()) == 1){
                            result = 0;
                         }
                    }else
                    {
                        result = 2;
                    }
                }else{
                    result = 1;
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alias_edit.setText("");

            if(result == 0){
                Toast.makeText(getContext(), "Se agrego exitosamente!", Toast.LENGTH_LONG).show();
            }else if (result == 1){
                Toast.makeText(getContext(), "Alias ya existe!", Toast.LENGTH_LONG).show();
            }else if(result == 2){
                Toast.makeText(getContext(), "Codigo ya existe!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
