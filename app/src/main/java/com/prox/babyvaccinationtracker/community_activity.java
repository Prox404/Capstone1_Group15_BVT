package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.PostAdapter;
import com.prox.babyvaccinationtracker.model.Comment;
import com.prox.babyvaccinationtracker.model.Post;
import com.prox.babyvaccinationtracker.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class community_activity extends AppCompatActivity {
    private FrameLayout popupDialog;
    private EditText editTextContent,editTextPopupContent, editTextHashtag;
    ImageButton imageButtonClose;
    String image_url = "https://res.cloudinary.com/du42cexqi/image/upload/v1696504103/nt4cybkx1k25elc2jrng.jpg"; // ảnh mặc định
    RecyclerView recycleViewImage; // nơi hiện ảnh đã chọn
    RecyclerAdapter recyclerAdapter; // Hiển thị những ảnh đã chọn
    ArrayList<Uri> uri = new ArrayList<>(); // những đường dẫn ảnh đã lưu
    ArrayList<String> Image_url = new ArrayList<>();
    LinearLayout linearLayoutAddImage;
    private static final int PERMISSION_CODE = 1;
    private static final int PICK_IMAGE = 1;
    RecyclerView recyclerViewPost;
    ArrayList<Post> postArrayList;
    PostAdapter postAdapter;
    Button buttonAddNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        popupDialog = findViewById(R.id.popupDialog);
        editTextContent = findViewById(R.id.editTextContent);
        imageButtonClose = findViewById(R.id.imageButtonClose);
        recycleViewImage = findViewById(R.id.recycleViewImage);
        linearLayoutAddImage = findViewById(R.id.linearLayoutAddImage);
        editTextHashtag = findViewById(R.id.editTextHashtag);
        buttonAddNewPost = findViewById(R.id.buttonAddNewPost);
        editTextPopupContent = findViewById(R.id.editTextPopupContent);
        recyclerViewPost = findViewById(R.id.recyclerViewPost);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("customer_id", "");
        String user_name = sharedPreferences.getString("cus_name", "");
        String user_avatar = sharedPreferences.getString("cus_avatar", "");

        User user = new User(user_id, user_name, user_avatar, "customer");

        recyclerAdapter = new RecyclerAdapter(uri,community_activity.this);
        recycleViewImage.setLayoutManager(new GridLayoutManager(community_activity.this, 3));
        recycleViewImage.setAdapter(recyclerAdapter);

        editTextContent.setFocusable(false);
        editTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị popupDialog
                showPopupDialogWithAnimation();
            }
        });

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ẩn popupDialog
                hidePopupDialogWithAnimation();
            }
        });

        linearLayoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Uri imageUri) {
                int position = uri.indexOf(imageUri);
                if (position != -1) {
                    uri.remove(position);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        buttonAddNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextPopupContent.getText().toString();
                String hashtag = editTextHashtag.getText().toString();
                ArrayList<String> hashtags = new ArrayList<>();
                if (content.isEmpty()) {
                    Toast.makeText(community_activity.this, "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    if (hashtag.length() > 0) {
                        hashtags = getHashtag(hashtag);
                    }

                    Post post = new Post();
                    post.setContent(content);
                    post.setHashtags(hashtags);
                    post.setUser(user);

                    uploadImagesToCloudinaryAndFirebase(post);


                }
            }
        });

        // Hiển thị bài viết
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = new Post();
                    post.setUser(dataSnapshot.child("user").getValue(User.class));
                    post.setContent(dataSnapshot.child("content").getValue(String.class));
                    post.setCreated_at(dataSnapshot.child("created_at").getValue(String.class));
                    ArrayList<String> hashtags = (ArrayList<String>) Objects.requireNonNull(dataSnapshot.child("hashtags").getValue());
                    post.setHashtags(hashtags);
                    ArrayList<String> image_url = (ArrayList<String>) Objects.requireNonNull(dataSnapshot.child("image_url").getValue());
                    post.setImage_url(image_url);
                    post.setPost_id(dataSnapshot.getKey());
                    post.setComments((HashMap<String, Comment>) dataSnapshot.child("comments").getValue());
                    post.setLiked_users((ArrayList<String>) dataSnapshot.child("liked_users").getValue());
                    postArrayList.add(post);
                }
                postAdapter = new PostAdapter(postArrayList, user);
                recyclerViewPost.setLayoutManager(new GridLayoutManager(community_activity.this, 1));
                recyclerViewPost.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void uploadImagesToCloudinaryAndFirebase(Post post) {
        int total_image = uri.size();
        if(total_image != 0){
            AtomicInteger uploadedImageCount = new AtomicInteger(0);
            for (int i = 0; i < total_image; i++) {
                Uri image = uri.get(i);

                MediaManager.get().upload(image.toString()).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.i("upload image", "onStart: ");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.i("uploading image", "Uploading... ");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String url = resultData.get("url").toString();
                        Log.i("upload image onSuccess", "image URL: " + url);
                        Image_url.add(url);
                        uploadedImageCount.incrementAndGet();

                        if (uploadedImageCount.get() == total_image) {
                            // All images are uploaded, proceed with saving data to Firebase.
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
                            String post_id = databaseReference.push().getKey();
                            post.setImage_url(Image_url);
                            post.setPost_id(post_id);
                            databaseReference.child(post_id).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        postArrayList.add(post);
                                        postAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            Toast.makeText(community_activity.this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                            hidePopupDialogWithAnimation();

                        }
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.i("upload image onError", "error " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.i("upload image onReschedule", "Reschedule " + error.getDescription());
                    }
                }).dispatch();
            }
        }

    }

    private ArrayList<String> getHashtag(String content) {
        ArrayList<String> hashtag = new ArrayList<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtag.add(word);
            }
        }
        return hashtag;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    public void accessTheGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery();
            } else {
                Toast.makeText(this, "Hãy cấp quyền chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //get the image’s file location
//        filepath = getRealPathFromUri(data.getData(), this);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String filePath = getRealPathFromUri(selectedImageUri, this);
                Log.i("Add image", "onActivityResult: " + filePath);
                if(!uri.contains(Uri.parse(filePath))){
                    uri.add(Uri.parse(filePath));
                }else{
                    Toast.makeText(this, "Ảnh đã được chọn trước đó", Toast.LENGTH_SHORT).show();
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);
        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void showPopupDialogWithAnimation() {
        // Sử dụng Animation để thực hiện hiệu ứng xuất hiện
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        popupDialog.startAnimation(animation);

        // Đặt sự kiện visibility cho popupDialog
        popupDialog.setVisibility(View.VISIBLE);
    }

    private void hidePopupDialogWithAnimation() {
        // Sử dụng Animation để thực hiện hiệu ứng ẩn đi
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        popupDialog.startAnimation(animation);

        // Đặt sự kiện visibility cho popupDialog
        popupDialog.setVisibility(View.GONE);
    }
}