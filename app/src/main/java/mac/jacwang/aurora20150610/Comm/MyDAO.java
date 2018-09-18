package mac.jacwang.aurora20150610.Comm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.niusounds.sqlite.Check;
import com.niusounds.sqlite.Collate;
import com.niusounds.sqlite.Default;
import com.niusounds.sqlite.NotNull;
import com.niusounds.sqlite.Persistence;
import com.niusounds.sqlite.PrimaryKey;
import com.niusounds.sqlite.SQLBuildHelper;
import com.niusounds.sqlite.TableName;
import com.niusounds.sqlite.TablePrimaryKey;
import com.niusounds.sqlite.TableUnique;
import com.niusounds.sqlite.Unique;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mac.jacwang.aurora20150610.DataAnalysis.t_store;

/**
 * Created by jac on 15/8/22.
 */
public class MyDAO extends SQLiteOpenHelper {
    private static MyDAO instance;
    private Class<?>[] classes;

    private static ContentValues createContentValues(Object o) {
        try {
            ContentValues e = new ContentValues();
            Iterator var3 = getPersistenceFields(o.getClass()).iterator();

            while(true) {
                Field f;
                do {
                    if(!var3.hasNext()) {
                        return e;
                    }

                    f = (Field)var3.next();
                } while(f.isAnnotationPresent(PrimaryKey.class) && ((PrimaryKey)f.getAnnotation(PrimaryKey.class)).autoIncrement());

                Class type = f.getType();
                String name = f.getName();
                if(type == Integer.TYPE) {
                    e.put(name, Integer.valueOf(f.getInt(o)));
                } else if(type == Long.TYPE) {
                    e.put(name, Long.valueOf(f.getLong(o)));
                } else if(type == Float.TYPE) {
                    e.put(name, Float.valueOf(f.getFloat(o)));
                } else if(type == Double.TYPE) {
                    e.put(name, Double.valueOf(f.getDouble(o)));
                } else if(type == String.class) {
                    e.put(name, (String)f.get(o));
                } else if(type == byte[].class) {
                    e.put(name, (byte[])f.get(o));
                } else if(type == Boolean.TYPE) {
                    e.put(name, Boolean.valueOf(f.getBoolean(o)));
                } else if(type == Short.TYPE) {
                    e.put(name, Short.valueOf(f.getShort(o)));
                } else if(type == Date.class) {
                    Date e1 = (Date)f.get(o);
                    e.put(name, e1 != null?Long.valueOf(e1.getTime()):null);
                } else if(type.isEnum()) {
                    Enum e2 = (Enum)f.get(o);
                    e.put(name, e2 != null?e2.name():null);
                }
            }
        } catch (Exception var7) {
            return null;
        }
    }

    private static Set<Field> getPersistenceFields(Class<?> clz) {
        HashSet fields = new HashSet();
        Field[] var5;
        int var4 = (var5 = clz.getDeclaredFields()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Field f = var5[var3];
            if(f.isAnnotationPresent(Persistence.class)) {
                f.setAccessible(true);
                fields.add(f);
            }
        }

        return fields;
    }

    public static String getTableName(Class<?> clz) {
        return clz.isAnnotationPresent(TableName.class)?((TableName)clz.getAnnotation(TableName.class)).value():clz.getSimpleName();
    }


    public static MyDAO getInstance(Context context, String name, Class... classes) {
        return getInstance(context, name, (CursorFactory)null, 1, classes);
    }


    public static MyDAO getInstance(Context context, String name, CursorFactory factory, int version, Class... classes) {
        if(instance == null) {
            instance = new MyDAO(context, name, factory, version, classes);
        }

        return instance;
    }

    private MyDAO(Context context, String name, CursorFactory factory, int version, Class... classes) {
        super(context, name, factory, version);
        this.classes = classes;
    }

