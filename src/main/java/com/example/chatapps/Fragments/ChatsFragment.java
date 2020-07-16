//package com.example.chatapps.Fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.chatapps.MainActivity;
//import com.example.chatapps.R;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//
//import static android.text.TextUtils.isEmpty;
//
//
//public class ChatsFragment extends Fragment{
//    TextView text, text2;
//    Button save;
//    FirebaseAuth auth;
//    TextView NIM, Nama,Jurusan, key;
//    private String[] nama = {"Dandi", "Indra", "Wijaya"};
//    Activity activity = getActivity();
//    DatabaseReference getReference;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View inventaris = inflater.inflate(R.layout.fragment_chats, container, false);
//
//        text2 = inventaris.findViewById(R.id.text2);
//        save = inventaris.findViewById(R.id.save);
//
////        FirebaseUser firebaseUser = auth.getCurrentUser();
////        assert firebaseUser != null;
////        final String getUserID = firebaseUser.getUid();
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String getNIM = NIM.getText().toString();
//                final String getKodeBarang = Nama.getText().toString();
//                final String getDeskripsi = Jurusan.getText().toString();
//
//
//                if(isEmpty(getNIM) && isEmpty(getKodeBarang) && isEmpty(getDeskripsi)){
//                    Toast.makeText(activity, "Data Tersimpan", Toast.LENGTH_SHORT).show();
//                }else {
//
//                    FirebaseDatabase.getInstance().getReference().child("Admin").child(getUserID).child("Mahasiswa").push()
//                            .setValue(new data_barang(getNIM, getKodeBarang, getDeskripsi));
//
//
//                }
//            }
//        });
//
//
//
//        text = inventaris.findViewById(R.id.text);
//        text.setText("csdf");
//        return  inventaris;
//    }
//
//
//
//}
//
//
