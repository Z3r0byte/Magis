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

import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.container.Study;
import net.ilexiconn.magister.container.sub.GradeRow;
import net.ilexiconn.magister.container.sub.SubSubject;
import net.ilexiconn.magister.container.type.RowType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by bas on 8-7-16.
 */
public class GradesDB extends SQLiteOpenHelper {
    private static final String TAG = "GradesDB";

    private static final int DATABASE_VERSION = 12;

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
    private static final String KEY_GRADE_ROW_TYPE = "gradeType";
    private static final String KEY_TEACHER_ABBRIVATION = "teacherAbbrevation";
    private static final String KEY_GRADE_ROW_ID_OF_ELO = "gradeRowIdOfElo";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_SORTABLE_DATE = "sortableDate";
    private static final String KEY_STUDY_ID = "studyId";
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
                + KEY_GRADE_ROW_TYPE + " INTEGER,"
                + KEY_IS_SUFFICIENT + " BOOLEAN,"
                + KEY_SORTABLE_DATE + " TEXT,"
                + KEY_STUDY_ID + " INTEGER,"
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


    public void addGrades(Grade[] grades, int studyId) {
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
                try {
                    contentValues.put(KEY_GRADE_ROW_TYPE, grade.gradeRow.rowSort.getID());
                } catch (NullPointerException e) {
                    contentValues.put(KEY_GRADE_ROW_TYPE, 99);
                }
                contentValues.put(KEY_IS_SUFFICIENT, grade.isSufficient);
                try {
                    contentValues.put(KEY_SORTABLE_DATE, grade.filledInDateString.replaceAll("[-Z.:T]", ""));
                } catch (Exception e) {
                }
                contentValues.put(KEY_SUBJECT, gson.toJson(grade.subject));
                contentValues.put(KEY_STUDY_ID, studyId);
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
                try {
                    contentValues.put(KEY_GRADE_ROW_TYPE, grade.gradeRow.rowSort.getID());
                } catch (NullPointerException e) {
                    contentValues.put(KEY_GRADE_ROW_TYPE, 99);
                }
                contentValues.put(KEY_IS_SUFFICIENT, grade.isSufficient);
                try {
                    contentValues.put(KEY_SORTABLE_DATE, grade.filledInDateString.replaceAll("[-Z.:T]", ""));
                } catch (Exception e) {
                }
                contentValues.put(KEY_SUBJECT, gson.toJson(grade.subject));
                contentValues.put(KEY_STUDY_ID, studyId);
                contentValues.put(KEY_TEACHER_ABBRIVATION, gson.toJson(grade.teacherAbbreviation));

                db.update(TABLE_GRADES, contentValues, KEY_GRADE_ID + " = " + grade.id, null);
            }
        }

        db.close();
    }

    public Grade[] getUniqueAverageGrades(Study study, Boolean isOldFormat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query;
        if (isOldFormat) {
            Query = "SELECT * FROM " + TABLE_GRADES + " WHERE " + KEY_STUDY_ID + " = " + study.id + " AND " + KEY_GRADE_ID
                    + " != 0 AND " + KEY_SORTABLE_DATE + " IN (SELECT MAX(" + KEY_SORTABLE_DATE
                    + ") FROM " + TABLE_GRADES + " WHERE " + KEY_GRADE_ROW_TYPE + " = 2 OR " + KEY_GRADE_ROW_TYPE + " = 6"
                    + " GROUP BY " + KEY_SUBJECT + ") ORDER BY " + KEY_GRADE_ROW_TYPE + " DESC";
        } else {
            Query = "SELECT * FROM " + TABLE_GRADES + " WHERE " + KEY_STUDY_ID + " = " + study.id + " AND " + KEY_GRADE_ID
                    + " != 0 AND " + KEY_SORTABLE_DATE + " IN (SELECT MAX(" + KEY_SORTABLE_DATE
                    + ") FROM " + TABLE_GRADES + " WHERE " + KEY_GRADE_ROW_TYPE + " = 4 OR " + KEY_GRADE_ROW_TYPE + " = 6"
                    + " GROUP BY " + KEY_SUBJECT + ") ORDER BY " + KEY_GRADE_ROW_TYPE + " DESC";
        }
        Cursor cursor = db.rawQuery(Query, null);
        Log.d(TAG, "getAppointmentsByDate: Query: " + Query);
        Log.d(TAG, "getAppointmentsByDate: amount of items: " + cursor.getCount());
        Grade[] grades = new Grade[cursor.getCount()];
        int i = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Gson gson = new Gson();
                    Grade grade = new Grade();

                    grade.grade = cursor.getString(cursor.getColumnIndex(KEY_GRADE));
                    grade.subject = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT)), SubSubject.class);
                    grade.filledInDate = DateUtils.parseDate(cursor.getString(cursor.getColumnIndex(KEY_FILLED_IN_DATE)), "yyyy-MM-dd'T'HH:mm:ss");
                    grade.gradeRow = new GradeRow();
                    grade.gradeRow.rowSort = RowType.getTypeById(cursor.getInt(cursor.getColumnIndex(KEY_GRADE_ROW_TYPE)));
                    Log.d(TAG, "getUniqueAverageGrades: rowtype: " + grade.gradeRow.rowSort.getID());
                    grades[i] = grade;
                    i++;
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        List<Grade> gradeList = new ArrayList<Grade>();
        if (isOldFormat) {
            for (Grade grade : grades) {
                try {
                    if (grade.gradeRow.rowSort.getID() == 2 || grade.gradeRow.rowSort.getID() == 6) {
                        gradeList.add(grade);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Grade grade : grades) {
                try {
                    if (grade.gradeRow.rowSort.getID() == 2 || grade.gradeRow.rowSort.getID() == 4 || grade.gradeRow.rowSort.getID() == 6) {
                        gradeList.add(grade);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        grades = gradeList.toArray(new Grade[gradeList.size()]);

        if (isOldFormat) {
            Collections.reverse(Arrays.asList(grades));
        }
        return grades;
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

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        removeAll(db);
    }

    public void removeAll(SQLiteDatabase db) {
        db.delete(TABLE_GRADES, null, null);
    }

}
