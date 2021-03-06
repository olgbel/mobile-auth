package com.example.mobile_auth.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.R
import com.example.mobile_auth.Repository
import kotlinx.android.synthetic.main.item_load_more.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class FooterViewHolder(
    private val coroutineScope: CoroutineScope,
    val adapter: PostAdapter, view: View
) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            loadMoreBtn.setOnClickListener {
                loadMoreBtn.isEnabled = false
                progressbar.visibility = View.VISIBLE
                coroutineScope.launch(Dispatchers.Main) {
                    val response =
                        if (adapter.list.isEmpty()) {
                            Repository.getRecentPosts()
                        } else {
                            Repository.getPostsBefore(adapter.list[adapter.list.size - 1].id)
                        }

                    progressbar.visibility = View.INVISIBLE
                    loadMoreBtn.isEnabled = true

                    when {
                        requireNotNull(response.body()?.isEmpty()) -> context.toast(R.string.all_posts)
                        response.isSuccessful -> {
                            val newItems = requireNotNull(response.body())
                            adapter.list.addAll(adapter.list.size, newItems)
                            adapter.notifyItemRangeInserted(adapter.list.size, newItems.size)
                        }
                        else -> context.toast(R.string.error_occured)
                    }
                }
            }
        }
    }
}