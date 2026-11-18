package com.khaulahqurrota.ecoplant

import android.os.Bundle
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khaulahqurrota.ecoplant.ui.theme.EcoPlantTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

private val Forest = Color(0xFF174C38)
private val Leaf = Color(0xFF2F7955)
private val Mint = Color(0xFFE8F2EA)
private val Cream = Color(0xFFF7F8F4)
private val Ink = Color(0xFF1E2D25)
private val Muted = Color(0xFF78837B)
private val DeepForest = Color(0xFF0C3025)
private val StoreSurface = Color(0xD9174634)
private val StoreCard = Color(0xD920563E)
private val StoreText = Color(0xFFF5F7F0)
private val StoreMuted = Color(0xFFBDD0C1)

data class Plant(
    val id: Int,
    val name: String,
    val subtitle: String,
    val price: Int,
    val category: String,
    val photoRes: Int,
    val rating: String = "4.9",
    val scientificName: String = "",
    val description: String = "",
    val gardenArea: String = "Area koleksi tanaman",
    val careTips: String = "Rawat sesuai kebutuhan cahaya dan airnya.",
    val healthStatus: String = "Sehat",
)

private val plants = listOf(
    Plant(1, "Monstera Deliciosa", "Tanaman hias · pot 14 cm", 85000, "Indoor", R.drawable.plant_monstera, scientificName = "Monstera deliciosa", description = "Monstera dikenal dengan daun besar berlubang yang membantu tanaman menangkap cahaya di habitat tropis.", gardenArea = "Rumah Kaca Tropis", careTips = "Sukai cahaya terang tidak langsung dan tanah yang lembap, bukan becek."),
    Plant(2, "Lidah Mertua", "Mudah dirawat · pot 12 cm", 45000, "Indoor", R.drawable.plant_snake, scientificName = "Dracaena trifasciata", description = "Tanaman berdaun tegak dengan corak hijau. Tahan kondisi kering dan cocok untuk koleksi dalam ruang.", gardenArea = "Taman Tanaman Tangguh", careTips = "Letakkan di area terang atau teduh dan siram setelah tanah mengering."),
    Plant(3, "Peace Lily", "Penyegar ruangan · pot 14 cm", 68000, "Indoor", R.drawable.plant_peace_lily, scientificName = "Spathiphyllum", description = "Peace Lily memiliki bunga putih khas dan daun hijau mengilap. Tanaman ini menyukai lingkungan yang lembap.", gardenArea = "Taman Teduh", careTips = "Tempatkan di cahaya tidak langsung dan jaga media tetap sedikit lembap."),
    Plant(4, "Kaktus Mini", "Suka cahaya matahari", 25000, "Sukulen", R.drawable.plant_cactus, scientificName = "Cactaceae", description = "Kaktus menyimpan air pada batangnya dan memiliki duri sebagai bentuk adaptasi di lingkungan kering.", gardenArea = "Taman Kering", careTips = "Berikan banyak cahaya dan siram sedikit saat media sudah kering."),
    Plant(5, "Mawar Mini", "Bunga cantik · pot 12 cm", 55000, "Berbunga", R.drawable.plant_rose, scientificName = "Rosa", description = "Mawar mini adalah tanaman berbunga dengan kelopak berlapis dan aroma khas. Warnanya dapat berbeda menurut varietas.", gardenArea = "Taman Bunga", careTips = "Butuh cahaya matahari cukup, penyiraman teratur, dan pemangkasan bunga layu."),
    Plant(6, "Anggrek Bulan", "Bunga tahan lama · pot 14 cm", 95000, "Berbunga", R.drawable.plant_orchid, scientificName = "Phalaenopsis", description = "Anggrek bulan memiliki bunga lebar yang tumbuh pada tangkai melengkung dan dapat mekar cukup lama.", gardenArea = "Rumah Anggrek", careTips = "Gunakan media berpori, cahaya terang tidak langsung, dan hindari air menggenang."),
    Plant(7, "Kalanchoe", "Bunga mini warna cerah", 39000, "Berbunga", R.drawable.plant_kalanchoe, scientificName = "Kalanchoe blossfeldiana", description = "Kalanchoe adalah sukulen berbunga dengan kumpulan bunga kecil dan daun berdaging.", gardenArea = "Taman Bunga Mini", careTips = "Letakkan di tempat terang dan siram saat media tanam mengering."),
    Plant(8, "Lavender", "Aroma lembut · area cerah", 62000, "Berbunga", R.drawable.plant_lavender, scientificName = "Lavandula", description = "Lavender dikenal dari bunga ungu dan aromanya. Bunga keringnya sering digunakan sebagai pewangi.", gardenArea = "Taman Aroma", careTips = "Sukai sinar matahari dan tanah dengan drainase baik; jangan terlalu sering disiram."),
)

private data class TourismInfo(
    val name: String = "Wisata EcoPlant",
    val description: String = "Jelajahi koleksi tanaman dan nikmati suasana hijau bersama keluarga.",
    val address: String = "Alamat lokasi wisata",
    val hours: String = "Setiap hari · 08.00–17.00",
    val contact: String = "Kontak pengelola",
)

private data class PlantReport(
    val id: Int,
    val type: String,
    val plantId: Int?,
    val plantName: String,
    val condition: String,
    val description: String,
    val status: String = "Menunggu ditinjau",
    val createdAt: Long = System.currentTimeMillis(),
)

private enum class StoreTab { Home, Guide, Search, Cart, Profile }
private enum class ProfileSection { Orders, Address, Favorites, Care, Settings, TourismInfo }
enum class AppRole { Buyer, Admin, Officer }
private enum class StaffTab { Overview, Products, Orders, PlantChecks, Reports, TourismInfo }

private data class OrderSummary(
    val orderNumber: Int,
    val recipient: String,
    val address: String,
    val plants: List<Plant>,
    val status: String = "Menunggu diproses",
) {
    val total: Int get() = plants.sumOf { it.price }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EcoPlantTheme { PlantStoreApp() } }
    }
}

