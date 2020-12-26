package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.finalproject.pojo.Urls;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.util.Arrays.asList;

/**
 * 消费情况统计图模块
 */
public class CountActivityOut extends AppCompatActivity {

    public String token,avatar;
    public BarChart barChart;
    public long Maximum;  //图标最大值
    public Integer[] moneyValue = new Integer[]{0, 0, 0, 0, 0}; //五条数字值
    public String[] labelName = new String[]{"投资理财", "服装美容", "生活日用", "餐饮美食", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_count_out);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");

        calingInterface();
    }

    private void calingInterface(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Urls.getUrl() +"outaccount/outaccountCount")
                            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                            .header("token",token)
                            .build();  //创造http请求
                    Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                    String responseData = response.body().string(); //获取返回回来的json结果

                    JSONObject jsonObject = new JSONObject(responseData);  //转换为json数组格式
                    JSONArray jsonArray = jsonObject.getJSONArray("detail");
                    for (int i=0; i<labelName.length;i++){
                        for (int j=0;j<jsonArray.length();j++){
                            if (labelName[i].equals(jsonArray.getJSONObject(j).get("type"))){
                                moneyValue[i] = (Integer) jsonArray.getJSONObject(j).get("nums");
                            }
                        }
                    }
                    Maximum = Math.round((int) Collections.max(asList(moneyValue)) * 1.3);
                    initBarChart();

                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CountActivityOut.this,"网络失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();


    }

    private void initBarChart(){
        barChart = findViewById(R.id.CountChart);
        barChart.getDescription().setEnabled(false); //不显示描述
        barChart.setExtraOffsets(20,20,20,20);
        setData();  // 设置数据
        setLegend(); // 设置图例
        setAxis(); // 设置坐标轴
    }

    /**
     * 因为此处的 barData.setBarWidth(0.3f);，也就是说柱子的宽度是0.3f
     * 所以第二个柱子的值要比第一个柱子的值多0.3f，这样才会并列显示两根柱子
     */
    private void setData() {
        List<IBarDataSet> sets = new ArrayList<>();
        // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
        // x是横坐标，表示位置，y是纵坐标，表示高度
        List<BarEntry> barEntries1 = new ArrayList<>();
        barEntries1.add(new BarEntry(0, moneyValue[0]));
        barEntries1.add(new BarEntry(1, moneyValue[1]));
        barEntries1.add(new BarEntry(2, moneyValue[2]));
        barEntries1.add(new BarEntry(3, moneyValue[3]));
        barEntries1.add(new BarEntry(4, moneyValue[4]));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
        barDataSet1.setValueTextColor(Color.RED); // 值的颜色
        barDataSet1.setValueTextSize(15f); // 值的大小
        barDataSet1.setColor(Color.parseColor("#1AE61A")); // 柱子的颜色
        barDataSet1.setLabel("支出金额"); // 设置标签之后，图例的内容默认会以设置的标签显示
        // 设置柱子上数据显示的格式
        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // 此处的value默认保存一位小数
                return value + "元";
            }
        });
        sets.add(barDataSet1);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f); // 设置柱子的宽度
        barChart.setData(barData);
    }

    private void setLegend() {
        Legend legend = barChart.getLegend();
        legend.setFormSize(12f); // 图例的图形大小
        legend.setTextSize(15f); // 图例的文字大小
        legend.setDrawInside(true); // 设置图例在图中
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例的方向为垂直
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //显示位置，水平右对齐
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 显示位置，垂直上对齐
        // 设置水平与垂直方向的偏移量
        legend.setYOffset(55f);
        legend.setXOffset(30f);
    }

    private void setAxis() {
        // 设置x轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(5);  // 设置x轴上的标签个数
        xAxis.setTextSize(15f); // x轴上标签的大小


        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) value < labelName.length) {
                    return labelName[(int) value];
                } else {
                    return "";
                }
            }
        });
        xAxis.setYOffset(15); // 设置标签对x轴的偏移量，垂直方向

        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(Maximum);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(Maximum);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setTextSize(15f); // 设置y轴的标签大小
    }
}