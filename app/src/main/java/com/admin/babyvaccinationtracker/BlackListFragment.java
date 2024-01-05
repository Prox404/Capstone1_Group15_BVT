package com.admin.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.babyvaccinationtracker.Adapter.BlockUserAdapter;
import com.admin.babyvaccinationtracker.model.BlackList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlackListFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    BlockUserAdapter blockUserAdapter;
    DatabaseReference databaseReference;
    List<BlackList> blackListLists = new ArrayList<>();
    List<BlackList> BlackList_origin = new ArrayList<>();
    TextView editTexte_Search_Block_user;
    ImageView btn_blackListUser_Switch;

    View empty_layout;

    Boolean check = true;

    public BlackListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        editTexte_Search_Block_user.setText("");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlackListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlackListFragment newInstance(String param1, String param2) {
        BlackListFragment fragment = new BlackListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    void Loaddata(Boolean check){
        if(check == true){
            databaseReference.child("customers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("BLACKLISTSNAPSHOT", ""+snapshot);
                    if(snapshot.exists()){
                        Log.i("DataExists", "Data exists in the snapshot");
                        blackListLists.clear();
                        for(DataSnapshot a : snapshot.getChildren()){
                            BlackList black = a.getValue(BlackList.class);
                            black.setBlacklist_id(a.getKey());
                            blackListLists.add(black);
                        }
                        BlackList_origin = new ArrayList<>(blackListLists);
                        if(BlackList_origin.size() <= 0){
                            empty_layout.setVisibility(View.VISIBLE);
                        }else {
                            empty_layout.setVisibility(View.GONE);
                        }
                        blockUserAdapter.notifyDataSetChanged();
                        blockUserAdapter.setCheckisCustomer(true);

                    }
                    else {
                        BlackList_origin.clear();
                        blackListLists.clear();
                        empty_layout.setVisibility(View.VISIBLE);
                        blockUserAdapter.notifyDataSetChanged();
                        Log.i("DataNotExists", "Data does not exist in the snapshot");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context,"Xảy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });
        }else  {
            databaseReference.child("Vaccine_center").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("BLACKLISTSNAPSHOT", ""+snapshot);
                    if(snapshot.exists()){
                        Log.i("DataExists", "Data exists in the snapshot");
                        blackListLists.clear();
                        for(DataSnapshot a : snapshot.getChildren()){
                            BlackList black = a.getValue(BlackList.class);
                            black.setBlacklist_id(a.getKey());
                            blackListLists.add(black);
                        }
                        BlackList_origin = new ArrayList<>(blackListLists);
                        if(BlackList_origin.size() <= 0){
                            empty_layout.setVisibility(View.VISIBLE);
                        }else {
                            empty_layout.setVisibility(View.GONE);
                        }
                        blockUserAdapter.notifyDataSetChanged();
                        blockUserAdapter.setCheckisCustomer(false);

                    }
                    else {
                        BlackList_origin.clear();
                        empty_layout.setVisibility(View.VISIBLE);
                        blackListLists.clear();
                        blockUserAdapter.notifyDataSetChanged();
                        Log.i("DataNotExists", "Data does not exist in the snapshot");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context,"Xảy ra lỗi", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_black_list, container, false);
        context = container != null ? container.getContext() : null;
        recyclerView = view.findViewById(R.id.RecylerViewListBlockUser);
        editTexte_Search_Block_user = view.findViewById(R.id.editTexte_Search_Block_user);
        btn_blackListUser_Switch = view.findViewById(R.id.btn_blackListUser_Switch);
        empty_layout = view.findViewById(R.id.empty_layout);


        databaseReference = FirebaseDatabase.getInstance().getReference("BlackList");
        blockUserAdapter = new BlockUserAdapter(blackListLists,check);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(blockUserAdapter);

        Loaddata(check);

        btn_blackListUser_Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check == true){
                    Loaddata(false);
                    editTexte_Search_Block_user.setHint("Tìm kiếm người dùng bị chặn");
                    check = false;
                }
                else {
                    Loaddata(true);
                    editTexte_Search_Block_user.setHint("Tìm kiếm người dùng bị chặn");
                    check = true;
                }

            }
        });



         editTexte_Search_Block_user.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                 search(name);
             }
         });

        return view;
    }

    private void search(String name) {
        List<BlackList> listfilter = new ArrayList<>();
        if(!name.isEmpty()){
            for(BlackList b : BlackList_origin){
                if(removeDiacritics(b.getCus_name().toLowerCase()).contains(removeDiacritics(name.toLowerCase()))){
                    listfilter.add(b);
                }
            }
            blackListLists = new ArrayList<>(listfilter);

        }else {
            blackListLists = new ArrayList<>(BlackList_origin);
        }
        blockUserAdapter.setData(blackListLists);
    }
    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}