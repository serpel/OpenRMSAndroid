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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intellisysla.rmsscanner.BLL.ActivityCommunicator;
import com.intellisysla.rmsscanner.BLL.ExecuteQuery;
import com.intellisysla.rmsscanner.BLL.FragmentCommunicator;
import com.intellisysla.rmsscanner.BLL.Utils;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DepartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DepartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartmentFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ActivityCommunicator activityCommunicator;

    private Item foundItem;
    private Spinner department_spinner;
    private ArrayList<Department> departments;
    private Button saveButton;
    private Store store;
    private TextView store_text, code_text;
    private int new_department;

    private OnFragmentInteractionListener mListener;

    public DepartmentFragment() {
        // Required empty public constructor
    }

    public void Clear(){

        foundItem = null;

        if(code_text != null){
            code_text.setText("");
        }

        if(department_spinner != null){
            department_spinner.setSelection(0);
        }
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DepartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepartmentFragment newInstance() {
        DepartmentFragment fragment = new DepartmentFragment();
        return fragment;
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

        View rootView = inflater.inflate(R.layout.fragment_department, container, false);
        department_spinner = (Spinner) rootView.findViewById(R.id.department_spinner);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        store_text = (TextView) rootView.findViewById(R.id.store_text);
        code_text = (TextView) rootView.findViewById(R.id.itemcode_text);

        if(store_text != null){
            store_text.setText(store.getName());
        }

        if(foundItem != null){
            if(code_text != null){
                code_text.setText(foundItem.getItem_code());
            }

            if(departments != null && department_spinner != null ){
                ArrayAdapter<Department> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, departments);
                department_spinner.setAdapter(adapter);

                int pos = adapter.getPosition(new Department(foundItem.getDepartpment_id(), foundItem.getDepartmentName()));
                department_spinner.setSelection(pos);
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foundItem != null){
                    Department d = (Department) department_spinner.getSelectedItem();
                    if(foundItem.getDepartpment_id() != d.getId())
                    {
                        new_department = d.getId();
                        UpdateTask updateTask = new UpdateTask();
                        updateTask.execute(store);
                    }
                }
            }
        });

        DepartmentSearch departmentSearch = new DepartmentSearch();
        departmentSearch.execute(store);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        activityCommunicator =(ActivityCommunicator)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void SetItem(Item item) {
        foundItem = item;

        if(foundItem != null){
            if(code_text != null){
                code_text.setText(foundItem.getItem_code());
            }

            if(departments != null && department_spinner != null ){
                ArrayAdapter<Department> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, departments);
                department_spinner.setAdapter(adapter);

                int pos = adapter.getPosition(new Department(foundItem.getDepartpment_id(), foundItem.getDepartmentName()));
                department_spinner.setSelection(pos);
            }
        }
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

    private class UpdateTask extends AsyncTask<Store, Void, Void>{

        private static final String TAG = "UpdateTask";
        private int result = 0;

        @Override
        protected Void doInBackground(Store... params) {
            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0], getActivity());

                if(foundItem == null)
                    return null;

                result = executeQuery.updateDepartment(foundItem.getId(), new_department);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result > 0){
                Toast.makeText(getContext(), "Se actualizo exitosamente!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DepartmentSearch extends AsyncTask<Store, Void, Void>{

        private static final String TAG = "DepartmentSearch";

        @Override
        protected Void doInBackground(Store... params) {
            try {
                ExecuteQuery executeQuery = new ExecuteQuery(params[0], getContext());

                departments = executeQuery.getDepartments();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(departments != null && department_spinner != null){
                ArrayAdapter<Department> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, departments);
                department_spinner.setAdapter(adapter);

                if(foundItem != null){
                    int pos = adapter.getPosition(new Department(foundItem.getDepartpment_id(), foundItem.getDepartmentName()));
                    department_spinner.setSelection(pos);
                }
            }
        }
    }
}
