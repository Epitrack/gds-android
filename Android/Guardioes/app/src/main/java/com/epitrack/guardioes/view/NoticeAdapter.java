package com.epitrack.guardioes.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private final NoticeListener listener;

    private ViewHolder viewHolder;

    private List<Notice> noticeList = new ArrayList<>();

    public NoticeAdapter(final NoticeListener listener, final List<Notice> noticeList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.noticeList = noticeList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_title)
        TextView textViewTitle;

        @Bind(R.id.text_view_source)
        TextView textViewSource;

        @Bind(R.id.text_view_date)
        TextView textViewDate;

        @Bind(R.id.image_view_image)
        ImageView imageViewImage;

        public ViewHolder(final View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        
        final View view = LayoutInflater.from(viewGroup.getContext())
                                        .inflate(R.layout.notice_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoticeAdapter.ViewHolder viewHolder, final int position) {

        final Notice notice = noticeList.get(position);

        viewHolder.textViewTitle.setText(notice.getTitle());
        viewHolder.textViewSource.setText(notice.getSource());
        viewHolder.textViewDate.setText(notice.getPublicationDate());
        viewHolder.imageViewImage.setImageResource(R.drawable.stub1);

        viewHolder.imageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoticeSelect(notice);
            }
        });

        viewHolder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoticeSelect(notice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
