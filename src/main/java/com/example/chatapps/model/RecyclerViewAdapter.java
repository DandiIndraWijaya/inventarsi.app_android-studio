package com.example.chatapps.model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.chatapps.R.color.ijo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    //Deklarasi Variable
    private ArrayList<data_barang> listMahasiswa;
    private Context context;

    //Membuat Konstruktor, untuk menerima input dari Database


    public RecyclerViewAdapter(ArrayList listMahasiswa, Context context) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView NIM, Nama, Jurusan, Peminjam, WaktuPinjam, TenggatWaktu;
        ImageView imageView;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            imageView = itemView.findViewById(R.id.imageview);
            NIM = itemView.findViewById(R.id.namaBarang);
            Nama = itemView.findViewById(R.id.kodeBarang);
            Peminjam = itemView.findViewById(R.id.peminjam);
            WaktuPinjam = itemView.findViewById(R.id.waktuPinjam);
            TenggatWaktu = itemView.findViewById(R.id.tenggatWaktu);
            Jurusan = itemView.findViewById(R.id.deskripsi);

            ListItem = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String NIM = listMahasiswa.get(position).getNamaBarang();
        final String Nama = listMahasiswa.get(position).getKodeBarang();
        final String Jurusan = listMahasiswa.get(position).getDeskripsi();
        final String Peminjam = listMahasiswa.get(position).getPeminjam();
        final String WaktuPinjam = listMahasiswa.get(position).getWaktuPinjam();
        final String TenggatWaktu = listMahasiswa.get(position).getTenggatWaktu();
        final String Gambar = listMahasiswa.get(position).getGambar();
        final String ijo = "#5cb85c";



        final String imageUrl = "https://3.bp.blogspot.com/-vWFEp1ViIDI/WtfaUdGRPRI/AAAAAAAAGn8/q4tq3LieoAIMIsrorMNpR_KAe4UiWeClQCLcBGAs/s1600/WildanTechnoArt-CRUD%2BFirebase%2BRealtime%2BDatabase%2BMembuat%2BFungsi%2BRead.jpg";
        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.NIM.setText("Nama: "+NIM);
        holder.Nama.setText("Kode: "+Nama);
        holder.Jurusan.setText("Deskripsi: "+Jurusan);
        holder.Peminjam.setText("Peminjam: "+Peminjam);
        holder.WaktuPinjam.setText("Dipinjam Pada: "+WaktuPinjam);
        holder.TenggatWaktu.setText("Sampai: "+TenggatWaktu
        );
        Picasso.get().load(Gambar).into(holder.imageView);

        if(holder.Peminjam.getText() != "Peminjam: -"){
            holder.Peminjam.setTextColor(ContextCompat.getColor(context, R.color.ijo));
        }
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    final String[] action = {"Update"};
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setItems(action,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch (i){
                                case 0:
                        /*
                          Berpindah Activity pada halaman layout updateData
                          dan mengambil data pada listMahasiswa, berdasarkan posisinya
                          untuk dikirim pada activity selanjutnya
                        */
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dataNIM", listMahasiswa.get(position).getNamaBarang());
                                    bundle.putString("dataNama", listMahasiswa.get(position).getKodeBarang());
                                    bundle.putString("dataJurusan", listMahasiswa.get(position).getDeskripsi());
                                    bundle.putString("peminjam", listMahasiswa.get(position).getPeminjam());
                                    bundle.putString("waktuPinjam", listMahasiswa.get(position).getWaktuPinjam());
                                    bundle.putString("tenggatWaktu", listMahasiswa.get(position).getTenggatWaktu());
                                    bundle.putString("gambar", listMahasiswa.get(position).getGambar());
                                    bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                                    Intent intent = new Intent(view.getContext(), UpdateData.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
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
        }


    }





    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listMahasiswa.size();
    }

}
