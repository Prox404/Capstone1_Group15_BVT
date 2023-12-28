package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.admin.babyvaccinationtracker.Adapter.ImageAdapter;
import com.admin.babyvaccinationtracker.model.Post;
import com.admin.babyvaccinationtracker.model.User;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Activity_add_post extends AppCompatActivity {

    private EditText topicEditText , editTextContent;
    String image_url = "https://res.cloudinary.com/du42cexqi/image/upload/v1696504103/nt4cybkx1k25elc2jrng.jpg"; // ảnh mặc định
    RecyclerView recycleViewImage; // nơi hiện ảnh đã chọn
    ImageAdapter recyclerAdapter; // Hiển thị những ảnh đã chọn
    ArrayList<Uri> uri = new ArrayList<>(); // những đường dẫn ảnh đã lưu
    ArrayList<String> Image_url = new ArrayList<>();
    LinearLayout linearLayoutAddImage;
    private static final int PERMISSION_CODE = 2;
    private static final int PICK_IMAGE = 1;
    Button buttonAddNewPost;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        recycleViewImage = findViewById(R.id.recycleViewImage);
        linearLayoutAddImage = findViewById(R.id.linearLayoutAddImage);
        topicEditText = findViewById(R.id.editTextTopic);
        editTextContent = findViewById(R.id.editTextContent);
        buttonAddNewPost = findViewById(R.id.buttonAddNewPost);

        recyclerAdapter = new ImageAdapter(uri);
        recycleViewImage.setLayoutManager(new GridLayoutManager(Activity_add_post.this, 3));
        recycleViewImage.setAdapter(recyclerAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("admin_id", "");
        String user_name = sharedPreferences.getString("admin_name", "");
        String user_avatar = sharedPreferences.getString("admin_avatar", "");

        user = new User();
        user.setUser_id(user_id);
        user.setUser_avatar(user_avatar);
        user.setUser_name(user_name);
        user.setUser_role("Admin");

        linearLayoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        recyclerAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
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
                String content = editTextContent.getText().toString().trim();
                String hastag = topicEditText.getText().toString();
                if(!content.isEmpty() && uri.size() != 0 && !hastag.isEmpty()){
                    Post post = new Post();
                    ArrayList<String> hashtag = getHashtag(hastag);
                    post.setHashtags(hashtag);
                    post.setContent(content);
                    post.setUser(user);
                    uploadImagesToCloudinaryAndFirebase(post);
                }
                else {
                    Toast.makeText(Activity_add_post.this, "Phải nhập đủ nội dung",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

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

    public void uploadImagesToCloudinaryAndFirebase(Post post) {
        int total_image = uri.size();
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("posts");
                        String post_id = databaseReference.push().getKey();
                        post.setImage_url(Image_url);
                        post.setPost_id(post_id);
                        databaseReference.child(post_id)
                                .setValue(post)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                topicEditText.setText("");
                                editTextContent.setText("");
                                uri.clear();
                                recyclerAdapter.notifyDataSetChanged();
                                Toast.makeText(Activity_add_post.this,
                                        "Đăng bài thành công",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

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



    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (Activity_add_post.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE},
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                    Toast.makeText(this, "Ảnh đã được chọn trước đó",
                            Toast.LENGTH_SHORT).show();
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver()
                .query(imageUri, null, null, null, null);
        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}