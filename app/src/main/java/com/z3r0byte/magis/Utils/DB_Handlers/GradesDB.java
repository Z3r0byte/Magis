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

import net.ilexiconn.magister.container.Grade;

/**
 * Created by bas on 8-7-16.
 */
public class GradesDB extends SQLiteOpenHelper {
    private static final String TAG = "GradesDB";

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "gradesDB";
    private static final String TABLE_GRADES = "grades";

    private static final String KEY_DB_ID = "dbID";
    private static final String KEY_GRADE_ID = "id";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_IS_SUFFICIENT = "isSufficient";
    private static final String KEY_FILLED_IN_BY = "filledInBy";
    private static final String KEY_FILLED_IN_DATE = "filledInDate";
    private static final String KEY_GRADE_PERIOD = "gradePeriod";
    private static final String KEY_DO_AT_LATER_DATE = "doAtLaterDate";
    private static final String KEY_DISPENSATION = "dispensation";
    private static final String KEY_DOES_COUNT = "doesCount";
    private static final String KEY_GRADE_ROW = "gradeRow";
    private static final String KEY_TEACHER_ABBRIVATION = "teacherAbbrevation";
    private static final String KEY_GRADE_ROW_ID_OF_ELO = "gradeRowIdOfElo";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_DISPENSATION_FOR_COURSE = "dispensationOfCourse";
    private static final String KEY_DISPENSATION_FOR_COURSE2 = "dispensationOfCourse2";


    public GradesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GRADES_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_GRADES + "("
                + KEY_DB_ID + " INTEGER PRIMARY KEY,"
                + KEY_GRADE_ID + " INTEGER,"
                + KEY_DISPENSATION + " BOOLEAN,"
                + KEY_DISPENSATION_FOR_COURSE + " STRING,"
                + KEY_DISPENSATION_FOR_COURSE2 + " STRING,"
                + KEY_DOES_COUNT + " BOOLEAN,"
                + KEY_DO_AT_LATER_DATE + " BOOLEAN,"
                + KEY_FILLED_IN_BY + " TEXT,"
                + KEY_FILLED_IN_DATE + " TEXT,"
                + KEY_GRADE + " TEXT,"
                + KEY_GRADE_PERIOD + " TEXT,"
                + KEY_GRADE_ROW + " TEXT,"
                + KEY_GRADE_ROW_ID_OF_ELO + " TEXT,"
                + KEY_IS_SUFFICIENT + " BOOLEAN,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_TEACHER_ABBRIVATION + " TEXT"
                + ")";
        db.execSQL(CREATE_GRADES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: New Version!");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        onCreate(db);
    }


    public void addGrades(Grade[] grades) {
        if (grades == null || grades.length == 0) {
            Log.d(TAG, "addGrades: No Grades!");
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();

        ContentValues contentValues = new ContentValues();
        for (Grade grade :
                grades) {

            if (!isInDataBase(grade, db)) {
                contentValues.put(KEY_GRADE_ID, grade.id);
                contentValues.put(KEY_DISPENSATION, grade.dispensation);
                contentValues.put(KEY_DISPENSATION_FOR_COURSE, grade.dispensationForCourse);
                contentValues.put(KEY_DISPENSATION_FOR_COURSE2, grade.dispensationForCourse2);
                contentValues.put(KEY_DO_AT_LATER_DATE, grade.doAtLaterDate);
                contentValues.put(KEY_DOES_COUNT, grade.doesCount);
                contentValues.put(KEY_FILLED_IN_BY, grade.filledInBy);
                contentValues.put(KEY_FILLED_IN_DATE, grade.filledInDateString);
                contentValues.put(KEY_GRADE, grade.grade);
                contentValues.put(KEY_GRADE_PERIOD, gson.toJson(grade.gradePeriod));
                contentValues.put(KEY_GRADE_ROW, gson.toJson(grade.gradeRow));
                contentValues.put(KEY_GRADE_ROW_ID_OF_ELO, gson.toJson(grade.gradeRowIdOfElo));
                contentValues.put(KEY_IS_SUFFICIENT, grade.isSufficient);
                contentValues.put(KEY_SUBJECT, gson.toJson(grade.subject));
                contentValues.put(KEY_TEACHER_ABBRIVATION, gson.toJson(grade.teacherAbbreviation));

                db.insert(TABLE_GRADES, null, contentValues);

            } else {
                contentValues.put(KEY_GRADE_ID, grade.id);
                contentValues.put(KEY_DISPENSATION, grade.dispensation);
                contentValues.put(KEY_DISPENSATION_FOR_COURSE, grade.dispensationForCourse);
                contentValues.put(KEY_DISPENSATION_FOR_COURSE2, grade.dispensationForCourse2);
                contentValues.put(KEY_DO_AT_LATER_DATE, grade.doAtLaterDate);
                contentValues.put(KEY_DOES_COUNT, grade.doesCount);
                contentValues.put(KEY_FILLED_IN_BY, grade.filledInBy);
                contentValues.put(KEY_FILLED_IN_DATE, grade.filledInDateString);
                contentValues.put(KEY_GRADE, grade.grade);
                contentValues.put(KEY_GRADE_PERIOD, gson.toJson(grade.gradePeriod));
                contentValues.put(KEY_GRADE_ROW, gson.toJson(grade.gradeRow));
                contentValues.put(KEY_GRADE_ROW_ID_OF_ELO, gson.toJson(grade.gradeRowIdOfElo));
                contentValues.put(KEY_IS_SUFFICIENT, grade.isSufficient);
                contentValues.put(KEY_SUBJECT, gson.toJson(grade.subject));
                contentValues.put(KEY_TEACHER_ABBRIVATION, gson.toJson(grade.teacherAbbreviation));

                db.update(TABLE_GRADES, contentValues, KEY_GRADE_ID + " = " + grade.id, null);
            }
        }

        db.close();
    }


    private Boolean isInDataBase(Grade grade, SQLiteDatabase db) {
        String Query = "Select * from " + TABLE_GRADES + " where " + KEY_GRADE_ID + " = " + grade.id;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() >= 1) {
            cursor.close();
            return true;
        }
        cursor.close();

        return false;
    }

}
