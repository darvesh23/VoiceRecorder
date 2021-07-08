package com.example.democheckbox;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CheckBox chk1,chk2,chk3,chk4;
    EditText txt1,txt2,txt3,txt4;
    Button btn;
    int num1=1,num2=1,num3=1,num4=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chk1=(CheckBox)findViewById(R.id.chk1);
        chk2=(CheckBox)findViewById(R.id.chk2);
        chk3=(CheckBox)findViewById(R.id.chk3);
        chk4=(CheckBox)findViewById(R.id.chk4);

        txt1=(EditText)findViewById(R.id.edTxt1);
        txt2=(EditText)findViewById(R.id.edTxt2);
        txt3=(EditText)findViewById(R.id.edTxt3);
        txt4=(EditText)findViewById(R.id.edTxt4);

        btn=(Button)findViewById(R.id.btn1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             int total=0;

             StringBuilder result= new StringBuilder();
             result.append("Your Orders are : ");
                try {
                     num1 = Integer.parseInt(txt1.getText().toString());
                     num2 = Integer.parseInt(txt2.getText().toString());
                     num3 = Integer.parseInt(txt3.getText().toString());
                     num4 = Integer.parseInt(txt4.getText().toString());
                }
                catch  (NumberFormatException e) {

                }
                    if (chk1.isChecked()) {
                        result.append("\n Expresso "+num1);
                        total += num1 * 200;
                    }

                    if (chk2.isChecked()) {
                        result.append("\n Americano "+num2);
                        total += num2 * 200;
                    }

                    if (chk3.isChecked()) {
                        result.append("\n Capachino "+num3);
                        total += num3 * 200;
                    }
                    if (chk4.isChecked()) {
                        result.append("\n Latte "+num4);
                        total += num4 * 200;
                    }

                result.append("\n Total Bill = INR "+total+"/-");
                Toast.makeText(MainActivity.this,result.toString(), Toast.LENGTH_SHORT).show();


            }
        });



    }
}