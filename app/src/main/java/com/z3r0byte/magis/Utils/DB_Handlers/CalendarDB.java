/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z3r0byte.magis.Utils.DB_Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.ilexiconn.magister.container.Appointment;

import java.util.Arrays;


/**
 * Created by bas on 31-5-16.
 */
public class CalendarDB extends SQLiteOpenHelper {

    private static final String TAG = "CalendarDB";
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDB";

    private static final String TABLE_CALENDAR = "calendar";

    private static final String KEY_ID = "id";
    private static final String KEY_DESC = "description";
    private static final String KEY_CALENDAR_ID = "calendarId";
    private static final String KEY_CLASS_ROOMS = "classrooms";
    private static final String KEY_TEACHER = "teachers";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_FULL_DATE = "fullDate";
    private static final String KEY_PERIOD_FROM = "periodFrom";
    private static final String KEY_PERIOD_TO = "periodTo";
    private static final String KEY_TAKES_ALL_DAY = "takesAllDay";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATE = "state";
    private static final String KEY_FINISHED = "finished";
    private static final String KEY_LINKS = "links";
    private static final String KEY_TYPE = "type";
    private static final String KEY_INFO_TYPE = "infoType";
    private static final String KEY_SUBJECTS = "subjects";
    private static final String KEY_GROUP = "group";

    public CalendarDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CALENDAR_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CALENDAR + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DESC + " TEXT,"
                + KEY_CALENDAR_ID + " INTEGER,"
                + KEY_CLASS_ROOMS + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_END + " TEXT,"
                + KEY_FINISHED + " BOOLEAN,"
                + KEY_FULL_DATE + " TEXT,"
                + KEY_INFO_TYPE + " TEXT,"
                + KEY_LINKS + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_PERIOD_FROM + " INTEGER,"
                + KEY_PERIOD_TO + " INTEGER,"
                + KEY_SUBJECTS + " TEXT,"
                + KEY_START + " TEXT,"
                + KEY_STATE + " TEXT,"
                + KEY_TEACHER + " TEXT,"
                + KEY_TAKES_ALL_DAY + " BOOLEAN,"
                + KEY_TYPE + " TEXT"
                + ")";
        db.execSQL(CREATE_CALENDAR_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);

        // Create tables again
        onCreate(db);
    }

    public void addItems(Appointment[] appointments) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Log.d(TAG, "addItems: amount of items: " + appointments.length);

        for (Appointment item :
                appointments) {
            Integer id = item.id;
            if (!CheckInDB(TABLE_CALENDAR, KEY_CALENDAR_ID, id.toString())) {
                Log.d(TAG, "addItems: item doesnt exist");
                contentValues.put(KEY_CALENDAR_ID, id);
                contentValues.put(KEY_DESC, item.description);
                contentValues.put(KEY_CLASS_ROOMS, Arrays.toString(item.classrooms));
                contentValues.put(KEY_CONTENT, item.content);
                contentValues.put(KEY_END, item.endDateString);
                contentValues.put(KEY_FINISHED, item.finished);
                contentValues.put(KEY_FULL_DATE, item.startDateString.replaceAll("[T:Z.]", ""));
                contentValues.put(KEY_LINKS, Arrays.toString(item.links));
                contentValues.put(KEY_LOCATION, item.location);
                contentValues.put(KEY_PERIOD_FROM, item.periodFrom);
                contentValues.put(KEY_PERIOD_TO, item.periodUpToAndIncluding);
                contentValues.put(KEY_START, item.startDateString);
                contentValues.put(KEY_STATE, item.classState);
                contentValues.put(KEY_SUBJECTS, Arrays.toString(item.subjects));
                contentValues.put(KEY_TEACHER, Arrays.toString(item.teachers));
                contentValues.put(KEY_TAKES_ALL_DAY, item.takesAllDay);


                db.insert(TABLE_CALENDAR, null, contentValues);
            } else {
                Log.d(TAG, "addItems: updating item");
                contentValues.put(KEY_CALENDAR_ID, id);
                contentValues.put(KEY_DESC, item.description);
                contentValues.put(KEY_CLASS_ROOMS, Arrays.toString(item.classrooms));
                contentValues.put(KEY_CONTENT, item.content);
                contentValues.put(KEY_END, item.endDateString);
                contentValues.put(KEY_FINISHED, item.finished);
                contentValues.put(KEY_FULL_DATE, item.startDateString.replaceAll("[T:Z.]", ""));
                contentValues.put(KEY_LINKS, Arrays.toString(item.links));
                contentValues.put(KEY_LOCATION, item.location);
                contentValues.put(KEY_PERIOD_FROM, item.periodFrom);
                contentValues.put(KEY_PERIOD_TO, item.periodUpToAndIncluding);
                contentValues.put(KEY_START, item.startDateString);
                contentValues.put(KEY_STATE, item.classState);
                contentValues.put(KEY_SUBJECTS, Arrays.toString(item.subjects));
                contentValues.put(KEY_TEACHER, Arrays.toString(item.teachers));
                contentValues.put(KEY_TAKES_ALL_DAY, item.takesAllDay);

                db.update(TABLE_CALENDAR, contentValues, KEY_CALENDAR_ID + "=" + id, null);
            }


        }

        db.close();

    }
    
    public Appointment[] getAppointmentsByDate(Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query ="Select * from " + TableName + " where
    }

    public boolean CheckInDB(String TableName, String dbfield, String fieldValue) {

        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