@Composable
private fun PlantStoreApp() {
    var role by remember { mutableStateOf<AppRole?>(null) }
    var tab by remember { mutableStateOf(StoreTab.Home) }
    var selectedPlant by remember { mutableStateOf<Plant?>(null) }
    var selectedGuidePlant by remember { mutableStateOf<Plant?>(null) }
    var checkout by remember { mutableStateOf(false) }
    var orderDone by remember { mutableStateOf(false) }
    var profileSection by remember { mutableStateOf<ProfileSection?>(null) }
    var lastOrder by remember { mutableStateOf<OrderSummary?>(null) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var tipsEnabled by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val cart = remember { mutableStateListOf<Plant>() }
    val favoriteIds = remember { mutableStateListOf<Int>() }
    val catalog = remember { mutableStateListOf<Plant>().apply { addAll(plants) } }
    val orders = remember { mutableStateListOf<OrderSummary>() }
    val reports = remember { mutableStateListOf<PlantReport>() }
    var tourismInfo by remember { mutableStateOf(TourismInfo()) }

    if (role == null) {
        LoginScreen(
            onLogin = { role = it },
            onBrowseAsBuyer = { role = AppRole.Buyer },
        )
        return
    }

    if (role == AppRole.Admin || role == AppRole.Officer) {
        Box(Modifier.fillMaxSize()) {
            EcoPlantBackdrop(Modifier.fillMaxSize())
            StaffConsole(
                role = role!!,
                catalog = catalog,
                orders = orders,
                onLogout = { role = null },
                onSavePlant = { plant ->
                    if (plant.id == 0) {
                        val nextId = (catalog.maxOfOrNull { it.id } ?: 0) + 1
                        catalog.add(plant.copy(id = nextId))
                    } else {
                        val index = catalog.indexOfFirst { it.id == plant.id }
                        if (index >= 0) catalog[index] = plant
                    }
                },
                onDeletePlant = { catalog.remove(it) },
                onUpdateOrder = { orderNumber, status ->
                    val index = orders.indexOfFirst { it.orderNumber == orderNumber }
                    if (index >= 0) orders[index] = orders[index].copy(status = status)
                },
                reports = reports,
                onAddReport = { reports.add(0, it) },
                onUpdateReport = { id, status ->
                    val index = reports.indexOfFirst { it.id == id }
                    if (index >= 0) reports[index] = reports[index].copy(status = status)
                },
                onUpdatePlantHealth = { plantId, status ->
                    val index = catalog.indexOfFirst { it.id == plantId }
                    if (index >= 0) catalog[index] = catalog[index].copy(healthStatus = status)
                },
                tourismInfo = tourismInfo,
                onSaveTourismInfo = { tourismInfo = it },
            )
        }
        return
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (selectedPlant == null && selectedGuidePlant == null && !checkout && !orderDone && profileSection == null) {
                StoreNavigation(tab = tab, cartCount = cart.size, onSelect = { tab = it })
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            EcoPlantBackdrop(Modifier.fillMaxSize())
            when {
                orderDone -> OrderConfirmation(onDone = { orderDone = false; tab = StoreTab.Home })
                checkout -> CheckoutScreen(
                    cart = cart.toList(),
                    onBack = { checkout = false },
                    onPlaceOrder = { recipient, address ->
                        val newOrder = OrderSummary(orders.size + 1, recipient, address, cart.toList())
                        orders.add(newOrder)
                        lastOrder = newOrder
                        cart.clear()
                        checkout = false
                        orderDone = true
                    },
                )
                selectedPlant != null -> PlantDetail(
                    plant = selectedPlant!!,
                    onBack = { selectedPlant = null },
                    onAdd = { cart.add(selectedPlant!!); selectedPlant = null; profileSection = null; tab = StoreTab.Cart },
                    isFavorite = selectedPlant!!.id in favoriteIds,
                    onToggleFavorite = {
                        val id = selectedPlant!!.id
                        if (id in favoriteIds) favoriteIds.remove(id) else favoriteIds.add(id)
                    },
                )
                selectedGuidePlant != null -> PlantInfoDetail(plant = selectedGuidePlant!!, onBack = { selectedGuidePlant = null })
                tab == StoreTab.Guide -> PlantGuideScreen(catalog = catalog, onPlantClick = { selectedGuidePlant = it })
                tab == StoreTab.Home -> HomeScreen(
                    selectedCategory = selectedCategory,
                    catalog = catalog,
                    onCategoryChange = { selectedCategory = it },
                    onPlantClick = { selectedPlant = it },
                    onCart = { tab = StoreTab.Cart },
                    onSeeAll = { tab = StoreTab.Search },
                    onCare = { tab = StoreTab.Profile; profileSection = ProfileSection.Care },
                    onGuide = { tab = StoreTab.Guide },
                    favoriteIds = favoriteIds,
                    onToggleFavorite = { plant ->
                        if (plant.id in favoriteIds) favoriteIds.remove(plant.id) else favoriteIds.add(plant.id)
                    },
                )
                tab == StoreTab.Search -> SearchScreen(catalog = catalog, onPlantClick = { selectedPlant = it })
                tab == StoreTab.Cart -> CartScreen(
                    cart = cart.toList(),
                    onBackToShop = { tab = StoreTab.Home },
                    onCheckout = { checkout = true },
                    onRemove = { cart.remove(it) },
                )
                tab == StoreTab.Profile && profileSection != null -> ProfileSectionScreen(
                    section = profileSection!!,
                    order = lastOrder,
                    favoritePlants = catalog.filter { it.id in favoriteIds },
                    notificationsEnabled = notificationsEnabled,
                    tipsEnabled = tipsEnabled,
                    onBack = { profileSection = null },
                    onPlantClick = { selectedPlant = it },
                    onNotificationsChange = { notificationsEnabled = it },
                    onTipsChange = { tipsEnabled = it },
                    tourismInfo = tourismInfo,
                )
                else -> ProfileScreen(
                    favoriteCount = favoriteIds.size,
                    orderCount = orders.size,
                    onMenuSelect = { profileSection = it },
                    onLogout = { role = null; profileSection = null },
                )
            }
        }
    }
}

@Composable
private fun StaffConsole(
    role: AppRole,
    catalog: List<Plant>,
    orders: List<OrderSummary>,
    onLogout: () -> Unit,
    onSavePlant: (Plant) -> Unit,
    onDeletePlant: (Plant) -> Unit,
    onUpdateOrder: (Int, String) -> Unit,
    reports: List<PlantReport>,
    onAddReport: (PlantReport) -> Unit,
    onUpdateReport: (Int, String) -> Unit,
    onUpdatePlantHealth: (Int, String) -> Unit,
    tourismInfo: TourismInfo,
    onSaveTourismInfo: (TourismInfo) -> Unit,
) {
    var page by remember(role) { mutableStateOf(if (role == AppRole.Admin) StaffTab.Overview else StaffTab.PlantChecks) }
    var editingPlant by remember { mutableStateOf<Plant?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var reportTarget by remember { mutableStateOf<Pair<Plant?, String>?>(null) }
    var editTourism by remember { mutableStateOf(false) }
    var scanMessage by remember { mutableStateOf("") }
    val activity = LocalActivity.current

    Column(Modifier.fillMaxSize().background(Cream.copy(alpha = .82f)).padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("EcoPlant · Panel ${if (role == AppRole.Admin) "Admin" else "Petugas"}", color = Forest, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                Text("Halo, ${if (role == AppRole.Admin) "Admin" else "Petugas"} 🌱", color = Muted, fontSize = 12.sp)
            }
            TextButton(onClick = onLogout) { Text("Keluar", color = Color(0xFF9A554A), fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val availableTabs = if (role == AppRole.Admin) {
                    listOf(StaffTab.Overview, StaffTab.Products, StaffTab.Reports, StaffTab.TourismInfo, StaffTab.Orders)
                } else {
                    listOf(StaffTab.PlantChecks, StaffTab.Reports, StaffTab.Orders)
                }
                availableTabs.forEach { item ->
                    val label = when (item) {
                        StaffTab.Overview -> "Ringkasan"
                        StaffTab.Products -> "Produk"
                        StaffTab.Orders -> "Pesanan"
                        StaffTab.PlantChecks -> "Cek tanaman"
                        StaffTab.Reports -> "Laporan"
                        StaffTab.TourismInfo -> "Info wisata"
                    }
                    Surface(modifier = Modifier.clickable { page = item }, color = if (page == item) Forest else Color.White, shape = RoundedCornerShape(14.dp)) {
                        Text(label, color = if (page == item) Color.White else Muted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp))
                    }
                }
        }
        Spacer(Modifier.height(16.dp))

        when (page) {
            StaffTab.Overview -> {
                Text("Ringkasan EcoPlant", color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StaffStat("Produk", catalog.size.toString(), Modifier.weight(1f))
                    StaffStat("Pesanan", orders.size.toString(), Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StaffStat("Laporan", reports.count { it.status == "Menunggu ditinjau" }.toString(), Modifier.weight(1f))
                    StaffStat("Koleksi wisata", catalog.size.toString(), Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = { page = StaffTab.Products }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Forest), shape = RoundedCornerShape(14.dp)) { Text("Kelola data tanaman") }
                Button(onClick = { page = StaffTab.Reports }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Leaf), shape = RoundedCornerShape(14.dp)) { Text("Tinjau laporan petugas") }
                Button(onClick = { page = StaffTab.TourismInfo }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Forest), shape = RoundedCornerShape(14.dp)) { Text("Ubah informasi wisata") }
                Button(onClick = { page = StaffTab.Orders }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Leaf), shape = RoundedCornerShape(14.dp)) { Text("Lihat pesanan") }
                Text("Admin dapat menambah, mengubah, dan menghapus produk serta memantau pesanan.", color = Muted, fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.padding(top = 8.dp))
            }
            StaffTab.Products -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Katalog tanaman", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Button(onClick = { showAddDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Forest), shape = RoundedCornerShape(12.dp)) { Text("+ Tambah") }
                }
                Spacer(Modifier.height(10.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    items(catalog, key = { it.id }) { plant ->
                        Card(shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                PlantArtwork(plant, Modifier.size(65.dp))
                                Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                                    Text(plant.name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("${plant.scientificName} · ${plant.gardenArea}", color = Muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("Kondisi: ${plant.healthStatus}", color = if (plant.healthStatus == "Sehat") Leaf else Color(0xFFB5483D), fontSize = 10.sp)
                                }
                                Column {
                                    TextButton(onClick = { editingPlant = plant }) { Text("Edit", color = Leaf, fontSize = 11.sp) }
                                    TextButton(onClick = { onDeletePlant(plant) }) { Text("Hapus", color = Color(0xFF9A554A), fontSize = 11.sp) }
                                }
                            }
                        }
                    }
                }
            }
            StaffTab.Orders -> {
                Text(if (role == AppRole.Admin) "Semua pesanan" else "Pesanan masuk", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Text(if (role == AppRole.Admin) "Pantau aktivitas toko EcoPlant." else "Perbarui status pesanan pelanggan.", color = Muted, fontSize = 12.sp)
                if (role == AppRole.Officer) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val scannerActivity = activity
                            if (scannerActivity == null) {
                                scanMessage = "Pemindai tidak dapat dibuka dari layar ini."
                            } else {
                                val options = GmsBarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                    .enableAutoZoom()
                                    .build()
                                GmsBarcodeScanning.getClient(scannerActivity, options).startScan()
                                    .addOnSuccessListener { barcode ->
                                        val rawValue = barcode.rawValue?.trim().orEmpty()
                                        val orderNumber = rawValue.substringAfterLast('-').toIntOrNull()
                                        val order = orders.firstOrNull { it.orderNumber == orderNumber }
                                        scanMessage = if (order != null) {
                                            "Pesanan #${order.orderNumber.toString().padStart(4, '0')} · ${order.recipient} · ${order.status}"
                                        } else if (rawValue.isNotBlank()) {
                                            "QR terbaca: $rawValue. QR pesanan EcoPlant memakai format ECOPLANT-ORDER-0001."
                                        } else {
                                            "QR tidak berisi teks."
                                        }
                                    }
                                    .addOnCanceledListener { scanMessage = "Pemindaian dibatalkan." }
                                    .addOnFailureListener { scanMessage = "Pemindai belum tersedia. Periksa Google Play Services lalu coba lagi." }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Forest),
                        shape = RoundedCornerShape(14.dp),
                    ) { Text("▦  Scan QR Pesanan", fontWeight = FontWeight.Bold) }
                    Text("Pindai QR untuk melihat detail pesanan pelanggan.", color = Muted, fontSize = 11.sp)
                    if (scanMessage.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        InfoCard("Hasil pemindaian", scanMessage)
                    }
                }
                Spacer(Modifier.height(12.dp))
                if (orders.isEmpty()) {
                    EmptyProfileState("Belum ada pesanan", "Pesanan baru dari pembeli akan muncul di sini.", "📦")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(orders, key = { it.orderNumber }) { order ->
                            StaffOrderCard(order, canUpdate = role == AppRole.Officer, onUpdate = { status -> onUpdateOrder(order.orderNumber, status) })
                        }
                    }
                }
            }
            StaffTab.PlantChecks -> {
                Text("Pemeriksaan tanaman", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Text("Catat kondisi tanaman yang kamu temui di area wisata.", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(10.dp))
                Button(onClick = { reportTarget = null to "Usulan tanaman" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Leaf), shape = RoundedCornerShape(13.dp)) { Text("＋ Usulkan tanaman baru") }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    items(catalog, key = { "check-${it.id}" }) { plant ->
                        Card(shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                PlantArtwork(plant, Modifier.size(62.dp))
                                Column(Modifier.weight(1f).padding(horizontal = 10.dp)) {
                                    Text(plant.name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(plant.gardenArea, color = Muted, fontSize = 10.sp)
                                    Text("Kondisi: ${plant.healthStatus}", color = if (plant.healthStatus == "Sehat") Leaf else Color(0xFFB5483D), fontSize = 10.sp)
                                }
                                TextButton(onClick = { reportTarget = plant to "Kondisi tanaman" }) { Text("Laporkan", color = Forest, fontSize = 11.sp) }
                            }
                        }
                    }
                }
            }
            StaffTab.Reports -> {
                Text(if (role == AppRole.Admin) "Laporan petugas" else "Laporan masuk", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                if (reports.isEmpty()) EmptyProfileState("Belum ada laporan", "Laporan pemeriksaan dan usulan tanaman akan tampil di sini.", "📝")
                else LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    items(reports, key = { it.id }) { report ->
                        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(Modifier.fillMaxWidth().padding(14.dp)) {
                                Text(report.type, color = Forest, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(report.plantName + if (report.condition.isNotBlank()) " · ${report.condition}" else "", color = Ink, fontSize = 12.sp)
                                Text(report.description, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Status: ${report.status}", color = Leaf, fontSize = 11.sp, modifier = Modifier.weight(1f))
                                    if (role == AppRole.Admin && report.status == "Menunggu ditinjau") {
                                        TextButton(onClick = { onUpdateReport(report.id, "Ditindaklanjuti") }) { Text("Tindak lanjuti", color = Forest, fontSize = 10.sp) }
                                        TextButton(onClick = { onUpdateReport(report.id, "Ditolak") }) { Text("Tolak", color = Muted, fontSize = 10.sp) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            StaffTab.TourismInfo -> {
                Text("Informasi wisata", color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                InfoCard(tourismInfo.name, tourismInfo.description)
                InfoCard("Lokasi", tourismInfo.address)
                InfoCard("Jam kunjungan", tourismInfo.hours)
                InfoCard("Kontak", tourismInfo.contact)
                Button(onClick = { editTourism = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Forest), shape = RoundedCornerShape(14.dp)) { Text("Edit informasi wisata") }
            }
        }
    }

    if (showAddDialog || editingPlant != null) {
        ProductEditorDialog(
            plant = editingPlant,
            onDismiss = { showAddDialog = false; editingPlant = null },
            onSave = { onSavePlant(it); showAddDialog = false; editingPlant = null },
        )
    }
    if (reportTarget != null) {
        val (plant, type) = reportTarget!!
        PlantReportDialog(type, plant, onDismiss = { reportTarget = null }, onSubmit = { report ->
            onAddReport(report)
            if (plant != null && report.condition.isNotBlank()) onUpdatePlantHealth(plant.id, report.condition)
            reportTarget = null
        })
    }
    if (editTourism) TourismInfoEditorDialog(tourismInfo, onDismiss = { editTourism = false }, onSave = { onSaveTourismInfo(it); editTourism = false })
}

@Composable
private fun StaffStat(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(17.dp)).background(Mint).padding(16.dp)) {
        Text(value, color = Forest, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Text(title, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun PlantReportDialog(type: String, plant: Plant?, onDismiss: () -> Unit, onSubmit: (PlantReport) -> Unit) {
    var name by remember { mutableStateOf(plant?.name.orEmpty()) }
    var condition by remember { mutableStateOf("Perlu perhatian") }
    var details by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(type) }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(name, { name = it; error = "" }, label = { Text(if (plant == null) "Nama tanaman yang diusulkan" else "Nama tanaman") }, singleLine = true)
            if (plant != null) {
                Text("Pilih kondisi", color = Ink, fontWeight = FontWeight.SemiBold)
                listOf("Sehat", "Perlu perhatian", "Mati").forEach { option ->
                    Row(Modifier.fillMaxWidth().clickable { condition = option }, verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material3.RadioButton(selected = condition == option, onClick = { condition = option })
                        Text(option, color = Ink, fontSize = 13.sp)
                    }
                }
            }
            OutlinedTextField(details, { details = it; error = "" }, label = { Text(if (plant == null) "Alasan / lokasi yang disarankan" else "Catatan pemeriksaan") }, minLines = 2)
            if (error.isNotBlank()) Text(error, color = Color(0xFFB5483D), fontSize = 11.sp)
        }
    }, confirmButton = {
        TextButton(onClick = {
            if (name.isBlank() || details.isBlank()) error = "Isi nama tanaman dan keterangan terlebih dahulu."
            else onSubmit(PlantReport(System.currentTimeMillis().toInt(), type, plant?.id, name.trim(), if (plant == null) "" else condition, details.trim()))
        }) { Text("Kirim laporan", color = Forest) }
    }, dismissButton = { TextButton(onClick = onDismiss) { Text("Batal", color = Muted) } })
}

@Composable
private fun TourismInfoEditorDialog(info: TourismInfo, onDismiss: () -> Unit, onSave: (TourismInfo) -> Unit) {
    var name by remember { mutableStateOf(info.name) }
    var description by remember { mutableStateOf(info.description) }
    var address by remember { mutableStateOf(info.address) }
    var hours by remember { mutableStateOf(info.hours) }
    var contact by remember { mutableStateOf(info.contact) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Edit informasi wisata") }, text = {
        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Nama wisata") })
            OutlinedTextField(description, { description = it }, label = { Text("Deskripsi") }, minLines = 2)
            OutlinedTextField(address, { address = it }, label = { Text("Alamat") })
            OutlinedTextField(hours, { hours = it }, label = { Text("Jam kunjungan") })
            OutlinedTextField(contact, { contact = it }, label = { Text("Kontak") })
        }
    }, confirmButton = { TextButton(onClick = { onSave(TourismInfo(name, description, address, hours, contact)) }) { Text("Simpan", color = Forest) } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Batal", color = Muted) } })
}

@Composable
private fun PlantGuideScreen(catalog: List<Plant>, onPlantClick: (Plant) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Wisata tanaman", color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
            Text("Kenali nama, ciri, dan lokasi koleksi EcoPlant.", color = StoreMuted, fontSize = 12.sp)
        }
        items(catalog, key = { "guide-${it.id}" }) { plant ->
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(StoreCard).clickable { onPlantClick(plant) }.padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
                PlantArtwork(plant, Modifier.size(82.dp))
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(plant.name, color = StoreText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(plant.scientificName, color = StoreMuted, fontSize = 11.sp)
                    Text(plant.gardenArea, color = Color(0xFFCBE5C8), fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
                Text("›", color = StoreMuted, fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun PlantInfoDetail(plant: Plant, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("‹  Kembali ke koleksi", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onBack() }.padding(vertical = 8.dp))
        PlantArtwork(plant, Modifier.fillMaxWidth().height(300.dp).padding(top = 10.dp))
        Spacer(Modifier.height(18.dp))
        Text(plant.name, color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Text(plant.scientificName, color = Color(0xFFCBE5C8), fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        Spacer(Modifier.height(16.dp))
        InfoCard("Tentang tanaman", plant.description)
        InfoCard("Lokasi koleksi", plant.gardenArea)
        InfoCard("Kondisi terakhir", plant.healthStatus)
        InfoCard("Cara perawatan", plant.careTips)
    }
}

@Composable
private fun StaffOrderCard(order: OrderSummary, canUpdate: Boolean, onUpdate: (String) -> Unit) {
    val nextStatus = when (order.status) {
        "Menunggu diproses" -> "Sedang disiapkan"
        "Sedang disiapkan" -> "Dalam pengiriman"
        "Dalam pengiriman" -> "Selesai"
        else -> null
    }
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.fillMaxWidth().padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Pesanan #${order.orderNumber.toString().padStart(4, '0')}", color = Ink, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(order.status, color = Leaf, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(6.dp))
            Text("${order.recipient} · ${order.plants.size} tanaman", color = Muted, fontSize = 12.sp)
            Text(order.address, color = Muted, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text("Total Rp ${order.total.rupiah()}", color = Forest, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
            if (canUpdate && nextStatus != null) {
                Spacer(Modifier.height(10.dp))
                Button(onClick = { onUpdate(nextStatus) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Forest), shape = RoundedCornerShape(12.dp)) {
                    Text(when (nextStatus) {
                        "Sedang disiapkan" -> "Mulai proses pesanan"
                        "Dalam pengiriman" -> "Tandai sedang dikirim"
                        else -> "Tandai pesanan selesai"
                    })
                }
            }
        }
    }
}

@Composable
private fun ProductEditorDialog(plant: Plant?, onDismiss: () -> Unit, onSave: (Plant) -> Unit) {
    var name by remember(plant) { mutableStateOf(plant?.name.orEmpty()) }
    var price by remember(plant) { mutableStateOf(plant?.price?.toString().orEmpty()) }
    var category by remember(plant) { mutableStateOf(plant?.category ?: "Indoor") }
    var scientificName by remember(plant) { mutableStateOf(plant?.scientificName.orEmpty()) }
    var description by remember(plant) { mutableStateOf(plant?.description.orEmpty()) }
    var gardenArea by remember(plant) { mutableStateOf(plant?.gardenArea ?: "Area koleksi tanaman") }
    var careTips by remember(plant) { mutableStateOf(plant?.careTips ?: "Rawat sesuai kebutuhan cahaya dan airnya.") }
    var error by remember(plant) { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (plant == null) "Tambah tanaman" else "Edit tanaman") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it; error = "" }, label = { Text("Nama tanaman") }, singleLine = true)
                OutlinedTextField(scientificName, { scientificName = it }, label = { Text("Nama ilmiah") }, singleLine = true)
                OutlinedTextField(description, { description = it }, label = { Text("Deskripsi wisata") }, minLines = 2)
                OutlinedTextField(gardenArea, { gardenArea = it }, label = { Text("Area koleksi") }, singleLine = true)
                OutlinedTextField(careTips, { careTips = it }, label = { Text("Tips perawatan") }, minLines = 2)
                OutlinedTextField(price, { price = it.filter(Char::isDigit); error = "" }, label = { Text("Harga toko (angka)") }, singleLine = true)
                OutlinedTextField(category, { category = it; error = "" }, label = { Text("Kategori, contoh: Indoor") }, singleLine = true)
                if (error.isNotBlank()) Text(error, color = Color(0xFFB5483D), fontSize = 11.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val parsedPrice = price.toIntOrNull()
                if (name.isBlank() || parsedPrice == null || category.isBlank()) error = "Lengkapi nama, harga, dan kategori."
                else onSave(Plant(plant?.id ?: 0, name.trim(), plant?.subtitle ?: "Tanaman hias EcoPlant", parsedPrice, category.trim(), plant?.photoRes ?: plants.first().photoRes, plant?.rating ?: "5.0", scientificName.trim(), description.trim(), gardenArea.trim(), careTips.trim(), plant?.healthStatus ?: "Sehat"))
            }) { Text("Simpan", color = Forest) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal", color = Muted) } },
    )
}

@Composable
private fun HomeScreen(
    selectedCategory: String,
    catalog: List<Plant>,
    onCategoryChange: (String) -> Unit,
    onPlantClick: (Plant) -> Unit,
    onCart: () -> Unit,
    onSeeAll: () -> Unit,
    onCare: () -> Unit,
    onGuide: () -> Unit,
    favoriteIds: List<Int>,
    onToggleFavorite: (Plant) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 22.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("EcoPlant", color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Tanam bahagia, hidup lebih hijau", color = StoreMuted, fontSize = 12.sp)
                }
                Surface(shape = CircleShape, color = StoreCard, modifier = Modifier.size(44.dp).clickable { onCart() }) {
                    Box(contentAlignment = Alignment.Center) { Text("🛒", fontSize = 19.sp) }
                }
            }
        }
        item { HeroBanner(onExplore = onGuide) }
        item {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(StoreCard).clickable { onGuide() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🌿", fontSize = 28.sp)
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text("Jelajahi wisata tanaman", color = StoreText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Baca cerita dan info koleksi", color = StoreMuted, fontSize = 11.sp)
                }
                Text("→", color = Color(0xFFCBE5C8), fontSize = 20.sp)
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Temukan tanaman", style = MaterialTheme.typography.titleLarge, color = StoreText, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("Lihat semua  →", color = Color(0xFFB9D8B8), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onSeeAll() }.padding(6.dp))
            }
        }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Semua", "Indoor", "Sukulen", "Berbunga").forEach { label ->
                    val selected = selectedCategory == label
                    Surface(shape = RoundedCornerShape(20.dp), color = if (selected) Color(0xFFDCEBDD) else StoreSurface, modifier = Modifier.clickable { onCategoryChange(label) }) {
                        Text(label, color = if (selected) Forest else StoreMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp))
                    }
                }
            }
        }
        item {
            val visiblePlants = when (selectedCategory) {
                "Semua" -> catalog
                else -> catalog.filter { it.category == selectedCategory }
            }
            ProductGrid(visiblePlants, onPlantClick, favoriteIds, onToggleFavorite)
        }
        item { CareTip(onClick = onCare) }
    }
}

