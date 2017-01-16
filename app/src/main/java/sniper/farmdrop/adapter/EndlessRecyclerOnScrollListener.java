package sniper.farmdrop.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sniper on 01/16/17.
 * This code is Solution for Endless Recycler View Adapter
 * Load more pages with items
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading;
    private int stepsBeforeNextLoad = 3; // The steps before the latest list item. When this step has been reached "load more" will know to start
    int lastVisibleItem, totalItemCount;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public abstract void onLoadMore(int current_page);

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //if we scroll back just break
        if(dy <= 0) return;
        totalItemCount = mLinearLayoutManager.getItemCount();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        synchronized (this) {
            //condition to load more items (Load more if list has been reached stepsBeforeNextLoad items or less before the end of all items in the list)
            boolean needLoadingMore = lastVisibleItem + stepsBeforeNextLoad >= totalItemCount;
            //current page will be 0 only if list reach its end or some error happen and we don't have any data
            if (currentPage > 0 && !loading && needLoadingMore) {
                // List end has been reached, Do something
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
        }
    }
    /**
     * Call this method to reset the endless adapter. After that pages will start from 1.
     */
    public void reset(boolean loading) {
        this.loading = loading;
        currentPage = 1;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    public void setCurrentPage(int page) { this.currentPage = page; }
    public int getCurrentPage(){
        return currentPage;
    }
    public void setLayoutManager(LinearLayoutManager layoutManager){
        this.mLinearLayoutManager = layoutManager;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return mLinearLayoutManager;
    }

    /**
     * Set this steps to tell the listener when to start load more
     * @param steps - usually 2, 3 or 4 should do the job (by default is 3)
     */
    public void setStepsBeforeNextLoad(int steps) {
        this.stepsBeforeNextLoad = steps;
    }
}
