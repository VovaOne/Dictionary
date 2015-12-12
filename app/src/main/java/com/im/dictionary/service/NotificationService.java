package com.im.dictionary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.im.dictionary.storage.db.DB;
import com.im.dictionary.model.Card;
import com.im.dictionary.util.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    public static final String SERVICE_POWER_MODE_SHARED_PREFERENCE_PROPERTY = "com.im.dictionary.service.power.mode.shared.preference.property";

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();

        mTimer.schedule(mMyTimerTask, 10_000 /*10c*/, 60_000 /*60c*/);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd:MMMM:yyyy HH:mm:ss a", Locale.getDefault());
            final String strDate = simpleDateFormat.format(calendar.getTime());

            Card randCard = getRandomCard();
            if (randCard == null) return;
            Notification.showNotification(randCard.word, randCard.translate);
        }


        public Card getRandomCard() {
            List<Card> cards = DB.CARD_DAO_INSTANCE.getAllCards();
            if (cards.isEmpty()) return null;
            int randMin = 0;
            int randMax = cards.size()-1;

            Random r = new Random();
            int rand = r.nextInt(randMax - randMin + 1) + randMin;

            return cards.get(rand);
        }
    }
}
