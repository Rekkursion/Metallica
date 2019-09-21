package com.rekkursion.metallica.manager

import java.io.*
import java.lang.ClassCastException

object SerializationManager {
    // serialize out
    @Synchronized fun save(obj: Any?, path: String): Boolean {
        if (obj == null)
            return false

        var oos: ObjectOutputStream? = null
        try {
            oos = ObjectOutputStream(FileOutputStream(path))
            oos.writeObject(obj)
            oos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (oos != null) {
                try {
                    oos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return false
    }

    // serialize in
    @SuppressWarnings("unchecked")
    @Synchronized fun<T> load(path: String): T? {
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(FileInputStream(path))
            val ret: T? = ois.readObject() as T?
            ois.close()
            return ret
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } finally {
            ois?.close()
        }

        return null
    }
}