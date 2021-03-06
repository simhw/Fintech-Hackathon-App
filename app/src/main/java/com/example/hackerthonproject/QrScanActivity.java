package com.example.hackerthonproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.w3c.dom.Text;


public class QrScanActivity extends AppCompatActivity{

    WebView wv;
    TextView et, pt;
    private int point = 1000;// 기본 포인트
    private int bonus = 1000; // switch 나 if로 거리마다 보너스 세팅할 것
    Button bt, reScan, pointCheck, usePoint;
    IntentIntegrator integrator;
    String checkAddress; // qr을 새로 찍었을 떄 같은 건지 다른 건지 확인할 때 필요한 변수

    // 리츠 투자 상품 목록을 알려줄 url
    String REITsURL = "http://reits.molit.go.kr/svc/svc/openPage.do?pageId=020302";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        et = findViewById(R.id.et); // url보여주는 textfield
        pt = findViewById(R.id.pt); // 포인트
        wv = findViewById(R.id.wv); // web view
        bt = findViewById(R.id.bt); // 이 버튼이 어떤 버튼인 지 아직 이해가 잘 안감
        reScan = findViewById(R.id.reScan); // 다시 스캔하기 버튼
        pointCheck = findViewById(R.id.pointCheck); // 포인트 조회 버튼
        usePoint = findViewById(R.id.usePoint); // 리츠 투자 상품 목록으로 이어지는 버튼

        WebSettings webSettings = wv.getSettings();

        //화면 비율
        webSettings.setUseWideViewPort(true); // wide viewport를 사용하도록 설정
        webSettings.setLoadWithOverviewMode(true); // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        //웹뷰 멀티 터치 가능하게(줌기능)
        webSettings.setBuiltInZoomControls(true); // 줌 아이콘 사용
        webSettings.setSupportZoom(true);


        // point 조회 가능 및 키보드 없애기 및 텍스트 오른쪽 정렬
        pt.setText(printInfo());
        pt.setShowSoftInputOnFocus(false);
        pt.setGravity(Gravity.CENTER);



        // 스캔 완료 후 url수정 방지를 위한 키보드 숨기기
        et.setShowSoftInputOnFocus(false);


        //자바 스크립트 사용을 할 수 있도록 합니다.
        webSettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view,String url){
                /*
                웹뷰 로딩 완료 후 로직 구역
                */
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //bt의 onClick을 실행
                    bt.callOnClick();
                    //키보드 숨기기
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    return true;
                }
                // 같은 바코드를 여러번 찍는 경우에 대해서는 어떻게 대처할 것인 지 구현 못한 상태
                return false;
            }
        });


        integrator = new IntentIntegrator(this);

        integrator.setPrompt("QR 코드를 사각형 안에 위치 시켜주세요");

        //QR 코드 인식 시에 삐- 소리가 나게 할것인지 여부
        integrator.setBeepEnabled(true);

        integrator.setBarcodeImageEnabled(true);

        integrator.setCaptureActivity(CaptureActivity.class);

        //스캐너 시작 메소드
        integrator.initiateScan();

    }

    public void onClick(View view){
        String address = et.getText().toString();

        if(!address.startsWith("http://")){
            address = "http://" + address;
        }

        wv.loadUrl(address);

        // 기존의 url을 알려주는 부분에 포인트 사용을 위한 힌트 구문 제시
        et.setText("포인트를 리츠에 투자해보세요!");
        et.setGravity(Gravity.CENTER);
    }

    public void reScan(View view){
        integrator.initiateScan();
    }

    public void pointCheck(View view){
        pointCheck.setText(getPoint() + "원");
        pointCheck.setGravity(Gravity.CENTER);
    }

    public void usePoint(View view){
        // 리츠 투자 상품 url로 view를 전환
        wv.loadUrl(REITsURL);
    }

    @Override
    public void onBackPressed() {
        if(wv.isActivated()){
            if(wv.canGoBack()){
                wv.goBack();
            }else{
                //스캐너 재시작
                integrator.initiateScan();
            }

        }else{
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){

            }else{
                //qr코드를 읽어서 EditText에 입력해줍니다.
                et.setText(result.getContents());
                addPoint(); // 같은 바코드를 찍으면 point가 올라가면 안되게 바꿔야해
                pointCheck.setText("My 포인트 조회");

                //Button의 onclick호출
                bt.callOnClick();

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    // field 값들은 쉽게 바뀌면 안되니 private으로 선언

    public int getPoint() {
        return point;
    }

    // 적립될 포인트
    public void setPoint(int point) {
        this.point = point;
    }

    public void addPoint() {
        this.point += getBonus();
    }

    public String printInfo(){
        return "  적립된 Point : " + getBonus() + "원";
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}