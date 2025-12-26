# 📊 Crypto Dashboard – Simulated Real-time Cryptocurrency Visualization

## 📖 Tentang Program

**Crypto Dashboard** adalah aplikasi desktop berbasis **Java Swing** yang digunakan untuk **visualisasi pergerakan harga cryptocurrency secara simulasi dengan pembaruan real-time**.
Aplikasi ini dirancang untuk **monitoring, visualisasi, dan analisis data kripto**, tanpa fitur transaksi jual beli.

> ⚠️ **Catatan:** Data harga yang digunakan merupakan **data simulasi (dummy)** yang diperbarui secara berkala untuk tujuan akademik dan pembelajaran.

---

## 🎯 Tujuan Aplikasi

* **Visualisasi Data** – Menampilkan grafik pergerakan harga cryptocurrency secara dinamis
* **Monitoring** – Memantau perubahan harga dengan pembaruan otomatis
* **Manajemen Data** – Mengelola data cryptocurrency menggunakan operasi CRUD
* **Analisis** – Menyediakan laporan dan riwayat perubahan harga

---

## ✨ Fitur Utama

### 🟣 1. Dashboard Utama

* Tabel lengkap daftar cryptocurrency
* Harga terkini dan perubahan 24 jam
* Indikator naik/turun dengan warna
* Fitur pencarian dan pengurutan data
* Auto-refresh setiap **10 detik**

### 🟣 2. Detail Harga & Grafik

* Grafik garis pergerakan harga (simulasi real-time)
* Pilihan timeframe (1 menit, 5 menit, 1 jam, dll)
* Panel statistik: harga saat ini, high/low 24 jam, volume
* Tema **dark mode** untuk grafik

### 🟣 3. Input Data (CRUD Lengkap)

* **Create** – Menambahkan data cryptocurrency baru
* **Read** – Menampilkan daftar cryptocurrency
* **Update** – Mengedit data cryptocurrency
* **Delete** – Menghapus data cryptocurrency
* Validasi input dan pengecekan duplikasi data

### 🟣 4. Laporan & Riwayat

* Riwayat harga cryptocurrency
* Grafik pie distribusi market

---

## 🛠️ Teknologi yang Digunakan

* **Bahasa**: Java 11+
* **GUI**: Java Swing
* **Grafik**: JFreeChart 1.5.3
* **Penyimpanan Data**: File CSV

---

## 📁 Struktur Proyek

```
crypto-dashboard/
├── src/
│   ├── Main.java                         # Application entry point
│   ├── components/                       # Custom UI components
│   │   ├── ModernButton.java             # Styled button
│   │   ├── CryptoTable.java              # Custom table
│   │   └── SidebarPanel.java             # Navigation panel
│   ├── dashboard/                        # UI pages
│   │   ├── CryptoDashboard.java          # Main dashboard
│   │   ├── DetailChartFrame.java         # Chart page
│   │   ├── DataInputFrame.java           # CRUD page
│   │   └── ReportFrame.java              # Reports page
│   ├── models/                           # Data models
│   │   ├── Cryptocurrency.java           # Cryptocurrency model
│   ├── services/                         # Business logic
│   │   ├── FileHandler.java              # File I/O operations
│   │   └── APIService.java               # Price data simulation
│── README.md                             # This documentation
└── cryptocurrencies.csv                  # Cryptocurrency data
```

---
Siap 👍
Berikut **versi revisi “Cara Menjalankan Program”** sesuai permintaan: **JFreeChart di-download manual lalu dipasang lewat Project Structure (Module & Dependencies)**.

---

## 🚀 Cara Menjalankan Program (Manual JFreeChart)

### 📌 Prasyarat

* **Java JDK 11 atau lebih baru**
* **IDE** (disarankan: IntelliJ IDEA / NetBeans / Eclipse)
* Koneksi internet (untuk download JFreeChart)

---

## 🔽 1. Download Library JFreeChart

1. Buka situs resmi JFreeChart
   👉 [https://www.jfree.org/jfreechart/](https://www.jfree.org/jfreechart/)
2. Download file **JFreeChart (ZIP / JAR)**
3. Extract hingga mendapatkan file:

  * `jfreechart-x.x.x.jar`
  * `jcommon-x.x.x.jar` (jika ada)

---

## 🧩 2. Pasang JFreeChart ke Project Structure (IntelliJ IDEA)

### Langkah-langkah:

1. Buka project di **IntelliJ IDEA**
2. Klik **File → Project Structure**
3. Masuk ke **Modules**
4. Pilih module project kamu
5. Buka tab **Dependencies**
6. Klik tombol **+ → JARs or Directories**
7. Pilih file:

  * `jfreechart-x.x.x.jar`
  * `jcommon-x.x.x.jar`
8. Klik **Apply → OK**

📌 Pastikan scope library adalah **Compile**

---

## ▶️ 3. Menjalankan Program

1. Pastikan file `Main.java` memiliki method:

   ```java
   public static void main(String[] args);
   ```
2. Klik kanan `Main.java`
3. Pilih **Run 'Main'**

---

## 💾 Penyimpanan Data

Aplikasi menggunakan **file CSV** untuk menyimpan data:

* Data cryptocurrency
* Auto-create data contoh jika file tidak ditemukan

---

## 🔄 Pembaruan Data (Simulasi Real-time)

* Interval update: **10 detik**
* Sumber data: **Simulasi API (untuk tujuan akademik)**
* Dashboard dan grafik diperbarui otomatis
* Indikator waktu update terakhir ditampilkan

---

## 🧪 Pengujian CRUD

* **Create**: Menambahkan data baru melalui form input
* **Update**: Mengedit data dari tabel
* **Delete**: Menghapus data terpilih
* Semua perubahan tersimpan ke file CSV dan langsung tampil di dashboard

---

## 🎨 Fitur UI Modern

* Dark Mode
* Warna status (hijau naik, merah turun)
* Layout responsif
---

## 📌 Keterangan

Aplikasi ini dibuat sesuai dengan ketentuan dan kriteria Modul UAP Pemrograman Lanjut serta telah melalui proses testing dan code review untuk memastikan aplikasi berjalan dengan baik dan stabil.

---