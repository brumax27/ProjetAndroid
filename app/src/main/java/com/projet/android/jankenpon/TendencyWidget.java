package com.projet.android.jankenpon;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Implementation of App Widget functionality.
 */
public class TendencyWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tendency_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        fetchTendencies(new TendencyDataProvider() {
            @Override
            public void onFetch(User user) {
                views.setTextViewText(R.id.victory_ratio, user.victoryRatio() + " %");
                views.setTextViewText(R.id.paper_ratio, user.paperRatio() + " %");
                views.setTextViewText(R.id.rock_ratio, user.rockRatio() + " %");
                views.setTextViewText(R.id.scissors_ratio, user.scissorsRatio() + " %");
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void fetchTendencies(final TendencyDataProvider tendencyDataProvider) {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        // TODO : Replace "1" by current player id
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("users").child("1");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tendencyDataProvider.onFetch(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mFirebaseDatabase.addValueEventListener(userListener);
    }

    private interface TendencyDataProvider {
        void onFetch(User user);
    }
}

