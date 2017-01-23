package com.intellisysla.rmsscanner.BLL;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.intellisysla.rmsscanner.DAL.Category;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Input;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.UI.View.Activity.SearchActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alienware on 12/9/2016.
 */

public class ExecuteQuery {

    private static final String TAG = "ExecuteQuery";
    private Store store;
    private Context context;


    public ExecuteQuery(Store store) {
        this.store = store;
        this.context = null;
    }

    public ExecuteQuery(Store store, Context context) {
        this.store = store;
        this.context = context;
    }

    public Item getItem(String code)
    {
        Item item = null;

        String query = "" +
                "select i.ID,\n" +
                "i.ItemLookupCode,\n" +
                "i.[Description],\n" +
                "i.DepartmentID,\n" +
                "d.[Name] as DepartmentName,\n" +
                "i.CategoryID,\n" +
                "c.[Name] as CategoryName,\n" +
                "i.Price,\n" +
                "i.SalePrice Offer,\n" +
                "i.SaleStartDate,\n" +
                "i.SaleEndDate,  \n" +
                "isnull(i.quantity,0) Quantity,\n" +
                "isnull(i.fisico,0) FisicQuantity,\n" +
                "i.SaleType\n" +
                "from dbo.Item i\n" +
                "left join dbo.alias a on a.itemid = i.ID\n" +
                "left join dbo.Department d on d.ID = i.DepartmentId\n" +
                "left join dbo.Category c on c.ID = i.CategoryId\n" +
                "where i.ItemLookupCode = '"+ code +"'\n" +
                "or a.alias = '"+code+"'";

        try {

            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {

                item = new Item(
                        rs.getInt("ID"),
                        rs.getString("ItemLookupCode"),
                        rs.getString("Description"),
                        rs.getInt("DepartmentId"),
                        rs.getString("DepartmentName"),
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName"),
                        rs.getInt("Quantity"),
                        rs.getInt("FisicQuantity"),
                        rs.getFloat("Price"),
                        rs.getFloat("Offer"),
                        rs.getDate("SaleStartDate"),
                        rs.getDate("SaleEndDate"),
                        rs.getBoolean("SaleType"));
            }
            rs.close();
            statement.close();
            connection.close();

        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return item;
    }

    public ArrayList<Category> getCategories(int department_id){

        ArrayList<Category> categories = new ArrayList<>();

        String query = "" +
                "select c.ID, \n" +
                "       c.[Name]\n" +
                "  from dbo.Category c\n" +
                " where c.DepartmentID = '"+ department_id +"'\n"+
                " order by c.[Name]";
        try {

            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rd = statement.executeQuery(query);

            while (rd.next()) {
                categories.add(new Category(rd.getInt("ID"), rd.getString("Name")));
            }

            rd.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

       return categories;
    }

    public ArrayList<Department> getDepartments(){

        ArrayList<Department> departments = new ArrayList<>();

        String query = "" +
                "select [ID]\n" +
                "      ,[Name]\n" +
                "  from [dbo].[Department]" +
                "  order by [Name] ";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rd = statement.executeQuery(query);

            while (rd.next()) {
                departments.add(new Department(rd.getInt("ID"), rd.getString("Name")));
            }

            rd.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return departments;
    }

    public int updateDepartment(int id, int departmentId){

        int result = 0;
        String query = "update item set departmentid='"+departmentId+"' where id= '"+id+"' ";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public int updateOffer(int id, float offer, String begin, String end){

        int result = 0;
        String query = "UPDATE Item SET SalePrice = '"+offer+"', SaleStartDate =  '"+begin+"', SaleEndDate = '"+end+"', SaleType = 1 WHERE ID ='"+ id +"' ";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public int removeOffer(int id){

        int result = 0;
        String query = "UPDATE Item SET  SaleType = 0 WHERE ID ='"+ id +"' ";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public int insertAlias(int id, String text){

        int result = 0;
        String query = "INSERT INTO [Alias]([ItemID],[Alias]) VALUES('"+id+"','"+text+"')";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public int searchAlias(int id, String alias){

        int isFound = 0;

        String query = "SELECT [ID]\n" +
                "      ,[ItemID]\n" +
                "      ,[Alias]\n" +
                "  FROM [dbo].[Alias] a\n" +
                "  where a.Alias = '"+alias+"'";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rd = statement.executeQuery(query);

            if (rd.next()) {
                isFound = 1;
            }

            rd.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            isFound = 0;
            Log.e(TAG, e.getMessage());
        }

        return isFound;
    }

    public Boolean login(String user, String pass){

        boolean isLogin = false;

        String query =
                "select Name\n" +
                "from dbo.cashier\n" +
                "where number='"+user+"'\n" +
                "and FloorLimit= '"+ pass +"' ";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rd = statement.executeQuery(query);

            if (rd.next()) {
                isLogin = true;
            }

            rd.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            isLogin = false;
            Log.e(TAG, e.getMessage());
        }

        return isLogin;
    }

    public int updateQuantity(String itemLookupCode, int quantity){

        int result = 0;
        String query = "update item set fisico = (fisico + "+ quantity +") where ItemLookupCode='"+ itemLookupCode +"'";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public int insertInput(String itemLookupCode, String location, int quantity, String name){

        int result = 0;
        String query = "INSERT INTO Ingresos(PLU,cantidad,ElaboradoPor,BinLocation,[Fecha],Tipo)VALUES('"+itemLookupCode+"','"+quantity+"','"+name+"','"+location+"',getdate(), '0')";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();

            result = statement.executeUpdate(query);

            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return result;
    }




    public ArrayList<Input> getInputs(String itemcode){

        ArrayList<Input> inputs = new ArrayList<>();

        String query = "SELECT ElaboradoPor Name,Cantidad Quantity,Fecha date, BinLocation as Location FROM dbo.Ingresos WHERE PLU = '" + itemcode +"' ORDER BY Fecha DESC";

        try {
            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rd = statement.executeQuery(query);

            while (rd.next()) {
                inputs.add(new Input(rd.getString("Name"), rd.getInt("Quantity"), rd.getString("Location"), rd.getTimestamp("date")));
            }

            rd.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return inputs;
    }

    public ArrayList<Item> getItems(String word) {
        ArrayList<Item> items = new ArrayList<>();

        String query = "" +
                "select top 10 i.ID,\n" +
                "i.ItemLookupCode,\n" +
                "i.[Description],\n" +
                "i.DepartmentID,\n" +
                "d.[Name] as DepartmentName,\n" +
                "i.CategoryID,\n" +
                "c.[Name] as CategoryName,\n" +
                "i.Price,\n" +
                "i.SalePrice Offer,\n" +
                "i.SaleStartDate,\n" +
                "i.SaleEndDate,  \n" +
                "isnull(i.quantity,0) Quantity,\n" +
                "isnull(i.fisico,0) FisicQuantity,\n" +
                "i.SaleType\n" +
                "from dbo.Item i\n" +
                "left join dbo.alias a on a.itemid = i.ID\n" +
                "left join dbo.Department d on d.ID = i.DepartmentId\n" +
                "left join dbo.Category c on c.ID = i.CategoryId\n" +
                "where i.ItemLookupCode like '%"+ word +"%'\n" +
                "or a.alias like '%"+word+"%'\n" +
                "or d.[Name] like '%"+word+"%'";

        try {

            Connection connection = ConnectionClass.CONN(store.getIp(), store.getDb(), store.getUser(), store.getPassword());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                Item item = new Item(
                        rs.getInt("ID"),
                        rs.getString("ItemLookupCode"),
                        rs.getString("Description"),
                        rs.getInt("DepartmentId"),
                        rs.getString("DepartmentName"),
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName"),
                        rs.getInt("Quantity"),
                        rs.getInt("FisicQuantity"),
                        rs.getFloat("Price"),
                        rs.getFloat("Offer"),
                        rs.getDate("SaleStartDate"),
                        rs.getDate("SaleEndDate"),
                        rs.getBoolean("SaleType"));

                items.add(item);
            }
            rs.close();
            statement.close();
            connection.close();

        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        return items;
    }
}