    private void createTable(SQLiteDatabase db, Class<?> clz) {
        String tableName = getTableName(clz);
        StringBuilder columnDefs = new StringBuilder();
        Iterator sb = getPersistenceFields(clz).iterator();

        while(sb.hasNext()) {
            Field tableConstraints = (Field)sb.next();
            columnDefs.append(",").append(tableConstraints.getName());
            Class sql = tableConstraints.getType();
            columnDefs.append(" ").append(SQLBuildHelper.getSQLType(sql));
            if(tableConstraints.isAnnotationPresent(PrimaryKey.class)) {
                PrimaryKey annotation = (PrimaryKey)tableConstraints.getAnnotation(PrimaryKey.class);
                columnDefs.append(SQLBuildHelper.getPrimaryKeySQL(annotation));
            }

            if(tableConstraints.isAnnotationPresent(NotNull.class)) {
                NotNull annotation2 = (NotNull)tableConstraints.getAnnotation(NotNull.class);
                columnDefs.append(SQLBuildHelper.getNotNullSQL(annotation2));
            }

            if(tableConstraints.isAnnotationPresent(Unique.class)) {
                Unique annotation1 = (Unique)tableConstraints.getAnnotation(Unique.class);
                columnDefs.append(SQLBuildHelper.getUniqueSQL(annotation1));
            }

            if(tableConstraints.isAnnotationPresent(Check.class)) {
                Check annotation4 = (Check)tableConstraints.getAnnotation(Check.class);
                columnDefs.append(SQLBuildHelper.getCheckSQL(annotation4));
            }

            if(tableConstraints.isAnnotationPresent(Default.class)) {
                Default annotation3 = (Default)tableConstraints.getAnnotation(Default.class);
                columnDefs.append(SQLBuildHelper.getDefaultSQL(annotation3));
            }

            if(tableConstraints.isAnnotationPresent(Collate.class)) {
                Collate annotation5 = (Collate)tableConstraints.getAnnotation(Collate.class);
                columnDefs.append(SQLBuildHelper.getCollateSQL(annotation5));
            }
        }

        StringBuilder tableConstraints1 = new StringBuilder();
        if(clz.isAnnotationPresent(TablePrimaryKey.class)) {
            TablePrimaryKey sb2 = (TablePrimaryKey)clz.getAnnotation(TablePrimaryKey.class);
            tableConstraints1.append(",").append(SQLBuildHelper.getTablePrimaryKeySQL(sb2));
        }

        if(clz.isAnnotationPresent(TableUnique.class)) {
            TableUnique sb1 = (TableUnique)clz.getAnnotation(TableUnique.class);
            tableConstraints1.append(",").append(SQLBuildHelper.getTableUniqueSQL(sb1));
        }

        StringBuilder sb3 = new StringBuilder();
        sb3.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb3.append(" (");
        sb3.append(columnDefs.substring(1));
        sb3.append(tableConstraints1.toString());
        sb3.append(" )");
        String sql1 = sb3.toString();
        //Logger.log(sql1);
        db.execSQL(sql1);
    }

    public int delete(Class<?> clz, String whereClause, String... whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();

        int var6;
        try {
            var6 = this.delete(db, clz, whereClause, whereArgs);
        } finally {
            if(!db.inTransaction()) {
                db.close();
            }

        }

        return var6;
    }

    private int delete(SQLiteDatabase db, Class<?> clz, String whereClause, String... whereArgs) {
        return db.delete(getTableName(clz), whereClause, whereArgs);
    }

