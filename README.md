# EcoPlant Android App + PHP API

EcoPlant mendukung katalog dan informasi wisata tanaman, laporan kondisi tanaman oleh petugas, pengelolaan informasi wisata oleh admin, serta alur pesanan toko.

## Android

1. Buka repo ini di Android Studio dan tunggu Gradle Sync.
2. Atur nilai lokal pada local.properties memakai contoh di local.properties.example.
3. Pilih perangkat lalu tekan Run.

## Backend PHP

Folder API ada di backend php. Di komputer ini PHP tersedia dari XAMPP. Jalankan dari folder backend:

```powershell
& 'C:\xampp\php\php.exe' -S 0.0.0.0:8080 -t public public/router.php
```

Cek server pada http://127.0.0.1:8080/api/health. Android Emulator menggunakan base URL http://10.0.2.2:8080/api. Untuk HP fisik, gunakan alamat IPv4 komputer dan pastikan keduanya satu jaringan serta port 8080 diizinkan firewall.

## Penyimpanan dan database

Sebelum skema database kelompok diberikan, API memakai penyimpanan JSON sementara di backend php/storage/data.json. File ini dibuat otomatis dan diabaikan Git. Login admin/petugas masih memakai konfigurasi lokal aplikasi; integrasi akun, role, dan database akan disesuaikan setelah ERD/skema database dibagikan.

Jangan commit local.properties, token API, password, file data, cache, atau hasil build.