package com.example.weather;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * 输入字符串生成二维码，使用了工具包QRCodeUtil，调用里面的方法QRCodeUtil.createQRCodeBitmap
 */
public class RecommendFragment extends Fragment {
    ImageView imageView;
    EditText editText;

    public RecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //editText=view.findViewById(R.id.edit);
        imageView=view.findViewById(R.id.image);

        Button button= view.findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String getText=editText.getText().toString();
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap("天气APP", 480, 480);
                imageView.setImageBitmap(mBitmap);
            }
        });
    }
}
