package com.epitrack.guardioes.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 * @author Miqueias Lopes
 */
public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOTICE = 1;

    private final NoticeListener listener;

    private List<Notice> noticeList = new ArrayList<>();

    public NoticeAdapter(final NoticeListener listener, final List<Notice> noticeList) {
        this.listener = listener;
        this.noticeList = noticeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View view) {
            super(view);
        }
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_view_date)
        TextView textViewDate;

        @Bind(R.id.text_view_notice)
        TextView textViewTitle;

        @Bind(R.id.text_view_like)
        TextView textViewLike;

        @Bind(R.id.text_view_hour)
        TextView textViewHour;

        public NoticeViewHolder(final View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        if (viewType == TYPE_HEADER) {

            final View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.notice_header, viewGroup, false);

            return new ViewHolder(view);
        }
        
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notice_item, viewGroup, false);

        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NoticeViewHolder) {

            final NoticeViewHolder viewHolder = (NoticeViewHolder) holder;

            final Notice notice = noticeList.get(position);

            viewHolder.textViewTitle.setText(notice.getTitle());
            viewHolder.textViewHour.setText(notice.getClock());
            viewHolder.textViewLike.setText(notice.getLike());
            viewHolder.textViewDate.setText(notice.getPublicationDate());

            viewHolder.textViewTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {

                    listener.onNoticeSelect(notice);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return position == 0 ? TYPE_HEADER : TYPE_NOTICE;
    }
}
