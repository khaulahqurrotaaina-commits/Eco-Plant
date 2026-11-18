# Backend PHP EcoPlant

Folder ini menyediakan REST API untuk aplikasi Android dan website EcoPlant. Penyimpanan saat ini menggunakan JSON sementara agar alur frontend bisa dihubungkan sebelum skema database kelompok dikirim. Setelah database/ERD dibagikan, lapisan penyimpanan ini akan dipindah ke PDO dan disesuaikan dengan tabel yang benar.

## Menjalankan di komputer

1. Pasang PHP 8.1 atau lebih baru (di komputer ini tersedia melalui XAMPP).
2. Buka PowerShell pada folder `backend php`.
3. Jalankan: `& 'C:\xampp\php\php.exe' -S 0.0.0.0:8080 -t public public/router.php`.
4. Cek `http://127.0.0.1:8080/api/health`.

Backend membuat `storage/data.json` otomatis dan mengisinya dengan data tanaman contoh. File tersebut lokal dan tidak ikut Git.

## Endpoint

- `GET /api/health`
- `GET /api/plants`, `POST /api/plants`, `PUT /api/plants/{id}`, `DELETE /api/plants/{id}`
- `GET /api/tourism`, `PUT /api/tourism`
- `GET /api/reports`, `POST /api/reports`, `PATCH /api/reports/{id}`
- `GET /api/orders`, `POST /api/orders`

Semua body dan respons memakai JSON. Jika `ECOPLANT_API_TOKEN` diatur, setiap permintaan harus menyertakan header `Authorization: Bearer <token>`. Kosongkan hanya untuk uji lokal. Jangan taruh token rahasia ke repository publik.

Login admin/petugas pada aplikasi masih memakai konfigurasi lokal karena tabel akun dan rancangan database belum dibagikan. Setelah skema database tersedia, autentikasi role dan penyimpanan permanen akan dipindah ke backend.

## Menghubungkan Android

Android Emulator biasanya mengakses komputer melalui `http://10.0.2.2:8080/api`. HP fisik harus memakai alamat IPv4 komputer yang satu jaringan, misalnya `http://192.168.x.x:8080/api`, dan Windows Firewall harus mengizinkan port 8080. Atur `ecoplant.api.baseUrl` (dan opsional `ecoplant.api.token`) pada `local.properties` Android.

HTTP tanpa TLS hanya untuk pengembangan jaringan lokal. Untuk hosting publik, aktifkan HTTPS, autentikasi berbasis akun/role, validasi izin admin/petugas, dan database sebelum dipakai sungguhan.

