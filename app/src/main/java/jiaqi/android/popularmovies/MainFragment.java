package jiaqi.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static jiaqi.android.popularmovies.TMDbFetcher.MovieListType.MOST_POPULAR;
import static jiaqi.android.popularmovies.TMDbFetcher.MovieListType.TOP_RATED;

/**
 * Created by Jiaqi on 5/3/2016.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    public static final String KEY_MOVIE_LIST = "key_movie_list";
    public static final String KEY_ITEM_POSITION = "key_item_position";

    private RecyclerView mRvMovieList;
    private ArrayList<Movie> mMovies;

    private TMDbFetcher.MovieListType mCurrentListType = MOST_POPULAR;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new LoadMovieList().execute(MOST_POPULAR);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_most_popular:
                if (mCurrentListType == MOST_POPULAR) {
                    // do nothing, do not have to update view
                } else {
                    // update recycler view
                    new LoadMovieList().execute(MOST_POPULAR);
                    mCurrentListType = MOST_POPULAR;
                }
                return true;
            case R.id.menu_item_top_rated:
                if (mCurrentListType == TOP_RATED) {
                    // do nothing, do not have to update view
                } else {
                    // update recycler view
                    new LoadMovieList().execute(TOP_RATED);
                    mCurrentListType = TOP_RATED;
                }
                return true;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRvMovieList = (RecyclerView) view.findViewById(R.id.rv_movies_list);
        mRvMovieList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return view;
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_movie_list_item);
        }

        public void loadImage(int position) {
            String posterPath = mMovies.get(position).getPath();
            String fullPath = TMDbFetcher.POSTER_BASE_URL_STRING + TMDbFetcher.POSTER_SIZE_W185 + posterPath;
            //Log.d(TAG, fullPath);

            Picasso.with(getActivity()).load(fullPath).into(mImageView);
            addOnClickListener(position);
        }

        public void addOnClickListener(final int position) {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "You clicked image view # " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class MovieListAdapter extends RecyclerView.Adapter<ImageHolder> {

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.movie_list_photo_item, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageHolder imageHolder, int position) {
            imageHolder.loadImage(position);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

    }

    class LoadMovieList extends AsyncTask<TMDbFetcher.MovieListType, Void, Void> {

        @Override
        protected Void doInBackground(TMDbFetcher.MovieListType... params) {
            mMovies = null;
            switch (params[0]) {
                case TOP_RATED:
                    mMovies = new TMDbFetcher().getTopRatedList();
                    break;
                case MOST_POPULAR:
                    mMovies = new TMDbFetcher().getMostPopularList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mMovies != null) {
                mRvMovieList.setAdapter(new MovieListAdapter());
                for (Movie movie : mMovies) {
                    Log.d("list", movie.getName());
                    Log.d("path", movie.getPath());
                }
            }
        }
    }
}
