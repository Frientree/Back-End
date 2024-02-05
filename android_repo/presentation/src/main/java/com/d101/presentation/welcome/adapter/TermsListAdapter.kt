package com.d101.presentation.welcome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.d101.presentation.databinding.ItemTermsAgreeBinding
import com.d101.presentation.model.TermsItem

class TermsListAdapter() : ListAdapter<TermsItem, TermsListAdapter.TermsViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TermsViewHolder {
        val binding = ItemTermsAgreeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return TermsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TermsViewHolder(private val binding: ItemTermsAgreeBinding) : ViewHolder(binding.root) {
        fun bind(termsItem: TermsItem) {
            binding.termsItem = termsItem
        }
    }

    companion object {
        val diffUtil = object : ItemCallback<TermsItem>() {
            override fun areItemsTheSame(oldItem: TermsItem, newItem: TermsItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: TermsItem, newItem: TermsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
