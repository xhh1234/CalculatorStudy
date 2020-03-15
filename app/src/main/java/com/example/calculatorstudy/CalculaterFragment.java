package com.example.calculatorstudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * CalculaterFragment
 */
public class CalculaterFragment extends BaseFragment implements View.OnClickListener {
    TextView tv_display;
    boolean opraterLock=false;// 防止两个数之间输入多于两个运算符
    boolean pointLock1 = false;     // 防止一个数中有多个小数点，摁下一个点后就锁住
    boolean pointLock2 = false;     // 防止在运算符后连接小数点
    BigDecimal result = BigDecimal.valueOf(0);  // java中的大数值，为了实现精确的计算，因为这是一个计算器
    int reservedDecimalNumber = 2;   // 默认保留的小数位数为2位小数

    @Override
    protected int LayoutId() {
        return R.layout.fragment_calculater;
    }

    @Override
    protected void initViews(View view, ViewHolder viewHolder) {
        super.initViews(view, viewHolder);
        tv_display = viewHolder.get(R.id.tv_display);
        viewHolder.setOnClickLinstener(this, R.id.btn_zero, R.id.btn_one, R.id.btn_two, R.id.btn_three
                , R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_seven, R.id.btn_eight, R.id.btn_nine, R.id.btn_add
                , R.id.btn_del, R.id.btn_devide, R.id.btn_equal, R.id.btn_minus, R.id.btn_percentage, R.id.btn_point
                , R.id.btn_take, R.id.btn_clear);
    }

    @Override
    public void onClick(View v) {
        String str = tv_display.getText().toString();

        switch (v.getId()) {
            case R.id.btn_zero:
            case R.id.btn_one:
            case R.id.btn_two:
            case R.id.btn_three:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_seven:
            case R.id.btn_eight:
            case R.id.btn_nine:
                if (str.equals("错误")) {
                    tv_display.setText("0");
                }

                String text;
                //如果屏幕显示是0就把摁下的数据，显示出来，如果不是就把数字连在一起
                if (str.equals("0")) {
                    text = "" + ((Button) v).getText();
                } else {
                    text = str + ((Button) v).getText();
                }
                tv_display.setText(text);
                break;

            case R.id.btn_point:
                if (!pointLock1&&!pointLock2) {
                    String text2 = str + ((Button) v).getText();
                    tv_display.setText(text2);
                    pointLock1=true;
                }
                break;

            case R.id.btn_take:
            case R.id.btn_add:
            case R.id.btn_devide:
            case R.id.btn_minus:
                //如果前一位不为小数点才能摁下运算符按键
                if (str.charAt(str.length() - 1) != '.' && !opraterLock) {
                    opraterLock = true;
                    pointLock1 = false;
                    pointLock2 = false;
                    String text3 = str + ((Button) v).getText();
                    tv_display.setText(text3);
                }
                break;
            case R.id.btn_equal:
                char lastChar=str.charAt(str.length()-1);
                if (lastChar!='.'&&lastChar!='+'&&lastChar!='-'&&lastChar!='/'&&lastChar!='*'){
                    try {
                        calculate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        str="错误";
                        tv_display.setText(str);
                    }
                }
                break;

            case R.id.btn_percentage:
                percentage();
                break;
            case R.id.btn_del:
               del(str);
                break;

            case R.id.btn_clear:
                str = "0";
                tv_display.setText(str);
                pointLock1 = false;
                pointLock2 = false;
                opraterLock = false;
                break;
            //求运算结果
        }
    }

    /**
     * 计算
     */
    private void calculate() {
        String expression=tv_display.getText().toString();
        // 利用正则表达式将各个数值分开，提取到数组里
        String[] expArr = expression.split("\\+|\\-|\\*|\\/");//\\+或者是-
        String[] operate = expression.split("\\d+|\\.");
        String[] foperate = new String[100];
        double[] numArr=new double[expArr.length];
        int index=0,index1;
        double sum=0;
        if (expression.charAt(0)=='-'){
            index1=1;
        }else {
            index1=0;
        }
        for (;index1<expArr.length;index1++){
            numArr[index1]=Double.parseDouble(expArr[index1]);
        }

        for (String mystr:operate){
            if ((!mystr.equals(""))&&(!mystr.equals(null))){
                foperate[index]=mystr;
            }
        }

        index = 0;
        for (String operater : foperate) {
            if (operater != null) {
                if (operater.equals("-")) {
                    numArr[index + 1] = -numArr[index + 1];
                }
                index++;
            }
        }


        index = 0;
        for (int i = 0; i < foperate.length; i++) {
            if (foperate[i] != null) {
                if (foperate[i].equals("*")) {
                    numArr[index + 1] = numArr[index] * numArr[index + 1];
                    numArr[index] = 0;
                    foperate[i] = null;
                } else if (foperate[i].equals("/")) {
                    numArr[index + 1] = numArr[index] / numArr[index + 1];
                    numArr[index] = 0;
                    foperate[i] = null;
                }
                index++;
            }
        }

        for (Double d : numArr) {
            sum += d;
        }
        BigDecimal accurateSum = BigDecimal.valueOf(sum).setScale(reservedDecimalNumber, BigDecimal.ROUND_HALF_UP);
        result = accurateSum;
        tv_display.setText(String.valueOf(accurateSum));
    }

    /**
     * 百分号的计算
     */
    private void percentage(){
        String tv_display_Str=tv_display.getText().toString();
        String regEx="^(-?\\d+)(\\.\\d+)?$ ||^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$||^[0-9]*$";//正则表达式
        Pattern pattern=Pattern.compile(regEx);
        Matcher metcher=pattern.matcher(tv_display_Str);
        if (metcher.matches()){
            double str1=Double.parseDouble(tv_display_Str)/100;
            tv_display.setText(String.valueOf(str1));
        }else {
            tv_display.setText("错误");
        }
    }

    /**
     * 删除
     */
    private void del(String str){
        //当小数点被删除后，锁1要打开，不然无法再次输入小数点，
        // 因为锁1只有在摁下运算符后才会打开
        if (str.charAt(str.length() - 1) == '.') {
            pointLock1 = false;
            pointLock2 = false;
        }
        //如果运算符被删掉，opraterLock打开，能够再次输入运算符
        //pointLock1要锁上，以免在输入运算符再将其删除后，输入一个数字即又可以输入point的情况
        if (str.charAt(str.length() - 1) == '+' || str.charAt(str.length() - 1) == '-' ||
                str.charAt(str.length() - 1) == '*' || str.charAt(str.length() - 1) == '/') {
            opraterLock = false;
            pointLock1 = true;
        }

        //如果长度大于一，返回去掉最后一个字符的字符串
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);

        }
        //否则（即长度为1时直接设置显示器中为0）
        else {
            str = "0";
        }
        tv_display.setText(str);
    }


}
