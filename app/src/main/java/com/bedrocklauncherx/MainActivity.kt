package com.bedrocklauncherx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bedrocklauncherx.data.LauncherDatabase
import com.bedrocklauncherx.data.World
import kotlinx.coroutines.launch

private val Night = Color(0xFF101018)
private val Panel = Color(0xFF1B1B28)
private val NeonGreen = Color(0xFF69F0AE)
private val NeonPurple = Color(0xFFB388FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = LauncherDatabase.get(this).worldDao()
        setContent { LauncherApp(dao) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LauncherApp(dao: com.bedrocklauncherx.data.WorldDao) {
    var arabic by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf("home") }
    var showCreate by remember { mutableStateOf(false) }
    val worlds by dao.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    val title = if (arabic) "BedrockLauncherX" else "BedrockLauncherX"
    val nav = if (arabic) listOf("الرئيسية", "العوالم", "الخوادم", "الإضافات", "الأداء", "الإعدادات", "حول") else listOf("Home", "Worlds", "Servers", "Mods", "Performance", "Settings", "About")
    val keys = listOf("home", "worlds", "servers", "mods", "performance", "settings", "about")

    CompositionLocalProvider(LocalLayoutDirection provides if (arabic) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        MaterialTheme(colorScheme = darkColorScheme(
            primary = NeonGreen, secondary = NeonPurple, background = Night, surface = Panel,
            onPrimary = Color.Black, onBackground = Color.White, onSurface = Color.White
        )) {
            Scaffold(
                containerColor = Night,
                topBar = {
                    TopAppBar(
                        title = { Text(title, fontWeight = FontWeight.Bold, color = Color.White) },
                        actions = { TextButton(onClick = { arabic = !arabic }) { Text(if (arabic) "EN" else "عربي") } },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Night)
                    )
                },
                bottomBar = {
                    NavigationBar(containerColor = Panel) {
                        nav.forEachIndexed { index, label ->
                            NavigationBarItem(selected = page == keys[index], onClick = { page = keys[index] },
                                icon = { Icon(when (index) { 0 -> Icons.Default.Home; 1 -> Icons.Default.Public; 2 -> Icons.Default.Dns; 3 -> Icons.Default.Extension; 4 -> Icons.Default.Speed; 5 -> Icons.Default.Settings; else -> Icons.Default.Info }, contentDescription = label) },
                                label = { Text(label, fontSize = 11.sp) })
                        }
                    }
                }
            ) { padding ->
                when (page) {
                    "home" -> HomeScreen(arabic, worlds.firstOrNull(), worlds.size, onWorlds = { page = "worlds" }, modifier = Modifier.padding(padding))
                    "worlds" -> WorldsScreen(arabic, worlds, onAdd = { showCreate = true }, onDelete = { id -> scope.launch { dao.deleteById(id) } }, modifier = Modifier.padding(padding))
                    else -> InfoScreen(arabic, page, Modifier.padding(padding))
                }
            }
            if (showCreate) CreateWorldDialog(arabic, onDismiss = { showCreate = false }, onCreate = { name, seed, mode, diff ->
                scope.launch { dao.insert(World(name = name, seed = seed, gameMode = mode, difficulty = diff)) }
                showCreate = false
            })
        }
    }
}

