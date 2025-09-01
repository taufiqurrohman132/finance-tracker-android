# Project Brief: Aplikasi "FinTrackr" (MVP v1.0)

## 📜 Filosofi Inti
Membangun fondasi yang kokoh dengan merilis **Minimum Viable Product (MVP)** yang simpel, berkualitas tinggi, dan berpusat pada pengguna (*user-centric*) untuk portofolio profesional.

---

## ✨ Fitur Inti (MVP)
Fokus hanya pada 4 pilar fungsionalitas ini untuk versi pertama:

1.  **Otentikasi Latar Belakang (Guest Mode):** Pengguna bisa langsung pakai aplikasi tanpa login/register berkat **Firebase Anonymous Authentication**.
2.  **Upgrade Akun:** Pilihan bagi pengguna di halaman Pengaturan untuk menautkan akun tamu mereka ke akun permanen (Email/Google) demi keamanan dan sinkronisasi data.
3.  **Tambah Transaksi:** Satu halaman form yang efisien untuk mencatat **Pemasukan** dan **Pengeluaran**.
4.  **Dashboard Utama:** Menampilkan **ringkasan keuangan** (saldo, dll.) dan **riwayat transaksi** terbaru secara *real-time*.

---

## 🎨 Desain & Pengalaman Pengguna (UX)

* **Palet Warna:** Modern & Profesional. Direkomendasikan **"Fintech Modern"** (gradien biru tua-ungu) atau **"Green Growth"** (hijau tua), didukung warna latar netral (putih gading/abu-abu gelap) dan aksen semantik (hijau untuk pemasukan, merah untuk pengeluaran).
* **Alur Pengguna Awal (First Launch):**
    * **Splash Screen** singkat (1-2 detik) untuk proses *anonymous sign-in* di latar belakang.
    * **Langsung ke Halaman Utama.** Tidak ada *onboarding slider* atau tur.
    * Halaman Utama menampilkan **"Empty State"** yang informatif jika belum ada data.
* **Alur Otentikasi (Upgrade Akun):**
    * Menggunakan **satu halaman tunggal** yang "pintar" untuk menangani Login dan Register.
    * Memprioritaskan **Google Sign-In** sebagai pilihan utama.

---

## 📱 Blueprint Halaman (3 Halaman Utama)

1.  **Halaman Utama / Beranda:**
    * Area atas: Ringkasan Saldo.
    * Area tengah: `RecyclerView` untuk riwayat.
    * Pojok bawah: `FloatingActionButton (+)` untuk menambah transaksi.
2.  **Halaman Tambah Transaksi:**
    * Form tunggal dengan `SegmentedButton` (Pengeluaran/Pemasukan), `EditText` (Jumlah, Deskripsi), dan `DatePicker`.
3.  **Halaman Pengaturan:**
    * Sangat minimalis. Isinya hanya untuk **Upgrade Akun/Logout** dan info dasar seperti **Kebijakan Privasi** dan **Versi Aplikasi**.

---

## 🛠️ Spesifikasi Teknis

* **Bahasa:** `Kotlin`
* **Minimal SDK:** `minSdkVersion = 26` (Android 8.0 Oreo)
* **Arsitektur:** `MVVM`, `Repository Pattern`, dengan prinsip `Offline-First` (Room sebagai *Single Source of Truth*, Firestore sebagai mekanisme sinkronisasi).
* **Library Utama:**
    * **Jetpack:** ViewModel, Room, Navigation Component, LiveData/Flow.
    * **Asynchronous:** Kotlin Coroutines.
    * **Backend:** Firebase Authentication & Cloud Firestore.
    * **UI:** XML dengan Material Design 3.

---

## 🗓️ Target Jadwal (Sprint 4 Minggu)

* **Minggu 1:** **Desain & Fondasi.** Membuat *wireframe*, menentukan sistem desain, dan setup proyek teknis.
* **Minggu 2:** **Fungsionalitas Otentikasi.** Mengimplementasikan alur *Guest Mode* hingga *Upgrade Akun*.
* **Minggu 3:** **Fungsionalitas Inti.** Membangun fitur tambah transaksi dan menampilkannya di dashboard.
* **Minggu 4:** **Poles, Testing, & Rilis.** Memperbaiki UI, melakukan testing menyeluruh, dan mempublikasikan ke Play Store.

---

## 📂 Manajemen Portofolio & Kode

* **Visibilitas Kode:** **GitHub Public.**
* **Keamanan:** File `google-services.json` **wajib** ada di dalam `.gitignore`.
* **Dokumentasi:** `README.md` yang profesional.
* **Lisensi:** **MIT License.**

---

## 🚀 Strategi Pasca-Rilis

* **Update:** Iteratif. Rilis v1.1 dengan fitur **Statistik** dalam **1-2 bulan** setelah rilis awal.
* **Monetisasi (Iklan):** **TUNDA.** Jangan pasang iklan sampai aplikasi memiliki basis pengguna yang sehat (minimal **1.000+ pengguna aktif harian**) dan rating yang bagus (stabil di **4.5+ bintang**).
