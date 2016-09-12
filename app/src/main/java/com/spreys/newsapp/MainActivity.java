package com.spreys.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_empty_view)
    TextView emptyView;

    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");

        new NewsRetrievalTask(this).execute();
    }

    private class NewsRetrievalTask extends AsyncTask<Void, Void, List<News>> {
        private static final String API_URL = "http://content.guardianapis.com/search?q=android&api-key=test";
        private final Context context;

        public NewsRetrievalTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override protected List<News> doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(API_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return NewsFactory.GetNewsFromJson(NetworkUtils.GetJsonObjectFromUrl(url));
        }

        @Override
        protected void onPostExecute(List<News> news) {
            super.onPostExecute(news);
            progressDialog.dismiss();

            if (!news.isEmpty()) {
                emptyView.setVisibility(GONE);
                recyclerView.setAdapter(new NewsAdapter(context, news));
            }

        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<News> news;
        private Context context;

        public NewsAdapter(Context context, List<News> news) {
            this.news = news;
            this.context = context;
        }

        @Override
        public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    int position = recyclerView.indexOfChild(view);

                    String url = news.get(position).getUrl();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            News story = news.get(position);

            holder.type.setText(story.getType());
            holder.title.setText(story.getTitle());
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.news_item_title_textview)
            TextView title;

            @BindView(R.id.news_item_type_textview)
            TextView type;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
