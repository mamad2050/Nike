package com.example.nikestore.feature.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Comment

class CommentAdapter(val showAll: Boolean = false) : RecyclerView.Adapter<CommentAdapter.Holder>() {

    var comments = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindComment(comments[position])
    }

    override fun getItemCount() = if (comments.size > 4 && !showAll) 4 else comments.size


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTv = itemView.findViewById<TextView>(R.id.commentTitleTv)
        private val dateTv = itemView.findViewById<TextView>(R.id.commentDateTv)
        private val authorTv = itemView.findViewById<TextView>(R.id.commentAuthorTv)
        private val contentTv = itemView.findViewById<TextView>(R.id.commentContent)
        private val divider = itemView.findViewById<View>(R.id.commentDivider)

        fun bindComment(comment: Comment) {

            titleTv.text = comment.title
            dateTv.text = comment.date
            contentTv.text = comment.content
            authorTv.text = comment.author.email


        }

    }


}