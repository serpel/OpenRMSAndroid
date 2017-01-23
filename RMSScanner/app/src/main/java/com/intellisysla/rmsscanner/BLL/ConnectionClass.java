package com.intellisysla.rmsscanner.BLL;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by alienware on 12/7/2016.
 */

public class ConnectionClass {
    private static final String TAG = "ConnectionClass";
    private static final String classs = "net.sourceforge.jtds.jdbc.Driver";

    @SuppressLint("NewApi")
    static Connection CONN(String ip, String db, String user, String pass) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + "/" + db
                    + ";user=" + user
                    + ";password=" + pass
                    + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return conn;
    }
}
