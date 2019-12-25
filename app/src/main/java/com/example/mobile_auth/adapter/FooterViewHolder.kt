package com.example.mobile_auth.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_auth.Repository
import kotlinx.android.synthetic.main.item_load_more.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class FooterViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            loadMoreBtn.setOnClickListener {
                loadMoreBtn.isEnabled = false
                progressbar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val response = Repository.getPostsBefore(adapter.list[0].id)
                    progressbar.visibility = View.INVISIBLE
                    loadMoreBtn.isEnabled = true

                    if (response.body()?.isEmpty()!!) {
                        context.toast("There is all posts")
                    }
                    if (response.isSuccessful) {
                        val newItems = response.body()!!
                        adapter.list.addAll(0, newItems)
                        adapter.notifyItemRangeInserted(0, newItems.size)
                    } else {
                        context.toast("Error occured")
                    }
                }
            }
        }
    }
}