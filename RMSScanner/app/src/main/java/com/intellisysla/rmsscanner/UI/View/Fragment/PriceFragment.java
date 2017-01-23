package com.intellisysla.rmsscanner.UI.View.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceFragment extends Fragment {
    private static final String TAG = "PriceFragment" ;

    private Button saveButton, removeButton;
    private TextView price_text, begin_text, end_text, code_text, store_text;
    private EditText offer_edit;
    private Store store;
    private Item foundItem;
    private float offer;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date_begin, date_end;
    private String begindate, enddate;
    private CheckBox isOfferActive;

    private OnFragmentInteractionListener mListener;

    public PriceFragment() {
        // Required empty public constructor
    }

    public void Clear(){

        foundItem = null;

        if(code_text != null){
            code_text.setText("");
        }

        if(offer_edit != null){
            offer_edit.setText("0");
        }

        if(begin_text != null){
            begin_text.setText("");
        }

        if(end_text != null){
            end_text.setText("");
        }

        if(price_text != null){
            price_text.setText("");
        }

        if(isOfferActive != null){
            isOfferActive.setChecked(false);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PriceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceFragment newInstance() {
        PriceFragment fragment = new PriceFragment();
        return fragment;
    }


    public void SetItem(Item item) {
        foundItem = item;

        if(offer_edit != null){
            offer_edit.setText(String.valueOf(foundItem.getOffer()));
        }

        if(isOfferActive != null){
            isOfferActive.setChecked(foundItem.isOfferActive());
        }
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

        View rootView = inflater.inflate(R.layout.fragment_price, container, false);
        // Inflate the layout for this fragment

        code_text = (TextView) rootView.findViewById(R.id.itemcode_text);
        price_text = (TextView) rootView.findViewById(R.id.price_text);
        offer_edit = (EditText) rootView.findViewById(R.id.offer_edit);
        store_text = (TextView) rootView.findViewById(R.id.store_text);
        isOfferActive = (CheckBox) rootView.findViewById(R.id.is_offer_active_check);

        begin_text = (TextView) rootView.findViewById(R.id.begin_text);
        end_text = (TextView) rootView.findViewById(R.id.end_text);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        removeButton = (Button) rootView.findViewById(R.id.remove_button);

        if(foundItem != null){
            code_text.setText(foundItem.getItem_code());
            begin_text.setText(foundItem.getSalesStartDate() != null ? foundItem.getSalesStartDate().toString() : null);
            end_text.setText(foundItem.getSalesStartDate() != null ? foundItem.getSalesEndDate().toString() : null);
            price_text.setText(String.valueOf(foundItem.getPrice()));
            offer_edit.setText(String.valueOf(foundItem.getOffer()));
            isOfferActive.setChecked(foundItem.isOfferActive());
        }

        if(store_text != null){
            store_text.setText(store.getName());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foundItem != null){
                    try {
                        offer = Float.valueOf(offer_edit.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(getContext(), "error en campo de oferta", Toast.LENGTH_LONG).show();
                        Log.e(TAG, e.getMessage());
                    }
                    begindate = begin_text.getText().toString();
                    enddate = end_text.getText().toString();
                    UpdateTask updateTask = new UpdateTask();
                    updateTask.execute(store);
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foundItem != null){
                    RemoveTask removeTask = new RemoveTask();
                    removeTask.execute(store);
                }
            }
        });


        myCalendar = Calendar.getInstance();

        date_begin = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                begin_text.setText(sdf.format(myCalendar.getTime()));
            }
        };

        date_end = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                end_text.setText(sdf.format(myCalendar.getTime()));
            }
        };

        begin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date_begin, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        end_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date_end, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        @Override
        protected Void doInBackground(Store... params) {
            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0], getContext());

                if(foundItem == null)
                    return null;

                if(offer > 0 ){
                    result = executeQuery.updateOffer(foundItem.getId(), offer, begindate, enddate);
                }else
                {
                    result = 0;
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result > 0){
                Toast.makeText(getContext(), "Se actualizo exitosamente la oferta!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "No se pudo actualizar", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class RemoveTask extends AsyncTask<Store, Void, Void> {

        private static final String TAG = "Remove";
        private int result = 0;

        @Override
        protected Void doInBackground(Store... params) {
            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0], getContext());

                if(foundItem == null)
                    return null;

                result = executeQuery.removeOffer(foundItem.getId());

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            offer_edit.setText("");

            if(result > 0){
                isOfferActive.setChecked(false);
                Toast.makeText(getContext(), "Se removio exitosamente!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "No se pudo remover", Toast.LENGTH_LONG).show();
            }
        }
    }
}
