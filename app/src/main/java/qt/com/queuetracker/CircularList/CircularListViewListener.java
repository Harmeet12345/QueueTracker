package qt.com.queuetracker.CircularList;

public interface CircularListViewListener {
    void onCircularLayoutFinished(CircularListView circularListView,
                                  int firstVisibleItem,
                                  int visibleItemCount,
                                  int totalItemCount);
}