    private void dropTable(SQLiteDatabase db, Class<?> clz) {
        String tableName = getTableName(clz);
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public <T> List<T> get(Class<T> clz, String selection, String[] selectionArgs, String orderBy, String limit) {
        return this.get(clz, (String[])null, selection, selectionArgs, (String)null, (String)null, orderBy, limit);
    }
    public <T> List<T> get(Class<T> clz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return this.get(db, clz, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    private <T> List<T> get(SQLiteDatabase db, Class<T> clz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor c = this.query(db, clz, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        ArrayList result = new ArrayList();
        try {
            label210:
            while(c.moveToNext()) {
                Object e = clz.newInstance();
                Iterator var14 = getPersistenceFields(clz).iterator();
                while(true) {
                    while(true) {
                        Field f;
                        int idx;
                        do {
                            if(!var14.hasNext()) {
                                result.add(e);
                                continue label210;
                            }

                            f = (Field)var14.next();
                            idx = c.getColumnIndex(f.getName());
                        } while(idx <= -1);

                        Class type = f.getType();
                        if(!c.isNull(idx)) {
                            if(type == Integer.TYPE) {
                                f.set(e, Integer.valueOf(c.getInt(idx)));
                            } else if(type == Long.TYPE) {
                                f.set(e, Long.valueOf(c.getLong(idx)));
                            } else if(type == Float.TYPE) {
                                f.set(e, Float.valueOf(c.getFloat(idx)));
                            } else if(type == Double.TYPE) {
                                f.set(e, Double.valueOf(c.getDouble(idx)));
                            } else if(type == String.class) {
                                f.set(e, c.getString(idx));
                            } else if(type == byte[].class) {
                                f.set(e, c.getBlob(idx));
                            } else if(type == Boolean.TYPE) {
                                f.set(e, Boolean.valueOf(c.getInt(idx) == 1));
                            } else if(type == Short.TYPE) {
                                f.set(e, Short.valueOf(c.getShort(idx)));
                            } else if(type == Date.class) {
                                f.set(e, new Date(c.getLong(idx)));
                            } else if(type.isEnum()) {
                                f.set(e, Enum.valueOf(type, c.getString(idx)));
                            }
                        } else if(type != Integer.TYPE && type != Long.TYPE && type != Float.TYPE && type != Double.TYPE && type != Short.TYPE) {
                            if(!type.isPrimitive()) {
                                f.set(e, (Object)null);
                            }
                        } else {
                            f.set(e, Integer.valueOf(0));
                        }
                    }
                }
            }

            ArrayList var18 = result;
            return var18;

        } catch (InstantiationException var22) {
            var22.printStackTrace();
        } catch (IllegalAccessException var23) {
            var23.printStackTrace();
        } finally {
            if(!db.inTransaction()) {
                c.close();
            }

        }

        return null;
    }

    public List<Long> insertAll(List<?> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ArrayList result = new ArrayList();
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                Object o = var5.next();
                result.add(Long.valueOf(this.insert(db, o)));
            }

            ArrayList var7 = result;
            return var7;
        } finally {
            if(!db.inTransaction()) {
                db.close();
            }

        }
    }

    public long insert(Object o) {
        SQLiteDatabase db = this.getWritableDatabase();

        long var4;
        try {
            var4 = this.insert(db, o);
        } finally {
            if(!db.inTransaction()) {
                db.close();
            }

        }

        return var4;
    }

    private long insert(SQLiteDatabase db, Object o) {
        ContentValues values = createContentValues(o);
        return values != null?db.insert(getTableName(o.getClass()), (String)null, values):-1L;
    }

    public void onCreate(SQLiteDatabase db) {
        if(this.classes != null) {
            Class[] var5 = this.classes;
            int var4 = this.classes.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                Class c = var5[var3];
                this.createTable(db, c);
            }
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(this.classes != null) {
            Class[] var7 = this.classes;
            int var6 = this.classes.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                Class c = var7[var5];
                this.dropTable(db, c);
                this.createTable(db, c);
            }
        }

    }


    private Cursor query(SQLiteDatabase db, Class<?> clz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return !limit.isEmpty()?db.query(getTableName(clz), columns, selection, selectionArgs, groupBy, having, orderBy, limit):db.query(getTableName(clz), columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void transaction(MyDAO.Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            if(t.execute(this)) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }

    }



    public interface Transaction {
        boolean execute(MyDAO var1);
    }
}
