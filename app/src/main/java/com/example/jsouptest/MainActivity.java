package com.example.jsouptest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anye.greendao.gen.GreenBeanDao;
import com.example.jsouptest.greenDao.GreenBean;
import com.example.jsouptest.subsection.Play;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<News> newsList;
    private NewsAdapter adapter;
    private Handler handler;
    private ListView lv;
    private GreenBeanDao dao;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.news_lv);
        getNews();
        //添加GreenDao
        dao = App.getInstances().getDaoSession().getGreenBeanDao();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){

                    adapter = new NewsAdapter(MainActivity.this,newsList);
                    lv.setAdapter(adapter);


                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News news = newsList.get(position);
                            String title = news.getNewsTitle();
                            String newsUrl = news.getNewsUrl();
                            //添加
                            GreenBean greenBean = new GreenBean(title, newsUrl);
                            dao.insert(greenBean);
                            Intent intent = new Intent(MainActivity.this,Play.class);

                            intent.putExtra("news_url",news.getNewsUrl());
                            startActivity(intent);
                        }
                    });
                }
            }
        };
    }



    private void getNews(){
        new Thread(new Runnable() {
            private News news;
            private String pl;
            private String uri;
            private String title;
            private String time;
            @Override
            public void run() {
                try{
                    //获取虎扑新闻20页的数据，网址格式为：https://voice.hupu.com/nba/第几页

                        Document doc = Jsoup.connect(Api.URL).get();
                        Elements select = doc.select("ul.module-list");
                    Elements elements = select.get(1).select("li.module-list-item");
                        for(int j = 0;j <elements.size();j++){

                            title = elements.get(j).select("a").text();
                            uri = elements.get(j).select("a").attr("href");
                            news = new News(title,uri);
                            newsList.add(news);
                        }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
            }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}




