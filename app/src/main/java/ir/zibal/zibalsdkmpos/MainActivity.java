package ir.zibal.zibalsdkmpos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.zibal.zibalsdk.ZibalActivity;
import ir.zibal.zibalsdk.ZibalResponseEnum;

public class MainActivity extends AppCompatActivity {


    final static int PAYMENT_REQUEST_CODE = 2000;
    EditText et_zibalId;
    Button btn_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_payment = findViewById(R.id.payBtn);
        et_zibalId = findViewById(R.id.zibalIdEditText);
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPaymentIntent();
            }
        });


    }

    protected void callPaymentIntent(){
        String zibalId = et_zibalId.getText().toString();
        if(zibalId.length() == 0){
            Toast.makeText(MainActivity.this,"لطفا شناسه زیبال را وارد کنید.",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(MainActivity.this, ZibalActivity.class);
            intent.putExtra("zibalId",zibalId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent,PAYMENT_REQUEST_CODE);
        } catch (Exception e) {
            Log.d("error happened", e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            switch (resultCode) {
                case ZibalResponseEnum.RESULT_DEVICE_CONNECTION_FAILED:
                    Toast.makeText(MainActivity.this, "اتصال با دستگاه برقرار نشد.", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_USER_CANCELED:
                    Toast.makeText(MainActivity.this, "کاربر از پرداخت منصرف شده است", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_PAYMENT_SUCCESSFUL:
                    if (data != null)
                        Toast.makeText(MainActivity.this,
                                "پرداخت با موفقیت انجام شد." + "\n" +
                                        "شماره مرجع " + data.getStringExtra("refNumber") + "\n" +
                                        "شماره رهگیری " + data.getStringExtra("traceNumber") + "\n" +
                                        "شناسه زیبال " + data.getStringExtra("paidZibalId")
                                , Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "پرداخت با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_ERROR_IN_PAYMENT:
                    Toast.makeText(MainActivity.this, "خطای عملیات پرداخت", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_ZIBAL_ID_ALREADY_PAID:
                    Toast.makeText(MainActivity.this, "شناسه قبلا پرداخت شده.", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_INVALID_ZIBAL_ID:
                    Toast.makeText(MainActivity.this, "شناسه زیبال نامعتبر است.", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.RESULT_UNREACHABLE_ZIBAL_SERVER:
                    Toast.makeText(MainActivity.this, "عدم دسترسی به سرور زیبال", Toast.LENGTH_SHORT).show();
                    break;
                case ZibalResponseEnum.MINIMUM_SDK_VERSION_NOT_MET:
                    Toast.makeText(MainActivity.this, "ورژن Sdk زیبال خود را بروزرسانی کنید.", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }
}

