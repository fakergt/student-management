package com.example.studentmanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.studentmanagement.DBHelper;
import com.example.studentmanagement.List49;
import com.example.studentmanagement.R;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity {
    String DBName = "CatPrSalesSt";

    public DecimalFormat df = new DecimalFormat("#.##");
    SQLiteDatabase db;
    TextView tv;
    DBHelper dbHelper;
    String[] tabNames = new String[20];
    int numberOfTables;
    ArrayList<String> rows;

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> masa = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();
    ArrayList<Integer> cat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }

    public void createDB(View v) {
        dbHelper = new DBHelper(this, DBName);
        db = dbHelper.getWritableDatabase();
        Log.d("Create DB=", "The DB " + DBName + "  was created OR Opened the exiting one!");
        //db.execSQL("DROP TABLE IF EXISTS detbord");
        //db.execSQL("DROP TABLE IF EXISTS bord");
    }

    public String[] TableInfo(String fn) {
        String tab[] = new String[201];
        String tabN = fn;
        rows = new ArrayList<String>();

        try {
            //File myFile = new File("/storage/extSdCard/"+tabN+".txt");
            File myFile = new File("/mnt/sdcard/" + tabN + ".txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            int i = 0;
            while ((tab[i] = myReader.readLine()) != null) {
                rows.add(tab[i]);
                i++;
                if (i > 200) {
                    Log.d("TABLE", " The count od rows in the TXT file must be less than 200!");
                    break;
                }
                //aBuffer += aDataRow + "\n";
            }
//tv.setText(aBuffer);
            myReader.close();
            Log.d("AAAA", "Rows in the file=" + rows.size());
        } catch (Exception e) {
//Toast.makeText(getBaseContext(), e.getMessage(),
            //Toast.LENGTH_SHORT).show();
            Log.d("ERROR", "Eloare la citire TablesM");
        }

        return tab;
    }

    public void createTAB(View v) {
        String aDataRow = "";
        String aBuffer = "";
        String tt;
        int nf;
        String[] fieldsN = new String[10];
        String[] fieldsT = new String[10];
        //String [] tabNames=new String[10];
        // Read the list of tables
        String tn[] = new String[100];
        // nt - the list of tables
        tn[0] = "ProductsM";
        //int nt=tn.length;
        int nt = 1;
        numberOfTables = nt;// nt - the count of tables

        Log.d("", "N=" + String.valueOf(nt) + "  Nume tabel=" + tn[0]);

        //for(int i=0;i<nt;i++) tabNames[i]=tn[i];
        tabNames[0] = "ProductsM";
        for (int i = 0; i < nt; i++) {
            boolean te = exists(tn[i]);
            if (!te) {
                String[] tfS = TableInfo(tn[i] + "s");
                String[] tfC = TableInfo(tn[i]);
                nf = 0;
                while (tfS[nf] != null) nf++;
                //nf=tfS.length;
                Log.d("nf1=", "" + nf);
                for (int j = 0; j < nf; j++) {
                    String[] fields = tfS[j].split("\t");
                    fieldsN[j] = fields[0];
                    fieldsT[j] = fields[1];
                }
                Log.d("Tabelul : ", tn[i]);
                Log.d("Fields", fieldsN[0] + " , " + fieldsT[0] + " , " + fieldsN[1] + " , " + fieldsT[1]);

                // create table i

                boolean tableExists = false;

                Log.d("before try Table:", tn[i] + "   to create???   ");
                try {
                    // creating a table
                    dbHelper.createT(db, tn[i], fieldsN, fieldsT, nf);
                    tableExists = true;

                    Log.d("Table:", "The  " + tn[i] + "   was created   ");
                } catch (Exception e) {
                    // /* fail */
                    Log.d("Table:", "The table " + tn[i] + "  was existing, and was not created again   ");
                }
            }
        }
    }

    public boolean exists(String table) {
        Cursor c = null;
        boolean tableExists = false;
        /* get cursor on it */
        try {
            c = db.query(table, null, null, null, null, null, null);
            tableExists = true;
            Log.d("About existing ", "The table " + table + "  exists! :))))");
        } catch (Exception e) {
            /* fail */
            Log.d("The table is missing", table + " doesn't exist :(((");
        }
        return tableExists;
    }

    public void fillTAB(View v) {
        String tabN;
        for (int i = 0; i < numberOfTables; i++) {
            tabN = tabNames[i];
            boolean te = exists(tabN);

            Log.d("Before if", tabN + "+  nrtab=" + numberOfTables);

            if (te) {  //if the tb exists then fill it
                Log.d("Inside  if", "The table:  " + tabN + "   Exists");
                //the table exests:  	//clear the table
                db.delete(tabN, null, null);
                Log.d("After delete", tabN);
                //fill the table
                String tt, tabContent;
                int nf;
                String[] tfC = TableInfo(tabN);
                //int nr=tfC.length;
                String[] fieldsN = tfC[0].split("\t");
                String[] fieldsT = tfC[1].split("\t");
                Log.d("After table content", tabN);
                nf = fieldsN.length;
                // insert rows
                ContentValues cv = new ContentValues();
                int sw;
                double nnf;
                //for (int j=2;j<nr;j++) //on rows nr=tfC.length;
                int nrow = 0;
                while (tfC[nrow] != null) nrow++;
                Log.d("Number od rows=", "nrow=" + nrow);
                int j = 2;

                while (tfC[j] != null) {
                    cv.clear();
                    String[] rcd = tfC[j].toString().split("\t");
                    for (int k = 0; k < nf; k++)// on fields nf=fieldsN.length;
                    {
                        sw = Integer.valueOf(fieldsT[k]);
                        switch (sw) {
                            case 1:
                                cv.put(fieldsN[k], Integer.valueOf(rcd[k].toString()));
                                break;
                            case 2:
                                cv.put(fieldsN[k], rcd[k].toString());
                                break;
                            default:
                                break;
                        }
                    }// end of fields
                    db.insert(tabN, null, cv);
                    j++;
                }  // end of rows

                // show the table content
                Log.d("Datele in tabel", "------" + tabN);
                Cursor cc = null;
                cc = db.query(tabN, null, null, null, null, null, null);
                logCursor1(cc);
                cc.close();
                Log.d("Datele in tabel", "--- ---");
            }// end of  if(te) (if the tb exists then fill it
        }//  end of for(int i=0;i<numberOfTables;i++)
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick_List(View v) {
        Intent intent = new Intent(this, List49.class);
        intent.putIntegerArrayListExtra("ids", ids);
        intent.putStringArrayListExtra("names", names);
        intent.putStringArrayListExtra("masa", masa);
        intent.putIntegerArrayListExtra("values", values);
        intent.putIntegerArrayListExtra("cat", cat);
        startActivity(intent);
    }

    // afisare in LOG din Cursor
    public void logCursor1(Cursor c) {
        Log.d("COLUMNS NR=", "nc=" + c.getColumnCount());

        tv.setText("");
        String str2 = "Results For the Fields : ";
        String cnn = "", coln = "";

        if (c != null) {
            if (c.moveToFirst()) {
                String str, str1, name;

                int klu = 0, rr = 0, nc, nr = 0;
                do {
                    nc = 0;
                    str = "";
                    name = "";
                    // for the next records
                    for (String cn : c.getColumnNames()) {
                        if (rr > -1) {
                            str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");  //row concatenated
                            cnn = cnn.concat(cn + " ; ");
                        }
                        nc++;
                        rr++;
                        if (nc == 1) {
                            ids.add(c.getInt(c.getColumnIndex(cn)));
                        }
                        if (nc == 2) {
                            names.add(c.getString(c.getColumnIndex(cn)));
                            nr++;
                        }
                        if (nc == 3) {
                            masa.add(c.getString(c.getColumnIndex(cn)));
                        }
                        if (nc == 4) {
                            values.add(c.getInt(c.getColumnIndex(cn)));
                        }
                        if (nc == 5) {
                            cat.add(c.getInt(c.getColumnIndex(cn)));
                        }

                        Log.d("COLUMNS NR=", "nc=" + nc + ", rr=" + rr + ", nr=" + nr);
                    }
                    if (klu == 0) {
                        str2 = str2.concat(cnn);
                        tv.setText(cnn + "\n");
                    }
                    klu++;

                    str1 = tv.getText() + str + "\n";
                    Log.d(" Rindul = ", str);
                    tv.setText(str1);

                } while (c.moveToNext());
            }
            // txtData.setText(str2 + " with WHERE Conditions : " + "\n");
        } else
            Log.d("Rindul", "Cursor is null");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.add("UpDate");
        menu.add("IneqGraphSol");
        menu.add("MovRotate");
        menu.add("Grid");
        menu.add("GraphFun");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getTitle() == "Grid") {

        }
        return super.onOptionsItemSelected(item);
    }
}





