<?php
declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(204);
    exit;
}

function reply(int $status, array $payload): never
{
    http_response_code($status);
    echo json_encode($payload, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
    exit;
}

function requestBody(): array
{
    $raw = file_get_contents('php://input');
    if ($raw === false || trim($raw) === '') return [];
    $decoded = json_decode($raw, true);
    if (!is_array($decoded)) reply(400, ['error' => 'Body JSON tidak valid.']);
    return $decoded;
}

function initialStore(): array
{
    $plants = [
        ['id'=>1,'name'=>'Monstera Deliciosa','subtitle'=>'Tanaman hias · pot 14 cm','price'=>85000,'category'=>'Indoor','imageKey'=>'plant_monstera','rating'=>'4.9','scientificName'=>'Monstera deliciosa','description'=>'Monstera dikenal dengan daun besar berlubang yang membantu tanaman menangkap cahaya di habitat tropis.','gardenArea'=>'Rumah Kaca Tropis','careTips'=>'Sukai cahaya terang tidak langsung dan tanah yang lembap, bukan becek.','healthStatus'=>'Sehat'],
        ['id'=>2,'name'=>'Lidah Mertua','subtitle'=>'Mudah dirawat · pot 12 cm','price'=>45000,'category'=>'Indoor','imageKey'=>'plant_snake','rating'=>'4.9','scientificName'=>'Dracaena trifasciata','description'=>'Tanaman berdaun tegak dengan corak hijau. Tahan kondisi kering dan cocok untuk koleksi dalam ruang.','gardenArea'=>'Taman Tanaman Tangguh','careTips'=>'Letakkan di area terang atau teduh dan siram setelah tanah mengering.','healthStatus'=>'Sehat'],
        ['id'=>3,'name'=>'Peace Lily','subtitle'=>'Penyegar ruangan · pot 14 cm','price'=>68000,'category'=>'Indoor','imageKey'=>'plant_peace_lily','rating'=>'4.9','scientificName'=>'Spathiphyllum','description'=>'Peace Lily memiliki bunga putih khas dan daun hijau mengilap. Tanaman ini menyukai lingkungan yang lembap.','gardenArea'=>'Taman Teduh','careTips'=>'Tempatkan di cahaya tidak langsung dan jaga media tetap sedikit lembap.','healthStatus'=>'Sehat'],
        ['id'=>4,'name'=>'Kaktus Mini','subtitle'=>'Suka cahaya matahari','price'=>25000,'category'=>'Sukulen','imageKey'=>'plant_cactus','rating'=>'4.9','scientificName'=>'Cactaceae','description'=>'Kaktus menyimpan air pada batangnya dan memiliki duri sebagai bentuk adaptasi di lingkungan kering.','gardenArea'=>'Taman Kering','careTips'=>'Berikan banyak cahaya dan siram sedikit saat media sudah kering.','healthStatus'=>'Sehat'],
        ['id'=>5,'name'=>'Mawar Mini','subtitle'=>'Bunga cantik · pot 12 cm','price'=>55000,'category'=>'Berbunga','imageKey'=>'plant_rose','rating'=>'4.9','scientificName'=>'Rosa','description'=>'Mawar mini adalah tanaman berbunga dengan kelopak berlapis dan aroma khas.','gardenArea'=>'Taman Bunga','careTips'=>'Butuh cahaya matahari cukup, penyiraman teratur, dan pemangkasan bunga layu.','healthStatus'=>'Sehat'],
        ['id'=>6,'name'=>'Anggrek Bulan','subtitle'=>'Bunga tahan lama · pot 14 cm','price'=>95000,'category'=>'Berbunga','imageKey'=>'plant_orchid','rating'=>'4.9','scientificName'=>'Phalaenopsis','description'=>'Anggrek bulan memiliki bunga lebar yang tumbuh pada tangkai melengkung dan dapat mekar cukup lama.','gardenArea'=>'Rumah Anggrek','careTips'=>'Gunakan media berpori, cahaya terang tidak langsung, dan hindari air menggenang.','healthStatus'=>'Sehat'],
        ['id'=>7,'name'=>'Kalanchoe','subtitle'=>'Bunga mini warna cerah','price'=>39000,'category'=>'Berbunga','imageKey'=>'plant_kalanchoe','rating'=>'4.9','scientificName'=>'Kalanchoe blossfeldiana','description'=>'Kalanchoe adalah sukulen berbunga dengan kumpulan bunga kecil dan daun berdaging.','gardenArea'=>'Taman Bunga Mini','careTips'=>'Letakkan di tempat terang dan siram saat media tanam mengering.','healthStatus'=>'Sehat'],
        ['id'=>8,'name'=>'Lavender','subtitle'=>'Aroma lembut · area cerah','price'=>62000,'category'=>'Berbunga','imageKey'=>'plant_lavender','rating'=>'4.9','scientificName'=>'Lavandula','description'=>'Lavender dikenal dari bunga ungu dan aromanya. Bunga keringnya sering digunakan sebagai pewangi.','gardenArea'=>'Taman Aroma','careTips'=>'Sukai sinar matahari dan tanah dengan drainase baik; jangan terlalu sering disiram.','healthStatus'=>'Sehat'],
    ];
    return [
        'plants'=>$plants,
        'tourism'=>['name'=>'Wisata Kebun Bandung','description'=>'Jelajahi koleksi tanaman dan nikmati suasana hijau bersama keluarga.','address'=>'Alamat lokasi wisata','hours'=>'Setiap hari · 08.00–17.00','contact'=>'Kontak pengelola'],
        'reports'=>[],
        'orders'=>[],
        'next'=>['plant'=>9,'report'=>1,'order'=>1],
    ];
}

function saveStore($handle, array $store): void
{
    rewind($handle);
    ftruncate($handle, 0);
    fwrite($handle, json_encode($store, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT));
    fflush($handle);
}

$expectedToken = getenv('ECOPLANT_API_TOKEN') ?: '';
if ($expectedToken !== '' && !hash_equals($expectedToken, str_replace('Bearer ', '', $_SERVER['HTTP_AUTHORIZATION'] ?? ''))) {
    reply(401, ['error'=>'Token API tidak valid atau belum dikirim.']);
}

$storageDir = dirname(__DIR__) . DIRECTORY_SEPARATOR . 'storage';
if (!is_dir($storageDir) && !mkdir($storageDir, 0775, true) && !is_dir($storageDir)) {
    reply(500, ['error'=>'Folder penyimpanan backend tidak dapat dibuat.']);
}
$handle = fopen($storageDir . DIRECTORY_SEPARATOR . 'data.json', 'c+');
if ($handle === false || !flock($handle, LOCK_EX)) reply(500, ['error'=>'Penyimpanan backend tidak dapat dibuka.']);
$rawStore = stream_get_contents($handle);
$store = is_string($rawStore) && trim($rawStore) !== '' ? json_decode($rawStore, true) : null;
$initialized = !is_array($store);
if ($initialized) $store = initialStore();

$method = $_SERVER['REQUEST_METHOD'];
$path = parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
$path = '/' . trim((string)preg_replace('#^/api#', '', $path), '/');
$body = requestBody();
$status = 200;
$result = null;
$changed = false;

if ($path === '/health' && $method === 'GET') {
    $result = ['ok'=>true,'service'=>'EcoPlant PHP API','storage'=>'temporary-json'];
} elseif ($path === '/plants' && $method === 'GET') {
    $result = ['data'=>array_values($store['plants'])];
} elseif ($path === '/plants' && $method === 'POST') {
    if (trim((string)($body['name'] ?? '')) === '') reply(422, ['error'=>'Nama tanaman wajib diisi.']);
    $id = (int)$store['next']['plant']++;
    $plant = array_merge(['id'=>$id,'subtitle'=>'Tanaman wisata EcoPlant','price'=>0,'category'=>'Indoor','imageKey'=>'plant_monstera','rating'=>'5.0','scientificName'=>'','description'=>'','gardenArea'=>'Area koleksi tanaman','careTips'=>'','healthStatus'=>'Sehat'], $body, ['id'=>$id]);
    $store['plants'][] = $plant; $result = ['data'=>$plant]; $status = 201; $changed = true;
} elseif (preg_match('#^/plants/(\d+)$#', $path, $match) && $method === 'PUT') {
    $id = (int)$match[1]; $found = false;
    foreach ($store['plants'] as $index=>$plant) if ((int)$plant['id'] === $id) { $store['plants'][$index] = array_merge($plant, $body, ['id'=>$id]); $result=['data'=>$store['plants'][$index]]; $found=true; break; }
    if (!$found) reply(404, ['error'=>'Tanaman tidak ditemukan.']); $changed=true;
} elseif (preg_match('#^/plants/(\d+)$#', $path, $match) && $method === 'DELETE') {
    $id=(int)$match[1]; $before=count($store['plants']); $store['plants']=array_values(array_filter($store['plants'], fn($plant)=>(int)$plant['id']!==$id));
    if (count($store['plants'])===$before) reply(404, ['error'=>'Tanaman tidak ditemukan.']); $result=['message'=>'Tanaman dihapus.']; $changed=true;
} elseif ($path === '/tourism' && $method === 'GET') {
    $result=['data'=>$store['tourism']];
} elseif ($path === '/tourism' && $method === 'PUT') {
    $store['tourism']=array_merge($store['tourism'],$body); $result=['data'=>$store['tourism']]; $changed=true;
} elseif ($path === '/reports' && $method === 'GET') {
    $result=['data'=>array_values($store['reports'])];
} elseif ($path === '/reports' && $method === 'POST') {
    if (trim((string)($body['plantName'] ?? '')) === '') reply(422, ['error'=>'Nama tanaman wajib diisi.']);
    $reportId=(int)$store['next']['report']++;
    $report=array_merge(['id'=>$reportId,'type'=>'Laporan kondisi','plantId'=>null,'condition'=>'Perlu perhatian','description'=>'','status'=>'Menunggu ditinjau','createdAt'=>(int)(microtime(true)*1000)],$body,['id'=>$reportId]);
    $store['reports'][]=$report; $result=['data'=>$report]; $status=201; $changed=true;
} elseif (preg_match('#^/reports/(\d+)$#', $path, $match) && $method === 'PATCH') {
    $id=(int)$match[1]; $allowed=['Menunggu ditinjau','Ditindaklanjuti','Ditolak']; $newStatus=(string)($body['status'] ?? '');
    if (!in_array($newStatus,$allowed,true)) reply(422,['error'=>'Status laporan tidak dikenal.']);
    $found=false; foreach($store['reports'] as $index=>$report) if((int)$report['id']===$id){$store['reports'][$index]['status']=$newStatus;$result=['data'=>$store['reports'][$index]];$found=true;break;}
    if(!$found) reply(404,['error'=>'Laporan tidak ditemukan.']); $changed=true;
} elseif ($path === '/orders' && $method === 'GET') {
    $result=['data'=>array_values($store['orders'])];
} elseif ($path === '/orders' && $method === 'POST') {
    $plants=is_array($body['plants'] ?? null)?$body['plants']:[];
    $orderNumber=(int)$store['next']['order']++;
    $order=array_merge(['orderNumber'=>$orderNumber,'recipient'=>'','address'=>'','plants'=>$plants,'status'=>'Menunggu diproses','total'=>array_sum(array_map(fn($plant)=>(int)($plant['price']??0),$plants))],$body,['orderNumber'=>$orderNumber]);
    $store['orders'][]=$order; $result=['data'=>$order]; $status=201; $changed=true;
} elseif (preg_match('#^/orders/(\d+)$#', $path, $match) && $method === 'PATCH') {
    $id=(int)$match[1]; $newStatus=trim((string)($body['status'] ?? ''));
    if ($newStatus === '') reply(422,['error'=>'Status pesanan wajib diisi.']);
    $found=false; foreach($store['orders'] as $index=>$order) if((int)$order['orderNumber']===$id){$store['orders'][$index]['status']=$newStatus;$result=['data'=>$store['orders'][$index]];$found=true;break;}
    if(!$found) reply(404,['error'=>'Pesanan tidak ditemukan.']); $changed=true;
} else {
    $status=404; $result=['error'=>'Endpoint tidak ditemukan.'];
}

if ($changed || $initialized) saveStore($handle, $store);
flock($handle, LOCK_UN);
fclose($handle);
reply($status, $result ?? ['error'=>'Permintaan tidak dapat diproses.']);

