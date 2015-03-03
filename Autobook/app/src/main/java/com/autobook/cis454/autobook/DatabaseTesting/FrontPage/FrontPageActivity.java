package com.autobook.cis454.autobook.DatabaseTesting.FrontPage;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.autobook.cis454.autobook.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FrontPageActivity extends ActionBarActivity {

    public static final String DATABASE_NAME = "AutoBookDatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FrontpageFragment())
                    .commit();
        }

        //this try-catch block checks if there is a database present, if there is not a database
        //then an exception is thrown and the stacktrace is printed
//        try{
//            String destPath = "data/data" + getPackageName() + "/databases/TestDatabase";
//            File f = new File(destPath);
//            if(!f.exists()){
//                CopyDB(getBaseContext().getAssets().open("mydb"),
//                        new FileOutputStream(destPath));
//
//            }
//        } catch(FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }

    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer))> 0){
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }




}