package com.example.myshoppingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    private static final String CART_NAME = "CART_NAME";
    private static final String CART_COUNT = "CART_COUNT";
    private static final String CART_COST = "CART_COST";
    private static final String CART_ID = "CART_ID";
    private static final String CART_STATE = "CART_STATE";


    private static final String CART_YEAR = "CART_YEAR";
    private static final String CART_MONTH = "CART_MONTH";
    private static final String CART_DAY = "CART_DAY";

    private static final String CART_HOUR = "CART_HOUR";
    private static final String CART_MINUTE = "CART_MINUTE";
    private static final String CART_LIMIT = "CART_LIMIT";


    private static final String ITEM_TABLE = "ITEM_TABLE";
    private static final String ITEM_NAME = "ITEM_NAME";
    private static final String ITEM_COUNT = "ITEM_COUNT";
    private static final String ITEM_COST = "ITEM_COST";
    private static final String ITEM_ID = "ITEM_ID";
    private static final String ITEM_STATE = "ITEM_STATE";
    private static final String ITEM_CART = "ITEM_CART";
    private static final String ITEM_IMAGID = "ITEM_IMAGID";

    private static final String PROFILE_TABLE = "PROFILE_TABLE";
    private static final String PROFILE_SPENT = "PROFILE_SPENT";
    private static final String PROFILE_SAVED = "PROFILE_SAVED";
    private static final String PROFILE_CARTS = "PROFILE_CARTS";
    private static final String PROFILE_ITEMS = "PROFILE_ITEMS";

    private static final String IMAGE_TABLE = "IMAGE_TABLE";
    private static final String IMAGE_NAME = "IMAGE_NAME";
    private static final String IMAGE_BITMAP = "IMAGE_BITMAP";
    private static final String IMAGE_ID = "IMAGE_ID";
    private ByteArrayOutputStream objetByteArrayOutputStream;
    private byte[] imageInBytes;

    public DataBase(@Nullable Context context) {
        super(context, "carts.db", null, 1);
        //this.context = context
    }

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DataBase(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    //called the first time when trying to access DB object
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " ( " + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CART_NAME + " TEXT, " + CART_COUNT + " INT, " + CART_COST + " DOUBLE, " + CART_STATE + " INT, " + CART_LIMIT + " DOUBLE, " + CART_YEAR + " INT, "
                + CART_MONTH + " INT, " + CART_DAY + " INT, " + CART_HOUR + " INT, " + CART_MINUTE + " INT  )";

        String createTableItems = "CREATE TABLE " + ITEM_TABLE + " ( " + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME + " TEXT, " + ITEM_COUNT + " INT, " + ITEM_COST + " DOUBLE, " + ITEM_STATE + " INT, " + ITEM_CART + " INT, " + IMAGE_BITMAP + " BLOB )";
        String createTableProfile = "CREATE TABLE " + PROFILE_TABLE + " ( " + PROFILE_SPENT + " DOUBLE, " + PROFILE_SAVED + " DOUBLE, " + PROFILE_CARTS + " INT, " + PROFILE_ITEMS + " INT )";

        sqLiteDatabase.execSQL(createTableStatement);
        sqLiteDatabase.execSQL(createTableProfile);
        sqLiteDatabase.execSQL(createTableItems);
    }

    //when version number of db changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void CreateProfile(){ //cia kad sukurti pirma kart profilio eilute
        SQLiteDatabase base = this.getWritableDatabase();    //'"+ item.getName() +"'"
        ContentValues cv = new ContentValues();

        cv.put(PROFILE_SPENT,0); //These Fields should be your String values of actual column names
        cv.put(PROFILE_SAVED, 0 );
        cv.put(PROFILE_CARTS, 0 );
        cv.put(PROFILE_ITEMS, 0);
        //base.update(PROFILE_TABLE, cv ,null, null);
        base.insert(PROFILE_TABLE,null, cv);
    }

    public boolean addCart(NewCart cart) { //pridedu nauja cart i sistema
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CART_NAME, cart.getName());
        cv.put(CART_COUNT, cart.getCount());
        cv.put(CART_COST, cart.getCost());
        cv.put(CART_STATE, cart.getState());
        cv.put(CART_LIMIT, cart.getLimit());
        cv.put(CART_YEAR, cart.getYear());
        cv.put(CART_MONTH, cart.getMonth());
        cv.put(CART_DAY, cart.getDay());
        cv.put(CART_HOUR, cart.getHour());
        cv.put(CART_MINUTE, cart.getMinute());

        int temp = getProfileCount();
        if(temp == 0){
            CreateProfile();
        }

        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else
            return true;
    }
    public NewCart getCart(int cartID){
        NewCart temp = new NewCart();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_ID + " = " + cartID + "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {
                temp.setID(cursor.getInt(0));
                temp.setCount(cursor.getInt(2));
                temp.setPrice(cursor.getInt(3));
                temp.setLimit(cursor.getInt(5));
            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        return temp;
    }
    public int updateProfile(int id){
        NewCart cart = new NewCart();
        cart = getCart(id);

        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        double spent = cart.getCost();
        double saved = cart.getLimit() - spent;
        if (saved < 0){
            saved = 0; //nes vistiek nieko nesutaupe, tik isleido pinigu
        }
        int items = cart.getCount();

        //tuomet atlieka updatinima informacijos
        Profile old = getProfileInfo();

        spent += old.getSpent();
        saved += old.getSaved();
        items += old.getItems();
        int carts = old.getCarts() + 1;

        cv.put(PROFILE_SPENT,spent);
        cv.put(PROFILE_SAVED, saved );
        cv.put(PROFILE_CARTS, carts );
        cv.put(PROFILE_ITEMS, items);
        base.update(PROFILE_TABLE, cv ,null, null);

        return 1;
    }

    public void ClearProfile(){
        SQLiteDatabase base = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        double vienas, du;
        int trys, keturi;
        vienas = 0; du = 0; trys = 0; keturi = 0;
        cv.put(PROFILE_SPENT,vienas);
        cv.put(PROFILE_SAVED, du);
        cv.put(PROFILE_CARTS, trys);
        cv.put(PROFILE_ITEMS, keturi);
        base.update(PROFILE_TABLE, cv ,null, null);
    }

    public Profile getProfileInfo(){
        Profile temp = new Profile();
        String queryString = "SELECT * FROM " + PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {
                double spent = cursor.getDouble(0);
                double saved = cursor.getDouble(1);
                int carts = cursor.getInt(2);
                int items = cursor.getInt(3);
                temp.setSpent(spent);
                temp.setSaved(saved);
                temp.setCarts(carts);
                temp.setItems(items);
//                Log.e("Spent", String.valueOf(spent));
//                Log.e("Saved", String.valueOf(saved));
//                Log.e("items", String.valueOf(carts));
//                Log.e("Carts", String.valueOf(items));

            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        return temp;
    }
    public boolean changeItemState(NewItem item, int cartId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(item.getState() == 1){
            item.setState(0);
        }
        else{
            item.setState(1);
        }
        String query = "UPDATE " + ITEM_TABLE + " SET " + ITEM_STATE + " = '" + item.getState() + "' WHERE " + ITEM_CART + " = " + cartId + " AND " + ITEM_NAME + " = '" + item.getName() + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return false; //ERROR
        } else {
            return true; //SUCCESS
        }
    }
    public int getCartState(int cartID){
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_ID + " = " + cartID + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set
        int state = 0;
        if (cursor.moveToFirst()) {
            do {
                state = cursor.getInt(4);
            } while (cursor.moveToNext());
        } else {
            //failed. don't add anything to list
        }
        return state;
    }

    public int changeCartState(int state, int cartID){
        SQLiteDatabase base = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMER_TABLE + " SET " + CART_STATE + " = '" + state + "' WHERE " + CART_ID + " = '" + cartID + "'";
        Cursor cursor = base.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return state;
        } else {
            return state;
        }
    }

    public boolean addItem(NewItem item, int cartid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEM_NAME, item.getName());
        cv.put(ITEM_COUNT, item.getCount());
        cv.put(ITEM_COST, item.getCost());
        cv.put(ITEM_STATE, item.getState());
        cv.put(ITEM_CART, cartid);
        try{
            Bitmap imageToStoreBitmap = item.getImage();
            objetByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objetByteArrayOutputStream);
            imageInBytes = objetByteArrayOutputStream.toByteArray(); //convert to byte array
            cv.put(IMAGE_BITMAP, imageInBytes);
        }
        catch (Exception e){
            db.close();
            return false;
        }

        if (searchItem(cartid, item.getName()) == 0) {//check if exists
            long insert = db.insert(ITEM_TABLE, null, cv);
            if (insert == -1) {
                db.close();
                return false;
            } else {
                db.close();
                updateCartOnAddedNewItem(item, cartid);
                return true;
            }
        } else {
            db.close();
            return false;
        }
    }

    //cant have two items with the same name
    public int searchItem(int cartid, String name) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT * FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cartid + " AND " + ITEM_NAME + " = '" + name + "'";

        Cursor cursor = base.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return 1; //ERROR
        } else {
            return 0; //SUCCESS
        }
    }

    public boolean updateItem(NewItem item, int cartid, String item_name) {
        //search for id in db and if found then delete it
        SQLiteDatabase base = this.getWritableDatabase();    //'"+ item.getName() +"'"

        try{
            Bitmap imageToStoreBitmap = item.getImage();
            objetByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objetByteArrayOutputStream);
            imageInBytes = objetByteArrayOutputStream.toByteArray(); //convert to byte array
            //cv.put(IMAGE_BITMAP, imageInBytes);
        }
        catch (Exception e){
            base.close();
            return false;
        }
        ContentValues cv = new ContentValues();
        try{
            cv.put(ITEM_NAME,item.getName()); //These Fields should be your String values of actual column names
            cv.put(ITEM_COUNT, item.getCount() );
            cv.put(ITEM_COST, item.getCost() );
            cv.put(IMAGE_BITMAP, imageInBytes);
            base.update(ITEM_TABLE, cv ,ITEM_CART + " = " + cartid + " AND " + ITEM_NAME + " = '" + item_name + "'", null); //ITEM_CART + " = " + cartid + " AND " + ITEM_NAME + " = " + item_name
        }
        catch (Exception e){
            base.close();
            return false;
        }

        refreshCartInfo(cartid);
        return true;
    }

    public int loadCartData(NewCart temp) {
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_NAME + " = '" + temp.getName() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {

                temp.setID(cursor.getInt(0));
                temp.setName(cursor.getString(1));
                temp.setLimit(cursor.getDouble(5));
                temp.setYear(cursor.getInt(6));
                temp.setMonth(cursor.getInt(7));
                temp.setDay(cursor.getInt(8));
                temp.setHour(cursor.getInt(9));
                temp.setMinute(cursor.getInt(10));
            } while (cursor.moveToNext());
        } else {
            //failed. don't add anything to list
        }
        cursor.close();
        db.close();
        return 0;//on success
    }

    public boolean updateCart(NewCart item, String item_name) {
        SQLiteDatabase base = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMER_TABLE + " SET " + CART_NAME + " = '" + item.getName() + "' , " + CART_LIMIT + " = " + item.getLimit() + " , " + CART_YEAR + " = " + item.getYear() + " , "
                + CART_MONTH + " = " + item.getMonth() + " , " + CART_DAY + " = " + item.getDay() + " , "
                + CART_HOUR + " = " + item.getHour() + " , " + CART_MINUTE + " = " + item.getMinute() + " WHERE " + CART_NAME + " = '" + item_name + "'";
        if (item_name.equals(item.getName())) {
            Cursor cursor = base.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                base.close();
                return false;
            } else {
                base.close();
                return true;
            }
        } else {
            if (searchCarts(item.getName()) == 0) {
                Cursor cursor = base.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    base.close();
                    return false;
                } else {
                    base.close();
                    return true;
                }
            } else return false;
        }
    }

    public int searchCarts(String name) {
        SQLiteDatabase base = this.getReadableDatabase();
        String query = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_NAME + " = '" + name + "'";

        Cursor cursor = base.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return 1; //ERROR
        } else {
            return 0; //SUCCESS
        }
    }

    public int getProfileCount(){ //tikrinu ar nera sukurtos profilio eilutes
        int temp = 0;
        String queryString = "SELECT * FROM " + PROFILE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set
        if (cursor.moveToFirst()) {
            temp++;
        }

        return temp;
    }

    public List<NewCart> getAllCarts() {
        List<NewCart> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {
                int cartID = cursor.getInt(0);
                String cartName = cursor.getString(1);
                int cartCount = cursor.getInt(2);
                double cartCost = cursor.getDouble(3);
                int cartState = cursor.getInt(4);

                NewCart newCart = new NewCart(cartID, cartName, cartCount, cartCost, cartState);
                returnList.add(newCart);

            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public void ClearDB(){
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int cartID = cursor.getInt(0);

                deleteCartItems(cartID);
                NewCart temp = new NewCart();
                temp.setID(cartID);
                deleteCart(temp);

            } while (cursor.moveToNext());
        } else {
        }
        cursor.close();
        db.close();
    }

    public double getAllCartsCost() {
        List<NewCart> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        double Cost = 0;
        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {
                Cost += cursor.getDouble(3);
            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        cursor.close();
        db.close();

        return Cost;
    }

    public List<NewCart> getAllCarts(String name) {
        List<NewCart> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_NAME + " LIKE  '" + name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int cartID = cursor.getInt(0);
                String cartName = cursor.getString(1);
                int cartCount = cursor.getInt(2);
                double cartCost = cursor.getDouble(3);
                int cartState = cursor.getInt(4);

                NewCart newCart = new NewCart(cartID, cartName, cartCount, cartCost, cartState);
                returnList.add(newCart);

            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<NewItem> getAllItems(int cartid) {
        List<NewItem> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cartid;
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(queryString, null); //cursor is like result set

        if (cursor.moveToFirst()) {
            do {
                int cartID = cursor.getInt(0);
                String cartName = cursor.getString(1);
                int cartCount = cursor.getInt(2);
                double cartCost = cursor.getDouble(3);
                int cartState = cursor.getInt(4);
                int cartItem = cursor.getInt(5);
                byte[] image = cursor.getBlob(6);
                if(image.length < 1){
                    Log.e("Bitai", "Negerai nuskaitant image bytes");
                }
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);


                NewItem newItem = new NewItem(cartName, cartCount, cartCost, cartID, cartState, objectBitmap);
                returnList.add(newItem);

            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<NewItem> getSelectedItems(int cartid, String name) {
        List<NewItem> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cartid + " AND " + ITEM_NAME + " LIKE '" + name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int cartID = cursor.getInt(0);
                String cartName = cursor.getString(1);
                int cartCount = cursor.getInt(2);
                double cartCost = cursor.getDouble(3);
                int cartState = cursor.getInt(4);
                byte[] image = cursor.getBlob(6);
                if(image.length < 1){
                    Log.e("Bits", "Error reading images bits");
                }
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                NewItem newItem = new NewItem(cartName, cartCount, cartCost, cartID, cartState, objectBitmap);
                returnList.add(newItem);

            } while (cursor.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteCart(NewCart cart) {
        //search for id in db and if found the delete it
        deleteCartItems(cart.getID());

        SQLiteDatabase base = this.getWritableDatabase();
        String query = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + CART_ID + " = " + cart.getID();

        Cursor cursor = base.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        } else return false;
    }

    public boolean deleteCartItems(int cartId) {
        Log.i("CartID", String.valueOf(cartId));
        SQLiteDatabase deletedb = this.getWritableDatabase();
        deletedb.execSQL("DELETE FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cartId);
        deletedb.close();

        return true;
    }

    public boolean deleteItem(int cart, NewItem item) {
        //search for id in db and if found the delete it
        SQLiteDatabase base = this.getWritableDatabase();
        String query = "DELETE FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cart + " AND " + ITEM_NAME + " = '" + item.getName() + "'";

        Cursor cursor = base.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            refreshCartInfo(cart);
            return true;
        } else {
            refreshCartInfo(cart);
            return false;
        }
    }

    public boolean updateCartOnAddedNewItem(NewItem item, int cartID) {
        SQLiteDatabase baseRead = this.getReadableDatabase();
        int cartCount = 0;
        double cartCost = 0;

        String queryRead = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_ID + " = " + cartID;
        Cursor first = baseRead.rawQuery(queryRead, null);
        if (first.moveToFirst()) {
            cartCount = first.getInt(2); // laukas geras
            cartCost = first.getDouble(3); //laukas geras
        } else {
            Log.i("Error", "Could not take cart information \n");
            //failed. dont add anything to list
        }
        first.close();
        baseRead.close();

        cartCount += item.getCount();
        cartCost += item.getCost();
        SQLiteDatabase baseWrite = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMER_TABLE + " SET " + CART_COUNT + " = " + cartCount + " , " + CART_COST + " = " + cartCost + " WHERE " + CART_ID + " = " + cartID;
        Cursor cursor = baseWrite.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        } else
            return false;
    }

    public boolean updateCartOnRemovedNewItem(NewItem item, int cartID) {
        SQLiteDatabase baseWrite = this.getWritableDatabase();
        SQLiteDatabase baseRead = this.getReadableDatabase();
        int cartCount = 0;
        double cartCost = 0;

        String queryRead = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CART_ID + " = " + cartID;
        Cursor first = baseRead.rawQuery(queryRead, null);
        if (first.moveToFirst()) {
            cartCount = first.getInt(2);
            cartCost = first.getDouble(3);
        } else {
            //failed. dont add anything to list
        }
        first.close();

        cartCount -= 1;
        cartCost -= item.getCost();

        String query = "UPDATE " + CUSTOMER_TABLE + " SET " + CART_COUNT + " = " + cartCount + " , " + CART_COST + " = " + cartCost + " WHERE " + CART_ID + " = " + cartID;
        Cursor cursor = baseWrite.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        } else return false;
    }

    public boolean refreshCartInfo(int cid) {
        SQLiteDatabase baseWrite = this.getWritableDatabase();
        SQLiteDatabase baseRead = this.getReadableDatabase();
        int cartCount = 0;
        double cartCost = 0;

        String queryRead = "SELECT * FROM " + ITEM_TABLE + " WHERE " + ITEM_CART + " = " + cid;
        Cursor first = baseRead.rawQuery(queryRead, null);
        if (first.moveToFirst()) {
            do {
                cartCount += first.getInt(2);
                cartCost += first.getDouble(3);
            } while (first.moveToNext());
        } else {
            //failed. dont add anything to list
        }
        first.close();

        String query = "UPDATE " + CUSTOMER_TABLE + " SET " + CART_COUNT + " = " + cartCount + " , " + CART_COST + " = " + cartCost + " WHERE " + CART_ID + " = " + cid;
        Cursor cursor = baseWrite.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
}
