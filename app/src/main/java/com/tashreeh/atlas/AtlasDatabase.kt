package com.tashreeh.atlas

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "organs")
data class Organ(
    @PrimaryKey val id: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val favorite: Boolean = false
)
@Dao
interface AtlasDao {
    @Query("SELECT * FROM organs ORDER BY id")
    fun observe(): Flow<List<Organ>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun seed(items: List<Organ>)
    @Query("UPDATE organs SET favorite = NOT favorite WHERE id = :id")
    suspend fun toggleFavorite(id: String)
}
@Database(entities = [Organ::class], version = 1, exportSchema = false)
abstract class AtlasDatabase : RoomDatabase() {
    abstract fun atlasDao(): AtlasDao
    companion object {
        @Volatile private var instance: AtlasDatabase? = null
        fun get(context: Context): AtlasDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, AtlasDatabase::class.java, "tashreeh.db"
            ).build().also { instance = it }
        }
    }
}
val starterOrgans = listOf(
    Organ("brain", "الدماغ", "Brain", "يتحكم في وظائف الجسم، ويشارك في التفكير والذاكرة والحركة والإحساس."),
    Organ("heart", "القلب", "Heart", "عضو عضلي يضخ الدم إلى الرئتين وسائر الجسم عبر الدورة الدموية."),
    Organ("lungs", "الرئتان", "Lungs", "تتبادلان الأكسجين وثاني أكسيد الكربون بين الهواء والدم."),
    Organ("skeleton", "الهيكل العظمي", "Skeleton", "يدعم الجسم ويحمي أعضاء داخلية ويساعد على الحركة بالاشتراك مع العضلات."),
    Organ("kidneys", "الكليتان", "Kidneys", "ترشحان الدم وتساعدان في تنظيم الماء والأملاح والتخلص من الفضلات.")
)
