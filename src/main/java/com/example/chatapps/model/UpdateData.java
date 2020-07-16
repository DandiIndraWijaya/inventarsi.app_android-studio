package com.example.chatapps.model;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapps.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateData extends AppCompatActivity {

    private EditText peminjam, tenggat_waktu;
    private TextView nimBaru, namaBaru, jurusanBaru;
    private Button update, hapus;
    private DatabaseReference database;
    private ImageView image;
    private FirebaseAuth auth;
    private String cekNIM, cekNama, cekJurusan, cekPeminjam, cekWaktuPinjam, cekTenggatWaktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
//        getSupportActionBar().setTitle("Update Data");
        image = findViewById(R.id.imageContainer);
        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        jurusanBaru = findViewById(R.id.new_jurusan);
        peminjam = findViewById(R.id.new_peminjam);
        tenggat_waktu =findViewById(R.id.new_tenggat);
        update = findViewById(R.id.update);
        hapus = findViewById(R.id.hapus);

        final String key = getIntent().getExtras().getString("getPrimaryKey");
        final String getGambar = getIntent().getExtras().getString("gambar");

        String sekarang_tanggal = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
        String sekarang_pukul = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        final String waktu_dipinjam = sekarang_pukul + " WIB, " + sekarang_tanggal;

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData();

        hapus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String[] action = {"Hapus", "Tidak"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:

                                FirebaseDatabase.getInstance().getReference().child("Admin").child("Barang").child(key)
                                        .removeValue();
                                Toast.makeText(UpdateData.this, "Barang Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case 1:

                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mendapatkan Data Mahasiswa yang akan dicek

                cekPeminjam = peminjam.getText().toString();

                final String getNIM = getIntent().getExtras().getString("dataNIM");
                final String getNama = getIntent().getExtras().getString("dataNama");
                final String getJurusan = getIntent().getExtras().getString("dataJurusan");
                //Mengecek agar tidak ada data yang kosong, saat proses update
                if(TextUtils.isEmpty(cekPeminjam)){
                    data_barang setMahasiswa = new data_barang();
                    setMahasiswa.setNamaBarang(getNIM);
                    setMahasiswa.setKodeBarang(getNama);
                    setMahasiswa.setDeskripsi(getJurusan);
                    setMahasiswa.setPeminjam("-");
                    setMahasiswa.setWaktuPinjam("-");
                    setMahasiswa.setTenggatWaktu("-");
                    setMahasiswa.setGambar(getGambar);
                    updateMahasiswa(setMahasiswa);
                }else {
                    /*
                      Menjalankan proses update data.
                      Method Setter digunakan untuk mendapakan data baru yang diinputkan User.
                    */
                    data_barang setMahasiswa = new data_barang();
                    setMahasiswa.setNamaBarang(getNIM);
                    setMahasiswa.setKodeBarang(getNama);
                    setMahasiswa.setDeskripsi(getJurusan);
                    setMahasiswa.setPeminjam(peminjam.getText().toString());
                    setMahasiswa.setWaktuPinjam(waktu_dipinjam);
                    setMahasiswa.setTenggatWaktu(tenggat_waktu.getText().toString());
                    setMahasiswa.setGambar(getGambar);

                    updateMahasiswa(setMahasiswa);
                }
            }
        });
    }

    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData(){
        //Menampilkan data dari item yang dipilih sebelumnya
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getJurusan = getIntent().getExtras().getString("dataJurusan");
        final String getPeminjam = getIntent().getExtras().getString("peminjam");
        final String key = getIntent().getExtras().getString("getPrimaryKey");
        final String getTenggatWaktu = getIntent().getExtras().getString("tenggatWaktu");
        final String getGambar = getIntent().getExtras().getString("gambar");

        Picasso.get().load(getGambar).into(image);
        nimBaru.setText("Nama Barang: "+getNIM);
        namaBaru.setText("Kode Barang: "+getNama);
        jurusanBaru.setText("Deskripsi: " +getJurusan);
        peminjam.setText(getPeminjam);
        tenggat_waktu.setText(getTenggatWaktu);
    }

    //Proses Update data yang sudah ditentukan
    private void updateMahasiswa(data_barang mahasiswa){
        String userID = auth.getUid();
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Admin")
                .child("Barang")
                .child(getKey)
                .setValue(mahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nimBaru.setText("");
                        namaBaru.setText("");
                        jurusanBaru.setText("");
                        peminjam.setText("");
                        tenggat_waktu.setText("");
                        Toast.makeText(UpdateData.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
