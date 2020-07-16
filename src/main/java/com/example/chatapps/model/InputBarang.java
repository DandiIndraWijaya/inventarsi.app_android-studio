package com.example.chatapps.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapps.R;
import com.example.chatapps.StartActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class InputBarang extends AppCompatActivity {

    TextView username;
    CircleImageView profile_image;
    Button save, showData;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    StorageReference reference2;
    TextView KodeBarang, Peminjam, WaktuPinjam, TenggatWaktu, NamaBarang,Deskripsi, key;
    private Button Upload, UnggahGambar;
    private ImageView ImageContainer;
    private ProgressBar progressBar;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_barang);


        UnggahGambar = findViewById(R.id.select_Image);

        ImageContainer = findViewById(R.id.imageContainer);
        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        NamaBarang = findViewById(R.id.namaBarang);
        KodeBarang = findViewById(R.id.kodeBarang);
        Deskripsi = findViewById(R.id.deskripsi);
//        Peminjam = findViewById(R.id.peminjam);
//        WaktuPinjam = findViewById(R.id.waktuPinjam);
//        TenggatWaktu = findViewById(R.id.tenggatWaktu);

//        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save);
        showData = findViewById(R.id.showdata);

                reference2 = FirebaseStorage.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String getUserID = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        UnggahGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getNamaBarang = NamaBarang.getText().toString();
                final String getKodeBarang = KodeBarang.getText().toString();
                final String getDeskripsi = Deskripsi.getText().toString();
                final String getPeminjam = "-";
                final String getWaktuPinjam = "-";
                final String getTenggatWaktu = "-";

                if(TextUtils.isEmpty(getNamaBarang) || TextUtils.isEmpty(getKodeBarang) || TextUtils.isEmpty(getDeskripsi)){
                    Toast.makeText(InputBarang.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{
                    ImageContainer.setDrawingCacheEnabled(true);
                    ImageContainer.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    //Lokasi lengkap dimana gambar akan disimpan
                    String namaFile = UUID.randomUUID()+".jpg";
                    String pathImage = "gambar/"+namaFile;
                    final StorageReference ref = reference2.child(pathImage);
                    final UploadTask uploadTask = reference2.child(pathImage).putBytes(data);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressBar.setProgress((int) progress);
                        }
                    }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Uri downUri = task.getResult();
                                String url = downUri.toString();
                                NamaBarang.setText("");
                                KodeBarang.setText("");
                                Deskripsi.setText("");

                                Toast.makeText(InputBarang.this, "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference().child("Admin").child("Barang").push()
                                        .setValue(new data_barang(getNamaBarang, getKodeBarang, getDeskripsi, getPeminjam, getWaktuPinjam, getTenggatWaktu, url));

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(InputBarang.this, "Uploading Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });


//                            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                @Override
//                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                    if (!task.isSuccessful()) {
//                                        throw task.getException();
//                                    }
//
//                                    // Continue with the task to get the download URL
//                                    return ref.getDownloadUrl();
//                                }
//                            });
//
//                            String url = urlTask.toString();
//                            NamaBarang.setText(url);
//                            progressBar.setVisibility(View.GONE);
//                           Toast.makeText(InputBarang.this, "Uploading Berhasil", Toast.LENGTH_SHORT).show();
//                            FirebaseDatabase.getInstance().getReference().child("Admin").child("Barang").push()
//                                    .setValue(new data_barang(getNamaBarang, getKodeBarang, getDeskripsi, getPeminjam, getWaktuPinjam, getTenggatWaktu));
//                            Toast.makeText(InputBarang.this, "good", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(InputBarang.this, "Uploading Gagal", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                            progressBar.setVisibility(View.VISIBLE);
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            progressBar.setProgress((int) progress);
//                        }
//                    });
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    });



//                    Toast.makeText(InputBarang.this, "good", Toast.LENGTH_SHORT).show();
                }

            }
        });



//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        ViewPager viewPager = findViewById(R.id.view_pager);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        viewPagerAdapter.assFragment(new ChatsFragment(), "Chats");
//        viewPagerAdapter.assFragment(new UserFragment(), "Users");
//
//        viewPager.setAdapter(viewPagerAdapter);
//
//        tabLayout.setupWithViewPager(viewPager);

    }

    private void getImage(){
        Intent imageIntentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Menghandle hasil data yang diambil dari kamera atau galeri untuk ditampilkan pada ImageView
        switch(requestCode){
            case REQUEST_CODE_CAMERA:
                if(resultCode == RESULT_OK){
                    ImageContainer.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageContainer.setImageBitmap(bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if(resultCode == RESULT_OK){
                    ImageContainer.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    ImageContainer.setImageURI(uri);
                }
                break;
        }
    }


    private void uploadImage(){
        //Mendapatkan data dari ImageView sebagai Bytes

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InputBarang.this, StartActivity.class));
                finish();
                return true;
        }

        return false;
    }

//    public static class ViewPagerAdapter extends FragmentPagerAdapter{
//
//        private ArrayList<Fragment> fragments;
//        private ArrayList<String> titles;
//
//
//
//        public  ViewPagerAdapter(FragmentManager fm){
//            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//            this.fragments = new ArrayList<>();
//            this.titles = new ArrayList<>();
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            return fragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//        public void assFragment(Fragment fragment, String title){
//            fragments.add(fragment);
//            titles.add(title);
//        }
//
//        // ctrl + o
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles.get(position);
//        }
//    }
}
