package com.tekken.loadmoresample;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Model> ModelList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private int selectedPosition;
    private int firstVisiblePosition;
    private boolean firstTime = true;

    public DataAdapter(List<Model> ModelList, RecyclerView recyclerView) {
        this.ModelList = ModelList;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // For first time
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
            selectedPosition = (firstVisiblePosition + lastVisibleItem)/2;
            setSelected(selectedPosition);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                    selectedPosition = (firstVisiblePosition + lastVisibleItem)/2;
                    if(firstTime) {
                        setSelected(selectedPosition);
                        firstTime = false;
                    } else {
                        setSelected();
                    }
                    setSelected(selectedPosition);
                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if(viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            viewHolder = new ModelViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ModelViewHolder) {
            ((ModelViewHolder) viewHolder).Model = ModelList.get(position);
            ((ModelViewHolder) viewHolder).bindModel(ModelList.get(position));
            if(selectedPosition == position) {
                ((ModelViewHolder) viewHolder).root.setCardBackgroundColor(((ModelViewHolder) viewHolder).tvName.getContext().getResources().getColor(android.R.color.holo_red_light));
            } else {
                ((ModelViewHolder) viewHolder).root.setCardBackgroundColor(((ModelViewHolder) viewHolder).tvName.getContext().getResources().getColor(android.R.color.white));
            }
//            notifyItemChanged(selectedPosition);

        } else {
            ((ProgressViewHolder)viewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ModelList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded(){
        loading = false;
    }

    public void setSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyItemChanged(selectedPosition);
    }

    public void setSelected() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ModelList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {

        public CardView root;
        public TextView tvName;
        public TextView tvEmailId;

        public Model Model;

        public ModelViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.root);
            tvName = view.findViewById(R.id.tvName);
            tvEmailId = view.findViewById(R.id.tvEmailId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "OnCLick : " + Model.getName() + "\n" + Model.getid(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindModel(Model Model){
            tvName.setText(Model.getName());
            tvEmailId.setText(Model.getid());

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        ProgressViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }
}