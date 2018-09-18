package mac.jacwang.aurora20150610.Comm;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int space,top,right,bottom,left;
    private int spanCount;
    private int lastItemInFirstLane = -1;
    public DividerItemDecoration(int space) {
        this(space, 1);
    }

    public DividerItemDecoration(int top,int right, int bottom, int left) {
        this(top,right,bottom,left, 1);
    }

    /**
     * @param space
     * @param spanCount spans count of one lane
     */
    public DividerItemDecoration(int space, int spanCount) {
        //this.space = space;
        this.top=this.right=this.bottom=this.left=space;
        this.spanCount = spanCount;
    }
    public DividerItemDecoration(int top,int right, int bottom, int left, int spanCount) {
        //this.space = space;
        this.top=top;
        this.right=right;
        this.bottom=bottom;
        this.left=left;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int position = params.getViewPosition();
        final int spanSize;
        final int index;
        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
            spanSize = gridParams.getSpanSize();
            index = gridParams.getSpanIndex();
        }else{
            spanSize = 1;
            index = position % spanCount;
        }
        // invalid value
        if (spanSize < 1 || index < 0) return;

        if (spanSize == spanCount) { // full span
//            outRect.left = space;
//            outRect.right = space;
            outRect.left = left;
            outRect.right = right;
        } else {
            if (index == 0) {  // left one
//                outRect.left = space;
                outRect.left = left;
            }
            // spanCount >= 1
            if (index == spanCount - 1) { // right one
//                outRect.right = space;
                outRect.right = right;
            }
            if (outRect.left == 0) {
//                outRect.left = space / 2;
                outRect.left = left/2;
            }
            if (outRect.right == 0) {
//                outRect.right = space / 2;
                outRect.right = right/2;
            }
        }
        // set top to all in first lane
//        if (position < spanCount && spanSize <= spanCount) {
//            if (lastItemInFirstLane < 0) { // lay out at first time
//                lastItemInFirstLane = position + spanSize == spanCount ? position : lastItemInFirstLane;
////                outRect.top = space;
//                outRect.top = top;
//            } else if (position <= lastItemInFirstLane) { // scroll to first lane again
////                outRect.top = space;
//                outRect.top = top;
//            }
//        }
        outRect.top = top;
//        outRect.bottom = space;
        outRect.bottom = bottom;

    }
}