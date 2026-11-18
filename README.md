# EcoPlant Android App

Aplikasi Android EcoPlant untuk wisata kebun: pengunjung dapat melihat informasi tanaman, sementara petugas melaporkan kondisi tanaman dan admin mengelola informasi kebun. Katalog toko tanaman tetap tersedia.

## Menjalankan proyek

1. Clone repository ini dan buka folder proyek di Android Studio.
2. Tunggu Gradle Sync selesai.
3. Salin `local.properties.example` menjadi `local.properties` di folder proyek (jangan commit `local.properties`).
4. Isi username dan password lokal untuk admin serta petugas pada `local.properties`.
5. Pilih perangkat Android lalu tekan Run.

Konfigurasi akun lokal:

- `ecoplant.admin.username`
- `ecoplant.admin.password`
- `ecoplant.officer.username`
- `ecoplant.officer.password`

File `local.properties`, cache, dan hasil build tidak disertakan karena bersifat lokal.