@Composable
private fun HeroBanner(onExplore: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF123C2D), Color(0xFF2D714B)))).padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Wisata Kebun Bandung", color = Color(0xFFCBE3CC), fontSize = 15.sp, fontWeight = FontWeight.Bold, lineHeight = 19.sp)
            Text("Selamat datang!", color = Color.White, fontSize = 26.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Yuk, kenali tanaman dan cerita kebun kami.", color = Color.White.copy(alpha = .84f), fontSize = 11.sp)
            Spacer(Modifier.height(14.dp))
            Surface(color = Color.White, shape = RoundedCornerShape(20.dp)) {
                Text("Jelajahi wisata  →", color = Forest, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.clickable { onExplore() }.padding(horizontal = 14.dp, vertical = 9.dp))
            }
        }
        Box(Modifier.size(112.dp).clip(CircleShape).background(Color.White.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
            Text("🌿", fontSize = 58.sp)
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<Plant>,
    onPlantClick: (Plant) -> Unit,
    favoriteIds: List<Int>,
    onToggleFavorite: (Plant) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        products.chunked(2).forEach { rowPlants ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowPlants.forEach { plant ->
                    ProductCard(plant, Modifier.weight(1f), plant.id in favoriteIds, onPlantClick) { onToggleFavorite(plant) }
                }
                if (rowPlants.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ProductCard(
    plant: Plant,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onClick: (Plant) -> Unit,
    onToggleFavorite: () -> Unit = {},
) {
    Card(
        modifier = modifier.clickable { onClick(plant) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StoreSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(Modifier.padding(10.dp)) {
            PlantArtwork(plant, Modifier.fillMaxWidth().height(122.dp), isFavorite, onToggleFavorite, showFavorite = true)
            Spacer(Modifier.height(10.dp))
            Text(plant.name, color = StoreText, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(plant.subtitle, color = StoreMuted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("★ ${plant.rating}", color = Color(0xFFF2CF72), fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun PlantArtwork(
    plant: Plant,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    showFavorite: Boolean = false,
) {
    Box(modifier.clip(RoundedCornerShape(15.dp)).background(Brush.linearGradient(listOf(Color(0xFF4A8A5B), Color(0xFF173F30)))), contentAlignment = Alignment.Center) {
        PlantPhoto(plant)
        if (showFavorite) {
            Surface(
                color = Color.White.copy(alpha = .94f),
                shape = CircleShape,
                modifier = Modifier.align(Alignment.TopEnd).padding(7.dp).size(30.dp).clickable { onToggleFavorite() },
            ) {
                Box(contentAlignment = Alignment.Center) { Text(if (isFavorite) "♥" else "♡", color = if (isFavorite) Color(0xFFD36B66) else Forest, fontSize = 17.sp) }
            }
        }
        Surface(color = Color.White.copy(alpha = .9f), shape = RoundedCornerShape(12.dp), modifier = Modifier.align(Alignment.BottomStart).padding(7.dp)) {
            Text("${plant.category}", color = Forest, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
        }
    }
}

@Composable
private fun PlantPhoto(plant: Plant) {
    Image(
        painter = painterResource(plant.photoRes),
        contentDescription = plant.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun CareTip(onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(StoreCard).clickable { onClick() }.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("☀️", fontSize = 25.sp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text("Tips merawat tanaman", color = StoreText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Kenali kebutuhan cahaya dan air tanamanmu.", color = StoreMuted, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SearchScreen(catalog: List<Plant>, onPlantClick: (Plant) -> Unit) {
    var query by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp)) {
        Text("Cari tanaman", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = StoreText)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp), colors = storeInputColors(), placeholder = { Text("Nama tanaman atau kategori", color = StoreMuted) }, leadingIcon = { Text("⌕", color = StoreMuted, fontSize = 24.sp) })
        Spacer(Modifier.height(18.dp))
        Text("${catalog.count { it.name.contains(query, true) || it.category.contains(query, true) }} tanaman ditemukan", color = StoreMuted, fontSize = 12.sp)
        Spacer(Modifier.height(10.dp))
        val results = catalog.filter { it.name.contains(query, true) || it.category.contains(query, true) }
        if (results.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(top = 42.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌱", fontSize = 42.sp)
                Text("Tanaman belum ditemukan", color = StoreText, fontWeight = FontWeight.Bold)
                Text("Coba kata kunci yang lain, ya.", color = StoreMuted, fontSize = 12.sp)
            }
        } else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(results) { plant ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(StoreSurface).clickable { onPlantClick(plant) }.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    PlantArtwork(plant, Modifier.size(74.dp))
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(plant.name, color = StoreText, fontWeight = FontWeight.Bold)
                        Text(plant.subtitle, color = StoreMuted, fontSize = 11.sp)
                        Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("›", color = Leaf, fontSize = 26.sp)
                }
            }
        }
    }
}

@Composable
private fun PlantDetail(plant: Plant, onBack: () -> Unit, onAdd: () -> Unit, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("‹   Kembali", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onBack() }.padding(vertical = 8.dp))
        PlantArtwork(plant, Modifier.fillMaxWidth().weight(1f).padding(vertical = 14.dp), isFavorite, onToggleFavorite, showFavorite = true)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(plant.name, color = StoreText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text(plant.subtitle, color = StoreMuted, fontSize = 12.sp)
            }
            Text("★ ${plant.rating}", color = Color(0xFFF2CF72), fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Text("Tanaman pilihan untuk membuat ruangan terasa lebih segar. Rawat dengan penyiraman secukupnya dan letakkan di area dengan cahaya yang sesuai.", color = StoreMuted, fontSize = 13.sp, lineHeight = 20.sp)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            Button(onClick = onAdd, shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Leaf)) { Text("Tambah ke keranjang") }
        }
    }
}

@Composable
private fun CartScreen(cart: List<Plant>, onBackToShop: () -> Unit, onCheckout: () -> Unit, onRemove: (Plant) -> Unit) {
    val total = cart.sumOf { it.price }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp)) {
        Text("Keranjang", color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("${cart.size} tanaman pilihanmu", color = StoreMuted, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        if (cart.isEmpty()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("🪴", fontSize = 72.sp)
                Text("Keranjangmu masih kosong", color = StoreText, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("Yuk temukan tanaman yang cocok untukmu.", color = StoreMuted, fontSize = 12.sp)
                Spacer(Modifier.height(14.dp))
                Button(onClick = onBackToShop, colors = ButtonDefaults.buttonColors(containerColor = Leaf)) { Text("Jelajahi tanaman") }
            }
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(cart) { plant ->
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(StoreSurface).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        PlantArtwork(plant, Modifier.size(78.dp))
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(plant.name, color = StoreText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        TextButton(onClick = { onRemove(plant) }) { Text("Hapus", color = Color(0xFF9A554A), fontSize = 11.sp) }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Subtotal", color = StoreMuted); Text("Rp ${total.rupiah()}", color = StoreText, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Leaf)) { Text("Lanjut checkout  →", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun CheckoutScreen(cart: List<Plant>, onBack: () -> Unit, onPlaceOrder: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val total = cart.sumOf { it.price }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("‹   Kembali ke keranjang", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onBack() })
        Spacer(Modifier.height(18.dp))
        Text("Pengiriman", color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("Isi alamat tujuan untuk pesananmu.", color = StoreMuted, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(name, { name = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Nama penerima") }, colors = storeInputColors(), shape = RoundedCornerShape(14.dp))
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(address, { address = it }, modifier = Modifier.fillMaxWidth(), minLines = 3, label = { Text("Alamat lengkap") }, colors = storeInputColors(), shape = RoundedCornerShape(14.dp))
        Spacer(Modifier.height(20.dp))
        Text("Ringkasan pesanan", color = StoreText, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        cart.forEach { plant ->
            Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(plant.name, color = StoreMuted, fontSize = 12.sp)
                Text("Rp ${plant.price.rupiah()}", color = StoreText, fontSize = 12.sp)
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Mint)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total", color = StoreText, fontWeight = FontWeight.Bold); Text("Rp ${total.rupiah()}", color = Color(0xFFCBE5C8), fontWeight = FontWeight.ExtraBold) }
        Spacer(Modifier.height(18.dp))
        Button(onClick = { onPlaceOrder(name.trim(), address.trim()) }, enabled = name.isNotBlank() && address.isNotBlank() && cart.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Leaf)) { Text("Buat pesanan") }
    }
}

@Composable
private fun OrderConfirmation(onDone: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(112.dp)) { Box(contentAlignment = Alignment.Center) { Text("✓", color = Forest, fontSize = 58.sp, fontWeight = FontWeight.Light) } }
        Spacer(Modifier.height(20.dp))
        Text("Pesanan dibuat!", color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Text("Terima kasih sudah memilih tanaman dari EcoPlant. Detail pesananmu siap ditinjau.", color = StoreMuted, fontSize = 13.sp, lineHeight = 20.sp)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onDone, colors = ButtonDefaults.buttonColors(containerColor = Leaf), shape = RoundedCornerShape(15.dp)) { Text("Kembali ke beranda") }
    }
}

@Composable
private fun ProfileScreen(
    favoriteCount: Int,
    orderCount: Int,
    onMenuSelect: (ProfileSection) -> Unit,
    onLogout: () -> Unit,
) {
    val page = Cream.copy(alpha = .82f)
    val title = Ink
    val muted = Muted
    val accent = Forest

    Column(
        Modifier.fillMaxSize().background(page).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Text("Profil", color = title, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)

        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(25.dp))
                .background(Brush.horizontalGradient(listOf(Forest, Leaf)))
                .padding(17.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = .72f), modifier = Modifier.size(62.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("🌿", fontSize = 31.sp) }
                }
                Column(Modifier.padding(start = 13.dp)) {
                    Text("Halo, Sahabat EcoPlant 👋", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(3.dp))
                    Text("Selamat datang di Wisata Kebun Bandung", color = Color.White.copy(alpha = .82f), fontSize = 11.sp, lineHeight = 15.sp)
                }
            }
            Spacer(Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileStat("▣", orderCount.toString(), "Pesanan", Modifier.weight(1f), title, accent)
                ProfileStat("♥", favoriteCount.toString(), "Tanaman favorit", Modifier.weight(1f), title, accent)
            }
        }

        Text("Akses cepat", color = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileQuickCard("▣", "Pesanan saya", "Lihat pesananmu", Modifier.weight(1f), title, muted, accent) {
                onMenuSelect(ProfileSection.Orders)
            }
            ProfileQuickCard("♥", "Favorit", "Tanaman tersimpan", Modifier.weight(1f), title, muted, accent) {
                onMenuSelect(ProfileSection.Favorites)
            }
        }

        Text("Preferensi", color = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        ProfileMenuCard {
            ProfileMenuRow("⌖", "Alamat pengiriman", "Kelola alamat pesanan", title, muted, accent) { onMenuSelect(ProfileSection.Address) }
            HorizontalDivider(color = Mint)
            ProfileMenuRow("♧", "Notifikasi", "Atur pengingat tanaman", title, muted, accent) { onMenuSelect(ProfileSection.Settings) }
        }

        Text("Info & bantuan", color = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        ProfileMenuCard {
            ProfileMenuRow("?", "Tips perawatan", "Panduan merawat tanaman", title, muted, accent) { onMenuSelect(ProfileSection.Care) }
            HorizontalDivider(color = Mint)
            ProfileMenuRow("i", "Tentang wisata kebun", "Lokasi dan informasi EcoPlant", title, muted, accent) { onMenuSelect(ProfileSection.TourismInfo) }
        }

        Surface(
            modifier = Modifier.fillMaxWidth().clickable { onLogout() },
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 2.dp,
        ) {
            Row(Modifier.padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("↪", color = accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("  Keluar dari akun", color = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text("EcoPlant · rawat hijau, rawat bumi", color = muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp))
    }
}

@Composable
private fun ProfileStat(icon: String, value: String, label: String, modifier: Modifier, title: Color, accent: Color) {
    Surface(modifier, shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha = .94f)) {
        Row(Modifier.padding(horizontal = 11.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(31.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(icon, color = accent, fontSize = 15.sp, fontWeight = FontWeight.Bold) }
            }
            Column(Modifier.padding(start = 8.dp)) {
                Text(value, color = title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Text(label, color = Muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun ProfileQuickCard(icon: String, heading: String, description: String, modifier: Modifier, title: Color, muted: Color, accent: Color, onClick: () -> Unit) {
    Surface(modifier.clickable { onClick() }, shape = RoundedCornerShape(19.dp), color = Color.White, shadowElevation = 2.dp) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(34.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(icon, color = accent, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            }
            Column(Modifier.weight(1f).padding(start = 9.dp)) {
                Text(heading, color = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(description, color = muted, fontSize = 10.sp, lineHeight = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Text("›", color = accent, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileMenuCard(content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(19.dp), color = Color.White, shadowElevation = 2.dp) {
        Column(Modifier.padding(horizontal = 13.dp)) { content() }
    }
}

@Composable
private fun ProfileMenuRow(icon: String, heading: String, description: String, title: Color, muted: Color, accent: Color, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = Mint, modifier = Modifier.size(34.dp)) {
            Box(contentAlignment = Alignment.Center) { Text(icon, color = accent, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        }
        Column(Modifier.weight(1f).padding(start = 11.dp)) {
            Text(heading, color = title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(description, color = muted, fontSize = 10.sp)
        }
        Text("›", color = accent, fontSize = 21.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileSectionScreen(
    section: ProfileSection,
    order: OrderSummary?,
    favoritePlants: List<Plant>,
    notificationsEnabled: Boolean,
    tipsEnabled: Boolean,
    onBack: () -> Unit,
    onPlantClick: (Plant) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onTipsChange: (Boolean) -> Unit,
    tourismInfo: TourismInfo,
) {
    val title = when (section) {
        ProfileSection.Orders -> "Pesanan saya"
        ProfileSection.Address -> "Alamat pengiriman"
        ProfileSection.Favorites -> "Tanaman favorit"
        ProfileSection.Care -> "Tips perawatan"
        ProfileSection.Settings -> "Pengaturan"
        ProfileSection.TourismInfo -> "Informasi wisata EcoPlant"
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("‹   Kembali ke profil", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onBack() }.padding(vertical = 8.dp))
        Spacer(Modifier.height(12.dp))
        Text(title, color = StoreText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        when (section) {
            ProfileSection.Orders -> {
                if (order == null) EmptyProfileState("Belum ada pesanan", "Pesanan yang kamu buat akan tampil di sini.", "📦", dark = true)
                else {
                    InfoCard("Pesanan EcoPlant", "${order.plants.size} tanaman · Rp ${order.total.rupiah()}")
                    order.plants.forEach { plant ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(plant.name, color = StoreText, fontSize = 13.sp)
                            Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Text("Status: pesanan diterima", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("Kode QR pesanan: ECOPLANT-ORDER-${order.orderNumber.toString().padStart(4, '0')}", color = StoreMuted, fontSize = 11.sp)
                }
            }
            ProfileSection.Address -> {
                if (order == null) EmptyProfileState("Belum ada alamat", "Alamat tersimpan setelah kamu membuat pesanan pertama.", "📍", dark = true)
                else InfoCard(order.recipient, order.address)
            }
            ProfileSection.Favorites -> {
                if (favoritePlants.isEmpty()) EmptyProfileState("Belum ada favorit", "Tekan ikon hati pada tanaman untuk menyimpannya.", "♡", dark = true)
                favoritePlants.forEach { plant ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp).clip(RoundedCornerShape(16.dp)).background(StoreSurface).clickable { onPlantClick(plant) }.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        PlantArtwork(plant, Modifier.size(68.dp))
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(plant.name, color = StoreText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Rp ${plant.price.rupiah()}", color = Color(0xFFCBE5C8), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                        Text("›", color = StoreMuted, fontSize = 22.sp)
                    }
                }
            }
            ProfileSection.Care -> {
                listOf(
                    "☀️ Cahaya" to "Letakkan tanaman sesuai kebutuhan cahayanya. Hindari matahari langsung untuk tanaman indoor yang sensitif.",
                    "💧 Penyiraman" to "Periksa kelembapan tanah sebelum menyiram. Kebanyakan air bisa membuat akar tanaman membusuk.",
                    "🍃 Daun sehat" to "Bersihkan daun dari debu secara rutin agar tanaman dapat menyerap cahaya dengan baik.",
                ).forEach { (heading, body) -> InfoCard(heading, body) }
            }
            ProfileSection.Settings -> {
                SettingRow("Pengingat tanaman", "Ingatkan jadwal perawatan", notificationsEnabled, onNotificationsChange)
                HorizontalDivider(color = Mint)
                SettingRow("Tips EcoPlant", "Tampilkan tips perawatan", tipsEnabled, onTipsChange)
            }
            ProfileSection.TourismInfo -> {
                InfoCard(tourismInfo.name, tourismInfo.description)
                InfoCard("Lokasi wisata", tourismInfo.address)
                InfoCard("Jam kunjungan", tourismInfo.hours)
                InfoCard("Kontak", tourismInfo.contact)
                Text("Lihat juga koleksi tanaman melalui tab Wisata.", color = StoreMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
            }
        }
    }
}

@Composable
private fun EmptyProfileState(title: String, body: String, symbol: String, dark: Boolean = false) {
    Column(Modifier.fillMaxWidth().padding(top = 34.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(symbol, fontSize = 48.sp)
        Spacer(Modifier.height(8.dp))
        Text(title, color = if (dark) StoreText else Ink, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(body, color = if (dark) StoreMuted else Muted, fontSize = 12.sp)
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp).clip(RoundedCornerShape(16.dp)).background(StoreSurface).padding(16.dp)) {
        Text(title, color = Color(0xFFCBE5C8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(5.dp))
        Text(body, color = StoreMuted, fontSize = 12.sp, lineHeight = 18.sp)
    }
}

@Composable
private fun SettingRow(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = StoreText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(description, color = StoreMuted, fontSize = 11.sp)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun StoreNavigation(tab: StoreTab, cartCount: Int, onSelect: (StoreTab) -> Unit) {
    Surface(color = DeepForest, shadowElevation = 12.dp) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            NavigationItem("⌂", "Beranda", tab == StoreTab.Home) { onSelect(StoreTab.Home) }
            NavigationItem("⌕", "Cari", tab == StoreTab.Search) { onSelect(StoreTab.Search) }
            NavigationItem("♧", "Wisata", tab == StoreTab.Guide) { onSelect(StoreTab.Guide) }
            NavigationItem(if (cartCount > 0) "🛒 $cartCount" else "🛒", "Keranjang", tab == StoreTab.Cart) { onSelect(StoreTab.Cart) }
            NavigationItem("◉", "Profil", tab == StoreTab.Profile) { onSelect(StoreTab.Profile) }
        }
    }
}

@Composable
private fun NavigationItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(Modifier.clip(RoundedCornerShape(14.dp)).background(if (selected) StoreCard else Color.Transparent).clickable { onClick() }.padding(horizontal = 12.dp, vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, color = if (selected) Color(0xFFDCEBDD) else StoreMuted, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Text(label, color = if (selected) Color(0xFFDCEBDD) else StoreMuted, fontSize = 9.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun storeInputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = StoreText,
    unfocusedTextColor = StoreText,
    focusedContainerColor = StoreSurface,
    unfocusedContainerColor = StoreSurface,
    focusedBorderColor = Color(0xFFB9D8B8),
    unfocusedBorderColor = StoreMuted,
    focusedLabelColor = Color(0xFFB9D8B8),
    unfocusedLabelColor = StoreMuted,
    cursorColor = Color(0xFFB9D8B8),
)

private fun Int.rupiah(): String = toString().reversed().chunked(3).joinToString(".").reversed()

