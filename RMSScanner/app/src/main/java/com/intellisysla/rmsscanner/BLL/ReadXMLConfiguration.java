package com.intellisysla.rmsscanner.BLL;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.intellisysla.rmsscanner.DAL.Store;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by alienware on 12/11/2016.
 */

public class ReadXMLConfiguration {
    private static final String TAG = "ReadXMLConfiguration";
    private ArrayList<Store> stores;
    private String filename;
    private Context context;

    public ReadXMLConfiguration(String filename, Context context) {
        this.stores = new ArrayList<>();
        this.filename = filename;
        this.context = context;

        if(!filename.isEmpty())
            ReadFile();
    }

    private String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    private void ReadFile(){

        try {
            String path = context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();
            File file = new File(path, filename);
            InputStream is = new FileInputStream(file);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("store");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element2 = (Element) node;

                    String name = getValue("name", element2);
                    String ip = getValue("ip", element2);
                    String db = getValue("db", element2);
                    String user = getValue("user", element2);
                    String password = getValue("pass", element2);

                    Store store = new Store(ip, db, user, password, name, "", "");
                    this.stores.add(store);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
}
