package ro.code4.deurgenta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ro.code4.deurgenta.data.dao.AddressDao
import ro.code4.deurgenta.data.helper.DateConverter
import ro.code4.deurgenta.data.model.Address

@Database(
    entities = [Address::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    companion object {
        @JvmStatic
        private lateinit var INSTANCE: AppDatabase

        private fun isInitialized(): Boolean = ::INSTANCE.isInitialized
        fun getDatabase(context: Context): AppDatabase {
            if (!isInitialized()) {
                synchronized(AppDatabase::class.java) {
                    if (!isInitialized()) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "database"
                        )
                            //.addMigrations(*Migrations.ALL)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
