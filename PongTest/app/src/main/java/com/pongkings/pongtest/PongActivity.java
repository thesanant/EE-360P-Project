package com.pongkings.pongtest;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;


public class PongActivity extends AppCompatActivity {
    public GamePanel gp;

    public static void setClient(Client client) {
        PongActivity.client = client;
    }

    public static Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gp = new GamePanel(this, getWindowManager());

        new ClientAsync().execute(gp);

        setContentView(gp);

    }

    public int getDeviceWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    protected void onPause() {
        super.onPause();
        gp.setClientRunning(false);
        try {
            client.getSocket().close();
        }catch(IOException e){}
    }

    @Override
    protected void onResume(){
        super.onResume();
        new ClientAsync().execute(gp);
    }

    /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */
}