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

import com.google.gson.Gson;
import com.z3r0byte.magis.Utils.DateUtils;

import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.sub.Classroom;
import net.ilexiconn.magister.container.sub.Link;
import net.ilexiconn.magister.container.sub.SubSubject;
import net.ilexiconn.magister.container.sub.Teacher;
import net.ilexiconn.magister.container.type.InfoType;

import java.util.Date;


public class CalendarDB extends SQLiteOpenHelper {

    private static final String TAG = "CalendarDB";

    private static final int DATABASE_VERSION = 3;

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
        if (appointments.length == 0 || appointments == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Log.d(TAG, "addItems: amount of items: " + appointments.length);
        String day = "";

        for (Appointment item :
                appointments) {
            Integer id = item.id;
            if (!day.equals(item.startDateString.substring(0, 10))) {
                deleteAppointmentByDateString(item.startDateString);
            }
            day = item.startDateString.substring(0, 10);

            contentValues.put(KEY_CALENDAR_ID, id);
            contentValues.put(KEY_DESC, item.description);
            contentValues.put(KEY_CLASS_ROOMS, new Gson().toJson(item.classrooms));
            contentValues.put(KEY_CONTENT, item.content);
            contentValues.put(KEY_END, item.endDateString);
            contentValues.put(KEY_FINISHED, item.finished);
            contentValues.put(KEY_FULL_DATE, item.startDateString.replaceAll("[T:Z.-]", ""));
            contentValues.put(KEY_INFO_TYPE, item.infoType.getID());
            contentValues.put(KEY_LINKS, new Gson().toJson(item.links));
            contentValues.put(KEY_LOCATION, item.location);
            contentValues.put(KEY_PERIOD_FROM, item.periodFrom);
            contentValues.put(KEY_PERIOD_TO, item.periodUpToAndIncluding);
            contentValues.put(KEY_START, item.startDateString);
            contentValues.put(KEY_STATE, item.classState);
            contentValues.put(KEY_SUBJECTS, new Gson().toJson(item.subjects));
            contentValues.put(KEY_TEACHER, new Gson().toJson(item.teachers));
            contentValues.put(KEY_TAKES_ALL_DAY, item.takesAllDay);


            db.insert(TABLE_CALENDAR, null, contentValues);

        }

        db.close();

    }

    public void deleteAppointmentByDate(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String Query = "DELETE FROM " + TABLE_CALENDAR + " WHERE " + KEY_FULL_DATE + " LIKE '" + dateStr + "%'";
        db.execSQL(Query);
    }

    public void deleteAppointmentByDateString(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dateStr = date.replaceAll("[T:Z.-]", "").substring(0, 8);
        String Query = "DELETE FROM " + TABLE_CALENDAR + " WHERE " + KEY_FULL_DATE + " LIKE '" + dateStr + "%'";
        db.execSQL(Query);
    }

    public void finishAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FINISHED, appointment.finished);

        db.update(TABLE_CALENDAR, contentValues, KEY_CALENDAR_ID + "=" + appointment.id, null);
    }
    
    public Appointment[] getAppointmentsByDate(Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String Query = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + KEY_FULL_DATE + " LIKE '" + dateStr + "%'";
        Cursor cursor = db.rawQuery(Query, null);
        Log.d(TAG, "getAppointmentsByDate: Query: " + Query);
        Log.d(TAG, "getAppointmentsByDate: amount of items: " + cursor.getCount());
        Appointment[] results = new Appointment[cursor.getCount()];
        int i = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Gson gson = new Gson();
                    Appointment appointment = new Appointment();
                    appointment.id = cursor.getInt(cursor.getColumnIndex(KEY_CALENDAR_ID));
                    appointment.startDate = DateUtils.parseDate(cursor.getString(cursor.getColumnIndex(KEY_START)), "yyyy-MM-dd'T'HH:mm:ss.0000000'Z'");
                    appointment.classrooms = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_CLASS_ROOMS)), Classroom[].class);
                    appointment.content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
                    appointment.endDate = DateUtils.parseDate(cursor.getString(cursor.getColumnIndex(KEY_END)), "yyyy-MM-dd'T'HH:mm:ss.0000000'Z'");
                    appointment.finished = cursor.getInt(cursor.getColumnIndex(KEY_FINISHED)) > 0;
                    appointment.infoType = InfoType.getTypeById(cursor.getInt(cursor.getColumnIndex(KEY_INFO_TYPE)));
                    appointment.links = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_LINKS)), Link[].class);
                    appointment.location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
                    appointment.periodFrom = cursor.getInt(cursor.getColumnIndex(KEY_PERIOD_FROM));
                    appointment.periodUpToAndIncluding = cursor.getInt(cursor.getColumnIndex(KEY_PERIOD_TO));
                    appointment.description = cursor.getString(cursor.getColumnIndex(KEY_DESC));
                    appointment.classState = cursor.getInt(cursor.getColumnIndex(KEY_STATE));
                    appointment.subjects = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_SUBJECTS)), SubSubject[].class);
                    appointment.teachers = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_TEACHER)), Teacher[].class);

                    results[i] = appointment;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return results;
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

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_CALENDAR, null, null);
    }
}
