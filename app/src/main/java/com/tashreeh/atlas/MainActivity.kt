package com.tashreeh.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dao = AtlasDatabase.get(this).atlasDao()
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFF80D8FF), secondary = Color(0xFF9BE7CA),
                background = Color(0xFF101722), surface = Color(0xFF192638)
            )) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AtlasScreen(dao)
                }
            }
        }
    }
}
@Composable
private fun AtlasScreen(dao: AtlasDao) {
    val organs by dao.observe().collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }
    var favorites by rememberSaveable { mutableStateOf(false) }
    var answer by rememberSaveable { mutableStateOf("") }
    var seedError by remember { mutableStateOf(false) }
    LaunchedEffect(dao) {
        try { dao.seed(starterOrgans) } catch (e: Exception) { seedError = true }
    }
    val visible = organs.filter {
        (!favorites || it.favorite) && (it.nameAr.contains(query, true) || it.nameEn.contains(query, true))
    }
    Scaffold { insets ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(insets),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text("تَشْرِيح", style = MaterialTheme.typography.headlineLarge)
                Text("أطلس الإنسان • النسخة التأسيسية 0.1.0", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text("يعمل دون إنترنت — محتوى تعليمي وليس للتشخيص الطبي.")
                if (seedError) Text("تعذر تحميل المحتوى المحلي. أعد فتح التطبيق.")
            }
            item {
                OutlinedTextField(value = query, onValueChange = { query = it },
                    label = { Text("ابحث عن عضو") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = !favorites, onClick = { favorites = false }, label = { Text("كل الأعضاء") })
                    FilterChip(selected = favorites, onClick = { favorites = true }, label = { Text("المفضلة") })
                }
            }
            if (visible.isEmpty()) item { Text("لا توجد نتائج مطابقة.") }
            items(visible, key = { it.id }) { organ ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(organ.nameAr, style = MaterialTheme.typography.titleLarge)
                        Text(organ.nameEn, color = MaterialTheme.colorScheme.primary)
                        Text(organ.descriptionAr)
                        OutlinedButton(onClick = { scope.launch { dao.toggleFavorite(organ.id) } }) {
                            Text(if (organ.favorite) "إزالة من المفضلة" else "حفظ في المفضلة")
                        }
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("اختبر معلوماتك", style = MaterialTheme.typography.titleLarge)
                        Text("أي عضو يضخ الدم إلى سائر الجسم؟")
                        listOf("القلب", "الرئتان", "الدماغ").forEach { option ->
                            OutlinedButton(onClick = { answer = option }) { Text(option) }
                        }
                        if (answer.isNotEmpty()) Text(if (answer == "القلب") "إجابة صحيحة!" else "حاول مرة أخرى.")
                    }
                }
            }
            item {
                Text("المرآة التشريحية ثلاثية الأبعاد: المرحلة التالية.", color = MaterialTheme.colorScheme.secondary)
                Text("المزامنة السحابية: غير مفعلة بعد.")
            }
        }
    }
}
