package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.admin.babyvaccinationtracker.model.Customer;
import java.util.Map;

public class CustomerRegistration {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public CustomerRegistration() {
        // Khởi tạo Firebase Auth và Realtime Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers");
    }

    public void registerCustomer(Context context, Customer customer, String filePath) {
        // Đăng ký người dùng vào Firebase Authentication
        Log.i("CustomerRegistration", customer.getCus_email() + " - " + customer.getCus_password());
//        uploadAvatar(avatar, "uaduiygaisdu");
        firebaseAuth.createUserWithEmailAndPassword(customer.getCus_email(), customer.getCus_password())
                .addOnCompleteListener(task -> {
                    Log.i("CustomerRegistration", "addOnCompleteListener");
                    if (task.isSuccessful()) {
                        Log.i("CustomerRegistration", "isSuccessful");
                        // Lấy thông tin người dùng đã đăng ký
                        String uid = task.getResult().getUser().getUid();
                        customer.setCus_password(null); // Xoá mật khẩu trước khi lưu vào Realtime Database

                        // Lưu thông tin khách hàng vào Realtime Database
                        databaseReference.child(uid).setValue(customer);

                        // Tải hình ảnh avatar lên imgbb và lưu đường dẫn vào Realtime Database
                        uploadAvatar(filePath, uid);
                        Toast.makeText(context, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                        ((AuthActivity)context).changeFragment("login");
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // Đã tồn tại người dùng với cùng email, xử lý tùy ý
                            Log.i("CustomerRegistration", "Duplicated User");
                            Toast.makeText(context, "Đã tồn tại người dùng với cùng email !", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Đăng ký thất bại, xử lý tùy ý
                            // ...
                            Log.i("CustomerRegistration", "Failed Register User");
                            Toast.makeText(context, "Đăng ký thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadAvatar(String filePath, String uid) {
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.i("upload image", "onStart: ");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.i("upload image", "Uploading... ");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.i("upload image", "image URL: "+resultData.get("url").toString());
                // save image url to firebase
                String avatarUrl = resultData.get("url").toString();
                databaseReference.child(uid).child("cus_avatar").setValue(avatarUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.i("upload image", "error "+ error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("upload image", "Reshedule "+error.getDescription());
            }
        }).dispatch();
    }
}