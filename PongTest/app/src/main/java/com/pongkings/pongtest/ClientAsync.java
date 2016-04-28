package com.pongkings.pongtest;

import android.os.AsyncTask;

/**
 * Created by Antonio San Pedro on 4/23/2016.
 */
class ClientAsync extends AsyncTask<GamePanel, Client, Void> {

    private Exception exception;

    protected Void doInBackground(GamePanel... gps) {
        try {
            while(gps[0] == null){}
            Client client = new Client(gps[0]);
        } catch(Exception e){ e.printStackTrace(); }
        return null;
    }

    protected void onPostExecute(Client client) {
        PongActivity.setClient(client);
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}