@Composable
private fun HomeScreen(ar: Boolean, featured: World?, worldCount: Int, onWorlds: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(if (ar) "مرحبًا بك في عالمك" else "Your worlds, ready to play", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (ar) "العالم المميز" else "FEATURED WORLD", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(featured?.name ?: if (ar) "لا توجد عوالم بعد" else "No worlds yet", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(if (featured != null) "${if (ar) "النمط" else "Mode"}: ${featured.gameMode} · ${if (ar) "الصعوبة" else "Difficulty"}: ${featured.difficulty}" else if (ar) "أنشئ عالمًا للبدء" else "Create a world to get started", color = Color.LightGray)
                Button(onClick = onWorlds, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Explore, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(if (ar) "إدارة العوالم" else "Manage Worlds", color = Color.Black)
                }
                Text(if (ar) "الخادم المحلي غير مفعّل في هذه المرحلة" else "Local server hosting is planned for a later phase", color = Color.Gray, fontSize = 12.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(if (ar) "العوالم" else "Worlds", worldCount.toString(), Modifier.weight(1f))
            MetricCard(if (ar) "الخادم" else "Server", if (ar) "غير متاح بعد" else "Not installed", Modifier.weight(1f))
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) { Text(label, color = Color.Gray, fontSize = 12.sp); Text(value, color = NeonGreen, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun WorldsScreen(ar: Boolean, worlds: List<World>, onAdd: () -> Unit, onDelete: (Long) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(if (ar) "عوالمك" else "Your Worlds", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            FilledTonalButton(onClick = onAdd) { Icon(Icons.Default.Add, contentDescription = null); Text(if (ar) "إنشاء" else "Create") }
        }
        if (worlds.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(if (ar) "لا توجد عوالم. أنشئ عالمك الأول." else "No worlds yet. Create your first world.", color = Color.LightGray) }
        } else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
            items(worlds, key = { it.id }) { world ->
                Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(16.dp)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Terrain, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(36.dp))
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(world.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("${world.gameMode} · ${world.difficulty}${if (world.seed.isNotBlank()) " · seed ${world.seed}" else ""}", color = Color.LightGray, fontSize = 12.sp)
                        }
                        IconButton(onClick = { onDelete(world.id) }) { Icon(Icons.Default.DeleteOutline, contentDescription = if (ar) "حذف" else "Delete", tint = Color(0xFFFF8A80)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateWorldDialog(ar: Boolean, onDismiss: () -> Unit, onCreate: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var seed by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("survival") }
    var difficulty by remember { mutableStateOf("normal") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (ar) "إنشاء عالم" else "Create World") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text(if (ar) "اسم العالم" else "World name") }, singleLine = true)
            OutlinedTextField(seed, { seed = it }, label = { Text(if (ar) "البذرة (اختياري)" else "Seed (optional)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
            Text(if (ar) "نمط اللعب" else "Game mode")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("survival", "creative").forEach { value -> FilterChip(selected = mode == value, onClick = { mode = value }, label = { Text(if (ar) if (value == "survival") "بقاء" else "إبداعي" else value) }) } }
            Text(if (ar) "الصعوبة" else "Difficulty")
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { listOf("peaceful", "easy", "normal", "hard").forEach { value -> FilterChip(selected = difficulty == value, onClick = { difficulty = value }, label = { Text(if (ar) mapOf("peaceful" to "سلمي", "easy" to "سهل", "normal" to "عادي", "hard" to "صعب").getValue(value) else value) }) } }
        } },
        confirmButton = { TextButton(enabled = name.isNotBlank(), onClick = { onCreate(name.trim(), seed.trim(), mode, difficulty) }) { Text(if (ar) "حفظ" else "Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(if (ar) "إلغاء" else "Cancel") } })
}

@Composable
private fun InfoScreen(ar: Boolean, page: String, modifier: Modifier = Modifier) {
    val title = when (page) {
        "servers" -> if (ar) "إدارة الخوادم" else "Server Manager"
        "mods" -> if (ar) "الإضافات" else "Mods"
        "performance" -> if (ar) "الأداء" else "Performance"
        "settings" -> if (ar) "الإعدادات" else "Settings"
        else -> if (ar) "حول BedrockLauncherX" else "About BedrockLauncherX"
    }
    Column(modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        val detail = when (page) {
            "servers" -> if (ar) "لم يتم تثبيت محرك خادم بعد. ستأتي إدارة الخادم وخدمة الخلفية في مرحلة لاحقة." else "No server engine is installed yet. Server lifecycle and background service are planned for a later phase."
            "mods" -> if (ar) "إدارة الإضافات غير متاحة بعد؛ لا توجد إضافات مثبتة في هذه المرحلة." else "Mod management is not available yet; no mods are installed in this phase."
            "performance" -> if (ar) "لا تتوفر قياسات أداء للخادم قبل تشغيل محركه. لن نعرض أرقامًا تقديرية." else "Server performance metrics are unavailable until an engine runs. No estimated values are displayed."
            "about" -> if (ar) "الإصدار 0.1.0 · تطبيق مرافق مستقل. غير تابع لـ Mojang أو Microsoft." else "Version 0.1.0 · Independent companion app. Not affiliated with Mojang or Microsoft."
            else -> if (ar) "الإصدار 0.1.0 · المرحلة الأولى. استخدم زر اللغة بالأعلى للتبديل بين العربية والإنجليزية." else "Version 0.1.0 · Phase 1. Use the language button above to switch between English and Arabic."
        }
        Text(detail, color = Color.LightGray)
    }
}
