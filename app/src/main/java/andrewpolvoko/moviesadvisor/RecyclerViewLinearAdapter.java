package andrewpolvoko.moviesadvisor;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RecyclerViewLinearAdapter extends RealmBasedRecyclerViewAdapter<MovieRealmEntity, RecyclerViewLinearAdapter.MovieViewHolder> {
    private Context context;

    public RecyclerViewLinearAdapter(
            Context context,
            RealmResults<MovieRealmEntity> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.context = context;
    }

    public static class MovieViewHolder extends RealmViewHolder {

        public ImageView poster;
        public ProgressBar progressBar;
        public TextView title;
        public TextView releaseDate;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.poster = (ImageView) itemView.findViewById(R.id.LinearRecyclerViewItemPoster);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.LinearRecyclerViewItemProgressBar);
            this.title = (TextView) itemView.findViewById(R.id.LinearRecyclerViewItemTitle);
            this.releaseDate = (TextView) itemView.findViewById(R.id.LinearRecyclerViewItemReleaseDate);
        }

    }

    @Override
    public MovieViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.linear_recycler_view_item, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(MovieViewHolder movieViewHolder, final int position) {
        final MovieRealmEntity movieRealmEntity = realmResults.get(position);
        movieViewHolder.title.setText(movieRealmEntity.getTitle());
        movieViewHolder.releaseDate.setText(movieRealmEntity.getReleaseDate());
        setPoster(movieViewHolder, movieRealmEntity.getFullPosterPath());

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieInfoActivity.class);
                intent.putExtra("id", realmResults.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    public void setPoster(final MovieViewHolder holder, String url) {
        if (!(url == null))
            Glide.with(context)
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(holder.poster);
        else {
            holder.poster.setImageResource(R.drawable.no_image);
            //Glide.with(MyApplication.context).load(R.drawable.no_image).into(movieViewHolder.poster);
            //movieViewHolder.poster.setImageDrawable(ContextCompat.getDrawable(MyApplication.context,R.drawable.no_poster_w600_and_h900));
            //movieViewHolder.poster.setImageResource(R.drawable.ic_block_black_24dp);
        }
    }
}
