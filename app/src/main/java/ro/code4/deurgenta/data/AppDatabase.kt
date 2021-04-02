package ro.code4.deurgenta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ro.code4.deurgenta.data.dao.AddressDao
import ro.code4.deurgenta.data.dao.BackpackDao
import ro.code4.deurgenta.data.helper.DateConverter
import ro.code4.deurgenta.data.model.Backpack
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemConverters
import ro.code4.deurgenta.data.model.MapAddress

@Database(
    entities = [Backpack::class, BackpackItem::class, MapAddress::class],
    version = 3
)
@TypeConverters(
    DateConverter::class,
    BackpackItemConverters::class,
    MapAddress.MapAddressConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    abstract fun backpackDao(): BackpackDao

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
